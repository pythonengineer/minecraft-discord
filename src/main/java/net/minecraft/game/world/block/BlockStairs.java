package net.minecraft.game.world.block;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;

public class BlockStairs extends Block {
    private Block modelBlock;

    protected BlockStairs(int blockid, Block block) {
        super(blockid, block.blockIndexInTexture, block.material);
        this.modelBlock = block;
        this.setHardness(block.hardness);
        this.setResistance(block.resistance / 3.0F);
        this.setStepSound(block.stepSound);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return 10;
    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int xCoord, int yCoord, int zCoord, int i5) {
        return super.shouldSideBeRendered(blockAccess, xCoord, yCoord, zCoord, i5);
    }

    public void getCollidingBoundingBoxes(World world, int xCoord, int yCoord, int zCoord, AxisAlignedBB aabb, ArrayList arrayList) {
        int i7 = world.getBlockMetadata(xCoord, yCoord, zCoord);
        if(i7 == 0) {
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

    public void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int i5) {
        if(world.getBlockMaterial(xCoord, yCoord + 1, zCoord).isSolid()) {
            world.setBlockWithNotify(xCoord, yCoord, zCoord, this.modelBlock.blockID);
        } else {
            this.updateState(world, xCoord, yCoord, zCoord);
            this.updateState(world, xCoord + 1, yCoord - 1, zCoord);
            this.updateState(world, xCoord - 1, yCoord - 1, zCoord);
            this.updateState(world, xCoord, yCoord - 1, zCoord - 1);
            this.updateState(world, xCoord, yCoord - 1, zCoord + 1);
            this.updateState(world, xCoord + 1, yCoord + 1, zCoord);
            this.updateState(world, xCoord - 1, yCoord + 1, zCoord);
            this.updateState(world, xCoord, yCoord + 1, zCoord - 1);
            this.updateState(world, xCoord, yCoord + 1, zCoord + 1);
        }

        this.modelBlock.onNeighborBlockChange(world, xCoord, yCoord, zCoord, i5);
    }

    private void updateState(World world, int xCoord, int yCoord, int zCoord) {
        if(this.isBlockStair(world, xCoord, yCoord, zCoord)) {
            byte b5 = -1;
            if(this.isBlockStair(world, xCoord + 1, yCoord + 1, zCoord)) {
                b5 = 0;
            }

            if(this.isBlockStair(world, xCoord - 1, yCoord + 1, zCoord)) {
                b5 = 1;
            }

            if(this.isBlockStair(world, xCoord, yCoord + 1, zCoord + 1)) {
                b5 = 2;
            }

            if(this.isBlockStair(world, xCoord, yCoord + 1, zCoord - 1)) {
                b5 = 3;
            }

            if(b5 < 0) {
                if(this.isBlockSolid(world, xCoord + 1, yCoord, zCoord) && !this.isBlockSolid(world, xCoord - 1, yCoord, zCoord)) {
                    b5 = 0;
                }

                if(this.isBlockSolid(world, xCoord - 1, yCoord, zCoord) && !this.isBlockSolid(world, xCoord + 1, yCoord, zCoord)) {
                    b5 = 1;
                }

                if(this.isBlockSolid(world, xCoord, yCoord, zCoord + 1) && !this.isBlockSolid(world, xCoord, yCoord, zCoord - 1)) {
                    b5 = 2;
                }

                if(this.isBlockSolid(world, xCoord, yCoord, zCoord - 1) && !this.isBlockSolid(world, xCoord, yCoord, zCoord + 1)) {
                    b5 = 3;
                }
            }

            if(b5 < 0) {
                if(this.isBlockStair(world, xCoord - 1, yCoord - 1, zCoord)) {
                    b5 = 0;
                }

                if(this.isBlockStair(world, xCoord + 1, yCoord - 1, zCoord)) {
                    b5 = 1;
                }

                if(this.isBlockStair(world, xCoord, yCoord - 1, zCoord - 1)) {
                    b5 = 2;
                }

                if(this.isBlockStair(world, xCoord, yCoord - 1, zCoord + 1)) {
                    b5 = 3;
                }
            }

            if(b5 >= 0) {
                world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, b5);
            }

        }
    }

    private boolean isBlockSolid(World world, int xCoord, int yCoord, int zCoord) {
        return world.getBlockMaterial(xCoord, yCoord, zCoord).isSolid();
    }

    private boolean isBlockStair(World world, int xCoord, int yCoord, int zCoord) {
        int i5 = world.getBlockId(xCoord, yCoord, zCoord);
        return i5 == 0 ? false : Block.blocksList[i5].getRenderType() == 10;
    }

    public void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, EaglercraftRandom random5) {
        this.modelBlock.randomDisplayTick(world, xCoord, yCoord, zCoord, random5);
    }

    public void onBlockClicked(World world, int xCoord, int yCoord, int zCoord, EntityPlayer entityPlayer) {
        this.modelBlock.onBlockClicked(world, xCoord, yCoord, zCoord, entityPlayer);
    }

    public void onBlockDestroyedByPlayer(World world, int xCoord, int yCoord, int zCoord, int nya4) {
        this.modelBlock.onBlockDestroyedByPlayer(world, xCoord, yCoord, zCoord, nya4);
    }

    public float getBlockBrightness(IBlockAccess blockAccess, int xCoord, int yCoord, int zCoord) {
        return this.modelBlock.getBlockBrightness(blockAccess, xCoord, yCoord, zCoord);
    }

    public float getExplosionResistance(Entity entity) {
        return this.modelBlock.getExplosionResistance(entity);
    }

    public int getRenderBlockPass() {
        return this.modelBlock.getRenderBlockPass();
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return this.modelBlock.idDropped(i1, random2);
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return this.modelBlock.quantityDropped(random1);
    }

    public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(i1, i2);
    }

    public int getBlockTextureFromSide(int i1) {
        return this.modelBlock.getBlockTextureFromSide(i1);
    }

    public int getBlockTexture(IBlockAccess blockAccess, int xCoord, int yCoord, int zCoord, int metadataValue) {
        return this.modelBlock.getBlockTexture(blockAccess, xCoord, yCoord, zCoord, metadataValue);
    }

    public int tickRate() {
        return this.modelBlock.tickRate();
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int xCoord, int yCoord, int zCoord) {
        return this.modelBlock.getSelectedBoundingBoxFromPool(world, xCoord, yCoord, zCoord);
    }

    public void velocityToAddToEntity(World world, int xCoord, int yCoord, int zCoord, Entity entity, Vec3D vec3D) {
        this.modelBlock.velocityToAddToEntity(world, xCoord, yCoord, zCoord, entity, vec3D);
    }

    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(int i1, boolean z2) {
        return this.modelBlock.canCollideCheck(i1, z2);
    }

    public boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
        return this.modelBlock.canPlaceBlockAt(world, xCoord, yCoord, zCoord);
    }

    public void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
        this.onNeighborBlockChange(world, xCoord, yCoord, zCoord, 0);
        this.modelBlock.onBlockAdded(world, xCoord, yCoord, zCoord);
    }

    public void onBlockRemoval(World world, int xCoord, int yCoord, int zCoord) {
        this.modelBlock.onBlockRemoval(world, xCoord, yCoord, zCoord);
    }

    public void dropBlockAsItemWithChance(World world, int xCoord, int yCoord, int zCoord, int nya4, float nya5) {
        this.modelBlock.dropBlockAsItemWithChance(world, xCoord, yCoord, zCoord, nya4, nya5);
    }

    public void dropBlockAsItem(World world, int xCoord, int yCoord, int zCoord, int nya4) {
        this.modelBlock.dropBlockAsItem(world, xCoord, yCoord, zCoord, nya4);
    }

    public void onEntityWalking(World world, int xCoord, int yCoord, int zCoord, Entity entity) {
        this.modelBlock.onEntityWalking(world, xCoord, yCoord, zCoord, entity);
    }

    public void updateTick(World world, int xCoord, int yCoord, int zCoord, EaglercraftRandom random5) {
        this.modelBlock.updateTick(world, xCoord, yCoord, zCoord, random5);
    }

    public boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer entityPlayer) {
        return this.modelBlock.blockActivated(world, xCoord, yCoord, zCoord, entityPlayer);
    }

    public void onBlockDestroyedByExplosion(World world, int xCoord, int yCoord, int zCoord) {
        this.modelBlock.onBlockDestroyedByExplosion(world, xCoord, yCoord, zCoord);
    }
}
