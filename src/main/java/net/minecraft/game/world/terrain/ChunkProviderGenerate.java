package net.minecraft.game.world.terrain;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.terrain.generate.WorldGenFlowers;
import net.minecraft.game.world.terrain.generate.WorldGenLiquids;
import net.minecraft.game.world.terrain.generate.WorldGenMinable;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public final class ChunkProviderGenerate implements IChunkProvider {
    private EaglercraftRandom rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    private NoiseGeneratorOctaves noiseGen5;
    private NoiseGeneratorOctaves noiseGen6;
    private NoiseGeneratorOctaves noiseGen7;
    private NoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;
    private double[] noiseArray;
    private double[] noise3;
    private double[] noise1;
    private double[] noise2;
    private double[] noise6;
    private double[] noise7;

    public ChunkProviderGenerate(World world, long randomSeed) {
        this.worldObj = world;
        this.rand = new EaglercraftRandom(randomSeed);
        new EaglercraftRandom(randomSeed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 4);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen7 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        new NoiseGeneratorOctaves(6);
        new NoiseGeneratorOctaves(6);
        new NoiseGeneratorOctaves(6);
    }

    public final Chunk provideChunk(int chunkX, int chunkZ) {
        this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        byte[] b3 = new byte[32768];
        Chunk chunk4 = new Chunk(this.worldObj, b3, chunkX, chunkZ);
        int i10003 = chunkX << 2;
        int i10005 = chunkZ << 2;
        boolean z56 = true;
        boolean z55 = true;
        boolean z54 = true;
        int i9 = i10005;
        boolean z8 = false;
        int i7 = i10003;
        double[] d6 = this.noiseArray;
        ChunkProviderGenerate chunkProviderGenerate5 = this;
        if(d6 == null) {
            d6 = new double[425];
        }

        this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i7, 0, i9, 5, 1, 5, 1.0D, 0.0D, 1.0D);
        this.noise7 = this.noiseGen7.generateNoiseOctaves(this.noise7, i7, 0, i9, 5, 1, 5, 100.0D, 0.0D, 100.0D);
        this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, i7, 0, i9, 5, 17, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, i7, 0, i9, 5, 17, 5, 684.412D, 684.412D, 684.412D);
        this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, i7, 0, i9, 5, 17, 5, 684.412D, 684.412D, 684.412D);
        i9 = 0;
        int i62 = 0;

        for(int i63 = 0; i63 < 5; ++i63) {
            for(int i64 = 0; i64 < 5; ++i64) {
                double d65;
                if((d65 = (chunkProviderGenerate5.noise6[i62] + 256.0D) / 512.0D) > 1.0D) {
                    d65 = 1.0D;
                }

                double d67;
                if((d67 = chunkProviderGenerate5.noise7[i62] / 8000.0D) < 0.0D) {
                    d67 = -d67;
                }

                if((d67 = d67 * 3.0D - 3.0D) < 0.0D) {
                    if((d67 /= 2.0D) < -1.0D) {
                        d67 = -1.0D;
                    }

                    d67 = (d67 /= 1.4D) / 2.0D;
                    d65 = 0.0D;
                } else {
                    if(d67 > 1.0D) {
                        d67 = 1.0D;
                    }

                    d67 /= 6.0D;
                }

                d65 += 0.5D;
                d67 = d67 * 17.0D / 16.0D;
                double d69 = 8.5D + d67 * 4.0D;
                ++i62;

                for(int i71 = 0; i71 < 17; ++i71) {
                    double d74;
                    if((d74 = ((double)i71 - d69) * 12.0D / d65) < 0.0D) {
                        d74 *= 4.0D;
                    }

                    double d76 = chunkProviderGenerate5.noise1[i9] / 512.0D;
                    double d78 = chunkProviderGenerate5.noise2[i9] / 512.0D;
                    double d72;
                    double d80;
                    if((d80 = (chunkProviderGenerate5.noise3[i9] / 10.0D + 1.0D) / 2.0D) < 0.0D) {
                        d72 = d76;
                    } else if(d80 > 1.0D) {
                        d72 = d78;
                    } else {
                        d72 = d76 + (d78 - d76) * d80;
                    }

                    d72 -= d74;
                    d6[i9] = d72;
                    ++i9;
                }
            }
        }

        this.noiseArray = d6;

        int i84;
        int i85;
        for(i84 = 0; i84 < 4; ++i84) {
            for(i85 = 0; i85 < 4; ++i85) {
                for(i7 = 0; i7 < 16; ++i7) {
                    double d87 = this.noiseArray[(i84 * 5 + i85) * 17 + i7];
                    double d10 = this.noiseArray[(i84 * 5 + i85 + 1) * 17 + i7];
                    double d12 = this.noiseArray[((i84 + 1) * 5 + i85) * 17 + i7];
                    double d14 = this.noiseArray[((i84 + 1) * 5 + i85 + 1) * 17 + i7];
                    double d16 = this.noiseArray[(i84 * 5 + i85) * 17 + i7 + 1];
                    double d18 = this.noiseArray[(i84 * 5 + i85 + 1) * 17 + i7 + 1];
                    double d20 = this.noiseArray[((i84 + 1) * 5 + i85) * 17 + i7 + 1];
                    double d22 = this.noiseArray[((i84 + 1) * 5 + i85 + 1) * 17 + i7 + 1];

                    for(int i24 = 0; i24 < 8; ++i24) {
                        double d25 = (double)i24 / 8.0D;
                        double d27 = d87 + (d16 - d87) * d25;
                        double d29 = d10 + (d18 - d10) * d25;
                        double d31 = d12 + (d20 - d12) * d25;
                        double d33 = d14 + (d22 - d14) * d25;

                        for(int i94 = 0; i94 < 4; ++i94) {
                            double d36 = (double)i94 / 4.0D;
                            double d38 = d27 + (d31 - d27) * d36;
                            double d40 = d29 + (d33 - d29) * d36;
                            int i26 = i94 + (i84 << 2) << 11 | 0 + (i85 << 2) << 7 | (i7 << 3) + i24;

                            for(int i35 = 0; i35 < 4; ++i35) {
                                double d44 = (double)i35 / 4.0D;
                                double d46 = d38 + (d40 - d38) * d44;
                                int i95 = 0;
                                if((i7 << 3) + i24 < 64) {
                                    i95 = Block.waterStill.blockID;
                                }

                                if(d46 > 0.0D) {
                                    i95 = Block.stone.blockID;
                                }

                                b3[i26] = (byte)i95;
                                i26 += 128;
                            }
                        }
                    }
                }
            }
        }

        for(i84 = 0; i84 < 16; ++i84) {
            for(i85 = 0; i85 < 16; ++i85) {
                double d86 = (double)((chunkX << 4) + i84);
                double d89 = (double)((chunkZ << 4) + i85);
                boolean z13 = this.noiseGen4.generateNoiseOctaves(d86 * 8.0D / 256D, d89 * 8.0D / 256D, 0.0D) + this.rand.nextDouble() * 0.2D > 0.0D;
                boolean z90 = this.noiseGen4.generateNoiseOctaves(d89 * 8.0D / 256D, 109.0134D, d86 * 8.0D / 256D) + this.rand.nextDouble() * 0.2D > 3.0D;
                int i15 = (int)(this.noiseGen5.generateNoiseOctaves(d86 * 8.0D / 256D * 2.0D, d89 * 8.0D / 256D * 2.0D) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int i91 = i84 << 11 | i85 << 7 | 127;
                int i17 = -1;
                int i92 = Block.grass.blockID;
                int i19 = Block.dirt.blockID;

                for(int i93 = 127; i93 >= 0; --i93) {
                    if(i93 <= this.rand.nextInt(5)) {
                        b3[i91] = (byte)Block.bedrock.blockID;
                    } else if(b3[i91] == 0) {
                        i17 = -1;
                    } else if(b3[i91] == Block.stone.blockID) {
                        if(i17 == -1) {
                            if(i15 <= 0) {
                                i92 = 0;
                                i19 = (byte)Block.stone.blockID;
                            } else if(i93 >= 60 && i93 <= 65) {
                                i92 = Block.grass.blockID;
                                i19 = Block.dirt.blockID;
                                if(z90) {
                                    i92 = 0;
                                }

                                if(z90) {
                                    i19 = Block.gravel.blockID;
                                }

                                if(z13) {
                                    i92 = Block.sand.blockID;
                                }

                                if(z13) {
                                    i19 = Block.sand.blockID;
                                }
                            }

                            if(i93 < 64 && i92 == 0) {
                                i92 = Block.waterStill.blockID;
                            }

                            i17 = i15;
                            if(i93 >= 63) {
                                b3[i91] = (byte)i92;
                            } else {
                                b3[i91] = (byte)i19;
                            }
                        } else if(i17 > 0) {
                            --i17;
                            b3[i91] = (byte)i19;
                        }
                    }

                    --i91;
                }
            }
        }

        byte[] b88 = b3;
        i7 = chunkZ;
        i85 = chunkX;
        chunkProviderGenerate5 = this;
        this.rand.setSeed(this.worldObj.seed);
        long j96 = (this.rand.nextLong() / 2L << 1) + 1L;
        long j97 = (this.rand.nextLong() / 2L << 1) + 1L;

        for(chunkX -= 8; chunkX <= i85 + 8; ++chunkX) {
            for(chunkZ = i7 - 8; chunkZ <= i7 + 8; ++chunkZ) {
                chunkProviderGenerate5.rand.setSeed((long)chunkX * j96 + (long)chunkZ * j97 ^ chunkProviderGenerate5.worldObj.seed);
                int i83 = chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(40) + 1) + 1);
                if(chunkProviderGenerate5.rand.nextInt(10) != 0) {
                    i83 = 0;
                }

                for(i9 = 0; i9 < i83; ++i9) {
                    double d98 = (double)((chunkX << 4) + chunkProviderGenerate5.rand.nextInt(16));
                    double d99 = (double)chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(120) + 8);
                    double d66 = (double)((chunkZ << 4) + chunkProviderGenerate5.rand.nextInt(16));
                    int i68 = 1;
                    if(chunkProviderGenerate5.rand.nextInt(4) == 0) {
                        chunkProviderGenerate5.generateCaves(i85, i7, b88, d98, d99, d66, 1.0F + chunkProviderGenerate5.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
                        i68 = 1 + chunkProviderGenerate5.rand.nextInt(4);
                    }

                    for(int i100 = 0; i100 < i68; ++i100) {
                        float f70 = chunkProviderGenerate5.rand.nextFloat() * (float)Math.PI * 2.0F;
                        float f101 = (chunkProviderGenerate5.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                        float f102 = chunkProviderGenerate5.rand.nextFloat() * 2.0F + chunkProviderGenerate5.rand.nextFloat();
                        chunkProviderGenerate5.generateCaves(i85, i7, b88, d98, d99, d66, f102, f70, f101, 0, 0, 1.0D);
                    }
                }
            }
        }

        chunk4.generateHeightMap();
        return chunk4;
    }

    private void generateCaves(int chunkX, int chunkZ, byte[] chunkData, double x, double y, double z, float scaleFactor, float directionHorizontal, float directionVertical, int outwardsSize, int inwardsSize, double radius) {
        label204:
        while(true) {
            double d17 = (double)((chunkX << 4) + 8);
            double d19 = (double)((chunkZ << 4) + 8);
            float f21 = 0.0F;
            float f22 = 0.0F;
            EaglercraftRandom random23 = new EaglercraftRandom(this.rand.nextLong());
            if(inwardsSize <= 0) {
                inwardsSize = 112 - random23.nextInt(28);
            }

            boolean z24 = false;
            if(outwardsSize == -1) {
                outwardsSize = inwardsSize / 2;
                z24 = true;
            }

            int i25 = random23.nextInt(inwardsSize / 2) + inwardsSize / 4;

            for(boolean z26 = random23.nextInt(6) == 0; outwardsSize < inwardsSize; ++outwardsSize) {
                double d27;
                double d29 = (d27 = 1.5D + (double)(MathHelper.sin((float)outwardsSize * (float)Math.PI / (float)inwardsSize) * scaleFactor)) * radius;
                float f31 = MathHelper.cos(directionVertical);
                float f32 = MathHelper.sin(directionVertical);
                x += (double)(MathHelper.cos(directionHorizontal) * f31);
                y += (double)f32;
                z += (double)(MathHelper.sin(directionHorizontal) * f31);
                if(z26) {
                    directionVertical *= 0.92F;
                } else {
                    directionVertical *= 0.7F;
                }

                directionVertical += f22 * 0.1F;
                directionHorizontal += f21 * 0.1F;
                f22 *= 0.9F;
                f21 *= 0.75F;
                f22 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0F;
                f21 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4.0F;
                if(!z24 && outwardsSize == i25 && scaleFactor > 1.0F) {
                    this.generateCaves(chunkX, chunkZ, chunkData, x, y, z, random23.nextFloat() * 0.5F + 0.5F, directionHorizontal - (float)Math.PI / 2F, directionVertical / 3.0F, outwardsSize, inwardsSize, 1.0D);
                    float f10007 = random23.nextFloat() * 0.5F + 0.5F;
                    float f10008 = directionHorizontal + (float)Math.PI / 2F;
                    float f10009 = directionVertical / 3.0F;
                    radius = 1.0D;
                    inwardsSize = inwardsSize;
                    outwardsSize = outwardsSize;
                    directionVertical = f10009;
                    directionHorizontal = f10008;
                    scaleFactor = f10007;
                    z = z;
                    y = y;
                    x = x;
                    chunkData = chunkData;
                    chunkZ = chunkZ;
                    chunkX = chunkX;
                    continue label204;
                }

                if(z24 || random23.nextInt(4) != 0) {
                    double d33 = x - d17;
                    double d35 = z - d19;
                    double d37 = (double)(inwardsSize - outwardsSize);
                    double d39 = (double)(scaleFactor + 2.0F + 16.0F);
                    if(d33 * d33 + d35 * d35 - d37 * d37 > d39 * d39) {
                        return;
                    }

                    if(x >= d17 - 16.0D - d27 * 2.0D && z >= d19 - 16.0D - d27 * 2.0D && x <= d17 + 16.0D + d27 * 2.0D && z <= d19 + 16.0D + d27 * 2.0D) {
                        int i53 = MathHelper.floor_double(x - d27) - (chunkX << 4) - 1;
                        int i34 = MathHelper.floor_double(x + d27) - (chunkX << 4) + 1;
                        int i55 = MathHelper.floor_double(y - d29) - 1;
                        int i36 = MathHelper.floor_double(y + d29) + 1;
                        int i56 = MathHelper.floor_double(z - d27) - (chunkZ << 4) - 1;
                        int i38 = MathHelper.floor_double(z + d27) - (chunkZ << 4) + 1;
                        if(i53 < 0) {
                            i53 = 0;
                        }

                        if(i34 > 16) {
                            i34 = 16;
                        }

                        if(i55 <= 0) {
                            i55 = 1;
                        }

                        if(i36 > 120) {
                            i36 = 120;
                        }

                        if(i56 < 0) {
                            i56 = 0;
                        }

                        if(i38 > 16) {
                            i38 = 16;
                        }

                        boolean z57 = false;

                        int i40;
                        int i51;
                        for(i40 = i53; !z57 && i40 < i34; ++i40) {
                            for(int i41 = i56; !z57 && i41 < i38; ++i41) {
                                for(int i42 = i36 + 1; !z57 && i42 >= i55 - 1; --i42) {
                                    i51 = ((i40 << 4) + i41 << 7) + i42;
                                    if(i42 >= 0 && i42 < 128) {
                                        if(chunkData[i51] == Block.waterMoving.blockID || chunkData[i51] == Block.waterStill.blockID) {
                                            z57 = true;
                                        }

                                        if(i42 != i55 - 1 && i40 != i53 && i40 != i34 - 1 && i41 != i56 && i41 != i38 - 1) {
                                            i42 = i55;
                                        }
                                    }
                                }
                            }
                        }

                        if(!z57) {
                            for(i40 = i53; i40 < i34; ++i40) {
                                double d59 = ((double)(i40 + (chunkX << 4)) + 0.5D - x) / d27;

                                for(i51 = i56; i51 < i38; ++i51) {
                                    double d44 = ((double)(i51 + (chunkZ << 4)) + 0.5D - z) / d27;
                                    int i52 = ((i40 << 4) + i51 << 7) + i36;
                                    boolean z54 = false;

                                    for(int i58 = i36 - 1; i58 >= i55; --i58) {
                                        double d49;
                                        if((d49 = ((double)i58 + 0.5D - y) / d29) > -0.7D && d59 * d59 + d49 * d49 + d44 * d44 < 1.0D) {
                                            byte b43;
                                            if((b43 = chunkData[i52]) == Block.grass.blockID) {
                                                z54 = true;
                                            }

                                            if(b43 == Block.stone.blockID || b43 == Block.dirt.blockID || b43 == Block.grass.blockID) {
                                                if(i58 < 10) {
                                                    chunkData[i52] = (byte)Block.lavaMoving.blockID;
                                                } else {
                                                    chunkData[i52] = 0;
                                                    if(z54 && chunkData[i52 - 1] == Block.dirt.blockID) {
                                                        chunkData[i52 - 1] = (byte)Block.grass.blockID;
                                                    }
                                                }
                                            }
                                        }

                                        --i52;
                                    }
                                }
                            }

                            if(z24) {
                                break;
                            }
                        }
                    }
                }
            }

            return;
        }
    }

    public final boolean chunkExists(int chunkX, int chunkZ) {
        return true;
    }

    public final void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
        this.rand.setSeed((long)chunkX * 318279123L + (long)chunkZ * 919871212L);
        int i8 = chunkX << 4;
        chunkX = chunkZ << 4;

        int i4;
        int i5;
        int i6;
        for(chunkZ = 0; chunkZ < 20; ++chunkZ) {
            i4 = i8 + this.rand.nextInt(16);
            i5 = this.rand.nextInt(128);
            i6 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, i4, i5, i6);
        }

        for(chunkZ = 0; chunkZ < 10; ++chunkZ) {
            i4 = i8 + this.rand.nextInt(16);
            i5 = this.rand.nextInt(128);
            i6 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, i4, i5, i6);
        }

        for(chunkZ = 0; chunkZ < 20; ++chunkZ) {
            i4 = i8 + this.rand.nextInt(16);
            i5 = this.rand.nextInt(128);
            i6 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, i4, i5, i6);
        }

        for(chunkZ = 0; chunkZ < 20; ++chunkZ) {
            i4 = i8 + this.rand.nextInt(16);
            i5 = this.rand.nextInt(64);
            i6 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, i4, i5, i6);
        }

        if(this.rand.nextInt(1) == 0) {
            chunkZ = i8 + this.rand.nextInt(16);
            i4 = this.rand.nextInt(32);
            i5 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, chunkZ, i4, i5);
        }

        if(this.rand.nextInt(4) == 0) {
            chunkZ = i8 + this.rand.nextInt(16);
            i4 = this.rand.nextInt(16);
            i5 = chunkX + this.rand.nextInt(16);
            (new WorldGenMinable(Block.oreDiamond.blockID, 8)).generate(this.worldObj, this.rand, chunkZ, i4, i5);
        }

        if((chunkZ = (int)(this.mobSpawnerNoise.generateNoiseOctaves((double)i8 * 0.5D, (double)chunkX * 0.5D) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D)) < 0) {
            chunkZ = 0;
        }

        WorldGenTrees worldGenTrees9 = new WorldGenTrees();
        if(this.rand.nextInt(10) == 0) {
            ++chunkZ;
        }

        int i7;
        for(i5 = 0; i5 < chunkZ; ++i5) {
            i6 = i8 + this.rand.nextInt(16) + 8;
            i7 = chunkX + this.rand.nextInt(16) + 8;
            worldGenTrees9.generate(this.worldObj, this.rand, i6, this.worldObj.getHeightValue(i6, i7), i7);
        }

        for(i5 = 0; i5 < 2; ++i5) {
            i6 = i8 + this.rand.nextInt(16) + 8;
            i7 = this.rand.nextInt(128);
            chunkZ = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i6, i7, chunkZ);
        }

        if(this.rand.nextInt(2) == 0) {
            i5 = i8 + this.rand.nextInt(16) + 8;
            i6 = this.rand.nextInt(128);
            i7 = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i5, i6, i7);
        }

        if(this.rand.nextInt(4) == 0) {
            i5 = i8 + this.rand.nextInt(16) + 8;
            i6 = this.rand.nextInt(128);
            i7 = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i5, i6, i7);
        }

        if(this.rand.nextInt(8) == 0) {
            i5 = i8 + this.rand.nextInt(16) + 8;
            i6 = this.rand.nextInt(128);
            i7 = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i5, i6, i7);
        }

        for(i5 = 0; i5 < 50; ++i5) {
            i6 = i8 + this.rand.nextInt(16) + 8;
            i7 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            chunkZ = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i6, i7, chunkZ);
        }

        for(i5 = 0; i5 < 20; ++i5) {
            i6 = i8 + this.rand.nextInt(16) + 8;
            i7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
            chunkZ = chunkX + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i6, i7, chunkZ);
        }

    }

    public final void saveChunks(boolean flag) {
    }

    public final boolean unload100OldestChunks() {
        return false;
    }

    public final boolean canSave() {
        return true;
    }
}
