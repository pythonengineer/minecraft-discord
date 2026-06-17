package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class InventoryCrafting implements IInventory {
	private ItemStack[] stackList;
	private int gridSize;
    private CraftingInventoryCB craftingInventory;

    public InventoryCrafting(CraftingInventoryCB craftingInventoryCB, int x, int y) {
        this.gridSize = x * y;
        this.stackList = new ItemStack[this.gridSize];
        this.craftingInventory = craftingInventoryCB;
    }

    public InventoryCrafting(CraftingInventoryCB craftingInventoryCB, ItemStack[] stack) {
        this.gridSize = stack.length;
        this.stackList = stack;
        this.craftingInventory = craftingInventoryCB;
    }

	public int getSizeInventory() {
		return this.gridSize;
	}

    public ItemStack getStackInSlot(int slot) {
        return this.stackList[slot];
    }

	public String getInvName() {
		return "Crafting";
	}

    public ItemStack decrStackSize(int slot, int decrementAmount) {
        if(this.stackList[slot] != null) {
            ItemStack decrementAmount1;
            if(this.stackList[slot].stackSize <= decrementAmount) {
                decrementAmount1 = this.stackList[slot];
                this.stackList[slot] = null;
                this.craftingInventory.onCraftMatrixChanged(this);
                return decrementAmount1;
            } else {
                decrementAmount1 = this.stackList[slot].splitStack(decrementAmount);
                if(this.stackList[slot].stackSize == 0) {
                    this.stackList[slot] = null;
                }

                this.craftingInventory.onCraftMatrixChanged(this);
                return decrementAmount1;
            }
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.stackList[slot] = stack;
        this.craftingInventory.onCraftMatrixChanged(this);
    }

	public int getInventoryStackLimit() {
		return 64;
	}

    public void onInventoryChanged() {
    }
}
