package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSource extends Block {
    private int a;

    protected BlockSource(int var1, int var2) {
        super(var1, Block.blocksList[var2].blockIndexInTexture, Material.water);
        this.a = var2;
        this.setTickOnLoad(true);
    }

    public final void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
        super.onNeighborBlockChange(var1, var2, var3, var4);
        if(var1.getBlockId(var2 - 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 - 1, var3, var4, this.a);
        }

        if(var1.getBlockId(var2 + 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 + 1, var3, var4, this.a);
        }

        if(var1.getBlockId(var2, var3, var4 - 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 - 1, this.a);
        }

        if(var1.getBlockId(var2, var3, var4 + 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 + 1, this.a);
        }

    }

    public final void updateTick(World var1, int var2, int var3, int var4, EaglercraftRandom var5) {
        super.updateTick(var1, var2, var3, var4, var5);
        if(var1.getBlockId(var2 - 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 - 1, var3, var4, this.a);
        }

        if(var1.getBlockId(var2 + 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 + 1, var3, var4, this.a);
        }

        if(var1.getBlockId(var2, var3, var4 - 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 - 1, this.a);
        }

        if(var1.getBlockId(var2, var3, var4 + 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 + 1, this.a);
        }

    }
}
