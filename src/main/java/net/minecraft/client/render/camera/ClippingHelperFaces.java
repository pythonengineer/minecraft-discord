package net.minecraft.client.render.camera;

import net.lax1dude.eaglercraft.util.MathHelper;

final class ClippingHelperFaces {
    private float posX;
    private float posY;
    private float posZ;
    private float posXPlus;
    private float posZPlus;
    private float posYPlus;
    private float clippingBoundingBox;

    public ClippingHelperFaces(float var1, float var2, float var3, float var4, float var5) {
        this.posX = var1;
        this.posY = var2;
        this.posZ = var3;
        this.posXPlus = MathHelper.sin(var4 / 180.0F * (float)Math.PI) * MathHelper.cos(var5 / 180.0F * (float)Math.PI);
        this.posYPlus = -MathHelper.cos(var4 / 180.0F * (float)Math.PI) * MathHelper.cos(var5 / 180.0F * (float)Math.PI);
        this.posZPlus = -MathHelper.sin(var5 / 180.0F * (float)Math.PI);
        this.clippingBoundingBox = this.posX * this.posXPlus + this.posY * this.posZPlus + this.posZ * this.posYPlus;
    }

    public final boolean isVisible(float var1, float var2, float var3, float var4) {
        return var1 * this.posXPlus + var2 * this.posZPlus + var3 * this.posYPlus > this.clippingBoundingBox - var4;
    }

    public final boolean isVisible(float var1, float var2, float var3, float var4, float var5, float var6) {
        return var1 * this.posXPlus + var2 * this.posZPlus + var3 * this.posYPlus > this.clippingBoundingBox || var4 * this.posXPlus + var2 * this.posZPlus + var3 * this.posYPlus > this.clippingBoundingBox || var1 * this.posXPlus + var5 * this.posZPlus + var3 * this.posYPlus > this.clippingBoundingBox || var4 * this.posXPlus + var5 * this.posZPlus + var3 * this.posYPlus > this.clippingBoundingBox || var1 * this.posXPlus + var2 * this.posZPlus + var6 * this.posYPlus > this.clippingBoundingBox || var4 * this.posXPlus + var2 * this.posZPlus + var6 * this.posYPlus > this.clippingBoundingBox || var1 * this.posXPlus + var5 * this.posZPlus + var6 * this.posYPlus > this.clippingBoundingBox || var4 * this.posXPlus + var5 * this.posZPlus + var6 * this.posYPlus > this.clippingBoundingBox;
    }
}
