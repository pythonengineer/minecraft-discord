package net.minecraft.client.gui;

import net.minecraft.client.net.NetClientHandler;

public class GuiDownloadTerrain extends GuiScreen {
    private NetClientHandler netHandler;

    public GuiDownloadTerrain(NetClientHandler netClientHandler1) {
        this.netHandler = netClientHandler1;
    }

    protected void keyTyped(char c1, int i2) {
    }

    public void initGui() {
        this.controlList.clear();
    }

    public void updateScreen() {
        if(this.netHandler != null) {
            this.netHandler.processReadPackets();
        }

    }

    protected void actionPerformed(GuiButton guiButton1) {
    }

    public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
        this.drawBackground(0);
        this.drawCenteredString(this.fontRenderer, "Receiving level", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, renderPartialTick);
    }
}
