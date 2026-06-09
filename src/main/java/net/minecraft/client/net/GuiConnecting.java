package net.minecraft.client.net;

import net.minecraft.client.GuiMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiConnecting extends GuiScreen {
    private NetClientHandler clientHandler;

    public GuiConnecting(Minecraft minecraft1, String string2, int i3) {
        (new ThreadConnectToServer(this, minecraft1, string2, i3)).start();
    }

    public void updateScreen() {
        if(this.clientHandler != null) {
            this.clientHandler.processReadPackets();
        }

    }

    protected void keyTyped(char c1, int i2) {
    }

    public void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
    }

    protected void actionPerformed(GuiButton guiButton1) {
        if(guiButton1.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }

    }

    public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
        this.drawDefaultBackground();
        if(this.clientHandler == null) {
            this.drawCenteredString(this.fontRenderer, "Connecting to the server...", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
            this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRenderer, "Logging in...", this.width / 2, this.height / 2 - 50, 0xFFFFFF);
            this.drawCenteredString(this.fontRenderer, this.clientHandler.loginProgress, this.width / 2, this.height / 2 - 10, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, renderPartialTick);
    }

    static NetClientHandler setNetClientHandler(GuiConnecting guiConnecting0, NetClientHandler netClientHandler1) {
        return guiConnecting0.clientHandler = netClientHandler1;
    }

    static NetClientHandler getNetClientHandler(GuiConnecting guiConnecting0) {
        return guiConnecting0.clientHandler;
    }
}
