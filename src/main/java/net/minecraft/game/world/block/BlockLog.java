package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.material.Material;

public final class BlockLog extends Block {
    protected BlockLog(int var1) {
        super(17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
        return 1;
	}

    public final int idDropped(int var1, EaglercraftRandom var2) {
        return Block.wood.blockID;
    }

    public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
