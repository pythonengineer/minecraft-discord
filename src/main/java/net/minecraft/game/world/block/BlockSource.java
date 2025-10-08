package net.minecraft.game.world.block;

import net.minecraft.game.world.material.Material;

public final class BlockSource extends Block {
	protected BlockSource(int var1, int var2) {
		super(var1, Block.blocksList[var2].blockIndexInTexture, Material.water);
		this.setTickOnLoad(true);
	}
}
