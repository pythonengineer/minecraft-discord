package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;

import java.io.IOException;

final class ConnectionThread extends Thread {
    private String ip;
    private int port;
    private String username;
    private String mpPass;
    private Minecraft minecraft;
    private ConnectionManager connectionManager;

    ConnectionThread(ConnectionManager connectionManager1, String string2, int i3, String string4, String string5, Minecraft minecraft6) {
        this.connectionManager = connectionManager1;
        this.ip = string2;
        this.port = i3;
        this.username = string4;
        this.mpPass = string5;
        this.minecraft = minecraft6;
    }

    public final void run() {
        try {
            ConnectionManager connectionManager10000 = this.connectionManager;
            SocketConnection socketConnection2 = new SocketConnection(this.ip, this.port);
            connectionManager10000.connection = socketConnection2;
            ConnectionManager connectionManager1 = this.connectionManager;
            ConnectionManager connectionManager4 = this.connectionManager;
            SocketConnection socketConnection10001 = this.connectionManager.connection;
            this.connectionManager.connection.manager = connectionManager4;
            connectionManager1 = this.connectionManager;
            this.connectionManager.connection.sendPacket(Packet.LOGIN, new Object[]{(byte)4, this.username, this.mpPass});
            boolean z5 = true;
            connectionManager1 = this.connectionManager;
            this.connectionManager.processData = true;
        } catch (IOException iOException3) {
            this.minecraft.hideGui = false;
            this.minecraft.connectionManager = null;
            this.minecraft.setScreen(new ErrorScreen("Failed to connect", "You failed to connect to the server. It\'s probably down!"));
        }
    }
}
