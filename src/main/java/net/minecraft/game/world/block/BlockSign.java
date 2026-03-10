package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.material.Material;

public final class BlockSign extends BlockContainer {
    private Class signEntityClass;

    protected BlockSign(int blockID, Class signEntityClas) {
        super(63, Material.ground);
        this.blockIndexInTexture = 0;
        this.signEntityClass = signEntityClas;
        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.625F, 0.75F);
    }

    public final int getRenderType() {
        return -1;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    protected final TileEntity getBlockEntity() {
        try {
            return (TileEntity)this.signEntityClass.newInstance();
        } catch (Exception exception2) {
            throw new RuntimeException(exception2);
        }
    }

    public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    public final int idDropped(int metadata, EaglercraftRandom rand) {
        return Block.planks.blockID;
    }

    public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if(!world.isBlockNormalCube(x, y - 1, z)) {
            this.harvestBlock(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.notifyBlockChange(x, y, z, 0);
        }

    }
}