package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFluid extends Block {
	protected Material material;
	protected int stillId;
	protected int movingId;

	protected BlockFluid(int var1, Material var2) {
		super(var1);
		this.material = var2;
		this.blockIndexInTexture = 14;
		if(var2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockFluid[var1] = true;
		this.movingId = var1;
		this.stillId = var1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
	}

	public final int getBlockTexture(int var1) {
		return this.material == Material.lava ? this.blockIndexInTexture : (var1 == 1 ? this.blockIndexInTexture : (var1 == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final void onBlockPlaced(World var1, int var2, int var3, int var4) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.movingId);
	}

	public void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		boolean var8 = false;
		int var11 = var4;
		var4 = var3;
		var3 = var2;
		World var10 = var1;
		BlockFluid var9 = this;
		boolean var6 = false;

		boolean var7;
		do {
			--var4;
			if(var10.getBlockId(var3, var4, var11) != 0 || !var9.h(var10, var3, var4, var11)) {
				break;
			}

			var7 = var10.setBlockWithNotify(var3, var4, var11, var9.movingId);
			if(var7) {
				var6 = true;
			}
		} while(var7 && var9.material != Material.lava);

		++var4;
		if(var9.material == Material.water || !var6) {
			var6 |= var9.i(var10, var3 - 1, var4, var11);
			var6 |= var9.i(var10, var3 + 1, var4, var11);
			var6 |= var9.i(var10, var3, var4, var11 - 1);
			var6 |= var9.i(var10, var3, var4, var11 + 1);
		}

		if(!var6) {
			var10.setTileNoUpdate(var3, var4, var11, var9.stillId);
		} else {
			var10.scheduleBlockUpdate(var3, var4, var11, var9.movingId);
		}

	}

	private boolean h(World var1, int var2, int var3, int var4) {
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

	private boolean i(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockId(var2, var3, var4);
		if(var5 == 0) {
			if(!this.h(var1, var2, var3, var4)) {
				return false;
			}

			boolean var6 = var1.setBlockWithNotify(var2, var3, var4, this.movingId);
			if(var6) {
				var1.scheduleBlockUpdate(var2, var3, var4, this.movingId);
			}
		}

		return false;
	}

	public final float getBlockBrightness(World var1, int var2, int var3, int var4) {
		return this.material == Material.lava ? 100.0F : var1.getBlockLightValue(var2, var3, var4);
	}

	public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		if(var2 >= 0 && var3 >= 0 && var4 >= 0 && var2 < var1.width && var4 < var1.length) {
			int var6 = var1.getBlockId(var2, var3, var4);
			return var6 != this.movingId && var6 != this.stillId ? (var5 != 1 || var1.getBlockId(var2 - 1, var3, var4) != 0 && var1.getBlockId(var2 + 1, var3, var4) != 0 && var1.getBlockId(var2, var3, var4 - 1) != 0 && var1.getBlockId(var2, var3, var4 + 1) != 0 ? super.shouldSideBeRendered(var1, var2, var3, var4, var5) : true) : false;
		} else {
			return false;
		}
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final Material getBlockMaterial() {
		return this.material;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(var5 != 0) {
			Material var6 = Block.blocksList[var5].getBlockMaterial();
			if(this.material == Material.water && var6 == Material.lava || var6 == Material.water && this.material == Material.lava) {
				var1.setBlockWithNotify(var2, var3, var4, Block.stone.blockID);
				return;
			}
		}

		var1.scheduleBlockUpdate(var2, var3, var4, var5);
	}

	public final int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public final void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, float var5) {
	}

	public final void dropBlockAsItem(World var1, int var2, int var3, int var4) {
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}
}
