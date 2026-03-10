package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockGlass extends BlockBreakable {
	public BlockGlass(int i1, int i2, Material material3, boolean z4) {
		super(20, 49, material3, false);
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}
}