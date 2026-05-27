package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.material.Material;

public class BlockSign extends BlockContainer {
    private Class signEntityClass;
    private int itemDropID;

    protected BlockSign(int blockID, Class signEntityClas, int droppedItemID) {
        super(blockID, Material.wood);
        this.blockIndexInTexture = 4;
        this.signEntityClass = signEntityClas;
        float f4 = 0.25F;
        float f5 = 1.625F;
        this.setBlockBounds(0.5F - f4, 0.0F, 0.5F - f4, 0.5F + f4, f5, 0.5F + f4);
        this.itemDropID = droppedItemID;
    }

    public int getRenderType() {
        return -1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    protected TileEntity getBlockEntity() {
        try {
            return (TileEntity)this.signEntityClass.newInstance();
        } catch (Exception exception2) {
            throw new RuntimeException(exception2);
        }
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    public int idDropped(int metadata, EaglercraftRandom rand) {
        return this.itemDropID;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if(!world.isBlockNormalCube(x, y - 1, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }

    }
}
