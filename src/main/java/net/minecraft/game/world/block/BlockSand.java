package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSand extends Block {
    public BlockSand(int var1, int var2) {
        super(var1, var2, Material.sand);
    }

    public final void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
        int var5 = var4;
        var4 = var3;
        var3 = var2;
        World var11 = var1;
        int var10 = var4;

        while(true) {
            int var8 = var10 - 1;
            int var6 = var11.getBlockId(var3, var8, var5);
            boolean var10000;
            if(var6 == 0) {
                var10000 = true;
            } else if(var6 == Block.fire.blockID) {
                var10000 = true;
            } else {
                Material var12 = Block.blocksList[var6].material;
                var10000 = var12 == Material.water ? true : var12 == Material.lava;
            }

            if(!var10000 || var10 < 0) {
                if(var10 < 0) {
                    var11.setTileNoUpdate(var3, var4, var5, 0);
                }

                if(var10 != var4) {
                    var6 = var11.getBlockId(var3, var10, var5);
                    if(var6 > 0 && Block.blocksList[var6].material != Material.air) {
                        var11.setTileNoUpdate(var3, var10, var5, 0);
                    }

                    var11.swap(var3, var4, var5, var3, var10, var5);
                }

                return;
            }

            --var10;
            if(var11.getBlockId(var3, var10, var5) == Block.fire.blockID) {
                var11.setTileNoUpdate(var3, var10, var5, 0);
            }
        }
    }
}
