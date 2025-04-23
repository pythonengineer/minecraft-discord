package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFlower extends Block {
	protected BlockFlower(int var1, int var2) {
		super(var1);
		this.blockIndexInTexture = var2;
		this.setTickOnLoad(true);
		float var3 = 0.2F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, var3 + 0.5F, var3 * 3.0F, var3 + 0.5F);
	}

	public void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
		if(!var1.multiplayerWorld) {
			int var6 = var1.getBlockId(var2, var3 - 1, var4);
			if(!var1.isHalfLit(var2, var3, var4) || var6 != Block.dirt.blockID && var6 != Block.grass.blockID) {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}

		}
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

	public final int getRenderType() {
		return 1;
	}
}
