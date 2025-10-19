package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFluid extends Block {
	protected int a;
	protected int b;

	protected BlockFluid(int var1, Material var2) {
		super(var1, var2);
		this.blockIndexInTexture = 14;
		if(var2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockContainer[var1] = true;
		this.b = var1;
		this.a = var1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
		this.setResistance(2.0F);
	}

	public final int getBlockTextureFromSide(int var1) {
		return this.material == Material.lava ? this.blockIndexInTexture : (var1 == 1 ? this.blockIndexInTexture : (var1 == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.b);
	}

	public void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		this.e(var1, var2, var3, var4, 0);
	}

	public boolean e(World var1, int var2, int var3, int var4, int var5) {
		boolean var7 = false;
		h(var1, var2, var3, var4);

		boolean var6;
		do {
			--var3;
			if(!this.e(var1, var2, var3, var4)) {
				break;
			}

			h(var1, var2, var3, var4);
			var6 = var1.setBlockWithNotify(var2, var3, var4, this.b);
			if(var6) {
				var7 = true;
			}
		} while(var6 && this.material != Material.lava);

		++var3;
		if(this.material == Material.water || !var7) {
			var7 |= this.j(var1, var2 - 1, var3, var4);
			var7 |= this.j(var1, var2 + 1, var3, var4);
			var7 |= this.j(var1, var2, var3, var4 - 1);
			var7 |= this.j(var1, var2, var3, var4 + 1);
		}

		if(this.material == Material.lava) {
			var7 |= i(var1, var2 - 1, var3, var4);
			var7 |= i(var1, var2 + 1, var3, var4);
			var7 |= i(var1, var2, var3, var4 - 1);
			var7 |= i(var1, var2, var3, var4 + 1);
		}

		if(!var7) {
			var1.setTileNoUpdate(var2, var3, var4, this.a);
		} else {
			var1.scheduleBlockUpdate(var2, var3, var4, this.b);
		}

		return var7;
	}

	private static boolean h(World var0, int var1, int var2, int var3) {
		return var0.getBlockMaterial(var1, var2 - 1, var3).e();
	}

	protected final boolean e(World var1, int var2, int var3, int var4) {
		if(!var1.getBlockMaterial(var2, var3, var4).e()) {
			return false;
		} else {
			if(this.material == Material.water) {
				for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
					for(int var6 = var3 - 2; var6 <= var3 + 2; ++var6) {
						for(int var7 = var4 - 2; var7 <= var4 + 2; ++var7) {
							if(var1.getBlockId(var5, var6, var7) == Block.sponge.blockID) {
								return false;
							}
						}
					}
				}
			}

			return true;
		}
	}

	private static boolean i(World var0, int var1, int var2, int var3) {
		if(Block.fire.b(var0.getBlockId(var1, var2, var3))) {
			Block.fire.h(var0, var1, var2, var3);
			return true;
		} else {
			return false;
		}
	}

	private boolean j(World var1, int var2, int var3, int var4) {
		if(!this.e(var1, var2, var3, var4)) {
			return false;
		} else {
			boolean var5 = var1.setBlockWithNotify(var2, var3, var4, this.b);
			if(var5) {
				var1.scheduleBlockUpdate(var2, var3, var4, this.b);
			}

			return false;
		}
	}

	public final float getBlockBrightness(World var1, int var2, int var3, int var4) {
		return this.material == Material.lava ? 100.0F : super.getBlockBrightness(var1, var2, var3, var4);
	}

	public boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		int var6 = var1.getBlockId(var2, var3, var4);
		return var6 != this.b && var6 != this.a ? (var5 != 1 || var1.getBlockId(var2 - 1, var3, var4) != 0 && var1.getBlockId(var2 + 1, var3, var4) != 0 && var1.getBlockId(var2, var3, var4 - 1) != 0 && var1.getBlockId(var2, var3, var4 + 1) != 0 ? super.shouldSideBeRendered(var1, var2, var3, var4, var5) : true) : false;
	}

	public boolean isCollidable() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(var5 != 0) {
			Material var6 = Block.blocksList[var5].material;
			if(this.material == Material.water && var6 == Material.lava || var6 == Material.water && this.material == Material.lava) {
				var1.setBlockWithNotify(var2, var3, var4, Block.stone.blockID);
			}
		}

		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID);
	}

	public int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}

	public final void randomDisplayTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		if(var5.nextInt(128) == -1 && var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
			if(this.material == Material.lava) {
				var1.playSoundEffect((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), "liquid.lava", var5.nextFloat() * 0.25F + 12.0F / 16.0F, var5.nextFloat() * 0.5F + 0.3F);
			}

			if(this.material == Material.water) {
				var1.playSoundEffect((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), "liquid.water", var5.nextFloat() * 0.25F + 12.0F / 16.0F, var5.nextFloat() + 0.5F);
			}
		}

		if(this.material == Material.lava && var1.getBlockMaterial(var2, var3 + 1, var4) == Material.air && !var1.isBlockNormalCube(var2, var3 + 1, var4) && var5.nextInt(100) == 0) {
			double var6 = (double)((float)var2 + var5.nextFloat());
			double var8 = (double)var3 + this.maxY;
			double var10 = (double)((float)var4 + var5.nextFloat());
			var1.spawnParticle("lava", var6, var8, var10, 0.0D, 0.0D, 0.0D);
		}

		if(this.material == Material.water) {
			int var7;
			if(k(var1, var2 + 1, var3, var4)) {
				for(var7 = 0; var7 < 4; ++var7) {
					var1.spawnParticle("splash", (double)((float)(var2 + 1) + 2.0F / 16.0F), (double)var3, (double)((float)var4 + var5.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(k(var1, var2 - 1, var3, var4)) {
				for(var7 = 0; var7 < 4; ++var7) {
					var1.spawnParticle("splash", (double)((float)var2 - 2.0F / 16.0F), (double)var3, (double)((float)var4 + var5.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(k(var1, var2, var3, var4 + 1)) {
				for(var7 = 0; var7 < 4; ++var7) {
					var1.spawnParticle("splash", (double)((float)var2 + var5.nextFloat()), (double)var3, (double)((float)(var4 + 1) + 2.0F / 16.0F), 0.0D, 0.0D, 0.0D);
				}
			}

			if(k(var1, var2, var3, var4 - 1)) {
				for(var7 = 0; var7 < 4; ++var7) {
					var1.spawnParticle("splash", (double)((float)var2 + var5.nextFloat()), (double)var3, (double)((float)var4 - 2.0F / 16.0F), 0.0D, 0.0D, 0.0D);
				}
			}
		}

	}

	private static boolean k(World var0, int var1, int var2, int var3) {
		Material var4 = var0.getBlockMaterial(var1, var2, var3);
		Material var5 = var0.getBlockMaterial(var1, var2 - 1, var3);
		return !var4.getCanBlockGrass() && !var4.getIsLiquid() ? var5.getCanBlockGrass() || var5.getIsLiquid() : false;
	}
}
