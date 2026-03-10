package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSource extends Block {
	private int fluid;

	protected BlockSource(int blockID, int textureIndex) {
		super(blockID, Block.blocksList[textureIndex].blockIndexInTexture, Material.water);
		this.fluid = textureIndex;
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if(world.getBlockId(x - 1, y, z) == 0) {
			world.notifyBlockChange(x - 1, y, z, this.fluid);
		}

		if(world.getBlockId(x + 1, y, z) == 0) {
			world.notifyBlockChange(x + 1, y, z, this.fluid);
		}

		if(world.getBlockId(x, y, z - 1) == 0) {
			world.notifyBlockChange(x, y, z - 1, this.fluid);
		}

		if(world.getBlockId(x, y, z + 1) == 0) {
			world.notifyBlockChange(x, y, z + 1, this.fluid);
		}

	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
		if(world.getBlockId(x - 1, y, z) == 0) {
			world.notifyBlockChange(x - 1, y, z, this.fluid);
		}

		if(world.getBlockId(x + 1, y, z) == 0) {
			world.notifyBlockChange(x + 1, y, z, this.fluid);
		}

		if(world.getBlockId(x, y, z - 1) == 0) {
			world.notifyBlockChange(x, y, z - 1, this.fluid);
		}

		if(world.getBlockId(x, y, z + 1) == 0) {
			world.notifyBlockChange(x, y, z + 1, this.fluid);
		}

	}
}