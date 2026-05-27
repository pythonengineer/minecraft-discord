package net.minecraft.game;

import net.minecraft.game.item.ItemStack;

public class InventoryLargeChest implements IInventory {
    private String name;
    private IInventory upperChest;
    private IInventory lowerChest;

    public InventoryLargeChest(String name, IInventory upperChestInventory, IInventory lowerChestInventory) {
        this.name = name;
        this.upperChest = upperChestInventory;
        this.lowerChest = lowerChestInventory;
    }

    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public String getInvName() {
        return this.name;
    }

    public ItemStack getStackInSlot(int slot) {
        return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slot - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slot);
    }

    public ItemStack decrStackSize(int slot, int decrementAmount) {
        return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(slot - this.upperChest.getSizeInventory(), decrementAmount) : this.upperChest.decrStackSize(slot, decrementAmount);
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if(slot >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(slot - this.upperChest.getSizeInventory(), stack);
        } else {
            this.upperChest.setInventorySlotContents(slot, stack);
        }
    }

    public int getInventoryStackLimit() {
        return this.upperChest.getInventoryStackLimit();
    }

    public void onInventoryChanged() {
        this.upperChest.onInventoryChanged();
        this.lowerChest.onInventoryChanged();
    }
}
