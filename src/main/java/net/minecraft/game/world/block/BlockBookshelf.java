package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.wood);
	}

	public int getBlockTextureFromSide(int side) {
		return side <= 1 ? 4 : this.blockIndexInTexture;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}
}
