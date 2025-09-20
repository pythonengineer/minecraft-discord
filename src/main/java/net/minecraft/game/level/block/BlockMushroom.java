package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int var1, int var2) {
        super(var1, var2);
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
    }

    protected final boolean canThisPlantGrowOnThisBlockID(int var1) {
        return Block.opaqueCubeLookup[var1];
    }

    protected final void checkFlowerChange(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockId(var2, var3 - 1, var4);
        if(!var1.isFullyLit(var2, var3, var4) || !Block.opaqueCubeLookup[var5]) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }
}
