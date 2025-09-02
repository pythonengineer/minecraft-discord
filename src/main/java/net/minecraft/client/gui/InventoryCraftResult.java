package net.minecraft.client.gui;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

class InventoryCraftResult implements IInventory {
    private ItemStack[] stackResult;

    private InventoryCraftResult(GuiCrafting var1, byte var2) {
        this.stackResult = new ItemStack[1];
    }

    public final int getSizeInventory() {
        return 1;
    }

    public final ItemStack getStackInSlot(int var1) {
        return this.stackResult[var1];
    }

    public final String getInvName() {
        return "Result";
    }

    public final void setInventorySlotContents(int var1, ItemStack var2) {
        this.stackResult[var1] = var2;
    }

    public final int getInventoryStackLimit() {
        return 64;
    }

    InventoryCraftResult(GuiCrafting var1) {
        this(var1, (byte)0);
    }
}
