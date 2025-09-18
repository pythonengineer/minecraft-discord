package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public final class RenderLiving extends Render {
    private ModelBase mainModel = new ModelBiped();

    public RenderLiving() {
        this.shadowSize = 0.5F;
    }

    public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
        EntityLiving var10001 = (EntityLiving)var1;
        var6 = var6;
        var5 = var4;
        var4 = var3;
        var3 = var2;
        EntityLiving var12 = var10001;
        RenderLiving var11 = this;
        GL11.glPushMatrix();

        try {
            float var7 = var12.prevRenderYawOffset + (var12.renderYawOffset - var12.prevRenderYawOffset) * var6;
            float var8 = var12.prevRotationYaw + (var12.rotationYaw - var12.prevRotationYaw) * var6;
            float var9 = var12.prevRotationPitch + (var12.rotationPitch - var12.prevRotationPitch) * var6;
            GL11.glTranslatef(var3, var4, var5);
            var11.loadTexture(var12.texture);
            GL11.glRotatef(180.0F - var7, 0.0F, 1.0F, 0.0F);
            if(var12.deathTime > 0) {
                var3 = ((float)var12.deathTime + var6 - 1.0F) / 20.0F * 1.6F;
                var3 = MathHelper.sqrt_float(var3);
                if(var3 > 1.0F) {
                    var3 = 1.0F;
                }

                GL11.glRotatef(var3 * 90.0F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(-(1.0F / 16.0F), -(1.0F / 16.0F), 1.0F / 16.0F);
            GL11.glTranslatef(0.0F, -24.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            var3 = var12.moveStrafing + (var12.moveForward - var12.moveStrafing) * var6;
            var4 = var12.randomYawVelocity - var12.moveForward * (1.0F - var6);
            if(var3 > 1.0F) {
                var3 = 1.0F;
            }

            var11.mainModel.render(var4, var3, 0.0F, var8 - var7, var9, 1.0F);
            if(var12.hurtTime > 0 || var12.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                var11.mainModel.render(var4, var3, 0.0F, var8 - var7, var9, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        GL11.glPopMatrix();
    }
}
