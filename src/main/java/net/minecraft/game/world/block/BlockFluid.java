package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFluid extends Block {
	protected int stillBlockId;
	protected int movingBlockId;

	protected BlockFluid(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.canBlockGrass[i1] = true;
		this.movingBlockId = i1;
		this.stillBlockId = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
		this.setResistance(2.0F);
	}

	public final int getBlockTextureFromSide(int side) {
		return this.blockMaterial == Material.lava ? this.blockIndexInTexture : (side == 1 ? this.blockIndexInTexture : (side == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.movingBlockId);
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		this.update(world, x, y, z, 0);
	}

	public boolean update(World world, int x, int y, int z, int rand) {
		boolean z7 = false;
		liquidSolidCheck(world, x, y, z);

		boolean z6;
		do {
			--y;
			if(!this.canFlow(world, x, y, z)) {
				break;
			}

			liquidSolidCheck(world, x, y, z);
			if(z6 = world.notifyBlockChange(x, y, z, this.movingBlockId)) {
				z7 = true;
			}
		} while(z6 && this.blockMaterial != Material.lava);

		++y;
		if(this.blockMaterial == Material.water || !z7) {
			z7 = z7 | this.flow(world, x - 1, y, z) | this.flow(world, x + 1, y, z) | this.flow(world, x, y, z - 1) | this.flow(world, x, y, z + 1);
		}

		if(this.blockMaterial == Material.lava) {
			z7 = z7 | extinguishFireLava(world, x - 1, y, z) | extinguishFireLava(world, x + 1, y, z) | extinguishFireLava(world, x, y, z - 1) | extinguishFireLava(world, x, y, z + 1);
		}

		if(!z7) {
			world.setBlock(x, y, z, this.stillBlockId);
		} else {
			world.scheduleBlockUpdate(x, y, z, this.movingBlockId);
		}

		return z7;
	}

	private static boolean liquidSolidCheck(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y - 1, z).liquidSolidCheck();
	}

	protected final boolean canFlow(World world, int x, int y, int z) {
		if(!world.getBlockMaterial(x, y, z).liquidSolidCheck()) {
			return false;
		} else {
			if(this.blockMaterial == Material.water) {
				for(int i5 = x - 2; i5 <= x + 2; ++i5) {
					for(int i6 = y - 2; i6 <= y + 2; ++i6) {
						for(int i7 = z - 2; i7 <= z + 2; ++i7) {
							if(world.getBlockId(i5, i6, i7) == Block.sponge.blockID) {
								return false;
							}
						}
					}
				}
			}

			return true;
		}
	}

	private static boolean extinguishFireLava(World world, int x, int y, int z) {
		if(Block.fire.getChanceOfNeighborsEncouragingFire(world.getBlockId(x, y, z))) {
			Block.fire.fireSpread(world, x, y, z);
			return true;
		} else {
			return false;
		}
	}

	private boolean flow(World world, int x, int y, int z) {
		if(!this.canFlow(world, x, y, z)) {
			return false;
		} else {
			if(world.notifyBlockChange(x, y, z, this.movingBlockId)) {
				world.scheduleBlockUpdate(x, y, z, this.movingBlockId);
			}

			return false;
		}
	}

	public final float getBlockBrightness(World world, int x, int y, int z) {
		return this.blockMaterial == Material.lava ? 100.0F : super.getBrightness(world, x, y, z);
	}

	public boolean getIsBlockSolid(World world, int x, int y, int z, int metadata) {
		int i6;
		return (i6 = world.getBlockId(x, y, z)) != this.movingBlockId && i6 != this.stillBlockId ? (metadata != 1 || world.getBlockId(x - 1, y, z) != 0 && world.getBlockId(x + 1, y, z) != 0 && world.getBlockId(x, y, z - 1) != 0 && world.getBlockId(x, y, z + 1) != 0 ? super.getIsBlockSolid(world, x, y, z, metadata) : true) : false;
	}

	public boolean isCollidable() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		if(blockID != 0) {
			Material blockID1 = Block.blocksList[blockID].blockMaterial;
			if(this.blockMaterial == Material.water && blockID1 == Material.lava || blockID1 == Material.water && this.blockMaterial == Material.lava) {
				world.notifyBlockChange(x, y, z, Block.stone.blockID);
			}
		}

		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public int tickRate() {
		return this.blockMaterial == Material.lava ? 25 : 5;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public int getRenderBlockPass() {
		return this.blockMaterial == Material.water ? 1 : 0;
	}

	public final void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(rand.nextInt(128) == -1 && world.getBlockMaterial(x, y + 1, z).getIsSolid()) {
			if(this.blockMaterial == Material.lava) {
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() * 0.5F + 0.3F);
			}

			if(this.blockMaterial == Material.water) {
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F);
			}
		}

		if(this.blockMaterial == Material.lava && world.getBlockMaterial(x, y + 1, z) == Material.air && !world.isBlockNormalCube(x, y + 1, z) && rand.nextInt(100) == 0) {
			double d6 = (double)((float)x + rand.nextFloat());
			double d8 = (double)y + this.maxY;
			double d10 = (double)((float)z + rand.nextFloat());
			world.spawnParticle("lava", d6, d8, d10, 0.0D, 0.0D, 0.0D);
		}

		if(this.blockMaterial == Material.water) {
			int i7;
			if(liquidAirCheck(world, x + 1, y, z)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world.spawnParticle("splash", (double)((float)(x + 1) + 0.125F), (double)y, (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world, x - 1, y, z)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world.spawnParticle("splash", (double)((float)x - 0.125F), (double)y, (double)((float)z + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world, x, y, z + 1)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world.spawnParticle("splash", (double)((float)x + rand.nextFloat()), (double)y, (double)((float)(z + 1) + 0.125F), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world, x, y, z - 1)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world.spawnParticle("splash", (double)((float)x + rand.nextFloat()), (double)y, (double)((float)z - 0.125F), 0.0D, 0.0D, 0.0D);
				}
			}
		}

	}

	private static boolean liquidAirCheck(World world, int x, int y, int z) {
		Material material4 = world.getBlockMaterial(x, y, z);
		Material world1 = world.getBlockMaterial(x, y - 1, z);
		return !material4.getIsSolid() && !material4.getIsLiquid() ? world1.getIsSolid() || world1.getIsLiquid() : false;
	}
}