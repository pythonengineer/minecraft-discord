package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public class RenderLiving extends Render {
    protected ModelBase mainModel;
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
        EntityLiving var16 = var10001;
        RenderLiving var15 = this;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        try {
            float var7 = var16.prevRenderYawOffset + (var16.renderYawOffset - var16.prevRenderYawOffset) * var6;
            float var8 = var16.prevRotationYaw + (var16.rotationYaw - var16.prevRotationYaw) * var6;
            float var9 = var16.prevRotationPitch + (var16.rotationPitch - var16.prevRotationPitch) * var6;
            GL11.glTranslatef(var3, var4, var5);
            var3 = (float)var16.ticksExisted + var6;
            GL11.glRotatef(180.0F - var7, 0.0F, 1.0F, 0.0F);
            if(var16.deathTime > 0) {
                var4 = ((float)var16.deathTime + var6 - 1.0F) / 20.0F * 1.6F;
                var4 = MathHelper.sqrt_float(var4);
                if(var4 > 1.0F) {
                    var4 = 1.0F;
                }

                GL11.glRotatef(var4 * var15.getDeathMaxRotation(var16), 0.0F, 0.0F, 1.0F);
            }

            GL11.glScalef(-(1.0F / 16.0F), -(1.0F / 16.0F), 1.0F / 16.0F);
            var15.preRenderCallback(var16, var6);
            GL11.glTranslatef(0.0F, -24.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            var4 = var16.prevLimbYaw + (var16.limbYaw - var16.prevLimbYaw) * var6;
            var5 = var16.limbSwing - var16.limbYaw * (1.0F - var6);
            if(var4 > 1.0F) {
                var4 = 1.0F;
            }

            var15.loadDownloadableImageTexture(var16.skinUrl, var16.getTexture());
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            var15.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

            for(int var10 = 0; var10 < 4; ++var10) {
                if(var15.shouldRenderPass(var16, var10)) {
                    var15.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            float var18 = var16.getBrightness(var6);
            int var17 = var15.getColorMultiplier(var16, var18, var6);
            if(var17 >>> 24 > 0 || var16.hurtTime > 0 || var16.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);
                if(var16.hurtTime > 0 || var16.deathTime > 0) {
                    GL11.glColor4f(var18, 0.0F, 0.0F, 0.4F);
                    var15.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

                    for(int var11 = 0; var11 < 4; ++var11) {
                        if(var15.shouldRenderPass(var16, var11)) {
                            GL11.glColor4f(var18, 0.0F, 0.0F, 0.4F);
                            var15.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                        }
                    }
                }

                if(var17 >>> 24 > 0) {
                    float var19 = (float)(var17 >> 16 & 255) / 255.0F;
                    var18 = (float)(var17 >> 8 & 255) / 255.0F;
                    float var12 = (float)(var17 & 255) / 255.0F;
                    var6 = (float)(var17 >>> 24) / 255.0F;
                    GL11.glColor4f(var19, var18, var12, var6);
                    var15.mainModel.render(var5, var4, var3, var8 - var7, var9, 1.0F);

                    for(int var13 = 0; var13 < 4; ++var13) {
                        if(var15.shouldRenderPass(var16, var13)) {
                            GL11.glColor4f(var19, var18, var12, var6);
                            var15.e.render(var5, var4, var3, var8 - var7, var9, 1.0F);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
