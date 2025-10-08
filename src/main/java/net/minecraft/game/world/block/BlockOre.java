package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.material.Material;

public final class BlockOre extends Block {
	public BlockOre(int var1, int var2) {
		super(var1, var2, Material.rock);
	}

	public final int idDropped(int var1, EaglercraftRandom var2) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 1;
	}
}
