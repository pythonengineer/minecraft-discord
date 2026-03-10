package net.minecraft.game.world.block;

import net.minecraft.game.world.material.Material;

public final class BlockOreBlock extends Block {
    public BlockOreBlock(int blockID, int textureIndex) {
        super(blockID, Material.iron);
        this.blockIndexInTexture = textureIndex;
    }

    public final int getBlockTextureFromSide(int side) {
        return side == 1 ? this.blockIndexInTexture - 16 : (side == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
    }
}