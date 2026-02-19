package net.minecraft.game.world.terrain;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.terrain.generate.WorldGenMinable;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public final class ChunkProviderGenerate implements IChunkProvider {
    private EaglercraftRandom rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;

    public ChunkProviderGenerate(World var1, long var2) {
        this.worldObj = var1;
        this.rand = new EaglercraftRandom(var2);
        new EaglercraftRandom(var2);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
        new NoiseGeneratorOctaves(this.rand, 4);
        new NoiseGeneratorOctaves(this.rand, 4);
        new NoiseGeneratorOctaves(this.rand, 5);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 5);
    }

    public final Chunk provideChunk(int var1, int var2) {
        this.rand.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
        byte[] var3 = new byte[-Short.MIN_VALUE];
        Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);

        int var5;
        int var6;
        int var8;
        int var9;
        for(var5 = 0; var5 < 4; ++var5) {
            for(var6 = 0; var6 < 4; ++var6) {
                double[][] var7 = new double[33][4];
                var8 = (var1 << 2) + var5;
                var9 = (var2 << 2) + var6;

                for(int var10 = 0; var10 < var7.length; ++var10) {
                    var7[var10][0] = this.initializeNoiseField((double)var8, (double)var10, (double)var9);
                    var7[var10][1] = this.initializeNoiseField((double)var8, (double)var10, (double)(var9 + 1));
                    var7[var10][2] = this.initializeNoiseField((double)(var8 + 1), (double)var10, (double)var9);
                    var7[var10][3] = this.initializeNoiseField((double)(var8 + 1), (double)var10, (double)(var9 + 1));
                }

                for(var8 = 0; var8 < 32; ++var8) {
                    double var50 = var7[var8][0];
                    double var11 = var7[var8][1];
                    double var13 = var7[var8][2];
                    double var15 = var7[var8][3];
                    double var17 = var7[var8 + 1][0];
                    double var19 = var7[var8 + 1][1];
                    double var21 = var7[var8 + 1][2];
                    double var23 = var7[var8 + 1][3];

                    for(int var25 = 0; var25 < 4; ++var25) {
                        double var26 = (double)var25 / 4.0D;
                        double var28 = var50 + (var17 - var50) * var26;
                        double var30 = var11 + (var19 - var11) * var26;
                        double var32 = var13 + (var21 - var13) * var26;
                        double var34 = var15 + (var23 - var15) * var26;

                        for(int var51 = 0; var51 < 4; ++var51) {
                            double var37 = (double)var51 / 4.0D;
                            double var39 = var28 + (var32 - var28) * var37;
                            double var41 = var30 + (var34 - var30) * var37;
                            int var27 = var51 + (var5 << 2) << 11 | 0 + (var6 << 2) << 7 | (var8 << 2) + var25;

                            for(int var36 = 0; var36 < 4; ++var36) {
                                double var45 = (double)var36 / 4.0D;
                                double var47 = var39 + (var41 - var39) * var45;
                                int var52 = 0;
                                if((var8 << 2) + var25 < 64) {
                                    var52 = Block.waterStill.blockID;
                                }

                                if(var47 > 0.0D) {
                                    var52 = Block.stone.blockID;
                                }

                                var3[var27] = (byte)var52;
                                var27 += 128;
                            }
                        }
                    }
                }
            }
        }

        for(var5 = 0; var5 < 16; ++var5) {
            for(var6 = 0; var6 < 16; ++var6) {
                int var49 = var5 << 11 | var6 << 7 | 127;
                var8 = -1;

                for(var9 = 127; var9 >= 0; --var9) {
                    if(var3[var49] == 0) {
                        var8 = -1;
                    } else if(var3[var49] == Block.stone.blockID) {
                        if(var8 == -1) {
                            var8 = 3;
                            if(var9 >= 63) {
                                var3[var49] = (byte)Block.grass.blockID;
                            } else {
                                var3[var49] = (byte)Block.dirt.blockID;
                            }
                        } else if(var8 > 0) {
                            --var8;
                            var3[var49] = (byte)Block.dirt.blockID;
                        }
                    }

                    --var49;
                }
            }
        }

        var4.generateHeightMap();
        return var4;
    }

    private double initializeNoiseField(double var1, double var3, double var5) {
        double var7 = var3 * 4.0D - 64.0D;
        if(var7 < 0.0D) {
            var7 *= 3.0D;
        }

        double var9 = this.noiseGen3.generateNoiseOctaves(var1 * 684.412D / 80.0D, var3 * 684.412D / 400.0D, var5 * 684.412D / 80.0D) / 2.0D;
        double var11;
        double var13;
        if(var9 < -1.0D) {
            var11 = this.noiseGen1.generateNoiseOctaves(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D;
            var13 = var11 - var7;
            if(var13 < -10.0D) {
                var13 = -10.0D;
            }

            if(var13 > 10.0D) {
                var13 = 10.0D;
            }
        } else if(var9 > 1.0D) {
            var11 = this.noiseGen2.generateNoiseOctaves(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D;
            var13 = var11 - var7;
            if(var13 < -10.0D) {
                var13 = -10.0D;
            }

            if(var13 > 10.0D) {
                var13 = 10.0D;
            }
        } else {
            double var15 = this.noiseGen1.generateNoiseOctaves(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D - var7;
            double var17 = this.noiseGen2.generateNoiseOctaves(var1 * 684.412D, var3 * 984.412D, var5 * 684.412D) / 512.0D - var7;
            if(var15 < -10.0D) {
                var15 = -10.0D;
            }

            if(var15 > 10.0D) {
                var15 = 10.0D;
            }

            if(var17 < -10.0D) {
                var17 = -10.0D;
            }

            if(var17 > 10.0D) {
                var17 = 10.0D;
            }

            double var19 = (var9 + 1.0D) / 2.0D;
            var11 = var15 + (var17 - var15) * var19;
            var13 = var11;
        }

        return var13;
    }

    public final boolean chunkExists(int var1, int var2) {
        return true;
    }

    public final void populate(IChunkProvider var1, int var2, int var3) {
        this.rand.setSeed((long)var2 * 318279123L + (long)var3 * 919871212L);
        int var18 = var2 << 4;
        var2 = var3 << 4;

        int var4;
        int var5;
        int var6;
        for(var3 = 0; var3 < 20; ++var3) {
            var4 = var18 + this.rand.nextInt(16);
            var5 = this.rand.nextInt(128);
            var6 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreCoal.blockID)).generate(this.worldObj, this.rand, var4, var5, var6);
        }

        for(var3 = 0; var3 < 10; ++var3) {
            var4 = var18 + this.rand.nextInt(16);
            var5 = this.rand.nextInt(64);
            var6 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreIron.blockID)).generate(this.worldObj, this.rand, var4, var5, var6);
        }

        if(this.rand.nextInt(2) == 0) {
            var3 = var18 + this.rand.nextInt(16);
            var4 = this.rand.nextInt(32);
            var5 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreGold.blockID)).generate(this.worldObj, this.rand, var3, var4, var5);
        }

        if(this.rand.nextInt(8) == 0) {
            var3 = var18 + this.rand.nextInt(16);
            var4 = this.rand.nextInt(16);
            var5 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreDiamond.blockID)).generate(this.worldObj, this.rand, var3, var4, var5);
        }

        var3 = (int)this.mobSpawnerNoise.noiseGenerator((double)var18 * 0.25D, (double)var2 * 0.25D) << 3;

        for(var4 = 0; var4 < var3; ++var4) {
            var5 = var18 + this.rand.nextInt(16);
            var6 = var2 + this.rand.nextInt(16);
            new WorldGenTrees();
            World var10000 = this.worldObj;
            EaglercraftRandom var10001 = this.rand;
            int var8 = this.worldObj.getHeightValue(var5, var6);
            EaglercraftRandom var20 = var10001;
            World var19 = var10000;
            int var7 = var5 + 2;
            int var9 = var6 + 2;
            int var10 = var20.nextInt(3) + 4;
            boolean var11 = true;
            boolean var23;
            if(var8 > 0 && var8 + var10 + 1 <= 128) {
                int var12;
                int var14;
                int var15;
                int var16;
                for(var12 = var8; var12 <= var8 + 1 + var10; ++var12) {
                    byte var13 = 1;
                    if(var12 == var8) {
                        var13 = 0;
                    }

                    if(var12 >= var8 + 1 + var10 - 2) {
                        var13 = 2;
                    }

                    for(var14 = var7 - var13; var14 <= var7 + var13 && var11; ++var14) {
                        for(var15 = var9 - var13; var15 <= var9 + var13 && var11; ++var15) {
                            if(var12 >= 0 && var12 < 128) {
                                var16 = var19.getBlockId(var14, var12, var15);
                                if(var16 != 0) {
                                    var11 = false;
                                }
                            } else {
                                var11 = false;
                            }
                        }
                    }
                }

                if(!var11) {
                    var23 = false;
                } else {
                    var12 = var19.getBlockId(var7, var8 - 1, var9);
                    if((var12 == Block.grass.blockID || var12 == Block.dirt.blockID) && var8 < 128 - var10 - 1) {
                        var19.setTileNoUpdate(var7, var8 - 1, var9, Block.dirt.blockID);

                        int var22;
                        for(var22 = var8 - 3 + var10; var22 <= var8 + var10; ++var22) {
                            var14 = var22 - (var8 + var10);
                            var15 = 1 - var14 / 2;

                            for(var16 = var7 - var15; var16 <= var7 + var15; ++var16) {
                                int var21 = var16 - var7;

                                for(var12 = var9 - var15; var12 <= var9 + var15; ++var12) {
                                    int var17 = var12 - var9;
                                    if((Math.abs(var21) != var15 || Math.abs(var17) != var15 || var20.nextInt(2) != 0 && var14 != 0) && !Block.opaqueCubeLookup[var19.getBlockId(var16, var22, var12)]) {
                                        var19.setTileNoUpdate(var16, var22, var12, Block.leaves.blockID);
                                    }
                                }
                            }
                        }

                        for(var22 = 0; var22 < var10; ++var22) {
                            if(!Block.opaqueCubeLookup[var19.getBlockId(var7, var8 + var22, var9)]) {
                                var19.setTileNoUpdate(var7, var8 + var22, var9, Block.wood.blockID);
                            }
                        }

                        var23 = true;
                    } else {
                        var23 = false;
                    }
                }
            } else {
                var23 = false;
            }
        }

    }

    public final void saveChunks(boolean var1) {
    }
}
