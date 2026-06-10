package net.minecraft.client.gui;

import net.minecraft.client.net.GuiConnecting;

public class GuiMultiplayer extends GuiScreen {
    private GuiScreen parentScreen;
    private int updateCounter = 0;
    private String ipText = "";
    private String ipServer = "";

    public GuiMultiplayer(GuiScreen guiScreen1) {
        this.parentScreen = guiScreen1;
    }

    public void updateScreen() {
        ++this.updateCounter;
    }

    public void initGui() {
        this.ipServer = "OK:" + this.mc.serverName + ":" + this.mc.serverPort;
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Connect"));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
    }

    protected void actionPerformed(GuiButton guiButton1) {
        if(guiButton1.enabled) {
            if(guiButton1.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if(guiButton1.id == 0) {
                System.out.println(this.ipServer);
                String[] string2 = this.ipServer.split(":");
                if(string2[0].equals("OK")) {
                    String host = string2[1];
                    String port = string2[2];
                    if(string2[1].equals("ws")) {
                        string2 = this.ipServer.split("ws://")[1].split(":");
                        host = "ws://" + string2[0];
                        port = string2[1];
                    } else if(string2[1].equals("wss")) {
                        string2 = this.ipServer.split("wss://")[1].split(":");
                        host = "wss://" + string2[0];
                        port = string2[1];
                    }

                    this.mc.displayGuiScreen(new GuiConnecting(this.mc, host, Integer.parseInt(port)));
                }
            }

        }
    }

    protected void keyTyped(char c1, int i2) {
        if(i2 == 14 && this.ipText.length() > 0) {
            this.ipText = this.ipText.substring(0, this.ipText.length() - 1);
        }

        if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c1) >= 0 && this.ipText.length() < 16) {
            this.ipText = this.ipText + c1;
        }

        ((GuiButton)this.controlList.get(0)).enabled = this.cipherCode(this.ipText.trim());
    }

    private boolean cipherCode(String string1) {
        return true;
    }

    public void drawScreen(int i1, int i2, float f3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Play Multiplayer", this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.drawString(this.fontRenderer, "Minecraft Multiplayer is currently not finished, but there", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
        this.drawString(this.fontRenderer, "is some invite-only testing going on.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 9, 10526880);
        this.drawString(this.fontRenderer, "Public (and buggy) testing is coming in a few weeks or so.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
        this.drawString(this.fontRenderer, "If you\'ve received an invite code, enter the code below:", this.width / 2 - 140, this.height / 4 - 60 + 60 + 45, 10526880);
        int i4 = this.width / 2 - 100;
        int i5 = this.height / 4 - 10 + 50 + 18;
        short s6 = 200;
        byte b7 = 20;
        this.drawRect(i4 - 1, i5 - 1, i4 + s6 + 1, i5 + b7 + 1, -6250336);
        this.drawRect(i4, i5, i4 + s6, i5 + b7, 0xFF000000);
        String string8 = "################";
        this.drawString(this.fontRenderer, string8.substring(0, this.ipText.length()) + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), i4 + 4, i5 + (b7 - 8) / 2, 14737632);
        super.drawScreen(i1, i2, f3);
    }
}
