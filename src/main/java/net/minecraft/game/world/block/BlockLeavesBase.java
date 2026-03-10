package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockLeavesBase extends Block {
	private boolean graphicsLevel = true;

	protected BlockLeavesBase(int blockID, int textureIndex, Material material, boolean flag) {
		super(blockID, textureIndex, material);
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean getIsBlockSolid(World world, int x, int y, int z, int metadata) {
		int i6 = world.getBlockId(x, y, z);
		return !this.graphicsLevel && i6 == this.blockID ? false : super.getIsBlockSolid(world, x, y, z, metadata);
	}
}