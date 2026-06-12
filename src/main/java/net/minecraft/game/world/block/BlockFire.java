package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookshelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);
        this.initializeBlock(Block.cloth.blockID, 30, 60);
		this.setTickOnLoad(true);
	}

	private void initializeBlock(int blockID, int fireEncourageChance, int catchFireAbility) {
		this.chanceToEncourageFire[blockID] = fireEncourageChance;
		this.abilityToCatchFire[blockID] = catchFireAbility;
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
		return 3;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public int tickRate() {
		return 10;
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(i6 < 15) {
			world.setBlockMetadataWithNotify(x, y, z, i6 + 1);
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

		if(!this.canNeighborBurn(world, x, y, z)) {
			if(!world.isBlockNormalCube(x, y - 1, z) || i6 > 3) {
				world.setBlockWithNotify(x, y, z, 0);
			}

		} else if(!this.canBlockCatchFire(world, x, y - 1, z) && i6 == 15 && rand.nextInt(4) == 0) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			if(i6 % 2 == 0 && i6 > 2) {
				this.tryToCatchBlockOnFire(world, x + 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(world, x - 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(world, x, y - 1, z, 100, rand);
				this.tryToCatchBlockOnFire(world, x, y + 1, z, 200, rand);
				this.tryToCatchBlockOnFire(world, x, y, z - 1, 300, rand);
				this.tryToCatchBlockOnFire(world, x, y, z + 1, 300, rand);

				for(int i7 = x - 1; i7 <= x + 1; ++i7) {
					for(int i8 = z - 1; i8 <= z + 1; ++i8) {
						for(int i9 = y - 1; i9 <= y + 4; ++i9) {
							if(i7 != x || i9 != y || i8 != z) {
								int i10 = 100;
								if(i9 > y + 1) {
									i10 += (i9 - (y + 1)) * 100;
								}

								int i11 = this.getChanceOfNeighborsEncouragingFire(world, i7, i9, i8);
								if(i11 > 0 && rand.nextInt(i10) <= i11) {
									world.setBlockWithNotify(i7, i9, i8, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world, int x, int y, int z, int catchFireAbility, EaglercraftRandom rand) {
		int i7 = this.abilityToCatchFire[world.getBlockId(x, y, z)];
		if(rand.nextInt(catchFireAbility) < i7) {
			boolean z8 = world.getBlockId(x, y, z) == Block.tnt.blockID;
			if(rand.nextInt(2) == 0) {
				world.setBlockWithNotify(x, y, z, this.blockID);
			} else {
				world.setBlockWithNotify(x, y, z, 0);
			}

			if(z8) {
				Block.tnt.onBlockDestroyedByPlayer(world, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canBlockCatchFire(world, x + 1, y, z) ? true : (this.canBlockCatchFire(world, x - 1, y, z) ? true : (this.canBlockCatchFire(world, x, y - 1, z) ? true : (this.canBlockCatchFire(world, x, y + 1, z) ? true : (this.canBlockCatchFire(world, x, y, z - 1) ? true : this.canBlockCatchFire(world, x, y, z + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
		byte b5 = 0;
		if(world.getBlockId(x, y, z) != 0) {
			return 0;
		} else {
			int i6 = this.getChanceToEncourageFire(world, x + 1, y, z, b5);
			i6 = this.getChanceToEncourageFire(world, x - 1, y, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y - 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y + 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z - 1, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z + 1, i6);
			return i6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess iBlockAccess, int x, int y, int z) {
		return this.chanceToEncourageFire[iBlockAccess.getBlockId(x, y, z)] > 0;
	}

	public int getChanceToEncourageFire(World world, int x, int y, int z, int fireEncourageChance) {
		int i6 = this.chanceToEncourageFire[world.getBlockId(x, y, z)];
		return i6 > fireEncourageChance ? i6 : fireEncourageChance;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}
	}

	public void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(rand.nextInt(24) == 0) {
			world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
		}

		int i6;
		float f7;
		float f8;
		float f9;
		if(!world.isBlockNormalCube(x, y - 1, z) && !Block.fire.canBlockCatchFire(world, x, y - 1, z)) {
			if(Block.fire.canBlockCatchFire(world, x - 1, y, z)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat() * 0.1F;
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat();
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world, x + 1, y, z)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)(x + 1) - rand.nextFloat() * 0.1F;
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat();
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world, x, y, z - 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat();
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world, x, y, z + 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat();
					f8 = (float)y + rand.nextFloat();
					f9 = (float)(z + 1) - rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world, x, y + 1, z)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat();
					f8 = (float)(y + 1) - rand.nextFloat() * 0.1F;
					f9 = (float)z + rand.nextFloat();
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

		} else {
			for(i6 = 0; i6 < 3; ++i6) {
				f7 = (float)x + rand.nextFloat();
				f8 = (float)y + rand.nextFloat() * 0.5F + 0.5F;
				f9 = (float)z + rand.nextFloat();
				world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
			}

		}
	}
}
