package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class BlockLeaves extends BlockLeavesBase {
	protected BlockLeaves() {
		super(18, 22);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return var1.nextInt(10) == 0 ? 1 : 0;
	}
}
