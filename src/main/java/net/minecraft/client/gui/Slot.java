package net.minecraft.client.gui;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class Slot {
    public final int slotIndex;
    public final int xPos;
    public final int yPos;
    private IInventory inventory;
    private GuiInventory guiHandler;

    public Slot(GuiInventory var1, IInventory var2, int var3, int var4, int var5) {
        this.guiHandler = var1;
        this.inventory = var2;
        this.slotIndex = var3;
        this.xPos = var4;
        this.yPos = var5;
    }

    public final boolean isAtCursorPos(int var1, int var2) {
        int var3 = (this.guiHandler.width - this.guiHandler.xSize) / 2;
        int var4 = (this.guiHandler.height - this.guiHandler.ySize) / 2;
        var1 -= var3;
        var2 -= var4;
        return var1 >= this.xPos - 1 && var1 < this.xPos + 16 + 1 && var2 >= this.yPos - 1 && var2 < this.yPos + 16 + 1;
    }

    public final void putStacks(Slot var1) {
        ItemStack var2 = this.inventory.getStackInSlot(this.slotIndex);
        ItemStack var3 = var1.inventory.getStackInSlot(var1.slotIndex);
        var1.inventory.setInventorySlotContents(var1.slotIndex, var2);
        this.inventory.setInventorySlotContents(this.slotIndex, var3);
    }

    static IInventory b(Slot var0) {
        return var0.inventory;
    }
}
