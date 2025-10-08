package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;

public final class BlockCrops extends BlockFlower {
	protected BlockCrops(int var1, int var2) {
		super(59, 88);
		this.blockIndexInTexture = 88;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int var1) {
		return var1 == Block.tilledField.blockID;
	}

	public final int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		if(var2 < 0) {
			var2 = 7;
		}

		return this.blockIndexInTexture + var2;
	}

	public final int getRenderType() {
		return 6;
	}

	public final void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
		super.onBlockDestroyedByPlayer(var1, var2, var3, var4, var5);

		for(int var6 = 0; var6 < 3; ++var6) {
			if(var1.rand.nextInt(15) <= var5) {
				float var7 = var1.rand.nextFloat() * 0.7F + 0.15F;
				float var8 = var1.rand.nextFloat() * 0.7F + 0.15F;
				float var9 = var1.rand.nextFloat() * 0.7F + 0.15F;
				EntityItem var10 = new EntityItem(var1, (double)((float)var2 + var7), (double)((float)var3 + var8), (double)((float)var4 + var9), new ItemStack(Item.seeds));
				var10.delayBeforeCanPickup = 10;
			}
		}

	}

	public final int idDropped(int var1, EaglercraftRandom var2) {
		System.out.println("Get resource: " + var1);
		return var1 == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 1;
	}
}
