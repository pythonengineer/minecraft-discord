package net.minecraft.game.world.block;

import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.material.Material;

public class BlockLeavesBase extends Block {
    protected boolean graphicsLevel = false;

    protected BlockLeavesBase(int blockID, int textureIndex, Material material, boolean flag) {
        super(blockID, textureIndex, material);
    }

    public boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int metadata) {
		int i6 = iBlockAccess.getBlockId(x, y, z);
		return !this.graphicsLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(iBlockAccess, x, y, z, metadata);
	}
}