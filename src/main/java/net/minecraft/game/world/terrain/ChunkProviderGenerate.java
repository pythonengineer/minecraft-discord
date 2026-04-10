package net.minecraft.game.world.terrain;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.IChunkProvider;
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
	}

	public final Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		byte[] b3 = new byte[32768];
		Chunk chunk4 = new Chunk(this.worldObj, b3, chunkX, chunkZ);
		int i10003 = chunkX << 2;
		int i10005 = chunkZ << 2;
		boolean z5 = true;
		z5 = true;
		z5 = true;
		int i8 = i10005;
		z5 = false;
		int i7 = i10003;
		double[] d6 = this.noiseArray;
		ChunkProviderGenerate chunkProviderGenerate78 = this;
		if(d6 == null) {
			d6 = new double[425];
		}

		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i7, 0, i8, 5, 1, 5, 1.0D, 0.0D, 1.0D);
		this.noise7 = this.noiseGen7.generateNoiseOctaves(this.noise7, i7, 0, i8, 5, 1, 5, 100.0D, 0.0D, 100.0D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, i7, 0, i8, 5, 17, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, i7, 0, i8, 5, 17, 5, 684.412D, 684.412D, 684.412D);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, i7, 0, i8, 5, 17, 5, 684.412D, 684.412D, 684.412D);
		i7 = 0;
		i8 = 0;

		for(int i9 = 0; i9 < 5; ++i9) {
			for(int i10 = 0; i10 < 5; ++i10) {
				double d61;
				if((d61 = (chunkProviderGenerate78.noise6[i8] + 256.0D) / 512.0D) > 1.0D) {
					d61 = 1.0D;
				}

				double d63;
				if((d63 = chunkProviderGenerate78.noise7[i8] / 8000.0D) < 0.0D) {
					d63 = -d63;
				}

				if((d63 = d63 * 3.0D - 3.0D) < 0.0D) {
					if((d63 /= 2.0D) < -1.0D) {
						d63 = -1.0D;
					}

					d63 /= 1.4D;
					d61 = 0.0D;
				} else {
					if(d63 > 1.0D) {
						d63 = 1.0D;
					}

					d63 /= 6.0D;
				}

				d61 += 0.5D;
				d63 = d63 * 17.0D / 16.0D;
				double d65 = 8.5D + d63 * 4.0D;
				++i8;

				for(int i67 = 0; i67 < 17; ++i67) {
					double d70;
					if((d70 = ((double)i67 - d65) * 12.0D / d61) < 0.0D) {
						d70 *= 4.0D;
					}

					double d72 = chunkProviderGenerate78.noise1[i7] / 512.0D;
					double d74 = chunkProviderGenerate78.noise2[i7] / 512.0D;
					double d68;
					double d76;
					if((d76 = (chunkProviderGenerate78.noise3[i7] / 10.0D + 1.0D) / 2.0D) < 0.0D) {
						d68 = d72;
					} else if(d76 > 1.0D) {
						d68 = d74;
					} else {
						d68 = d72 + (d74 - d72) * d76;
					}

					d68 -= d70;
					d6[i7] = d68;
					++i7;
				}
			}
		}

		this.noiseArray = d6;

		int i79;
		int i80;
		for(i79 = 0; i79 < 4; ++i79) {
			for(i80 = 0; i80 < 4; ++i80) {
				for(i7 = 0; i7 < 16; ++i7) {
					double d82 = this.noiseArray[(i79 * 5 + i80) * 17 + i7];
					double d84 = this.noiseArray[(i79 * 5 + i80 + 1) * 17 + i7];
					double d12 = this.noiseArray[((i79 + 1) * 5 + i80) * 17 + i7];
					double d14 = this.noiseArray[((i79 + 1) * 5 + i80 + 1) * 17 + i7];
					double d16 = this.noiseArray[(i79 * 5 + i80) * 17 + i7 + 1];
					double d18 = this.noiseArray[(i79 * 5 + i80 + 1) * 17 + i7 + 1];
					double d20 = this.noiseArray[((i79 + 1) * 5 + i80) * 17 + i7 + 1];
					double d22 = this.noiseArray[((i79 + 1) * 5 + i80 + 1) * 17 + i7 + 1];

					for(int i24 = 0; i24 < 8; ++i24) {
						double d25 = (double)i24 / 8.0D;
						double d27 = d82 + (d16 - d82) * d25;
						double d29 = d84 + (d18 - d84) * d25;
						double d31 = d12 + (d20 - d12) * d25;
						double d33 = d14 + (d22 - d14) * d25;

						for(int i89 = 0; i89 < 4; ++i89) {
							double d36 = (double)i89 / 4.0D;
							double d38 = d27 + (d31 - d27) * d36;
							double d40 = d29 + (d33 - d29) * d36;
							int i26 = i89 + (i79 << 2) << 11 | 0 + (i80 << 2) << 7 | (i7 << 3) + i24;

							for(int i35 = 0; i35 < 4; ++i35) {
								double d44 = (double)i35 / 4.0D;
								double d46 = d38 + (d40 - d38) * d44;
								int i90 = 0;
								if((i7 << 3) + i24 < 64) {
									i90 = Block.waterStill.blockID;
								}

								if(d46 > 0.0D) {
									i90 = Block.stone.blockID;
								}

								b3[i26] = (byte)i90;
								i26 += 128;
							}
						}
					}
				}
			}
		}

		for(i79 = 0; i79 < 16; ++i79) {
			for(i80 = 0; i80 < 16; ++i80) {
				double d81 = (double)((chunkX << 4) + i79);
				double d83 = (double)((chunkZ << 4) + i80);
				boolean z13 = this.noiseGen4.generateNoiseOctaves(d81 * 8.0D / 256D, d83 * 8.0D / 256D, 0.0D) + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean z85 = this.noiseGen4.generateNoiseOctaves(d83 * 8.0D / 256D, 109.0134D, d81 * 8.0D / 256D) + this.rand.nextDouble() * 0.2D > 3.0D;
				int i15 = (int)(this.noiseGen5.generateNoiseOctaves(d81 * 8.0D / 256D * 2.0D, d83 * 8.0D / 256D * 2.0D) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i86 = i79 << 11 | i80 << 7 | 127;
				int i17 = -1;
				int i87 = Block.grass.blockID;
				int i19 = Block.dirt.blockID;

				for(int i88 = 127; i88 >= 0; --i88) {
					if(b3[i86] == 0) {
						i17 = -1;
					} else if(b3[i86] == Block.stone.blockID) {
						if(i17 == -1) {
							if(i15 <= 0) {
								i87 = 0;
								i19 = (byte)Block.stone.blockID;
							} else if(i88 >= 60 && i88 <= 65) {
								i87 = Block.grass.blockID;
								i19 = Block.dirt.blockID;
								if(z85) {
									i87 = 0;
								}

								if(z85) {
									i19 = Block.gravel.blockID;
								}

								if(z13) {
									i87 = Block.sand.blockID;
								}

								if(z13) {
									i19 = Block.sand.blockID;
								}
							}

							if(i88 < 64 && i87 == 0) {
								i87 = Block.waterStill.blockID;
							}

							i17 = i15;
							if(i88 >= 63) {
								b3[i86] = (byte)i87;
							} else {
								b3[i86] = (byte)i19;
							}
						} else if(i17 > 0) {
							--i17;
							b3[i86] = (byte)i19;
						}
					}

					--i86;
				}
			}
		}

		chunk4.generateHeightMap();
		return chunk4;
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
			(new WorldGenMinable(Block.oreCoal.blockID)).generate(this.worldObj, this.rand, i4, i5, i6);
		}

		for(chunkZ = 0; chunkZ < 10; ++chunkZ) {
			i4 = i8 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(64);
			i6 = chunkX + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID)).generate(this.worldObj, this.rand, i4, i5, i6);
		}

		if(this.rand.nextInt(2) == 0) {
			chunkZ = i8 + this.rand.nextInt(16);
			i4 = this.rand.nextInt(32);
			i5 = chunkX + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID)).generate(this.worldObj, this.rand, chunkZ, i4, i5);
		}

		if(this.rand.nextInt(8) == 0) {
			chunkZ = i8 + this.rand.nextInt(16);
			i4 = this.rand.nextInt(16);
			i5 = chunkX + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID)).generate(this.worldObj, this.rand, chunkZ, i4, i5);
		}

		if((chunkZ = (int)(this.mobSpawnerNoise.generateNoiseOctaves((double)i8 * 0.5D, (double)chunkX * 0.5D) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D)) < 0) {
			chunkZ = 0;
		}

		WorldGenTrees worldGenTrees9 = new WorldGenTrees();
		if(this.rand.nextInt(10) == 0) {
			++chunkZ;
		}

		for(i5 = 0; i5 < chunkZ; ++i5) {
			i6 = i8 + this.rand.nextInt(16) + 8;
			int i7 = chunkX + this.rand.nextInt(16) + 8;
			worldGenTrees9.generate(this.worldObj, this.rand, i6, this.worldObj.getHeightValue(i6, i7), i7);
		}

	}

	public final void saveChunks(boolean flag) {
	}

	public final boolean unload100OldestChunks() {
		return false;
	}
}