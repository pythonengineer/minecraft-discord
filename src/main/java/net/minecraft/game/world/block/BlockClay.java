package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.material.Material;

public class BlockClay extends Block {
    public BlockClay(int id, int tex) {
        super(id, tex, Material.clay);
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Item.clay.shiftedIndex;
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 4;
    }
}
