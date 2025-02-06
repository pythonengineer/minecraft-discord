// https://github.com/saschazesiger/websockify-js-express/blob/main/websockify.js
'use strict';

import express from "express";
import net from "net";
import url from "url";
import path from "path";
import fs from "fs";
import mime from "mime";
import png from "pngjs";
import { WebSocketServer } from "ws";

let webServer, wsServer, source_host, source_port, target_host, target_port, argv = null, onConnectedCallback = null, onDisconnectedCallback = null;

const app = express();

class Packet {
    constructor(id, length) {
        this.id = id;
        this.length = length;
    }

    getId() {
        return this.id;
    }

    getLength() {
        return this.length;
    }
}

class PacketRegistry {
    static #packets = new Map();

    static register(packetClass) {
        PacketRegistry.#packets.set(packetClass.getId(), packetClass);
    }

    static getPacket(id) {
        return PacketRegistry.#packets.get(id);
    }
}

PacketRegistry.register(new Packet(0, 129));
PacketRegistry.register(new Packet(1, 0));
PacketRegistry.register(new Packet(2, 0));
PacketRegistry.register(new Packet(3, 1027));
PacketRegistry.register(new Packet(4, 6));
PacketRegistry.register(new Packet(5, 8));
PacketRegistry.register(new Packet(6, 7));
PacketRegistry.register(new Packet(7, 73));
PacketRegistry.register(new Packet(8, 9));
PacketRegistry.register(new Packet(9, 6));
PacketRegistry.register(new Packet(10, 4));
PacketRegistry.register(new Packet(11, 3));
PacketRegistry.register(new Packet(12, 1));
PacketRegistry.register(new Packet(13, 65));
PacketRegistry.register(new Packet(14, 64));
PacketRegistry.register(new Packet(15, 1090));

let tokens = new Set();

export function newClientToken(token) {
    tokens.add(token);
}

async function getSkin(username) {
    const filePath = `skincache/${username}.png`;
    let skinBuffer;
    try {
        const buffer = fs.readFileSync(filePath);
        skinBuffer = png.PNG.sync.read(buffer).data;
        console.log(`retrieved ${username} skin from cache`);
    } catch (err) {
        let url = await fetch(`https://playerdb.co/api/player/minecraft/${username}`)
            .then(response => response.json())
            .then(data => {
                return data.data.player.skin_texture
            })
            .catch((e) => {
            })
        const image = await fetch(url)
        const imageBlob = await image.blob()
        skinBuffer = await imageBlob.arrayBuffer();
        const buffer = Buffer.from(skinBuffer);
        fs.writeFile(filePath, buffer, (err) => {
            if (err) {
                console.error(`error writing ${username} skin`);
            } else {
                console.log(`caching ${username} skin`);
            }
        });
        skinBuffer = png.PNG.sync.read(buffer).data;
    }

    return new Uint8Array(skinBuffer);
}

async function handlePacket(packet, data, username) {
    try {
        if (packet == 7) {
            let name = data.toString('utf8', 2, 66).trimEnd();
            if (name != username) {
                let arr = await getSkin(name);
                let size = arr.length;
                let buffer = Buffer.alloc(0);
                while (arr.length > 0) {
                    let buf = Buffer.alloc(1091);

                    buf[0] = 15;
                    for (let i = 0; i < 64; i++) {
                        buf[i + 1] = 32;
                    }
                    for (let i = 0; i < name.length; i++) {
                        buf[i + 1] = name.charCodeAt(i);
                    }

                    let len = arr.length > 1024 ? 1024 : arr.length;
                    buf.writeInt16BE(size, 65);
                    for (let i = 0; i < len; i++) {
                        buf[i + 67] = arr[i];
                    }

                    buffer = Buffer.concat([buffer, buf]);

                    if (arr.length == len) {
                        return buffer;
                    } else {
                        arr = arr.subarray(len);
                    }
                }
            }
        }
    } catch (e) {
    }
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
    let remainingBuf = Buffer.alloc(0);
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
            data = Buffer.concat([remainingBuf, data]);
            let packet = data.readUint8(0);
            handlePacket(packet, data, username).then(result => {
                if (result != null) {
                    remainingBuf = Buffer.concat([remainingBuf, result]);
                }
            });
            let length = PacketRegistry.getPacket(packet).getLength() + 1;
            while (data.length >= length) {
                if (data.length > length) {
                    const buf = data.subarray(0, length);
                    client.send(buf);
                    data = data.subarray(length);
                    packet = data.readUint8(0);
                    handlePacket(packet, data, username).then(result => {
                        if (result != null) {
                            remainingBuf = Buffer.concat([remainingBuf, result]);
                        }
                    });
                    length = PacketRegistry.getPacket(packet).getLength() + 1;
                }
                if (data.length <= length) {
                    if (data.length < length) {
                        remainingBuf = data;
                    } else {
                        client.send(data);
                        remainingBuf = Buffer.alloc(0);
                    }
                    break
                }
            }
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

    client.on('message', function (msg) {
        try {
            if (msg.readUint8(0) == 0) { // intercept LOGIN packet for token check
                username = msg.toString('utf8', 2, 66).trimEnd();
                let token = msg.toString('utf8', 66, 130).trimEnd();
                if (!tokens.has(token)) {
                    log('bad token auth');
                    end(client, target);
                } else {
                    log('client auth');
                    hasAuthed = true;
                    target.write(msg);
                }
            } else if (!hasAuthed) {
                log('sent packet before login');
                end(client, target);
            } else {
                target.write(msg);
            }
        } catch (e) {
            log('client auth error');
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
