package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSand extends Block {
    private EaglercraftRandom rand = new EaglercraftRandom();

    public BlockSand(int var1, int var2) {
        super(var1, var2, Material.sand);
    }

    public final void onBlockAdded(World var1, int var2, int var3, int var4) {
        this.e(var1, var2, var3, var4);
    }

    public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        this.e(var1, var2, var3, var4);
    }

    private void e(World var1, int var2, int var3, int var4) {
        int var5 = var3;

        while(true) {
            int var8 = var5 - 1;
            int var6 = var1.getBlockId(var2, var8, var4);
            boolean var10000;
            if(var6 == 0) {
                var10000 = true;
            } else if(var6 == Block.fire.blockID) {
                var10000 = true;
            } else {
                Material var10 = Block.blocksList[var6].material;
                var10000 = var10 == Material.water ? true : var10 == Material.lava;
            }

            if(!var10000 || var5 < 0) {
                if(var5 < 0) {
                    var1.setTileNoUpdate(var2, var3, var4, 0);
                }

                if(var5 != var3) {
                    var6 = var1.getBlockId(var2, var5, var4);
                    if(var6 > 0 && Block.blocksList[var6].material != Material.air) {
                        var1.setTileNoUpdate(var2, var5, var4, 0);
                    }

                    var1.swap(var2, var3, var4, var2, var5, var4);
                }

                return;
            }

            --var5;
            if(var1.getBlockId(var2, var5, var4) == Block.fire.blockID) {
                var1.setBlock(var2, var5, var4, 0);
            }
        }
    }

    public final boolean onBlockPlaced(World var1, float var2, float var3, float var4) {
        int var5 = Block.glass.blockID;
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
