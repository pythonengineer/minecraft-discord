package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockSponge extends Block {
	protected BlockSponge() {
		super(19);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		for(int var7 = var2 - 2; var7 <= var2 + 2; ++var7) {
			for(int var5 = var3 - 2; var5 <= var3 + 2; ++var5) {
				for(int var6 = var4 - 2; var6 <= var4 + 2; ++var6) {
					if(var1.isWater(var7, var5, var6)) {
						var1.setBlock(var7, var5, var6, 0);
					}
				}
			}
		}

	}

	public final void onBlockRemoval(World var1, int var2, int var3, int var4) {
		for(int var7 = var2 - 2; var7 <= var2 + 2; ++var7) {
			for(int var5 = var3 - 2; var5 <= var3 + 2; ++var5) {
				for(int var6 = var4 - 2; var6 <= var4 + 2; ++var6) {
					var1.notifyBlocksOfNeighborChange(var7, var5, var6, var1.getBlockId(var7, var5, var6));
				}
			}
		}

	}
}
