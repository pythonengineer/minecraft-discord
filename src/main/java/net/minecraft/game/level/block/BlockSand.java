package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSand extends Block {
	public BlockSand(int var1, int var2) {
		super(var1, var2);
	}

	public final void onBlockPlaced(World var1, int var2, int var3, int var4) {
		this.fall(var1, var2, var3, var4);
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		this.fall(var1, var2, var3, var4);
	}

	private void fall(World var1, int var2, int var3, int var4) {
		int var11 = var2;
		int var5 = var3;
		int var6 = var4;

		while(true) {
			int var9 = var5 - 1;
			Material var7 = null;
			int var12 = var1.getBlockId(var11, var9, var6);
			boolean var10000;
			if(var12 == 0) {
				var10000 = true;
			} else {
				var7 = Block.blocksList[var12].getMaterial();
				var10000 = var7 == Material.water ? true : var7 == Material.lava;
			}

			if(!var10000 || var5 <= 0) {
				if(var5 != var3) {
					var12 = var1.getBlockId(var11, var5, var6);
					if(var12 > 0 && Block.blocksList[var12].getMaterial() != Material.air) {
						var1.setTileNoUpdate(var11, var5, var6, 0);
					}

					var1.swap(var2, var3, var4, var11, var5, var6);
				}

				return;
			}

			--var5;
		}
	}
}
