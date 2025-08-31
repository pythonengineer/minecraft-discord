package net.minecraft.client.gui;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

final class InventoryCrafting implements IInventory {
    private ItemStack[] stackList;
    private GuiCrafting eventHandler;

    private InventoryCrafting(GuiCrafting var1, byte var2) {
        this.eventHandler = var1;
        this.stackList = new ItemStack[9];
    }

    public final int getSizeInventory() {
        return 9;
    }

    public final ItemStack getStackInSlot(int var1) {
        return this.stackList[var1];
    }

    public final String getInvName() {
        return "Crafting";
    }

    public final void setInventorySlotContents(int var1, ItemStack var2) {
        this.stackList[var1] = var2;
        this.eventHandler.onCraftMatrixChanged();
    }

    public final int getInventoryStackLimit() {
        return 1;
    }

    InventoryCrafting(GuiCrafting var1) {
        this(var1, (byte)0);
    }

    static ItemStack[] a(InventoryCrafting var0) {
        return var0.stackList;
    }
}
