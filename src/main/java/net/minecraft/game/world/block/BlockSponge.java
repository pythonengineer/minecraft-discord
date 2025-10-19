package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSponge extends Block {
    protected BlockSponge(int var1) {
        super(19, Material.sponge);
        this.blockIndexInTexture = 48;
    }

    public final void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
        for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
            for(int var6 = var3 - 2; var6 <= var3 + 2; ++var6) {
                for(int var7 = var4 - 2; var7 <= var4 + 2; ++var7) {
                    var1.getBlockMaterial(var5, var6, var7);
                }
            }
        }

    }
}
