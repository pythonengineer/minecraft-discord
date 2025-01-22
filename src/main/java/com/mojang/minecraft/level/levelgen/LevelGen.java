package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.util.ArrayList;

public final class LevelGen {
	private Minecraft minecraft;
	private int width;
	private int height;
	private int depth;
	private EaglercraftRandom random = new EaglercraftRandom();
	private byte[] blocks;
	private int[] coords = new int[1048576];

	public LevelGen(Minecraft minecraft1) {
		this.minecraft = minecraft1;
	}

	public final boolean generateLevel(Level level1, String string2, int i3, int i4, int i5) {
		this.minecraft.beginLevelLoading("Generating level");
		this.width = 256;
		this.height = 256;
		this.depth = 64;
		this.blocks = new byte[256 << 8 << 6];
		this.minecraft.levelLoadUpdate("Raising..");
		LevelGen levelGen27 = this;
		Distort distort8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise perlinNoise10 = new PerlinNoise(this.random, 8);
		int[] i11 = new int[this.width * this.height];

		int i13;
		for(i5 = 0; i5 < levelGen27.width; ++i5) {
			levelGen27.setNextPhase(i5 * 100 / (levelGen27.width - 1));

			for(i13 = 0; i13 < levelGen27.height; ++i13) {
				double d14 = distort8.getValue((double)i5, (double)i13) / 8.0D - 8.0D;
				double d16 = distort9.getValue((double)i5, (double)i13) / 8.0D + 8.0D;
				if(perlinNoise10.getValue((double)i5, (double)i13) / 8.0D > 2.0D) {
					d16 = d14;
				}

				double d20 = ((d20 = Math.max(d14, d16)) * d20 * d20 / 100.0D + d20 * 3.0D) / 8.0D;
				i11[i5 + i13 * levelGen27.width] = (int)d20;
			}
		}

		this.minecraft.levelLoadUpdate("Eroding..");
		int[] i28 = i11;
		levelGen27 = this;
		distort9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort32 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int i15;
		int i34;
		int i41;
		for(i34 = 0; i34 < levelGen27.width; ++i34) {
			levelGen27.setNextPhase(i34 * 100 / (levelGen27.width - 1));

			for(i5 = 0; i5 < levelGen27.height; ++i5) {
				double d36 = distort9.getValue((double)(i34 << 1), (double)(i5 << 1)) / 8.0D;
				i15 = distort32.getValue((double)(i34 << 1), (double)(i5 << 1)) > 0.0D ? 1 : 0;
				if(d36 > 2.0D) {
					i41 = ((i28[i34 + i5 * levelGen27.width] - i15) / 2 << 1) + i15;
					i28[i34 + i5 * levelGen27.width] = i41;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Soiling..");
		i28 = i11;
		levelGen27 = this;
		int i31 = this.width;
		int i33 = this.height;
		i34 = this.depth;

		int i17;
		int i18;
		int i37;
		for(i5 = 0; i5 < i31; ++i5) {
			levelGen27.setNextPhase(i5 * 100 / (levelGen27.width - 1));

			for(i13 = 0; i13 < i34; ++i13) {
				for(i37 = 0; i37 < i33; ++i37) {
					i15 = (i13 * levelGen27.height + i37) * levelGen27.width + i5;
					i17 = (i41 = i28[i5 + i37 * i31] + i34 / 2) - 2;
					i18 = 0;
					if(i13 == i41 && i13 >= i34 / 2 - 1) {
						i18 = Tile.grass.id;
					} else if(i13 <= i41) {
						i18 = Tile.dirt.id;
					}

					if(i13 <= i17) {
						i18 = Tile.rock.id;
					}

					levelGen27.blocks[i15] = (byte)i18;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Carving..");
		levelGen27 = this;
		int i29 = this.width;
		i31 = this.height;
		i33 = this.depth;
		i34 = i29 * i31 * i33 / 256 / 64;

		for(i5 = 0; i5 < i34; ++i5) {
			levelGen27.setNextPhase(i5 * 100 / (i34 - 1));
			float f38 = levelGen27.random.nextFloat() * (float)i29;
			float f39 = levelGen27.random.nextFloat() * (float)i33;
			float f40 = levelGen27.random.nextFloat() * (float)i31;
			i41 = (int)(levelGen27.random.nextFloat() + levelGen27.random.nextFloat() * 150.0F);
			float f43 = (float)((double)levelGen27.random.nextFloat() * Math.PI * 2.0D);
			float f44 = 0.0F;
			float f19 = (float)((double)levelGen27.random.nextFloat() * Math.PI * 2.0D);
			float f45 = 0.0F;

			for(int i21 = 0; i21 < i41; ++i21) {
				f38 = (float)((double)f38 + Math.sin((double)f43) * Math.cos((double)f19));
				f40 = (float)((double)f40 + Math.cos((double)f43) * Math.cos((double)f19));
				f39 = (float)((double)f39 + Math.sin((double)f19));
				f43 += f44 * 0.2F;
				f44 = (f44 *= 0.9F) + (levelGen27.random.nextFloat() - levelGen27.random.nextFloat());
				f19 = (f19 + f45 * 0.5F) * 0.5F;
				f45 = (f45 *= 0.9F) + (levelGen27.random.nextFloat() - levelGen27.random.nextFloat());
				float f26 = (float)(Math.sin((double)i21 * Math.PI / (double)i41) * 2.5D + 1.0D);

				for(int i6 = (int)(f38 - f26); i6 <= (int)(f38 + f26); ++i6) {
					for(int i7 = (int)(f39 - f26); i7 <= (int)(f39 + f26); ++i7) {
						for(int i12 = (int)(f40 - f26); i12 <= (int)(f40 + f26); ++i12) {
							float f22 = (float)i6 - f38;
							float f23 = (float)i7 - f39;
							float f24 = (float)i12 - f40;
							if(f22 * f22 + f23 * f23 * 2.0F + f24 * f24 < f26 * f26 && i6 >= 1 && i7 >= 1 && i12 >= 1 && i6 < levelGen27.width - 1 && i7 < levelGen27.depth - 1 && i12 < levelGen27.height - 1) {
								int i46 = (i7 * levelGen27.height + i12) * levelGen27.width + i6;
								if(levelGen27.blocks[i46] == Tile.rock.id) {
									levelGen27.blocks[i46] = 0;
								}
							}
						}
					}
				}
			}
		}

		this.minecraft.levelLoadUpdate("Watering..");
		levelGen27 = this;
		long j30 = EagRuntime.nanoTime();
		long j35 = 0L;
		i13 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(i37 = 0; i37 < levelGen27.width; ++i37) {
			j35 = j35 + levelGen27.floodFillLiquid(i37, levelGen27.depth / 2 - 1, 0, 0, i13) + levelGen27.floodFillLiquid(i37, levelGen27.depth / 2 - 1, levelGen27.height - 1, 0, i13);
		}

		for(i37 = 0; i37 < levelGen27.height; ++i37) {
			j35 = j35 + levelGen27.floodFillLiquid(0, levelGen27.depth / 2 - 1, i37, 0, i13) + levelGen27.floodFillLiquid(levelGen27.width - 1, levelGen27.depth / 2 - 1, i37, 0, i13);
		}

		i37 = levelGen27.width * levelGen27.height / 200;

		for(i15 = 0; i15 < i37; ++i15) {
			if(i15 % 100 == 0) {
				levelGen27.setNextPhase(i15 * 100 / (i37 - 1));
			}

			i41 = levelGen27.random.nextInt(levelGen27.width);
			i17 = levelGen27.depth / 2 - 1 - levelGen27.random.nextInt(3);
			i18 = levelGen27.random.nextInt(levelGen27.height);
			if(levelGen27.blocks[(i17 * levelGen27.height + i18) * levelGen27.width + i41] == 0) {
				j35 += levelGen27.floodFillLiquid(i41, i17, i18, 0, i13);
			}
		}

		levelGen27.setNextPhase(100);
		long j42 = EagRuntime.nanoTime();
		System.out.println("Flood filled " + j35 + " tiles in " + (double)(j42 - j30) / 1000000.0D + " ms");
		this.minecraft.levelLoadUpdate("Melting..");
		this.addLava();
		level1.setData(256, 64, 256, this.blocks);
		level1.createTime = EagRuntime.currentTimeMillis();
		level1.creator = string2;
		level1.name = "A Nice World";
		return true;
	}

	private void setNextPhase(int i1) {
		this.minecraft.setLoadingProgress(i1);
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
