package net.minecraft.client.gui;

final class Slot {
    public final int slotIndex;
    public final int xDisplayPosition;
    public final int yDisplayPosition;
    private GuiInventory inventory;

    public Slot(GuiInventory var1, int var2, int var3, int var4) {
        this.inventory = var1;
        this.slotIndex = var2;
        this.xDisplayPosition = var3;
        this.yDisplayPosition = var4;
    }

    public final boolean getIsMouseOverSlot(int var1, int var2) {
        int var3 = (this.inventory.width - 176) / 2;
        int var4 = (this.inventory.height - 184) / 2;
        var1 -= var3;
        var2 -= var4;
        return var1 >= this.xDisplayPosition - 1 && var1 < this.xDisplayPosition + 16 + 1 && var2 >= this.yDisplayPosition - 1 && var2 < this.yDisplayPosition + 16 + 1;
    }
}
