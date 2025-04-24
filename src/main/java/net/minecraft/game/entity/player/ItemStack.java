package net.minecraft.game.entity.player;

import net.minecraft.game.level.block.Block;

public final class ItemStack {
    public int stackSize;
    public int itemID;
    public int iconIndex;
    public int animationsToGo;

    public ItemStack(Block var1) {
        this(var1, 1);
    }

    public ItemStack(Block var1, int var2) {
        this.itemID = -1;
        this.iconIndex = -1;
        this.itemID = var1.blockID;
        this.stackSize = var2;
    }

    public ItemStack(int var1) {
        this.itemID = -1;
        this.iconIndex = -1;
        this.iconIndex = var1;
        this.stackSize = 1;
    }

    public ItemStack(ItemStack var1) {
        this.itemID = -1;
        this.iconIndex = -1;
        this.itemID = var1.itemID;
        this.iconIndex = var1.iconIndex;
        this.stackSize = 1;
    }

    public boolean shouldUseOnTouchEagler() {
        return false;
    }
}
