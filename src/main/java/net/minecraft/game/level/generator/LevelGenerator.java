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

		LevelGenerator var9;
		int var41;
		int var45;
		int var47;
		for(int var7 = 0; var7 < var6; ++var7) {
			this.waterLevel = var4 - 32 - var7 * 48;
			this.groundLevel = this.waterLevel - 2;
			int[] var8;
			int var18;
			int var21;
			double var28;
			int[] var38;
			int var48;
			int var55;
			if(this.flatGen) {
				var8 = new int[var2 * var3];

				for(int var37 = 0; var37 < var8.length; ++var37) {
					var8[var37] = 0;
				}
			} else {
				this.guiLoading.displayLoadingString("Raising..");
				var9 = this;
				NoiseGeneratorDistort var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorDistort var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorOctaves var12 = new NoiseGeneratorOctaves(this.rand, 6);
				NoiseGeneratorOctaves var13 = new NoiseGeneratorOctaves(this.rand, 2);
				int[] var14 = new int[this.width * this.depth];
				var18 = 0;

				label356:
				while(true) {
					if(var18 >= var9.width) {
						var8 = var14;
						this.guiLoading.displayLoadingString("Eroding..");
						var38 = var14;
						var9 = this;
						var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						NoiseGeneratorDistort var43 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						var47 = 0;

						while(true) {
							if(var47 >= var9.width) {
								break label356;
							}

							var9.setNextPhase(var47 * 100 / (var9.width - 1));

							for(var48 = 0; var48 < var9.depth; ++var48) {
								double var16 = var11.generateNoise((double)(var47 << 1), (double)(var48 << 1)) / 8.0D;
								var18 = var43.generateNoise((double)(var47 << 1), (double)(var48 << 1)) > 0.0D ? 1 : 0;
								if(var16 > 2.0D) {
									var55 = var38[var47 + var48 * var9.width];
									var55 = ((var55 - var18) / 2 << 1) + var18;
									var38[var47 + var48 * var9.width] = var55;
								}
							}

							++var47;
						}
					}

					double var19 = Math.abs(((double)var18 / ((double)var9.width - 1.0D) - 0.5D) * 2.0D);
					var9.setNextPhase(var18 * 100 / (var9.width - 1));

					for(var21 = 0; var21 < var9.depth; ++var21) {
						double var22 = Math.abs(((double)var21 / ((double)var9.depth - 1.0D) - 0.5D) * 2.0D);
						double var24 = var10.generateNoise((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 6.0D + -4.0D;
						double var26 = var11.generateNoise((double)((float)var18 * 1.3F), (double)((float)var21 * 1.3F)) / 5.0D + 10.0D + -4.0D;
						var28 = var12.generateNoise((double)var18, (double)var21) / 8.0D;
						if(var28 > 0.0D) {
							var26 = var24;
						}

						double var30 = Math.max(var24, var26) / 2.0D;
						if(var9.islandGen) {
							double var32 = Math.sqrt(var19 * var19 + var22 * var22) * (double)1.2F;
							double var35 = var13.generateNoise((double)((float)var18 * 0.05F), (double)((float)var21 * 0.05F)) / 4.0D + 1.0D;
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

						var14[var18 + var21 * var9.width] = (int)var30;
					}

					++var18;
				}
			}

			this.guiLoading.displayLoadingString("Soiling..");
			var38 = var8;
			var9 = this;
			var41 = this.width;
			var45 = this.depth;
			var47 = this.height;
			NoiseGeneratorOctaves var49 = new NoiseGeneratorOctaves(this.rand, 8);
			NoiseGeneratorOctaves var50 = new NoiseGeneratorOctaves(this.rand, 8);

			int var17;
			int var20;
			int var25;
			int var27;
			int var71;
			for(var17 = 0; var17 < var41; ++var17) {
				double var53 = Math.abs(((double)var17 / ((double)var41 - 1.0D) - 0.5D) * 2.0D);
				var9.setNextPhase(var17 * 100 / (var9.width - 1));

				for(var20 = 0; var20 < var45; ++var20) {
					double var60 = Math.abs(((double)var20 / ((double)var45 - 1.0D) - 0.5D) * 2.0D);
					double var23 = Math.max(var53, var60);
					var23 = var23 * var23 * var23;
					var25 = (int)(var49.generateNoise((double)var17, (double)var20) / 24.0D) - 4;
					var71 = var38[var17 + var20 * var41] + var9.waterLevel;
					var27 = var71 + var25;
					var38[var17 + var20 * var41] = Math.max(var71, var27);
					if(var38[var17 + var20 * var41] > var47 - 2) {
						var38[var17 + var20 * var41] = var47 - 2;
					}

					if(var38[var17 + var20 * var41] <= 0) {
						var38[var17 + var20 * var41] = 1;
					}

					var28 = var50.generateNoise((double)var17 * 2.3D, (double)var20 * 2.3D) / 24.0D;
					int var74 = (int)(Math.sqrt(Math.abs(var28)) * Math.signum(var28) * 20.0D) + var9.waterLevel;
					var74 = (int)((double)var74 * (1.0D - var23) + var23 * (double)var9.height);
					if(var74 > var9.waterLevel) {
						var74 = var9.height;
					}

					for(int var31 = 0; var31 < var47; ++var31) {
						int var78 = (var31 * var9.depth + var20) * var9.width + var17;
						int var33 = 0;
						if(var31 <= var71) {
							var33 = Block.dirt.blockID;
						}

						if(var31 <= var27) {
							var33 = Block.stone.blockID;
						}

						if(var9.floatingGen && var31 < var74) {
							var33 = 0;
						}

						if(var9.blocksByteArray[var78] == 0) {
							var9.blocksByteArray[var78] = (byte)var33;
						}
					}
				}
			}

			int var51;
			if(var7 == var6 - 1) {
				this.guiLoading.displayLoadingString("Carving..");
				boolean var42 = true;
				boolean var39 = false;
				var9 = this;
				var45 = this.width;
				var47 = this.depth;
				var48 = this.height;
				var51 = var45 * var47 * var48 / 256 / 64 << 1;
				var17 = 0;

				while(true) {
					if(var17 >= var51) {
                        this.populateOre(Block.oreCoal.blockID, 90, 1, 5, (var4 << 2) / 4);
                        this.populateOre(Block.oreIron.blockID, 70, 2, 5, var4 * 3 / 4);
                        this.populateOre(Block.oreGold.blockID, 30, 3, 5, (var4 << 1) / 4);
                        this.populateOre(Block.oreDiamond.blockID, 20, 4, 5, var4 / 4);
						break;
					}

					var9.setNextPhase(var17 * 100 / (var51 - 1) / 5);
					float var54 = var9.rand.nextFloat() * (float)var45;
					float var56 = var9.rand.nextFloat() * (float)var48;
					float var57 = var9.rand.nextFloat() * (float)var47;
					var21 = (int)((var9.rand.nextFloat() + var9.rand.nextFloat()) * 200.0F);
					float var62 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
					float var64 = 0.0F;
					float var65 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
					float var68 = 0.0F;
					float var72 = var9.rand.nextFloat() * var9.rand.nextFloat();

					for(var27 = 0; var27 < var21; ++var27) {
						var54 += MathHelper.sin(var62) * MathHelper.cos(var65);
						var57 += MathHelper.cos(var62) * MathHelper.cos(var65);
						var56 += MathHelper.sin(var65);
						var62 += var64 * 0.2F;
						var64 *= 0.9F;
						var64 += var9.rand.nextFloat() - var9.rand.nextFloat();
						var65 += var68 * 0.5F;
						var65 *= 0.5F;
						var68 *= 12.0F / 16.0F;
						var68 += var9.rand.nextFloat() - var9.rand.nextFloat();
						if(var9.rand.nextFloat() >= 0.25F) {
							float var73 = var54 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var29 = var56 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var76 = var57 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
							float var75 = ((float)var9.height - var29) / (float)var9.height;
							float var79 = 1.2F + (var75 * 3.5F + 1.0F) * var72;
							float var77 = MathHelper.sin((float)var27 * (float)Math.PI / (float)var21) * var79;

							for(int var40 = (int)(var73 - var77); var40 <= (int)(var73 + var77); ++var40) {
								for(int var80 = (int)(var29 - var77); var80 <= (int)(var29 + var77); ++var80) {
									for(int var36 = (int)(var76 - var77); var36 <= (int)(var76 + var77); ++var36) {
										float var44 = (float)var40 - var73;
										float var15 = (float)var80 - var29;
										var75 = (float)var36 - var76;
										var44 = var44 * var44 + var15 * var15 * 2.0F + var75 * var75;
										if(var44 < var77 * var77 && var40 > 0 && var80 > 0 && var36 > 0 && var40 < var9.width - 1 && var80 < var9.height - 1 && var36 < var9.depth - 1) {
											var41 = (var80 * var9.depth + var36) * var9.width + var40;
											if(var9.blocksByteArray[var41] == Block.stone.blockID) {
												var9.blocksByteArray[var41] = 0;
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

			this.guiLoading.displayLoadingString("Watering..");
			var9 = this;
			var47 = Block.waterStill.blockID;
			if(this.levelType == 1) {
				var47 = Block.lavaStill.blockID;
			}

			if(!this.floatingGen) {
				this.setNextPhase(0);

				for(var48 = 0; var48 < var9.width; ++var48) {
					var9.floodFill(var48, var9.waterLevel - 1, 0, 0, var47);
					var9.floodFill(var48, var9.waterLevel - 1, var9.depth - 1, 0, var47);
				}

				for(var48 = 0; var48 < var9.depth; ++var48) {
					var9.floodFill(0, var9.waterLevel - 1, var48, 0, var47);
					var9.floodFill(var9.width - 1, var9.waterLevel - 1, var48, 0, var47);
				}
			}

			var48 = var9.width * var9.depth * var9.height / 1000;

			for(var51 = 0; var51 < var48; ++var51) {
				if(var51 % 100 == 0) {
					var9.setNextPhase(var51 * 100 / (var48 - 1));
				}

				var17 = var9.rand.nextInt(var9.width);
				var18 = var9.rand.nextInt(var9.height);
				var55 = var9.rand.nextInt(var9.depth);
				if(var9.blocksByteArray[(var18 * var9.depth + var55) * var9.width + var17] == 0) {
					long var59 = var9.floodFill(var17, var18, var55, 0, 255);
					if(var59 > 0L && var59 < 512L) {
						var9.floodFill(var17, var18, var55, 255, var47);
					} else {
						var9.floodFill(var17, var18, var55, 255, 0);
					}
				}
			}

			var9.setNextPhase(100);
			this.guiLoading.displayLoadingString("Melting..");
			var9 = this;
			var41 = this.width * this.depth * this.height / 2000;

			for(var45 = 0; var45 < var41; ++var45) {
				if(var45 % 100 == 0) {
					var9.setNextPhase(var45 * 100 / (var41 - 1));
				}

				var47 = var9.rand.nextInt(var9.width);
				var48 = Math.min(Math.min(var9.rand.nextInt(var9.height), var9.rand.nextInt(var9.width)), Math.min(var9.rand.nextInt(var9.height), var9.rand.nextInt(var9.width)));
				var51 = var9.rand.nextInt(var9.depth);
				if(var9.blocksByteArray[(var48 * var9.depth + var51) * var9.width + var47] == 0) {
					long var52 = var9.floodFill(var47, var48, var51, 0, 255);
					if(var52 > 0L && var52 < 512L) {
						var9.floodFill(var47, var48, var51, 255, Block.lavaStill.blockID);
					} else {
						var9.floodFill(var47, var48, var51, 255, 0);
					}
				}
			}

			var9.setNextPhase(100);
			this.guiLoading.displayLoadingString("Growing..");
			var38 = var8;
			var9 = this;
			var41 = this.width;
			var45 = this.depth;
			var49 = new NoiseGeneratorOctaves(this.rand, 8);
			var50 = new NoiseGeneratorOctaves(this.rand, 8);

			int var63;
			int var66;
			int var69;
			for(var17 = 0; var17 < var41; ++var17) {
				var9.setNextPhase(var17 * 100 / (var9.width - 1));

				for(var18 = 0; var18 < var45; ++var18) {
					boolean var58 = var49.generateNoise((double)var17, (double)var18) > 8.0D;
					if(var9.islandGen) {
						var58 = var49.generateNoise((double)var17, (double)var18) > -8.0D;
					}

					boolean var61 = var50.generateNoise((double)var17, (double)var18) > 12.0D;
					var21 = var38[var17 + var18 * var41];
					var63 = (var21 * var9.depth + var18) * var9.width + var17;
					var66 = var9.blocksByteArray[((var21 + 1) * var9.depth + var18) * var9.width + var17] & 255;
					if((var66 == Block.waterMoving.blockID || var66 == Block.waterStill.blockID) && var21 <= var9.waterLevel - 1 && var61) {
						var9.blocksByteArray[var63] = (byte)Block.gravel.blockID;
					}

					if(var66 == 0) {
						var69 = -1;
						if(var21 <= var9.waterLevel - 1 && var58) {
							var69 = Block.sand.blockID;
						}

						if(var9.blocksByteArray[var63] != 0 && var69 > 0) {
							var9.blocksByteArray[var63] = (byte)var69;
						}
					}
				}
			}

			this.guiLoading.displayLoadingString("Planting..");
			var38 = var8;
			var9 = this;
			var41 = this.width;
			var45 = this.width * this.depth / 3000;

			for(var47 = 0; var47 < var45; ++var47) {
				var48 = var9.rand.nextInt(2);
				var9.setNextPhase(var47 * 50 / var45);
				var51 = var9.rand.nextInt(var9.width);
				var17 = var9.rand.nextInt(var9.depth);

				for(var18 = 0; var18 < 10; ++var18) {
					var55 = var51;
					var20 = var17;

					for(var21 = 0; var21 < 5; ++var21) {
						var55 += var9.rand.nextInt(6) - var9.rand.nextInt(6);
						var20 += var9.rand.nextInt(6) - var9.rand.nextInt(6);
						if((var48 < 2 || var9.rand.nextInt(4) == 0) && var55 >= 0 && var20 >= 0 && var55 < var9.width && var20 < var9.depth) {
							var63 = var38[var55 + var20 * var41] + 1;
							boolean var67 = (var9.blocksByteArray[(var63 * var9.depth + var20) * var9.width + var55] & 255) == 0;
							if(var67) {
								var69 = (var63 * var9.depth + var20) * var9.width + var55;
								var25 = var9.blocksByteArray[((var63 - 1) * var9.depth + var20) * var9.width + var55] & 255;
								if(var25 == Block.grass.blockID || var25 == Block.dirt.blockID) {
									if(var48 == 0) {
										var9.blocksByteArray[var69] = (byte)Block.plantYellow.blockID;
									} else if(var48 == 1) {
										var9.blocksByteArray[var69] = (byte)Block.plantRed.blockID;
									}
								}
							}
						}
					}
				}
			}

			var38 = var8;
			var9 = this;
			var41 = this.width;
			var47 = this.width * this.depth * this.height / 2000;

			for(var48 = 0; var48 < var47; ++var48) {
				var51 = var9.rand.nextInt(2);
				var9.setNextPhase(var48 * 50 / (var47 - 1) + 50);
				var17 = var9.rand.nextInt(var9.width);
				var18 = var9.rand.nextInt(var9.height);
				var55 = var9.rand.nextInt(var9.depth);

				for(var20 = 0; var20 < 20; ++var20) {
					var21 = var17;
					var63 = var18;
					var66 = var55;

					for(var69 = 0; var69 < 5; ++var69) {
						var21 += var9.rand.nextInt(6) - var9.rand.nextInt(6);
						var63 += var9.rand.nextInt(2) - var9.rand.nextInt(2);
						var66 += var9.rand.nextInt(6) - var9.rand.nextInt(6);
						if((var51 < 2 || var9.rand.nextInt(4) == 0) && var21 >= 0 && var66 >= 0 && var63 > 0 && var21 < var9.width && var66 < var9.depth && var63 < var38[var21 + var66 * var41] - 1) {
							boolean var70 = (var9.blocksByteArray[(var63 * var9.depth + var66) * var9.width + var21] & 255) == 0;
							if(var70) {
								var71 = (var63 * var9.depth + var66) * var9.width + var21;
								var27 = var9.blocksByteArray[((var63 - 1) * var9.depth + var66) * var9.width + var21] & 255;
								if(var27 == Block.stone.blockID) {
									if(var51 == 0) {
										var9.blocksByteArray[var71] = (byte)Block.mushroomBrown.blockID;
									} else if(var51 == 1) {
										var9.blocksByteArray[var71] = (byte)Block.mushroomRed.blockID;
									}
								}
							}
						}
					}
				}
			}
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
			World var46 = var5;
			var9 = this;

			for(var41 = 0; var41 < var9.width; ++var41) {
				for(var45 = 0; var45 < var9.height; ++var45) {
					for(var47 = 0; var47 < var9.depth; ++var47) {
						if(var46.getBlockId(var41, var45, var47) == Block.dirt.blockID && var46.isHalfLit(var41, var45 + 1, var47) && var46.getBlockMaterial(var41, var45 + 1, var47) == Material.air) {
							var46.setBlock(var41, var45, var47, Block.grass.blockID);
						}
					}
				}
			}

			this.growTrees(var5);
		} else {
			this.growTrees(var5);
		}

		var5.createTime = EagRuntime.currentTimeMillis();
		var5.authorName = var1;
		var5.name = "A Nice World";
		return var5;
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

    private void populateOre(int var1, int var2, int var3, int var4, int var5) {
        byte var26 = (byte)var1;
        var4 = this.width;
        int var6 = this.depth;
        int var7 = this.height;
        int var8 = var4 * var6 * var7 / 256 / 64 * var2 / 100;

        for(int var9 = 0; var9 < var8; ++var9) {
            this.setNextPhase(var9 * 100 / (var8 - 1) / 5 + var3 * 100 / 5);
            float var10 = this.rand.nextFloat() * (float)var4;
            float var11 = this.rand.nextFloat() * (float)var7;
            float var12 = this.rand.nextFloat() * (float)var6;
            if(var11 <= (float)var5) {
                int var13 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)var2 / 100.0F);
                float var14 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var15 = 0.0F;
                float var16 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var17 = 0.0F;

                for(int var18 = 0; var18 < var13; ++var18) {
                    var10 += MathHelper.sin(var14) * MathHelper.cos(var16);
                    var12 += MathHelper.cos(var14) * MathHelper.cos(var16);
                    var11 += MathHelper.sin(var16);
                    var14 += var15 * 0.2F;
                    var15 *= 0.9F;
                    var15 += this.rand.nextFloat() - this.rand.nextFloat();
                    var16 += var17 * 0.5F;
                    var16 *= 0.5F;
                    var17 *= 0.9F;
                    var17 += this.rand.nextFloat() - this.rand.nextFloat();
                    float var19 = MathHelper.sin((float)var18 * (float)Math.PI / (float)var13) * (float)var2 / 100.0F + 1.0F;

                    for(int var20 = (int)(var10 - var19); var20 <= (int)(var10 + var19); ++var20) {
                        for(int var21 = (int)(var11 - var19); var21 <= (int)(var11 + var19); ++var21) {
                            for(int var22 = (int)(var12 - var19); var22 <= (int)(var12 + var19); ++var22) {
                                float var23 = (float)var20 - var10;
                                float var24 = (float)var21 - var11;
                                float var25 = (float)var22 - var12;
                                var23 = var23 * var23 + var24 * var24 * 2.0F + var25 * var25;
                                if(var23 < var19 * var19 && var20 > 0 && var21 > 0 && var22 > 0 && var20 < this.width - 1 && var21 < this.height - 1 && var22 < this.depth - 1) {
                                    int var27 = (var21 * this.depth + var22) * this.width + var20;
                                    if(this.blocksByteArray[var27] == Block.stone.blockID) {
                                        this.blocksByteArray[var27] = var26;
                                    }
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
