package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSponge extends Block {
	protected BlockSponge(int blockID) {
		super(19, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		for(int i5 = x - 2; i5 <= x + 2; ++i5) {
			for(int i6 = y - 2; i6 <= y + 2; ++i6) {
				for(int i7 = z - 2; i7 <= z + 2; ++i7) {
					world.getBlockMaterial(i5, i6, i7);
				}
			}
		}

	}

	public final void onBlockRemoval(World world, int x, int y, int z) {
		for(int i5 = x - 2; i5 <= x + 2; ++i5) {
			for(int i6 = y - 2; i6 <= y + 2; ++i6) {
				for(int i7 = z - 2; i7 <= z + 2; ++i7) {
					world.notifyBlocksOfNeighborChange(i5, i6, i7, world.getBlockId(i5, i6, i7));
				}
			}
		}

	}
}