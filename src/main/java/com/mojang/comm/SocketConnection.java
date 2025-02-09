package com.mojang.comm;

import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.net.Packet;

import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public final class SocketConnection {
    public volatile boolean connected;
    private IWebSocketClient webSocket;
    public ByteBuffer readBuffer = ByteBuffer.allocate(1048576);
    public ByteBuffer writeBuffer = ByteBuffer.allocate(1048576);
    public ConnectionManager manager;
    private boolean initialized = false;
    private byte[] stringPacket = new byte[64];
    private HashMap<String,ByteArrayOutputStream> skinBuffer = new HashMap<String,ByteArrayOutputStream>();

    public SocketConnection(String string1, int i2) throws IOException {
        if (i2 != 0) {
            webSocket = PlatformNetworking.openWebSocket(string1 + ":" + i2);
        } else {
            webSocket = PlatformNetworking.openWebSocket(string1);
        }
        if (webSocket == null) {
            throw new IOException();
        }
        boolean connected = webSocket.connectBlocking(200);
        if (!connected) {
            if (webSocket != null) {
                webSocket.close();
            }
            throw new IOException();
        }
        this.connected = true;
        this.readBuffer.clear();
        this.writeBuffer.clear();
    }

    public final void disconnect() {
        try {
            if(this.writeBuffer.position() > 0) {
                this.writeBuffer.flip();
                this.webSocket.write(this.writeBuffer);
                this.writeBuffer.compact();
            }
        } catch (Exception exception2) {
        }

        this.connected = false;

        try {
            this.webSocket.close();
        } catch (Exception exception1) {
        }

        this.webSocket = null;
    }

    public final void processData() throws IOException {
        this.webSocket.read(this.readBuffer);
        int i1 = 0;

        while(this.readBuffer.position() > 0 && i1++ != 100) {
            this.readBuffer.flip();
            byte b2 = this.readBuffer.get(0);
            Packet packet3;
            if((packet3 = Packet.PACKETS[b2]) == null) {
                throw new IOException("Bad command: " + b2);
            }

            if(this.readBuffer.remaining() < packet3.size + 1) {
                this.readBuffer.compact();
                break;
            }

            this.readBuffer.get();
            Object[] object11 = new Object[packet3.fields.length];

            for(int i4 = 0; i4 < object11.length; ++i4) {
                object11[i4] = this.read(packet3.fields[i4]);
            }

            ConnectionManager connectionManager12 = this.manager;
            if(this.manager.processData) {
                if(packet3 == Packet.LOGIN) {
                    connectionManager12.minecraft.beginLevelLoading(object11[1].toString());
                    connectionManager12.minecraft.levelLoadUpdate(object11[2].toString());
                    connectionManager12.minecraft.player.userType = ((Byte)object11[3]).byteValue();
                } else if(packet3 == Packet.LEVEL_INITIALIZE) {
                    connectionManager12.minecraft.setLevel((Level)null);
                    connectionManager12.levelBuffer = new ByteArrayOutputStream();
                } else {
                    byte b6;
                    if(packet3 == Packet.LEVEL_DATA_CHUNK) {
                        short s13 = ((Short)object11[0]).shortValue();
                        byte[] b5 = (byte[])((byte[])object11[1]);
                        b6 = ((Byte)object11[2]).byteValue();
                        connectionManager12.minecraft.setLoadingProgress(b6);
                        connectionManager12.levelBuffer.write(b5, 0, s13);
                    } else {
                        short s17;
                        short s18;
                        short s21;
                        if(packet3 == Packet.LEVEL_FINALIZE) {
                            try {
                                connectionManager12.levelBuffer.close();
                            } catch (IOException iOException10) {
                                iOException10.printStackTrace();
                            }

                            byte[] b14 = LevelIO.loadBlocks(new ByteArrayInputStream(connectionManager12.levelBuffer.toByteArray()));
                            connectionManager12.levelBuffer = null;
                            s18 = ((Short)object11[0]).shortValue();
                            s21 = ((Short)object11[1]).shortValue();
                            s17 = ((Short)object11[2]).shortValue();
                            Level level7;
                            (level7 = new Level()).setNetworkMode(true);
                            level7.setData(s18, s21, s17, b14);
                            connectionManager12.minecraft.setLevel(level7);
                            connectionManager12.minecraft.hideGui = false;
                            connectionManager12.connected = true;
                        } else if(packet3 == Packet.SET_TILE) {
                            if(connectionManager12.minecraft.level != null) {
                                connectionManager12.minecraft.level.netSetTile(((Short)object11[0]).shortValue(), ((Short)object11[1]).shortValue(), ((Short)object11[2]).shortValue(), ((Byte)object11[3]).byteValue());
                            }
                        } else {
                            byte b8;
                            byte b15;
                            byte b10001;
                            short s10003;
                            String string19;
                            short s10004;
                            NetworkPlayer networkPlayer20;
                            if(packet3 == Packet.PLAYER_JOIN) {
                                b10001 = ((Byte)object11[0]).byteValue();
                                String string10002 = (String)object11[1];
                                s10003 = ((Short)object11[2]).shortValue();
                                s10004 = ((Short)object11[3]).shortValue();
                                short s10005 = ((Short)object11[4]).shortValue();
                                byte b10006 = ((Byte)object11[5]).byteValue();
                                byte b9 = ((Byte)object11[6]).byteValue();
                                b8 = b10006;
                                short s24 = s10005;
                                s21 = s10004;
                                s18 = s10003;
                                string19 = string10002;
                                b15 = b10001;
                                if(b15 >= 0) {
                                    networkPlayer20 = new NetworkPlayer(connectionManager12.minecraft, b15, string19, s18, s21, s24, (float)(-b8 * 360) / 256.0F, (float)(b9 * 360) / 256.0F);
                                    connectionManager12.players.put(b15, networkPlayer20);
                                    connectionManager12.minecraft.level.entities.add(networkPlayer20);
                                } else {
                                    connectionManager12.minecraft.level.setSpawnPos(s18 / 32, s21 / 32, s24 / 32, (float)(b8 * 320 / 256));
                                    connectionManager12.minecraft.player.moveTo((float)s18 / 32.0F, (float)s21 / 32.0F, (float)s24 / 32.0F, (float)(b8 * 360) / 256.0F, (float)(b9 * 360) / 256.0F);
                                }
                            } else {
                                byte b25;
                                NetworkPlayer networkPlayer28;
                                byte b33;
                                if(packet3 == Packet.PLAYER_TELEPORT) {
                                    b10001 = ((Byte)object11[0]).byteValue();
                                    short s29 = ((Short)object11[1]).shortValue();
                                    s10003 = ((Short)object11[2]).shortValue();
                                    s10004 = ((Short)object11[3]).shortValue();
                                    b33 = ((Byte)object11[4]).byteValue();
                                    b8 = ((Byte)object11[5]).byteValue();
                                    b25 = b33;
                                    s21 = s10004;
                                    s18 = s10003;
                                    s17 = s29;
                                    b15 = b10001;
                                    if(b15 < 0) {
                                        connectionManager12.minecraft.player.moveTo((float)s17 / 32.0F, (float)s18 / 32.0F, (float)s21 / 32.0F, (float)(b25 * 360) / 256.0F, (float)(b8 * 360) / 256.0F);
                                    } else if((networkPlayer28 = (NetworkPlayer)connectionManager12.players.get(b15)) != null) {
                                        networkPlayer28.teleport(s17, s18, s21, (float)(-b25 * 360) / 256.0F, (float)(b8 * 360) / 256.0F);
                                    }
                                } else {
                                    byte b22;
                                    byte b23;
                                    byte b30;
                                    byte b31;
                                    if(packet3 == Packet.PLAYER_MOVE_AND_ROTATE) {
                                        b10001 = ((Byte)object11[0]).byteValue();
                                        b30 = ((Byte)object11[1]).byteValue();
                                        b31 = ((Byte)object11[2]).byteValue();
                                        byte b32 = ((Byte)object11[3]).byteValue();
                                        b33 = ((Byte)object11[4]).byteValue();
                                        b8 = ((Byte)object11[5]).byteValue();
                                        b25 = b33;
                                        b6 = b32;
                                        b22 = b31;
                                        b23 = b30;
                                        b15 = b10001;
                                        if(b15 >= 0 && (networkPlayer28 = (NetworkPlayer)connectionManager12.players.get(b15)) != null) {
                                            networkPlayer28.queue(b23, b22, b6, (float)(-b25 * 360) / 256.0F, (float)(b8 * 360) / 256.0F);
                                        }
                                    } else if(packet3 == Packet.PLAYER_ROTATE) {
                                        b10001 = ((Byte)object11[0]).byteValue();
                                        b30 = ((Byte)object11[1]).byteValue();
                                        b22 = ((Byte)object11[2]).byteValue();
                                        b23 = b30;
                                        b15 = b10001;
                                        NetworkPlayer networkPlayer26;
                                        if(b15 >= 0 && (networkPlayer26 = (NetworkPlayer)connectionManager12.players.get(b15)) != null) {
                                            networkPlayer26.queue((float)(-b23 * 360) / 256.0F, (float)(b22 * 360) / 256.0F);
                                        }
                                    } else if(packet3 == Packet.PLAYER_MOVE) {
                                        b10001 = ((Byte)object11[0]).byteValue();
                                        b30 = ((Byte)object11[1]).byteValue();
                                        b31 = ((Byte)object11[2]).byteValue();
                                        b6 = ((Byte)object11[3]).byteValue();
                                        b22 = b31;
                                        b23 = b30;
                                        b15 = b10001;
                                        NetworkPlayer networkPlayer27;
                                        if(b15 >= 0 && (networkPlayer27 = (NetworkPlayer)connectionManager12.players.get(b15)) != null) {
                                            networkPlayer27.queue(b23, b22, b6);
                                        }
                                    } else if(packet3 == Packet.PLAYER_DISCONNECT) {
                                        b15 = ((Byte)object11[0]).byteValue();
                                        if(b15 >= 0 && (networkPlayer20 = (NetworkPlayer)connectionManager12.players.remove(b15)) != null) {
                                            networkPlayer20.clear();
                                            connectionManager12.minecraft.level.entities.remove(networkPlayer20);
                                        }
                                    } else if(packet3 == Packet.CHAT_MESSAGE) {
                                        b10001 = ((Byte)object11[0]).byteValue();
                                        string19 = (String)object11[1];
                                        b15 = b10001;
                                        if(b15 < 0) {
                                            connectionManager12.minecraft.addChatMessage("&e" + string19);
                                        } else {
                                            connectionManager12.players.get(b15);
                                            connectionManager12.minecraft.addChatMessage(string19);
                                        }
                                    } else if(packet3 == Packet.KICK_PLAYER) {
                                        connectionManager12.minecraft.setScreen(new ErrorScreen("Connection lost", (String)object11[0]));
                                    } else if(packet3 == Packet.PLAYER_SKIN) {
                                        String name = (String)object11[0];
                                        if(!this.skinBuffer.containsKey(name)) {
                                            this.skinBuffer.put(name, new ByteArrayOutputStream());
                                        }
                                        short len = ((Short)object11[1]).shortValue();
                                        byte[] bytes = (byte[])((byte[])object11[2]);
                                        ByteArrayOutputStream os = this.skinBuffer.get(name);
                                        os.write(bytes, 0, bytes.length);
                                        byte[] texBytes = os.toByteArray();
                                        if(texBytes.length == len) {
                                            NetworkPlayer player = null;
                                            Iterator<NetworkPlayer> itr = connectionManager12.players.values().iterator();
                                            while (itr.hasNext()) {
                                                NetworkPlayer p = itr.next();
                                                if (p.name.equals(name)) {
                                                    player = p;
                                                    break;
                                                }
                                            }
                                            if (player != null) {
                                                this.skinBuffer.remove(name);
                                                int[] skin = new int[len / 4];
                                                for (int i = 0; i < skin.length; ++i)
                                                {
                                                    int r = texBytes[i * 4 + 0] & 0xFF;
                                                    int g = texBytes[i * 4 + 1] & 0xFF;
                                                    int b = texBytes[i * 4 + 2] & 0xFF;
                                                    int a = texBytes[i * 4 + 3] & 0xFF;
                                                    int color = (a << 24) | (r << 16) | (g << 8) | b;
                                                    skin[i] = (color & 0xFF00FF00) | ((color & 0x00FF0000) >>> 16) | ((color & 0x000000FF) << 16);
                                                }
                                                ImageData tex = new ImageData(64, 64, skin, true).getSubImage(0, 0, 64, 32);
                                                player.newTexture = tex;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(!this.connected) {
                break;
            }

            this.readBuffer.compact();
        }

        if(this.writeBuffer.position() > 0) {
            this.writeBuffer.flip();
            this.webSocket.write(this.writeBuffer);
            this.writeBuffer.compact();
        }

    }

    public final void sendPacket(Packet packet1, Object... object2) {
        if(this.connected) {
            this.writeBuffer.put(packet1.id);

            for(int i3 = 0; i3 < object2.length; ++i3) {
                Class class10001 = packet1.fields[i3];
                Object object6 = object2[i3];
                Class class5 = class10001;
                SocketConnection socketConnection4 = this;
                if(this.connected) {
                    try {
                        if(class5 == Long.TYPE) {
                            socketConnection4.writeBuffer.putLong(((Long)object6).longValue());
                        } else if(class5 == Integer.TYPE) {
                            socketConnection4.writeBuffer.putInt(((Number)object6).intValue());
                        } else if(class5 == Short.TYPE) {
                            socketConnection4.writeBuffer.putShort(((Number)object6).shortValue());
                        } else if(class5 == Byte.TYPE) {
                            socketConnection4.writeBuffer.put(((Number)object6).byteValue());
                        } else if(class5 == Double.TYPE) {
                            socketConnection4.writeBuffer.putDouble(((Double)object6).doubleValue());
                        } else if(class5 == Float.TYPE) {
                            socketConnection4.writeBuffer.putFloat(((Float)object6).floatValue());
                        } else {
                            byte[] b8;
                            if(class5 != String.class) {
                                if(class5 == byte[].class) {
                                    if((b8 = (byte[])((byte[])object6)).length < 1024) {
                                        b8 = Arrays.copyOf(b8, 1024);
                                    }

                                    socketConnection4.writeBuffer.put(b8);
                                }
                            } else {
                                b8 = ((String)object6).getBytes("UTF-8");
                                Arrays.fill(socketConnection4.stringPacket, (byte)32);

                                int i9;
                                for(i9 = 0; i9 < 64 && i9 < b8.length; ++i9) {
                                    socketConnection4.stringPacket[i9] = b8[i9];
                                }

                                for(i9 = b8.length; i9 < 64; ++i9) {
                                    socketConnection4.stringPacket[i9] = 32;
                                }

                                socketConnection4.writeBuffer.put(socketConnection4.stringPacket);
                            }
                        }
                    } catch (Exception exception7) {
                        this.manager.disconnect(exception7);
                    }
                }
            }

        }
    }

    public Object read(Class class1) {
        if(!this.connected) {
            return null;
        } else {
            try {
                if(class1 == Long.TYPE) {
                    return this.readBuffer.getLong();
                } else if(class1 == Integer.TYPE) {
                    return this.readBuffer.getInt();
                } else if(class1 == Short.TYPE) {
                    return this.readBuffer.getShort();
                } else if(class1 == Byte.TYPE) {
                    return this.readBuffer.get();
                } else if(class1 == Double.TYPE) {
                    return this.readBuffer.getDouble();
                } else if(class1 == Float.TYPE) {
                    return this.readBuffer.getFloat();
                } else if(class1 == String.class) {
                    this.readBuffer.get(this.stringPacket);
                    return (new String(this.stringPacket, "UTF-8")).trim();
                } else if(class1 == byte[].class) {
                    byte[] b3 = new byte[1024];
                    this.readBuffer.get(b3);
                    return b3;
                } else {
                    return null;
                }
            } catch (Exception exception2) {
                this.manager.disconnect(exception2);
                return null;
            }
        }
    }
}
