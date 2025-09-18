package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockOre extends Block {
	public BlockOre(int var1, int var2) {
		super(var1, var2, Material.rock);
	}

	public final int idDropped() {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 1;
	}

	public final boolean onBlockPlaced(World var1, float var2, float var3, float var4) {
		int var5 = 0;
		if(this.blockID == Block.oreCoal.blockID) {
			var5 = Item.coal.shiftedIndex;
		}

		if(this.blockID == Block.oreDiamond.blockID) {
			var5 = Item.diamond.shiftedIndex;
		}

		if(this.blockID == Block.oreIron.blockID) {
			var5 = Item.ingotIron.shiftedIndex;
		}

		if(this.blockID == Block.oreGold.blockID) {
			var5 = Item.ingotGold.shiftedIndex;
		}

		for(int var6 = 0; var6 <= 0; ++var6) {
			if(var1.random.nextFloat() <= 1.0F) {
				float var7 = var1.random.nextFloat() * 0.7F + 0.15F;
				float var8 = var1.random.nextFloat() * 0.7F + 0.15F;
				float var9 = var1.random.nextFloat() * 0.7F + 0.15F;
				EntityItem var10 = new EntityItem(var1, var2 + var7, var3 + var8, var4 + var9, new ItemStack(var5));
				var10.delayBeforeCanPickup = 10;
				var1.spawnEntityInWorld(var10);
			}
		}

		return true;
	}
}
