package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public final class ItemStack {
    public int stackSize;
    public int animationsToGo;
    public int itemID;
    public int itemDamage;

    public ItemStack(Block var1) {
        this((Block)var1, 1);
    }

    public ItemStack(Block var1, int var2) {
        this(var1.blockID, var2);
    }

    public ItemStack(Item var1) {
        this((Item)var1, 1);
    }

    public ItemStack(Item var1, int var2) {
        this(var1.shiftedIndex, var2);
    }

    public ItemStack(int var1) {
        this(var1, 1);
    }

    public ItemStack(int var1, int var2) {
        this.stackSize = 0;
        this.itemID = var1;
        this.stackSize = var2;
    }

    private ItemStack(int var1, int var2, int var3) {
        this.stackSize = 0;
        this.itemID = var1;
        this.stackSize = var2;
        this.itemDamage = var3;
    }

    public final ItemStack splitStack(int var1) {
        this.stackSize -= var1;
        return new ItemStack(this.itemID, var1, this.itemDamage);
    }

    public final Item getItem() {
        return Item.itemsList[this.itemID];
    }

    public final int isItemStackDamageable() {
        return Item.itemsList[this.itemID].getMaxDamage();
    }

    public final void damageItem(int var1) {
        this.itemDamage += var1;
        if(this.itemDamage > this.isItemStackDamageable()) {
            --this.stackSize;
            if(this.stackSize < 0) {
                this.stackSize = 0;
            }

            this.itemDamage = 0;
        }

    }

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.itemID, this.stackSize, this.itemDamage);
        return itemstack;
    }
}
