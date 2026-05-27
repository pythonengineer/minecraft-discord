package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public class Frustrum implements ICamera {
    private ClippingHelper clippingHelper = ClippingHelperImplementation.getInstance();
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public void setPosition(double x, double y, double z) {
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }

    public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.clippingHelper.isBoxInFrustum(minX - this.xPosition, minY - this.yPosition, minZ - this.zPosition, maxX - this.xPosition, maxY - this.yPosition, maxZ - this.zPosition);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB aabb) {
        return this.isBoxInFrustum(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }
}
