package net.minecraft.game.world.block;

public final class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int var1, int var2) {
        super(var1, var2);
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
    }

    protected final boolean canThisPlantGrowOnThisBlockID(int var1) {
        return Block.opaqueCubeLookup[var1];
    }
}
