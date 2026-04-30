package net.minecraft.game.world.block;

import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.material.Material;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int blockID, int textureIndex, Material material, boolean flag) {
		super(blockID, textureIndex, material);
		this.localFlag = flag;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean getIsBlockSolid(IBlockAccess iBlockAccess, int x, int y, int z, int metadata) {
		int i6 = iBlockAccess.getBlockId(x, y, z);
		return !this.localFlag && i6 == this.blockID ? false : super.getIsBlockSolid(iBlockAccess, x, y, z, metadata);
	}
}