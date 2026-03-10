package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFlowing extends BlockFluid {
	private int stillBlockId;
	private int movingBlockId;

	protected BlockFlowing(int i1, Material material2) {
		super(i1, material2);
		new EaglercraftRandom();
		int[] i10000 = new int[]{0, 1, 2, 3};
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.canBlockGrass[i1] = true;
		this.movingBlockId = i1;
		this.stillBlockId = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.movingBlockId);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		boolean world1 = false;
		boolean z10000 = false;
	}

	public final boolean update(World world, int x, int y, int z, int rand) {
		return false;
	}

	public final boolean getIsBlockSolid(World world, int x, int y, int z, int metadata) {
		int i6;
		return (i6 = world.getBlockId(x, y, z)) != this.movingBlockId && i6 != this.stillBlockId ? (metadata != 1 || world.getBlockId(x - 1, y, z) != 0 && world.getBlockId(x + 1, y, z) != 0 && world.getBlockId(x, y, z - 1) != 0 && world.getBlockId(x, y, z + 1) != 0 ? super.getIsBlockSolid(world, x, y, z, metadata) : true) : false;
	}

	public final boolean isCollidable() {
		return false;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
	}

	public final int tickRate() {
		return this.blockMaterial == Material.lava ? 25 : 5;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.blockMaterial == Material.water ? 1 : 0;
	}
}