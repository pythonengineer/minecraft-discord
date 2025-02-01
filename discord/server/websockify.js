// https://github.com/saschazesiger/websockify-js-express/blob/main/websockify.js
'use strict';

import express from "express";
import net from "net";
import url from "url";
import path from "path";
import fs from "fs";
import mime from "mime";
import { WebSocketServer } from "ws";

let webServer, wsServer, source_host, source_port, target_host, target_port, argv = null, onConnectedCallback = null, onDisconnectedCallback = null;

const app = express();

let tokens = new Set();

export function newClientToken(token) {
    tokens.add(token);
}

// Handle new WebSocket client
const new_client = function (client, req) {
    const clientAddr = client._socket.remoteAddress;
    let log;
    console.log(req ? req.url : client.upgradeReq.url);
    log = function (msg) {
        console.log(' ' + clientAddr + ': ' + msg);
    };
    log('WebSocket connection from: ' + clientAddr);
    let hasAuthed = false;
    const target = net.createConnection(target_port, target_host, function () {
        log('connected to target');
        if (onConnectedCallback) {
            try {
                onConnectedCallback(client, target);
            } catch (e) {
                log("onConnectedCallback failed, cleaning up target");
                target.end();
                target.destroy();
            }
        }
    });
    target.on('data', function (data) {
        try {
            client.send(data);
        } catch (e) {
            log("Client closed, cleaning up target");
            target.end();
            target.destroy();
        }
    });
    target.on('end', function () {
        log('target disconnected');
        client.close();
    });
    target.on('error', function () {
        log('target connection error');
        target.end();
        target.destroy();
        client.close();
    });

    client.on('message', function (msg) {
        try {
            if (msg.readUint8(0) == 0) { // intercept LOGIN packet for token check
                let token = msg.toString('utf8', 66, 130).trimEnd();
                if (!tokens.has(token)) {
                    log('Bad token auth');
                    target.end();
                    target.destroy();
                    client.close();
                } else {
                    log('client authed');
                    hasAuthed = true;
                }
            } else if (!hasAuthed) {
                log('Sent packets before login');
                target.end();
                target.destroy();
                client.close();
            }
            target.write(msg);
        } catch (e) {
            log('WebSocket client auth error');
            target.end();
            target.destroy();
            client.close();
        }
    });
    client.on('close', function (code, reason) {
        if (onDisconnectedCallback) {
            try {
                onDisconnectedCallback(client, code, reason);
            } catch (e) {
                log("onDisconnectedCallback failed");
            }
        }
        log('WebSocket client disconnected: ' + code + ' [' + reason + ']');
        target.end();
        target.destroy();
    });
    client.on('error', function (a) {
        log('WebSocket client error: ' + a);
        target.end();
        target.destroy();
    });
};

// Send an HTTP error response
const http_error = function (response, code, msg) {
    response.writeHead(code, { "Content-Type": "text/plain" });
    response.write(msg + "\n");
    response.end();
    return;
}

export function initWsServer() {
    source_host = "";
    source_port = parseInt("3000");
    target_host = "127.0.0.1";
    target_port = parseInt("25565");

    console.log("    - proxying from " + source_host + ":" + source_port +
        " to " + target_host + ":" + target_port);

    const app = express();

    app.get('*', (req, res) => {
        if (!argv.web) {
            return http_error(res, 403, "403 Permission Denied");
        }
        const uri = url.parse(req.url).pathname;
        let filename = path.join(argv.web, uri);
        fs.exists(filename, function (exists) {
            if (!exists) {
                return http_error(res, 404, "404 Not Found");
            }

            if (fs.statSync(filename).isDirectory()) {
                filename += '/index.html';
            }

            fs.readFile(filename, "binary", function (err, file) {
                if (err) {
                    return http_error(res, 500, err);
                }

                res.setHeader('Content-type', mime.getType(path.parse(uri).ext));
                res.writeHead(200);
                res.write(file, "binary");
                res.end();
            });
        });
    });

    webServer = app.listen(source_port, function () {
        wsServer = new WebSocketServer({ server: webServer });
        wsServer.on('connection', new_client);
    });
}
