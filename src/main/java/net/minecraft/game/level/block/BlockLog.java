package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class BlockLog extends Block {
	protected BlockLog() {
		super(17);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return var1.nextInt(3) + 3;
	}

    public final int idDropped() {
        return Block.planks.blockID;
    }

	public final int getBlockTexture(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
