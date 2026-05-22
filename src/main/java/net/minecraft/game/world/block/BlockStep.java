package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int blockID, boolean blockType) {
		super(blockID, 6, Material.rock);
		this.blockType = blockType;
		if(!blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public final int getBlockTextureFromSide(int side) {
		return side <= 1 ? 6 : 5;
	}

	public final boolean isOpaqueCube() {
		return this.blockType;
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(this == Block.stairSingle) {
			;
		}
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world, x, y, z);
		}

		if(world.getBlockId(x, y - 1, z) == stairSingle.blockID) {
			world.setBlockWithNotify(x, y, z, 0);
			world.setBlockWithNotify(x, y - 1, z, Block.stairDouble.blockID);
		}

	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.stairSingle.blockID;
	}

	public final boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public final boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int metadata) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(iBlockAccess, x, y, z, metadata);
		}

		return metadata == 1 ? true : (!super.shouldSideBeRendered(iBlockAccess, x, y, z, metadata) ? false : (metadata == 0 ? true : iBlockAccess.getBlockId(x, y, z) != this.blockID));
	}
}