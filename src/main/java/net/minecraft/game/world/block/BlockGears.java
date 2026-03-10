package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockGears extends Block {
	protected BlockGears(int blockID, int textureIndex) {
		super(55, 62, Material.circuits);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 5;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}

	public final boolean isCollidable() {
		return false;
	}
}