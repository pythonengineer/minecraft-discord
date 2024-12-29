package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.util.MathHelper;

public class ScaledResolution {
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(int width, int height) {
        this.scaledWidth = width;
        this.scaledHeight = height;
        this.scaleFactor = 1;
        while (this.scaleFactor < 3 && this.scaledWidth / (this.scaleFactor + 1) >= 320
                && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
