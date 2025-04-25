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
	private IProgressUpdate loadingScreen;
	private int width;
	private int depth;
	private int height;
	private EaglercraftRandom rand = new EaglercraftRandom();
	private byte[] blocks;
	private int waterLevel;
	private int[] coords = new int[1048576];

	public LevelGenerator(IProgressUpdate var1) {
		this.loadingScreen = var1;
	}

	public final World generate(String var1, int var2, int var3, int var4) {
		this.loadingScreen.displayProgressMessage("Generating level");
		this.width = var2;
		this.depth = var3;
		this.height = 64;
		this.waterLevel = 32;
		this.blocks = new byte[var2 * var3 << 6];
		this.loadingScreen.displayLoadingString("Raising..");
		LevelGenerator var5 = this;
		NoiseGeneratorDistort var6 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
		NoiseGeneratorDistort var7 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
		NoiseGeneratorOctaves var8 = new NoiseGeneratorOctaves(this.rand, 6);
		int[] var9 = new int[this.width * this.depth];

		int var11;
		int var14;
		for(var14 = 0; var14 < var5.width; ++var14) {
			var5.setNextPhase(var14 * 100 / (var5.width - 1));

			for(var11 = 0; var11 < var5.depth; ++var11) {
				double var16 = var6.generateNoise((double)((float)var14 * 1.3F), (double)((float)var11 * 1.3F)) / 6.0D + -4.0D;
				double var18 = var7.generateNoise((double)((float)var14 * 1.3F), (double)((float)var11 * 1.3F)) / 5.0D + 10.0D + -4.0D;
				double var20 = var8.generateNoise((double)var14, (double)var11) / 8.0D;
				if(var20 > 0.0D) {
					var18 = var16;
				}

				double var22 = Math.max(var16, var18) / 2.0D;
				if(var22 < 0.0D) {
					var22 *= 0.8D;
				}

				var9[var14 + var11 * var5.width] = (int)var22;
			}
		}

		this.loadingScreen.displayLoadingString("Eroding..");
		int[] var32 = var9;
		var5 = this;
		var7 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
		NoiseGeneratorDistort var37 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));

		int var10;
		int var41;
		int var47;
		for(var41 = 0; var41 < var5.width; ++var41) {
			var5.setNextPhase(var41 * 100 / (var5.width - 1));

			for(var10 = 0; var10 < var5.depth; ++var10) {
				double var13 = var7.generateNoise((double)(var41 << 1), (double)(var10 << 1)) / 8.0D;
				var11 = var37.generateNoise((double)(var41 << 1), (double)(var10 << 1)) > 0.0D ? 1 : 0;
				if(var13 > 2.0D) {
					var47 = var32[var41 + var10 * var5.width];
					var47 = ((var47 - var11) / 2 << 1) + var11;
					var32[var41 + var10 * var5.width] = var47;
				}
			}
		}

		this.loadingScreen.displayLoadingString("Soiling..");
		var32 = var9;
		var5 = this;
		int var35 = this.width;
		int var39 = this.depth;
		var41 = this.height;
		NoiseGeneratorOctaves var43 = new NoiseGeneratorOctaves(this.rand, 8);

		int var17;
		int var19;
		int var45;
		int var52;
		int var54;
		for(var45 = 0; var45 < var35; ++var45) {
			var5.setNextPhase(var45 * 100 / (var5.width - 1));

			for(var14 = 0; var14 < var39; ++var14) {
				var11 = (int)(var43.generateNoise((double)var45, (double)var14) / 24.0D) - 4;
				var47 = var32[var45 + var14 * var35] + var5.waterLevel;
				var17 = var47 + var11;
				var32[var45 + var14 * var35] = Math.max(var47, var17);
				if(var32[var45 + var14 * var35] > var41 - 2) {
					var32[var45 + var14 * var35] = var41 - 2;
				}

				if(var32[var45 + var14 * var35] <= 0) {
					var32[var45 + var14 * var35] = 1;
				}

				for(var52 = 0; var52 < var41; ++var52) {
					var19 = (var52 * var5.depth + var14) * var5.width + var45;
					var54 = 0;
					if(var52 <= var47) {
						var54 = Block.dirt.blockID;
					}

					if(var52 <= var17) {
						var54 = Block.stone.blockID;
					}

					if(var52 == 0) {
						var54 = Block.lavaMoving.blockID;
					}

					var5.blocks[var19] = (byte)var54;
				}
			}
		}

		this.loadingScreen.displayLoadingString("Carving..");
		boolean var36 = true;
		boolean var33 = false;
		var5 = this;
		var39 = this.width;
		var41 = this.depth;
		var10 = this.height;
		var45 = var39 * var41 * var10 / 256 / 64 << 1;

		for(var14 = 0; var14 < var45; ++var14) {
			var5.setNextPhase(var14 * 100 / (var45 - 1) / 4);
			float var44 = var5.rand.nextFloat() * (float)var39;
			float var48 = var5.rand.nextFloat() * (float)var10;
			float var49 = var5.rand.nextFloat() * (float)var41;
			var52 = (int)((var5.rand.nextFloat() + var5.rand.nextFloat()) * 200.0F);
			float var53 = var5.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var55 = 0.0F;
			float var21 = var5.rand.nextFloat() * (float)Math.PI * 2.0F;
			float var57 = 0.0F;
			float var23 = var5.rand.nextFloat() * var5.rand.nextFloat();

			for(var4 = 0; var4 < var52; ++var4) {
				var44 += MathHelper.sin(var53) * MathHelper.cos(var21);
				var49 += MathHelper.cos(var53) * MathHelper.cos(var21);
				var48 += MathHelper.sin(var21);
				var53 += var55 * 0.2F;
				var55 *= 0.9F;
				var55 += var5.rand.nextFloat() - var5.rand.nextFloat();
				var21 += var57 * 0.5F;
				var21 *= 0.5F;
				var57 *= 12.0F / 16.0F;
				var57 += var5.rand.nextFloat() - var5.rand.nextFloat();
				if(var5.rand.nextFloat() >= 0.25F) {
					float var34 = var44 + (var5.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var38 = var48 + (var5.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var12 = var49 + (var5.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var15 = ((float)var5.height - var38) / (float)var5.height;
					var15 = 1.2F + (var15 * 3.5F + 1.0F) * var23;
					var15 = MathHelper.sin((float)var4 * (float)Math.PI / (float)var52) * var15;

					for(int var24 = (int)(var34 - var15); var24 <= (int)(var34 + var15); ++var24) {
						for(int var25 = (int)(var38 - var15); var25 <= (int)(var38 + var15); ++var25) {
							for(int var26 = (int)(var12 - var15); var26 <= (int)(var12 + var15); ++var26) {
								float var27 = (float)var24 - var34;
								float var28 = (float)var25 - var38;
								float var29 = (float)var26 - var12;
								var27 = var27 * var27 + var28 * var28 * 2.0F + var29 * var29;
								if(var27 < var15 * var15 && var24 > 0 && var25 > 0 && var26 > 0 && var24 < var5.width - 1 && var25 < var5.height - 1 && var26 < var5.depth - 1) {
									int var62 = (var25 * var5.depth + var26) * var5.width + var24;
									if(var5.blocks[var62] == Block.stone.blockID) {
										var5.blocks[var62] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.populateOres(Block.oreCoal.blockID, 90, 1, 4);
		this.populateOres(Block.oreIron.blockID, 70, 2, 4);
		this.populateOres(Block.oreGold.blockID, 50, 3, 4);
		this.loadingScreen.displayLoadingString("Watering..");
		var5 = this;
		var41 = Block.waterStill.blockID;
		this.setNextPhase(0);

		for(var10 = 0; var10 < var5.width; ++var10) {
			var5.floodFill(var10, var5.height / 2 - 1, 0, 0, var41);
			var5.floodFill(var10, var5.height / 2 - 1, var5.depth - 1, 0, var41);
		}

		for(var10 = 0; var10 < var5.depth; ++var10) {
			var5.floodFill(0, var5.height / 2 - 1, var10, 0, var41);
			var5.floodFill(var5.width - 1, var5.height / 2 - 1, var10, 0, var41);
		}

		var10 = var5.width * var5.depth / 8000;

		for(var45 = 0; var45 < var10; ++var45) {
			if(var45 % 100 == 0) {
				var5.setNextPhase(var45 * 100 / (var10 - 1));
			}

			var14 = var5.rand.nextInt(var5.width);
			var11 = var5.waterLevel - 1 - var5.rand.nextInt(2);
			var47 = var5.rand.nextInt(var5.depth);
			if(var5.blocks[(var11 * var5.depth + var47) * var5.width + var14] == 0) {
				var5.floodFill(var14, var11, var47, 0, var41);
			}
		}

		var5.setNextPhase(100);
		this.loadingScreen.displayLoadingString("Melting..");
		var5 = this;
		var35 = this.width * this.depth * this.height / 20000;

		for(var39 = 0; var39 < var35; ++var39) {
			if(var39 % 100 == 0) {
				var5.setNextPhase(var39 * 100 / (var35 - 1));
			}

			var41 = var5.rand.nextInt(var5.width);
			var10 = (int)(var5.rand.nextFloat() * var5.rand.nextFloat() * (float)(var5.waterLevel - 3));
			var45 = var5.rand.nextInt(var5.depth);
			if(var5.blocks[(var10 * var5.depth + var45) * var5.width + var41] == 0) {
				var5.floodFill(var41, var10, var45, 0, Block.lavaStill.blockID);
			}
		}

		var5.setNextPhase(100);
		this.loadingScreen.displayLoadingString("Growing..");
		var32 = var9;
		var5 = this;
		var35 = this.width;
		var39 = this.depth;
		var41 = this.height;
		var43 = new NoiseGeneratorOctaves(this.rand, 8);
		NoiseGeneratorOctaves var46 = new NoiseGeneratorOctaves(this.rand, 8);

		int var56;
		for(var14 = 0; var14 < var35; ++var14) {
			var5.setNextPhase(var14 * 100 / (var5.width - 1));

			for(var11 = 0; var11 < var39; ++var11) {
				boolean var51 = var43.generateNoise((double)var14, (double)var11) > 8.0D;
				boolean var50 = var46.generateNoise((double)var14, (double)var11) > 12.0D;
				var52 = var32[var14 + var11 * var35];
				var19 = (var52 * var5.depth + var11) * var5.width + var14;
				var54 = var5.blocks[((var52 + 1) * var5.depth + var11) * var5.width + var14] & 255;
				if((var54 == Block.waterMoving.blockID || var54 == Block.waterStill.blockID) && var52 <= var41 / 2 - 1 && var50) {
					var5.blocks[var19] = (byte)Block.gravel.blockID;
				}

				if(var54 == 0) {
					var56 = Block.grass.blockID;
					if(var52 <= var41 / 2 - 1 && var51) {
						var56 = Block.sand.blockID;
					}

					var5.blocks[var19] = (byte)var56;
				}
			}
		}

		this.loadingScreen.displayLoadingString("Planting..");
		var32 = var9;
		var5 = this;
		var35 = this.width;
		var39 = this.width * this.depth / 3000;

		for(var41 = 0; var41 < var39; ++var41) {
			var10 = var5.rand.nextInt(2);
			var5.setNextPhase(var41 * 50 / (var39 - 1));
			var45 = var5.rand.nextInt(var5.width);
			var14 = var5.rand.nextInt(var5.depth);

			for(var11 = 0; var11 < 10; ++var11) {
				var47 = var45;
				var17 = var14;

				for(var52 = 0; var52 < 5; ++var52) {
					var47 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					var17 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					if((var10 < 2 || var5.rand.nextInt(4) == 0) && var47 >= 0 && var17 >= 0 && var47 < var5.width && var17 < var5.depth) {
						var19 = var32[var47 + var17 * var35] + 1;
						boolean var58 = (var5.blocks[(var19 * var5.depth + var17) * var5.width + var47] & 255) == 0;
						if(var58) {
							var56 = (var19 * var5.depth + var17) * var5.width + var47;
							int var60 = var5.blocks[((var19 - 1) * var5.depth + var17) * var5.width + var47] & 255;
							if(var60 == Block.grass.blockID) {
								if(var10 == 0) {
									var5.blocks[var56] = (byte)Block.plantYellow.blockID;
								} else if(var10 == 1) {
									var5.blocks[var56] = (byte)Block.plantRed.blockID;
								}
							}
						}
					}
				}
			}
		}

		var32 = var9;
		var5 = this;
		var35 = this.width;
		var41 = this.width * this.depth * this.height / 2000;

		for(var10 = 0; var10 < var41; ++var10) {
			var45 = var5.rand.nextInt(2);
			var5.setNextPhase(var10 * 50 / (var41 - 1) + 50);
			var14 = var5.rand.nextInt(var5.width);
			var11 = var5.rand.nextInt(var5.height);
			var47 = var5.rand.nextInt(var5.depth);

			for(var17 = 0; var17 < 20; ++var17) {
				var52 = var14;
				var19 = var11;
				var54 = var47;

				for(var56 = 0; var56 < 5; ++var56) {
					var52 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					var19 += var5.rand.nextInt(2) - var5.rand.nextInt(2);
					var54 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					if((var45 < 2 || var5.rand.nextInt(4) == 0) && var52 >= 0 && var54 >= 0 && var19 > 0 && var52 < var5.width && var54 < var5.depth && var19 < var32[var52 + var54 * var35] - 1) {
						boolean var61 = (var5.blocks[(var19 * var5.depth + var54) * var5.width + var52] & 255) == 0;
						if(var61) {
							int var59 = (var19 * var5.depth + var54) * var5.width + var52;
							var4 = var5.blocks[((var19 - 1) * var5.depth + var54) * var5.width + var52] & 255;
							if(var4 == Block.stone.blockID) {
								if(var45 == 0) {
									var5.blocks[var59] = (byte)Block.mushroomBrown.blockID;
								} else if(var45 == 1) {
									var5.blocks[var59] = (byte)Block.mushroomRed.blockID;
								}
							}
						}
					}
				}
			}
		}

		World var31 = new World();
		var31.waterLevel = this.waterLevel;
		var31.setLevel(var2, 64, var3, this.blocks);
		EagRuntime.currentTimeMillis();
		int[] var42 = var9;
		World var40 = var31;
		var5 = this;
		var39 = this.width;
		var41 = this.width * this.depth / 4000;

		for(var10 = 0; var10 < var41; ++var10) {
			var5.setNextPhase(var10 * 50 / (var41 - 1) + 50);
			var45 = var5.rand.nextInt(var5.width);
			var14 = var5.rand.nextInt(var5.depth);

			for(var11 = 0; var11 < 20; ++var11) {
				var47 = var45;
				var17 = var14;

				for(var52 = 0; var52 < 20; ++var52) {
					var47 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					var17 += var5.rand.nextInt(6) - var5.rand.nextInt(6);
					if(var47 >= 0 && var17 >= 0 && var47 < var5.width && var17 < var5.depth) {
						var19 = var42[var47 + var17 * var39] + 1;
						if(var5.rand.nextInt(4) == 0) {
							var40.growTrees(var47, var19, var17);
						}
					}
				}
			}
		}

		return var31;
	}

	private void populateOres(int var1, int var2, int var3, int var4) {
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
								if(this.blocks[var26] == Block.stone.blockID) {
									this.blocks[var26] = var25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setNextPhase(int var1) {
		this.loadingScreen.setLoadingProgress(var1);
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
		this.coords[0] = ((var2 << var8) + var3 << var7) + var1;
		long var13 = 0L;
		var1 = this.width * this.depth;

		while(var22 > 0) {
			--var22;
			var2 = this.coords[var22];
			if(var22 == 0 && var21.size() > 0) {
				this.coords = (int[])var21.remove(var21.size() - 1);
				var22 = this.coords.length;
			}

			var3 = var2 >> var7 & var9;
			int var11 = var2 >> var7 + var8;
			int var12 = var2 & var10;

			int var15;
			for(var15 = var12; var12 > 0 && this.blocks[var2 - 1] == 0; --var2) {
				--var12;
			}

			while(var15 < this.width && this.blocks[var2 + var15 - var12] == 0) {
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
				this.blocks[var2] = var20;
				boolean var19;
				if(var3 > 0) {
					var19 = this.blocks[var2 - this.width] == 0;
					if(var19 && !var23) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - this.width;
					}

					var23 = var19;
				}

				if(var3 < this.depth - 1) {
					var19 = this.blocks[var2 + this.width] == 0;
					if(var19 && !var24) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 + this.width;
					}

					var24 = var19;
				}

				if(var11 > 0) {
					byte var25 = this.blocks[var2 - var1];
					if((var20 == Block.lavaMoving.blockID || var20 == Block.lavaStill.blockID) && (var25 == Block.waterMoving.blockID || var25 == Block.waterStill.blockID)) {
						this.blocks[var2 - var1] = (byte)Block.stone.blockID;
					}

					var19 = var25 == 0;
					if(var19 && !var18) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - var1;
					}

					var18 = var19;
				}

				++var2;
			}
		}

		return var13;
	}
}
