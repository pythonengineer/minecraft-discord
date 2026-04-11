package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockMinecartTrack extends Block {
    protected BlockMinecartTrack(int blockID, int textureIndex) {
        super(66, 128, Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
        this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
        return super.collisionRayTrace(world1, i2, i3, i4, vec3D5, vec3D6);
    }

    public final void setBlockBoundsBasedOnState(World world1, int i2, int i3, int i4) {
        int i5;
        if((i5 = world1.getBlockMetadata(i2, i3, i4)) >= 2 && i5 <= 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
        return i2 >= 6 ? this.blockIndexInTexture - 16 : this.blockIndexInTexture;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final int getRenderType() {
        return 9;
    }

    public final int quantityDropped(EaglercraftRandom random1) {
        return 1;
    }

    public final boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
        return world1.isBlockNormalCube(i2, i3 - 1, i4);
    }

    public final void onBlockAdded(World world1, int i2, int i3, int i4) {
        this.refreshTrackShape(world1, i2, i3, i4);
        world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
        world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
    }

    public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        if(!world1.isBlockNormalCube(i2, i3 - 1, i4)) {
            this.harvestBlock(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
            world1.setBlockWithNotify(i2, i3, i4, 0);
        } else {
            this.refreshTrackShape(world1, i2, i3, i4);
        }
    }

    private void refreshTrackShape(World world, int x, int y, int z) {
        boolean z5 = this.isBlockARailTrack(world, x, y, z - 1);
        boolean z6 = this.isBlockARailTrack(world, x, y, z + 1);
        boolean z7 = this.isBlockARailTrack(world, x - 1, y, z);
        boolean z8 = this.isBlockARailTrack(world, x + 1, y, z);
        boolean z9 = world.isBlockNormalCube(x, y, z - 1) && !world.isBlockNormalCube(x, y + 1, z - 1);
        boolean z10 = world.isBlockNormalCube(x, y, z + 1) && !world.isBlockNormalCube(x, y + 1, z + 1);
        boolean z11 = world.isBlockNormalCube(x - 1, y, z) && !world.isBlockNormalCube(x - 1, y + 1, z);
        boolean z12 = world.isBlockNormalCube(x + 1, y, z) && !world.isBlockNormalCube(x + 1, y + 1, z);
        byte b13 = -1;
        if(z5 || z6) {
            b13 = 0;
        }

        if(z7 || z8) {
            b13 = 1;
        }

        if((z7 || z8) && z12 && !z11) {
            b13 = 2;
        }

        if((z7 || z8) && z11 && !z12) {
            b13 = 3;
        }

        if((z5 || z6) && z9 && !z10) {
            b13 = 4;
        }

        if((z5 || z6) && z10 && !z9) {
            b13 = 5;
        }

        if(z6 && z8 && !z5 && !z7) {
            b13 = 6;
        }

        if(z6 && z7 && !z5 && !z8) {
            b13 = 7;
        }

        if(z5 && z7 && !z6 && !z8) {
            b13 = 8;
        }

        if(z5 && z8 && !z6 && !z7) {
            b13 = 9;
        }

        if(b13 < 0) {
            b13 = 0;
        }

        world.setBlockMetadata(x, y, z, b13);
    }

    public final void onBlockRemoval(World world1, int i2, int i3, int i4) {
        super.onBlockRemoval(world1, i2, i3, i4);
        world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
        world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
    }

    private boolean isBlockARailTrack(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == this.blockID ? true : (world.isBlockNormalCube(x, y, z) ? world.getBlockId(x, y + 1, z) == this.blockID : world.getBlockId(x, y - 1, z) == this.blockID);
    }
}