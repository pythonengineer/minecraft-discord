package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSponge extends Block {
	protected BlockSponge(int blockID) {
		super(blockID, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		byte b5 = 2;

		for(int i6 = x - b5; i6 <= x + b5; ++i6) {
			for(int i7 = y - b5; i7 <= y + b5; ++i7) {
				for(int i8 = z - b5; i8 <= z + b5; ++i8) {
					if(world.getBlockMaterial(i6, i7, i8) == Material.water) {
						;
					}
				}
			}
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		byte b5 = 2;

		for(int i6 = x - b5; i6 <= x + b5; ++i6) {
			for(int i7 = y - b5; i7 <= y + b5; ++i7) {
				for(int i8 = z - b5; i8 <= z + b5; ++i8) {
					world.notifyBlocksOfNeighborChange(i6, i7, i8, world.getBlockId(i6, i7, i8));
				}
			}
		}

	}
}
