package net.minecraft.game.world.terrain;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
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
		boolean z50 = true;
		boolean z49 = true;
		boolean z48 = true;
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
		int i10 = 0;
		int i56 = 0;

		for(int i57 = 0; i57 < 5; ++i57) {
			for(int i58 = 0; i58 < 5; ++i58) {
				double d59 = (double)(i7 + i57);
				double d61 = (double)(i9 + i58);
				Math.sqrt(d59 * d59 + d61 * d61);
				double d65;
				if((d65 = (chunkProviderGenerate5.noise6[i56] + 256.0D) / 512.0D) > 1.0D) {
					d65 = 1.0D;
				}

				double d69;
				if((d69 = chunkProviderGenerate5.noise7[i56] / 8000.0D) < 0.0D) {
					d69 = -d69;
				}

				if((d69 = d69 * 3.0D - 3.0D) < 0.0D) {
					if((d69 /= 2.0D) < -1.0D) {
						d69 = -1.0D;
					}

					d69 = (d69 /= 1.4D) / 2.0D;
					d65 = 0.0D;
				} else {
					if(d69 > 1.0D) {
						d69 = 1.0D;
					}

					d69 /= 6.0D;
				}

				d65 += 0.5D;
				d69 = d69 * 17.0D / 16.0D;
				double d71 = 8.5D + d69 * 4.0D;
				++i56;

				for(int i73 = 0; i73 < 17; ++i73) {
					double d76;
					if((d76 = ((double)i73 - d71) * 12.0D / d65) < 0.0D) {
						d76 *= 4.0D;
					}

					double d78 = chunkProviderGenerate5.noise1[i10] / 512.0D;
					double d80 = chunkProviderGenerate5.noise2[i10] / 512.0D;
					double d74;
					double d82;
					if((d82 = (chunkProviderGenerate5.noise3[i10] / 10.0D + 1.0D) / 2.0D) < 0.0D) {
						d74 = d78;
					} else if(d82 > 1.0D) {
						d74 = d80;
					} else {
						d74 = d78 + (d80 - d78) * d82;
					}

					d74 -= d76;
					double d84;
					if(i73 > 13) {
						d84 = (double)((float)(i73 - 13) / 3.0F);
						d74 = d74 * (1.0D - d84) + d84 * -10.0D;
					}

					if((double)i73 < 0.0D) {
						if((d84 = (0.0D - (double)i73) / 4.0D) < 0.0D) {
							d84 = 0.0D;
						}

						if(d84 > 1.0D) {
							d84 = 1.0D;
						}

						d74 = d74 * (1.0D - d84) + d84 * -10.0D;
					}

					d6[i10] = d74;
					++i10;
				}
			}
		}

		this.noiseArray = d6;

		int i87;
		int i88;
		for(i87 = 0; i87 < 4; ++i87) {
			for(i88 = 0; i88 < 4; ++i88) {
				for(i7 = 0; i7 < 16; ++i7) {
					double d93 = this.noiseArray[(i87 * 5 + i88) * 17 + i7];
					double d12 = this.noiseArray[(i87 * 5 + i88 + 1) * 17 + i7];
					double d14 = this.noiseArray[((i87 + 1) * 5 + i88) * 17 + i7];
					double d16 = this.noiseArray[((i87 + 1) * 5 + i88 + 1) * 17 + i7];
					double d18 = (this.noiseArray[(i87 * 5 + i88) * 17 + i7 + 1] - d93) * 0.125D;
					double d20 = (this.noiseArray[(i87 * 5 + i88 + 1) * 17 + i7 + 1] - d12) * 0.125D;
					double d22 = (this.noiseArray[((i87 + 1) * 5 + i88) * 17 + i7 + 1] - d14) * 0.125D;
					double d24 = (this.noiseArray[((i87 + 1) * 5 + i88 + 1) * 17 + i7 + 1] - d16) * 0.125D;

					for(int i90 = 0; i90 < 8; ++i90) {
						double d27 = d93;
						double d29 = d12;
						double d31 = (d14 - d93) * 0.25D;
						double d33 = (d16 - d12) * 0.25D;

						for(i9 = 0; i9 < 4; ++i9) {
							int i26 = i9 + (i87 << 2) << 11 | 0 + (i88 << 2) << 7 | (i7 << 3) + i90;
							double d37 = d27;
							double d39 = (d29 - d27) * 0.25D;

							for(int i35 = 0; i35 < 4; ++i35) {
								int i36 = 0;
								if((i7 << 3) + i90 < 64) {
									i36 = Block.waterStill.blockID;
								}

								if(d37 > 0.0D) {
									i36 = Block.stone.blockID;
								}

								b3[i26] = (byte)i36;
								i26 += 128;
								d37 += d39;
							}

							d27 += d31;
							d29 += d33;
						}

						d93 += d18;
						d12 += d20;
						d14 += d22;
						d16 += d24;
					}
				}
			}
		}

		for(i87 = 0; i87 < 16; ++i87) {
			for(i88 = 0; i88 < 16; ++i88) {
				double d89 = (double)((chunkX << 4) + i87);
				double d92 = (double)((chunkZ << 4) + i88);
				boolean z13 = this.noiseGen4.generateNoiseOctaves(d89 * 8.0D / 256D, d92 * 8.0D / 256D, 0.0D) + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean z94 = this.noiseGen4.generateNoiseOctaves(d92 * 8.0D / 256D, 109.0134D, d89 * 8.0D / 256D) + this.rand.nextDouble() * 0.2D > 3.0D;
				int i15 = (int)(this.noiseGen5.generateNoiseOctaves(d89 * 8.0D / 256D * 2.0D, d92 * 8.0D / 256D * 2.0D) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i95 = -1;
				int i17 = Block.grass.blockID;
				int i96 = Block.dirt.blockID;
				int i19 = 0;

				int i97;
				for(i97 = i87 << 11 | i88 << 7; i19 < 128 && (b3[i97] == 0 || b3[i97] == Block.waterStill.blockID); ++i97) {
					b3[i97] = 0;
					++i19;
				}

				i97 = i87 << 11 | i88 << 7 | 127;

				for(int i21 = 127; i21 >= i19; --i21) {
					if(i21 <= i19 + this.rand.nextInt(6) - 1) {
						b3[i97] = (byte)Block.bedrock.blockID;
					} else if(b3[i97] == 0) {
						i95 = -1;
					} else if(b3[i97] == Block.stone.blockID) {
						if(i95 == -1) {
							if(i15 <= 0) {
								i17 = 0;
								i96 = (byte)Block.stone.blockID;
							} else if(i21 >= 60 && i21 <= 65) {
								i17 = Block.grass.blockID;
								i96 = Block.dirt.blockID;
								if(z94) {
									i17 = 0;
								}

								if(z94) {
									i96 = Block.gravel.blockID;
								}

								if(z13) {
									i17 = Block.sand.blockID;
								}

								if(z13) {
									i96 = Block.sand.blockID;
								}
							}

							if(i21 < 64 && i17 == 0) {
								i17 = Block.waterStill.blockID;
							}

							i95 = i15;
							if(i21 >= 63) {
								b3[i97] = (byte)i17;
							} else {
								b3[i97] = (byte)i96;
							}
						} else if(i95 > 0) {
							--i95;
							b3[i97] = (byte)i96;
						}
					}

					--i97;
				}
			}
		}

		byte[] b91 = b3;
		i7 = chunkZ;
		i88 = chunkX;
		chunkProviderGenerate5 = this;
		this.rand.setSeed(this.worldObj.seed);
		long j98 = (this.rand.nextLong() / 2L << 1) + 1L;
		long j99 = (this.rand.nextLong() / 2L << 1) + 1L;

		for(chunkX -= 8; chunkX <= i88 + 8; ++chunkX) {
			for(chunkZ = i7 - 8; chunkZ <= i7 + 8; ++chunkZ) {
				chunkProviderGenerate5.rand.setSeed((long)chunkX * j98 + (long)chunkZ * j99 ^ chunkProviderGenerate5.worldObj.seed);
				int i86 = chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(40) + 1) + 1);
				if(chunkProviderGenerate5.rand.nextInt(15) != 0) {
					i86 = 0;
				}

				for(i10 = 0; i10 < i86; ++i10) {
					double d100 = (double)((chunkX << 4) + chunkProviderGenerate5.rand.nextInt(16));
					double d101 = (double)chunkProviderGenerate5.rand.nextInt(chunkProviderGenerate5.rand.nextInt(120) + 8);
					double d60 = (double)((chunkZ << 4) + chunkProviderGenerate5.rand.nextInt(16));
					int i62 = 1;
					if(chunkProviderGenerate5.rand.nextInt(4) == 0) {
						chunkProviderGenerate5.generateCaves(i88, i7, b91, d100, d101, d60, 1.0F + chunkProviderGenerate5.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
						i62 = 1 + chunkProviderGenerate5.rand.nextInt(4);
					}

					for(i9 = 0; i9 < i62; ++i9) {
						float f64 = chunkProviderGenerate5.rand.nextFloat() * (float)Math.PI * 2.0F;
						float f102 = (chunkProviderGenerate5.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
						float f66 = chunkProviderGenerate5.rand.nextFloat() * 2.0F + chunkProviderGenerate5.rand.nextFloat();
						chunkProviderGenerate5.generateCaves(i88, i7, b91, d100, d101, d60, f66, f64, f102, 0, 0, 1.0D);
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
		int i10 = chunkX << 4;
		int i4 = chunkZ << 4;
		this.rand.setSeed(this.worldObj.seed);
		long j6 = (this.rand.nextLong() / 2L << 1) + 1L;
		long j8 = (this.rand.nextLong() / 2L << 1) + 1L;
		this.rand.setSeed((long)chunkX * j6 + (long)chunkZ * j8 ^ this.worldObj.seed);

		int i5;
		int i12;
		for(chunkX = 0; chunkX < 20; ++chunkX) {
			chunkZ = i10 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(128);
			i12 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, chunkZ, i5, i12);
		}

		for(chunkX = 0; chunkX < 10; ++chunkX) {
			chunkZ = i10 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(128);
			i12 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, chunkZ, i5, i12);
		}

		for(chunkX = 0; chunkX < 20; ++chunkX) {
			chunkZ = i10 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(128);
			i12 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, chunkZ, i5, i12);
		}

		for(chunkX = 0; chunkX < 20; ++chunkX) {
			chunkZ = i10 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(64);
			i12 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, chunkZ, i5, i12);
		}

		if(this.rand.nextInt(1) == 0) {
			chunkX = i10 + this.rand.nextInt(16);
			chunkZ = this.rand.nextInt(32);
			i5 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, chunkX, chunkZ, i5);
		}

		if(this.rand.nextInt(4) == 0) {
			chunkX = i10 + this.rand.nextInt(16);
			chunkZ = this.rand.nextInt(16);
			i5 = i4 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID, 8)).generate(this.worldObj, this.rand, chunkX, chunkZ, i5);
		}

		if((chunkX = (int)(this.mobSpawnerNoise.generateNoiseOctaves((double)i10 * 0.5D, (double)i4 * 0.5D) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D)) < 0) {
			chunkX = 0;
		}

		WorldGenTrees worldGenTrees11 = new WorldGenTrees();
		if(this.rand.nextInt(10) == 0) {
			++chunkX;
		}

		int i7;
		for(i5 = 0; i5 < chunkX; ++i5) {
			i12 = i10 + this.rand.nextInt(16) + 8;
			i7 = i4 + this.rand.nextInt(16) + 8;
			worldGenTrees11.generate(this.worldObj, this.rand, i12, this.worldObj.getHeightValue(i12, i7), i7);
		}

		for(i5 = 0; i5 < 2; ++i5) {
			i12 = i10 + this.rand.nextInt(16) + 8;
			i7 = this.rand.nextInt(128);
			chunkX = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i12, i7, chunkX);
		}

		if(this.rand.nextInt(2) == 0) {
			i5 = i10 + this.rand.nextInt(16) + 8;
			i12 = this.rand.nextInt(128);
			i7 = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i5, i12, i7);
		}

		if(this.rand.nextInt(4) == 0) {
			i5 = i10 + this.rand.nextInt(16) + 8;
			i12 = this.rand.nextInt(128);
			i7 = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i5, i12, i7);
		}

		if(this.rand.nextInt(8) == 0) {
			i5 = i10 + this.rand.nextInt(16) + 8;
			i12 = this.rand.nextInt(128);
			i7 = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i5, i12, i7);
		}

		for(i5 = 0; i5 < 50; ++i5) {
			i12 = i10 + this.rand.nextInt(16) + 8;
			i7 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			chunkX = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i12, i7, chunkX);
		}

		for(i5 = 0; i5 < 20; ++i5) {
			i12 = i10 + this.rand.nextInt(16) + 8;
			i7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
			chunkX = i4 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i12, i7, chunkX);
		}

	}

	public final boolean saveChunks(boolean flag, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public final boolean unload100OldestChunks() {
		return false;
	}

	public final boolean canSave() {
		return true;
	}
}
