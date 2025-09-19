package net.minecraft.game.level.generator;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;

public final class LevelGenerator {
    private IProgressUpdate guiLoading;
    private int width;
    private int depth;
    private int height;
    private EaglercraftRandom rand = new EaglercraftRandom();
    private byte[] blocksByteArray;
    private int waterLevel;
    private int groundLevel;
    public boolean islandGen = false;
    public boolean floatingGen = false;
    public boolean flatGen = false;
    public int levelType;
    private int[] floodFillBlocks = new int[1048576];

    public LevelGenerator(IProgressUpdate var1) {
        this.guiLoading = var1;
    }

    public final World generate(String var1, int var2, int var3, int var4) {
        this.guiLoading.displayProgressMessage("Generating level");
        World var5 = new World();
        var5.waterLevel = this.waterLevel;
        var5.groundLevel = this.groundLevel;
        this.width = var2;
        this.depth = var3;
        this.height = var4;
        this.blocksByteArray = new byte[var2 * var3 * var4];
        int var6 = 1;
        if(this.floatingGen) {
            var6 = (var4 - 64) / 48 + 1;
        }

        for(int var7 = 0; var7 < var6; ++var7) {
            this.waterLevel = var4 - 32 - var7 * 48;
            this.groundLevel = this.waterLevel - 2;
            int[] var8;
            LevelGenerator var9;
            int var24;
            double var31;
            int var40;
            int[] var41;
            int var49;
            int var50;
            if(this.flatGen) {
                var8 = new int[var2 * var3];

                for(var40 = 0; var40 < var8.length; ++var40) {
                    var8[var40] = 0;
                }
            } else {
                this.guiLoading.displayLoadingString("Raising..");
                var9 = this;
                NoiseGeneratorDistort var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                NoiseGeneratorDistort var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                NoiseGeneratorOctaves var12 = new NoiseGeneratorOctaves(this.rand, 6);
                NoiseGeneratorOctaves var13 = new NoiseGeneratorOctaves(this.rand, 2);
                int[] var14 = new int[this.width * this.depth];
                int var21 = 0;

                label180:
                while(true) {
                    if(var21 >= var9.width) {
                        var8 = var14;
                        this.guiLoading.displayLoadingString("Eroding..");
                        var41 = var14;
                        var9 = this;
                        var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                        NoiseGeneratorDistort var46 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                        var49 = 0;

                        while(true) {
                            if(var49 >= var9.width) {
                                break label180;
                            }

                            var9.setNextPhase(var49 * 100 / (var9.width - 1));

                            for(var50 = 0; var50 < var9.depth; ++var50) {
                                double var19 = var11.generateNoise((double)(var49 << 1), (double)(var50 << 1)) / 8.0D;
                                var21 = var46.generateNoise((double)(var49 << 1), (double)(var50 << 1)) > 0.0D ? 1 : 0;
                                if(var19 > 2.0D) {
                                    int var56 = var41[var49 + var50 * var9.width];
                                    var56 = ((var56 - var21) / 2 << 1) + var21;
                                    var41[var49 + var50 * var9.width] = var56;
                                }
                            }

                            ++var49;
                        }
                    }

                    double var22 = Math.abs(((double)var21 / ((double)var9.width - 1.0D) - 0.5D) * 2.0D);
                    var9.setNextPhase(var21 * 100 / (var9.width - 1));

                    for(var24 = 0; var24 < var9.depth; ++var24) {
                        double var25 = Math.abs(((double)var24 / ((double)var9.depth - 1.0D) - 0.5D) * 2.0D);
                        double var27 = var10.generateNoise((double)((float)var21 * 1.3F), (double)((float)var24 * 1.3F)) / 6.0D + -4.0D;
                        double var29 = var11.generateNoise((double)((float)var21 * 1.3F), (double)((float)var24 * 1.3F)) / 5.0D + 10.0D + -4.0D;
                        var31 = var12.generateNoise((double)var21, (double)var24) / 8.0D;
                        if(var31 > 0.0D) {
                            var29 = var27;
                        }

                        double var33 = Math.max(var27, var29) / 2.0D;
                        if(var9.islandGen) {
                            double var35 = Math.sqrt(var22 * var22 + var25 * var25) * (double)1.2F;
                            double var38 = var13.generateNoise((double)((float)var21 * 0.05F), (double)((float)var24 * 0.05F)) / 4.0D + 1.0D;
                            var35 = Math.min(var35, var38);
                            var35 = Math.max(var35, Math.max(var22, var25));
                            if(var35 > 1.0D) {
                                var35 = 1.0D;
                            }

                            if(var35 < 0.0D) {
                                var35 = 0.0D;
                            }

                            var35 *= var35;
                            var33 = var33 * (1.0D - var35) - var35 * 10.0D + 5.0D;
                            if(var33 < 0.0D) {
                                var33 -= var33 * var33 * (double)0.2F;
                            }
                        } else if(var33 < 0.0D) {
                            var33 *= 0.8D;
                        }

                        var14[var21 + var24 * var9.width] = (int)var33;
                    }

                    ++var21;
                }
            }

            this.guiLoading.displayLoadingString("Soiling..");
            var41 = var8;
            var9 = this;
            int var44 = this.width;
            int var48 = this.depth;
            var49 = this.height;
            NoiseGeneratorOctaves var51 = new NoiseGeneratorOctaves(this.rand, 8);
            NoiseGeneratorOctaves var52 = new NoiseGeneratorOctaves(this.rand, 8);

            int var20;
            int var30;
            for(var20 = 0; var20 < var44; ++var20) {
                double var54 = Math.abs(((double)var20 / ((double)var44 - 1.0D) - 0.5D) * 2.0D);
                var9.setNextPhase(var20 * 100 / (var9.width - 1));

                for(int var23 = 0; var23 < var48; ++var23) {
                    double var59 = Math.abs(((double)var23 / ((double)var48 - 1.0D) - 0.5D) * 2.0D);
                    double var26 = Math.max(var54, var59);
                    var26 = var26 * var26 * var26;
                    int var28 = (int)(var51.generateNoise((double)var20, (double)var23) / 24.0D) - 4;
                    int var64 = var41[var20 + var23 * var44] + var9.waterLevel;
                    var30 = var64 + var28;
                    var41[var20 + var23 * var44] = Math.max(var64, var30);
                    if(var41[var20 + var23 * var44] > var49 - 2) {
                        var41[var20 + var23 * var44] = var49 - 2;
                    }

                    if(var41[var20 + var23 * var44] <= 0) {
                        var41[var20 + var23 * var44] = 1;
                    }

                    var31 = var52.generateNoise((double)var20 * 2.3D, (double)var23 * 2.3D) / 24.0D;
                    int var67 = (int)(Math.sqrt(Math.abs(var31)) * Math.signum(var31) * 20.0D) + var9.waterLevel;
                    var67 = (int)((double)var67 * (1.0D - var26) + var26 * (double)var9.height);
                    if(var67 > var9.waterLevel) {
                        var67 = var9.height;
                    }

                    for(int var34 = 0; var34 < var49; ++var34) {
                        int var71 = (var34 * var9.depth + var23) * var9.width + var20;
                        int var36 = 0;
                        if(var34 <= var64) {
                            var36 = Block.dirt.blockID;
                        }

                        if(var34 <= var30) {
                            var36 = Block.stone.blockID;
                        }

                        if(var9.floatingGen && var34 < var67) {
                            var36 = 0;
                        }

                        if(var9.blocksByteArray[var71] == 0) {
                            var9.blocksByteArray[var71] = (byte)var36;
                        }
                    }
                }
            }

            if(var7 == var6 - 1) {
                this.guiLoading.displayLoadingString("Carving..");
                boolean var45 = true;
                boolean var42 = false;
                var9 = this;
                var48 = this.width;
                var49 = this.depth;
                var50 = this.height;
                int var53 = var48 * var49 * var50 / 256 / 64 << 1;
                var20 = 0;

                while(true) {
                    int var43;
                    if(var20 >= var53) {
                        var40 = this.a(Block.oreCoal.blockID, 1000, 10, 1, 5, (var4 << 2) / 5);
                        var43 = this.a(Block.oreIron.blockID, 800, 8, 2, 5, var4 * 3 / 5);
                        var44 = this.a(Block.oreGold.blockID, 500, 6, 3, 5, (var4 << 1) / 5);
                        var48 = this.a(Block.oreDiamond.blockID, 800, 2, 4, 5, var4 / 5);
                        System.out.println("Coal: " + var40 + ", Iron: " + var43 + ", Gold: " + var44 + ", Diamond: " + var48);
                        break;
                    }

                    var9.setNextPhase(var20 * 100 / (var53 - 1) / 5);
                    float var55 = var9.rand.nextFloat() * (float)var48;
                    float var57 = var9.rand.nextFloat() * (float)var50;
                    float var58 = var9.rand.nextFloat() * (float)var49;
                    var24 = (int)((var9.rand.nextFloat() + var9.rand.nextFloat()) * 200.0F);
                    float var60 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
                    float var61 = 0.0F;
                    float var62 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
                    float var63 = 0.0F;
                    float var65 = var9.rand.nextFloat() * var9.rand.nextFloat();

                    for(var30 = 0; var30 < var24; ++var30) {
                        var55 += MathHelper.sin(var60) * MathHelper.cos(var62);
                        var58 += MathHelper.cos(var60) * MathHelper.cos(var62);
                        var57 += MathHelper.sin(var62);
                        var60 += var61 * 0.2F;
                        var61 *= 0.9F;
                        var61 += var9.rand.nextFloat() - var9.rand.nextFloat();
                        var62 += var63 * 0.5F;
                        var62 *= 0.5F;
                        var63 *= 12.0F / 16.0F;
                        var63 += var9.rand.nextFloat() - var9.rand.nextFloat();
                        if(var9.rand.nextFloat() >= 0.25F) {
                            float var66 = var55 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var32 = var57 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var69 = var58 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var68 = ((float)var9.height - var32) / (float)var9.height;
                            float var72 = 1.2F + (var68 * 3.5F + 1.0F) * var65;
                            float var70 = MathHelper.sin((float)var30 * (float)Math.PI / (float)var24) * var72;

                            for(var43 = (int)(var66 - var70); var43 <= (int)(var66 + var70); ++var43) {
                                for(int var73 = (int)(var32 - var70); var73 <= (int)(var32 + var70); ++var73) {
                                    for(int var39 = (int)(var69 - var70); var39 <= (int)(var69 + var70); ++var39) {
                                        float var47 = (float)var43 - var66;
                                        float var15 = (float)var73 - var32;
                                        float var16 = (float)var39 - var69;
                                        var47 = var47 * var47 + var15 * var15 * 2.0F + var16 * var16;
                                        if(var47 < var70 * var70 && var43 > 0 && var73 > 0 && var39 > 0 && var43 < var9.width - 1 && var73 < var9.height - 1 && var39 < var9.depth - 1) {
                                            var44 = (var73 * var9.depth + var39) * var9.width + var43;
                                            if(var9.blocksByteArray[var44] == Block.stone.blockID) {
                                                var9.blocksByteArray[var44] = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ++var20;
                }
            }

            this.guiLoading.displayLoadingString("Watering..");
            this.a();
            this.guiLoading.displayLoadingString("Melting..");
            this.b();
            this.guiLoading.displayLoadingString("Growing..");
            this.a(var8);
            this.guiLoading.displayLoadingString("Planting..");
            this.b(var8);
            this.c(var8);
        }

        var5.cloudHeight = var4 + 2;
        if(this.floatingGen) {
            this.groundLevel = -128;
            this.waterLevel = this.groundLevel + 1;
            var5.cloudHeight = -16;
        } else if(!this.islandGen) {
            this.groundLevel = this.waterLevel + 1;
            this.waterLevel = this.groundLevel - 16;
        } else {
            this.groundLevel = this.waterLevel - 9;
        }

        if(this.levelType == 1) {
            var5.cloudColor = 2164736;
            var5.fogColor = 1049600;
            var5.skyColor = 1049600;
            var5.skyBrightness = 0.3F;
            var5.defaultFluid = Block.lavaMoving.blockID;
            if(this.floatingGen) {
                var5.cloudHeight = var4 + 2;
                this.waterLevel = -16;
            }
        }

        var5.waterLevel = this.waterLevel;
        var5.groundLevel = this.groundLevel;
        this.guiLoading.displayLoadingString("Calculating light..");
        var5.generate(var2, var4, var3, this.blocksByteArray);
        this.guiLoading.displayLoadingString("Post-processing..");
        if(this.levelType != 1) {
            this.growTrees(var5);
        }

        this.b(var5);
        var5.createTime = EagRuntime.currentTimeMillis();
        var5.authorName = var1;
        var5.name = "A Nice World";
        return var5;
    }

    private void growTrees(World var1) {
        for(int var2 = 0; var2 < this.width; ++var2) {
            for(int var3 = 0; var3 < this.height; ++var3) {
                for(int var4 = 0; var4 < this.depth; ++var4) {
                    if(var1.getBlockId(var2, var3, var4) == Block.dirt.blockID && var1.isHalfLit(var2, var3 + 1, var4) && !var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
                        var1.setBlock(var2, var3, var4, Block.grass.blockID);
                    }
                }
            }
        }

    }

    private void a(int[] var1) {
        int var2 = this.width;
        int var3 = this.depth;
        NoiseGeneratorOctaves var4 = new NoiseGeneratorOctaves(this.rand, 8);
        NoiseGeneratorOctaves var5 = new NoiseGeneratorOctaves(this.rand, 8);

        for(int var6 = 0; var6 < var2; ++var6) {
            this.setNextPhase(var6 * 100 / (this.width - 1));

            for(int var7 = 0; var7 < var3; ++var7) {
                boolean var8 = var4.generateNoise((double)var6, (double)var7) > 8.0D;
                if(this.islandGen) {
                    var8 = var4.generateNoise((double)var6, (double)var7) > -8.0D;
                }

                boolean var9 = var5.generateNoise((double)var6, (double)var7) > 12.0D;
                int var10 = var1[var6 + var7 * var2];
                int var11 = (var10 * this.depth + var7) * this.width + var6;
                int var12 = this.blocksByteArray[((var10 + 1) * this.depth + var7) * this.width + var6] & 255;
                if((var12 == Block.waterMoving.blockID || var12 == Block.waterStill.blockID) && var10 <= this.waterLevel - 1 && var9) {
                    this.blocksByteArray[var11] = (byte)Block.gravel.blockID;
                }

                if(var12 == 0) {
                    int var13 = -1;
                    if(var10 <= this.waterLevel - 1 && var8) {
                        var13 = Block.sand.blockID;
                    }

                    if(this.blocksByteArray[var11] != 0 && var13 > 0) {
                        this.blocksByteArray[var11] = (byte)var13;
                    }
                }
            }
        }

    }

    private void b(World var1) {
        int var2 = this.width * this.depth * this.height / 32000;

        for(int var3 = 0; var3 < var2; ++var3) {
            this.setNextPhase(var3 * 50 / var2 + 50);
            int var4 = this.rand.nextInt(this.width);
            int var5 = this.rand.nextInt(this.height);
            int var6 = this.rand.nextInt(this.depth);

            for(int var7 = 0; var7 < 100; ++var7) {
                int var8 = var4;
                int var9 = var5;
                int var10 = var6;

                for(int var11 = 0; var11 < 20; ++var11) {
                    var8 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    var9 += this.rand.nextInt(3) - this.rand.nextInt(3);
                    var10 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    if(var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.width && var9 < this.height && var10 < this.depth && this.rand.nextInt(4) == 0) {
                        var1.growTrees(var8, var9, var10);
                    }
                }
            }
        }

    }

    private void b(int[] var1) {
        int var2 = this.width;
        int var3 = this.width * this.depth / 3000;

        for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = this.rand.nextInt(2);
            this.setNextPhase(var4 * 50 / var3);
            int var6 = this.rand.nextInt(this.width);
            int var7 = this.rand.nextInt(this.depth);

            for(int var8 = 0; var8 < 10; ++var8) {
                int var9 = var6;
                int var10 = var7;

                for(int var11 = 0; var11 < 5; ++var11) {
                    var9 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    var10 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    if((var5 < 2 || this.rand.nextInt(4) == 0) && var9 >= 0 && var10 >= 0 && var9 < this.width && var10 < this.depth) {
                        int var12 = var1[var9 + var10 * var2] + 1;
                        boolean var13 = (this.blocksByteArray[(var12 * this.depth + var10) * this.width + var9] & 255) == 0;
                        if(var13) {
                            int var14 = (var12 * this.depth + var10) * this.width + var9;
                            var12 = this.blocksByteArray[((var12 - 1) * this.depth + var10) * this.width + var9] & 255;
                            if(var12 == Block.grass.blockID || var12 == Block.dirt.blockID) {
                                if(var5 == 0) {
                                    this.blocksByteArray[var14] = (byte)Block.plantYellow.blockID;
                                } else if(var5 == 1) {
                                    this.blocksByteArray[var14] = (byte)Block.plantRed.blockID;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void c(int[] var1) {
        int var2 = this.width;
        int var3 = this.width * this.depth * this.height / 2000;

        for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = this.rand.nextInt(2);
            this.setNextPhase(var4 * 50 / (var3 - 1) + 50);
            int var6 = this.rand.nextInt(this.width);
            int var7 = this.rand.nextInt(this.height);
            int var8 = this.rand.nextInt(this.depth);

            for(int var9 = 0; var9 < 20; ++var9) {
                int var10 = var6;
                int var11 = var7;
                int var12 = var8;

                for(int var13 = 0; var13 < 5; ++var13) {
                    var10 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    var11 += this.rand.nextInt(2) - this.rand.nextInt(2);
                    var12 += this.rand.nextInt(6) - this.rand.nextInt(6);
                    if((var5 < 2 || this.rand.nextInt(4) == 0) && var10 >= 0 && var12 >= 0 && var11 > 0 && var10 < this.width && var12 < this.depth && var11 < var1[var10 + var12 * var2] - 1) {
                        boolean var14 = (this.blocksByteArray[(var11 * this.depth + var12) * this.width + var10] & 255) == 0;
                        if(var14) {
                            int var16 = (var11 * this.depth + var12) * this.width + var10;
                            int var15 = this.blocksByteArray[((var11 - 1) * this.depth + var12) * this.width + var10] & 255;
                            if(var15 == Block.stone.blockID) {
                                if(var5 == 0) {
                                    this.blocksByteArray[var16] = (byte)Block.mushroomBrown.blockID;
                                } else if(var5 == 1) {
                                    this.blocksByteArray[var16] = (byte)Block.mushroomRed.blockID;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private int a(int var1, int var2, int var3, int var4, int var5, int var6) {
        var5 = 0;
        byte var27 = (byte)var1;
        int var7 = this.width;
        int var8 = this.depth;
        int var9 = this.height;
        var2 = var7 * var8 * var9 / 256 / 64 * var2 / 100;

        for(int var10 = 0; var10 < var2; ++var10) {
            this.setNextPhase(var10 * 100 / (var2 - 1) / 5 + var4 * 100 / 5);
            float var11 = this.rand.nextFloat() * (float)var7;
            float var12 = this.rand.nextFloat() * (float)var9;
            float var13 = this.rand.nextFloat() * (float)var8;
            if(var12 <= (float)var6) {
                int var14 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)var3 / 100.0F);
                float var15 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var16 = 0.0F;
                float var17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var18 = 0.0F;

                for(int var19 = 0; var19 < var14; ++var19) {
                    var11 += MathHelper.sin(var15) * MathHelper.cos(var17);
                    var13 += MathHelper.cos(var15) * MathHelper.cos(var17);
                    var12 += MathHelper.sin(var17);
                    var15 += var16 * 0.2F;
                    var16 *= 0.9F;
                    var16 += this.rand.nextFloat() - this.rand.nextFloat();
                    var17 += var18 * 0.5F;
                    var17 *= 0.5F;
                    var18 *= 0.9F;
                    var18 += this.rand.nextFloat() - this.rand.nextFloat();
                    float var20 = MathHelper.sin((float)var19 * (float)Math.PI / (float)var14) * (float)var3 / 100.0F + 1.0F;

                    for(int var21 = (int)(var11 - var20); var21 <= (int)(var11 + var20); ++var21) {
                        for(int var22 = (int)(var12 - var20); var22 <= (int)(var12 + var20); ++var22) {
                            for(int var23 = (int)(var13 - var20); var23 <= (int)(var13 + var20); ++var23) {
                                float var24 = (float)var21 - var11;
                                float var25 = (float)var22 - var12;
                                float var26 = (float)var23 - var13;
                                var24 = var24 * var24 + var25 * var25 * 2.0F + var26 * var26;
                                if(var24 < var20 * var20 && var21 > 0 && var22 > 0 && var23 > 0 && var21 < this.width - 1 && var22 < this.height - 1 && var23 < this.depth - 1) {
                                    int var28 = (var22 * this.depth + var23) * this.width + var21;
                                    if(this.blocksByteArray[var28] == Block.stone.blockID) {
                                        this.blocksByteArray[var28] = var27;
                                        ++var5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return var5;
    }

    private void a() {
        int var1 = Block.waterStill.blockID;
        if(this.levelType == 1) {
            var1 = Block.lavaStill.blockID;
        }

        int var2;
        if(!this.floatingGen) {
            this.setNextPhase(0);

            for(var2 = 0; var2 < this.width; ++var2) {
                this.floodFill(var2, this.waterLevel - 1, 0, 0, var1);
                this.floodFill(var2, this.waterLevel - 1, this.depth - 1, 0, var1);
            }

            for(var2 = 0; var2 < this.depth; ++var2) {
                this.floodFill(0, this.waterLevel - 1, var2, 0, var1);
                this.floodFill(this.width - 1, this.waterLevel - 1, var2, 0, var1);
            }
        }

        var2 = this.width * this.depth * this.height / 1000;

        for(int var3 = 0; var3 < var2; ++var3) {
            if(var3 % 100 == 0) {
                this.setNextPhase(var3 * 100 / (var2 - 1));
            }

            int var4 = this.rand.nextInt(this.width);
            int var5 = this.rand.nextInt(this.height);
            int var6 = this.rand.nextInt(this.depth);
            if(this.blocksByteArray[(var5 * this.depth + var6) * this.width + var4] == 0) {
                long var7 = this.floodFill(var4, var5, var6, 0, 255);
                if(var7 > 0L && var7 < 640L) {
                    this.floodFill(var4, var5, var6, 255, var1);
                } else {
                    this.floodFill(var4, var5, var6, 255, 0);
                }
            }
        }

        this.setNextPhase(100);
    }

    private void setNextPhase(int var1) {
        this.guiLoading.setLoadingProgress(var1);
    }

    private void b() {
        int var1 = this.width * this.depth * this.height / 2000;
        int var2 = this.groundLevel;

        for(int var3 = 0; var3 < var1; ++var3) {
            if(var3 % 100 == 0) {
                this.setNextPhase(var3 * 100 / (var1 - 1));
            }

            int var4 = this.rand.nextInt(this.width);
            int var5 = Math.min(Math.min(this.rand.nextInt(var2), this.rand.nextInt(var2)), Math.min(this.rand.nextInt(var2), this.rand.nextInt(var2)));
            int var6 = this.rand.nextInt(this.depth);
            if(this.blocksByteArray[(var5 * this.depth + var6) * this.width + var4] == 0) {
                long var7 = this.floodFill(var4, var5, var6, 0, 255);
                if(var7 > 0L && var7 < 640L) {
                    this.floodFill(var4, var5, var6, 255, Block.lavaStill.blockID);
                } else {
                    this.floodFill(var4, var5, var6, 255, 0);
                }
            }
        }

        this.setNextPhase(100);
    }

    private long floodFill(int var1, int var2, int var3, int var4, int var5) {
        byte var6 = (byte)var5;
        byte var22 = (byte)var4;
        ArrayList var7 = new ArrayList();
        byte var8 = 0;
        int var9 = 1;

        int var10;
        for(var10 = 1; 1 << var9 < this.width; ++var9) {
        }

        while(1 << var10 < this.depth) {
            ++var10;
        }

        int var11 = this.depth - 1;
        int var12 = this.width - 1;
        int var23 = var8 + 1;
        this.floodFillBlocks[0] = ((var2 << var10) + var3 << var9) + var1;
        long var14 = 0L;
        var1 = this.width * this.depth;

        while(var23 > 0) {
            --var23;
            var2 = this.floodFillBlocks[var23];
            if(var23 == 0 && var7.size() > 0) {
                this.floodFillBlocks = (int[])var7.remove(var7.size() - 1);
                var23 = this.floodFillBlocks.length;
            }

            var3 = var2 >> var9 & var11;
            int var13 = var2 >> var9 + var10;
            int var16 = var2 & var12;

            int var17;
            for(var17 = var16; var16 > 0 && this.blocksByteArray[var2 - 1] == var22; --var2) {
                --var16;
            }

            while(var17 < this.width && this.blocksByteArray[var2 + var17 - var16] == var22) {
                ++var17;
            }

            int var18 = var2 >> var9 & var11;
            int var19 = var2 >> var9 + var10;
            if(var5 == 255 && (var16 == 0 || var17 == this.width - 1 || var13 == 0 || var13 == this.height - 1 || var3 == 0 || var3 == this.depth - 1)) {
                return -1L;
            }

            if(var18 != var3 || var19 != var13) {
                System.out.println("Diagonal flood!?");
            }

            boolean var24 = false;
            boolean var25 = false;
            boolean var20 = false;
            var14 += (long)(var17 - var16);

            for(var16 = var16; var16 < var17; ++var16) {
                this.blocksByteArray[var2] = var6;
                boolean var21;
                if(var3 > 0) {
                    var21 = this.blocksByteArray[var2 - this.width] == var22;
                    if(var21 && !var24) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 - this.width;
                    }

                    var24 = var21;
                }

                if(var3 < this.depth - 1) {
                    var21 = this.blocksByteArray[var2 + this.width] == var22;
                    if(var21 && !var25) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 + this.width;
                    }

                    var25 = var21;
                }

                if(var13 > 0) {
                    byte var26 = this.blocksByteArray[var2 - var1];
                    if((var6 == Block.lavaMoving.blockID || var6 == Block.lavaStill.blockID) && (var26 == Block.waterMoving.blockID || var26 == Block.waterStill.blockID)) {
                        this.blocksByteArray[var2 - var1] = (byte)Block.stone.blockID;
                    }

                    var21 = var26 == var22;
                    if(var21 && !var20) {
                        if(var23 == this.floodFillBlocks.length) {
                            var7.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var23 = 0;
                        }

                        this.floodFillBlocks[var23++] = var2 - var1;
                    }

                    var20 = var21;
                }

                ++var2;
            }
        }

        return var14;
    }
}
