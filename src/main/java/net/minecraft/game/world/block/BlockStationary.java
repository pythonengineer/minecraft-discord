package net.minecraft.game.world.block;

import net.minecraft.game.world.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int var1, Material var2) {
		super(var1, var2);
		this.movingId = var1 - 1;
		this.stillId = var1;
		this.setTickOnLoad(false);
	}
}
