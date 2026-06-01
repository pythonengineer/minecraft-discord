package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityMobSpawner;
import net.minecraft.game.world.material.Material;

public class BlockMobSpawner extends BlockContainer {
    protected BlockMobSpawner(int id, int tex) {
        super(id, tex, Material.rock);
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityMobSpawner();
    }

    public int idDropped(int blockID, EaglercraftRandom rand) {
        return 0;
    }

    public int quantityDropped(EaglercraftRandom rand) {
        return 0;
    }

    public boolean isOpaqueCube() {
        return false;
    }
}
