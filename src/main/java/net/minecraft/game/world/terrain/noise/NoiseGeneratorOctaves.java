package net.minecraft.game.world.terrain.noise;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class NoiseGeneratorOctaves extends NoiseGenerator {
    private NoiseGeneratorPerlin[] generatorCollection;
    private int octaves;

    public NoiseGeneratorOctaves(EaglercraftRandom rand, int octaves) {
        this.octaves = octaves;
        this.generatorCollection = new NoiseGeneratorPerlin[octaves];

        for(int i3 = 0; i3 < octaves; ++i3) {
            this.generatorCollection[i3] = new NoiseGeneratorPerlin(rand);
        }

    }

    public double generateNoiseOctaves(double x, double y) {
        double d5 = 0.0D;
        double d7 = 1.0D;

        for(int i9 = 0; i9 < this.octaves; ++i9) {
            d5 += this.generatorCollection[i9].generateNoise(x * d7, y * d7) / d7;
            d7 /= 2.0D;
        }

        return d5;
    }

    public double[] generateNoiseOctaves(double[] octavesArray, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double coordScaleX, double coordScaleY, double coordScaleZ) {
        if(octavesArray == null) {
            octavesArray = new double[sizeX * sizeY * sizeZ];
        } else {
            for(int i14 = 0; i14 < octavesArray.length; ++i14) {
                octavesArray[i14] = 0.0D;
            }
        }

        double d17 = 1.0D;

        for(int i16 = 0; i16 < this.octaves; ++i16) {
            this.generatorCollection[i16].populateNoiseArray(octavesArray, x, y, z, sizeX, sizeY, sizeZ, coordScaleX * d17, coordScaleY * d17, coordScaleZ * d17, d17);
            d17 /= 2.0D;
        }

        return octavesArray;
    }
}
