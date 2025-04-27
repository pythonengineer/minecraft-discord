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
import net.minecraft.game.level.material.Material;

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
        World var37 = new World();
        var37.waterLevel = this.waterLevel;
        var37.groundLevel = this.groundLevel;
        this.width = var2;
        this.depth = var3;
        this.height = var4;
        this.blocksByteArray = new byte[var2 * var3 * var4];
        int var5 = 1;
        if(this.floatingGen) {
            var5 = (var4 - 64) / 48 + 1;
        }

        LevelGenerator var8;
        int var42;
        int var46;
        int var48;
        for(int var6 = 0; var6 < var5; ++var6) {
            this.waterLevel = var4 - 32 - var6 * 48;
            this.groundLevel = this.waterLevel - 2;
            int[] var7;
            int var18;
            int var21;
            double var28;
            int[] var39;
            int var49;
            int var55;
            if(this.flatGen) {
                var7 = new int[var2 * var3];

                for(int var38 = 0; var38 < var7.length; ++var38) {
                    var7[var38] = 0;
                }
            } else {
                this.guiLoading.displayLoadingString("Raising..");
                var8 = this;
                NoiseGeneratorDistort var9 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                NoiseGeneratorDistort var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                NoiseGeneratorOctaves var11 = new NoiseGeneratorOctaves(this.rand, 6);
                NoiseGeneratorOctaves var12 = new NoiseGeneratorOctaves(this.rand, 2);
                int[] var13 = new int[this.width * this.depth];
                var18 = 0;

                label364:
                while(true) {
                    if(var18 >= var8.width) {
                        var7 = var13;
                        this.guiLoading.displayLoadingString("Eroding..");
                        var39 = var13;
                        var8 = this;
                        var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                        NoiseGeneratorDistort var44 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                        var48 = 0;

                        while(true) {
                            if(var48 >= var8.width) {
                                break label364;
                            }

                            var8.setNextPhase(var48 * 100 / (var8.width - 1));

                            for(var49 = 0; var49 < var8.depth; ++var49) {
                                double var16 = var10.generateNoise((double)(var48 << 1), (double)(var49 << 1)) / 8.0D;
                                var18 = var44.generateNoise((double)(var48 << 1), (double)(var49 << 1)) > 0.0D ? 1 : 0;
                                if(var16 > 2.0D) {
                                    var55 = var39[var48 + var49 * var8.width];
                                    var55 = ((var55 - var18) / 2 << 1) + var18;
                                    var39[var48 + var49 * var8.width] = var55;
                                }
                            }

                            ++var48;
                        }
                    }

                    double var19 = Math.abs(((double)var18 / ((double)var8.width - 1.0D) - 0.5D) * 2.0D);
                    var8.setNextPhase(var18 * 100 / (var8.width - 1));

                    for(var21 = 0; var21 < var8.depth; ++var21) {
                        double var22 = Math.abs(((double)var21 / ((double)var8.depth - 1.0D) - 0.5D) * 2.0D);
                        double var24 = var9.generateNoise((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 6.0D + -4.0D;
                        double var26 = var10.generateNoise((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 5.0D + 10.0D + -4.0D;
                        var28 = var11.generateNoise((double)var18, (double)var21) / 8.0D;
                        if(var28 > 0.0D) {
                            var26 = var24;
                        }

                        double var30 = Math.max(var24, var26) / 2.0D;
                        if(var8.islandGen) {
                            double var32 = Math.sqrt(var19 * var19 + var22 * var22) * (double)1.2F;
                            double var35 = var12.generateNoise((double)((float)var18 * 0.05F), (double)((float)var21 * 0.05F)) / 4.0D + 1.0D;
                            var32 = Math.min(var32, var35);
                            var32 = Math.max(var32, Math.max(var19, var22));
                            if(var32 > 1.0D) {
                                var32 = 1.0D;
                            }

                            if(var32 < 0.0D) {
                                var32 = 0.0D;
                            }

                            var32 *= var32;
                            var30 = var30 * (1.0D - var32) - var32 * 10.0D + 5.0D;
                            if(var30 < 0.0D) {
                                var30 -= var30 * var30 * (double)0.2F;
                            }
                        } else if(var30 < 0.0D) {
                            var30 *= 0.8D;
                        }

                        var13[var18 + var21 * var8.width] = (int)var30;
                    }

                    ++var18;
                }
            }

            this.guiLoading.displayLoadingString("Soiling..");
            var39 = var7;
            var8 = this;
            var42 = this.width;
            var46 = this.depth;
            var48 = this.height;
            NoiseGeneratorOctaves var50 = new NoiseGeneratorOctaves(this.rand, 8);
            NoiseGeneratorOctaves var51 = new NoiseGeneratorOctaves(this.rand, 8);

            int var17;
            int var20;
            int var25;
            int var27;
            int var70;
            for(var17 = 0; var17 < var42; ++var17) {
                double var53 = Math.abs(((double)var17 / ((double)var42 - 1.0D) - 0.5D) * 2.0D);
                var8.setNextPhase(var17 * 100 / (var8.width - 1));

                for(var20 = 0; var20 < var46; ++var20) {
                    double var60 = Math.abs(((double)var20 / ((double)var46 - 1.0D) - 0.5D) * 2.0D);
                    double var23 = Math.max(var53, var60);
                    var23 = var23 * var23 * var23;
                    var25 = (int)(var50.generateNoise((double)var17, (double)var20) / 24.0D) - 4;
                    var70 = var39[var17 + var20 * var42] + var8.waterLevel;
                    var27 = var70 + var25;
                    var39[var17 + var20 * var42] = Math.max(var70, var27);
                    if(var39[var17 + var20 * var42] > var48 - 2) {
                        var39[var17 + var20 * var42] = var48 - 2;
                    }

                    if(var39[var17 + var20 * var42] <= 0) {
                        var39[var17 + var20 * var42] = 1;
                    }

                    var28 = var51.generateNoise((double)var17 * 2.3D, (double)var20 * 2.3D) / 24.0D;
                    int var73 = (int)(Math.sqrt(Math.abs(var28)) * Math.signum(var28) * 20.0D) + var8.waterLevel;
                    var73 = (int)((double)var73 * (1.0D - var23) + var23 * (double)var8.height);
                    if(var73 > var8.waterLevel) {
                        var73 = var8.height;
                    }

                    for(int var31 = 0; var31 < var48; ++var31) {
                        int var77 = (var31 * var8.depth + var20) * var8.width + var17;
                        int var33 = 0;
                        if(var31 <= var70) {
                            var33 = Block.dirt.blockID;
                        }

                        if(var31 <= var27) {
                            var33 = Block.stone.blockID;
                        }

                        if(var8.floatingGen && var31 < var73) {
                            var33 = 0;
                        }

                        if(var8.blocksByteArray[var77] == 0) {
                            var8.blocksByteArray[var77] = (byte)var33;
                        }
                    }
                }
            }

            int var52;
            if(var6 == var5 - 1) {
                this.guiLoading.displayLoadingString("Carving..");
                boolean var43 = true;
                boolean var40 = false;
                var8 = this;
                var46 = this.width;
                var48 = this.depth;
                var49 = this.height;
                var52 = var46 * var48 * var49 / 256 / 64 << 1;
                var17 = 0;

                while(true) {
                    if(var17 >= var52) {
                        this.populateOre(Block.oreCoal.blockID, 90, 1, 4);
                        this.populateOre(Block.oreIron.blockID, 70, 2, 4);
                        this.populateOre(Block.oreGold.blockID, 50, 3, 4);
                        break;
                    }

                    var8.setNextPhase(var17 * 100 / (var52 - 1) / 4);
                    float var54 = var8.rand.nextFloat() * (float)var46;
                    float var56 = var8.rand.nextFloat() * (float)var49;
                    float var57 = var8.rand.nextFloat() * (float)var48;
                    var21 = (int)((var8.rand.nextFloat() + var8.rand.nextFloat()) * 200.0F);
                    float var61 = var8.rand.nextFloat() * (float)Math.PI * 2.0F;
                    float var63 = 0.0F;
                    float var64 = var8.rand.nextFloat() * (float)Math.PI * 2.0F;
                    float var67 = 0.0F;
                    float var71 = var8.rand.nextFloat() * var8.rand.nextFloat();

                    for(var27 = 0; var27 < var21; ++var27) {
                        var54 += MathHelper.sin(var61) * MathHelper.cos(var64);
                        var57 += MathHelper.cos(var61) * MathHelper.cos(var64);
                        var56 += MathHelper.sin(var64);
                        var61 += var63 * 0.2F;
                        var63 *= 0.9F;
                        var63 += var8.rand.nextFloat() - var8.rand.nextFloat();
                        var64 += var67 * 0.5F;
                        var64 *= 0.5F;
                        var67 *= 12.0F / 16.0F;
                        var67 += var8.rand.nextFloat() - var8.rand.nextFloat();
                        if(var8.rand.nextFloat() >= 0.25F) {
                            float var72 = var54 + (var8.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var29 = var56 + (var8.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var75 = var57 + (var8.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                            float var74 = ((float)var8.height - var29) / (float)var8.height;
                            float var78 = 1.2F + (var74 * 3.5F + 1.0F) * var71;
                            float var76 = MathHelper.sin((float)var27 * (float)Math.PI / (float)var21) * var78;

                            for(int var41 = (int)(var72 - var76); var41 <= (int)(var72 + var76); ++var41) {
                                for(int var79 = (int)(var29 - var76); var79 <= (int)(var29 + var76); ++var79) {
                                    for(int var36 = (int)(var75 - var76); var36 <= (int)(var75 + var76); ++var36) {
                                        float var45 = (float)var41 - var72;
                                        float var14 = (float)var79 - var29;
                                        float var15 = (float)var36 - var75;
                                        var45 = var45 * var45 + var14 * var14 * 2.0F + var15 * var15;
                                        if(var45 < var76 * var76 && var41 > 0 && var79 > 0 && var36 > 0 && var41 < var8.width - 1 && var79 < var8.height - 1 && var36 < var8.depth - 1) {
                                            var42 = (var79 * var8.depth + var36) * var8.width + var41;
                                            if(var8.blocksByteArray[var42] == Block.stone.blockID) {
                                                var8.blocksByteArray[var42] = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ++var17;
                }
            }

            if(!this.floatingGen) {
                this.guiLoading.displayLoadingString("Watering..");
                var8 = this;
                var48 = Block.waterStill.blockID;
                if(this.levelType == 1) {
                    var48 = Block.lavaStill.blockID;
                }

                this.setNextPhase(0);

                for(var49 = 0; var49 < var8.width; ++var49) {
                    var8.floodFill(var49, var8.waterLevel - 1, 0, 0, var48);
                    var8.floodFill(var49, var8.waterLevel - 1, var8.depth - 1, 0, var48);
                }

                for(var49 = 0; var49 < var8.depth; ++var49) {
                    var8.floodFill(0, var8.waterLevel - 1, var49, 0, var48);
                    var8.floodFill(var8.width - 1, var8.waterLevel - 1, var49, 0, var48);
                }

                var49 = var8.width * var8.depth / 8000;

                for(var52 = 0; var52 < var49; ++var52) {
                    if(var52 % 100 == 0) {
                        var8.setNextPhase(var52 * 100 / (var49 - 1));
                    }

                    var17 = var8.rand.nextInt(var8.width);
                    var18 = var8.waterLevel - 1 - var8.rand.nextInt(2);
                    var55 = var8.rand.nextInt(var8.depth);
                    if(var8.blocksByteArray[(var18 * var8.depth + var55) * var8.width + var17] == 0) {
                        var8.floodFill(var17, var18, var55, 0, var48);
                    }
                }

                var8.setNextPhase(100);
                this.guiLoading.displayLoadingString("Melting..");
                var8 = this;
                var42 = this.width * this.depth * this.height / 20000;

                for(var46 = 0; var46 < var42; ++var46) {
                    if(var46 % 100 == 0) {
                        var8.setNextPhase(var46 * 100 / (var42 - 1));
                    }

                    var48 = var8.rand.nextInt(var8.width);
                    var49 = (int)(var8.rand.nextFloat() * var8.rand.nextFloat() * (float)(var8.waterLevel - 3));
                    var52 = var8.rand.nextInt(var8.depth);
                    if(var8.blocksByteArray[(var49 * var8.depth + var52) * var8.width + var48] == 0) {
                        var8.floodFill(var48, var49, var52, 0, Block.lavaStill.blockID);
                    }
                }

                var8.setNextPhase(100);
            }

            this.guiLoading.displayLoadingString("Growing..");
            var39 = var7;
            var8 = this;
            var42 = this.width;
            var46 = this.depth;
            var50 = new NoiseGeneratorOctaves(this.rand, 8);
            var51 = new NoiseGeneratorOctaves(this.rand, 8);

            int var62;
            int var65;
            int var68;
            for(var17 = 0; var17 < var42; ++var17) {
                var8.setNextPhase(var17 * 100 / (var8.width - 1));

                for(var18 = 0; var18 < var46; ++var18) {
                    boolean var58 = var50.generateNoise((double)var17, (double)var18) > 8.0D;
                    if(var8.islandGen) {
                        var58 = var50.generateNoise((double)var17, (double)var18) > -8.0D;
                    }

                    boolean var59 = var51.generateNoise((double)var17, (double)var18) > 12.0D;
                    var21 = var39[var17 + var18 * var42];
                    var62 = (var21 * var8.depth + var18) * var8.width + var17;
                    var65 = var8.blocksByteArray[((var21 + 1) * var8.depth + var18) * var8.width + var17] & 255;
                    if((var65 == Block.waterMoving.blockID || var65 == Block.waterStill.blockID) && var21 <= var8.waterLevel - 1 && var59) {
                        var8.blocksByteArray[var62] = (byte)Block.gravel.blockID;
                    }

                    if(var65 == 0) {
                        var68 = -1;
                        if(var21 <= var8.waterLevel - 1 && var58) {
                            var68 = Block.sand.blockID;
                        }

                        if(var8.blocksByteArray[var62] != 0 && var68 > 0) {
                            var8.blocksByteArray[var62] = (byte)var68;
                        }
                    }
                }
            }

            this.guiLoading.displayLoadingString("Planting..");
            var39 = var7;
            var8 = this;
            var42 = this.width;
            var46 = this.width * this.depth / 3000;

            for(var48 = 0; var48 < var46; ++var48) {
                var49 = var8.rand.nextInt(2);
                var8.setNextPhase(var48 * 50 / var46);
                var52 = var8.rand.nextInt(var8.width);
                var17 = var8.rand.nextInt(var8.depth);

                for(var18 = 0; var18 < 10; ++var18) {
                    var55 = var52;
                    var20 = var17;

                    for(var21 = 0; var21 < 5; ++var21) {
                        var55 += var8.rand.nextInt(6) - var8.rand.nextInt(6);
                        var20 += var8.rand.nextInt(6) - var8.rand.nextInt(6);
                        if((var49 < 2 || var8.rand.nextInt(4) == 0) && var55 >= 0 && var20 >= 0 && var55 < var8.width && var20 < var8.depth) {
                            var62 = var39[var55 + var20 * var42] + 1;
                            boolean var66 = (var8.blocksByteArray[(var62 * var8.depth + var20) * var8.width + var55] & 255) == 0;
                            if(var66) {
                                var68 = (var62 * var8.depth + var20) * var8.width + var55;
                                var25 = var8.blocksByteArray[((var62 - 1) * var8.depth + var20) * var8.width + var55] & 255;
                                if(var25 == Block.grass.blockID) {
                                    if(var49 == 0) {
                                        var8.blocksByteArray[var68] = (byte)Block.plantYellow.blockID;
                                    } else if(var49 == 1) {
                                        var8.blocksByteArray[var68] = (byte)Block.plantRed.blockID;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var39 = var7;
            var8 = this;
            var42 = this.width;
            var48 = this.width * this.depth * this.height / 2000;

            for(var49 = 0; var49 < var48; ++var49) {
                var52 = var8.rand.nextInt(2);
                var8.setNextPhase(var49 * 50 / (var48 - 1) + 50);
                var17 = var8.rand.nextInt(var8.width);
                var18 = var8.rand.nextInt(var8.height);
                var55 = var8.rand.nextInt(var8.depth);

                for(var20 = 0; var20 < 20; ++var20) {
                    var21 = var17;
                    var62 = var18;
                    var65 = var55;

                    for(var68 = 0; var68 < 5; ++var68) {
                        var21 += var8.rand.nextInt(6) - var8.rand.nextInt(6);
                        var62 += var8.rand.nextInt(2) - var8.rand.nextInt(2);
                        var65 += var8.rand.nextInt(6) - var8.rand.nextInt(6);
                        if((var52 < 2 || var8.rand.nextInt(4) == 0) && var21 >= 0 && var65 >= 0 && var62 > 0 && var21 < var8.width && var65 < var8.depth && var62 < var39[var21 + var65 * var42] - 1) {
                            boolean var69 = (var8.blocksByteArray[(var62 * var8.depth + var65) * var8.width + var21] & 255) == 0;
                            if(var69) {
                                var70 = (var62 * var8.depth + var65) * var8.width + var21;
                                var27 = var8.blocksByteArray[((var62 - 1) * var8.depth + var65) * var8.width + var21] & 255;
                                if(var27 == Block.stone.blockID) {
                                    if(var52 == 0) {
                                        var8.blocksByteArray[var70] = (byte)Block.mushroomBrown.blockID;
                                    } else if(var52 == 1) {
                                        var8.blocksByteArray[var70] = (byte)Block.mushroomRed.blockID;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var37.cloudHeight = var4 + 2;
        if(this.floatingGen) {
            this.groundLevel = -128;
            this.waterLevel = this.groundLevel + 1;
            var37.cloudHeight = -16;
        } else if(!this.islandGen) {
            this.groundLevel = this.waterLevel + 1;
            this.waterLevel = this.groundLevel - 16;
        } else {
            this.groundLevel = this.waterLevel - 9;
        }

        if(this.levelType == 1) {
            var37.cloudColor = 2164736;
            var37.fogColor = 1049600;
            var37.skyColor = 1049600;
            var37.skyBrightness = 0.3F;
            var37.defaultFluid = Block.lavaMoving.blockID;
            if(this.floatingGen) {
                var37.cloudHeight = var4 + 2;
                this.waterLevel = -16;
            }
        }

        var37.waterLevel = this.waterLevel;
        var37.groundLevel = this.groundLevel;
        this.guiLoading.displayLoadingString("Calculating light..");
        var37.generate(var2, var4, var3, this.blocksByteArray);
        this.guiLoading.displayLoadingString("Post-processing..");
        if(this.levelType != 1) {
            World var47 = var37;
            var8 = this;

            for(var42 = 0; var42 < var8.width; ++var42) {
                for(var46 = 0; var46 < var8.height; ++var46) {
                    for(var48 = 0; var48 < var8.depth; ++var48) {
                        if(var47.getBlockId(var42, var46, var48) == Block.dirt.blockID && var47.isHalfLit(var42, var46 + 1, var48) && var47.getBlockMaterial(var42, var46 + 1, var48) == Material.air) {
                            var47.setBlock(var42, var46, var48, Block.grass.blockID);
                        }
                    }
                }
            }

            this.growTrees(var37);
        } else {
            this.growTrees(var37);
        }

        EagRuntime.currentTimeMillis();
        return var37;
    }

    private void growTrees(World var1) {
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

    private void populateOre(int var1, int var2, int var3, int var4) {
        byte var25 = (byte)var1;
        var4 = this.width;
        int var5 = this.depth;
        int var6 = this.height;
        int var7 = var4 * var5 * var6 / 256 / 64 * var2 / 100;

        for(int var8 = 0; var8 < var7; ++var8) {
            this.setNextPhase(var8 * 100 / (var7 - 1) / 4 + var3 * 100 / 4);
            float var9 = this.rand.nextFloat() * (float)var4;
            float var10 = this.rand.nextFloat() * (float)var6;
            float var11 = this.rand.nextFloat() * (float)var5;
            int var12 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)var2 / 100.0F);
            float var13 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var14 = 0.0F;
            float var15 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var16 = 0.0F;

            for(int var17 = 0; var17 < var12; ++var17) {
                var9 += MathHelper.sin(var13) * MathHelper.cos(var15);
                var11 += MathHelper.cos(var13) * MathHelper.cos(var15);
                var10 += MathHelper.sin(var15);
                var13 += var14 * 0.2F;
                var14 *= 0.9F;
                var14 += this.rand.nextFloat() - this.rand.nextFloat();
                var15 += var16 * 0.5F;
                var15 *= 0.5F;
                var16 *= 0.9F;
                var16 += this.rand.nextFloat() - this.rand.nextFloat();
                float var18 = MathHelper.sin((float)var17 * (float)Math.PI / (float)var12) * (float)var2 / 100.0F + 1.0F;

                for(int var19 = (int)(var9 - var18); var19 <= (int)(var9 + var18); ++var19) {
                    for(int var20 = (int)(var10 - var18); var20 <= (int)(var10 + var18); ++var20) {
                        for(int var21 = (int)(var11 - var18); var21 <= (int)(var11 + var18); ++var21) {
                            float var22 = (float)var19 - var9;
                            float var23 = (float)var20 - var10;
                            float var24 = (float)var21 - var11;
                            var22 = var22 * var22 + var23 * var23 * 2.0F + var24 * var24;
                            if(var22 < var18 * var18 && var19 > 0 && var20 > 0 && var21 > 0 && var19 < this.width - 1 && var20 < this.height - 1 && var21 < this.depth - 1) {
                                int var26 = (var20 * this.depth + var21) * this.width + var19;
                                if(this.blocksByteArray[var26] == Block.stone.blockID) {
                                    this.blocksByteArray[var26] = var25;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void setNextPhase(int var1) {
        this.guiLoading.setLoadingProgress(var1);
    }

    private long floodFill(int var1, int var2, int var3, int var4, int var5) {
        byte var20 = (byte)var5;
        ArrayList var21 = new ArrayList();
        byte var6 = 0;
        int var7 = 1;

        int var8;
        for(var8 = 1; 1 << var7 < this.width; ++var7) {
        }

        while(1 << var8 < this.depth) {
            ++var8;
        }

        int var9 = this.depth - 1;
        int var10 = this.width - 1;
        int var22 = var6 + 1;
        this.floodFillBlocks[0] = ((var2 << var8) + var3 << var7) + var1;
        long var13 = 0L;
        var1 = this.width * this.depth;

        while(var22 > 0) {
            --var22;
            var2 = this.floodFillBlocks[var22];
            if(var22 == 0 && var21.size() > 0) {
                this.floodFillBlocks = (int[])var21.remove(var21.size() - 1);
                var22 = this.floodFillBlocks.length;
            }

            var3 = var2 >> var7 & var9;
            int var11 = var2 >> var7 + var8;
            int var12 = var2 & var10;

            int var15;
            for(var15 = var12; var12 > 0 && this.blocksByteArray[var2 - 1] == 0; --var2) {
                --var12;
            }

            while(var15 < this.width && this.blocksByteArray[var2 + var15 - var12] == 0) {
                ++var15;
            }

            int var16 = var2 >> var7 & var9;
            int var17 = var2 >> var7 + var8;
            if(var16 != var3 || var17 != var11) {
                System.out.println("Diagonal flood!?");
            }

            boolean var23 = false;
            boolean var24 = false;
            boolean var18 = false;
            var13 += (long)(var15 - var12);

            for(var12 = var12; var12 < var15; ++var12) {
                this.blocksByteArray[var2] = var20;
                boolean var19;
                if(var3 > 0) {
                    var19 = this.blocksByteArray[var2 - this.width] == 0;
                    if(var19 && !var23) {
                        if(var22 == this.floodFillBlocks.length) {
                            var21.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var22 = 0;
                        }

                        this.floodFillBlocks[var22++] = var2 - this.width;
                    }

                    var23 = var19;
                }

                if(var3 < this.depth - 1) {
                    var19 = this.blocksByteArray[var2 + this.width] == 0;
                    if(var19 && !var24) {
                        if(var22 == this.floodFillBlocks.length) {
                            var21.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var22 = 0;
                        }

                        this.floodFillBlocks[var22++] = var2 + this.width;
                    }

                    var24 = var19;
                }

                if(var11 > 0) {
                    byte var25 = this.blocksByteArray[var2 - var1];
                    if((var20 == Block.lavaMoving.blockID || var20 == Block.lavaStill.blockID) && (var25 == Block.waterMoving.blockID || var25 == Block.waterStill.blockID)) {
                        this.blocksByteArray[var2 - var1] = (byte)Block.stone.blockID;
                    }

                    var19 = var25 == 0;
                    if(var19 && !var18) {
                        if(var22 == this.floodFillBlocks.length) {
                            var21.add(this.floodFillBlocks);
                            this.floodFillBlocks = new int[1048576];
                            var22 = 0;
                        }

                        this.floodFillBlocks[var22++] = var2 - var1;
                    }

                    var18 = var19;
                }

                ++var2;
            }
        }

        return var13;
    }
}
