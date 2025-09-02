package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;

public final class BlockOre extends Block {
    private EaglercraftRandom rand = new EaglercraftRandom();

	public BlockOre(int var1, int var2) {
		super(var1, var2);
	}

    public final int idDropped() {
        return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
    }

	public final int quantityDropped(EaglercraftRandom var1) {
        return this.idDropped() != this.blockID ? var1.nextInt(3) + 1 : 1;
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

        int var6 = this.rand.nextInt(3) + 1;

        for(int var7 = 0; var7 < var6; ++var7) {
            if(var1.random.nextFloat() <= 1.0F) {
                float var8 = var1.random.nextFloat() * 0.7F + 0.15F;
                float var9 = var1.random.nextFloat() * 0.7F + 0.15F;
                float var10 = var1.random.nextFloat() * 0.7F + 0.15F;
                EntityItem var11 = new EntityItem(var1, var2 + var8, var3 + var9, var4 + var10, new ItemStack(var5));
                var11.delayBeforeCanPickup = 10;
                var1.spawnEntityInWorld(var11);
            }
        }

        return true;
    }
}
