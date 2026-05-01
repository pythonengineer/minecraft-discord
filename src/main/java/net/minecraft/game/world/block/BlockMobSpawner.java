package net.minecraft.game.world.block;

import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.material.Material;

public final class BlockMobSpawner extends BlockContainer {
    protected BlockMobSpawner(int i1, int i2) {
        super(52, 65, Material.rock);
    }

    protected final TileEntity getBlockEntity() {
        return new TileEntityMobSpawner();
    }

    public final boolean isOpaqueCube() {
        return false;
    }
}