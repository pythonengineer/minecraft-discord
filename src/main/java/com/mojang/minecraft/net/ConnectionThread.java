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
	private Minecraft mc;
	private Client networkClient;

	ConnectionThread(Client nc, String ip, int port, String name, String mppass, Minecraft minecraft) {
		this.networkClient = nc;
		this.ip = ip;
		this.port = port;
		this.username = name;
		this.mpPass = mppass;
		this.mc = minecraft;
	}

	public final void run() {
		Client client1;
		boolean z2;
		try {
			Client client10000 = this.networkClient;
			SocketConnection socketConnection4 = new SocketConnection(this.ip, this.port);
			client10000.serverConnection = socketConnection4;
			client1 = this.networkClient;
			Client client5 = this.networkClient;
			SocketConnection socketConnection10001 = this.networkClient.serverConnection;
			this.networkClient.serverConnection.client = client5;
			client1 = this.networkClient;
			this.networkClient.serverConnection.sendPacket(Packet.LOGIN, new Object[]{(byte)6, this.username, this.mpPass, 0});
			z2 = true;
			client1 = this.networkClient;
			this.networkClient.processData = z2;
		} catch (IOException iOException3) {
			this.mc.hideScreen = false;
			this.mc.networkClient = null;
			this.mc.setScreen(new ErrorScreen("Failed to connect", "You failed to connect to the server. It\'s probably down!"));
			z2 = false;
			client1 = this.networkClient;
			this.networkClient.processData = z2;
		}
	}
}