package net.minecraft.game.world;

import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.material.Material;

public class ChunkCache implements IBlockAccess {
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;
    private World worldObj;

    public ChunkCache(World world, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.worldObj = world;
        this.chunkX = i2 >> 4;
        this.chunkZ = i4 >> 4;
        int i8 = i5 >> 4;
        int i9 = i7 >> 4;
        this.chunkArray = new Chunk[i8 - this.chunkX + 1][i9 - this.chunkZ + 1];

        for(int i10 = this.chunkX; i10 <= i8; ++i10) {
            for(int i11 = this.chunkZ; i11 <= i9; ++i11) {
                this.chunkArray[i10 - this.chunkX][i11 - this.chunkZ] = world.getChunkFromChunkCoords(i10, i11);
            }
        }

    }

    public int getBlockId(int xCoord, int yCoord, int zCoord) {
        if(yCoord < 0) {
            return 0;
        } else if(yCoord >= 128) {
            return 0;
        } else {
            int i4 = (xCoord >> 4) - this.chunkX;
            int i5 = (zCoord >> 4) - this.chunkZ;
            return this.chunkArray[i4][i5].getBlockID(xCoord & 15, yCoord, zCoord & 15);
        }
    }

    public TileEntity getBlockTileEntity(int xCoord, int yCoord, int zCoord) {
        int i4 = (xCoord >> 4) - this.chunkX;
        int i5 = (zCoord >> 4) - this.chunkZ;
        return this.chunkArray[i4][i5].getChunkBlockTileEntity(xCoord & 15, yCoord, zCoord & 15);
    }

    public float getBrightness(int nya1, int nya2, int nya3) {
        return World.lightBrightnessTable[this.getLightValue(nya1, nya2, nya3)];
    }

    public int getLightValue(int xCoord, int yCoord, int zCoord) {
        return this.getLightValueExt(xCoord, yCoord, zCoord, true);
    }

    public int getLightValueExt(int xCoord, int yCoord, int zCoord, boolean z4) {
        if(xCoord >= -32000000 && zCoord >= -32000000 && xCoord < 32000000 && zCoord <= 32000000) {
            int i5;
            int i6;
            if(z4) {
                i5 = this.getBlockId(xCoord, yCoord, zCoord);
                if(i5 == Block.stairSingle.blockID || i5 == Block.tilledField.blockID) {
                    i6 = this.getLightValueExt(xCoord, yCoord + 1, zCoord, false);
                    int i7 = this.getLightValueExt(xCoord + 1, yCoord, zCoord, false);
                    int i8 = this.getLightValueExt(xCoord - 1, yCoord, zCoord, false);
                    int i9 = this.getLightValueExt(xCoord, yCoord, zCoord + 1, false);
                    int i10 = this.getLightValueExt(xCoord, yCoord, zCoord - 1, false);
                    if(i7 > i6) {
                        i6 = i7;
                    }

                    if(i8 > i6) {
                        i6 = i8;
                    }

                    if(i9 > i6) {
                        i6 = i9;
                    }

                    if(i10 > i6) {
                        i6 = i10;
                    }

                    return i6;
                }
            }

            if(yCoord < 0) {
                return 0;
            } else if(yCoord >= 128) {
                i5 = 15 - this.worldObj.skylightSubtracted;
                if(i5 < 0) {
                    i5 = 0;
                }

                return i5;
            } else {
                i5 = (xCoord >> 4) - this.chunkX;
                i6 = (zCoord >> 4) - this.chunkZ;
                return this.chunkArray[i5][i6].getBlockLightValue(xCoord & 15, yCoord, zCoord & 15, this.worldObj.skylightSubtracted);
            }
        } else {
            return 15;
        }
    }

    public int getBlockMetadata(int xCoord, int yCoord, int zCoord) {
        if(yCoord < 0) {
            return 0;
        } else if(yCoord >= 128) {
            return 0;
        } else {
            int i4 = (xCoord >> 4) - this.chunkX;
            int i5 = (zCoord >> 4) - this.chunkZ;
            return this.chunkArray[i4][i5].getBlockMetadata(xCoord & 15, yCoord, zCoord & 15);
        }
    }

    public Material getBlockMaterial(int nya1, int nya2, int nya3) {
        int i4 = this.getBlockId(nya1, nya2, nya3);
        return i4 == 0 ? Material.air : Block.blocksList[i4].blockMaterial;
    }

    public boolean isBlockNormalCube(int xCoord, int yCoord, int zCoord) {
        Block block4 = Block.blocksList[this.getBlockId(xCoord, yCoord, zCoord)];
        return block4 == null ? false : block4.isOpaqueCube();
    }
}
