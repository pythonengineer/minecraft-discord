package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFarmland extends Block {
	protected BlockFarmland(int var1) {
		super(60, Material.ground);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 15.0F / 16.0F, 1.0F);
		this.setLightOpacity(255);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((double)var1, (double)var2, (double)var3, (double)(var1 + 1), (double)(var2 + 1), (double)(var3 + 1));
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		return var1 == 1 && var2 > 0 ? this.blockIndexInTexture - 1 : (var1 == 1 ? this.blockIndexInTexture : 2);
	}

	public final void onEntityWalking(World var1, int var2, int var3, int var4) {
		if(var1.rand.nextInt(4) == 0) {
			var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
		}

	}

	public final int idDropped(int var1, EaglercraftRandom var2) {
		return Block.dirt.idDropped(0, var2);
	}
}
