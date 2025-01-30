package com.mojang.minecraft.level.levelgen.synth;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class ImprovedNoise extends Synth {
    private int[] p;

    public ImprovedNoise() {
        this(new EaglercraftRandom());
    }

    public ImprovedNoise(EaglercraftRandom random1) {
        this.p = new int[512];

        int i2;
        for(i2 = 0; i2 < 256; this.p[i2] = i2++) {
        }

        for(i2 = 0; i2 < 256; ++i2) {
            int i3 = random1.nextInt(256 - i2) + i2;
            int i4 = this.p[i2];
            this.p[i2] = this.p[i3];
            this.p[i3] = i4;
            this.p[i2 + 256] = this.p[i2];
        }

    }

    private static double fade(double d0) {
        return d0 * d0 * d0 * (d0 * (d0 * 6.0D - 15.0D) + 10.0D);
    }

    private static double lerp(double d0, double d2, double d4) {
        return d2 + d0 * (d4 - d2);
    }

    private static double grad(int i0, double d1, double d3, double d5) {
        double d8 = (i0 &= 15) < 8 ? d1 : d3;
        double d10 = i0 < 4 ? d3 : (i0 != 12 && i0 != 14 ? d5 : d1);
        return ((i0 & 1) == 0 ? d8 : -d8) + ((i0 & 2) == 0 ? d10 : -d10);
    }

    public final double getValue(double d1, double d3) {
        double d10 = 0.0D;
        double d8 = d3;
        double d6 = d1;
        int i21 = (int)Math.floor(d1) & 255;
        int i2 = (int)Math.floor(d3) & 255;
        int i22 = (int)Math.floor(0.0D) & 255;
        d6 -= Math.floor(d6);
        d8 -= Math.floor(d8);
        d10 = 0.0D - Math.floor(0.0D);
        double d15 = fade(d6);
        double d17 = fade(d8);
        double d19 = fade(d10);
        int i4 = this.p[i21] + i2;
        int i5 = this.p[i4] + i22;
        i4 = this.p[i4 + 1] + i22;
        i21 = this.p[i21 + 1] + i2;
        i2 = this.p[i21] + i22;
        i21 = this.p[i21 + 1] + i22;
        return lerp(d19, lerp(d17, lerp(d15, grad(this.p[i5], d6, d8, d10), grad(this.p[i2], d6 - 1.0D, d8, d10)), lerp(d15, grad(this.p[i4], d6, d8 - 1.0D, d10), grad(this.p[i21], d6 - 1.0D, d8 - 1.0D, d10))), lerp(d17, lerp(d15, grad(this.p[i5 + 1], d6, d8, d10 - 1.0D), grad(this.p[i2 + 1], d6 - 1.0D, d8, d10 - 1.0D)), lerp(d15, grad(this.p[i4 + 1], d6, d8 - 1.0D, d10 - 1.0D), grad(this.p[i21 + 1], d6 - 1.0D, d8 - 1.0D, d10 - 1.0D))));
    }
}
