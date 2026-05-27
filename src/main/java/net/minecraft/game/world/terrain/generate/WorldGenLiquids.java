package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class WorldGenLiquids extends WorldGenerator {
    private int liquidBlockId;

    public WorldGenLiquids(int blockID) {
        this.liquidBlockId = blockID;
    }

    public boolean generate(World world, EaglercraftRandom rand, int x, int y, int z) {
        if(world.getBlockId(x, y + 1, z) != Block.stone.blockID) {
            return false;
        } else if(world.getBlockId(x, y - 1, z) != Block.stone.blockID) {
            return false;
        } else if(world.getBlockId(x, y, z) != 0 && world.getBlockId(x, y, z) != Block.stone.blockID) {
            return false;
        } else {
            int i6 = 0;
            if(world.getBlockId(x - 1, y, z) == Block.stone.blockID) {
                ++i6;
            }

            if(world.getBlockId(x + 1, y, z) == Block.stone.blockID) {
                ++i6;
            }

            if(world.getBlockId(x, y, z - 1) == Block.stone.blockID) {
                ++i6;
            }

            if(world.getBlockId(x, y, z + 1) == Block.stone.blockID) {
                ++i6;
            }

            int i7 = 0;
            if(world.getBlockId(x - 1, y, z) == 0) {
                ++i7;
            }

            if(world.getBlockId(x + 1, y, z) == 0) {
                ++i7;
            }

            if(world.getBlockId(x, y, z - 1) == 0) {
                ++i7;
            }

            if(world.getBlockId(x, y, z + 1) == 0) {
                ++i7;
            }

            if(i6 == 3 && i7 == 1) {
                world.setBlockWithNotify(x, y, z, this.liquidBlockId);
            }

            return true;
        }
    }
}
