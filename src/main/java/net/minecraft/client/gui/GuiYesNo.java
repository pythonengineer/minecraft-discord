package net.minecraft.client.gui;

public final class GuiYesNo extends GuiScreen {
    private GuiScreen parentScreen;
    private String title;
    private String desc;
    private int worldNumber;

    public GuiYesNo(GuiScreen parentScreen, String title, String desc, int worldIndex) {
        this.parentScreen = parentScreen;
        this.title = title;
        this.desc = desc;
        this.worldNumber = worldIndex;
    }

    public final void initGui() {
        this.controlList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 6 + 96, "Yes"));
        this.controlList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, "No"));
    }

    protected final void actionPerformed(GuiButton button) {
        this.parentScreen.deleteWorld(button.id == 0, this.worldNumber);
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, this.title, this.width / 2, 70, 0xFFFFFF);
        drawCenteredString(this.fontRenderer, this.desc, this.width / 2, 90, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}