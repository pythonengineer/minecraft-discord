package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int blockID, int textureIndex) {
		super(51, 31, Material.fire);
		this.setBurnRate(Block.planks.blockID, 5, 20);
		this.setBurnRate(Block.wood.blockID, 5, 5);
		this.setBurnRate(Block.leaves.blockID, 30, 60);
		this.setBurnRate(Block.bookshelf.blockID, 30, 20);
		this.setBurnRate(Block.tnt.blockID, 15, 100);

		for(blockID = 0; blockID < 16; ++blockID) {
			this.setBurnRate(Block.clothRed.blockID + blockID, 30, 60);
		}

		this.setTickOnLoad(true);
	}

	private void setBurnRate(int blockID, int fireEncourageChance, int catchFireAbility) {
		this.chanceToEncourageFire[blockID] = fireEncourageChance;
		this.abilityToCatchFire[blockID] = catchFireAbility;
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
		return 3;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public final int tickRate() {
		return 20;
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int i6;
		if((i6 = world.getBlockMetadata(x, y, z)) < 15) {
			world.setBlockMetadata(x, y, z, i6 + 1);
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

		if(!this.canNeighborBurn(world, x, y, z)) {
			if(!world.isBlockNormalCube(x, y - 1, z) || i6 > 3) {
				world.notifyBlockChange(x, y, z, 0);
			}

		} else if(!this.getChanceToEncourageFire(world, x, y - 1, z) && i6 == 15 && rand.nextInt(4) == 0) {
			world.notifyBlockChange(x, y, z, 0);
		} else {
			if(i6 % 5 == 0 && i6 > 5) {
				this.tryToCatchBlockOnFire(world, x + 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(world, x - 1, y, z, 300, rand);
				this.tryToCatchBlockOnFire(world, x, y - 1, z, 100, rand);
				this.tryToCatchBlockOnFire(world, x, y + 1, z, 200, rand);
				this.tryToCatchBlockOnFire(world, x, y, z - 1, 300, rand);
				this.tryToCatchBlockOnFire(world, x, y, z + 1, 300, rand);

				for(i6 = x - 1; i6 <= x + 1; ++i6) {
					for(int i7 = z - 1; i7 <= z + 1; ++i7) {
						for(int i8 = y - 1; i8 <= y + 4; ++i8) {
							if(i6 != x || i8 != y || i7 != z) {
								int i9 = 100;
								if(i8 > y + 1) {
									i9 = 100 + (i8 - (y + 1)) * 100;
								}

								int i10000;
								if(world.getBlockId(i6, i8, i7) != 0) {
									i10000 = 0;
								} else {
									int i15 = this.getChanceToEncourageFire(world, i6 + 1, i8, i7, 0);
									i15 = this.getChanceToEncourageFire(world, i6 - 1, i8, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i6, i8 - 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i6, i8 + 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i6, i8, i7 - 1, i15);
									i10000 = this.getChanceToEncourageFire(world, i6, i8, i7 + 1, i15);
								}

								int i10 = i10000;
								if(i10000 > 0 && rand.nextInt(i9) <= i10) {
									world.notifyBlockChange(i6, i8, i7, this.blockID);
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
			boolean catchFireAbility1 = world.getBlockId(x, y, z) == Block.tnt.blockID;
			if(rand.nextInt(2) == 0) {
				world.notifyBlockChange(x, y, z, this.blockID);
			} else {
				world.notifyBlockChange(x, y, z, 0);
			}

			if(catchFireAbility1) {
				Block.tnt.onBlockDestroyedByPlayer(world, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.getChanceToEncourageFire(world, x + 1, y, z) ? true : (this.getChanceToEncourageFire(world, x - 1, y, z) ? true : (this.getChanceToEncourageFire(world, x, y - 1, z) ? true : (this.getChanceToEncourageFire(world, x, y + 1, z) ? true : (this.getChanceToEncourageFire(world, x, y, z - 1) ? true : this.getChanceToEncourageFire(world, x, y, z + 1)))));
	}

	public final boolean isCollidable() {
		return false;
	}

	public final boolean getChanceToEncourageFire(World world, int x, int y, int z) {
		return this.chanceToEncourageFire[world.getBlockId(x, y, z)] > 0;
	}

	private int getChanceToEncourageFire(World world, int x, int y, int z, int fireEncourageChance) {
		int world1;
		return (world1 = this.chanceToEncourageFire[world.getBlockId(x, y, z)]) > fireEncourageChance ? world1 : fireEncourageChance;
	}

	public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
	}

	public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.notifyBlockChange(x, y, z, 0);
		}
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.notifyBlockChange(x, y, z, 0);
		} else {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}
	}

	public final boolean getChanceOfNeighborsEncouragingFire(int blockId) {
		return this.chanceToEncourageFire[blockId] > 0;
	}

	public final void fireSpread(World world, int x, int y, int z) {
		boolean z5 = false;
		if(!(z5 = fireCheck(world, x, y + 1, z))) {
			z5 = fireCheck(world, x - 1, y, z);
		}

		if(!z5) {
			z5 = fireCheck(world, x + 1, y, z);
		}

		if(!z5) {
			z5 = fireCheck(world, x, y, z - 1);
		}

		if(!z5) {
			z5 = fireCheck(world, x, y, z + 1);
		}

		if(!z5) {
			z5 = fireCheck(world, x, y - 1, z);
		}

		if(!z5) {
			world.notifyBlockChange(x, y, z, Block.fire.blockID);
		}

	}

	public final void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(rand.nextInt(24) == 0) {
			world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
		}

		int i6;
		float f7;
		float f8;
		float f9;
		if(!world.isBlockNormalCube(x, y - 1, z) && !Block.fire.getChanceToEncourageFire(world, x, y - 1, z)) {
			if(Block.fire.getChanceToEncourageFire(world, x - 1, y, z)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat() * 0.1F;
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat();
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.getChanceToEncourageFire(world, x + 1, y, z)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)(x + 1) - rand.nextFloat() * 0.1F;
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat();
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.getChanceToEncourageFire(world, x, y, z - 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat();
					f8 = (float)y + rand.nextFloat();
					f9 = (float)z + rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.getChanceToEncourageFire(world, x, y, z + 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)x + rand.nextFloat();
					f8 = (float)y + rand.nextFloat();
					f9 = (float)(z + 1) - rand.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.getChanceToEncourageFire(world, x, y + 1, z)) {
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

	private static boolean fireCheck(World world, int x, int y, int z) {
		int i4;
		if((i4 = world.getBlockId(x, y, z)) == Block.fire.blockID) {
			return true;
		} else if(i4 == 0) {
			world.notifyBlockChange(x, y, z, Block.fire.blockID);
			return true;
		} else {
			return false;
		}
	}
}