package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTorch extends Block {
	protected BlockTorch(int blockID, int textureIndex) {
		super(50, 80, Material.circuits);
		this.setTickOnLoad(true);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 2;
	}

	public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : (world.isBlockNormalCube(x, y, z + 1) ? true : world.isBlockNormalCube(x, y - 1, z))));
	}

	public final void onBlockPlaced(World world, int x, int y, int z, int side) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(side == 1 && world.isBlockNormalCube(x, y - 1, z)) {
			i6 = 5;
		}

		if(side == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			i6 = 4;
		}

		if(side == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			i6 = 3;
		}

		if(side == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			i6 = 2;
		}

		if(side == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			i6 = 1;
		}

		world.setBlockMetadata(x, y, z, i6);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
		if(world.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(world, x, y, z);
		}

	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		if(world.isBlockNormalCube(x - 1, y, z)) {
			world.setBlockMetadata(x, y, z, 1);
		} else if(world.isBlockNormalCube(x + 1, y, z)) {
			world.setBlockMetadata(x, y, z, 2);
		} else if(world.isBlockNormalCube(x, y, z - 1)) {
			world.setBlockMetadata(x, y, z, 3);
		} else if(world.isBlockNormalCube(x, y, z + 1)) {
			world.setBlockMetadata(x, y, z, 4);
		} else if(world.isBlockNormalCube(x, y - 1, z)) {
			world.setBlockMetadata(x, y, z, 5);
		}

		this.checkIfAttachedToBlock(world, x, y, z);
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(this.checkIfAttachedToBlock(world, x, y, z)) {
			blockID = world.getBlockMetadata(x, y, z);
			boolean z6 = false;
			if(!world.isBlockNormalCube(x - 1, y, z) && blockID == 1) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(x + 1, y, z) && blockID == 2) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(x, y, z - 1) && blockID == 3) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(x, y, z + 1) && blockID == 4) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(x, y - 1, z) && blockID == 5) {
				z6 = true;
			}

			if(z6) {
				this.harvestBlock(world, x, y, z, world.getBlockMetadata(x, y, z));
				world.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.harvestBlock(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public final MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		int i7;
		if((i7 = world.getBlockMetadata(x, y, z)) == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
		} else if(i7 == 2) {
			this.setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
		} else if(i7 == 3) {
			this.setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
		} else if(i7 == 4) {
			this.setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
		} else {
			this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
		}

		return super.collisionRayTrace(world, x, y, z, vector1, vector2);
	}

	public final void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int rand1 = world.getBlockMetadata(x, y, z);
		float x1 = (float)x + 0.5F;
		float y1 = (float)y + 0.7F;
		float z1 = (float)z + 0.5F;
		if(rand1 == 1) {
			world.spawnParticle("smoke", (double)(x1 - 0.27F), (double)(y1 + 0.22F), (double)z1, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", (double)(x1 - 0.27F), (double)(y1 + 0.22F), (double)z1, 0.0D, 0.0D, 0.0D);
		} else if(rand1 == 2) {
			world.spawnParticle("smoke", (double)(x1 + 0.27F), (double)(y1 + 0.22F), (double)z1, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", (double)(x1 + 0.27F), (double)(y1 + 0.22F), (double)z1, 0.0D, 0.0D, 0.0D);
		} else if(rand1 == 3) {
			world.spawnParticle("smoke", (double)x1, (double)(y1 + 0.22F), (double)(z1 - 0.27F), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", (double)x1, (double)(y1 + 0.22F), (double)(z1 - 0.27F), 0.0D, 0.0D, 0.0D);
		} else if(rand1 == 4) {
			world.spawnParticle("smoke", (double)x1, (double)(y1 + 0.22F), (double)(z1 + 0.27F), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", (double)x1, (double)(y1 + 0.22F), (double)(z1 + 0.27F), 0.0D, 0.0D, 0.0D);
		} else {
			world.spawnParticle("smoke", (double)x1, (double)y1, (double)z1, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", (double)x1, (double)y1, (double)z1, 0.0D, 0.0D, 0.0D);
		}
	}
}