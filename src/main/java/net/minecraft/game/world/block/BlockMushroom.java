package net.minecraft.game.world.block;

import net.minecraft.game.world.World;

public final class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int i1, int i2) {
        super(i1, i2);
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
    }

    protected final boolean canThisPlantGrowOnThisBlockID(int blockID) {
        return Block.opaqueCubeLookup[blockID];
    }

    public final boolean canBlockStay(World world, int x, int y, int z) {
        if(world.getBlockLightValue(x, y, z) <= 13) {
            x = world.getBlockId(x, y - 1, z);
            if(Block.opaqueCubeLookup[x]) {
                return true;
            }
        }

        return false;
    }
}