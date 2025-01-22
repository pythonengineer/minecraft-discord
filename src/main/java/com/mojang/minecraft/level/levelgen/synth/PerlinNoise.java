package com.mojang.minecraft.level.levelgen.synth;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class PerlinNoise extends Synth {
    private ImprovedNoise[] noiseLevels = new ImprovedNoise[8];
    private int levels = 8;

    public PerlinNoise(EaglercraftRandom random1, int i2) {
        for(i2 = 0; i2 < 8; ++i2) {
            this.noiseLevels[i2] = new ImprovedNoise(random1);
        }

    }

    public final double getValue(double d1, double d3) {
        double d5 = 0.0D;
        double d7 = 1.0D;

        for(int i9 = 0; i9 < this.levels; ++i9) {
            d5 += this.noiseLevels[i9].getValue(d1 / d7, d3 / d7) * d7;
            d7 *= 2.0D;
        }

        return d5;
    }
}
