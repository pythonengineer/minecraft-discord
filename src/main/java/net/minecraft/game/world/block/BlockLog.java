package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockLog extends Block {
	protected BlockLog(int blockID) {
		super(17, Material.ground);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.wood.blockID;
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? 21 : (side == 0 ? 21 : 20);
	}
}