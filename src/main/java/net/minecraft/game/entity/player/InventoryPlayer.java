package net.minecraft.game.entity.player;

import net.minecraft.game.level.block.Block;

public final class InventoryPlayer {
    public ItemStack[] mainInventory = new ItemStack[64];
    public int currentSlot = 0;

    public final ItemStack getCurrentItem() {
        return this.mainInventory[this.currentSlot];
    }

    private int getInventorySlotContainItem(int var1) {
        for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if(this.mainInventory[var2] != null) {
                ItemStack var3 = this.mainInventory[var2];
                if(var3.itemID == var1) {
                    return var2;
                }
            }
        }

        return -1;
    }

    private int getFirstEmptyStack() {
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
            this.currentSlot = var1;
        }
    }

    public final boolean removeResource(int var1) {
        var1 = this.getInventorySlotContainItem(var1);
        if(var1 < 0) {
            return false;
        } else {
            if(--this.mainInventory[var1].stackSize <= 0) {
                this.mainInventory[var1] = null;
            }

            return true;
        }
    }

    public final void setInventorySlotContents(int var1, int var2) {
        ItemStack var3 = this.mainInventory[var2];
        this.mainInventory[var2] = this.mainInventory[var1];
        this.mainInventory[var1] = var3;
    }

    public final boolean addItemStackToInventory(ItemStack var1) {
        int var2;
        if(var1.itemID > 0) {
            var2 = var1.itemID;
            int var3 = this.getInventorySlotContainItem(var2);
            if(var3 < 0) {
                var3 = this.getFirstEmptyStack();
            }

            if(var3 < 0) {
                return false;
            } else {
                if(this.mainInventory[var3] == null) {
                    this.mainInventory[var3] = new ItemStack(Block.blocksList[var2], 0);
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
            var2 = this.getFirstEmptyStack();
            if(var2 >= 0) {
                this.mainInventory[var2] = var1;
                return true;
            } else {
                return false;
            }
        }
    }
}
