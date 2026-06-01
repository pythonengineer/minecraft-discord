package net.minecraft.game.world.block;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.item.Item;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockRedstoneWire extends Block {
    private boolean wiresProvidePower = true;

    public BlockRedstoneWire(int i1, int i2) {
        super(i1, i2, Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
        return this.blockIndexInTexture + (i2 > 0 ? 16 : 0);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return 5;
    }

    public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
        return world1.isBlockNormalCube(i2, i3 - 1, i4);
    }

    private void updateAndPropagateCurrentStrength(World world1, int i2, int i3, int i4) {
        int i5 = world1.getBlockMetadata(i2, i3, i4);
        int i6 = 0;
        this.wiresProvidePower = false;
        boolean z7 = world1.isBlockIndirectlyGettingPowered(i2, i3, i4);
        this.wiresProvidePower = true;
        int i8;
        int i9;
        int i10;
        int i11;
        if(z7) {
            i6 = 15;
        } else {
            for(i8 = 0; i8 < 4; ++i8) {
                i9 = i2;
                i10 = i4;
                i11 = i3 - 1;
                if(i8 == 0) {
                    i9 = i2 - 1;
                }

                if(i8 == 1) {
                    ++i9;
                }

                if(i8 == 2) {
                    i10 = i4 - 1;
                }

                if(i8 == 3) {
                    ++i10;
                }

                if(world1.isBlockNormalCube(i9, i3, i10)) {
                    i11 += 2;
                }

                i6 = this.getMaxCurrentStrength(world1, i9, i3, i10, i6);
                i6 = this.getMaxCurrentStrength(world1, i9, i11, i10, i6);
            }

            if(i6 > 0) {
                --i6;
            } else {
                i6 = 0;
            }
        }

        if(i5 != i6) {
            world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
            world1.markBlocksDirty(i2, i3, i4, i2, i3, i4);
            if(i6 > 0) {
                --i6;
            }

            for(i8 = 0; i8 < 4; ++i8) {
                i9 = i2;
                i10 = i4;
                i11 = i3 - 1;
                if(i8 == 0) {
                    i9 = i2 - 1;
                }

                if(i8 == 1) {
                    ++i9;
                }

                if(i8 == 2) {
                    i10 = i4 - 1;
                }

                if(i8 == 3) {
                    ++i10;
                }

                if(world1.isBlockNormalCube(i9, i3, i10)) {
                    i11 += 2;
                }

                int i12 = this.getMaxCurrentStrength(world1, i9, i3, i10, -1);
                if(i12 >= 0 && i12 != i6) {
                    this.updateAndPropagateCurrentStrength(world1, i9, i3, i10);
                }

                i12 = this.getMaxCurrentStrength(world1, i9, i11, i10, -1);
                if(i12 >= 0 && i12 != i6) {
                    this.updateAndPropagateCurrentStrength(world1, i9, i11, i10);
                }
            }

            if(i5 == 0 || i6 == 0) {
                world1.notifyBlocksOfNeighborChange(i2, i3, i4, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2 - 1, i3, i4, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2 + 1, i3, i4, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2, i3, i4 - 1, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2, i3, i4 + 1, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
                world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
            }
        }

    }

    private void notifyWireNeighborsOfNeighborChange(World world1, int i2, int i3, int i4) {
        if(world1.getBlockId(i2, i3, i4) == this.blockID) {
            world1.notifyBlocksOfNeighborChange(i2, i3, i4, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2 - 1, i3, i4, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2 + 1, i3, i4, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2, i3, i4 - 1, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2, i3, i4 + 1, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
            world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
        }
    }

    public void onEntityCollidedWithBlock(World world1, int i2, int i3, int i4, Entity entity5) {
        float f6 = 0.125F;
        float f7 = 0.0625F;
        List list8 = world1.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)i2 + f6), (double)i3, (double)((float)i4 + f6), (double)((float)(i2 + 1) - f6), (double)((float)i3 + f7), (double)((float)(i4 + 1) - f6)));
        if(list8.size() > 0) {
            this.dropBlockAsItem(world1, i2, i3, i4, 0);
            world1.setBlockWithNotify(i2, i3, i4, 0);
        }

    }

    public void onBlockAdded(World world1, int i2, int i3, int i4) {
        super.onBlockAdded(world1, i2, i3, i4);
        this.updateAndPropagateCurrentStrength(world1, i2, i3, i4);
        world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
        world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
        this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3, i4);
        this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3, i4);
        this.notifyWireNeighborsOfNeighborChange(world1, i2, i3, i4 - 1);
        this.notifyWireNeighborsOfNeighborChange(world1, i2, i3, i4 + 1);
        if(world1.isBlockNormalCube(i2 - 1, i3, i4)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3 + 1, i4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3 - 1, i4);
        }

        if(world1.isBlockNormalCube(i2 + 1, i3, i4)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3 + 1, i4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3 - 1, i4);
        }

        if(world1.isBlockNormalCube(i2, i3, i4 - 1)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 + 1, i4 - 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 - 1, i4 - 1);
        }

        if(world1.isBlockNormalCube(i2, i3, i4 + 1)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 + 1, i4 + 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 - 1, i4 + 1);
        }

    }

    public void onBlockRemoval(World world1, int i2, int i3, int i4) {
        super.onBlockRemoval(world1, i2, i3, i4);
        world1.notifyBlocksOfNeighborChange(i2, i3 + 1, i4, this.blockID);
        world1.notifyBlocksOfNeighborChange(i2, i3 - 1, i4, this.blockID);
        this.updateAndPropagateCurrentStrength(world1, i2, i3, i4);
        this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3, i4);
        this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3, i4);
        this.notifyWireNeighborsOfNeighborChange(world1, i2, i3, i4 - 1);
        this.notifyWireNeighborsOfNeighborChange(world1, i2, i3, i4 + 1);
        if(world1.isBlockNormalCube(i2 - 1, i3, i4)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3 + 1, i4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 - 1, i3 - 1, i4);
        }

        if(world1.isBlockNormalCube(i2 + 1, i3, i4)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3 + 1, i4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2 + 1, i3 - 1, i4);
        }

        if(world1.isBlockNormalCube(i2, i3, i4 - 1)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 + 1, i4 - 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 - 1, i4 - 1);
        }

        if(world1.isBlockNormalCube(i2, i3, i4 + 1)) {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 + 1, i4 + 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(world1, i2, i3 - 1, i4 + 1);
        }

    }

    private int getMaxCurrentStrength(World world1, int i2, int i3, int i4, int i5) {
        if(world1.getBlockId(i2, i3, i4) != this.blockID) {
            return i5;
        } else {
            int i6 = world1.getBlockMetadata(i2, i3, i4);
            return i6 > i5 ? i6 : i5;
        }
    }

    public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        int i6 = world1.getBlockMetadata(i2, i3, i4);
        boolean z7 = this.canPlaceBlockAt(world1, i2, i3, i4);
        if(!z7) {
            this.dropBlockAsItem(world1, i2, i3, i4, i6);
            world1.setBlockWithNotify(i2, i3, i4, 0);
        } else {
            this.updateAndPropagateCurrentStrength(world1, i2, i3, i4);
        }

        super.onNeighborBlockChange(world1, i2, i3, i4, i5);
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Item.redstone.shiftedIndex;
    }

    public boolean isIndirectlyPoweringTo(World world1, int i2, int i3, int i4, int i5) {
        return !this.wiresProvidePower ? false : this.isPoweringTo(world1, i2, i3, i4, i5);
    }

    public boolean isPoweringTo(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
        if(!this.wiresProvidePower) {
            return false;
        } else if(iBlockAccess1.getBlockMetadata(i2, i3, i4) == 0) {
            return false;
        } else if(i5 == 1) {
            return true;
        } else {
            boolean z6 = iBlockAccess1.getBlockId(i2 - 1, i3, i4) == Block.redstoneWire.blockID || !iBlockAccess1.isBlockNormalCube(i2 - 1, i3, i4) && iBlockAccess1.getBlockId(i2 - 1, i3 - 1, i4) == Block.redstoneWire.blockID;
            boolean z7 = iBlockAccess1.getBlockId(i2 + 1, i3, i4) == Block.redstoneWire.blockID || !iBlockAccess1.isBlockNormalCube(i2 + 1, i3, i4) && iBlockAccess1.getBlockId(i2 + 1, i3 - 1, i4) == Block.redstoneWire.blockID;
            boolean z8 = iBlockAccess1.getBlockId(i2, i3, i4 - 1) == Block.redstoneWire.blockID || !iBlockAccess1.isBlockNormalCube(i2, i3, i4 - 1) && iBlockAccess1.getBlockId(i2, i3 - 1, i4 - 1) == Block.redstoneWire.blockID;
            boolean z9 = iBlockAccess1.getBlockId(i2, i3, i4 + 1) == Block.redstoneWire.blockID || !iBlockAccess1.isBlockNormalCube(i2, i3, i4 + 1) && iBlockAccess1.getBlockId(i2, i3 - 1, i4 + 1) == Block.redstoneWire.blockID;
            if(iBlockAccess1.isBlockNormalCube(i2 - 1, i3, i4) && iBlockAccess1.getBlockId(i2 - 1, i3 + 1, i4) == Block.redstoneWire.blockID) {
                z6 = true;
            }

            if(iBlockAccess1.isBlockNormalCube(i2 + 1, i3, i4) && iBlockAccess1.getBlockId(i2 + 1, i3 + 1, i4) == Block.redstoneWire.blockID) {
                z7 = true;
            }

            if(iBlockAccess1.isBlockNormalCube(i2, i3, i4 - 1) && iBlockAccess1.getBlockId(i2, i3 + 1, i4 - 1) == Block.redstoneWire.blockID) {
                z8 = true;
            }

            if(iBlockAccess1.isBlockNormalCube(i2, i3, i4 + 1) && iBlockAccess1.getBlockId(i2, i3 + 1, i4 + 1) == Block.redstoneWire.blockID) {
                z9 = true;
            }

            return !z8 && !z7 && !z6 && !z9 && i5 >= 2 && i5 <= 5 ? true : (i5 == 2 && z8 && !z6 && !z7 ? true : (i5 == 3 && z9 && !z6 && !z7 ? true : (i5 == 4 && z6 && !z8 && !z9 ? true : i5 == 5 && z7 && !z8 && !z9)));
        }
    }

    public boolean canProvidePower() {
        return this.wiresProvidePower;
    }
}
