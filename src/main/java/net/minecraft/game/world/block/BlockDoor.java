package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockDoor extends Block {
    protected BlockDoor(int blockID) {
        super(64, Material.ground);
        this.blockIndexInTexture = 97;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public final int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if(side != 0 && side != 1) {
            int i3;
            if(((i3 = getState(metadata)) == 0 || i3 == 2) ^ side <= 3) {
                return this.blockIndexInTexture;
            } else {
                side = i3 / 2 + (side & 1 ^ i3) + (metadata & 4) / 4;
                return this.blockIndexInTexture + (side & 1) - ((metadata & 8) << 1);
            }
        } else {
            return this.blockIndexInTexture;
        }
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final boolean renderAsNormalBlock() {
        return false;
    }

    public final int getRenderType() {
        return 7;
    }

    public final AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public final void setBlockBoundsBasedOnState(World world, int x, int y, int z) {
        x = getState(world.getBlockMetadata(x, y, z));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        if(x == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
        }

        if(x == 1) {
            this.setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if(x == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
        }

        if(x == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
        }

    }

    public final void onBlockClicked(World world, int x, int y, int z, EntityPlayer playerEntity) {
        this.blockActivated(world, x, y, z, playerEntity);
    }

    public final boolean blockActivated(World world, int x, int y, int z, EntityPlayer playerEntity) {
        int i6;
        if(((i6 = world.getBlockMetadata(x, y, z)) & 8) != 0) {
            if(world.getBlockId(x, y - 1, z) == this.blockID) {
                this.blockActivated(world, x, y - 1, z, playerEntity);
            }

            return true;
        } else {
            if(world.getBlockId(x, y + 1, z) == this.blockID) {
                world.setBlockMetadata(x, y + 1, z, (i6 ^ 4) + 8);
            }

            world.setBlockMetadata(x, y, z, i6 ^ 4);
            world.markBlocksDirty(x, y - 1, z, x, y, z);
            if(Math.random() < 0.5D) {
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            } else {
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            return true;
        }
    }

    public final void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if(((blockID = world.getBlockMetadata(x, y, z)) & 8) != 0) {
            if(world.getBlockId(x, y - 1, z) != this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
                return;
            }
        } else {
            boolean z6 = false;
            if(world.getBlockId(x, y + 1, z) != this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
                z6 = true;
            }

            if(!world.isBlockNormalCube(x, y - 1, z)) {
                world.setBlockWithNotify(x, y, z, 0);
                z6 = true;
                if(world.getBlockId(x, y + 1, z) == this.blockID) {
                    world.setBlockWithNotify(x, y + 1, z, 0);
                }
            }

            if(z6) {
                this.harvestBlock(world, x, y, z, blockID);
            }
        }

    }

    public final int idDropped(int metadata, EaglercraftRandom rand) {
        return (metadata & 8) != 0 ? 0 : Item.doorWood.shiftedIndex;
    }

    public final MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, vector1, vector2);
    }

    private static int getState(int metadata) {
        return (metadata & 4) == 0 ? metadata - 1 & 3 : metadata & 3;
    }

    public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return y >= 127 ? false : world.isBlockNormalCube(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }
}