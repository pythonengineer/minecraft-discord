package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
	protected BlockLeaves(int blockID, int textureIndex) {
		super(18, 52, Material.leaves, true);
		this.setTickOnLoad(true);
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return rand.nextInt(10) == 0 ? 1 : 0;
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.sapling.blockID;
	}
}