package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.material.Material;

public class BlockOre extends Block {
	public BlockOre(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.rock);
	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}
}
