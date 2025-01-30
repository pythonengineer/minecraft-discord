package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public final class ConnectionManager {
    public ByteArrayOutputStream levelBuffer;
    public SocketConnection connection;
    public Minecraft minecraft;
    public HashMap players = new HashMap();

    public ConnectionManager(Minecraft minecraft1, String string2, int i3, String string4) throws IOException {
        this.connection = new SocketConnection(string2, i3);
        SocketConnection socketConnection10001 = this.connection;
        this.connection.manager = this;
        this.connection.sendPacket(Packet.LOGIN, new Object[]{(byte)3, string4, "--"});
        this.minecraft = minecraft1;
        minecraft1.beginLevelLoading("Connecting..");
        minecraft1.hideGui = true;
    }

    public final void sendBlockChange(int i1, int i2, int i3, int i4, int i5) {
        this.connection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{i1, i2, i3, i4, i5});
    }
}
