package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int var1) {
		super(6, 15);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, var2 + 0.5F, var2 * 2.0F, var2 + 0.5F);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		int var6 = var1.getBlockId(var2, var3 - 1, var4);
		if(var1.isHalfLit(var2, var3, var4) && (var6 == Block.dirt.blockID || var6 == Block.grass.blockID)) {
			if(var5.nextInt(5) == 0) {
				var1.setTileNoUpdate(var2, var3, var4, 0);
				if(!var1.growTrees(var2, var3, var4)) {
					var1.setTileNoUpdate(var2, var3, var4, this.blockID);
				}
			}

		} else {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
	}
}
