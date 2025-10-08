package net.minecraft.game.world.block;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFlower extends Block {
	protected BlockFlower(int var1, int var2) {
		super(var1, Material.plants);
		this.blockIndexInTexture = var2;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return this.canThisPlantGrowOnThisBlockID(var1.getBlockId(var2, var3 - 1, var4));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int var1) {
		return var1 == Block.grass.blockID || var1 == Block.dirt.blockID || var1 == Block.tilledField.blockID;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
