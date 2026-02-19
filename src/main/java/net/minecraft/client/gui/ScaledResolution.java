package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class ScaledResolution {
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(Minecraft parMinecraft) {
        this.scaledWidth = parMinecraft.displayWidth;
        this.scaledHeight = parMinecraft.displayHeight;
        this.scaleFactor = 1;
        int i = parMinecraft.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }

        i = Math.round(i * Math.max(parMinecraft.displayDPI, 0.5f));

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320
                && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
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
