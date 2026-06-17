package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	public final int slotIndex;
	public final IInventory inventory;

    public Slot(IInventory inventory, int slot) {
        this.inventory = inventory;
        this.slotIndex = slot;
    }

	public void onPickupFromSlot() {
        this.onSlotChanged();
	}

    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean getHasStack() {
        return this.getStack() != null;
    }

    public ItemStack decrStackSize(int i) {
        return this.inventory.decrStackSize(this.slotIndex, i);
    }

    public void putStack(ItemStack stack) {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }

    public int getBackgroundIconIndex() {
        return -1;
    }

    public void onSlotChanged() {
        this.inventory.onInventoryChanged();
    }
}
