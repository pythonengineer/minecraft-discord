package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;

public final class BlockGrass extends Block {
    protected BlockGrass(int var1) {
		super(2);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTexture(int var1) {
		return var1 == 1 ? 0 : (var1 == 0 ? 2 : 3);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		if(var5.nextInt(4) == 0) {
			if(!var1.isHalfLit(var2, var3 + 1, var4)) {
				var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
			} else {
				for(int var9 = 0; var9 < 4; ++var9) {
					int var6 = var2 + var5.nextInt(3) - 1;
					int var7 = var3 + var5.nextInt(5) - 3;
					int var8 = var4 + var5.nextInt(3) - 1;
					if(var1.getBlockId(var6, var7, var8) == Block.dirt.blockID && var1.isHalfLit(var6, var7, var8)) {
						var1.setBlockWithNotify(var6, var7, var8, Block.grass.blockID);
					}
				}

			}
		}
	}

    public final int idDropped() {
        return Block.dirt.idDropped();
    }
}
