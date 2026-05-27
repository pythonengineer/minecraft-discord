package net.minecraft.game.world.block;

import net.minecraft.game.world.World;

public class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int i1, int i2) {
        super(i1, i2);
        float f3 = 0.2F;
        this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
    }

    protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
        return Block.opaqueCubeLookup[blockID];
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockLightValue(x, y, z) <= 13 && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
    }
}
