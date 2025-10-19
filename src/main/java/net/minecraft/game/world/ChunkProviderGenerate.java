package net.minecraft.game.world;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.terrain.generate.Empty;
import net.minecraft.game.world.terrain.generate.WorldGenMinable;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public final class ChunkProviderGenerate implements IChunkProvider {
    private EaglercraftRandom rand = new EaglercraftRandom();
    private EaglercraftRandom rand2 = new EaglercraftRandom();
    private World worldObj;
    private NoiseGeneratorOctaves noiseGen1 = new NoiseGeneratorOctaves(16);
    private NoiseGeneratorOctaves noiseGen2 = new NoiseGeneratorOctaves(16);
    private NoiseGeneratorOctaves noiseGen3 = new NoiseGeneratorOctaves(8);
    private NoiseGeneratorOctaves noiseGen4 = new NoiseGeneratorOctaves(4);
    private NoiseGeneratorOctaves noiseGen5 = new NoiseGeneratorOctaves(4);
    private NoiseGeneratorOctaves noiseGen6 = new NoiseGeneratorOctaves(5);
    private NoiseGeneratorOctaves j = new NoiseGeneratorOctaves(5);

    public ChunkProviderGenerate(World var1) {
        this.worldObj = var1;
    }

    public final Chunk provideChunk(int var1, int var2) {
        this.rand.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
        byte[] var3 = new byte[-Short.MIN_VALUE];
        Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);
        var1 <<= 4;
        var2 <<= 4;
        int var5 = 0;

        for(int var6 = var1; var6 < var1 + 16; ++var6) {
            for(int var7 = var2; var7 < var2 + 16; ++var7) {
                int var8 = var6 / 1024;
                int var9 = var7 / 1024;
                float var10 = (float)(this.noiseGen1.generateNoise((double)((float)var6 / 0.03125F), 0.0D, (double)((float)var7 / 0.03125F)) - this.noiseGen2.generateNoise((double)((float)var6 / 0.015625F), 0.0D, (double)((float)var7 / 0.015625F))) / 512.0F / 4.0F;
                float var11 = (float)this.noiseGen5.generateNoise((double)((float)var6 / 4.0F), (double)((float)var7 / 4.0F));
                float var12 = (float)this.noiseGen6.generateNoise((double)((float)var6 / 8.0F), (double)((float)var7 / 8.0F)) / 8.0F;
                var11 = var11 > 0.0F ? (float)(this.noiseGen3.generateNoise((double)((float)var6 * 0.25714284F * 2.0F), (double)((float)var7 * 0.25714284F * 2.0F)) * (double)var12 / 4.0D) : (float)(this.noiseGen4.generateNoise((double)((float)var6 * 0.25714284F), (double)((float)var7 * 0.25714284F)) * (double)var12);
                int var15 = (int)(var10 + 64.0F + var11);
                if((float)this.noiseGen5.generateNoise((double)var6, (double)var7) < 0.0F) {
                    var15 = var15 / 2 << 1;
                    if((float)this.noiseGen5.generateNoise((double)(var6 / 5), (double)(var7 / 5)) < 0.0F) {
                        ++var15;
                    }
                }

                for(int var16 = 0; var16 < 128; ++var16) {
                    int var17 = 0;
                    if(var16 == var15 + 1 && var15 >= 64 && Math.random() < 0.02D) {
                        var17 = Block.plantYellow.blockID;
                    } else if(var16 == var15 && var15 >= 64) {
                        var17 = Block.grass.blockID;
                    } else if(var16 <= var15 - 2) {
                        var17 = Block.stone.blockID;
                    } else if(var16 <= var15) {
                        var17 = Block.dirt.blockID;
                    } else if(var16 <= 64) {
                        var17 = Block.waterStill.blockID;
                    }

                    this.rand2.setSeed((long)(var8 + var9 * 13871));
                    int var13 = (var8 << 10) + 128 + this.rand2.nextInt(512);
                    int var14 = (var9 << 10) + 128 + this.rand2.nextInt(512);
                    var13 = var6 - var13;
                    var14 = var7 - var14;
                    if(var13 < 0) {
                        var13 = -var13;
                    }

                    if(var14 < 0) {
                        var14 = -var14;
                    }

                    if(var14 > var13) {
                        var13 = var14;
                    }

                    var13 = 127 - var13;
                    if(var13 == 255) {
                        var13 = 1;
                    }

                    if(var13 < var15) {
                        var13 = var15;
                    }

                    if(var16 <= var13 && (var17 == 0 || var17 == Block.waterStill.blockID)) {
                        var17 = Block.brick.blockID;
                    }

                    if(var17 < 0) {
                        var17 = 0;
                    }

                    var3[var5++] = (byte)var17;
                }
            }
        }

        var4.generateHeightMap();
        return var4;
    }

    public final boolean chunkExists(int var1, int var2) {
        return true;
    }

    public final void populate(IChunkProvider var1, int var2, int var3) {
        this.rand.setSeed((long)var2 * 318279123L + (long)var3 * 919871212L);
        int var58 = var2 << 4;
        var2 = var3 << 4;

        int var4;
        int var5;
        int var6;
        int var8;
        int var9;
        int var10;
        boolean var10000;
        World var59;
        EaglercraftRandom var60;
        int var61;
        for(var3 = 0; var3 < 128; var3 += 16) {
            var4 = (int)(this.j.generateNoise((double)var58 * (1.0D / 16.0D), (double)var3 * (1.0D / 16.0D) * 4.0D, (double)var2 * (1.0D / 16.0D)) + (double)(128 - var3) / 64.0D);

            label287:
            for(var5 = 0; var5 < var4; ++var5) {
                var6 = var58 + this.rand.nextInt(16);
                int var7 = var3 + this.rand.nextInt(16);
                var8 = var2 + this.rand.nextInt(16);
                var60 = this.rand;
                var59 = this.worldObj;
                float var11 = var60.nextFloat() * (float)Math.PI;
                double var18 = (double)((float)(var6 + 8) + MathHelper.sin(var11) * 7.0F);
                double var20 = (double)((float)(var6 + 8) - MathHelper.sin(var11) * 7.0F);
                double var22 = (double)((float)(var8 + 8) + MathHelper.cos(var11) * 7.0F);
                double var24 = (double)((float)(var8 + 8) - MathHelper.cos(var11) * 7.0F);
                double var26 = (double)(var7 + var60.nextInt(8) + 2);
                double var28 = (double)(var7 + var60.nextInt(8) + 2);
                double var30 = var60.nextDouble() * 4.0D + 2.0D;
                double var32 = var60.nextDouble() * 0.6D;
                long var34 = var60.nextLong();
                var60.setSeed(var34);

                double var37;
                double var39;
                double var41;
                double var43;
                double var45;
                double var47;
                double var52;
                double var54;
                double var56;
                for(var8 = 0; var8 <= 16; ++var8) {
                    var37 = var18 + (var20 - var18) * (double)var8 / 16.0D;
                    var39 = var26 + (var28 - var26) * (double)var8 / 16.0D;
                    var41 = var22 + (var24 - var22) * (double)var8 / 16.0D;
                    var43 = var60.nextDouble();
                    var45 = ((double)MathHelper.sin((float)var8 / 16.0F * (float)Math.PI) * var30 + 1.0D) * var43 + 1.0D;
                    var47 = ((double)MathHelper.sin((float)var8 / 16.0F * (float)Math.PI) * var30 + 1.0D) * var43 + 1.0D;

                    for(var9 = (int)(var37 - var45 / 2.0D); var9 <= (int)(var37 + var45 / 2.0D); ++var9) {
                        for(var10 = (int)(var39 - var47 / 2.0D); var10 <= (int)(var39 + var47 / 2.0D); ++var10) {
                            for(var61 = (int)(var41 - var45 / 2.0D); var61 <= (int)(var41 + var45 / 2.0D); ++var61) {
                                var52 = ((double)var9 + 0.5D - var37) / (var45 / 2.0D);
                                var54 = ((double)var10 + 0.5D - var39) / (var47 / 2.0D);
                                var56 = ((double)var61 + 0.5D - var41) / (var45 / 2.0D);
                                if(var52 * var52 + var54 * var54 + var56 * var56 < var60.nextDouble() * var32 + (1.0D - var32)) {
                                    for(int var12 = var9 - 2; var12 <= var9 + 1; ++var12) {
                                        for(int var13 = var10 - 1; var13 <= var10 + 1; ++var13) {
                                            for(int var14 = var61 - 1; var14 <= var61 + 1; ++var14) {
                                                if(var59.getBlockMaterial(var12, var13, var14).getIsLiquid()) {
                                                    var10000 = false;
                                                    continue label287;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                var60.setSeed(var34);

                for(var8 = 0; var8 <= 16; ++var8) {
                    var37 = var18 + (var20 - var18) * (double)var8 / 16.0D;
                    var39 = var26 + (var28 - var26) * (double)var8 / 16.0D;
                    var41 = var22 + (var24 - var22) * (double)var8 / 16.0D;
                    var43 = var60.nextDouble();
                    var45 = ((double)MathHelper.sin((float)var8 / 16.0F * (float)Math.PI) * var30 + 1.0D) * var43 + 1.0D;
                    var47 = ((double)MathHelper.sin((float)var8 / 16.0F * (float)Math.PI) * var30 + 1.0D) * var43 + 1.0D;

                    for(var9 = (int)(var37 - var45 / 2.0D); var9 <= (int)(var37 + var45 / 2.0D); ++var9) {
                        for(var10 = (int)(var39 - var47 / 2.0D); var10 <= (int)(var39 + var47 / 2.0D); ++var10) {
                            for(var61 = (int)(var41 - var45 / 2.0D); var61 <= (int)(var41 + var45 / 2.0D); ++var61) {
                                var52 = ((double)var9 + 0.5D - var37) / (var45 / 2.0D);
                                var54 = ((double)var10 + 0.5D - var39) / (var47 / 2.0D);
                                var56 = ((double)var61 + 0.5D - var41) / (var45 / 2.0D);
                                if(var52 * var52 + var54 * var54 + var56 * var56 < var60.nextDouble() * var32 + (1.0D - var32) && var59.getBlockId(var9, var10, var61) != 0) {
                                    var59.setBlockWithNotify(var9, var10, var61, 0);
                                }
                            }
                        }
                    }
                }

                var10000 = true;
            }
        }

        for(var3 = 0; var3 < 20; ++var3) {
            var4 = var58 + this.rand.nextInt(16);
            var5 = this.rand.nextInt(128);
            var6 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreCoal.blockID)).generate(this.worldObj, this.rand, var4, var5, var6);
        }

        for(var3 = 0; var3 < 10; ++var3) {
            var4 = var58 + this.rand.nextInt(16);
            var5 = this.rand.nextInt(64);
            var6 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreIron.blockID)).generate(this.worldObj, this.rand, var4, var5, var6);
        }

        if(this.rand.nextInt(2) == 0) {
            var3 = var58 + this.rand.nextInt(16);
            var4 = this.rand.nextInt(32);
            var5 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreGold.blockID)).generate(this.worldObj, this.rand, var3, var4, var5);
        }

        if(this.rand.nextInt(8) == 0) {
            var3 = var58 + this.rand.nextInt(16);
            var4 = this.rand.nextInt(16);
            var5 = var2 + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreDiamond.blockID)).generate(this.worldObj, this.rand, var3, var4, var5);
        }

        var3 = (int)this.j.generateNoise((double)var58 * (1.0D / 16.0D), (double)var2 * (1.0D / 16.0D)) << 3;

        for(var4 = 0; var4 < var3; ++var4) {
            var5 = var58 + this.rand.nextInt(16);
            var6 = var2 + this.rand.nextInt(16);
            new WorldGenTrees();
            World var62 = this.worldObj;
            EaglercraftRandom var10001 = this.rand;
            var9 = this.worldObj.getHeightValue(var5, var6);
            var60 = var10001;
            var59 = var62;
            var8 = var5 + 2;
            var10 = var6 + 2;
            var61 = var60.nextInt(3) + 4;
            boolean var63 = true;
            if(var9 > 0 && var9 + var61 + 1 <= 128) {
                int var19;
                int var21;
                int var23;
                int var66;
                for(var19 = var9; var19 <= var9 + 1 + var61; ++var19) {
                    byte var64 = 1;
                    if(var19 == var9) {
                        var64 = 0;
                    }

                    if(var19 >= var9 + 1 + var61 - 2) {
                        var64 = 2;
                    }

                    for(var21 = var8 - var64; var21 <= var8 + var64 && var63; ++var21) {
                        for(var66 = var10 - var64; var66 <= var10 + var64 && var63; ++var66) {
                            if(var19 >= 0 && var19 < 128) {
                                var23 = var59.getBlockId(var21, var19, var66);
                                if(var23 != 0) {
                                    var63 = false;
                                }
                            } else {
                                var63 = false;
                            }
                        }
                    }
                }

                if(!var63) {
                    var10000 = false;
                } else {
                    var19 = var59.getBlockId(var8, var9 - 1, var10);
                    if((var19 == Block.grass.blockID || var19 == Block.dirt.blockID) && var9 < 128 - var61 - 1) {
                        var59.setBlockWithNotify(var8, var9 - 1, var10, Block.dirt.blockID);

                        int var65;
                        for(var65 = var9 - 3 + var61; var65 <= var9 + var61; ++var65) {
                            var21 = var65 - (var9 + var61);
                            var66 = 1 - var21 / 2;

                            for(var23 = var8 - var66; var23 <= var8 + var66; ++var23) {
                                int var67 = var23 - var8;

                                for(int var25 = var10 - var66; var25 <= var10 + var66; ++var25) {
                                    int var68 = var25 - var10;
                                    if((Math.abs(var67) != var66 || Math.abs(var68) != var66 || var60.nextInt(2) != 0 && var21 != 0) && !Block.opaqueCubeLookup[var59.getBlockId(var23, var65, var25)]) {
                                        var59.setBlockWithNotify(var23, var65, var25, Block.leaves.blockID);
                                    }
                                }
                            }
                        }

                        for(var65 = 0; var65 < var61; ++var65) {
                            if(!Block.opaqueCubeLookup[var59.getBlockId(var8, var9 + var65, var10)]) {
                                var59.setBlockWithNotify(var8, var9 + var65, var10, Block.wood.blockID);
                            }
                        }

                        var10000 = true;
                    } else {
                        var10000 = false;
                    }
                }
            } else {
                var10000 = false;
            }
        }

    }
}
