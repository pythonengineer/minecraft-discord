package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class BlockObsidian extends BlockStone {
    public BlockObsidian(int i1, int i2) {
        super(49, 37);
    }

    public final int quantityDropped(EaglercraftRandom random1) {
        return 1;
    }

    public final int idDropped(int i1, EaglercraftRandom random2) {
        return Block.obsidian.blockID;
    }
}