package net.minecraft.game.world.terrain.noise;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class NoiseGeneratorPerlin extends NoiseGenerator {
    private int[] permutations;
    private double xCoord;
    private double yCoord;
    private double zCoord;

    public NoiseGeneratorPerlin() {
        this(new EaglercraftRandom());
    }

    public NoiseGeneratorPerlin(EaglercraftRandom rand) {
        this.permutations = new int[512];
        this.xCoord = rand.nextDouble() * 256.0D;
        this.yCoord = rand.nextDouble() * 256.0D;
        this.zCoord = rand.nextDouble() * 256.0D;

        int i2;
        for(i2 = 0; i2 < 256; this.permutations[i2] = i2++) {
        }

        for(i2 = 0; i2 < 256; ++i2) {
            int i3 = rand.nextInt(256 - i2) + i2;
            int i4 = this.permutations[i2];
            this.permutations[i2] = this.permutations[i3];
            this.permutations[i3] = i4;
            this.permutations[i2 + 256] = this.permutations[i2];
        }

    }

    private double generateNoise(double x, double y, double z) {
        double d7 = x + this.xCoord;
        double d9 = y + this.yCoord;
        double d11 = z + this.zCoord;
        int i25 = (int)d7;
        int i2 = (int)d9;
        int i26 = (int)d11;
        if(d7 < (double)i25) {
            --i25;
        }

        if(d9 < (double)i2) {
            --i2;
        }

        if(d11 < (double)i26) {
            --i26;
        }

        int i4 = i25 & 255;
        int i27 = i2 & 255;
        int i6 = i26 & 255;
        d7 -= (double)i25;
        d9 -= (double)i2;
        d11 -= (double)i26;
        double d19 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);
        double d21 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
        double d23 = d11 * d11 * d11 * (d11 * (d11 * 6.0D - 15.0D) + 10.0D);
        i25 = this.permutations[i4] + i27;
        i2 = this.permutations[i25] + i6;
        i25 = this.permutations[i25 + 1] + i6;
        i26 = this.permutations[i4 + 1] + i27;
        i4 = this.permutations[i26] + i6;
        i26 = this.permutations[i26 + 1] + i6;
        return lerp(d23, lerp(d21, lerp(d19, grad(this.permutations[i2], d7, d9, d11), grad(this.permutations[i4], d7 - 1.0D, d9, d11)), lerp(d19, grad(this.permutations[i25], d7, d9 - 1.0D, d11), grad(this.permutations[i26], d7 - 1.0D, d9 - 1.0D, d11))), lerp(d21, lerp(d19, grad(this.permutations[i2 + 1], d7, d9, d11 - 1.0D), grad(this.permutations[i4 + 1], d7 - 1.0D, d9, d11 - 1.0D)), lerp(d19, grad(this.permutations[i25 + 1], d7, d9 - 1.0D, d11 - 1.0D), grad(this.permutations[i26 + 1], d7 - 1.0D, d9 - 1.0D, d11 - 1.0D))));
    }

    private static double lerp(double x, double y, double z) {
        return y + x * (z - y);
    }

    private static double grad(int permutation, double x, double y, double z) {
        double d8 = (permutation &= 15) < 8 ? x : y;
        double d10 = permutation < 4 ? y : (permutation != 12 && permutation != 14 ? z : x);
        return ((permutation & 1) == 0 ? d8 : -d8) + ((permutation & 2) == 0 ? d10 : -d10);
    }

    public double generateNoise(double x, double y) {
        return this.generateNoise(x, y, 0.0D);
    }

    public void populateNoiseArray(double[] octaves, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double coordScaleX, double coordScaleY, double coordScaleZ, double noiseScale) {
        int i16 = 0;
        double d17 = 1.0D / noiseScale;
        int i61 = -1;
        double d26 = 0.0D;
        double d28 = 0.0D;
        double d30 = 0.0D;
        double d32 = 0.0D;

        for(int i22 = 0; i22 < sizeX; ++i22) {
            double d35;
            int i15 = (int)(d35 = (double)(x + i22) * coordScaleX + this.xCoord);
            if(d35 < (double)i15) {
                --i15;
            }

            int i23 = i15 & 255;
            double d39 = (d35 -= (double)i15) * d35 * d35 * (d35 * (d35 * 6.0D - 15.0D) + 10.0D);

            for(int i24 = 0; i24 < sizeZ; ++i24) {
                double d42;
                i15 = (int)(d42 = (double)(z + i24) * coordScaleZ + this.zCoord);
                if(d42 < (double)i15) {
                    --i15;
                }

                int i25 = i15 & 255;
                double d46 = (d42 -= (double)i15) * d42 * d42 * (d42 * (d42 * 6.0D - 15.0D) + 10.0D);

                for(int i34 = 0; i34 < sizeY; ++i34) {
                    double d49;
                    i15 = (int)(d49 = (double)(y + i34) * coordScaleY + this.yCoord);
                    if(d49 < (double)i15) {
                        --i15;
                    }

                    int i20 = i15 & 255;
                    double d53 = (d49 -= (double)i15) * d49 * d49 * (d49 * (d49 * 6.0D - 15.0D) + 10.0D);
                    if(i34 == 0 || i20 != i61) {
                        i61 = i20;
                        i15 = this.permutations[i23] + i20;
                        int i19 = this.permutations[i15] + i25;
                        i15 = this.permutations[i15 + 1] + i25;
                        i20 += this.permutations[i23 + 1];
                        int i21 = this.permutations[i20] + i25;
                        i20 = this.permutations[i20 + 1] + i25;
                        d26 = lerp(d39, grad(this.permutations[i19], d35, d49, d42), grad(this.permutations[i21], d35 - 1.0D, d49, d42));
                        d28 = lerp(d39, grad(this.permutations[i15], d35, d49 - 1.0D, d42), grad(this.permutations[i20], d35 - 1.0D, d49 - 1.0D, d42));
                        d30 = lerp(d39, grad(this.permutations[i19 + 1], d35, d49, d42 - 1.0D), grad(this.permutations[i21 + 1], d35 - 1.0D, d49, d42 - 1.0D));
                        d32 = lerp(d39, grad(this.permutations[i15 + 1], d35, d49 - 1.0D, d42 - 1.0D), grad(this.permutations[i20 + 1], d35 - 1.0D, d49 - 1.0D, d42 - 1.0D));
                    }

                    double d55 = lerp(d53, d26, d28);
                    double d57 = lerp(d53, d30, d32);
                    double d59 = lerp(d46, d55, d57);
                    int i10001 = i16++;
                    octaves[i10001] += d59 * d17;
                }
            }
        }

    }
}
