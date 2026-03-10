package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.movingBlockId = i1 - 1;
		this.stillBlockId = i1;
		this.setTickOnLoad(false);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		boolean z6 = false;
		if(this.canFlow(world, x, y - 1, z)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, x - 1, y, z)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, x + 1, y, z)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, x, y, z - 1)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, x, y, z + 1)) {
			z6 = true;
		}

		if(blockID != 0) {
			Material material7 = Block.blocksList[blockID].blockMaterial;
			if(this.blockMaterial == Material.water && material7 == Material.lava || material7 == Material.water && this.blockMaterial == Material.lava) {
				world.notifyBlockChange(x, y, z, Block.stone.blockID);
				return;
			}
		}

		if(Block.fire.getChanceOfNeighborsEncouragingFire(blockID)) {
			z6 = true;
		}

		if(z6) {
			world.setBlock(x, y, z, this.movingBlockId);
			world.scheduleBlockUpdate(x, y, z, this.movingBlockId);
		}

	}
}