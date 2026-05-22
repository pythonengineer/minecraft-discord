package net.minecraft.game.world;

import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.material.Material;

public final class ChunkCache implements IBlockAccess {
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;
    private World worldObj;

    public ChunkCache(World world, int i2, int i3, int i4, int i5) {
        this.worldObj = world;
        this.chunkX = i2 >> 4;
        this.chunkZ = i3 >> 4;
        i2 = i4 >> 4;
        i3 = i5 >> 4;
        this.chunkArray = new Chunk[i2 - this.chunkX + 1][i3 - this.chunkZ + 1];

        for(i4 = this.chunkX; i4 <= i2; ++i4) {
            for(i5 = this.chunkZ; i5 <= i3; ++i5) {
                this.chunkArray[i4 - this.chunkX][i5 - this.chunkZ] = world.getChunkFromChunkCoords(i4, i5);
            }
        }

    }

    public final int getBlockId(int i1, int i2, int i3) {
        if(i2 < 0) {
            return 0;
        } else if(i2 >= 128) {
            return 0;
        } else {
            int i4 = (i1 >> 4) - this.chunkX;
            int i5 = (i3 >> 4) - this.chunkZ;
            return this.chunkArray[i4][i5].getBlockID(i1 & 15, i2, i3 & 15);
        }
    }

    public final TileEntity getBlockTileEntity(int i1, int i2, int i3) {
        int i4 = (i1 >> 4) - this.chunkX;
        int i5 = (i3 >> 4) - this.chunkZ;
        return this.chunkArray[i4][i5].getChunkBlockTileEntity(i1 & 15, i2, i3 & 15);
    }

    public final float getBrightness(int i1, int i2, int i3) {
        return World.lightBrightnessTable[this.getLightValueExt(i1, i2, i3, true)];
    }

    private int getLightValueExt(int x, int y, int z, boolean z4) {
        if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            int i5;
            int i8;
            if(!z4 || (i8 = this.getBlockId(x, y, z)) != Block.stairSingle.blockID && i8 != Block.tilledField.blockID) {
                if(y < 0) {
                    return 0;
                } else if(y >= 128) {
                    if((i8 = 15 - this.worldObj.skylightSubtracted) < 0) {
                        i8 = 0;
                    }

                    return i8;
                } else {
                    i8 = (x >> 4) - this.chunkX;
                    i5 = (z >> 4) - this.chunkZ;
                    return this.chunkArray[i8][i5].getBlockLightValue(x & 15, y, z & 15, this.worldObj.skylightSubtracted);
                }
            } else {
                i5 = this.getLightValueExt(x, y + 1, z, false);
                i8 = this.getLightValueExt(x + 1, y, z, false);
                int i6 = this.getLightValueExt(x - 1, y, z, false);
                int i7 = this.getLightValueExt(x, y, z + 1, false);
                x = this.getLightValueExt(x, y, z - 1, false);
                if(i8 > i5) {
                    i5 = i8;
                }

                if(i6 > i5) {
                    i5 = i6;
                }

                if(i7 > i5) {
                    i5 = i7;
                }

                if(x > i5) {
                    i5 = x;
                }

                return i5;
            }
        } else {
            return 15;
        }
    }

    public final int getBlockMetadata(int i1, int i2, int i3) {
        if(i2 < 0) {
            return 0;
        } else if(i2 >= 128) {
            return 0;
        } else {
            int i4 = (i1 >> 4) - this.chunkX;
            int i5 = (i3 >> 4) - this.chunkZ;
            return this.chunkArray[i4][i5].getBlockMetadata(i1 & 15, i2, i3 & 15);
        }
    }

    public final Material getBlockMaterial(int i1, int i2, int i3) {
        return (i1 = this.getBlockId(i1, i2, i3)) == 0 ? Material.air : Block.blocksList[i1].blockMaterial;
    }

    public final boolean isBlockNormalCube(int i1, int i2, int i3) {
        Block block4;
        return (block4 = Block.blocksList[this.getBlockId(i1, i2, i3)]) == null ? false : block4.isOpaqueCube();
    }
}