package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public class BlockLog extends Block {
	protected BlockLog(int blockID) {
		super(blockID, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.wood.blockID;
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? 21 : (side == 0 ? 21 : 20);
	}
}
