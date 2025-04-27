package net.minecraft.game.entity.player;

import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer {
    public ItemStack[] mainInventory = new ItemStack[64];
    public int currentItem = 0;

    public final ItemStack getCurrentItem() {
        return this.mainInventory[this.currentItem];
    }

    private int getInventorySlotContainItem(int var1) {
        for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if(this.mainInventory[var2] != null && this.mainInventory[var2].itemID == var1) {
                return var2;
            }
        }

        return -1;
    }

    private int storeItemStack() {
        for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
            if(this.mainInventory[var1] == null) {
                return var1;
            }
        }

        return -1;
    }

    public final void getFirstEmptyStack(int var1) {
        var1 = this.getInventorySlotContainItem(var1);
        if(var1 >= 0 && var1 < 9) {
            this.currentItem = var1;
        }
    }

    public final void swapSlots(int var1, int var2) {
        ItemStack var3 = this.mainInventory[var2];
        this.mainInventory[var2] = this.mainInventory[var1];
        this.mainInventory[var1] = var3;
    }

    public final boolean addItemStackToInventory(ItemStack var1) {
        int var2;
        if(var1.itemID < 256) {
            var2 = var1.itemID;
            int var3 = this.getInventorySlotContainItem(var2);
            if(var3 < 0) {
                var3 = this.storeItemStack();
            }

            if(var3 < 0) {
                return false;
            } else {
                if(this.mainInventory[var3] == null) {
                    this.mainInventory[var3] = new ItemStack(var2, 0);
                }

                if(this.mainInventory[var3].stackSize >= 99) {
                    return false;
                } else {
                    ++this.mainInventory[var3].stackSize;
                    this.mainInventory[var3].animationsToGo = 5;
                    return true;
                }
            }
        } else {
            var2 = this.storeItemStack();
            if(var2 >= 0) {
                this.mainInventory[var2] = var1;
                return true;
            } else {
                return false;
            }
        }
    }
}
