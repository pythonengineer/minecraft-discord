package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class BlockBookshelf extends Block {
	public BlockBookshelf() {
		super(47, 35);
	}

	public final int getBlockTexture(int var1) {
		return var1 <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}
}
