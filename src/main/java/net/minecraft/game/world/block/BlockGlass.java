package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockGlass extends BlockBreakable {
	public BlockGlass(int var1, int var2, Material var3, boolean var4) {
		super(20, 49, var3, false);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}
}
