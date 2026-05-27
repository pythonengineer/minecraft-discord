package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockGears extends Block {
	protected BlockGears(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 5;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}

	public boolean isCollidable() {
		return false;
	}
}
