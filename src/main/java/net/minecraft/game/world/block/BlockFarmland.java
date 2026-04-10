package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFarmland extends Block {
	protected BlockFarmland(int blockID) {
		super(60, Material.grassMaterial);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return side == 1 && metadata > 0 ? this.blockIndexInTexture - 1 : (side == 1 ? this.blockIndexInTexture : 2);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(rand.nextInt(5) == 0) {
			int i8 = z;
			int i7 = y;
			int i6 = x;
			World world12 = world;
			int i9 = x - 4;

			int i10;
			int i11;
			boolean z10000;
			label69:
			while(true) {
				if(i9 > i6 + 4) {
					z10000 = false;
					break;
				}

				for(i10 = i7; i10 <= i7 + 1; ++i10) {
					for(i11 = i8 - 4; i11 <= i8 + 4; ++i11) {
						if(world12.getBlockMaterial(i9, i10, i11) == Material.water) {
							z10000 = true;
							break label69;
						}
					}
				}

				++i9;
			}

			if(z10000) {
				world.setBlockMetadata(x, y, z, 7);
				return;
			}

			int i13;
			if((i13 = world.getBlockMetadata(x, y, z)) > 0) {
				world.setBlockMetadata(x, y, z, i13 - 1);
				return;
			}

			i8 = z;
			i7 = y;
			i6 = x;
			world12 = world;
			i10 = x;

			label49:
			while(true) {
				if(i10 > i6) {
					z10000 = false;
					break;
				}

				for(i11 = i8; i11 <= i8; ++i11) {
					if(world12.getBlockId(i10, i7 + 1, i11) == Block.crops.blockID) {
						z10000 = true;
						break label49;
					}
				}

				++i10;
			}

			if(!z10000) {
				world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
			}
		}

	}

	public final void onEntityWalking(World world, int x, int y, int z) {
		if(world.rand.nextInt(4) == 0) {
			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		super.onNeighborBlockChange(world, x, y, z, blockID);
		if(world.getBlockMaterial(x, y + 1, z).isSolid()) {
			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		}

	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.dirt.idDropped(0, rand);
	}
}