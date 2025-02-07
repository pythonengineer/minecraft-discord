package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public final class ConnectionManager {
	public ByteArrayOutputStream levelBuffer;
	public SocketConnection connection;
	public Minecraft minecraft;
	public boolean processData = false;
    public boolean connected = false;
	public HashMap players = new HashMap();

	public ConnectionManager(Minecraft minecraft1, String string2, int i3, String string4, String string5) {
		minecraft1.hideGui = true;
		this.minecraft = minecraft1;
		(new ConnectionThread(this, string2, i3, string4, string5, minecraft1)).start();
	}

	public final void sendBlockChange(int i1, int i2, int i3, int i4, int i5) {
		this.connection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{i1, i2, i3, i4, i5});
	}

	public final void disconnect(Exception exception1) {
		this.connection.disconnect();
		this.minecraft.setScreen(new ErrorScreen("Disconnected!", exception1.getMessage()));
		exception1.printStackTrace();
	}

	public final boolean isConnected() {
		SocketConnection socketConnection1;
		return this.connection != null && (socketConnection1 = this.connection).connected;
	}
}
