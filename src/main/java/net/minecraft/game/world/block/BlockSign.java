package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.material.Material;

public class BlockSign extends BlockContainer {
    private Class signEntityClass;
    private boolean isFreestanding;

    protected BlockSign(int blockID, Class signEntityClas, boolean isFreestanding) {
        super(blockID, Material.wood);
        this.isFreestanding = isFreestanding;
        this.blockIndexInTexture = 4;
        this.signEntityClass = signEntityClas;
        float f5 = 0.25F;
        float f6 = 1.0F;
        this.setBlockBounds(0.5F - f5, 0.0F, 0.5F - f5, 0.5F + f5, f6, 0.5F + f5);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        if(!this.isFreestanding) {
            int i5 = blockAccess.getBlockMetadata(x, y, z);
            float f6 = 0.28125F;
            float f7 = 0.78125F;
            float f8 = 0.0F;
            float f9 = 1.0F;
            float f10 = 0.125F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            if(i5 == 2) {
                this.setBlockBounds(f8, f6, 1.0F - f10, f9, f7, 1.0F);
            }

            if(i5 == 3) {
                this.setBlockBounds(f8, f6, 0.0F, f9, f7, f10);
            }

            if(i5 == 4) {
                this.setBlockBounds(1.0F - f10, f6, f8, 1.0F, f7, f9);
            }

            if(i5 == 5) {
                this.setBlockBounds(0.0F, f6, f8, f10, f7, f9);
            }

        }
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

    public int idDropped(int metadata, EaglercraftRandom rand) {
        return Item.sign.shiftedIndex;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        boolean z6 = false;
        if(this.isFreestanding) {
            if(!world.getBlockMaterial(x, y - 1, z).isSolid()) {
                z6 = true;
            }
        } else {
            int i7 = world.getBlockMetadata(x, y, z);
            z6 = true;
            if(i7 == 2 && world.getBlockMaterial(x, y, z + 1).isSolid()) {
                z6 = false;
            }

            if(i7 == 3 && world.getBlockMaterial(x, y, z - 1).isSolid()) {
                z6 = false;
            }

            if(i7 == 4 && world.getBlockMaterial(x + 1, y, z).isSolid()) {
                z6 = false;
            }

            if(i7 == 5 && world.getBlockMaterial(x - 1, y, z).isSolid()) {
                z6 = false;
            }
        }

        if(z6) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }
}
