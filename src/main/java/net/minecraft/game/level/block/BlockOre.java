package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class BlockOre extends Block {
	public BlockOre(int var1, int var2) {
		super(var1, var2);
	}

    public final int idDropped() {
        return this == Block.oreCoal ? Block.stairSingle.blockID : (this == Block.oreGold ? Block.blockGold.blockID : (this == Block.oreIron ? Block.blockSteel.blockID : this.blockID));
    }

	public final int quantityDropped(EaglercraftRandom var1) {
		return var1.nextInt(3) + 1;
	}
}
