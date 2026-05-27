package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockLadder extends Block {
    protected BlockLadder(int blockID, int textureIndex) {
        super(blockID, textureIndex, Material.circuits);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int i5 = world.getBlockMetadata(x, y, z);
        float f6 = 0.125F;
        if(i5 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
        }

        if(i5 == 4) {
            this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
        }

        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int i5 = world.getBlockMetadata(x, y, z);
        float f6 = 0.125F;
        if(i5 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
        }

        if(i5 == 4) {
            this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if(i5 == 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
        }

        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return 8;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int i6 = world.getBlockMetadata(x, y, z);
        if((i6 == 0 || side == 2) && world.isBlockNormalCube(x, y, z + 1)) {
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

        world.setBlockMetadataWithNotify(x, y, z, i6);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
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

    public int quantityDropped(EaglercraftRandom rand) {
        return 1;
    }
}
