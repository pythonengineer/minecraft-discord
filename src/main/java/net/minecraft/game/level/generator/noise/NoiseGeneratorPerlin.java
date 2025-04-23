package net.minecraft.game.level.generator.noise;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class NoiseGeneratorPerlin extends NoiseGenerator {
	private int[] permutations;

	public NoiseGeneratorPerlin() {
		this(new EaglercraftRandom());
	}

	public NoiseGeneratorPerlin(EaglercraftRandom var1) {
		this.permutations = new int[512];

		int var2;
		for(var2 = 0; var2 < 256; this.permutations[var2] = var2++) {
		}

		for(var2 = 0; var2 < 256; ++var2) {
			int var3 = var1.nextInt(256 - var2) + var2;
			int var4 = this.permutations[var2];
			this.permutations[var2] = this.permutations[var3];
			this.permutations[var3] = var4;
			this.permutations[var2 + 256] = this.permutations[var2];
		}

	}

	private static double generateNoise(double var0) {
		return var0 * var0 * var0 * (var0 * (var0 * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double var0, double var2, double var4) {
		return var2 + var0 * (var4 - var2);
	}

	private static double grad(int var0, double var1, double var3, double var5) {
		var0 &= 15;
		double var8 = var0 < 8 ? var1 : var3;
		double var10 = var0 < 4 ? var3 : (var0 != 12 && var0 != 14 ? var5 : var1);
		return ((var0 & 1) == 0 ? var8 : -var8) + ((var0 & 2) == 0 ? var10 : -var10);
	}

	public final double generateNoise(double var1, double var3) {
		double var10 = 0.0D;
		double var8 = var3;
		double var6 = var1;
		int var18 = (int)Math.floor(var1) & 255;
		int var2 = (int)Math.floor(var3) & 255;
		int var19 = (int)Math.floor(0.0D) & 255;
		var6 -= Math.floor(var6);
		var8 -= Math.floor(var8);
		var10 = 0.0D - Math.floor(0.0D);
		double var12 = generateNoise(var6);
		double var14 = generateNoise(var8);
		double var16 = generateNoise(var10);
		int var4 = this.permutations[var18] + var2;
		int var5 = this.permutations[var4] + var19;
		var4 = this.permutations[var4 + 1] + var19;
		var18 = this.permutations[var18 + 1] + var2;
		var2 = this.permutations[var18] + var19;
		var18 = this.permutations[var18 + 1] + var19;
		return lerp(var16, lerp(var14, lerp(var12, grad(this.permutations[var5], var6, var8, var10), grad(this.permutations[var2], var6 - 1.0D, var8, var10)), lerp(var12, grad(this.permutations[var4], var6, var8 - 1.0D, var10), grad(this.permutations[var18], var6 - 1.0D, var8 - 1.0D, var10))), lerp(var14, lerp(var12, grad(this.permutations[var5 + 1], var6, var8, var10 - 1.0D), grad(this.permutations[var2 + 1], var6 - 1.0D, var8, var10 - 1.0D)), lerp(var12, grad(this.permutations[var4 + 1], var6, var8 - 1.0D, var10 - 1.0D), grad(this.permutations[var18 + 1], var6 - 1.0D, var8 - 1.0D, var10 - 1.0D))));
	}
}
