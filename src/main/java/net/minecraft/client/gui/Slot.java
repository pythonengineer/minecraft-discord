package net.minecraft.client.gui;

final class Slot {
    public final int slotIndex;
    public final int xPos;
    public final int yPos;
    private GuiInventory guiHandler;

    public Slot(GuiInventory var1, int var2, int var3, int var4) {
        this.guiHandler = var1;
        this.slotIndex = var2;
        this.xPos = var3;
        this.yPos = var4;
    }

    public final boolean isAtCursorPos(int var1, int var2) {
        int var3 = (this.guiHandler.width - 176) / 2;
        int var4 = (this.guiHandler.height - 184) / 2;
        var1 -= var3;
        var2 -= var4;
        return var1 >= this.xPos - 1 && var1 < this.xPos + 16 + 1 && var2 >= this.yPos - 1 && var2 < this.yPos + 16 + 1;
    }
}
