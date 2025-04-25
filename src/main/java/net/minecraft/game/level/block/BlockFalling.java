package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockFalling extends Block {
	public BlockFalling(int var1, int var2) {
		super(var1, var2);
	}

	public final void onBlockPlaced(World var1, int var2, int var3, int var4) {
		this.fall(var1, var2, var3, var4);
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		this.fall(var1, var2, var3, var4);
	}

	private void fall(World var1, int var2, int var3, int var4) {
		int var5 = var3;

		while(true) {
			int var8 = var5 - 1;
			int var6 = var1.getBlockId(var2, var8, var4);
			boolean var10000;
			if(var6 == 0) {
				var10000 = true;
			} else {
				Material var10 = Block.blocksList[var6].getBlockMaterial();
				var10000 = var10 == Material.water ? true : var10 == Material.lava;
			}

			if(!var10000 || var5 <= 0) {
				if(var5 != var3) {
					var6 = var1.getBlockId(var2, var5, var4);
					if(var6 > 0 && Block.blocksList[var6].getBlockMaterial() != Material.air) {
						var1.setTileNoUpdate(var2, var5, var4, 0);
					}

					var1.swap(var2, var3, var4, var2, var5, var4);
				}

				return;
			}

			--var5;
		}
	}
}
