package net.minecraft.game.world;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public final class ChunkProviderGenerate implements IChunkProvider {
	private EaglercraftRandom rand = new EaglercraftRandom();
	private World worldObj;
	private NoiseGeneratorOctaves noiseGen1 = new NoiseGeneratorOctaves(16);
	private NoiseGeneratorOctaves noiseGen2 = new NoiseGeneratorOctaves(16);
	private NoiseGeneratorOctaves noiseGen3 = new NoiseGeneratorOctaves(8);
	private NoiseGeneratorOctaves noiseGen4 = new NoiseGeneratorOctaves(4);
	private NoiseGeneratorOctaves noiseGen5 = new NoiseGeneratorOctaves(4);
	private NoiseGeneratorOctaves noiseGen6 = new NoiseGeneratorOctaves(5);

	public ChunkProviderGenerate(World var1) {
		this.worldObj = var1;
	}

	public final Chunk provideChunk(int var1, int var2) {
		byte[] var3 = new byte[-Short.MIN_VALUE];
		Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);
		var1 <<= 4;
		var2 <<= 4;
		int var5 = 0;

		for(int var6 = var1; var6 < var1 + 16; ++var6) {
			for(int var7 = var2; var7 < var2 + 16; ++var7) {
				int var8 = var6 / 1024;
				int var9 = var7 / 1024;
				float var10 = (float)(this.noiseGen1.generateNoise((double)((float)var6 / 0.03125F), 0.0D, (double)((float)var7 / 0.03125F)) - this.noiseGen2.generateNoise((double)((float)var6 / 0.015625F), 0.0D, (double)((float)var7 / 0.015625F))) / 512.0F / 4.0F;
				float var11 = (float)this.noiseGen5.generateNoise((double)((float)var6 / 4.0F), (double)((float)var7 / 4.0F));
				float var12 = (float)this.noiseGen6.generateNoise((double)((float)var6 / 8.0F), (double)((float)var7 / 8.0F)) / 8.0F;
				var11 = var11 > 0.0F ? (float)(this.noiseGen3.generateNoise((double)((float)var6 * 0.25714284F * 2.0F), (double)((float)var7 * 0.25714284F * 2.0F)) * (double)var12 / 4.0D) : (float)(this.noiseGen4.generateNoise((double)((float)var6 * 0.25714284F), (double)((float)var7 * 0.25714284F)) * (double)var12);
				int var15 = (int)(var10 + 64.0F + var11);
				if((float)this.noiseGen5.generateNoise((double)var6, (double)var7) < 0.0F) {
					var15 = var15 / 2 << 1;
					if((float)this.noiseGen5.generateNoise((double)(var6 / 5), (double)(var7 / 5)) < 0.0F) {
						++var15;
					}
				}

				for(int var16 = 0; var16 < 128; ++var16) {
					int var17 = 0;
					if(var16 == var15 + 1 && var15 >= 64 && Math.random() < 0.02D) {
						var17 = Block.plantYellow.blockID;
					} else if(var16 == var15 && var15 >= 64) {
						var17 = Block.grass.blockID;
					} else if(var16 <= var15 - 2) {
						var17 = Block.stone.blockID;
					} else if(var16 <= var15) {
						var17 = Block.dirt.blockID;
					} else if(var16 <= 64) {
						var17 = Block.waterStill.blockID;
					}

					this.rand.setSeed((long)(var8 + var9 * 13871));
					int var13 = (var8 << 10) + 128 + this.rand.nextInt(512);
					int var14 = (var9 << 10) + 128 + this.rand.nextInt(512);
					var13 = var6 - var13;
					var14 = var7 - var14;
					if(var13 < 0) {
						var13 = -var13;
					}

					if(var14 < 0) {
						var14 = -var14;
					}

					if(var14 > var13) {
						var13 = var14;
					}

					var13 = 127 - var13;
					if(var13 == 255) {
						var13 = 1;
					}

					if(var13 < var15) {
						var13 = var15;
					}

					if(var16 <= var13 && (var17 == 0 || var17 == Block.waterStill.blockID)) {
						var17 = Block.brick.blockID;
					}

					if(var17 < 0) {
						var17 = 0;
					}

					var3[var5++] = (byte)var17;
				}
			}
		}

		var4.generateHeightMap();
		return var4;
	}

	public final boolean chunkExists(int var1, int var2) {
		return true;
	}
}
