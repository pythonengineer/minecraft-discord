package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockObsidian extends BlockStone {
    public BlockObsidian(int i1, int i2) {
        super(i1, i2);
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 1;
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Block.obsidian.blockID;
    }
}
