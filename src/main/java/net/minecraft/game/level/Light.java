package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.game.level.block.Block;

public final class Light {
    private int lightingUpdateCounter = 0;
    private List lightingUpdateList = new ArrayList();
    private int[] lightingUpdateList3 = new int[65536];
    private World worldObj;
    private int worldWidth;
    private int worldLength;
    private int worldHeight;
    private byte[] blocks;
    private byte[] data;
    private int[] heightMap;
    private List k = new ArrayList();
    private List l = new ArrayList();
    private List m = new ArrayList();
    private MetadataChunkBlock metadataChunkBlock = null;
    private int o = 0;
    private int skylightSubtracted;

    public Light(World var1) {
        this.worldObj = var1;
        this.worldWidth = var1.width;
        this.worldLength = var1.length;
        this.worldHeight = var1.height;
        this.blocks = var1.blocks;
        this.data = var1.data;
        this.heightMap = var1.heightMap;
    }

    public final void updateSkylight(int var1, int var2, int var3, int var4) {
        this.m.add(new MetadataChunkBlock(this, var1, var2, 0, var3, var4, 1));
    }

    public final void updateDaylightCycle(int var1) {
        if(var1 > 15) {
            var1 = 15;
        }

        if(var1 < 0) {
            var1 = 0;
        }

        this.skylightSubtracted = var1 - this.worldObj.skylightSubtracted;
        if(this.skylightSubtracted != 0) {
            this.o = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = var1;
            this.metadataChunkBlock = new MetadataChunkBlock(this, 0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
        }
    }

    public final void updateBlockLight(int var1, int var2, int var3, int var4, int var5, int var6) {
        this.l.add(new MetadataChunkBlock(this, var1, var2, var3, var4, var5, var6));
    }

    private void b(int var1, int var2, int var3, int var4, int var5, int var6) {
        for(var2 = var2; var2 < var5; ++var2) {
            for(int var7 = var3; var7 < var6; ++var7) {
                for(int var8 = var1; var8 < var4; ++var8) {
                    this.lightingUpdateList3[this.lightingUpdateCounter++] = var8 << 20 | var2 << 10 | var7;
                    if(this.lightingUpdateCounter > this.lightingUpdateList3.length - 32) {
                        int var9 = this.lightingUpdateList3[--this.lightingUpdateCounter];
                        this.lightingUpdateList3[this.lightingUpdateList3.length - 1] = this.lightingUpdateCounter;
                        this.lightingUpdateList.add(this.lightingUpdateList3);
                        this.lightingUpdateList3 = new int[this.lightingUpdateList3.length];
                        this.lightingUpdateCounter = 1;
                        this.lightingUpdateList3[0] = var9;
                    }
                }
            }
        }

    }

    public final void updateLight() {
        int var1 = 64;

        int var3;
        while(this.k.size() > 0 && var1-- > 0) {
            MetadataChunkBlock var2 = (MetadataChunkBlock)this.k.remove(0);

            for(var3 = 0; var3 < this.worldObj.worldAccesses.size(); ++var3) {
                ((IWorldAccess)this.worldObj.worldAccesses.get(var3)).markBlockRangeNeedsUpdate(var2.x, var2.y, var2.z, var2.maxX, var2.maxY, var2.maxZ);
            }
        }

        int var6;
        int var7;
        int var8;
        int var9;
        int var10;
        int var11;
        int var12;
        int var13;
        int var14;
        int var22;
        if(this.metadataChunkBlock != null) {
            var3 = 64;
            Light var19 = this;
            var22 = this.metadataChunkBlock.x;
            int var23 = this.metadataChunkBlock.maxX;
            var6 = this.metadataChunkBlock.z;
            var7 = this.metadataChunkBlock.maxZ;
            var8 = this.metadataChunkBlock.maxY;

            for(var9 = var22; var9 < var23; ++var9) {
                if(var3-- <= 0) {
                    var19.metadataChunkBlock.x = var22;
                    var19.metadataChunkBlock.maxY = var8;
                }

                for(var10 = var6; var10 < var7; ++var10) {
                    for(var11 = var19.heightMap[var9 + var10 * var19.worldWidth] - 1; var11 > 0 && Block.lightOpacity[var19.blocks[(var11 * var19.worldLength + var10) * var19.worldWidth + var9]] < 100; --var11) {
                    }

                    for(var12 = var11 + 1; var12 < var19.worldHeight; ++var12) {
                        var13 = (var12 * var19.worldLength + var10) * var19.worldWidth + var9;
                        if(Block.lightValue[var19.blocks[var13]] == 0) {
                            var14 = var19.data[var13] & 15;
                            if(var14 <= var19.o) {
                                if(var19.skylightSubtracted < 0 && var14 > 0) {
                                    --var19.data[var13];
                                } else if(var19.skylightSubtracted > 0 && var14 < 15) {
                                    ++var19.data[var13];
                                }
                            }
                        }
                    }

                    if(var11 < var8) {
                        var8 = var11;
                    }
                }
            }

            var19.lightingUpdateCounter = 0;
            var19.lightingUpdateList.clear();

            for(var10 = 0; var10 < var19.worldWidth; var10 += 32) {
                for(var11 = 0; var11 < var19.worldLength; var11 += 32) {
                    var19.l.add(new MetadataChunkBlock(var19, var10, var8, var11, var10 + 32, var19.worldHeight, var11 + 32));
                    var19.k.add(new MetadataChunkBlock(var19, var10, var8, var11, var10 + 32, var19.worldHeight, var11 + 32));
                }
            }

            for(var10 = 0; var10 < var19.worldObj.worldAccesses.size(); ++var10) {
                IWorldAccess var24 = (IWorldAccess)var19.worldObj.worldAccesses.get(var10);
                var24.updateAllRenderers();
            }

            var19.metadataChunkBlock = null;
        } else {
            for(int var20 = 0; var20 < 200; ++var20) {
                boolean var21 = false;
                MetadataChunkBlock var4;
                if(this.l.size() > 0) {
                    var21 = true;
                    var4 = (MetadataChunkBlock)this.l.remove(0);
                    this.b(var4.x, var4.y, var4.z, var4.maxX, var4.maxY, var4.maxZ);
                }

                Light var5;
                if(this.m.size() > 0) {
                    var21 = true;
                    var4 = (MetadataChunkBlock)this.m.remove(0);
                    var9 = var4.maxY;
                    var8 = var4.maxX;
                    var7 = var4.y;
                    var6 = var4.x;
                    var5 = this;

                    for(var10 = var6; var10 < var6 + var8; ++var10) {
                        for(var11 = var7; var11 < var7 + var9; ++var11) {
                            var12 = var5.heightMap[var10 + var11 * var5.worldWidth];

                            for(var13 = var5.worldHeight - 1; var13 > 0 && Block.lightOpacity[var5.blocks[(var13 * var5.worldLength + var11) * var5.worldWidth + var10]] == 0; --var13) {
                            }

                            var5.heightMap[var10 + var11 * var5.worldWidth] = var13 + 1;
                            if(var12 != var13) {
                                var14 = var12 < var13 ? var12 : var13;
                                var3 = var12 > var13 ? var12 : var13;
                                var5.b(var10, var14, var11, var10 + 1, var3, var11 + 1);
                            }
                        }
                    }
                }

                var5 = this;
                var6 = this.worldObj.skylightSubtracted;
                var7 = -999;
                var8 = -999;
                var9 = -999;
                var10 = -999;
                var11 = -999;
                var12 = -999;
                var13 = 1024;
                var14 = 0;

                while(var13-- > 0 && (var5.lightingUpdateCounter > 0 || var5.lightingUpdateList.size() > 0)) {
                    ++var14;
                    if(var5.lightingUpdateCounter == 0) {
                        var5.lightingUpdateList3 = (int[])var5.lightingUpdateList.remove(var5.lightingUpdateList.size() - 1);
                        var5.lightingUpdateCounter = var5.lightingUpdateList3[var5.lightingUpdateList3.length - 1];
                    }

                    if(var5.lightingUpdateCounter > var5.lightingUpdateList3.length - 32) {
                        var3 = var5.lightingUpdateList3[--var5.lightingUpdateCounter];
                        var5.lightingUpdateList3[var5.lightingUpdateList3.length - 1] = var5.lightingUpdateCounter;
                        var5.lightingUpdateList.add(var5.lightingUpdateList3);
                        var5.lightingUpdateList3 = new int[var5.lightingUpdateList3.length];
                        var5.lightingUpdateCounter = 1;
                        var5.lightingUpdateList3[0] = var3;
                    } else {
                        var3 = var5.lightingUpdateList3[--var5.lightingUpdateCounter];
                        var1 = var3 >> 20 & 1023;
                        var22 = var3 >> 10 & 1023;
                        var3 &= 1023;
                        int var15 = var5.heightMap[var1 + var3 * var5.worldWidth];
                        var15 = var22 >= var15 ? var6 : 0;
                        byte var16 = var5.blocks[(var22 * var5.worldLength + var3) * var5.worldWidth + var1];
                        int var17 = Block.lightOpacity[var16];
                        if(var17 > 100) {
                            var15 = 0;
                        } else if(var15 < 14) {
                            var17 = var17;
                            if(var17 == 0) {
                                var17 = 1;
                            }

                            int var18;
                            if(var1 > 0) {
                                var18 = (var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + (var1 - 1)] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }

                            if(var1 < var5.worldWidth - 1) {
                                var18 = (var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + var1 + 1] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }

                            if(var22 > 0) {
                                var18 = (var5.data[((var22 - 1) * var5.worldLength + var3) * var5.worldWidth + var1] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }

                            if(var22 < var5.worldHeight - 1) {
                                var18 = (var5.data[((var22 + 1) * var5.worldLength + var3) * var5.worldWidth + var1] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }

                            if(var3 > 0) {
                                var18 = (var5.data[(var22 * var5.worldLength + (var3 - 1)) * var5.worldWidth + var1] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }

                            if(var3 < var5.worldLength - 1) {
                                var18 = (var5.data[(var22 * var5.worldLength + var3 + 1) * var5.worldWidth + var1] & 15) - var17;
                                if(var18 > var15) {
                                    var15 = var18;
                                }
                            }
                        }

                        if(var15 < Block.lightValue[var16]) {
                            var15 = Block.lightValue[var16];
                        }

                        var17 = var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + var1] & 15;
                        if(var17 != var15) {
                            var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + var1] = (byte)((var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + var1] & 240) + var15);
                            if(var1 > 0 && (var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + (var1 - 1)] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 - 1 << 20 | var22 << 10 | var3;
                            }

                            if(var1 < var5.worldWidth - 1 && (var5.data[(var22 * var5.worldLength + var3) * var5.worldWidth + var1 + 1] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 + 1 << 20 | var22 << 10 | var3;
                            }

                            if(var22 > 0 && (var5.data[((var22 - 1) * var5.worldLength + var3) * var5.worldWidth + var1] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 << 20 | var22 - 1 << 10 | var3;
                            }

                            if(var22 < var5.worldHeight - 1 && (var5.data[((var22 + 1) * var5.worldLength + var3) * var5.worldWidth + var1] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 << 20 | var22 + 1 << 10 | var3;
                            }

                            if(var3 > 0 && (var5.data[(var22 * var5.worldLength + (var3 - 1)) * var5.worldWidth + var1] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 << 20 | var22 << 10 | var3 - 1;
                            }

                            if(var3 < var5.worldLength - 1 && (var5.data[(var22 * var5.worldLength + var3 + 1) * var5.worldWidth + var1] & 15) != var15 - 1) {
                                var5.lightingUpdateList3[var5.lightingUpdateCounter++] = var1 << 20 | var22 << 10 | var3 + 1;
                            }

                            if(var7 == -999) {
                                var7 = var1;
                                var8 = var1;
                                var9 = var22;
                                var10 = var22;
                                var11 = var3;
                                var12 = var3;
                            }

                            if(var1 < var7) {
                                var7 = var1;
                            } else if(var1 > var8) {
                                var8 = var1;
                            }

                            if(var22 > var10) {
                                var10 = var22;
                            } else if(var22 < var9) {
                                var9 = var22;
                            }

                            if(var3 < var11) {
                                var11 = var3;
                            } else if(var3 > var12) {
                                var12 = var3;
                            }
                        }
                    }
                }

                if(var7 > -999) {
                    var5.k.add(new MetadataChunkBlock(var5, var7, var9, var11, var8, var10, var12));
                }

                if(var14 > 0) {
                    var21 = true;
                }
            }

        }
    }

    public final String debugSkylightUpdates() {
        return "" + (this.l.size() + this.m.size());
    }
}
