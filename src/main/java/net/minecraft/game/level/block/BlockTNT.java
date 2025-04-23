package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;

public final class BlockTNT extends Block {
	public BlockTNT() {
		super(46, 8);
	}

	public final int getBlockTexture(int var1) {
		return var1 == 0 ? this.blockIndexInTexture + 2 : (var1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public final void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4) {
		var1.createExplosion(var2, var3 - 1, var4, Block.planks.blockID);
	}
}
