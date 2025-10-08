package net.minecraft.game.world.block;

import net.minecraft.game.world.material.Material;

public class BlockDirt extends Block {
    protected BlockDirt(int var1, int var2) {
        super(3, 2, Material.ground);
    }

    protected BlockDirt(int var1) {
        super(19, Material.sponge);
        this.blockIndexInTexture = 48;
    }
}
