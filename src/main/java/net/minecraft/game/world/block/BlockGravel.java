package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;

public final class BlockGravel extends BlockSand {
	public BlockGravel(int i1, int i2) {
		super(13, 19);
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return rand.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}