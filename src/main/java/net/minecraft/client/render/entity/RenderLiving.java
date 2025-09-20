package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public class RenderLiving extends Render {
    private ModelBase mainModel;

    public RenderLiving(ModelBase var1, float var2) {
        this.mainModel = var1;
        this.shadowSize = var2;
    }

    protected float getDeathMaxRotation() {
        return 90.0F;
    }

    protected int getColorMultiplier(EntityLiving var1, float var2) {
        return 0;
    }

    protected void preRenderCallback(EntityLiving var1, float var2) {
    }

    public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
        EntityLiving var10001 = (EntityLiving)var1;
        var6 = var6;
        var5 = var4;
        var4 = var3;
        var3 = var2;
        EntityLiving var13 = var10001;
        RenderLiving var12 = this;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        try {
            float var7 = var13.prevRenderYawOffset + (var13.renderYawOffset - var13.prevRenderYawOffset) * var6;
            float var8 = var13.prevRotationYaw + (var13.rotationYaw - var13.prevRotationYaw) * var6;
            float var9 = var13.prevRotationPitch + (var13.rotationPitch - var13.prevRotationPitch) * var6;
            GL11.glTranslatef(var3, var4, var5);
            var12.loadTexture(var13.texture);
            GL11.glRotatef(180.0F - var7, 0.0F, 1.0F, 0.0F);
            if(var13.deathTime > 0) {
                var3 = ((float)var13.deathTime + var6 - 1.0F) / 20.0F * 1.6F;
                var3 = MathHelper.sqrt_float(var3);
                if(var3 > 1.0F) {
                    var3 = 1.0F;
                }

                GL11.glRotatef(var3 * var12.getDeathMaxRotation(), 0.0F, 0.0F, 1.0F);
            }

            GL11.glScalef(-(1.0F / 16.0F), -(1.0F / 16.0F), 1.0F / 16.0F);
            var12.preRenderCallback(var13, var6);
            GL11.glTranslatef(0.0F, -24.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            var3 = var13.prevLimbYaw + (var13.limbYaw - var13.prevLimbYaw) * var6;
            var4 = var13.limbSwing - var13.limbYaw * (1.0F - var6);
            if(var3 > 1.0F) {
                var3 = 1.0F;
            }

            var12.mainModel.render(var4, var3, 0.0F, var8 - var7, var9, 1.0F);
            var5 = var13.getBrightness(var6);
            int var14 = var12.getColorMultiplier(var13, var6);
            if(var14 >>> 24 > 0 || var13.hurtTime > 0 || var13.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);
                if(var13.hurtTime > 0 || var13.deathTime > 0) {
                    GL11.glColor4f(var5, 0.0F, 0.0F, 0.4F);
                    var12.mainModel.render(var4, var3, 0.0F, var8 - var7, var9, 1.0F);
                }

                if(var14 >>> 24 > 0) {
                    var2 = (float)(var14 >> 16 & 255) / 255.0F;
                    var5 = (float)(var14 >> 8 & 255) / 255.0F;
                    float var10 = (float)(var14 & 255) / 255.0F;
                    var6 = (float)(var14 >>> 24) / 255.0F;
                    GL11.glColor4f(var2, var5, var10, var6);
                    var12.mainModel.render(var4, var3, 0.0F, var8 - var7, var9, 1.0F);
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
