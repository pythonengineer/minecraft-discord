package net.minecraft.game.world.block;

import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.material.Material;

public class BlockMobSpawner extends BlockContainer {
    protected BlockMobSpawner(int i1, int i2) {
        super(i1, i2, Material.rock);
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityMobSpawner();
    }

    public boolean isOpaqueCube() {
        return false;
    }
}
