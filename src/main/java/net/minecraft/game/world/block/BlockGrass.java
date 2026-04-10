package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(2, Material.grassMaterial);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? 0 : (side == 0 ? 2 : 3);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockMaterial(x, y + 1, z).getCanBlockGrass()) {
			if(rand.nextInt(4) == 0) {
				world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
			}
		} else {
			if(world.getBlockLightValue(x, y + 1, z) >= 9) {
				x = x + rand.nextInt(3) - 1;
				y = y + rand.nextInt(5) - 3;
				z = z + rand.nextInt(3) - 1;
				if(world.getBlockId(x, y, z) == Block.dirt.blockID && world.getBlockLightValue(x, y + 1, z) >= 4 && !world.getBlockMaterial(x, y + 1, z).getCanBlockGrass()) {
					world.setBlockWithNotify(x, y, z, Block.grass.blockID);
				}
			}

		}
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.dirt.idDropped(0, rand);
	}
}