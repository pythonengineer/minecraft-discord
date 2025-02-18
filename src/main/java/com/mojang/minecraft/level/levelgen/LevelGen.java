package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.ProgressListener;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.util.ArrayList;

public final class LevelGen {
	private ProgressListener loadingScreen;
	private int width;
	private int height;
	private int depth;
	private EaglercraftRandom random = new EaglercraftRandom();
	private byte[] blocks;
	private int[] coords = new int[1048576];

    public LevelGen(ProgressListener progressListener1) {
        this.loadingScreen = progressListener1;
    }

	public final Level generateLevel(String string1, int i2, int i3, int i4) {
        this.loadingScreen.beginLevelLoading("Generating level");
		this.width = i2;
		this.height = i3;
		this.depth = 64;
		this.blocks = new byte[i2 * i3 << 6];
        this.loadingScreen.levelLoadUpdate("Raising..");
		LevelGen levelGen5 = this;
		Distort distort8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise perlinNoise10 = new PerlinNoise(this.random, 8);
		int[] i11 = new int[this.width * this.height];
		float f6 = 1.3F;

		int i13;
		int i14;
		for(i13 = 0; i13 < levelGen5.width; ++i13) {
			levelGen5.setNextPhase(i13 * 100 / (levelGen5.width - 1));

			for(i14 = 0; i14 < levelGen5.height; ++i14) {
				double d15 = distort8.getValue((double)((float)i13 * f6), (double)((float)i14 * f6)) / 8.0D - 8.0D;
				double d17 = distort9.getValue((double)((float)i13 * f6), (double)((float)i14 * f6)) / 6.0D + 6.0D;
				if(perlinNoise10.getValue((double)i13, (double)i14) / 8.0D > 0.0D) {
					d17 = d15;
				}

				double d21;
				if((d21 = Math.max(d15, d17) / 2.0D) < 0.0D) {
					d21 *= 0.8D;
				}

				i11[i13 + i14 * levelGen5.width] = (int)d21;
			}
		}

        this.loadingScreen.levelLoadUpdate("Eroding..");
		int[] i34 = i11;
		levelGen5 = this;
		distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort40 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int i16;
		int i32;
		int i43;
		int i46;
		for(i43 = 0; i43 < levelGen5.width; ++i43) {
			levelGen5.setNextPhase(i43 * 100 / (levelGen5.width - 1));

			for(i32 = 0; i32 < levelGen5.height; ++i32) {
				double d45 = distort9.getValue((double)(i43 << 1), (double)(i32 << 1)) / 8.0D;
				i46 = distort40.getValue((double)(i43 << 1), (double)(i32 << 1)) > 0.0D ? 1 : 0;
				if(d45 > 2.0D) {
					i16 = ((i34[i43 + i32 * levelGen5.width] - i46) / 2 << 1) + i46;
					i34[i43 + i32 * levelGen5.width] = i16;
				}
			}
		}

        this.loadingScreen.levelLoadUpdate("Soiling..");
		i34 = i11;
		levelGen5 = this;
		int i37 = this.width;
		int i42 = this.height;
		i43 = this.depth;
		PerlinNoise perlinNoise33 = new PerlinNoise(this.random, 8);

		int i18;
		int i50;
		for(i13 = 0; i13 < i37; ++i13) {
			levelGen5.setNextPhase(i13 * 100 / (levelGen5.width - 1));

			for(i14 = 0; i14 < i42; ++i14) {
				i46 = (int)(perlinNoise33.getValue((double)i13, (double)i14) / 24.0D) - 4;
				i50 = (i16 = i34[i13 + i14 * i37] + i43 / 2) + i46;
				i34[i13 + i14 * i37] = Math.max(i16, i50);

				for(i18 = 0; i18 < i43; ++i18) {
					int i19 = (i18 * levelGen5.height + i14) * levelGen5.width + i13;
					int i20 = 0;
					if(i18 <= i16) {
						i20 = Tile.dirt.id;
					}

					if(i18 <= i50) {
						i20 = Tile.rock.id;
					}

					levelGen5.blocks[i19] = (byte)i20;
				}
			}
		}

        this.loadingScreen.levelLoadUpdate("Carving..");
		boolean z39 = true;
		boolean z35 = false;
		levelGen5 = this;
		i42 = this.width;
		i43 = this.height;
		i32 = this.depth;
		i13 = i42 * i43 * i32 / 256 / 64;

		for(i14 = 0; i14 < i13; ++i14) {
			levelGen5.setNextPhase(i14 * 100 / (i13 - 1) / 4);
			float f47 = levelGen5.random.nextFloat() * (float)i42;
			float f48 = levelGen5.random.nextFloat() * (float)i32;
			float f51 = levelGen5.random.nextFloat() * (float)i43;
			i18 = (int)((levelGen5.random.nextFloat() + levelGen5.random.nextFloat()) * 75.0F);
			float f52 = (float)((double)levelGen5.random.nextFloat() * Math.PI * 2.0D);
			float f53 = 0.0F;
			float f54 = (float)((double)levelGen5.random.nextFloat() * Math.PI * 2.0D);
			float f22 = 0.0F;

			for(int i7 = 0; i7 < i18; ++i7) {
				f47 = (float)((double)f47 + Math.sin((double)f52) * Math.cos((double)f54));
				f51 = (float)((double)f51 + Math.cos((double)f52) * Math.cos((double)f54));
				f48 = (float)((double)f48 + Math.sin((double)f54));
				f52 += f53 * 0.2F;
				f53 = (f53 *= 0.9F) + (levelGen5.random.nextFloat() - levelGen5.random.nextFloat());
				f54 = (f54 + f22 * 0.5F) * 0.5F;
				f22 = (f22 *= 0.9F) + (levelGen5.random.nextFloat() - levelGen5.random.nextFloat());
				if(levelGen5.random.nextFloat() >= 0.3F) {
					float f36 = f47 + levelGen5.random.nextFloat() * 4.0F - 2.0F;
					float f41 = f48 + levelGen5.random.nextFloat() * 4.0F - 2.0F;
					float f12 = f51 + levelGen5.random.nextFloat() * 4.0F - 2.0F;
					float f23 = (float)(Math.sin((double)i7 * Math.PI / (double)i18) * 2.5D + 1.0D);

					for(int i24 = (int)(f36 - f23); i24 <= (int)(f36 + f23); ++i24) {
						for(int i25 = (int)(f41 - f23); i25 <= (int)(f41 + f23); ++i25) {
							for(int i26 = (int)(f12 - f23); i26 <= (int)(f12 + f23); ++i26) {
								float f27 = (float)i24 - f36;
								float f28 = (float)i25 - f41;
								float f29 = (float)i26 - f12;
								if(f27 * f27 + f28 * f28 * 2.0F + f29 * f29 < f23 * f23 && i24 >= 1 && i25 >= 1 && i26 >= 1 && i24 < levelGen5.width - 1 && i25 < levelGen5.depth - 1 && i26 < levelGen5.height - 1) {
									int i55 = (i25 * levelGen5.height + i26) * levelGen5.width + i24;
									if(levelGen5.blocks[i55] == Tile.rock.id) {
										levelGen5.blocks[i55] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.carveTunnels(Tile.oreCoal.id, 90, 1, 4);
		this.carveTunnels(Tile.oreIron.id, 70, 2, 4);
		this.carveTunnels(Tile.oreGold.id, 50, 3, 4);
        this.loadingScreen.levelLoadUpdate("Watering..");
		levelGen5 = this;
		long j38 = EagRuntime.nanoTime();
		long j44 = 0L;
		i13 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(i14 = 0; i14 < levelGen5.width; ++i14) {
			j44 = j44 + levelGen5.floodFillLiquid(i14, levelGen5.depth / 2 - 1, 0, 0, i13) + levelGen5.floodFillLiquid(i14, levelGen5.depth / 2 - 1, levelGen5.height - 1, 0, i13);
		}

		for(i14 = 0; i14 < levelGen5.height; ++i14) {
			j44 = j44 + levelGen5.floodFillLiquid(0, levelGen5.depth / 2 - 1, i14, 0, i13) + levelGen5.floodFillLiquid(levelGen5.width - 1, levelGen5.depth / 2 - 1, i14, 0, i13);
		}

		i14 = levelGen5.width * levelGen5.height / 200;

		for(i46 = 0; i46 < i14; ++i46) {
			if(i46 % 100 == 0) {
				levelGen5.setNextPhase(i46 * 100 / (i14 - 1));
			}

			i16 = levelGen5.random.nextInt(levelGen5.width);
			i50 = levelGen5.depth / 2 - 1 - levelGen5.random.nextInt(3);
			i18 = levelGen5.random.nextInt(levelGen5.height);
			if(levelGen5.blocks[(i50 * levelGen5.height + i18) * levelGen5.width + i16] == 0) {
				j44 += levelGen5.floodFillLiquid(i16, i50, i18, 0, i13);
			}
		}

		levelGen5.setNextPhase(100);
		long j49 = EagRuntime.nanoTime();
		System.out.println("Flood filled " + j44 + " tiles in " + (double)(j49 - j38) / 1000000.0D + " ms");
        this.loadingScreen.levelLoadUpdate("Melting..");
        this.addLava();
        this.loadingScreen.levelLoadUpdate("Growing..");
        this.addBeaches(i11);
        this.loadingScreen.levelLoadUpdate("Planting..");
		this.plantTrees(i11);
		Level level31;
		(level31 = new Level()).setData(i2, 64, i3, this.blocks);
		level31.createTime = EagRuntime.currentTimeMillis();
		level31.creator = string1;
		level31.name = "A Nice World";
		return level31;
	}

	private void addBeaches(int[] i1) {
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
				int i12 = ((i11 = i1[i7 + i8 * i2]) * this.height + i8) * this.width + i7;
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

	private void plantTrees(int[] i1) {
		int i2 = this.width;
		int i3 = this.width * this.height / 4000;

		for(int i4 = 0; i4 < i3; ++i4) {
			this.setNextPhase(i4 * 100 / (i3 - 1));
			int i5 = this.random.nextInt(this.width);
			int i6 = this.random.nextInt(this.height);

			for(int i7 = 0; i7 < 20; ++i7) {
				int i8 = i5;
				int i9 = i6;

				for(int i10 = 0; i10 < 20; ++i10) {
					i8 += this.random.nextInt(6) - this.random.nextInt(6);
					i9 += this.random.nextInt(6) - this.random.nextInt(6);
					if(i8 >= 0 && i9 >= 0 && i8 < this.width && i9 < this.height) {
						int i11 = i1[i8 + i9 * i2] + 1;
						int i12 = this.random.nextInt(3) + 4;
						boolean z13 = true;

						int i14;
						int i16;
						int i17;
						for(i14 = i11; i14 <= i11 + 1 + i12; ++i14) {
							byte b15 = 1;
							if(i14 >= i11 + 1 + i12 - 2) {
								b15 = 2;
							}

							for(i16 = i8 - b15; i16 <= i8 + b15 && z13; ++i16) {
								for(i17 = i9 - b15; i17 <= i9 + b15 && z13; ++i17) {
									if(i16 >= 0 && i14 >= 0 && i17 >= 0 && i16 < this.width && i14 < this.depth && i17 < this.height) {
										if((this.blocks[(i14 * this.height + i17) * this.width + i16] & 255) != 0) {
											z13 = false;
										}
									} else {
										z13 = false;
									}
								}
							}
						}

						if(z13) {
							i14 = (i11 * this.height + i9) * this.width + i8;
							if((this.blocks[((i11 - 1) * this.height + i9) * this.width + i8] & 255) == Tile.grass.id && i11 < this.depth - i12 - 1) {
								this.blocks[i14 - 1 * this.width * this.height] = (byte)Tile.dirt.id;

								for(i16 = i11 - 3 + i12; i16 <= i11 + i12; ++i16) {
									i17 = i16 - (i11 + i12);
									int i18 = 1 - i17 / 2;

									for(int i21 = i8 - i18; i21 <= i8 + i18; ++i21) {
										int i22 = i21 - i8;

										for(int i19 = i9 - i18; i19 <= i9 + i18; ++i19) {
											int i20 = i19 - i9;
											if(Math.abs(i22) != i18 || Math.abs(i20) != i18 || this.random.nextInt(2) != 0 && i17 != 0) {
												this.blocks[(i16 * this.height + i19) * this.width + i21] = (byte)Tile.leaf.id;
											}
										}
									}
								}

								for(i16 = 0; i16 < i12; ++i16) {
									this.blocks[i14 + i16 * this.width * this.height] = (byte)Tile.log.id;
								}
							}
						}
					}
				}
			}
		}

	}

	private void carveTunnels(int i1, int i2, int i3, int i4) {
		byte b25 = (byte)i1;
		i4 = this.width;
		int i5 = this.height;
		int i6 = this.depth;
		int i7 = i4 * i5 * i6 / 256 / 64 * i2 / 100;

		for(int i8 = 0; i8 < i7; ++i8) {
			this.setNextPhase(i8 * 100 / (i7 - 1) / 4 + i3 * 100 / 4);
			float f9 = this.random.nextFloat() * (float)i4;
			float f10 = this.random.nextFloat() * (float)i6;
			float f11 = this.random.nextFloat() * (float)i5;
			int i12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)i2 / 100.0F);
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
				float f18 = (float)(Math.sin((double)i17 * Math.PI / (double)i12) * (double)i2 / 100.0D + 1.0D);

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

	private void setNextPhase(int i1) {
        this.loadingScreen.setLoadingProgress(i1);
	}

	private void addLava() {
		int i1 = 0;
		int i2 = this.width * this.height * this.depth / 10000;

		for(int i3 = 0; i3 < i2; ++i3) {
			if(i3 % 100 == 0) {
				this.setNextPhase(i3 * 100 / (i2 - 1));
			}

			int i4 = this.random.nextInt(this.width);
			int i5 = this.random.nextInt(this.depth / 2 - 4);
			int i6 = this.random.nextInt(this.height);
			if(this.blocks[(i5 * this.height + i6) * this.width + i4] == 0) {
				++i1;
				this.floodFillLiquid(i4, i5, i6, 0, Tile.calmLava.id);
			}
		}

		this.setNextPhase(100);
		System.out.println("LavaCount: " + i1);
	}

	private long floodFillLiquid(int i1, int i2, int i3, int i4, int i5) {
		byte b20 = (byte)i5;
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
		this.coords[0] = ((i2 << i8) + i3 << i7) + i1;
		long j13 = 0L;
		i1 = this.width * this.height;

		while(i22 > 0) {
			--i22;
			i2 = this.coords[i22];
			if(i22 == 0 && arrayList21.size() > 0) {
				System.out.println("IT HAPPENED!");
				this.coords = (int[])arrayList21.remove(arrayList21.size() - 1);
				i22 = this.coords.length;
			}

			i3 = i2 >> i7 & i9;
			int i11 = i2 >> i7 + i8;

			int i12;
			int i15;
			for(i15 = i12 = i2 & i10; i12 > 0 && this.blocks[i2 - 1] == 0; --i2) {
				--i12;
			}

			while(i15 < this.width && this.blocks[i2 + i15 - i12] == 0) {
				++i15;
			}

			int i16 = i2 >> i7 & i9;
			int i17 = i2 >> i7 + i8;
			if(i16 != i3 || i17 != i11) {
				System.out.println("hoooly fuck");
			}

			boolean z23 = false;
			boolean z24 = false;
			boolean z18 = false;
			j13 += (long)(i15 - i12);

			for(i12 = i12; i12 < i15; ++i12) {
				this.blocks[i2] = b20;
				boolean z19;
				if(i3 > 0) {
					if((z19 = this.blocks[i2 - this.width] == 0) && !z23) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = i2 - this.width;
					}

					z23 = z19;
				}

				if(i3 < this.height - 1) {
					if((z19 = this.blocks[i2 + this.width] == 0) && !z24) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = i2 + this.width;
					}

					z24 = z19;
				}

				if(i11 > 0) {
					byte b25 = this.blocks[i2 - i1];
					if((b20 == Tile.lava.id || b20 == Tile.calmLava.id) && (b25 == Tile.water.id || b25 == Tile.calmWater.id)) {
						this.blocks[i2 - i1] = (byte)Tile.rock.id;
					}

					if((z19 = b25 == 0) && !z18) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = i2 - i1;
					}

					z18 = z19;
				}

				++i2;
			}
		}

		return j13;
	}
}
