package net.minecraft.game.world;

import net.minecraft.game.world.block.Block;

public final class Chunk {
    private byte[] blocks;
    private World worldObj;
    private NibbleArray data;
    private NibbleArray skyLightMap;
    private NibbleArray blockLightMap;
    private byte[] heightMap;
    private int lowestBlockHeight;
    private final int xPosition;
    private final int zPosition;
    public static boolean isLit;

    private Chunk(World var1, int var2, int var3) {
        this.worldObj = var1;
        this.xPosition = var2;
        this.zPosition = var3;
        this.heightMap = new byte[256];
    }

    public Chunk(World var1, byte[] var2, int var3, int var4) {
        this(var1, var3, var4);
        this.blocks = var2;
        this.data = new NibbleArray(var2.length);
        this.skyLightMap = new NibbleArray(var2.length);
        this.blockLightMap = new NibbleArray(var2.length);
    }

    public final boolean isAtLocation(int var1, int var2) {
        return var1 == this.xPosition && var2 == this.zPosition;
    }

    public final int getHeightValue(int var1, int var2) {
        return this.heightMap[var2 << 4 | var1] & 255;
    }

    public final void generateHeightMap() {
        int var1 = 127;

        int var2;
        int var3;
        for(var2 = 0; var2 < 16; ++var2) {
            for(var3 = 0; var3 < 16; ++var3) {
                this.heightMap[var3 << 4 | var2] = 127;
                this.relightBlock(var2, 127, var3);
                if((this.heightMap[var3 << 4 | var2] & 255) < var1) {
                    var1 = this.heightMap[var3 << 4 | var2] & 255;
                }
            }
        }

        this.lowestBlockHeight = var1;

        for(var2 = 0; var2 < 16; ++var2) {
            for(var3 = 0; var3 < 16; ++var3) {
                this.updateSkylight_do(var2, var3);
            }
        }

    }

    private void updateSkylight_do(int var1, int var2) {
        int var3 = this.getHeightValue(var1, var2);
        var1 += this.xPosition << 4;
        var2 += this.zPosition << 4;
        this.checkSkylightNeighborHeight(var1 - 1, var2, var3);
        this.checkSkylightNeighborHeight(var1 + 1, var2, var3);
        this.checkSkylightNeighborHeight(var1, var2 - 1, var3);
        this.checkSkylightNeighborHeight(var1, var2 + 1, var3);
    }

    private void checkSkylightNeighborHeight(int var1, int var2, int var3) {
        int var4 = this.worldObj.getHeightValue(var1, var2);
        if(var4 > var3) {
            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var1, var3, var2, var1, var4, var2);
        }

    }

    private void relightBlock(int var1, int var2, int var3) {
        int var4 = this.heightMap[var3 << 4 | var1] & 255;
        int var5 = var4;
        if(var2 > var4) {
            var5 = var2;
        }

        while(var5 > 0 && Block.lightOpacity[this.getBlockID(var1, var5 - 1, var3)] == 0) {
            --var5;
        }

        if(var5 != var4) {
            this.worldObj.markBlocksDirtyVertical(var1, var3, var5, var4);
            this.heightMap[var3 << 4 | var1] = (byte)var5;
            int var6;
            int var7;
            if(var5 < this.lowestBlockHeight) {
                this.lowestBlockHeight = var5;
            } else {
                var2 = 127;

                for(var6 = 0; var6 < 16; ++var6) {
                    for(var7 = 0; var7 < 16; ++var7) {
                        if((this.heightMap[var7 << 4 | var6] & 255) < var2) {
                            var2 = this.heightMap[var7 << 4 | var6] & 255;
                        }
                    }
                }

                this.lowestBlockHeight = var2;
            }

            var2 = (this.xPosition << 4) + var1;
            var6 = (this.zPosition << 4) + var3;
            if(var5 < var4) {
                for(var7 = var5; var7 < var4; ++var7) {
                    this.skyLightMap.setNibble(var1, var7, var3, 15);
                }
            } else {
                this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var2, var4, var6, var2, var5, var6);

                for(var7 = var4; var7 < var5; ++var7) {
                    this.skyLightMap.setNibble(var1, var7, var3, 0);
                }
            }

            var7 = 15;

            while(var5 > 0 && var7 > 0) {
                --var5;
                var4 = Block.lightOpacity[this.getBlockID(var1, var5, var3)];
                if(var4 == 0) {
                    var4 = 1;
                }

                var7 -= var4;
                if(var7 < 0) {
                    var7 = 0;
                }

                this.skyLightMap.setNibble(var1, var5, var3, var7);
                this.worldObj.neighborLightPropagationChanged(EnumSkyBlock.Sky, var2, var5, var6, -1);
            }

        }
    }

    public final int getBlockID(int var1, int var2, int var3) {
        return this.blocks[var1 << 11 | var3 << 7 | var2];
    }

    public final void setBlockID(int var1, int var2, int var3, int var4) {
        int var5 = this.heightMap[var3 << 4 | var1] & 255;
        this.blocks[var1 << 11 | var3 << 7 | var2] = (byte)var4;
        if(Block.lightOpacity[var4] != 0) {
            if(var2 >= var5) {
                this.relightBlock(var1, var2 + 1, var3);
            }
        } else if(var2 == var5 - 1) {
            this.relightBlock(var1, var2, var3);
        }

        var4 = (this.xPosition << 4) + var1;
        var5 = (this.zPosition << 4) + var3;
        this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var4, var2, var5, var4, var2, var5);
        this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, var4, var2, var5, var4, var2, var5);
        this.updateSkylight_do(var1, var3);
    }

    public final int getBlockMetadata(int var1, int var2, int var3) {
        return this.data.getNibble(var1, var2, var3);
    }

    public final void setBlockMetadata(int var1, int var2, int var3, int var4) {
        this.data.setNibble(var1, var2, var3, var4);
    }

    public final int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
        return var1 == EnumSkyBlock.Sky ? this.skyLightMap.getNibble(var2, var3, var4) : (var1 == EnumSkyBlock.Block ? this.blockLightMap.getNibble(var2, var3, var4) : 0);
    }

    public final void getBlockLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
        if(var1 == EnumSkyBlock.Sky) {
            this.skyLightMap.setNibble(var2, var3, var4, var5);
        } else if(var1 == EnumSkyBlock.Block) {
            this.blockLightMap.setNibble(var2, var3, var4, var5);
        }
    }

    public final int getBlockLightValue(int var1, int var2, int var3, int var4) {
        int var5 = this.skyLightMap.getNibble(var1, var2, var3);
        if(var5 > 0) {
            isLit = true;
        }

        var5 -= var4;
        var1 = this.blockLightMap.getNibble(var1, var2, var3);
        if(var1 > var5) {
            var5 = var1;
        }

        return var5;
    }

    public final boolean canBlockSeeTheSky(int var1, int var2, int var3) {
        return var2 >= (this.heightMap[var3 << 4 | var1] & 255);
    }
}
