package net.minecraft.game.entity.player;

public final class InventoryPlayer {
    public ItemStack[] mainInventory = new ItemStack[64];
    public int currentItem = 0;

    public final ItemStack getCurrentItem() {
        return this.mainInventory[this.currentItem];
    }

    public final int getInventorySlotContainItem(int var1) {
        for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if(this.mainInventory[var2] != null && this.mainInventory[var2].itemID == var1) {
                return var2;
            }
        }

        return -1;
    }

    final int getFirstEmptyStack() {
        for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
            if(this.mainInventory[var1] == null) {
                return var1;
            }
        }

        return -1;
    }

    public final void setInventorySlotContents(int var1, int var2) {
        ItemStack var3 = this.mainInventory[var2];
        this.mainInventory[var2] = this.mainInventory[var1];
        this.mainInventory[var1] = var3;
    }
}
