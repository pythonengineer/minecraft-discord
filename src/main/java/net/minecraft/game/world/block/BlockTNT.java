package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTNT extends Block {
    public BlockTNT(int var1, int var2) {
        super(46, 8, Material.tnt);
	}

    public final int getBlockTextureFromSide(int var1) {
		return var1 == 0 ? this.blockIndexInTexture + 2 : (var1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

    public final void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
        EntityTNTPrimed var6 = new EntityTNTPrimed(var1, (float)var2 + 0.5F, (float)var3 + 0.5F, (float)var4 + 0.5F);
        var1.spawnEntityInWorld(var6);
        var1.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
    }
}
