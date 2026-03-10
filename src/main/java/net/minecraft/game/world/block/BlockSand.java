package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSand extends Block {
	public BlockSand(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.sand);
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		this.tryToFall(world, x, y, z);
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		this.tryToFall(world, x, y, z);
	}

	private void tryToFall(World world, int x, int y, int z) {
		int i5 = y;

		while(true) {
			int i8 = i5 - 1;
			int i6;
			Material material10;
			if(!((i6 = world.getBlockId(x, i8, z)) == 0 ? true : (i6 == Block.fire.blockID ? true : ((material10 = Block.blocksList[i6].blockMaterial) == Material.water ? true : material10 == Material.lava))) || i5 < 0) {
				if(i5 < 0) {
					world.setBlock(x, y, z, 0);
				}

				if(i5 != y) {
					if((i6 = world.getBlockId(x, i5, z)) > 0 && Block.blocksList[i6].blockMaterial != Material.air) {
						world.setBlock(x, i5, z, 0);
					}

					world.swap(x, y, z, x, i5, z);
				}

				return;
			}

			--i5;
			if(world.getBlockId(x, i5, z) == Block.fire.blockID) {
				world.setBlock(x, i5, z, 0);
			}
		}
	}
}