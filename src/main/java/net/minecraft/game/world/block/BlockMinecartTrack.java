package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
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

    public final void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, int i2, int i3, int i4) {
        int i5;
        if((i5 = iBlockAccess.getBlockMetadata(i2, i3, i4)) >= 2 && i5 <= 5) {
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
        world1.setBlockMetadata(i2, i3, i4, 15);
        (new MinecartTrackLogic(this, world1, i2, i3, i4)).place();
    }

    public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        if(!world1.isBlockNormalCube(i2, i3 - 1, i4)) {
            this.harvestBlock(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
            world1.setBlockWithNotify(i2, i3, i4, 0);
        }

    }
}