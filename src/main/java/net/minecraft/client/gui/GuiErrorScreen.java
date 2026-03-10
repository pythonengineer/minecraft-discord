package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
    private String title;
    private String text;

    public GuiErrorScreen(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public final void initGui() {
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradient(0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 0xFFFFFF);
        drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected final void keyTyped(char typedChar, int keyCode) {
    }
}