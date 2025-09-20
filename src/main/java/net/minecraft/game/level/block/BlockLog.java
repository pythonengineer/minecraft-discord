package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.material.Material;

public final class BlockLog extends Block {
    protected BlockLog(int var1) {
        super(17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return var1.nextInt(3) + 3;
	}

    public final int idDropped(int var1) {
        return Block.planks.blockID;
    }

    public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
