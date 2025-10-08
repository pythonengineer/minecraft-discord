package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockGrass extends Block {
	protected BlockGrass(int var1) {
		super(2, Material.ground);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? 0 : (var1 == 0 ? 2 : 3);
	}

	public final int idDropped(int var1, EaglercraftRandom var2) {
		return Block.dirt.idDropped(0, var2);
	}
}
