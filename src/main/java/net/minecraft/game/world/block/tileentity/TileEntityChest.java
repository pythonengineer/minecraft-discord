package net.minecraft.game.world.block.tileentity;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class TileEntityChest extends TileEntity implements IInventory {
    private ItemStack[] chestContents = new ItemStack[36];

    public final int getSizeInventory() {
        return 27;
    }

    public final ItemStack getStackInSlot(int var1) {
        return this.chestContents[var1];
    }

    public final ItemStack decrStackSize(int var1, int var2) {
        if(this.chestContents[var1] != null) {
            ItemStack var3;
            if(this.chestContents[var1].stackSize <= var2) {
                var3 = this.chestContents[var1];
                this.chestContents[var1] = null;
                return var3;
            } else {
                var3 = this.chestContents[var1].splitStack(var2);
                if(this.chestContents[var1].stackSize == 0) {
                    this.chestContents[var1] = null;
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    public final void setInventorySlotContents(int var1, ItemStack var2) {
        this.chestContents[var1] = var2;
        if(var2 != null && var2.stackSize > 64) {
            var2.stackSize = 64;
        }

    }

    public final String getInvName() {
        return "Chest";
    }

    public final int getInventoryStackLimit() {
        return 64;
    }
}
