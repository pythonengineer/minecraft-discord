package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class InventoryCraftResult implements IInventory {
	private ItemStack[] stackResult = new ItemStack[1];

	public int getSizeInventory() {
		return 1;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.stackResult[slot];
	}

	public String getInvName() {
		return "Result";
	}

    public ItemStack decrStackSize(int slot, int decrementAmount) {
        if(this.stackResult[slot] != null) {
            ItemStack decrementAmount1 = this.stackResult[slot];
            this.stackResult[slot] = null;
            return decrementAmount1;
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.stackResult[slot] = stack;
    }

	public int getInventoryStackLimit() {
		return 64;
	}

    public void onInventoryChanged() {
    }
}
