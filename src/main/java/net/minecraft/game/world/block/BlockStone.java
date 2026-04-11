package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public class BlockStone extends Block {
	public BlockStone(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.rock);
	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.cobblestone.blockID;
	}
}