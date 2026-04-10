package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.setTickOnLoad(false);
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		int i6 = world.getBlockMetadata(x, y, z);
		world.setBlock(x, y, z, this.blockID - 1);
		world.setBlockAndMetadata(x, y, z, i6);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this.blockID - 1);
	}
}