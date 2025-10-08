package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public class Frustrum {
    private ClippingHelper clippingHelper = ClippingHelperImpl.init();

    public boolean isBoundingBoxInFrustrum(AxisAlignedBB var1) {
        ClippingHelper var17 = this.clippingHelper;
        double var15 = var1.maxZ;
        double var13 = var1.maxY;
        double var11 = var1.maxX;
        double var9 = var1.minZ;
        double var7 = var1.minY;
        double var5 = var1.minX;
        var17 = var17;

        for(int var18 = 0; var18 < 6; ++var18) {
            if((double)var17.frustrum[var18][0] * var5 + (double)var17.frustrum[var18][1] * var7 + (double)var17.frustrum[var18][2] * var9 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var11 + (double)var17.frustrum[var18][1] * var7 + (double)var17.frustrum[var18][2] * var9 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var5 + (double)var17.frustrum[var18][1] * var13 + (double)var17.frustrum[var18][2] * var9 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var11 + (double)var17.frustrum[var18][1] * var13 + (double)var17.frustrum[var18][2] * var9 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var5 + (double)var17.frustrum[var18][1] * var7 + (double)var17.frustrum[var18][2] * var15 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var11 + (double)var17.frustrum[var18][1] * var7 + (double)var17.frustrum[var18][2] * var15 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var5 + (double)var17.frustrum[var18][1] * var13 + (double)var17.frustrum[var18][2] * var15 + (double)var17.frustrum[var18][3] <= 0.0D && (double)var17.frustrum[var18][0] * var11 + (double)var17.frustrum[var18][1] * var13 + (double)var17.frustrum[var18][2] * var15 + (double)var17.frustrum[var18][3] <= 0.0D) {
                return false;
            }
        }

        return true;
    }
}
