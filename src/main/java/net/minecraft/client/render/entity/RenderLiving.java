package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public class RenderLiving extends Render {
    private ModelBase mainModel;
    private ModelBase e;

    public RenderLiving(ModelBase var1, float var2) {
        this.mainModel = var1;
        this.shadowSize = var2;
    }

    public final void setRenderPassModel(ModelBase var1) {
        this.e = var1;
    }

    protected boolean shouldRenderPass(EntityLiving var1, int var2) {
        return false;
    }

    protected float getDeathMaxRotation(EntityLiving var1) {
        return 90.0F;
    }

    protected int getColorMultiplier(EntityLiving var1, float var2, float var3) {
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
        EntityLiving var15 = var10001;
        RenderLiving var14 = this;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        try {
            float var7 = var15.prevRenderYawOffset + (var15.renderYawOffset - var15.prevRenderYawOffset) * var6;
            float var8 = var15.prevRotationYaw + (var15.rotationYaw - var15.prevRotationYaw) * var6;
            float var9 = var15.prevRotationPitch + (var15.rotationPitch - var15.prevRotationPitch) * var6;
            GL11.glTranslatef(var3, var4, var5);
            var3 = (float)var15.ticksExisted + var6;
            GL11.glRotatef(180.0F - var7, 0.0F, 1.0F, 0.0F);
            if(var15.deathTime > 0) {
                var4 = ((float)var15.deathTime + var6 - 1.0F) / 20.0F * 1.6F;
                var4 = MathHelper.sqrt_float(var4);
                if(var4 > 1.0F) {
                    var4 = 1.0F;
                }

                GL11.glRotatef(var4 * var14.getDeathMaxRotation(var15), 0.0F, 0.0F, 1.0F);
            }

            GL11.glScalef(-(1.0F / 16.0F), -(1.0F / 16.0F), 1.0F / 16.0F);
            var14.preRenderCallback(var15, var6);
            GL11.glTranslatef(0.0F, -24.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            var4 = var15.prevLimbYaw + (var15.limbYaw - var15.prevLimbYaw) * var6;
            var5 = var15.limbSwing - var15.limbYaw * (1.0F - var6);
            if(var4 > 1.0F) {
                var4 = 1.0F;
            }

            var14.loadDownloadableImageTexture(var15.skinUrl, var15.getTexture());
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            var14.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

            int var10;
            for(var10 = 0; var10 < 4; ++var10) {
                if(var14.shouldRenderPass(var15, var10)) {
                    var14.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                }
            }

            float var17 = var15.getBrightness(var6);
            int var16 = var14.getColorMultiplier(var15, var17, var6);
            if(var16 >>> 24 > 0 || var15.hurtTime > 0 || var15.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);
                if(var15.hurtTime > 0 || var15.deathTime > 0) {
                    GL11.glColor4f(var17, 0.0F, 0.0F, 0.4F);
                    var14.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

                    for(var10 = 0; var10 < 4; ++var10) {
                        if(var14.shouldRenderPass(var15, var10)) {
                            var14.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                        }
                    }
                }

                if(var16 >>> 24 > 0) {
                    var17 = (float)(var16 >> 16 & 255) / 255.0F;
                    float var11 = (float)(var16 >> 8 & 255) / 255.0F;
                    float var12 = (float)(var16 & 255) / 255.0F;
                    var6 = (float)(var16 >>> 24) / 255.0F;
                    GL11.glColor4f(var17, var11, var12, var6);
                    var14.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

                    for(var16 = 0; var16 < 4; ++var16) {
                        if(var14.shouldRenderPass(var15, var16)) {
                            var14.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
