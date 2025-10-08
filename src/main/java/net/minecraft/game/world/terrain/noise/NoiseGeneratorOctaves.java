package net.minecraft.game.world.terrain.noise;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class NoiseGeneratorOctaves extends NoiseGenerator {
	private NoiseGeneratorPerlin[] generatorCollection;
	private int octaves;

    public NoiseGeneratorOctaves(int var1) {
        this(new EaglercraftRandom(), var1);
    }

    private NoiseGeneratorOctaves(EaglercraftRandom var1, int var2) {
		this.octaves = var2;
		this.generatorCollection = new NoiseGeneratorPerlin[var2];

		for(int var3 = 0; var3 < var2; ++var3) {
			this.generatorCollection[var3] = new NoiseGeneratorPerlin(var1);
		}

	}

	public final double generateNoise(double var1, double var3) {
		double var5 = 0.0D;
		double var7 = 1.0D;

		for(int var9 = 0; var9 < this.octaves; ++var9) {
			var5 += this.generatorCollection[var9].generateNoise(var1 / var7, var3 / var7) * var7;
			var7 *= 2.0D;
		}

		return var5;
	}

    public final double generateNoise(double var1, double var3, double var5) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.generatorCollection[var11].generateNoiseD(var1 / var9, 0.0D / var9, var5 / var9) * var9;
            var9 *= 2.0D;
        }

        return var7;
    }
}
