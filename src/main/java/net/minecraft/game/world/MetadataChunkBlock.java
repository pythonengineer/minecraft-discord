package net.minecraft.game.world;

import net.minecraft.game.world.block.Block;

public class MetadataChunkBlock {
    public final EnumSkyBlock skyBlock;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public MetadataChunkBlock(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.skyBlock = skyBlock;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void updateLight(World world1) {
        for(int i2 = this.minX; i2 <= this.maxX; ++i2) {
            for(int i3 = this.minZ; i3 <= this.maxZ; ++i3) {
                if(world1.blockExists(i2, 0, i3)) {
                    for(int i4 = this.minY; i4 <= this.maxY; ++i4) {
                        if(i4 >= 0 && i4 < 128) {
                            int i5 = world1.getSavedLightValue(this.skyBlock, i2, i4, i3);
                            int i7 = world1.getBlockId(i2, i4, i3);
                            int i8 = Block.lightOpacity[i7];
                            if(i8 == 0) {
                                i8 = 1;
                            }

                            int i9 = 0;
                            if(this.skyBlock == EnumSkyBlock.Sky) {
                                if(world1.canExistingBlockSeeTheSky(i2, i4, i3)) {
                                    i9 = 15;
                                }
                            } else if(this.skyBlock == EnumSkyBlock.Block) {
                                i9 = Block.lightValue[i7];
                            }

                            int i10;
                            int i16;
                            if(i8 >= 15 && i9 == 0) {
                                i16 = 0;
                            } else {
                                i10 = world1.getSavedLightValue(this.skyBlock, i2 - 1, i4, i3);
                                int i11 = world1.getSavedLightValue(this.skyBlock, i2 + 1, i4, i3);
                                int i12 = world1.getSavedLightValue(this.skyBlock, i2, i4 - 1, i3);
                                int i13 = world1.getSavedLightValue(this.skyBlock, i2, i4 + 1, i3);
                                int i14 = world1.getSavedLightValue(this.skyBlock, i2, i4, i3 - 1);
                                int i15 = world1.getSavedLightValue(this.skyBlock, i2, i4, i3 + 1);
                                i16 = i10;
                                if(i11 > i10) {
                                    i16 = i11;
                                }

                                if(i12 > i16) {
                                    i16 = i12;
                                }

                                if(i13 > i16) {
                                    i16 = i13;
                                }

                                if(i14 > i16) {
                                    i16 = i14;
                                }

                                if(i15 > i16) {
                                    i16 = i15;
                                }

                                i16 -= i8;
                                if(i16 < 0) {
                                    i16 = 0;
                                }

                                if(i9 > i16) {
                                    i16 = i9;
                                }
                            }

                            if(i5 != i16) {
                                world1.setLightValue(this.skyBlock, i2, i4, i3, i16);
                                i10 = i16 - 1;
                                if(i10 < 0) {
                                    i10 = 0;
                                }

                                world1.neighborLightPropagationChanged(this.skyBlock, i2 - 1, i4, i3, i10);
                                world1.neighborLightPropagationChanged(this.skyBlock, i2, i4 - 1, i3, i10);
                                world1.neighborLightPropagationChanged(this.skyBlock, i2, i4, i3 - 1, i10);
                                if(i2 + 1 >= this.maxX) {
                                    world1.neighborLightPropagationChanged(this.skyBlock, i2 + 1, i4, i3, i10);
                                }

                                if(i4 + 1 >= this.maxY) {
                                    world1.neighborLightPropagationChanged(this.skyBlock, i2, i4 + 1, i3, i10);
                                }

                                if(i3 + 1 >= this.maxZ) {
                                    world1.neighborLightPropagationChanged(this.skyBlock, i2, i4, i3 + 1, i10);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public boolean getLightUpdated(int i1, int i2, int i3, int i4, int i5, int i6) {
        if(i1 >= this.minX && i2 >= this.minY && i3 >= this.minZ && i4 <= this.maxX && i5 <= this.maxY && i6 <= this.maxZ) {
            return true;
        } else {
            byte b7 = 1;
            if(i1 >= this.minX - b7 && i2 >= this.minY - b7 && i3 >= this.minZ - b7 && i4 <= this.maxX + b7 && i5 <= this.maxY + b7 && i6 <= this.maxZ + b7) {
                if(i1 < this.minX) {
                    this.minX = i1;
                }

                if(i2 < this.minY) {
                    this.minY = i2;
                }

                if(i3 < this.minZ) {
                    this.minZ = i3;
                }

                if(i4 > this.maxX) {
                    this.maxX = i4;
                }

                if(i5 > this.maxY) {
                    this.maxY = i5;
                }

                if(i6 > this.maxZ) {
                    this.maxZ = i6;
                }

                return true;
            } else {
                return false;
            }
        }
    }
}
