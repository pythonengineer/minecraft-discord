package net.minecraft.game.entity.player;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer implements IInventory {
    public ItemStack[] mainInventory = new ItemStack[36];
    public ItemStack[] armorInventory = new ItemStack[4];
    public int currentItem = 0;
    private EntityPlayer player;

    public InventoryPlayer(EntityPlayer var1) {
        this.player = var1;
    }

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

    public int getFirstEmptyStack() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null) continue;
            return i;
        }
        return -1;
    }

    public final void getFirstEmptyStack(int var1) {
        var1 = this.getInventorySlotContainItem(var1);
        if(var1 >= 0 && var1 < 9) {
            this.currentItem = var1;
        }
    }

    public final boolean consumeInventoryItem(int var1) {
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

    public final ItemStack decrStackSize(int var1, int var2) {
        ItemStack[] var3 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var3 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }

        if(var3[var1] != null) {
            ItemStack var4;
            if(var3[var1].stackSize <= var2) {
                var4 = var3[var1];
                var3[var1] = null;
                return var4;
            } else {
                var4 = var3[var1].splitStack(var2);
                if(var3[var1].stackSize == 0) {
                    var3[var1] = null;
                }

                return var4;
            }
        } else {
            return null;
        }
    }

    public final void setInventorySlotContents(int var1, ItemStack var2) {
        ItemStack[] var3 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var3 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }

        var3[var1] = var2;
    }

    public final int getSizeInventory() {
        return this.mainInventory.length + 4;
    }

    public final ItemStack getStackInSlot(int var1) {
        ItemStack[] var2 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var2 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }

        return var2[var1];
    }

    public final String getInvName() {
        return "Inventory";
    }

    public final int getInventoryStackLimit() {
        return 64;
    }

    public final int getPlayerArmorValue() {
        int var1 = 0;
        int var2 = 0;
        int var3 = 0;

        for(int var4 = 0; var4 < this.armorInventory.length; ++var4) {
            if(this.armorInventory[var4] != null && this.armorInventory[var4].getItem() instanceof ItemArmor) {
                int var5 = this.armorInventory[var4].isItemStackDamageable();
                int var6 = this.armorInventory[var4].itemDamage;
                var6 = var5 - var6;
                var2 += var6;
                var3 += var5;
                var5 = ((ItemArmor)this.armorInventory[var4].getItem()).damageReduceAmount;
                var1 += var5;
            }
        }

        if(var3 == 0) {
            return 0;
        } else {
            return (var1 - 1) * var2 / var3 + 1;
        }
    }
}
