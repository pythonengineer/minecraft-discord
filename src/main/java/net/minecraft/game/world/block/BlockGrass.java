package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(blockID, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return side == 1 ? 0 : (side == 0 ? 2 : (blockAccess.getBlockMaterial(x, y + 1, z) == Material.snow ? 68 : 3));
    }

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockMaterial(x, y + 1, z).getCanBlockGrass()) {
			if(rand.nextInt(4) != 0) {
				return;
			}

			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		} else if(world.getBlockLightValue(x, y + 1, z) >= 9) {
			int i6 = x + rand.nextInt(3) - 1;
			int i7 = y + rand.nextInt(5) - 3;
			int i8 = z + rand.nextInt(3) - 1;
			if(world.getBlockId(i6, i7, i8) == Block.dirt.blockID && world.getBlockLightValue(i6, i7 + 1, i8) >= 4 && !world.getBlockMaterial(i6, i7 + 1, i8).getCanBlockGrass()) {
				world.setBlockWithNotify(i6, i7, i8, Block.grass.blockID);
			}
		}

	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.dirt.idDropped(0, rand);
	}
}
