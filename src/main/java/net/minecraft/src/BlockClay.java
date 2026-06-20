package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockClay extends Block {
	public BlockClay(int var1, int var2) {
		super(var1, var2, Material.clay);
	}

	public int idDropped(int var1, EaglercraftRandom var2) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(EaglercraftRandom var1) {
		return 4;
	}
}
