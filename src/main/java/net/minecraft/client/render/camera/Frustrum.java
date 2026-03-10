package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public class Frustrum {
    private ClippingHelper clippingHelper = ClippingHelperImplementation.getInstance();
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public boolean isBoundingBoxInFrustum(AxisAlignedBB aabb) {
        return this.isBoxInFrustum(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public void setPosition(double x, double y, double z) {
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }

    public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double d10001 = minX - this.xPosition;
        double d10002 = minY - this.yPosition;
        double d10003 = minZ - this.zPosition;
        double d10004 = maxX - this.xPosition;
        double d10005 = maxY - this.yPosition;
        double d24 = maxZ - this.zPosition;
        double d22 = d10005;
        double d20 = d10004;
        double d18 = d10003;
        double d16 = d10002;
        double d14 = d10001;
        ClippingHelper clippingHelper26 = this.clippingHelper;

        for(int i2 = 0; i2 < 6; ++i2) {
            if((double)clippingHelper26.frustum[i2][0] * d14 + (double)clippingHelper26.frustum[i2][1] * d16 + (double)clippingHelper26.frustum[i2][2] * d18 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d20 + (double)clippingHelper26.frustum[i2][1] * d16 + (double)clippingHelper26.frustum[i2][2] * d18 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d14 + (double)clippingHelper26.frustum[i2][1] * d22 + (double)clippingHelper26.frustum[i2][2] * d18 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d20 + (double)clippingHelper26.frustum[i2][1] * d22 + (double)clippingHelper26.frustum[i2][2] * d18 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d14 + (double)clippingHelper26.frustum[i2][1] * d16 + (double)clippingHelper26.frustum[i2][2] * d24 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d20 + (double)clippingHelper26.frustum[i2][1] * d16 + (double)clippingHelper26.frustum[i2][2] * d24 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d14 + (double)clippingHelper26.frustum[i2][1] * d22 + (double)clippingHelper26.frustum[i2][2] * d24 + (double)clippingHelper26.frustum[i2][3] <= 0.0D && (double)clippingHelper26.frustum[i2][0] * d20 + (double)clippingHelper26.frustum[i2][1] * d22 + (double)clippingHelper26.frustum[i2][2] * d24 + (double)clippingHelper26.frustum[i2][3] <= 0.0D) {
                return false;
            }
        }

        return true;
    }
}