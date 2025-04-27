package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class World {
    private int MAX_TICKS = 100;
    private static float[] lightBrightnessTable = new float[16];
    public int width;
    public int length;
    public int height;
    private byte[] blocks;
    private byte[] data;
    public int xSpawn;
    public int ySpawn;
    public int zSpawn;
    public float rotSpawn;
    public int defaultFluid = Block.waterMoving.blockID;
    private ArrayList worldAccesses = new ArrayList();
    private ArrayList tickList = new ArrayList();
    private int[] heightMap;
    public EaglercraftRandom random = new EaglercraftRandom();
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
        for(var2 = 0; var2 < this.width; ++var2) {
            for(var5 = 0; var5 < this.length; ++var5) {
                for(var6 = 0; var6 < this.height; ++var6) {
                    int var7 = 0;
                    if(var6 < this.groundLevel - 1) {
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
                    if(var6 == 0 && var2 != 0 && var5 != 0 && var2 != this.width - 1 && var5 != this.length - 1) {
                        var6 = this.height - 2;
                    }
                }
            }
        }

        this.data = new byte[var4.length];
        this.heightMap = new int[var1 * var3];
        Arrays.fill(this.heightMap, this.height);
        World var9 = this;
        var2 = (int)(15.0F * this.skyBrightness);

        int var11;
        for(var3 = 0; var3 < var9.width; ++var3) {
            for(var11 = 0; var11 < var9.length; ++var11) {
                for(var5 = var9.height - 1; var5 > 0 && Block.lightOpacity[var9.getBlockId(var3, var5, var11)] == 0; --var5) {
                }

                var9.heightMap[var3 + var11 * var9.width] = var5 + 1;

                for(var5 = 0; var5 < var9.height; ++var5) {
                    var6 = var9.heightMap[var3 + var11 * var9.width];
                    var6 = var5 >= var6 ? var2 : 0;
                    byte var12 = var9.blocks[(var5 * var9.length + var11) * var9.width + var3];
                    if(var6 < Block.lightValue[var12]) {
                        var6 = Block.lightValue[var12];
                    }

                    var9.data[(var5 * var9.length + var11) * var9.width + var3] = (byte)var6;
                }
            }
        }

        for(var3 = 0; var3 < var9.width; ++var3) {
            for(var11 = 0; var11 < var9.length; ++var11) {
                var9.updateLight(var3, 0, var11, var3 + 1, var9.height, var11 + 1);
            }
        }

        for(var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            ((IWorldAccess)this.worldAccesses.get(var2)).loadRenderers();
        }

        this.tickList.clear();
        var9 = this;
        EaglercraftRandom var10 = new EaglercraftRandom();
        var3 = 0;

        while(true) {
            do {
                ++var3;
                var11 = var10.nextInt(var9.width / 2) + var9.width / 4;
                var5 = var10.nextInt(var9.length / 2) + var9.length / 4;
                var6 = var9.getFirstUncoveredBlock(var11, var5) + 1;
            } while(var6 < 4);

            if(var3 == 10000) {
                var9.xSpawn = var11;
                var9.ySpawn = -100;
                var9.zSpawn = var5;
                break;
            }

            if(var6 > var9.waterLevel) {
                var9.xSpawn = var11;
                var9.ySpawn = var6;
                var9.zSpawn = var5;
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
        int[] var8 = new int[8192];

        int var9;
        int var10;
        int var11;
        for(var9 = var1; var9 < var4; ++var9) {
            for(var10 = var3; var10 < var6; ++var10) {
                for(var11 = var2; var11 < var5; ++var11) {
                    var8[var7++] = var9 << 20 | var11 << 10 | var10;
                }
            }
        }

        var9 = (int)(15.0F * this.skyBrightness);

        while(var7 > 0) {
            int var12;
            while(var7 > var8.length - 16) {
                --var7;
                var10 = var8[var7];
                var11 = var10 >> 20 & 1023;
                var12 = var10 >> 10 & 1023;
                var10 &= 1023;
                this.updateLight(var11, var12, var10, var11 + 1, var12 + 1, var10 + 1);
            }

            --var7;
            var10 = var8[var7];
            var11 = var10 >> 20 & 1023;
            var12 = var10 >> 10 & 1023;
            var10 &= 1023;
            int var13 = this.heightMap[var11 + var10 * this.width];
            var13 = var12 >= var13 ? var9 : 0;
            byte var14 = this.blocks[(var12 * this.length + var10) * this.width + var11];
            int var15 = Block.lightOpacity[var14];
            if(var15 > 100) {
                var13 = 0;
            } else if(var13 < 14) {
                var15 = var15;
                if(var15 == 0) {
                    var15 = 1;
                }

                int var16;
                if(var11 > 0) {
                    var16 = (this.data[(var12 * this.length + var10) * this.width + (var11 - 1)] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }

                if(var11 < this.width - 1) {
                    var16 = (this.data[(var12 * this.length + var10) * this.width + var11 + 1] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }

                if(var12 > 0) {
                    var16 = (this.data[((var12 - 1) * this.length + var10) * this.width + var11] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }

                if(var12 < this.height - 1) {
                    var16 = (this.data[((var12 + 1) * this.length + var10) * this.width + var11] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }

                if(var10 > 0) {
                    var16 = (this.data[(var12 * this.length + (var10 - 1)) * this.width + var11] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }

                if(var10 < this.length - 1) {
                    var16 = (this.data[(var12 * this.length + var10 + 1) * this.width + var11] & 255) - var15;
                    if(var16 > var13) {
                        var13 = var16;
                    }
                }
            }

            if(var13 < Block.lightValue[var14]) {
                var13 = Block.lightValue[var14];
            }

            if(var11 < var1) {
                var1 = var11;
            } else if(var11 > var4) {
                var4 = var11;
            }

            if(var12 > var5) {
                var5 = var12;
            } else if(var12 < var2) {
                var2 = var12;
            }

            if(var10 < var3) {
                var3 = var10;
            } else if(var10 > var6) {
                var6 = var10;
            }

            var15 = this.data[(var12 * this.length + var10) * this.width + var11] & 255;
            if(var15 != var13) {
                this.data[(var12 * this.length + var10) * this.width + var11] = (byte)var13;
                if(var11 > 0 && (this.data[(var12 * this.length + var10) * this.width + (var11 - 1)] & 255) != var13 - 1) {
                    var8[var7++] = var11 - 1 << 20 | var12 << 10 | var10;
                }

                if(var11 < this.width - 1 && (this.data[(var12 * this.length + var10) * this.width + var11 + 1] & 255) != var13 - 1) {
                    var8[var7++] = var11 + 1 << 20 | var12 << 10 | var10;
                }

                if(var12 > 0 && (this.data[((var12 - 1) * this.length + var10) * this.width + var11] & 255) != var13 - 1) {
                    var8[var7++] = var11 << 20 | var12 - 1 << 10 | var10;
                }

                if(var12 < this.height - 1 && (this.data[((var12 + 1) * this.length + var10) * this.width + var11] & 255) != var13 - 1) {
                    var8[var7++] = var11 << 20 | var12 + 1 << 10 | var10;
                }

                if(var10 > 0 && (this.data[(var12 * this.length + (var10 - 1)) * this.width + var11] & 255) != var13 - 1) {
                    var8[var7++] = var11 << 20 | var12 << 10 | var10 - 1;
                }

                if(var10 < this.length - 1 && (this.data[(var12 * this.length + var10 + 1) * this.width + var11] & 255) != var13 - 1) {
                    var8[var7++] = var11 << 20 | var12 << 10 | var10 + 1;
                }
            }
        }

        Iterator var17 = this.worldAccesses.iterator();

        while(var17.hasNext()) {
            IWorldAccess var18 = (IWorldAccess)var17.next();
            var18.markBlockRangeNeedsUpdate(var1, var2, var3, var4, var5, var6);
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
        var6 = this.updateLCG / 200;
        this.updateLCG -= var6 * 200;

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
                    if(var9 != null && var9.getBlockMaterial() != Material.air) {
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
                    if(var10 != null && var10.getBlockMaterial() == var2) {
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
        for(var3 = this.height; (this.getBlockId(var1, var3 - 1, var2) == 0 || Block.blocksList[this.getBlockId(var1, var3 - 1, var2)].getBlockMaterial() != Material.air) && var3 > 0; --var3) {
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

        return lightBrightnessTable[this.data[(var2 * this.length + var3) * this.width + var1]];
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

        return this.data[(var2 * this.length + var3) * this.width + var1];
    }

    public final Material getBlockMaterial(int var1, int var2, int var3) {
        var1 = this.getBlockId(var1, var2, var3);
        return var1 == 0 ? Material.air : Block.blocksList[var1].getBlockMaterial();
    }

    public final boolean isWater(int var1, int var2, int var3) {
        var1 = this.getBlockId(var1, var2, var3);
        return var1 > 0 && Block.blocksList[var1].getBlockMaterial() == Material.water;
    }

    public final MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
        if(!Float.isNaN(var1.xCoord) && !Float.isNaN(var1.yCoord) && !Float.isNaN(var1.zCoord)) {
            if(!Float.isNaN(var2.xCoord) && !Float.isNaN(var2.yCoord) && !Float.isNaN(var2.zCoord)) {
                int var3 = (int)Math.floor((double)var2.xCoord);
                int var4 = (int)Math.floor((double)var2.yCoord);
                int var5 = (int)Math.floor((double)var2.zCoord);
                int var6 = (int)Math.floor((double)var1.xCoord);
                int var7 = (int)Math.floor((double)var1.yCoord);
                int var8 = (int)Math.floor((double)var1.zCoord);
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
                    var6 = (int)(var20.xCoord = (float)Math.floor((double)var1.xCoord));
                    if(var19 == 5) {
                        --var6;
                        ++var20.xCoord;
                    }

                    var7 = (int)(var20.yCoord = (float)Math.floor((double)var1.yCoord));
                    if(var19 == 1) {
                        --var7;
                        ++var20.yCoord;
                    }

                    var8 = (int)(var20.zCoord = (float)Math.floor((double)var1.zCoord));
                    if(var19 == 3) {
                        --var8;
                        ++var20.zCoord;
                    }

                    int var21 = this.getBlockId(var6, var7, var8);
                    Block var23 = Block.blocksList[var21];
                    if(var21 > 0 && var23.getBlockMaterial() == Material.air && var23.isCollidable()) {
                        MovingObjectPosition var22;
                        if(var23.renderAsNormalBlock()) {
                            var22 = var23.collisionRayTrace(var6, var7, var8, var1, var2);
                            if(var22 != null) {
                                return var22;
                            }
                        } else {
                            var22 = var23.collisionRayTrace(var6, var7, var8, var1, var2);
                            if(var22 != null) {
                                return var22;
                            }
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

    public final void releaseEntitySkin(Entity var1) {
        this.entityMap.insert(var1);
        var1.setWorld(this);
    }

    public final void onPickup(Entity var1) {
        this.entityMap.remove(var1);
    }

    public final void createExplosion(Entity var1, float var2, float var3, float var4, float var5) {
        int var16 = (int)(var2 - 4.0F - 1.0F);
        int var18 = (int)(var2 + 4.0F + 1.0F);
        int var6 = (int)(var3 - 4.0F - 1.0F);
        int var7 = (int)(var3 + 4.0F + 1.0F);
        int var8 = (int)(var4 - 4.0F - 1.0F);
        int var9 = (int)(var4 + 4.0F + 1.0F);

        int var11;
        float var13;
        float var14;
        for(int var10 = var16; var10 < var18; ++var10) {
            for(var11 = var7 - 1; var11 >= var6; --var11) {
                for(int var12 = var8; var12 < var9; ++var12) {
                    var13 = (float)var10 + 0.5F - var2;
                    var14 = (float)var11 + 0.5F - var3;
                    float var15 = (float)var12 + 0.5F - var4;
                    if(var10 >= 0 && var11 >= 0 && var12 >= 0 && var10 < this.width && var11 < this.height && var12 < this.length && var13 * var13 + var14 * var14 + var15 * var15 < 16.0F) {
                        int var22 = this.getBlockId(var10, var11, var12);
                        if(var22 > 0 && Block.blocksList[var22].getCanExplode()) {
                            Block.blocksList[var22].dropBlockAsItemWithChance(this, var10, var11, var12, 0.3F);
                            this.setBlockWithNotify(var10, var11, var12, 0);
                            Block.blocksList[var22].onBlockDestroyedByExplosion(this, var10, var11, var12);
                        }
                    }
                }
            }
        }

        List var20 = this.entityMap.getEntities((Entity)null, (float)var16, (float)var6, (float)var8, (float)var18, (float)var7, (float)var9);

        for(var11 = 0; var11 < var20.size(); ++var11) {
            Entity var21 = (Entity)var20.get(var11);
            var5 = var21.posX - var2;
            float var19 = var21.posY - var3;
            float var17 = var21.posZ - var4;
            var13 = MathHelper.sqrt_float(var5 * var5 + var19 * var19 + var17 * var17) / 4.0F;
            if(var13 <= 1.0F) {
                var14 = 1.0F - var13;
                var21.attackEntityFrom((Entity)null, (int)(var14 * 15.0F + 1.0F));
            }
        }

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

    static {
        for(int var0 = 0; var0 <= 15; ++var0) {
            float var1 = 1.0F - (float)var0 / 15.0F;
            lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.9F + 0.1F;
        }

    }
}
