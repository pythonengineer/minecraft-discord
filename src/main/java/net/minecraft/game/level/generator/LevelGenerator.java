package net.minecraft.game.level.generator;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.level.block.Block;

public final class LevelGenerator {
	public LoadingScreenRenderer progressBar;
	public int width;
	public int depth;
	public int height;
	public EaglercraftRandom rand = new EaglercraftRandom();
	public byte[] blocksByteArray;
	public int waterLevel;
	private int[] floodFillBlocks = new int[1048576];

	public LevelGenerator(LoadingScreenRenderer var1) {
		this.progressBar = var1;
	}

	public void populateOre(int var1, int var2, int var3, int var4) {
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
				var14 = var14 * 0.9F + (this.rand.nextFloat() - this.rand.nextFloat());
				var15 = (var15 + var16 * 0.5F) * 0.5F;
				var16 = var16 * 0.9F + (this.rand.nextFloat() - this.rand.nextFloat());
				float var18 = MathHelper.sin((float)var17 * (float)Math.PI / (float)var12) * (float)var2 / 100.0F + 1.0F;

				for(int var19 = (int)(var9 - var18); var19 <= (int)(var9 + var18); ++var19) {
					for(int var20 = (int)(var10 - var18); var20 <= (int)(var10 + var18); ++var20) {
						for(int var21 = (int)(var11 - var18); var21 <= (int)(var11 + var18); ++var21) {
							float var22 = (float)var19 - var9;
							float var23 = (float)var20 - var10;
							float var24 = (float)var21 - var11;
							if(var22 * var22 + var23 * var23 * 2.0F + var24 * var24 < var18 * var18 && var19 >= 1 && var20 >= 1 && var21 >= 1 && var19 < this.width - 1 && var20 < this.height - 1 && var21 < this.depth - 1) {
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

	public void setNextPhase(int var1) {
		this.progressBar.setLoadingProgress(var1);
	}

	public long floodFill(int var1, int var2, int var3, int var4, int var5) {
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

			for(var13 += (long)(var15 - var12); var12 < var15; ++var12) {
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
