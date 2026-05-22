package net.minecraft.game.world.block;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;

public final class BlockStairs extends Block {
    private Block modelBlock;

    protected BlockStairs(int blockid, Block block) {
        super(blockid, block.blockIndexInTexture, block.blockMaterial);
        this.modelBlock = block;
        this.setHardness(block.blockHardness);
        this.setResistance(block.blockResistance / 3.0F);
        this.setStepSound(block.stepSound);
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final int getRenderType() {
        return 10;
    }

    public final boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
        return super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
    }

    public final void getCollidingBoundingBoxes(World world, int xCoord, int yCoord, int zCoord, AxisAlignedBB aabb, ArrayList arrayList) {
        int i7;
        if((i7 = world.getBlockMetadata(xCoord, yCoord, zCoord)) == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
        } else if(i7 == 1) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
        } else if(i7 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
        } else if(i7 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, xCoord, yCoord, zCoord, aabb, arrayList);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        if(world1.getBlockMaterial(i2, i3 + 1, i4).isSolid()) {
            world1.setBlockWithNotify(i2, i3, i4, this.modelBlock.blockID);
        } else {
            this.updateState(world1, i2, i3, i4);
            this.updateState(world1, i2 + 1, i3 - 1, i4);
            this.updateState(world1, i2 - 1, i3 - 1, i4);
            this.updateState(world1, i2, i3 - 1, i4 - 1);
            this.updateState(world1, i2, i3 - 1, i4 + 1);
            this.updateState(world1, i2 + 1, i3 + 1, i4);
            this.updateState(world1, i2 - 1, i3 + 1, i4);
            this.updateState(world1, i2, i3 + 1, i4 - 1);
            this.updateState(world1, i2, i3 + 1, i4 + 1);
        }

        this.modelBlock.onNeighborBlockChange(world1, i2, i3, i4, i5);
    }

    private void updateState(World world, int xCoord, int yCoord, int zCoord) {
        if(isBlockStair(world, xCoord, yCoord, zCoord)) {
            byte b5 = -1;
            if(isBlockStair(world, xCoord + 1, yCoord + 1, zCoord)) {
                b5 = 0;
            }

            if(isBlockStair(world, xCoord - 1, yCoord + 1, zCoord)) {
                b5 = 1;
            }

            if(isBlockStair(world, xCoord, yCoord + 1, zCoord + 1)) {
                b5 = 2;
            }

            if(isBlockStair(world, xCoord, yCoord + 1, zCoord - 1)) {
                b5 = 3;
            }

            if(b5 < 0) {
                if(isBlockSolid(world, xCoord + 1, yCoord, zCoord) && !isBlockSolid(world, xCoord - 1, yCoord, zCoord)) {
                    b5 = 0;
                }

                if(isBlockSolid(world, xCoord - 1, yCoord, zCoord) && !isBlockSolid(world, xCoord + 1, yCoord, zCoord)) {
                    b5 = 1;
                }

                if(isBlockSolid(world, xCoord, yCoord, zCoord + 1) && !isBlockSolid(world, xCoord, yCoord, zCoord - 1)) {
                    b5 = 2;
                }

                if(isBlockSolid(world, xCoord, yCoord, zCoord - 1) && !isBlockSolid(world, xCoord, yCoord, zCoord + 1)) {
                    b5 = 3;
                }
            }

            if(b5 < 0) {
                if(isBlockStair(world, xCoord - 1, yCoord - 1, zCoord)) {
                    b5 = 0;
                }

                if(isBlockStair(world, xCoord + 1, yCoord - 1, zCoord)) {
                    b5 = 1;
                }

                if(isBlockStair(world, xCoord, yCoord - 1, zCoord - 1)) {
                    b5 = 2;
                }

                if(isBlockStair(world, xCoord, yCoord - 1, zCoord + 1)) {
                    b5 = 3;
                }
            }

            if(b5 >= 0) {
                world.setBlockMetadata(xCoord, yCoord, zCoord, b5);
            }

        }
    }

    private static boolean isBlockSolid(World world0, int i1, int i2, int i3) {
        return world0.getBlockMaterial(i1, i2, i3).isSolid();
    }

    private static boolean isBlockStair(World world0, int i1, int i2, int i3) {
        int i4;
        return (i4 = world0.getBlockId(i1, i2, i3)) == 0 ? false : Block.blocksList[i4].getRenderType() == 10;
    }

    public final void randomDisplayTick(World world1, int i2, int i3, int i4, EaglercraftRandom random5) {
        this.modelBlock.randomDisplayTick(world1, i2, i3, i4, random5);
    }

    public final void onBlockClicked(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
        this.modelBlock.onBlockClicked(world1, i2, i3, i4, entityPlayer5);
    }

    public final void onBlockDestroyedByPlayer(World world, int xCoord, int yCoord, int zCoord, int nya4) {
        this.modelBlock.onBlockDestroyedByPlayer(world, xCoord, yCoord, zCoord, nya4);
    }

    public final float getBlockBrightness(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
        return this.modelBlock.getBlockBrightness(iBlockAccess1, i2, i3, i4);
    }

    public final float getExplosionResistance(Entity entity1) {
        return this.modelBlock.getExplosionResistance(entity1);
    }

    public final int getRenderBlockPass() {
        return this.modelBlock.getRenderBlockPass();
    }

    public final int idDropped(int i1, EaglercraftRandom random2) {
        return this.modelBlock.idDropped(i1, random2);
    }

    public final int quantityDropped(EaglercraftRandom random1) {
        return this.modelBlock.quantityDropped(random1);
    }

    public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(i1, i2);
    }

    public final int getBlockTextureFromSide(int i1) {
        return this.modelBlock.getBlockTextureFromSide(i1);
    }

    public final int getBlockTexture(IBlockAccess blockAccess, int xCoord, int yCoord, int zCoord, int metadataValue) {
        return this.modelBlock.getBlockTexture(blockAccess, xCoord, yCoord, zCoord, metadataValue);
    }

    public final int tickRate() {
        return this.modelBlock.tickRate();
    }

    public final AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int xCoord, int yCoord, int zCoord) {
        return this.modelBlock.getSelectedBoundingBoxFromPool(world, xCoord, yCoord, zCoord);
    }

    public final void velocityToAddToEntity(World world1, int i2, int i3, int i4, Entity entity5, Vec3D vec3D6) {
        this.modelBlock.velocityToAddToEntity(world1, i2, i3, i4, entity5, vec3D6);
    }

    public final boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public final boolean canCollideCheck(int i1, boolean z2) {
        return this.modelBlock.canCollideCheck(i1, z2);
    }

    public final boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
        return this.modelBlock.canPlaceBlockAt(world1, i2, i3, i4);
    }

    public final void onBlockAdded(World world1, int i2, int i3, int i4) {
        this.onNeighborBlockChange(world1, i2, i3, i4, 0);
        this.modelBlock.onBlockAdded(world1, i2, i3, i4);
    }

    public final void onBlockRemoval(World world1, int i2, int i3, int i4) {
        this.modelBlock.onBlockRemoval(world1, i2, i3, i4);
    }

    public final void dropBlockAsItemWithChance(World world, int xCoord, int yCoord, int zCoord, int nya4, float nya5) {
        this.modelBlock.dropBlockAsItemWithChance(world, xCoord, yCoord, zCoord, nya4, nya5);
    }

    public final void dropBlockAsItem(World world, int xCoord, int yCoord, int zCoord, int nya4) {
        this.modelBlock.dropBlockAsItem(world, xCoord, yCoord, zCoord, nya4);
    }

    public final void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
        this.modelBlock.onEntityWalking(world1, i2, i3, i4, entity5);
    }

    public final void updateTick(World world1, int i2, int i3, int i4, EaglercraftRandom random5) {
        this.modelBlock.updateTick(world1, i2, i3, i4, random5);
    }

    public final boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer entityPlayer) {
        return this.modelBlock.blockActivated(world, xCoord, yCoord, zCoord, entityPlayer);
    }

    public final void onBlockDestroyedByExplosion(World world1, int i2, int i3, int i4) {
        this.modelBlock.onBlockDestroyedByExplosion(world1, i2, i3, i4);
    }
}