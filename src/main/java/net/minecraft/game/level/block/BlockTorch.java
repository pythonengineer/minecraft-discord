package net.minecraft.game.level.block;

import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockTorch extends Block {
    protected BlockTorch(int var1, int var2) {
		super(50, 80);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int g() {
		return 2;
	}
}
