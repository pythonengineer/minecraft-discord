package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockLadder extends Block {
    protected BlockLadder(int blockID, int textureIndex) {
        super(65, 83, Material.circuits);
    }

    public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int i5;
        if((i5 = world.getBlockMetadata(x, y, z)) == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
        }

        if(i5 == 4) {
            this.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
        }

        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public final AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int i5;
        if((i5 = world.getBlockMetadata(x, y, z)) == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
        }

        if(i5 == 4) {
            this.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
        }

        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final int getRenderType() {
        return 8;
    }

    public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
    }

    public final void onBlockPlaced(World world, int x, int y, int z, int side) {
        int i6;
        if(((i6 = world.getBlockMetadata(x, y, z)) == 0 || side == 2) && world.isBlockNormalCube(x, y, z + 1)) {
            i6 = 2;
        }

        if((i6 == 0 || side == 3) && world.isBlockNormalCube(x, y, z - 1)) {
            i6 = 3;
        }

        if((i6 == 0 || side == 4) && world.isBlockNormalCube(x + 1, y, z)) {
            i6 = 4;
        }

        if((i6 == 0 || side == 5) && world.isBlockNormalCube(x - 1, y, z)) {
            i6 = 5;
        }

        world.setBlockMetadata(x, y, z, i6);
    }

    public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        int i6 = world.getBlockMetadata(x, y, z);
        boolean z7 = false;
        if(i6 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
            z7 = true;
        }

        if(i6 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
            z7 = true;
        }

        if(i6 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
            z7 = true;
        }

        if(i6 == 5 && world.isBlockNormalCube(x - 1, y, z)) {
            z7 = true;
        }

        if(!z7) {
            this.dropBlockAsItem(world, x, y, z, i6);
            world.setBlockWithNotify(x, y, z, 0);
        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }

    public final int quantityDropped(EaglercraftRandom rand) {
        return 1;
    }
}