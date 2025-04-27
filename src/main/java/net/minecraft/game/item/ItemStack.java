package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemStack {
    public int stackSize;
    public int animationsToGo;
    public int itemID;

    public ItemStack(Block var1, int var2) {
        this(var1.blockID, 99);
    }

    public ItemStack(int var1) {
        this(var1, 1);
    }

    public ItemStack(int var1, int var2) {
        this.stackSize = 0;
        this.itemID = var1;
        this.stackSize = var2;
    }

    public final Item getItem() {
        return Item.itemsList[this.itemID];
    }
}
