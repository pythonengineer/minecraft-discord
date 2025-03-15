package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Creeper;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.Pig;
import com.mojang.minecraft.mob.Skeleton;
import com.mojang.minecraft.mob.Zombie;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.util.ArrayList;

public final class LevelGen {
	private LevelLoaderListener levelLoaderListener;
	private int width;
	private int height;
	private int depth;
	private EaglercraftRandom random = new EaglercraftRandom();
	private byte[] blocks;
	private int waterLevel;
	private int[] coords = new int[1048576];

	public LevelGen(LevelLoaderListener levelLoaderListener) {
		this.levelLoaderListener = levelLoaderListener;
	}

	public final Level generateLevel(String creator, int width, int height, int depth) {
		this.levelLoaderListener.beginLevelLoading("Generating level");
		this.width = width;
		this.height = height;
		this.depth = 64;
		this.waterLevel = 32;
		this.blocks = new byte[width * height << 6];
		this.levelLoaderListener.levelLoadUpdate("Raising..");
		LevelGen levelGen5 = this;
		Distort distort8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise perlinNoise10 = new PerlinNoise(this.random, 6);
		int[] i11 = new int[this.width * this.height];
		float f6 = 1.3F;

		int i14;
		int i15;
		for(i14 = 0; i14 < levelGen5.width; ++i14) {
			levelGen5.setNextPhase(i14 * 100 / (levelGen5.width - 1));

			for(i15 = 0; i15 < levelGen5.height; ++i15) {
				double d16 = distort8.getValue((double)((float)i14 * f6), (double)((float)i15 * f6)) / 6.0D + (double)-4;
				double d18 = distort9.getValue((double)((float)i14 * f6), (double)((float)i15 * f6)) / 5.0D + 10.0D + (double)-4;
				if(perlinNoise10.getValue((double)i14, (double)i15) / 8.0D > 0.0D) {
					d18 = d16;
				}

				double d22;
				if((d22 = Math.max(d16, d18) / 2.0D) < 0.0D) {
					d22 *= 0.8D;
				}

				i11[i14 + i15 * levelGen5.width] = (int)d22;
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Eroding..");
		int[] i35 = i11;
		levelGen5 = this;
		distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort41 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int i32;
		int i44;
		int i47;
		for(i44 = 0; i44 < levelGen5.width; ++i44) {
			levelGen5.setNextPhase(i44 * 100 / (levelGen5.width - 1));

			for(i32 = 0; i32 < levelGen5.height; ++i32) {
				double d13 = distort9.getValue((double)(i44 << 1), (double)(i32 << 1)) / 8.0D;
				i15 = distort41.getValue((double)(i44 << 1), (double)(i32 << 1)) > 0.0D ? 1 : 0;
				if(d13 > 2.0D) {
					i47 = ((i35[i44 + i32 * levelGen5.width] - i15) / 2 << 1) + i15;
					i35[i44 + i32 * levelGen5.width] = i47;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Soiling..");
		i35 = i11;
		levelGen5 = this;
		int i38 = this.width;
		int i43 = this.height;
		i44 = this.depth;
		PerlinNoise perlinNoise34 = new PerlinNoise(this.random, 8);

		int i17;
		int i46;
		int i52;
		for(i46 = 0; i46 < i38; ++i46) {
			levelGen5.setNextPhase(i46 * 100 / (levelGen5.width - 1));

			for(i14 = 0; i14 < i43; ++i14) {
				i15 = (int)(perlinNoise34.getValue((double)i46, (double)i14) / 24.0D) - 4;
				i17 = (i47 = i35[i46 + i14 * i38] + levelGen5.waterLevel) + i15;
				i35[i46 + i14 * i38] = Math.max(i47, i17);
				if(i35[i46 + i14 * i38] > i44 - 2) {
					i35[i46 + i14 * i38] = i44 - 2;
				}

				if(i35[i46 + i14 * i38] < 1) {
					i35[i46 + i14 * i38] = 1;
				}

				for(i52 = 0; i52 < i44; ++i52) {
					int i19 = (i52 * levelGen5.height + i14) * levelGen5.width + i46;
					int i20 = 0;
					if(i52 <= i47) {
						i20 = Tile.dirt.id;
					}

					if(i52 <= i17) {
						i20 = Tile.rock.id;
					}

					if(i52 == 0) {
						i20 = Tile.lava.id;
					}

					levelGen5.blocks[i19] = (byte)i20;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Carving..");
		boolean z40 = true;
		boolean z36 = false;
		levelGen5 = this;
		i43 = this.width;
		i44 = this.height;
		i32 = this.depth;
		i46 = i43 * i44 * i32 / 256 / 64 << 1;

		for(i14 = 0; i14 < i46; ++i14) {
			levelGen5.setNextPhase(i14 * 100 / (i46 - 1) / 4);
			float f48 = levelGen5.random.nextFloat() * (float)i43;
			float f49 = levelGen5.random.nextFloat() * (float)i32;
			float f50 = levelGen5.random.nextFloat() * (float)i44;
			i52 = (int)((levelGen5.random.nextFloat() + levelGen5.random.nextFloat()) * 200.0F);
			float f53 = (float)((double)levelGen5.random.nextFloat() * Math.PI * 2.0D);
			float f54 = 0.0F;
			float f21 = (float)((double)levelGen5.random.nextFloat() * Math.PI * 2.0D);
			float f55 = 0.0F;
			float f23 = levelGen5.random.nextFloat() * levelGen5.random.nextFloat();

			for(int i7 = 0; i7 < i52; ++i7) {
				f48 = (float)((double)f48 + Math.sin((double)f53) * Math.cos((double)f21));
				f50 = (float)((double)f50 + Math.cos((double)f53) * Math.cos((double)f21));
				f49 = (float)((double)f49 + Math.sin((double)f21));
				f53 += f54 * 0.2F;
				f54 = (f54 *= 0.9F) + (levelGen5.random.nextFloat() - levelGen5.random.nextFloat());
				f21 = (f21 + f55 * 0.5F) * 0.5F;
				f55 = (f55 *= 0.75F) + (levelGen5.random.nextFloat() - levelGen5.random.nextFloat());
				if(levelGen5.random.nextFloat() >= 0.25F) {
					float f37 = f48 + (levelGen5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f42 = f49 + (levelGen5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f12 = f50 + (levelGen5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f24 = ((float)levelGen5.depth - f42) / (float)levelGen5.depth;
					f24 = 1.2F + (f24 * 3.5F + 1.0F) * f23;
					f24 = (float)(Math.sin((double)i7 * Math.PI / (double)i52) * (double)f24);

					for(int i25 = (int)(f37 - f24); i25 <= (int)(f37 + f24); ++i25) {
						for(int i26 = (int)(f42 - f24); i26 <= (int)(f42 + f24); ++i26) {
							for(int i27 = (int)(f12 - f24); i27 <= (int)(f12 + f24); ++i27) {
								float f28 = (float)i25 - f37;
								float f29 = (float)i26 - f42;
								float f30 = (float)i27 - f12;
								if(f28 * f28 + f29 * f29 * 2.0F + f30 * f30 < f24 * f24 && i25 >= 1 && i26 >= 1 && i27 >= 1 && i25 < levelGen5.width - 1 && i26 < levelGen5.depth - 1 && i27 < levelGen5.height - 1) {
									int i56 = (i26 * levelGen5.height + i27) * levelGen5.width + i25;
									if(levelGen5.blocks[i56] == Tile.rock.id) {
										levelGen5.blocks[i56] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.addOre(Tile.coalOre.id, 90, 1, 4);
		this.addOre(Tile.ironOre.id, 70, 2, 4);
		this.addOre(Tile.goldOre.id, 50, 3, 4);
		this.levelLoaderListener.levelLoadUpdate("Watering..");
		levelGen5 = this;
		long j39 = EagRuntime.nanoTime();
		long j45 = 0L;
		i46 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(i14 = 0; i14 < levelGen5.width; ++i14) {
			j45 = j45 + levelGen5.floodFillLiquid(i14, levelGen5.depth / 2 - 1, 0, 0, i46) + levelGen5.floodFillLiquid(i14, levelGen5.depth / 2 - 1, levelGen5.height - 1, 0, i46);
		}

		for(i14 = 0; i14 < levelGen5.height; ++i14) {
			j45 = j45 + levelGen5.floodFillLiquid(0, levelGen5.depth / 2 - 1, i14, 0, i46) + levelGen5.floodFillLiquid(levelGen5.width - 1, levelGen5.depth / 2 - 1, i14, 0, i46);
		}

		i14 = levelGen5.width * levelGen5.height / 8000;

		for(i15 = 0; i15 < i14; ++i15) {
			if(i15 % 100 == 0) {
				levelGen5.setNextPhase(i15 * 100 / (i14 - 1));
			}

			i47 = levelGen5.random.nextInt(levelGen5.width);
			i17 = levelGen5.waterLevel - 1 - levelGen5.random.nextInt(2);
			i52 = levelGen5.random.nextInt(levelGen5.height);
			if(levelGen5.blocks[(i17 * levelGen5.height + i52) * levelGen5.width + i47] == 0) {
				j45 += levelGen5.floodFillLiquid(i47, i17, i52, 0, i46);
			}
		}

		levelGen5.setNextPhase(100);
		long j51 = EagRuntime.nanoTime();
		System.out.println("Flood filled " + j45 + " tiles in " + (double)(j51 - j39) / 1000000.0D + " ms");
		this.levelLoaderListener.levelLoadUpdate("Melting..");
		this.addLava();
		this.levelLoaderListener.levelLoadUpdate("Growing..");
		this.addBeaches(i11);
		this.levelLoaderListener.levelLoadUpdate("Planting..");
		this.plantTrees(i11);
		this.addMushrooms(i11);
		Level level33;
		(level33 = new Level()).waterLevel = this.waterLevel;
		level33.setData(width, 64, height, this.blocks);
		level33.createTime = EagRuntime.currentTimeMillis();
		level33.creator = creator;
		level33.name = "A Nice World";
		this.generateTrees(level33, i11);
		this.levelLoaderListener.levelLoadUpdate("Spawning..");
		this.spawnEntities(level33);
		return level33;
	}

	private void addBeaches(int[] blocks) {
		int i2 = this.width;
		int i3 = this.height;
		int i4 = this.depth;
		PerlinNoise perlinNoise5 = new PerlinNoise(this.random, 8);
		PerlinNoise perlinNoise6 = new PerlinNoise(this.random, 8);

		for(int i7 = 0; i7 < i2; ++i7) {
			this.setNextPhase(i7 * 100 / (this.width - 1));

			for(int i8 = 0; i8 < i3; ++i8) {
				boolean z9 = perlinNoise5.getValue((double)i7, (double)i8) > 8.0D;
				boolean z10 = perlinNoise6.getValue((double)i7, (double)i8) > 12.0D;
				int i11;
				int i12 = ((i11 = blocks[i7 + i8 * i2]) * this.height + i8) * this.width + i7;
				int i13;
				if(((i13 = this.blocks[((i11 + 1) * this.height + i8) * this.width + i7] & 255) == Tile.water.id || i13 == Tile.calmWater.id) && i11 <= i4 / 2 - 1 && z10) {
					this.blocks[i12] = (byte)Tile.gravel.id;
				}

				if(i13 == 0) {
					int i14 = Tile.grass.id;
					if(i11 <= i4 / 2 - 1 && z9) {
						i14 = Tile.sand.id;
					}

					this.blocks[i12] = (byte)i14;
				}
			}
		}

	}

	private void generateTrees(Level level, int[] blocks) {
		int i3 = this.width;
		int i4 = this.width * this.height / 4000;

		for(int i5 = 0; i5 < i4; ++i5) {
			this.setNextPhase(i5 * 50 / (i4 - 1) + 50);
			int i6 = this.random.nextInt(this.width);
			int i7 = this.random.nextInt(this.height);

			for(int i8 = 0; i8 < 20; ++i8) {
				int i9 = i6;
				int i10 = i7;

				for(int i11 = 0; i11 < 20; ++i11) {
					i9 += this.random.nextInt(6) - this.random.nextInt(6);
					i10 += this.random.nextInt(6) - this.random.nextInt(6);
					if(i9 >= 0 && i10 >= 0 && i9 < this.width && i10 < this.height) {
						int i12 = blocks[i9 + i10 * i3] + 1;
						if(this.random.nextInt(4) == 0) {
							level.maybeGrowTree(i9, i12, i10);
						}
					}
				}
			}
		}

	}

	private void spawnEntities(Level level) {
		int i2 = this.width * this.height * this.depth / 800;
		int i3 = 0;

		for(int i4 = 0; i4 < i2; ++i4) {
			this.setNextPhase(i4 * 100 / (i2 - 1));
			int i5 = this.random.nextInt(4);
			int i6 = this.random.nextInt(this.width);
			int i7 = (int)(Math.min(this.random.nextFloat(), this.random.nextFloat()) * (float)this.depth);
			int i8 = this.random.nextInt(this.height);
			if(!level.isSolidTile(i6, i7, i8) && level.getLiquid(i6, i7, i8) == Liquid.none && (!level.isLit(i6, i7, i8) || this.random.nextInt(5) == 0)) {
				for(int i9 = 0; i9 < 3; ++i9) {
					int i10 = i6;
					int i11 = i7;
					int i12 = i8;

					for(int i13 = 0; i13 < 3; ++i13) {
						i10 += this.random.nextInt(6) - this.random.nextInt(6);
						i11 += this.random.nextInt(1) - this.random.nextInt(1);
						i12 += this.random.nextInt(6) - this.random.nextInt(6);
						if(i10 >= 0 && i12 >= 1 && i11 >= 0 && i11 < this.depth - 2 && i10 < this.width && i12 < this.height && level.isSolidTile(i10, i11 - 1, i12) && !level.isSolidTile(i10, i11, i12) && !level.isSolidTile(i10, i11 + 1, i12)) {
							float f14 = (float)i10 + 0.5F;
							float f15 = (float)i11 + 1.0F;
							float f16 = (float)i12 + 0.5F;
							Object object17 = null;
							if(i5 == 0) {
								object17 = new Zombie(level, f14, f15, f16);
							}

							if(i5 == 1) {
								object17 = new Skeleton(level, f14, f15, f16);
							}

							if(i5 == 2) {
								object17 = new Pig(level, f14, f15, f16);
							}

							if(i5 == 3) {
								object17 = new Creeper(level, f14, f15, f16);
							}

							if(level.isFree(((Mob)object17).bb)) {
								++i3;
								level.addEntity((Entity)object17);
							}
						}
					}
				}
			}
		}

		System.out.println(i3 + " mobs");
	}

	private void plantTrees(int[] blocks) {
		int i2 = this.width;
		int i3 = this.width * this.height / 3000;

		for(int i4 = 0; i4 < i3; ++i4) {
			int i5 = this.random.nextInt(2);
			this.setNextPhase(i4 * 50 / (i3 - 1));
			int i6 = this.random.nextInt(this.width);
			int i7 = this.random.nextInt(this.height);

			for(int i8 = 0; i8 < 10; ++i8) {
				int i9 = i6;
				int i10 = i7;

				for(int i11 = 0; i11 < 5; ++i11) {
					i9 += this.random.nextInt(6) - this.random.nextInt(6);
					i10 += this.random.nextInt(6) - this.random.nextInt(6);
					if((i5 < 2 || this.random.nextInt(4) == 0) && i9 >= 0 && i10 >= 0 && i9 < this.width && i10 < this.height) {
						int i12 = blocks[i9 + i10 * i2] + 1;
						if((this.blocks[(i12 * this.height + i10) * this.width + i9] & 255) == 0) {
							int i13 = (i12 * this.height + i10) * this.width + i9;
							if((this.blocks[((i12 - 1) * this.height + i10) * this.width + i9] & 255) == Tile.grass.id) {
								if(i5 == 0) {
									this.blocks[i13] = (byte)Tile.flower.id;
								} else if(i5 == 1) {
									this.blocks[i13] = (byte)Tile.rose.id;
								}
							}
						}
					}
				}
			}
		}

	}

	private void addMushrooms(int[] blocks) {
		int i2 = this.width;
		int i3 = 0;
		int i4 = this.width * this.height * this.depth / 2000;

		for(int i5 = 0; i5 < i4; ++i5) {
			int i6 = this.random.nextInt(2);
			this.setNextPhase(i5 * 50 / (i4 - 1) + 50);
			int i7 = this.random.nextInt(this.width);
			int i8 = this.random.nextInt(this.depth);
			int i9 = this.random.nextInt(this.height);

			for(int i10 = 0; i10 < 20; ++i10) {
				int i11 = i7;
				int i12 = i8;
				int i13 = i9;

				for(int i14 = 0; i14 < 5; ++i14) {
					i11 += this.random.nextInt(6) - this.random.nextInt(6);
					i12 += this.random.nextInt(2) - this.random.nextInt(2);
					i13 += this.random.nextInt(6) - this.random.nextInt(6);
					if((i6 < 2 || this.random.nextInt(4) == 0) && i11 >= 0 && i13 >= 0 && i12 >= 1 && i11 < this.width && i13 < this.height && i12 < blocks[i11 + i13 * i2] - 1 && (this.blocks[(i12 * this.height + i13) * this.width + i11] & 255) == 0) {
						int i15 = (i12 * this.height + i13) * this.width + i11;
						if((this.blocks[((i12 - 1) * this.height + i13) * this.width + i11] & 255) == Tile.rock.id) {
							if(i6 == 0) {
								this.blocks[i15] = (byte)Tile.mushroom1.id;
							} else if(i6 == 1) {
								this.blocks[i15] = (byte)Tile.mushroom2.id;
							}

							++i3;
						}
					}
				}
			}
		}

		System.out.println("Added " + i3 + " mushrooms");
	}

	private void addOre(int tile, int rarity, int min, int max) {
		byte b25 = (byte)tile;
		max = this.width;
		int i5 = this.height;
		int i6 = this.depth;
		int i7 = max * i5 * i6 / 256 / 64 * rarity / 100;

		for(int i8 = 0; i8 < i7; ++i8) {
			this.setNextPhase(i8 * 100 / (i7 - 1) / 4 + min * 100 / 4);
			float f9 = this.random.nextFloat() * (float)max;
			float f10 = this.random.nextFloat() * (float)i6;
			float f11 = this.random.nextFloat() * (float)i5;
			int i12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)rarity / 100.0F);
			float f13 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float f14 = 0.0F;
			float f15 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float f16 = 0.0F;

			for(int i17 = 0; i17 < i12; ++i17) {
				f9 = (float)((double)f9 + Math.sin((double)f13) * Math.cos((double)f15));
				f11 = (float)((double)f11 + Math.cos((double)f13) * Math.cos((double)f15));
				f10 = (float)((double)f10 + Math.sin((double)f15));
				f13 += f14 * 0.2F;
				f14 = (f14 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				f15 = (f15 + f16 * 0.5F) * 0.5F;
				f16 = (f16 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				float f18 = (float)(Math.sin((double)i17 * Math.PI / (double)i12) * (double)rarity / 100.0D + 1.0D);

				for(int i19 = (int)(f9 - f18); i19 <= (int)(f9 + f18); ++i19) {
					for(int i20 = (int)(f10 - f18); i20 <= (int)(f10 + f18); ++i20) {
						for(int i21 = (int)(f11 - f18); i21 <= (int)(f11 + f18); ++i21) {
							float f22 = (float)i19 - f9;
							float f23 = (float)i20 - f10;
							float f24 = (float)i21 - f11;
							if(f22 * f22 + f23 * f23 * 2.0F + f24 * f24 < f18 * f18 && i19 >= 1 && i20 >= 1 && i21 >= 1 && i19 < this.width - 1 && i20 < this.depth - 1 && i21 < this.height - 1) {
								int i26 = (i20 * this.height + i21) * this.width + i19;
								if(this.blocks[i26] == Tile.rock.id) {
									this.blocks[i26] = b25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setNextPhase(int phase) {
		this.levelLoaderListener.setLoadingProgress(phase);
	}

	private void addLava() {
		int i1 = 0;
		int i2 = this.width * this.height * this.depth / 20000;

		for(int i3 = 0; i3 < i2; ++i3) {
			if(i3 % 100 == 0) {
				this.setNextPhase(i3 * 100 / (i2 - 1));
			}

			int i4 = this.random.nextInt(this.width);
			int i5 = (int)(this.random.nextFloat() * this.random.nextFloat() * (float)(this.waterLevel - 3));
			int i6 = this.random.nextInt(this.height);
			if(this.blocks[(i5 * this.height + i6) * this.width + i4] == 0) {
				++i1;
				this.floodFillLiquid(i4, i5, i6, 0, Tile.calmLava.id);
			}
		}

		this.setNextPhase(100);
		System.out.println("LavaCount: " + i1);
	}

	private long floodFillLiquid(int x, int y, int z, int source, int tt) {
		byte b20 = (byte)tt;
		ArrayList arrayList21 = new ArrayList();
		byte b6 = 0;
		int i7 = 1;

		int i8;
		for(i8 = 1; 1 << i7 < this.width; ++i7) {
		}

		while(1 << i8 < this.height) {
			++i8;
		}

		int i9 = this.height - 1;
		int i10 = this.width - 1;
		int i22 = b6 + 1;
		this.coords[0] = ((y << i8) + z << i7) + x;
		long j13 = 0L;
		x = this.width * this.height;

		while(i22 > 0) {
			--i22;
			y = this.coords[i22];
			if(i22 == 0 && arrayList21.size() > 0) {
				System.out.println("IT HAPPENED!");
				this.coords = (int[])arrayList21.remove(arrayList21.size() - 1);
				i22 = this.coords.length;
			}

			z = y >> i7 & i9;
			int i11 = y >> i7 + i8;

			int i12;
			int i15;
			for(i15 = i12 = y & i10; i12 > 0 && this.blocks[y - 1] == 0; --y) {
				--i12;
			}

			while(i15 < this.width && this.blocks[y + i15 - i12] == 0) {
				++i15;
			}

			int i16 = y >> i7 & i9;
			int i17 = y >> i7 + i8;
			if(i16 != z || i17 != i11) {
				System.out.println("hoooly fuck");
			}

			boolean z23 = false;
			boolean z24 = false;
			boolean z18 = false;
			j13 += (long)(i15 - i12);

			for(i12 = i12; i12 < i15; ++i12) {
				this.blocks[y] = b20;
				boolean z19;
				if(z > 0) {
					if((z19 = this.blocks[y - this.width] == 0) && !z23) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - this.width;
					}

					z23 = z19;
				}

				if(z < this.height - 1) {
					if((z19 = this.blocks[y + this.width] == 0) && !z24) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y + this.width;
					}

					z24 = z19;
				}

				if(i11 > 0) {
					byte b25 = this.blocks[y - x];
					if((b20 == Tile.lava.id || b20 == Tile.calmLava.id) && (b25 == Tile.water.id || b25 == Tile.calmWater.id)) {
						this.blocks[y - x] = (byte)Tile.rock.id;
					}

					if((z19 = b25 == 0) && !z18) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - x;
					}

					z18 = z19;
				}

				++y;
			}
		}

		return j13;
	}
}