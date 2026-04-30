package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
    public final void initGui() {
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradient(0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(this.fontRenderer, (String)null, this.width / 2, 90, 0xFFFFFF);
        drawCenteredString(this.fontRenderer, (String)null, this.width / 2, 110, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected final void keyTyped(char typedChar, int keyCode) {
    }
}