package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockBookshelf extends Block {
	public BlockBookshelf(int blockID, int textureIndex) {
		super(47, 35, Material.wood);
	}

	public final int getBlockTextureFromSide(int side) {
		return side <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}
}