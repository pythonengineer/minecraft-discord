package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.setTickOnLoad(false);
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        super.onNeighborBlockChange(world, x, y, z, blockID);
        if(world.getBlockId(x, y, z) == this.blockID) {
            this.setNotStationary(world, x, y, z);
        }

    }

    private void setNotStationary(World world, int x, int y, int z) {
        int i5 = world.getBlockMetadata(x, y, z);
        world.editingBlocks = true;
        world.setBlockAndMetadata(x, y, z, this.blockID - 1, i5);
        world.markBlocksDirty(x, y, z, x, y, z);
        world.scheduleBlockUpdate(x, y, z, this.blockID - 1);
        world.editingBlocks = false;
    }
}
