package net.minecraft.client.render;

import net.minecraft.game.physics.AxisAlignedBB;

public class ClippingHelper {
    public static boolean isVisible = false;
    public float[][] frustrum = new float[16][16];
    public float[] projectionMatrix = new float[16];
    public float[] modelviewMatrix = new float[16];
    public float[] clippingMatrix = new float[16];

    public final boolean isVisible(float var1, float var2, float var3, float var4, float var5, float var6) {
        for(int var7 = 0; var7 < 6; ++var7) {
            if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }

            if(this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }
        }

        return true;
    }

    public final boolean checkInFrustrum(float var1, float var2, float var3, float var4, float var5, float var6) {
        for(int var7 = 0; var7 < 6; ++var7) {
            if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
                return false;
            }
        }

        return true;
    }

    public final boolean isBoundingBoxInFrustrum(AxisAlignedBB var1) {
        return this.checkInFrustrum(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
    }
}
