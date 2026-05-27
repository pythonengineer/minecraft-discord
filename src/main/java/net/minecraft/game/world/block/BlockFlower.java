package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFlower extends Block {
	protected BlockFlower(int blockID, int textureIndex) {
		super(blockID, Material.plants);
		this.blockIndexInTexture = textureIndex;
		this.setTickOnLoad(true);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 3.0F, 0.5F + f3);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.tilledField.blockID;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		super.onNeighborBlockChange(world, x, y, z, blockID);
		this.checkFlowerChange(world, x, y, z);
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		this.checkFlowerChange(world, x, y, z);
	}

	protected void checkFlowerChange(World world, int x, int y, int z) {
		if(!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return (world.getBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
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
		return 1;
	}
}
