package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;

public final class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int var1, int var2) {
        super(var1, var2);
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
    }

	public final void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		int var6 = var1.getBlockId(var2, var3 - 1, var4);
		if(var1.isHalfLit(var2, var3, var4) || var6 != Block.stone.blockID && var6 != Block.gravel.blockID && var6 != Block.cobblestone.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}
}
