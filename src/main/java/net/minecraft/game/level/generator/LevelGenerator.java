package net.minecraft.game.level.generator;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.BlockFlower;
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
	private int phaseBar;
	private int phases;
	private float phaseBareLength = 0.0F;
	private int[] floodFillBlocks = new int[1048576];

	public LevelGenerator(IProgressUpdate var1) {
		this.guiLoading = var1;
	}

	public final World generate(String var1, int var2, int var3, int var4) {
		int var5 = 1;
		if(this.floatingGen) {
			var5 = (var4 - 64) / 48 + 1;
		}

		this.phases = 10 + var5 * 5;
		this.guiLoading.displayProgressMessage("Generating level");
		World var6 = new World();
		var6.waterLevel = this.waterLevel;
		var6.groundLevel = this.groundLevel;
		this.width = var2;
		this.depth = var3;
		this.height = var4;
		this.blocksByteArray = new byte[var2 * var3 * var4];

		int var7;
		LevelGenerator var9;
		int var20;
		int var24;
		int var30;
		int var44;
		int var49;
		int var50;
		int var51;
		int var54;
		for(var7 = 0; var7 < var5; ++var7) {
			this.waterLevel = var4 - 32 - var7 * 48;
			this.groundLevel = this.waterLevel - 2;
			int[] var8;
			NoiseGeneratorOctaves var13;
			double var31;
			int[] var45;
			if(this.flatGen) {
				var8 = new int[var2 * var3];

				for(var44 = 0; var44 < var8.length; ++var44) {
					var8[var44] = 0;
				}

				this.loadingBar();
				this.loadingBar();
			} else {
				this.guiLoading.displayLoadingString("Raising..");
				this.loadingBar();
				var9 = this;
				NoiseGeneratorDistort var10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorDistort var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorOctaves var12 = new NoiseGeneratorOctaves(this.rand, 6);
				var13 = new NoiseGeneratorOctaves(this.rand, 2);
				int[] var14 = new int[this.width * this.depth];
				int var21 = 0;

				label300:
				while(true) {
					if(var21 >= var9.width) {
						var8 = var14;
						this.guiLoading.displayLoadingString("Eroding..");
						this.loadingBar();
						var45 = var14;
						var9 = this;
						var11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						NoiseGeneratorDistort var48 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						var50 = 0;

						while(true) {
							if(var50 >= var9.width) {
								break label300;
							}

							var9.setNextPhase((float)var50 * 100.0F / (float)(var9.width - 1));

							for(var51 = 0; var51 < var9.depth; ++var51) {
								double var19 = var11.generateNoise((double)(var50 << 1), (double)(var51 << 1)) / 8.0D;
								var21 = var48.generateNoise((double)(var50 << 1), (double)(var51 << 1)) > 0.0D ? 1 : 0;
								if(var19 > 2.0D) {
									int var56 = var45[var50 + var51 * var9.width];
									var56 = ((var56 - var21) / 2 << 1) + var21;
									var45[var50 + var51 * var9.width] = var56;
								}
							}

							++var50;
						}
					}

					double var22 = Math.abs(((double)var21 / ((double)var9.width - 1.0D) - 0.5D) * 2.0D);
					var9.setNextPhase((float)var21 * 100.0F / (float)(var9.width - 1));

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
			this.loadingBar();
			var45 = var8;
			var9 = this;
			int var47 = this.width;
			var49 = this.depth;
			var50 = this.height;
			NoiseGeneratorOctaves var52 = new NoiseGeneratorOctaves(this.rand, 8);
			NoiseGeneratorOctaves var53 = new NoiseGeneratorOctaves(this.rand, 8);

			int var23;
			for(var20 = 0; var20 < var47; ++var20) {
				double var55 = Math.abs(((double)var20 / ((double)var47 - 1.0D) - 0.5D) * 2.0D);
				var9.setNextPhase((float)var20 * 100.0F / (float)(var47 - 1));

				for(var23 = 0; var23 < var49; ++var23) {
					double var63 = Math.abs(((double)var23 / ((double)var49 - 1.0D) - 0.5D) * 2.0D);
					double var26 = Math.max(var55, var63);
					var26 = var26 * var26 * var26;
					int var28 = (int)(var52.generateNoise((double)var20, (double)var23) / 24.0D) - 4;
					int var70 = var45[var20 + var23 * var47] + var9.waterLevel;
					var30 = var70 + var28;
					var45[var20 + var23 * var47] = Math.max(var70, var30);
					if(var45[var20 + var23 * var47] > var50 - 2) {
						var45[var20 + var23 * var47] = var50 - 2;
					}

					if(var45[var20 + var23 * var47] <= 0) {
						var45[var20 + var23 * var47] = 1;
					}

					var31 = var53.generateNoise((double)var20 * 2.3D, (double)var23 * 2.3D) / 24.0D;
					int var74 = (int)(Math.sqrt(Math.abs(var31)) * Math.signum(var31) * 20.0D) + var9.waterLevel;
					var74 = (int)((double)var74 * (1.0D - var26) + var26 * (double)var9.height);
					if(var74 > var9.waterLevel) {
						var74 = var9.height;
					}

					for(int var34 = 0; var34 < var50; ++var34) {
						int var77 = (var34 * var9.depth + var23) * var9.width + var20;
						int var36 = 0;
						if(var34 <= var70) {
							var36 = Block.dirt.blockID;
						}

						if(var34 <= var30) {
							var36 = Block.stone.blockID;
						}

						if(var9.floatingGen && var34 < var74) {
							var36 = 0;
						}

						if(var9.blocksByteArray[var77] == 0) {
							var9.blocksByteArray[var77] = (byte)var36;
						}
					}
				}
			}

			this.guiLoading.displayLoadingString("Melting..");
			this.loadingBar();
			var9 = this;
			var47 = this.width * this.depth * this.height / 2000;
			var49 = this.groundLevel;

			for(var50 = 0; var50 < var47; ++var50) {
				if(var50 % 100 == 0) {
					var9.setNextPhase((float)var50 * 100.0F / (float)(var47 - 1));
				}

				var51 = var9.rand.nextInt(var9.width);
				var54 = Math.min(Math.min(var9.rand.nextInt(var49), var9.rand.nextInt(var49)), Math.min(var9.rand.nextInt(var49), var9.rand.nextInt(var49)));
				var20 = var9.rand.nextInt(var9.depth);
				if(var9.blocksByteArray[(var54 * var9.depth + var20) * var9.width + var51] == 0) {
					long var57 = var9.floodFill(var51, var54, var20, 0, 255);
					if(var57 > 0L && var57 < 640L) {
						var9.floodFill(var51, var54, var20, 255, Block.lavaStill.blockID);
					} else {
						var9.floodFill(var51, var54, var20, 255, 0);
					}
				}
			}

			var9.setNextPhase(100.0F);
			this.guiLoading.displayLoadingString("Growing..");
			this.loadingBar();
			var45 = var8;
			var9 = this;
			var47 = this.width;
			var49 = this.depth;
			var13 = new NoiseGeneratorOctaves(this.rand, 8);
			var52 = new NoiseGeneratorOctaves(this.rand, 8);

			for(var54 = 0; var54 < var47; ++var54) {
				var9.setNextPhase((float)var54 * 100.0F / (float)(var47 - 1));

				for(var20 = 0; var20 < var49; ++var20) {
					boolean var58 = var13.generateNoise((double)var54, (double)var20) > 8.0D;
					if(var9.islandGen) {
						var58 = var13.generateNoise((double)var54, (double)var20) > -8.0D;
					}

					boolean var59 = var52.generateNoise((double)var54, (double)var20) > 12.0D;
					var23 = var45[var54 + var20 * var47];
					var24 = (var23 * var9.depth + var20) * var9.width + var54;
					int var64 = var9.blocksByteArray[((var23 + 1) * var9.depth + var20) * var9.width + var54] & 255;
					if((var64 == Block.waterMoving.blockID || var64 == Block.waterStill.blockID) && var23 <= var9.waterLevel - 1 && var59) {
						var9.blocksByteArray[var24] = (byte)Block.gravel.blockID;
					}

					if(var64 == 0) {
						int var66 = -1;
						if(var23 <= var9.waterLevel - 1 && var58) {
							var66 = Block.sand.blockID;
						}

						if(var9.blocksByteArray[var24] != 0 && var66 > 0) {
							var9.blocksByteArray[var24] = (byte)var66;
						}
					}
				}
			}
		}

		this.guiLoading.displayLoadingString("Carving..");
		this.loadingBar();
		var9 = this;
		var49 = this.width;
		var50 = this.depth;
		var51 = this.height;
		var54 = var49 * var50 * var51 / 256 / 64 << 1;

		for(var20 = 0; var20 < var54; ++var20) {
			var9.setNextPhase((float)var20 * 100.0F / (float)(var54 - 1));
			float var61 = var9.rand.nextFloat() * (float)var49;
			float var60 = var9.rand.nextFloat() * (float)var51;
			float var62 = var9.rand.nextFloat() * (float)var50;
			var24 = (int)((var9.rand.nextFloat() + var9.rand.nextFloat()) * 200.0F);
			float var65 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var68 = 0.0F;
			float var67 = var9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var69 = 0.0F;
			float var71 = var9.rand.nextFloat() * var9.rand.nextFloat();

			for(var30 = 0; var30 < var24; ++var30) {
				var61 += MathHelper.sin(var65) * MathHelper.cos(var67);
				var62 += MathHelper.cos(var65) * MathHelper.cos(var67);
				var60 += MathHelper.sin(var67);
				var65 += var68 * 0.2F;
				var68 *= 0.9F;
				var68 += var9.rand.nextFloat() - var9.rand.nextFloat();
				var67 += var69 * 0.5F;
				var67 *= 0.5F;
				var69 *= 12.0F / 16.0F;
				var69 += var9.rand.nextFloat() - var9.rand.nextFloat();
				if(var9.rand.nextFloat() >= 0.25F) {
					float var72 = var61 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var32 = var60 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var75 = var62 + (var9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var73 = ((float)var9.height - var32) / (float)var9.height;
					float var78 = 1.2F + (var73 * 3.5F + 1.0F) * var71;
					float var76 = MathHelper.sin((float)var30 * (float)Math.PI / (float)var24) * var78;

					for(var5 = (int)(var72 - var76); var5 <= (int)(var72 + var76); ++var5) {
						for(int var79 = (int)(var32 - var76); var79 <= (int)(var32 + var76); ++var79) {
							for(int var39 = (int)(var75 - var76); var39 <= (int)(var75 + var76); ++var39) {
								float var40 = (float)var5 - var72;
								float var42 = (float)var79 - var32;
								float var46 = (float)var39 - var75;
								var40 = var40 * var40 + var42 * var42 * 2.0F + var46 * var46;
								if(var40 < var76 * var76 && var5 > 0 && var79 > 0 && var39 > 0 && var5 < var9.width - 1 && var79 < var9.height - 1 && var39 < var9.depth - 1) {
									var7 = (var79 * var9.depth + var39) * var9.width + var5;
									if(var9.blocksByteArray[var7] == Block.stone.blockID) {
										var9.blocksByteArray[var7] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		var7 = this.populateOre(Block.oreCoal.blockID, 1000, 10, (var4 << 2) / 5);
		int var43 = this.populateOre(Block.oreIron.blockID, 800, 8, var4 * 3 / 5);
		var44 = this.populateOre(Block.oreGold.blockID, 500, 6, (var4 << 1) / 5);
		var5 = this.populateOre(Block.oreDiamond.blockID, 800, 2, var4 / 5);
		System.out.println("Coal: " + var7 + ", Iron: " + var43 + ", Gold: " + var44 + ", Diamond: " + var5);
		var6.cloudHeight = var4 + 2;
		if(this.floatingGen) {
			this.groundLevel = -128;
			this.waterLevel = this.groundLevel + 1;
			var6.cloudHeight = -16;
		} else if(!this.islandGen) {
			this.groundLevel = this.waterLevel + 1;
			this.waterLevel = this.groundLevel - 16;
		} else {
			this.groundLevel = this.waterLevel - 9;
		}

		if(!this.floatingGen) {
			var5 = Block.waterStill.blockID;
			if(this.levelType == 1) {
				var5 = Block.lavaStill.blockID;
			}

			for(var7 = 0; var7 < var2; ++var7) {
				this.floodFill(var7, this.waterLevel - 1, 0, 0, var5);
				this.floodFill(var7, this.waterLevel - 1, var3 - 1, 0, var5);
			}

			for(var7 = 0; var7 < var3; ++var7) {
				this.floodFill(var2 - 1, this.waterLevel - 1, var7, 0, var5);
				this.floodFill(0, this.waterLevel - 1, var7, 0, var5);
			}
		}

		this.guiLoading.displayLoadingString("Watering..");
		this.loadingBar();
		this.a();
		if(this.levelType == 1) {
			var6.cloudColor = 2164736;
			var6.fogColor = 1049600;
			var6.skyColor = 1049600;
			var6.skyBrightness = 7;
			var6.defaultFluid = Block.lavaMoving.blockID;
			if(this.floatingGen) {
				var6.cloudHeight = var4 + 2;
				this.waterLevel = -16;
			}
		}

		var6.waterLevel = this.waterLevel;
		var6.groundLevel = this.groundLevel;
		this.guiLoading.displayLoadingString("Calculating light..");
		this.loadingBar();
		this.setNextPhase(50.0F);
		var6.generate(var2, var4, var3, this.blocksByteArray);
		this.guiLoading.displayLoadingString("Planting..");
		this.loadingBar();
		if(this.levelType != 1) {
			this.growGrassOnDirt(var6);
		}

		this.loadingBar();
		this.growTrees(var6);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.plantYellow, 100);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.plantRed, 100);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.mushroomBrown, 50);
		this.loadingBar();
		this.populateFlowersAndMushrooms(var6, Block.mushroomRed, 50);
		this.guiLoading.displayLoadingString("Spawning..");
		this.loadingBar();
		MobSpawner var41 = new MobSpawner(var6);

		for(var7 = 0; var7 < 1000; ++var7) {
			this.setNextPhase((float)var7 * 100.0F / 999.0F);
			var41.performSpawning();
		}

		var6.createTime = EagRuntime.currentTimeMillis();
		var6.authorName = var1;
		var6.name = "A Nice World";
		if(this.phaseBar != this.phases) {
			throw new IllegalStateException("Wrong number of phases! Wanted " + this.phaseBar);
		} else {
			return var6;
		}
	}

	private void growGrassOnDirt(World var1) {
		for(int var2 = 0; var2 < this.width; ++var2) {
			this.setNextPhase((float)var2 * 100.0F / (float)(this.width - 1));

			for(int var3 = 0; var3 < this.height; ++var3) {
				for(int var4 = 0; var4 < this.depth; ++var4) {
					if(var1.getBlockId(var2, var3, var4) == Block.dirt.blockID && var1.getBlockLightValue(var2, var3 + 1, var4) >= 4 && !var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
						var1.setBlock(var2, var3, var4, Block.grass.blockID);
					}
				}
			}
		}

	}

	private void growTrees(World var1) {
		int var2 = this.width * this.depth * this.height / 80000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase((float)var3 * 100.0F / (float)(var2 - 1));
			}

			int var4 = this.rand.nextInt(this.width);
			int var5 = this.rand.nextInt(this.height);
			int var6 = this.rand.nextInt(this.depth);

			for(int var7 = 0; var7 < 25; ++var7) {
				int var8 = var4;
				int var9 = var5;
				int var10 = var6;

				for(int var11 = 0; var11 < 20; ++var11) {
					var8 += this.rand.nextInt(12) - this.rand.nextInt(12);
					var9 += this.rand.nextInt(3) - this.rand.nextInt(6);
					var10 += this.rand.nextInt(12) - this.rand.nextInt(12);
					if(var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.width && var9 < this.height && var10 < this.depth) {
						var1.growTrees(var8, var9, var10);
					}
				}
			}
		}

	}

	private void populateFlowersAndMushrooms(World var1, BlockFlower var2, int var3) {
		var3 = this.width * this.depth * this.height * var3 / 1600000;

		for(int var4 = 0; var4 < var3; ++var4) {
			if(var4 % 100 == 0) {
				this.setNextPhase((float)var4 * 100.0F / (float)(var3 - 1));
			}

			int var5 = this.rand.nextInt(this.width);
			int var6 = this.rand.nextInt(this.height);
			int var7 = this.rand.nextInt(this.depth);

			for(int var8 = 0; var8 < 10; ++var8) {
				int var9 = var5;
				int var10 = var6;
				int var11 = var7;

				for(int var12 = 0; var12 < 10; ++var12) {
					var9 += this.rand.nextInt(4) - this.rand.nextInt(4);
					var10 += this.rand.nextInt(2) - this.rand.nextInt(2);
					var11 += this.rand.nextInt(4) - this.rand.nextInt(4);
					if(var9 >= 0 && var11 >= 0 && var10 > 0 && var9 < this.width && var11 < this.depth && var10 < this.height && var1.getBlockId(var9, var10, var11) == 0 && var2.canBlockStay(var1, var9, var10, var11)) {
						var1.setBlockWithNotify(var9, var10, var11, var2.blockID);
					}
				}
			}
		}

	}

	private int populateOre(int var1, int var2, int var3, int var4) {
		int var5 = 0;
		byte var26 = (byte)var1;
		int var6 = this.width;
		int var7 = this.depth;
		int var8 = this.height;
		var2 = var6 * var7 * var8 / 256 / 64 * var2 / 100;

		for(int var9 = 0; var9 < var2; ++var9) {
			this.setNextPhase((float)var9 * 100.0F / (float)(var2 - 1));
			float var10 = this.rand.nextFloat() * (float)var6;
			float var11 = this.rand.nextFloat() * (float)var8;
			float var12 = this.rand.nextFloat() * (float)var7;
			if(var11 <= (float)var4) {
				int var13 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)var3 / 100.0F);
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
					float var19 = MathHelper.sin((float)var18 * (float)Math.PI / (float)var13) * (float)var3 / 100.0F + 1.0F;

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

		int var2 = this.width * this.depth * this.height / 1000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase((float)var3 * 100.0F / (float)(var2 - 1));
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

		this.setNextPhase(100.0F);
	}

	private void loadingBar() {
		++this.phaseBar;
		this.phaseBareLength = 0.0F;
		this.setNextPhase(0.0F);
	}

	private void setNextPhase(float var1) {
		if(var1 < 0.0F) {
			throw new IllegalStateException("Failed to set next phase!");
		} else {
			int var2 = (int)(((float)(this.phaseBar - 1) + var1 / 100.0F) * 100.0F / (float)this.phases);
			this.guiLoading.setLoadingProgress(var2);
		}
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
