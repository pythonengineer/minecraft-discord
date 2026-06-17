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

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int i5) {
        return super.shouldSideBeRendered(blockAccess, x, y, z, i5);
    }

    public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList collidingBoundingBoxes) {
        int i7 = world.getBlockMetadata(x, y, z);
        if(i7 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
        } else if(i7 == 1) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
        } else if(i7 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
        } else if(i7 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int i5) {
        if(!world.multiplayerWorld) {
            if(world.getBlockMaterial(x, y + 1, z).isSolid()) {
                world.setBlockWithNotify(x, y, z, this.modelBlock.blockID);
            } else {
                this.updateState(world, x, y, z);
                this.updateState(world, x + 1, y - 1, z);
                this.updateState(world, x - 1, y - 1, z);
                this.updateState(world, x, y - 1, z - 1);
                this.updateState(world, x, y - 1, z + 1);
                this.updateState(world, x + 1, y + 1, z);
                this.updateState(world, x - 1, y + 1, z);
                this.updateState(world, x, y + 1, z - 1);
                this.updateState(world, x, y + 1, z + 1);
            }

            this.modelBlock.onNeighborBlockChange(world, x, y, z, i5);
        }
    }

    private void updateState(World world, int x, int y, int z) {
        if(this.isBlockStair(world, x, y, z)) {
            byte b5 = -1;
            if(this.isBlockStair(world, x + 1, y + 1, z)) {
                b5 = 0;
            }

            if(this.isBlockStair(world, x - 1, y + 1, z)) {
                b5 = 1;
            }

            if(this.isBlockStair(world, x, y + 1, z + 1)) {
                b5 = 2;
            }

            if(this.isBlockStair(world, x, y + 1, z - 1)) {
                b5 = 3;
            }

            if(b5 < 0) {
                if(this.isBlockSolid(world, x + 1, y, z) && !this.isBlockSolid(world, x - 1, y, z)) {
                    b5 = 0;
                }

                if(this.isBlockSolid(world, x - 1, y, z) && !this.isBlockSolid(world, x + 1, y, z)) {
                    b5 = 1;
                }

                if(this.isBlockSolid(world, x, y, z + 1) && !this.isBlockSolid(world, x, y, z - 1)) {
                    b5 = 2;
                }

                if(this.isBlockSolid(world, x, y, z - 1) && !this.isBlockSolid(world, x, y, z + 1)) {
                    b5 = 3;
                }
            }

            if(b5 < 0) {
                if(this.isBlockStair(world, x - 1, y - 1, z)) {
                    b5 = 0;
                }

                if(this.isBlockStair(world, x + 1, y - 1, z)) {
                    b5 = 1;
                }

                if(this.isBlockStair(world, x, y - 1, z - 1)) {
                    b5 = 2;
                }

                if(this.isBlockStair(world, x, y - 1, z + 1)) {
                    b5 = 3;
                }
            }

            if(b5 >= 0) {
                world.setBlockMetadataWithNotify(x, y, z, b5);
            }

        }
    }

    private boolean isBlockSolid(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y, z).isSolid();
    }

    private boolean isBlockStair(World world, int x, int y, int z) {
        int i5 = world.getBlockId(x, y, z);
        return i5 == 0 ? false : Block.blocksList[i5].getRenderType() == 10;
    }

    public void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom random5) {
        this.modelBlock.randomDisplayTick(world, x, y, z, random5);
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        this.modelBlock.onBlockClicked(world, x, y, z, entityPlayer);
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        this.modelBlock.onBlockDestroyedByPlayer(world, x, y, z, metadata);
    }

    public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
        return this.modelBlock.getBlockBrightness(blockAccess, x, y, z);
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

    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(side, metadata);
    }

    public int getBlockTextureFromSide(int i1) {
        return this.modelBlock.getBlockTextureFromSide(i1);
    }

    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return this.modelBlock.getBlockTexture(blockAccess, x, y, z, side);
    }

    public int tickRate() {
        return this.modelBlock.tickRate();
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.modelBlock.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D vec3D) {
        this.modelBlock.velocityToAddToEntity(world, x, y, z, entity, vec3D);
    }

    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(int i1, boolean z2) {
        return this.modelBlock.canCollideCheck(i1, z2);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return this.modelBlock.canPlaceBlockAt(world, x, y, z);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.onNeighborBlockChange(world, x, y, z, 0);
        this.modelBlock.onBlockAdded(world, x, y, z);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        this.modelBlock.onBlockRemoval(world, x, y, z);
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int nya4, float nya5) {
        this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, nya4, nya5);
    }

    public void dropBlockAsItem(World world, int x, int y, int z, int metadata) {
        this.modelBlock.dropBlockAsItem(world, x, y, z, metadata);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        this.modelBlock.onEntityWalking(world, x, y, z, entity);
    }

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom random5) {
        this.modelBlock.updateTick(world, x, y, z, random5);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        return this.modelBlock.blockActivated(world, x, y, z, entityPlayer);
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
    }
}
