package net.minecraft.game.level.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.level.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
    protected BlockLeaves(int var1, int var2) {
        super(18, 52, Material.leaves, true);
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return var1.nextInt(6) == 0 ? 1 : 0;
	}

    public final int idDropped(int var1) {
        return Block.sapling.blockID;
    }
}
