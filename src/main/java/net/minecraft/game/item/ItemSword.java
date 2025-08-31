package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSword extends Item {
    public ItemSword(int var1) {
        super(var1);
        this.maxStackSize = 1;
    }

    public final float getStrVsBlock(Block var1) {
        return 1.5F;
    }
}
