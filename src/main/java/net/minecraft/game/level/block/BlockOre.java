package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;

public final class BlockOre extends Block {
	public BlockOre(int var1, int var2) {
		super(var1, var2);
	}

    public final int idDropped() {
        return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
    }

	public final int quantityDropped(EaglercraftRandom var1) {
        return this.idDropped() != this.blockID ? var1.nextInt(3) + 1 : 1;
	}
}
