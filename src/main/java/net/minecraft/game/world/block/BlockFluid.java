package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFluid extends Block {
	protected int stillId;
	protected int movingId;

	protected BlockFluid(int var1, Material var2) {
		super(var1, var2);
		this.blockIndexInTexture = 14;
		if(var2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockContainer[var1] = true;
		this.movingId = var1;
		this.stillId = var1 + 1;
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
    }

	public final float getBlockBrightness(World var1, int var2, int var3, int var4) {
		return this.material == Material.lava ? 100.0F : super.getBlockBrightness(var1, var2, var3, var4);
	}

	public boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		int var6 = var1.getBlockId(var2, var3, var4);
		return var6 != this.movingId && var6 != this.stillId ? (var5 != 1 || var1.getBlockId(var2 - 1, var3, var4) != 0 && var1.getBlockId(var2 + 1, var3, var4) != 0 && var1.getBlockId(var2, var3, var4 - 1) != 0 && var1.getBlockId(var2, var3, var4 + 1) != 0 ? super.shouldSideBeRendered(var1, var2, var3, var4, var5) : true) : false;
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

	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}
}
