package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStationary extends BlockFluid {
    protected BlockStationary(int var1, Material var2) {
        super(var1, var2);
        this.b = var1 - 1;
        this.a = var1;
        this.setTickOnLoad(false);
    }

    public final void a(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
    }

    public final void b(World var1, int var2, int var3, int var4, int var5) {
        boolean var6 = false;
        if(this.e(var1, var2, var3 - 1, var4)) {
            var6 = true;
        }

        if(!var6 && this.e(var1, var2 - 1, var3, var4)) {
            var6 = true;
        }

        if(!var6 && this.e(var1, var2 + 1, var3, var4)) {
            var6 = true;
        }

        if(!var6 && this.e(var1, var2, var3, var4 - 1)) {
            var6 = true;
        }

        if(!var6 && this.e(var1, var2, var3, var4 + 1)) {
            var6 = true;
        }

        if(var5 != 0) {
            Material var7 = Block.blocksList[var5].material;
            if(this.material == Material.water && var7 == Material.lava || var7 == Material.water && this.material == Material.lava) {
                var1.setBlockWithNotify(var2, var3, var4, Block.stone.blockID);
                return;
            }
        }

        if(Block.fire.b(var5)) {
            var6 = true;
        }

        if(var6) {
            var1.setTileNoUpdate(var2, var3, var4, this.b);
            var1.scheduleBlockUpdate(var2, var3, var4, this.b);
        }

    }
}
