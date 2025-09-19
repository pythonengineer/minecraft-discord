package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class World {
    private int MAX_TICKS = 200;
    private static float[] lightBrightnessTable = new float[16];
    public int width;
    public int length;
    public int height;
    public byte[] blocks;
    public byte[] data;
    public String name;
    public String authorName;
    public long createTime;
    public int xSpawn;
    public int ySpawn;
    public int zSpawn;
    public float rotSpawn;
    public int defaultFluid = Block.waterMoving.blockID;
    private List worldAccesses = new ArrayList();
    private List tickList = new LinkedList();
    public Map map = new HashMap();
    private int[] heightMap;
    public EaglercraftRandom random = new EaglercraftRandom();
    private EaglercraftRandom rand = new EaglercraftRandom();
    private int randId = this.random.nextInt();
    public EntityMap entityMap;
    public int waterLevel;
    public int groundLevel;
    public int cloudHeight;
    public int skyColor = 10079487;
    public int fogColor = 16777215;
    public int cloudColor = 16777215;
    private int updateLCG = 0;
    private int playTime = 0;
    public Entity playerEntity;
    public boolean survivalWorld = true;
    public float skyBrightness = 1.0F;
    private static short floodFillCounter;
    private short[] floodFillCounters = new short[1048576];
    private int[] coords = new int[1048576];
    private int[] floodedBlocks = new int[1048576];

    public final void load() {
        if(this.blocks == null) {
            throw new RuntimeException("The level is corrupt!");
        } else {
            this.worldAccesses = new ArrayList();
            this.heightMap = new int[this.width * this.length];
            Arrays.fill(this.heightMap, this.height);
            this.updateSkylight(0, 0, this.width, this.length);
            this.random = new EaglercraftRandom();
            this.randId = this.random.nextInt();
            this.tickList = new ArrayList();
            if(this.entityMap == null) {
                this.entityMap = new EntityMap(this.width, this.height, this.length);
            }

        }
    }

    public final void generate(int var1, int var2, int var3, byte[] var4) {
        this.width = var1;
        this.length = var3;
        this.height = var2;
        this.blocks = var4;

        int var5;
        int var6;
        int var7;
        for(var2 = 0; var2 < this.width; ++var2) {
            for(var5 = 0; var5 < this.length; ++var5) {
                for(var6 = 0; var6 < this.height; ++var6) {
                    var7 = 0;
                    if(var6 <= 1 && var6 < this.groundLevel - 1 && var4[((var6 + 1) * this.length + var5) * this.width + var2] == 0) {
                        var7 = Block.lavaStill.blockID;
                    } else if(var6 < this.groundLevel - 1) {
                        var7 = Block.bedrock.blockID;
                    } else if(var6 < this.groundLevel) {
                        if(this.groundLevel > this.waterLevel && this.defaultFluid == Block.waterMoving.blockID) {
                            var7 = Block.grass.blockID;
                        } else {
                            var7 = Block.dirt.blockID;
                        }
                    } else if(var6 < this.waterLevel) {
                        var7 = this.defaultFluid;
                    }

                    var4[(var6 * this.length + var5) * this.width + var2] = (byte)var7;
                    if(var6 == 1 && var2 != 0 && var5 != 0 && var2 != this.width - 1 && var5 != this.length - 1) {
                        var6 = this.height - 2;
                    }
                }
            }
        }

        this.data = new byte[var4.length];
        this.heightMap = new int[var1 * var3];
        Arrays.fill(this.heightMap, this.height);
        World var10 = this;
        var2 = (int)(15.0F * this.skyBrightness);

        int var12;
        for(var3 = 0; var3 < var10.width; ++var3) {
            for(var12 = 0; var12 < var10.length; ++var12) {
                for(var5 = var10.height - 1; var5 > 0 && Block.lightOpacity[var10.getBlockId(var3, var5, var12)] == 0; --var5) {
                }

                var10.heightMap[var3 + var12 * var10.width] = var5 + 1;

                for(var5 = 0; var5 < var10.height; ++var5) {
                    var6 = var10.heightMap[var3 + var12 * var10.width];
                    var6 = var5 >= var6 ? var2 : 0;
                    byte var13 = var10.blocks[(var5 * var10.length + var12) * var10.width + var3];
                    if(var6 < Block.lightValue[var13]) {
                        var6 = Block.lightValue[var13];
                    }

                    var10.data[(var5 * var10.length + var12) * var10.width + var3] = (byte)((var10.data[(var5 * var10.length + var12) * var10.width + var3] & 240) + var6);
                }
            }
        }

        for(var3 = 0; var3 < var10.width; ++var3) {
            for(var12 = 0; var12 < var10.length; ++var12) {
                var10.updateLight(var3, 0, var12, var3 + 1, var10.height, var12 + 1);
            }
        }

        for(var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            ((IWorldAccess)this.worldAccesses.get(var2)).loadRenderers();
        }

        this.tickList.clear();
        var10 = this;
        EaglercraftRandom var11 = new EaglercraftRandom();
        var3 = 0;

        label112:
        while(true) {
            ++var3;
            var12 = var11.nextInt(var10.width / 2) + var10.width / 4;
            var5 = var11.nextInt(var10.length / 2) + var10.length / 4;
            var6 = var10.getFirstUncoveredBlock(var12, var5) + 1;
            if(var3 == 10000) {
                var10.xSpawn = var12;
                var10.ySpawn = var10.height + 100;
                var10.zSpawn = var5;
                break;
            }

            if(var6 >= 4 && var6 > var10.waterLevel) {
                for(var7 = var12 - 5; var7 <= var12 + 5; ++var7) {
                    for(int var8 = var6; var8 <= var6 + 2; ++var8) {
                        for(int var9 = var5 - 5; var9 <= var5 + 5; ++var9) {
                            if(var10.getBlockMaterial(var7, var8, var9).isSolid()) {
                                continue label112;
                            }
                        }
                    }
                }

                var10.xSpawn = var12;
                var10.ySpawn = var6;
                var10.zSpawn = var5;
                break;
            }
        }

        this.load();
        System.gc();
    }

    private void updateSkylight(int var1, int var2, int var3, int var4) {
        for(int var5 = var1; var5 < var1 + var3; ++var5) {
            for(int var6 = var2; var6 < var2 + var4; ++var6) {
                int var7 = this.heightMap[var5 + var6 * this.width];

                int var8;
                for(var8 = this.height - 1; var8 > 0 && Block.lightOpacity[this.getBlockId(var5, var8, var6)] == 0; --var8) {
                }

                this.heightMap[var5 + var6 * this.width] = var8 + 1;
                if(var7 != var8) {
                    int var9 = var7 < var8 ? var7 : var8;
                    var7 = var7 > var8 ? var7 : var8;
                    this.updateLight(var5, var9, var6, var5 + 1, var7, var6 + 1);
                }
            }
        }

        this.updateLight(0, 0, 0, 10, 10, 10);
    }

    private void updateLight(int var1, int var2, int var3, int var4, int var5, int var6) {
        int var7 = 0;
        ArrayList var8 = new ArrayList();
        int[] var9 = new int[1024];

        int var10;
        int var11;
        int var12;
        for(var10 = var1; var10 < var4; ++var10) {
            for(var11 = var3; var11 < var6; ++var11) {
                for(var12 = var2; var12 < var5; ++var12) {
                    var9[var7++] = var10 << 20 | var12 << 10 | var11;
                }
            }
        }

        var10 = (int)(15.0F * this.skyBrightness);

        while(var7 > 0 || var8.size() > 0) {
            if(var7 == 0) {
                var9 = (int[])var8.remove(var8.size() - 1);
                var7 = var9[var9.length - 1];
            }

            if(var7 > var9.length - 32) {
                --var7;
                var11 = var9[var7];
                var9[var9.length - 1] = var7;
                var8.add(var9);
                var9 = new int[1024];
                var7 = 1;
                var9[0] = var11;
            } else {
                --var7;
                var11 = var9[var7];
                var12 = var11 >> 20 & 1023;
                int var13 = var11 >> 10 & 1023;
                var11 &= 1023;
                int var14 = this.heightMap[var12 + var11 * this.width];
                var14 = var13 >= var14 ? var10 : 0;
                byte var15 = this.blocks[(var13 * this.length + var11) * this.width + var12];
                int var16 = Block.lightOpacity[var15];
                if(var16 > 100) {
                    var14 = 0;
                } else if(var14 < 14) {
                    var16 = var16;
                    if(var16 == 0) {
                        var16 = 1;
                    }

                    int var17;
                    if(var12 > 0) {
                        var17 = (this.data[(var13 * this.length + var11) * this.width + (var12 - 1)] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }

                    if(var12 < this.width - 1) {
                        var17 = (this.data[(var13 * this.length + var11) * this.width + var12 + 1] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }

                    if(var13 > 0) {
                        var17 = (this.data[((var13 - 1) * this.length + var11) * this.width + var12] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }

                    if(var13 < this.height - 1) {
                        var17 = (this.data[((var13 + 1) * this.length + var11) * this.width + var12] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }

                    if(var11 > 0) {
                        var17 = (this.data[(var13 * this.length + (var11 - 1)) * this.width + var12] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }

                    if(var11 < this.length - 1) {
                        var17 = (this.data[(var13 * this.length + var11 + 1) * this.width + var12] & 15) - var16;
                        if(var17 > var14) {
                            var14 = var17;
                        }
                    }
                }

                if(var14 < Block.lightValue[var15]) {
                    var14 = Block.lightValue[var15];
                }

                if(var12 < var1) {
                    var1 = var12;
                } else if(var12 > var4) {
                    var4 = var12;
                }

                if(var13 > var5) {
                    var5 = var13;
                } else if(var13 < var2) {
                    var2 = var13;
                }

                if(var11 < var3) {
                    var3 = var11;
                } else if(var11 > var6) {
                    var6 = var11;
                }

                var16 = this.data[(var13 * this.length + var11) * this.width + var12] & 15;
                if(var16 != var14) {
                    this.data[(var13 * this.length + var11) * this.width + var12] = (byte)((this.data[(var13 * this.length + var11) * this.width + var12] & 240) + var14);
                    if(var12 > 0 && (this.data[(var13 * this.length + var11) * this.width + (var12 - 1)] & 15) != var14 - 1) {
                        var9[var7++] = var12 - 1 << 20 | var13 << 10 | var11;
                    }

                    if(var12 < this.width - 1 && (this.data[(var13 * this.length + var11) * this.width + var12 + 1] & 15) != var14 - 1) {
                        var9[var7++] = var12 + 1 << 20 | var13 << 10 | var11;
                    }

                    if(var13 > 0 && (this.data[((var13 - 1) * this.length + var11) * this.width + var12] & 15) != var14 - 1) {
                        var9[var7++] = var12 << 20 | var13 - 1 << 10 | var11;
                    }

                    if(var13 < this.height - 1 && (this.data[((var13 + 1) * this.length + var11) * this.width + var12] & 15) != var14 - 1) {
                        var9[var7++] = var12 << 20 | var13 + 1 << 10 | var11;
                    }

                    if(var11 > 0 && (this.data[(var13 * this.length + (var11 - 1)) * this.width + var12] & 15) != var14 - 1) {
                        var9[var7++] = var12 << 20 | var13 << 10 | var11 - 1;
                    }

                    if(var11 < this.length - 1 && (this.data[(var13 * this.length + var11 + 1) * this.width + var12] & 15) != var14 - 1) {
                        var9[var7++] = var12 << 20 | var13 << 10 | var11 + 1;
                    }
                }
            }
        }

        Iterator var18 = this.worldAccesses.iterator();

        while(var18.hasNext()) {
            IWorldAccess var19 = (IWorldAccess)var18.next();
            var19.markBlockRangeNeedsUpdate(var1, var2, var3, var4, var5, var6);
        }

    }

    public final void addWorldAccess(IWorldAccess var1) {
        this.worldAccesses.add(var1);
    }

    public final void finalize() {
    }

    public final void removeWorldAccess(IWorldAccess var1) {
        this.worldAccesses.remove(var1);
    }

    public final ArrayList getCollidingBoundingBoxes(AxisAlignedBB var1) {
        ArrayList var2 = new ArrayList();
        int var3 = (int)var1.minX;
        int var4 = (int)var1.maxX + 1;
        int var5 = (int)var1.minY;
        int var6 = (int)var1.maxY + 1;
        int var7 = (int)var1.minZ;
        int var8 = (int)var1.maxZ + 1;
        if(var1.minX < 0.0F) {
            --var3;
        }

        if(var1.minY < 0.0F) {
            --var5;
        }

        if(var1.minZ < 0.0F) {
            --var7;
        }

        for(var3 = var3; var3 < var4; ++var3) {
            for(int var9 = var5; var9 < var6; ++var9) {
                for(int var10 = var7; var10 < var8; ++var10) {
                    Block var11 = Block.blocksList[this.getBlockId(var3, var9, var10)];
                    AxisAlignedBB var12;
                    if(var11 != null) {
                        var12 = var11.getCollisionBoundingBoxFromPool(var3, var9, var10);
                        if(var12 != null && var1.intersectsWith(var12)) {
                            var2.add(var12);
                        }
                    } else if(this.groundLevel < 0 && (var9 < this.groundLevel || var9 < this.waterLevel)) {
                        var12 = Block.bedrock.getCollisionBoundingBoxFromPool(var3, var9, var10);
                        if(var12 != null && var1.intersectsWith(var12)) {
                            var2.add(var12);
                        }
                    }
                }
            }
        }

        return var2;
    }

    public final void swap(int var1, int var2, int var3, int var4, int var5, int var6) {
        int var7 = this.getBlockId(var1, var2, var3);
        int var8 = this.getBlockId(var4, var5, var6);
        this.setBlock(var1, var2, var3, var8);
        this.setBlock(var4, var5, var6, var7);
        this.notifyBlocksOfNeighborChange(var1, var2, var3, var8);
        this.notifyBlocksOfNeighborChange(var4, var5, var6, var7);
    }

    public final boolean setBlock(int var1, int var2, int var3, int var4) {
        if(var1 > 0 && var2 > 0 && var3 > 0 && var1 < this.width - 1 && var2 < this.height - 1 && var3 < this.length - 1) {
            if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
                return false;
            } else {
                if(var4 == 0 && (var1 == 0 || var3 == 0 || var1 == this.width - 1 || var3 == this.length - 1) && var2 >= this.groundLevel && var2 < this.waterLevel) {
                    var4 = Block.waterMoving.blockID;
                }

                byte var5 = this.blocks[(var2 * this.length + var3) * this.width + var1];
                this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
                if(var5 != 0) {
                    Block.blocksList[var5].onBlockRemoval(this, var1, var2, var3);
                }

                if(var4 != 0) {
                    Block.blocksList[var4].onBlockAdded(this, var1, var2, var3);
                }

                int var8 = this.random.nextInt(8);
                int var7 = var3;
                int var6 = var2;
                int var9 = var1;
                if(var1 < 0) {
                    var9 = 0;
                } else if(var1 >= this.width) {
                    var9 = this.width - 1;
                }

                if(var2 < 0) {
                    var6 = 0;
                } else if(var2 >= this.height) {
                    var6 = this.height - 1;
                }

                if(var3 < 0) {
                    var7 = 0;
                } else if(var3 >= this.length) {
                    var7 = this.length - 1;
                }

                this.data[(var6 * this.length + var7) * this.width + var9] = (byte)((this.data[(var6 * this.length + var7) * this.width + var9] & 15) + (var8 << 4));
                this.updateSkylight(var1, var3, 1, 1);
                this.updateLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);

                for(var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
                    ((IWorldAccess)this.worldAccesses.get(var4)).markBlockAndNeighborsNeedsUpdate(var1, var2, var3);
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public final boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
        if(this.setBlock(var1, var2, var3, var4)) {
            this.notifyBlocksOfNeighborChange(var1, var2, var3, var4);
            return true;
        } else {
            return false;
        }
    }

    public final void notifyBlocksOfNeighborChange(int var1, int var2, int var3, int var4) {
        this.notifyBlockOfNeighborChange(var1 - 1, var2, var3, var4);
        this.notifyBlockOfNeighborChange(var1 + 1, var2, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2 - 1, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2 + 1, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2, var3 - 1, var4);
        this.notifyBlockOfNeighborChange(var1, var2, var3 + 1, var4);
    }

    public final boolean setTileNoUpdate(int var1, int var2, int var3, int var4) {
        if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
            if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
                return false;
            } else {
                this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
                this.updateLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);
                return true;
            }
        } else {
            return false;
        }
    }

    private void notifyBlockOfNeighborChange(int var1, int var2, int var3, int var4) {
        if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
            Block var5 = Block.blocksList[this.blocks[(var2 * this.length + var3) * this.width + var1]];
            if(var5 != null) {
                var5.onNeighborBlockChange(this, var1, var2, var3, var4);
            }

        }
    }

    public final boolean isHalfLit(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return this.getBlockMetadata(var1, var2, var3) > 3;
    }

    public final boolean isFullyLit(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return this.getBlockMetadata(var1, var2, var3) < 14;
    }

    public final int getBlockId(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return this.blocks[(var2 * this.length + var3) * this.width + var1] & 255;
    }

    public final boolean isBlockNormalCube(int var1, int var2, int var3) {
        Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
        return var4 == null ? false : var4.isOpaqueCube();
    }

    public final void updateEntities() {
        this.entityMap.updateEntities();
    }

    public final void tick() {
        ++this.playTime;
        int var1 = 1;

        int var2;
        for(var2 = 1; 1 << var1 < this.width; ++var1) {
        }

        while(1 << var2 < this.length) {
            ++var2;
        }

        int var3 = this.length - 1;
        int var4 = this.width - 1;
        int var5 = this.height - 1;
        int var6 = this.tickList.size();
        if(var6 > this.MAX_TICKS) {
            var6 = this.MAX_TICKS;
        }

        int var7;
        int var10;
        for(var7 = 0; var7 < var6; ++var7) {
            NextTickListEntry var8 = (NextTickListEntry)this.tickList.remove(0);
            if(var8.scheduledTime > 0) {
                --var8.scheduledTime;
                this.tickList.add(var8);
            } else {
                int var12 = var8.zCoord;
                int var11 = var8.yCoord;
                var10 = var8.xCoord;
                if(var10 >= 0 && var11 >= 0 && var12 >= 0 && var10 < this.width && var11 < this.height && var12 < this.length) {
                    byte var9 = this.blocks[(var8.yCoord * this.length + var8.zCoord) * this.width + var8.xCoord];
                    if(var9 == var8.blockID && var9 > 0) {
                        Block.blocksList[var9].updateTick(this, var8.xCoord, var8.yCoord, var8.zCoord, this.random);
                    }
                }
            }
        }

        this.updateLCG += this.width * this.length * this.height;
        var6 = this.updateLCG / MAX_TICKS;
        this.updateLCG -= var6 * MAX_TICKS;

        for(var7 = 0; var7 < var6; ++var7) {
            this.randId = this.randId * 3 + 1013904223;
            int var13 = this.randId >> 2;
            int var14 = var13 & var4;
            var10 = var13 >> var1 & var3;
            var13 = var13 >> var1 + var2 & var5;
            byte var15 = this.blocks[(var13 * this.length + var10) * this.width + var14];
            if(Block.tickOnLoad[var15]) {
                Block.blocksList[var15].updateTick(this, var14, var13, var10, this.random);
            }
        }

    }

    public final int entitiesInLevelList(Class var1) {
        int var2 = 0;

        for(int var3 = 0; var3 < this.entityMap.all.size(); ++var3) {
            Entity var4 = (Entity)this.entityMap.all.get(var3);
            if(var1.isAssignableFrom(var4.getClass())) {
                ++var2;
            }
        }

        return var2;
    }

    public final int getGroundLevel() {
        return this.groundLevel;
    }

    public final int getWaterLevel() {
        return this.waterLevel;
    }

    public final boolean getIsAnyLiquid(AxisAlignedBB var1) {
        int var2 = (int)var1.minX;
        int var3 = (int)var1.maxX + 1;
        int var4 = (int)var1.minY;
        int var5 = (int)var1.maxY + 1;
        int var6 = (int)var1.minZ;
        int var7 = (int)var1.maxZ + 1;
        if(var1.minX < 0.0F) {
            --var2;
        }

        if(var1.minY < 0.0F) {
            --var4;
        }

        if(var1.minZ < 0.0F) {
            --var6;
        }

        if(var2 < 0) {
            var2 = 0;
        }

        if(var4 < 0) {
            var4 = 0;
        }

        if(var6 < 0) {
            var6 = 0;
        }

        if(var3 > this.width) {
            var3 = this.width;
        }

        if(var5 > this.height) {
            var5 = this.height;
        }

        if(var7 > this.length) {
            var7 = this.length;
        }

        for(int var10 = var2; var10 < var3; ++var10) {
            for(var2 = var4; var2 < var5; ++var2) {
                for(int var8 = var6; var8 < var7; ++var8) {
                    Block var9 = Block.blocksList[this.getBlockId(var10, var2, var8)];
                    if(var9 != null && var9.material.getIsLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public final boolean isBoundingBoxBurning(AxisAlignedBB var1) {
        int var2 = (int)var1.minX;
        int var3 = (int)var1.maxX + 1;
        int var4 = (int)var1.minY;
        int var5 = (int)var1.maxY + 1;
        int var6 = (int)var1.minZ;
        int var10 = (int)var1.maxZ + 1;

        for(var2 = var2; var2 < var3; ++var2) {
            for(int var7 = var4; var7 < var5; ++var7) {
                for(int var8 = var6; var8 < var10; ++var8) {
                    int var9 = this.getBlockId(var2, var7, var8);
                    if(var9 == Block.fire.blockID || var9 == Block.lavaMoving.blockID || var9 == Block.lavaStill.blockID) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public final boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2) {
        int var3 = (int)var1.minX;
        int var4 = (int)var1.maxX + 1;
        int var5 = (int)var1.minY;
        int var6 = (int)var1.maxY + 1;
        int var7 = (int)var1.minZ;
        int var11 = (int)var1.maxZ + 1;

        for(var3 = var3; var3 < var4; ++var3) {
            for(int var8 = var5; var8 < var6; ++var8) {
                for(int var9 = var7; var9 < var11; ++var9) {
                    Block var10 = Block.blocksList[this.getBlockId(var3, var8, var9)];
                    if(var10 != null && var10.material == var2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public final void scheduleBlockUpdate(int var1, int var2, int var3, int var4) {
        NextTickListEntry var5 = new NextTickListEntry(var1, var2, var3, var4);
        if(var4 > 0) {
            var3 = Block.blocksList[var4].tickRate();
            var5.scheduledTime = var3;
        }

        this.tickList.add(var5);
    }

    public final boolean checkIfAABBIsClear1(AxisAlignedBB var1) {
        return this.entityMap.getEntitiesWithinAABBExcludingEntity((Entity)null, var1).size() == 0;
    }

    public final boolean checkIfAABBIsClear(AxisAlignedBB var1) {
        List var4 = this.entityMap.getEntitiesWithinAABBExcludingEntity((Entity)null, var1);

        for(int var2 = 0; var2 < var4.size(); ++var2) {
            Entity var3 = (Entity)var4.get(var2);
            if(var3.preventEntitySpawning) {
                return false;
            }
        }

        return true;
    }

    public final List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
        return this.entityMap.getEntitiesWithinAABBExcludingEntity(var1, var2);
    }

    public final boolean isSolid(float var1, float var2, float var3, float var4) {
        return this.isSolid(var1 - 0.1F, var2 - 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 - 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 + 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 - 0.1F, var2 + 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 - 0.1F, var3 - 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 - 0.1F, var3 + 0.1F) ? true : (this.isSolid(var1 + 0.1F, var2 + 0.1F, var3 - 0.1F) ? true : this.isSolid(var1 + 0.1F, var2 + 0.1F, var3 + 0.1F)))))));
    }

    private boolean isSolid(float var1, float var2, float var3) {
        int var4 = this.getBlockId((int)var1, (int)var2, (int)var3);
        return var4 > 0 && Block.blocksList[var4].isOpaqueCube();
    }

    private int getFirstUncoveredBlock(int var1, int var2) {
        int var3;
        for(var3 = this.height; (this.getBlockId(var1, var3 - 1, var2) == 0 || Block.blocksList[this.getBlockId(var1, var3 - 1, var2)].material == Material.air) && var3 > 0; --var3) {
        }

        return var3;
    }

    public final void setSpawnLocation(int var1, int var2, int var3, float var4) {
        this.xSpawn = var1;
        this.ySpawn = var2;
        this.zSpawn = var3;
        this.rotSpawn = var4;
    }

    public final float getBlockLightValue(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return lightBrightnessTable[this.data[(var2 * this.length + var3) * this.width + var1] & 15];
    }

    private byte getBlockMetadata(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return (byte)(this.data[(var2 * this.length + var3) * this.width + var1] & 15);
    }

    public final byte getBlockBrightness(int var1, int var2, int var3) {
        if(var1 < 0) {
            var1 = 0;
        } else if(var1 >= this.width) {
            var1 = this.width - 1;
        }

        if(var2 < 0) {
            var2 = 0;
        } else if(var2 >= this.height) {
            var2 = this.height - 1;
        }

        if(var3 < 0) {
            var3 = 0;
        } else if(var3 >= this.length) {
            var3 = this.length - 1;
        }

        return (byte)(this.data[(var2 * this.length + var3) * this.width + var1] >>> 4 & 15);
    }

    public final Material getBlockMaterial(int var1, int var2, int var3) {
        var1 = this.getBlockId(var1, var2, var3);
        return var1 == 0 ? Material.air : Block.blocksList[var1].material;
    }

    public final boolean isWater(int var1, int var2, int var3) {
        var1 = this.getBlockId(var1, var2, var3);
        return var1 > 0 && Block.blocksList[var1].material == Material.water;
    }

    public final MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
        if(!Float.isNaN(var1.xCoord) && !Float.isNaN(var1.yCoord) && !Float.isNaN(var1.zCoord)) {
            if(!Float.isNaN(var2.xCoord) && !Float.isNaN(var2.yCoord) && !Float.isNaN(var2.zCoord)) {
                int var3 = MathHelper.floor_float(var2.xCoord);
                int var4 = MathHelper.floor_float(var2.yCoord);
                int var5 = MathHelper.floor_float(var2.zCoord);
                int var6 = MathHelper.floor_float(var1.xCoord);
                int var7 = MathHelper.floor_float(var1.yCoord);
                int var8 = MathHelper.floor_float(var1.zCoord);
                int var9 = 20;

                while(var9-- >= 0) {
                    if(Float.isNaN(var1.xCoord) || Float.isNaN(var1.yCoord) || Float.isNaN(var1.zCoord)) {
                        return null;
                    }

                    if(var6 == var3 && var7 == var4 && var8 == var5) {
                        return null;
                    }

                    float var10 = 999.0F;
                    float var11 = 999.0F;
                    float var12 = 999.0F;
                    if(var3 > var6) {
                        var10 = (float)var6 + 1.0F;
                    }

                    if(var3 < var6) {
                        var10 = (float)var6;
                    }

                    if(var4 > var7) {
                        var11 = (float)var7 + 1.0F;
                    }

                    if(var4 < var7) {
                        var11 = (float)var7;
                    }

                    if(var5 > var8) {
                        var12 = (float)var8 + 1.0F;
                    }

                    if(var5 < var8) {
                        var12 = (float)var8;
                    }

                    float var13 = 999.0F;
                    float var14 = 999.0F;
                    float var15 = 999.0F;
                    float var16 = var2.xCoord - var1.xCoord;
                    float var17 = var2.yCoord - var1.yCoord;
                    float var18 = var2.zCoord - var1.zCoord;
                    if(var10 != 999.0F) {
                        var13 = (var10 - var1.xCoord) / var16;
                    }

                    if(var11 != 999.0F) {
                        var14 = (var11 - var1.yCoord) / var17;
                    }

                    if(var12 != 999.0F) {
                        var15 = (var12 - var1.zCoord) / var18;
                    }

                    byte var19;
                    if(var13 < var14 && var13 < var15) {
                        if(var3 > var6) {
                            var19 = 4;
                        } else {
                            var19 = 5;
                        }

                        var1.xCoord = var10;
                        var1.yCoord += var17 * var13;
                        var1.zCoord += var18 * var13;
                    } else if(var14 < var15) {
                        if(var4 > var7) {
                            var19 = 0;
                        } else {
                            var19 = 1;
                        }

                        var1.xCoord += var16 * var14;
                        var1.yCoord = var11;
                        var1.zCoord += var18 * var14;
                    } else {
                        if(var5 > var8) {
                            var19 = 2;
                        } else {
                            var19 = 3;
                        }

                        var1.xCoord += var16 * var15;
                        var1.yCoord += var17 * var15;
                        var1.zCoord = var12;
                    }

                    Vec3D var20 = new Vec3D(var1.xCoord, var1.yCoord, var1.zCoord);
                    var6 = (int)(var20.xCoord = (float)MathHelper.floor_float(var1.xCoord));
                    if(var19 == 5) {
                        --var6;
                        ++var20.xCoord;
                    }

                    var7 = (int)(var20.yCoord = (float)MathHelper.floor_float(var1.yCoord));
                    if(var19 == 1) {
                        --var7;
                        ++var20.yCoord;
                    }

                    var8 = (int)(var20.zCoord = (float)MathHelper.floor_float(var1.zCoord));
                    if(var19 == 3) {
                        --var8;
                        ++var20.zCoord;
                    }

                    int var21 = this.getBlockId(var6, var7, var8);
                    Block var23 = Block.blocksList[var21];
                    if(var21 > 0 && var23.isCollidable()) {
                        MovingObjectPosition var22 = var23.collisionRayTrace(this, var6, var7, var8, var1, var2);
                        if(var22 != null) {
                            return var22;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public final boolean growTrees(int var1, int var2, int var3) {
        int var4 = this.random.nextInt(3) + 4;
        boolean var5 = true;
        if(var2 > 0 && var2 + var4 + 1 <= this.height) {
            int var6;
            int var8;
            int var9;
            int var10;
            for(var6 = var2; var6 <= var2 + 1 + var4; ++var6) {
                byte var7 = 1;
                if(var6 == var2) {
                    var7 = 0;
                }

                if(var6 >= var2 + 1 + var4 - 2) {
                    var7 = 2;
                }

                for(var8 = var1 - var7; var8 <= var1 + var7 && var5; ++var8) {
                    for(var9 = var3 - var7; var9 <= var3 + var7 && var5; ++var9) {
                        if(var8 >= 0 && var6 >= 0 && var9 >= 0 && var8 < this.width && var6 < this.height && var9 < this.length) {
                            var10 = this.blocks[(var6 * this.length + var9) * this.width + var8] & 255;
                            if(var10 != 0) {
                                var5 = false;
                            }
                        } else {
                            var5 = false;
                        }
                    }
                }
            }

            if(!var5) {
                return false;
            } else {
                var6 = this.blocks[((var2 - 1) * this.length + var3) * this.width + var1] & 255;
                if((var6 == Block.grass.blockID || var6 == Block.dirt.blockID) && var2 < this.height - var4 - 1) {
                    this.setBlockWithNotify(var1, var2 - 1, var3, Block.dirt.blockID);

                    int var13;
                    for(var13 = var2 - 3 + var4; var13 <= var2 + var4; ++var13) {
                        var8 = var13 - (var2 + var4);
                        var9 = 1 - var8 / 2;

                        for(var10 = var1 - var9; var10 <= var1 + var9; ++var10) {
                            int var12 = var10 - var1;

                            for(var6 = var3 - var9; var6 <= var3 + var9; ++var6) {
                                int var11 = var6 - var3;
                                if(Math.abs(var12) != var9 || Math.abs(var11) != var9 || this.random.nextInt(2) != 0 && var8 != 0) {
                                    this.setBlockWithNotify(var10, var13, var6, Block.leaves.blockID);
                                }
                            }
                        }
                    }

                    for(var13 = 0; var13 < var4; ++var13) {
                        this.setBlockWithNotify(var1, var2 + var13, var3, Block.wood.blockID);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public final Entity getPlayerEntity() {
        return this.playerEntity;
    }

    public final void spawnEntityInWorld(Entity var1) {
        this.entityMap.insert(var1);
        var1.setWorld(this);
    }

    public final void releaseEntitySkin(Entity var1) {
        this.entityMap.remove(var1);
    }

    public final void createExplosion(Entity var1, float var2, float var3, float var4, float var5) {
        TreeSet var21 = new TreeSet();

        int var6;
        int var7;
        float var8;
        float var9;
        float var10;
        float var15;
        int var16;
        int var17;
        int var18;
        int var19;
        int var23;
        for(var23 = 0; var23 < 16; ++var23) {
            for(var6 = 0; var6 < 16; ++var6) {
                for(var7 = 0; var7 < 16; ++var7) {
                    if(var23 == 0 || var23 == 15 || var6 == 0 || var6 == 15 || var7 == 0 || var7 == 15) {
                        var8 = (float)var23 / 15.0F * 2.0F - 1.0F;
                        var9 = (float)var6 / 15.0F * 2.0F - 1.0F;
                        var10 = (float)var7 / 15.0F * 2.0F - 1.0F;
                        float var11 = (float)Math.sqrt((double)(var8 * var8 + var9 * var9 + var10 * var10));
                        var8 /= var11;
                        var9 /= var11;
                        var10 /= var11;
                        float var12 = 4.0F * (0.7F + this.random.nextFloat() * 0.6F);
                        float var13 = var2;
                        float var14 = var3;

                        for(var15 = var4; var12 > 0.0F; var12 -= 0.22500001F) {
                            var16 = (int)var13;
                            var17 = (int)var14;
                            var18 = (int)var15;
                            var19 = this.getBlockId(var16, var17, var18);
                            if(var19 > 0) {
                                var12 -= (Block.blocksList[var19].getExplosionResistance() + 0.3F) * 0.3F;
                            }

                            if(var12 > 0.0F) {
                                int var20 = var16 + (var17 << 10) + (var18 << 10 << 10);
                                var21.add(Integer.valueOf(var20));
                            }

                            var13 += var8 * 0.3F;
                            var14 += var9 * 0.3F;
                            var15 += var10 * 0.3F;
                        }
                    }
                }
            }
        }

        var23 = (int)(var2 - 8.0F - 1.0F);
        var6 = (int)(var2 + 8.0F + 1.0F);
        var7 = (int)(var3 - 8.0F - 1.0F);
        int var26 = (int)(var3 + 8.0F + 1.0F);
        int var27 = (int)(var4 - 8.0F - 1.0F);
        int var28 = (int)(var4 + 8.0F + 1.0F);
        List var29 = this.entityMap.getEntities((Entity)null, (float)var23, (float)var7, (float)var27, (float)var6, (float)var26, (float)var28);
        Vec3D var30 = new Vec3D(var2, var3, var4);

        float var24;
        float var25;
        float var40;
        for(int var31 = 0; var31 < var29.size(); ++var31) {
            Entity var33 = (Entity)var29.get(var31);
            var24 = var33.posX - var2;
            var25 = var33.posY - var3;
            var5 = var33.posZ - var4;
            var15 = MathHelper.sqrt_float(var24 * var24 + var25 * var25 + var5 * var5) / 8.0F;
            if(var15 <= 1.0F) {
                var5 = var33.posX - var2;
                float var36 = var33.posY - var3;
                float var37 = var33.posZ - var4;
                float var38 = MathHelper.sqrt_float(var5 * var5 + var36 * var36 + var37 * var37);
                var5 /= var38;
                var36 /= var38;
                var37 /= var38;
                float var39 = this.getBlockDensity(var30, var33.boundingBox);
                var40 = (1.0F - var15) * var39;
                var33.attackEntityFrom((Entity)null, (int)((var40 * var40 + var40) / 2.0F * 64.0F + 1.0F));
                var33.motionX += var5 * var40;
                var33.motionY += var36 * var40;
                var33.motionZ += var37 * var40;
            }
        }

        ArrayList var32 = new ArrayList();
        var32.addAll(var21);

        for(int var34 = var32.size() - 1; var34 >= 0; --var34) {
            int var35 = ((Integer)var32.get(var34)).intValue();
            var23 = var35 & 1023;
            var16 = var35 >> 10 & 1023;
            var17 = var35 >> 20 & 1023;
            if(var23 >= 0 && var16 >= 0 && var17 >= 0 && var23 < this.width && var16 < this.height && var17 < this.length) {
                var18 = this.getBlockId(var23, var16, var17);

                for(var19 = 0; var19 <= 0; ++var19) {
                    var40 = (float)var23 + this.random.nextFloat();
                    var24 = (float)var16 + this.random.nextFloat();
                    float var22 = (float)var17 + this.random.nextFloat();
                    var25 = var40 - var2;
                    var8 = var24 - var3;
                    var9 = var22 - var4;
                    var10 = MathHelper.sqrt_float(var25 * var25 + var8 * var8 + var9 * var9);
                    var25 /= var10;
                    var8 /= var10;
                    var9 /= var10;
                    var10 = 0.5F / (var10 / 4.0F + 0.1F);
                    var10 *= this.random.nextFloat() * this.random.nextFloat() + 0.3F;
                    var25 *= var10;
                    var8 *= var10;
                    var9 *= var10;
                    this.spawnParticle("explode", (var40 + var2) / 2.0F, (var24 + var3) / 2.0F, (var22 + var4) / 2.0F, var25, var8, var9);
                    this.spawnParticle("smoke", var40, var24, var22, var25, var8, var9);
                }

                if(var18 > 0) {
                    Block.blocksList[var18].dropBlockAsItemWithChance(this, var23, var16, var17, 0.3F);
                    this.setBlockWithNotify(var23, var16, var17, 0);
                    Block.blocksList[var18].onBlockDestroyedByExplosion(this, var23, var16, var17);
                }
            }
        }

    }

    private float getBlockDensity(Vec3D var1, AxisAlignedBB var2) {
        float var3 = 1.0F / ((var2.maxX - var2.minX) * 2.0F + 1.0F);
        float var4 = 1.0F / ((var2.maxY - var2.minY) * 2.0F + 1.0F);
        float var5 = 1.0F / ((var2.maxZ - var2.minZ) * 2.0F + 1.0F);
        int var6 = 0;
        int var7 = 0;

        for(float var8 = 0.0F; var8 <= 1.0F; var8 += var3) {
            for(float var9 = 0.0F; var9 <= 1.0F; var9 += var4) {
                for(float var10 = 0.0F; var10 <= 1.0F; var10 += var5) {
                    float var11 = var2.minX + (var2.maxX - var2.minX) * var8;
                    float var12 = var2.minY + (var2.maxY - var2.minY) * var9;
                    float var13 = var2.minZ + (var2.maxZ - var2.minZ) * var10;
                    if(this.rayTraceBlocks(new Vec3D(var11, var12, var13), var1) == null) {
                        ++var6;
                    }

                    ++var7;
                }
            }
        }

        return (float)var6 / (float)var7;
    }

    public final Entity findSubclassOf(Class var1) {
        for(int var2 = 0; var2 < this.entityMap.all.size(); ++var2) {
            Entity var3 = (Entity)this.entityMap.all.get(var2);
            if(var1.isAssignableFrom(var3.getClass())) {
                return var3;
            }
        }

        return null;
    }

    public final int getMapHeight(int var1, int var2) {
        return this.heightMap[var1 + var2 * this.width];
    }

    public final int fluidFlowCheck(int var1, int var2, int var3, int var4, int var5) {
        if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
            int var6 = var1;
            int var7 = var3;
            int var8 = ((var2 << 10) + var3 << 10) + var1;
            byte var9 = 0;
            int var20 = var9 + 1;
            this.coords[0] = var1 + (var3 << 10);
            int var11 = -9999;
            if(var4 == Block.waterStill.blockID || var4 == Block.waterMoving.blockID) {
                var11 = Block.waterSource.blockID;
            }

            if(var4 == Block.lavaStill.blockID || var4 == Block.lavaMoving.blockID) {
                var11 = Block.lavaSource.blockID;
            }

            int var10;
            boolean var12;
            label170:
            do {
                var12 = false;
                int var13 = -1;
                var10 = 0;
                if(++floodFillCounter == 30000) {
                    Arrays.fill(this.floodFillCounters, (short)0);
                    floodFillCounter = 1;
                }

                while(true) {
                    int var14;
                    do {
                        if(var20 <= 0) {
                            ++var2;
                            int[] var21 = this.floodedBlocks;
                            this.floodedBlocks = this.coords;
                            this.coords = var21;
                            var20 = var10;
                            continue label170;
                        }

                        --var20;
                        var14 = this.coords[var20];
                    } while(this.floodFillCounters[var14] == floodFillCounter);

                    var1 = var14 % 1024;
                    var3 = var14 / 1024;
                    int var15 = var3 - var7;

                    for(var15 *= var15; var1 > 0 && this.floodFillCounters[var14 - 1] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var5); --var14) {
                        --var1;
                    }

                    if(var1 > 0 && this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var11) {
                        var12 = true;
                    }

                    boolean var16 = false;
                    boolean var17 = false;

                    for(boolean var18 = false; var1 < this.width && this.floodFillCounters[var14] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1] == var5); ++var1) {
                        byte var19;
                        boolean var22;
                        if(var3 > 0) {
                            var19 = this.blocks[(var2 * this.length + var3 - 1) * this.width + var1];
                            if(var19 == var11) {
                                var12 = true;
                            }

                            var22 = this.floodFillCounters[var14 - 1024] != floodFillCounter && (var19 == var4 || var19 == var5);
                            if(var22 && !var16) {
                                this.coords[var20++] = var14 - 1024;
                            }

                            var16 = var22;
                        }

                        if(var3 < this.length - 1) {
                            var19 = this.blocks[(var2 * this.length + var3 + 1) * this.width + var1];
                            if(var19 == var11) {
                                var12 = true;
                            }

                            var22 = this.floodFillCounters[var14 + 1024] != floodFillCounter && (var19 == var4 || var19 == var5);
                            if(var22 && !var17) {
                                this.coords[var20++] = var14 + 1024;
                            }

                            var17 = var22;
                        }

                        if(var2 < this.height - 1) {
                            var19 = this.blocks[((var2 + 1) * this.length + var3) * this.width + var1];
                            var22 = var19 == var4 || var19 == var5;
                            if(var22 && !var18) {
                                this.floodedBlocks[var10++] = var14;
                            }

                            var18 = var22;
                        }

                        int var23 = var1 - var6;
                        var23 *= var23;
                        var23 += var15;
                        if(var23 > var13) {
                            var13 = var23;
                            var8 = ((var2 << 10) + var3 << 10) + var1;
                        }

                        this.floodFillCounters[var14++] = floodFillCounter;
                    }

                    if(var1 < this.width && this.blocks[(var2 * this.length + var3) * this.width + var1] == var11) {
                        var12 = true;
                    }
                }
            } while(var10 > 0);

            if(var12) {
                return -9999;
            } else {
                return var8;
            }
        } else {
            return -1;
        }
    }

    public final int floodFill(int var1, int var2, int var3, int var4, int var5) {
        if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
            if(++floodFillCounter == 30000) {
                Arrays.fill(this.floodFillCounters, (short)0);
                floodFillCounter = 1;
            }

            byte var6 = 0;
            int var11 = var6 + 1;
            this.coords[0] = var1 + (var3 << 10);

            do {
                int var7;
                do {
                    if(var11 <= 0) {
                        return 1;
                    }

                    --var11;
                    var7 = this.coords[var11];
                } while(this.floodFillCounters[var7] == floodFillCounter);

                var1 = var7 % 1024;
                var3 = var7 / 1024;
                if(var1 == 0 || var1 == this.width - 1 || var2 == 0 || var2 == this.height - 1 || var3 == 0 || var3 == this.length - 1) {
                    return 2;
                }

                while(var1 > 0 && this.floodFillCounters[var7 - 1] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == var5)) {
                    --var1;
                    --var7;
                }

                if(var1 > 0 && this.blocks[(var2 * this.length + var3) * this.width + var1 - 1] == 0) {
                    return 0;
                }

                boolean var8 = false;

                for(boolean var9 = false; var1 < this.width && this.floodFillCounters[var7] != floodFillCounter && (this.blocks[(var2 * this.length + var3) * this.width + var1] == var4 || this.blocks[(var2 * this.length + var3) * this.width + var1] == var5); ++var1) {
                    if(var1 == 0 || var1 == this.width - 1) {
                        return 2;
                    }

                    byte var10;
                    boolean var12;
                    if(var3 > 0) {
                        var10 = this.blocks[(var2 * this.length + var3 - 1) * this.width + var1];
                        if(var10 == 0) {
                            return 0;
                        }

                        var12 = this.floodFillCounters[var7 - 1024] != floodFillCounter && (var10 == var4 || var10 == var5);
                        if(var12 && !var8) {
                            this.coords[var11++] = var7 - 1024;
                        }

                        var8 = var12;
                    }

                    if(var3 < this.length - 1) {
                        var10 = this.blocks[(var2 * this.length + var3 + 1) * this.width + var1];
                        if(var10 == 0) {
                            return 0;
                        }

                        var12 = this.floodFillCounters[var7 + 1024] != floodFillCounter && (var10 == var4 || var10 == var5);
                        if(var12 && !var9) {
                            this.coords[var11++] = var7 + 1024;
                        }

                        var9 = var12;
                    }

                    this.floodFillCounters[var7] = floodFillCounter;
                    ++var7;
                }
            } while(var1 >= this.width || this.blocks[(var2 * this.length + var3) * this.width + var1] != 0);

            return 0;
        } else {
            return 0;
        }
    }

    public final void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
        for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
            float var6 = 16.0F;
            if(var3 > 1.0F) {
                var6 = 16.0F * var3;
            }

            Entity var7 = this.playerEntity;
            float var9 = var7.posX - var1.posX;
            float var10 = var7.posY - var1.posY;
            float var11 = var7.posZ - var1.posZ;
            if(var9 * var9 + var10 * var10 + var11 * var11 < var6 * var6) {
                ((IWorldAccess)this.worldAccesses.get(var5)).playSound(var2, var1.posX, var1.posY - var1.yOffset, var1.posZ, var3, var4);
            }
        }

    }

    public final void playSoundEffect(float var1, float var2, float var3, String var4, float var5, float var6) {
        for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
            float var8 = 16.0F;
            if(var5 > 1.0F) {
                var8 = 16.0F * var5;
            }

            float var9 = var1 - this.playerEntity.posX;
            float var10 = var2 - this.playerEntity.posY;
            float var11 = var3 - this.playerEntity.posZ;
            if(var9 * var9 + var10 * var10 + var11 * var11 < var8 * var8) {
                ((IWorldAccess)this.worldAccesses.get(var7)).playSound(var4, var1, var2, var3, var5, var6);
            }
        }

    }

    public final void extinguishFire(int var1, int var2, int var3, int var4) {
        if(var4 == 0) {
            --var2;
        }

        if(var4 == 1) {
            ++var2;
        }

        if(var4 == 2) {
            --var3;
        }

        if(var4 == 3) {
            ++var3;
        }

        if(var4 == 4) {
            --var1;
        }

        if(var4 == 5) {
            ++var1;
        }

        if(this.getBlockId(var1, var2, var3) == Block.fire.blockID) {
            this.playSoundEffect((float)var1 + 0.5F, (float)var2 + 0.5F, (float)var3 + 0.5F, "random.fizz", 0.5F, 2.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.8F);
            this.setBlockWithNotify(var1, var2, var3, 0);
        }

    }

    public final void setBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
        this.map.put(Integer.valueOf(var1 + (var2 << 10) + (var3 << 10 << 10)), var4);
    }

    public final void removeBlockTileEntity(int var1, int var2, int var3) {
        this.map.remove(Integer.valueOf(var1 + (var2 << 10) + (var3 << 10 << 10)));
    }

    public final TileEntity getBlockTileEntity(int var1, int var2, int var3) {
        return (TileEntity)this.map.get(Integer.valueOf(var1 + (var2 << 10) + (var3 << 10 << 10)));
    }

    public final void spawnParticle(String var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        for(int var8 = 0; var8 < this.worldAccesses.size(); ++var8) {
            ((IWorldAccess)this.worldAccesses.get(var8)).spawnParticle(var1, var2, var3, var4, var5, var6, var7);
        }

    }

    public final void randomDisplayUpdates(int var1, int var2, int var3) {
        for(int var4 = 0; var4 < 1000; ++var4) {
            int var5 = var1 + this.random.nextInt(16) - this.random.nextInt(16);
            int var6 = var2 + this.random.nextInt(16) - this.random.nextInt(16);
            int var7 = var3 + this.random.nextInt(16) - this.random.nextInt(16);
            int var8 = this.getBlockId(var5, var6, var7);
            if(var8 > 0) {
                Block.blocksList[var8].randomDisplayTick(this, var5, var6, var7, this.rand);
            }
        }

    }

    public final String getDebugLoadedEntities() {
        EntityMap var1 = this.entityMap;
        return "" + var1.all.size();
    }

    public final String getDebugMapInfo() {
        return "" + this.tickList.size() + " / " + this.map.size();
    }

    static {
        for(int var0 = 0; var0 <= 15; ++var0) {
            float var1 = 1.0F - (float)var0 / 15.0F;
            lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.9F + 0.1F;
        }

        floodFillCounter = 0;
    }
}
