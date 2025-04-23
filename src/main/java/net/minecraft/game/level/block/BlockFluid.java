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

		Block.isBlockContainer[var1] = true;
		this.movingId = var1;
		this.stillId = var1 + 1;
		float var3 = 0.01F;
		float var4 = 0.1F;
		this.setBlockBounds(var3, 0.0F - var4 + var3, var3, var3 + 1.0F, 1.0F - var4 + var3, var3 + 1.0F);
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
		boolean var7 = false;
		var4 = var4;
		var3 = var3;
		var2 = var2;
		var1 = var1;
		BlockFluid var8 = this;
		boolean var9 = false;

		boolean var6;
		do {
			--var3;
			if(var1.getBlockId(var2, var3, var4) != 0 || !var8.canFlow(var1, var2, var3, var4)) {
				break;
			}

			var6 = var1.setBlockWithNotify(var2, var3, var4, var8.movingId);
			if(var6) {
				var9 = true;
			}
		} while(var6 && var8.material != Material.lava);

		++var3;
		if(var8.material == Material.water || !var9) {
			var9 = var9 | var8.flow(var1, var2 - 1, var3, var4) | var8.flow(var1, var2 + 1, var3, var4) | var8.flow(var1, var2, var3, var4 - 1) | var8.flow(var1, var2, var3, var4 + 1);
		}

		if(!var9) {
			var1.setTileNoUpdate(var2, var3, var4, var8.stillId);
		} else {
			var1.scheduleBlockUpdate(var2, var3, var4, var8.movingId);
		}
	}

	private boolean canFlow(World var1, int var2, int var3, int var4) {
		if(this.material == Material.water) {
			for(int var7 = var2 - 2; var7 <= var2 + 2; ++var7) {
				for(int var5 = var3 - 2; var5 <= var3 + 2; ++var5) {
					for(int var6 = var4 - 2; var6 <= var4 + 2; ++var6) {
						if(var1.getBlockId(var7, var5, var6) == Block.sponge.blockID) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean flow(World var1, int var2, int var3, int var4) {
		if(var1.getBlockId(var2, var3, var4) == 0) {
			if(!this.canFlow(var1, var2, var3, var4)) {
				return false;
			}

			if(var1.setBlockWithNotify(var2, var3, var4, this.movingId)) {
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

	public final Material getMaterial() {
		return this.material;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(var5 != 0) {
			Material var6 = Block.blocksList[var5].getMaterial();
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

	public final void dropBlockAsItemWithChance(World var1, float var2) {
	}

	public final void dropBlockAsItem(World var1) {
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}
}
