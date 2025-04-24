package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockStep extends Block {
	private boolean isDouble;

	public BlockStep(int var1, boolean var2) {
		super(var1, 6);
		this.isDouble = var2;
		if(!var2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

	}

	public final int getBlockTexture(int var1) {
		return var1 <= 1 ? 6 : 5;
	}

	public final boolean isOpaqueCube() {
		return this.isDouble;
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(this == Block.stairSingle) {
		}
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(var1, var2, var3, var4);
		}

		if(var1.getBlockId(var2, var3 - 1, var4) == stairSingle.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
			var1.setBlockWithNotify(var2, var3 - 1, var4, Block.stairDouble.blockID);
		}

	}

    public final int idDropped() {
        return Block.stairSingle.blockID;
    }

	public final boolean renderAsNormalBlock() {
		return this.isDouble;
	}

	public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		return var5 == 1 ? true : (!super.shouldSideBeRendered(var1, var2, var3, var4, var5) ? false : (var5 == 0 ? true : var1.getBlockId(var2, var3, var4) != this.blockID));
	}
}
