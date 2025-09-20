package net.minecraft.client.render.camera;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.physics.AxisAlignedBB;

public final class Frustrum implements ICamera {
    private ClippingHelperFaces[] clippingHelper = new ClippingHelperFaces[6];

    public Frustrum(EntityLiving var1, float var2, float var3) {
        float var4 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var3;
        float var5 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var3;
        float var6 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
        float var7 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
        float var10 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
        var3 = MathHelper.sin(var4 / 180.0F * (float)Math.PI) * MathHelper.cos(var5 / 180.0F * (float)Math.PI);
        float var8 = -MathHelper.cos(var4 / 180.0F * (float)Math.PI) * MathHelper.cos(var5 / 180.0F * (float)Math.PI);
        float var9 = -MathHelper.sin(var5 / 180.0F * (float)Math.PI);
        this.clippingHelper[0] = new ClippingHelperFaces(var6, var7, var10, var4, var5);
        this.clippingHelper[1] = new ClippingHelperFaces(var6, var7, var10, var4 + 30.0F, var5);
        this.clippingHelper[2] = new ClippingHelperFaces(var6, var7, var10, var4 - 30.0F, var5);
        this.clippingHelper[3] = new ClippingHelperFaces(var6, var7, var10, var4, var5 + 45.0F);
        this.clippingHelper[4] = new ClippingHelperFaces(var6, var7, var10, var4, var5 - 45.0F);
        this.clippingHelper[5] = new ClippingHelperFaces(var6 + var3 * var2, var7 + var9 * var2, var10 + var8 * var2, var4 + 180.0F, -var5);
    }

    public final boolean isBoundingBoxInFrustrum(AxisAlignedBB var1) {
        float var2;
        float var3;
        float var4;
        float var5;
        float var6;
        float var7;
        float var8;
        float var11;
        float var12;
        float var13;
        float var10000;
        label63: {
            var7 = var1.maxZ;
            var6 = var1.maxY;
            var5 = var1.maxX;
            var4 = var1.minZ;
            var3 = var1.minY;
            var2 = var1.minX;
            var8 = (var5 - var2) / 2.0F;
            float var9 = (var6 - var3) / 2.0F;
            float var10 = (var7 - var4) / 2.0F;
            var11 = var2 + var8;
            var12 = var3 + var9;
            var13 = var4 + var10;
            if(var8 > var9) {
                if(var8 <= var10) {
                    var10000 = var8;
                    break label63;
                }
            } else if(var9 > var10) {
                var10000 = var9;
                break label63;
            }

            var10000 = var10;
        }

        var8 = var10000 * 1.5F;
        return !this.clippingHelper[0].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[1].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[2].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[3].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[4].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[5].isVisible(var11, var12, var13, var8) ? false : (!this.clippingHelper[0].isVisible(var2, var3, var4, var5, var6, var7) ? false : (!this.clippingHelper[1].isVisible(var2, var3, var4, var5, var6, var7) ? false : (!this.clippingHelper[2].isVisible(var2, var3, var4, var5, var6, var7) ? false : (!this.clippingHelper[3].isVisible(var2, var3, var4, var5, var6, var7) ? false : (!this.clippingHelper[4].isVisible(var2, var3, var4, var5, var6, var7) ? false : this.clippingHelper[5].isVisible(var2, var3, var4, var5, var6, var7)))))))))));
    }
}
