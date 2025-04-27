package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemTool extends Item {
    private Block[] blocksEffectiveAgainst;
    private float efficiencyOnProperMaterial = 4.0F;

    public ItemTool(int var1, Block[] var2) {
        super(var1);
        this.blocksEffectiveAgainst = var2;
    }

    public final float getStrVsBlock(Block var1) {
        for(int var2 = 0; var2 < this.blocksEffectiveAgainst.length; ++var2) {
            if(this.blocksEffectiveAgainst[var2] == var1) {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }
}
