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

function end(client, target) {
    if (target != null) {
        target.end();
        target.destroy();
    }
    if (client != null) {
        client.close();
    }
}

// Handle new WebSocket client
const new_client = function (client, req) {
    const clientAddr = client._socket.remoteAddress;
    let log;
    let username;
    console.log(req ? req.url : client.upgradeReq.url);
    log = function (msg) {
        console.log(username + ': ' + msg);
    };
    console.log('WebSocket connection from: ' + clientAddr);
    let hasAuthed = false;
    const target = net.createConnection(target_port, target_host, function () {
        console.log('connected to target');
        if (onConnectedCallback) {
            try {
                onConnectedCallback(client, target);
            } catch (e) {
                console.log("onConnectedCallback failed, cleaning up target");
                end(client, target);
            }
        }
    });
    target.on('data', function (data) {
        try {
            client.send(data);
        } catch (e) {
            log(e);
            log("Client closed, cleaning up target");
            end(client, target);
        }
    });
    target.on('end', function () {
        log('target disconnected');
        end(client, target);
    });
    target.on('error', function () {
        log('target connection error');
        end(client, target);
    });

    let buffer = Buffer.alloc(0);
    client.on('message', function (msg) {
        try {
            if (hasAuthed) {
                target.write(msg);
            } else {
                let combinedBuffer = Buffer.concat([buffer, msg]);
                let offset = 0;

                while (offset < combinedBuffer.length) {
                    if (combinedBuffer.length - offset < 3) {
                        offset = 0;
                        break;
                    }

                    const packetId = combinedBuffer.readUint8(offset);
                    offset += 1;

                    if (packetId == 2) {
                        if (combinedBuffer.length - offset < 2) {
                            offset = 0;
                            break;
                        }

                        const usernameLen = combinedBuffer.readUint16BE(offset);
                        offset += 2;
                        if (combinedBuffer.length - offset < usernameLen * 2) {
                            offset = 0;
                            break;
                        }

                        offset += usernameLen * 2;
                        target.write(combinedBuffer.slice(0, offset));
                        combinedBuffer = combinedBuffer.slice(offset);
                        offset = 0;
                        continue;
                    }

                    if (combinedBuffer.length - offset < 6) {
                        offset = 0;
                        break;
                    }

                    const protocol = combinedBuffer.readInt32BE(offset);
                    offset += 4;

                    const usernameLen = combinedBuffer.readUint16BE(offset);
                    offset += 2;
                    if (combinedBuffer.length - offset < usernameLen * 2) {
                        offset = 0;
                        break;
                    }

                    username = '';
                    for (let i = 0; i < usernameLen; i++) {
                        username += String.fromCharCode(combinedBuffer.readUInt16BE(offset));
                        offset += 2;
                    }

                    let tokenOffset = offset;
                    const tokenLen = combinedBuffer.readUint16BE(offset);
                    offset += 2;
                    if (combinedBuffer.length - offset < tokenLen * 2) {
                        offset = 0;
                        break;
                    }

                    let token = '';
                    for (let i = 0; i < tokenLen; i++) {
                        token += String.fromCharCode(combinedBuffer.readUInt16BE(offset));
                        offset += 2;
                    }

                    if (packetId == 1) {
                        if (!tokens.has(token)) {
                            log('bad token auth');
                            end(client, target);
                        } else {
                            hasAuthed = true;
                            log('client auth');
                            combinedBuffer.copy(combinedBuffer, tokenOffset, tokenOffset + 2 + (tokenLen * 2));
                            combinedBuffer = combinedBuffer.slice(0, combinedBuffer.length - (2 + (tokenLen * 2)));
                            target.write(combinedBuffer);
                            break;
                        }
                    } else {
                        log('sent packet ' + packetId + ' before login');
                        end(client, target);
                    }
                }

                buffer = combinedBuffer.slice(offset);
            }
        } catch (e) {
            log('client auth error: ' + e);
            end(client, target);
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
        end(client, target);
    });
    client.on('error', function (a) {
        log('WebSocket client error: ' + a);
        end(client, target);
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
