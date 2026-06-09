package net.minecraft.client.net;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConnectFailed;

class ThreadConnectToServer extends Thread {
    final Minecraft mc;
    final String ip;
    final int port;
    final GuiConnecting connectingGui;

    ThreadConnectToServer(GuiConnecting guiConnecting, Minecraft minecraft, String ip, int port) {
        this.connectingGui = guiConnecting;
        this.mc = minecraft;
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(this.mc, this.ip, this.port));
            GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet1Handshake(this.mc.session.username, this.mc.session.sessionId, 10));
        } catch (IOException exception4) {
            exception4.printStackTrace();
            this.mc.displayGuiScreen(new GuiConnectFailed("Failed to connect to the server", exception4.toString()));
        }

    }
}
