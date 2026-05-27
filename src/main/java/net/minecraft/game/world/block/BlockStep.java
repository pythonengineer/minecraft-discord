package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int blockID, boolean blockType) {
		super(blockID, 6, Material.rock);
		this.blockType = blockType;
		if(!blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public int getBlockTextureFromSide(int side) {
		return side <= 1 ? 6 : 5;
	}

	public boolean isOpaqueCube() {
		return this.blockType;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(this == Block.stairSingle) {
			;
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world, x, y, z);
		}

		int i5 = world.getBlockId(x, y - 1, z);
		if(i5 == stairSingle.blockID) {
			world.setBlockWithNotify(x, y, z, 0);
			world.setBlockWithNotify(x, y - 1, z, Block.stairDouble.blockID);
		}

	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.stairSingle.blockID;
	}

	public boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(blockAccess, x, y, z, metadata);
		}

		return metadata == 1 ? true : (!super.shouldSideBeRendered(blockAccess, x, y, z, metadata) ? false : (metadata == 0 ? true : blockAccess.getBlockId(x, y, z) != this.blockID));
	}
}
