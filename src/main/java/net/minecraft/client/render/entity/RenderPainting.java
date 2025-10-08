package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.EnumArt;

public final class RenderPainting extends Render {
    private EaglercraftRandom rand = new EaglercraftRandom();

    public final void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        EntityPainting var22 = (EntityPainting)var1;
        this.rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glRotatef(var8, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);
        this.loadTexture("/art/kz.png");
        EnumArt var3 = var22.art;
        GL11.glScalef(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);
        int var25 = var3.offsetY;
        int var5 = var3.offsetX;
        int var24 = var3.sizeY;
        int var23 = var3.sizeX;
        var22 = var22;
        RenderPainting var21 = this;
        float var7 = (float)(-var23) / 2.0F;
        var8 = (float)(-var24) / 2.0F;

        for(int var26 = 0; var26 < var23 / 16; ++var26) {
            for(int var10 = 0; var10 < var24 / 16; ++var10) {
                float var11 = var7 + (float)(var26 + 1 << 4);
                float var27 = var7 + (float)(var26 << 4);
                float var13 = var8 + (float)(var10 + 1 << 4);
                float var14 = var8 + (float)(var10 << 4);
                float var10002 = (var11 + var27) / 2.0F;
                float var18 = (var13 + var14) / 2.0F;
                float var17 = var10002;
                int var19 = MathHelper.floor_double(var22.posX);
                int var28 = MathHelper.floor_double(var22.posY + (double)(var18 / 16.0F));
                int var20 = MathHelper.floor_double(var22.posZ);
                if(var22.direction == 0) {
                    var19 = MathHelper.floor_double(var22.posX + (double)(var17 / 16.0F));
                }

                if(var22.direction == 1) {
                    var20 = MathHelper.floor_double(var22.posZ - (double)(var17 / 16.0F));
                }

                if(var22.direction == 2) {
                    var19 = MathHelper.floor_double(var22.posX - (double)(var17 / 16.0F));
                }

                if(var22.direction == 3) {
                    var20 = MathHelper.floor_double(var22.posZ + (double)(var17 / 16.0F));
                }

                float var15 = var21.renderManager.worldObj.getBrightness(var19, var28, var20);
                GL11.glColor3f(var15, var15, var15);
                var15 = (float)(var5 + var23 - (var26 << 4)) / 256.0F;
                float var16 = (float)(var5 + var23 - (var26 + 1 << 4)) / 256.0F;
                var17 = (float)(var25 + var24 - (var10 << 4)) / 256.0F;
                var18 = (float)(var25 + var24 - (var10 + 1 << 4)) / 256.0F;
                Tessellator var29 = Tessellator.instance;
                var29.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                var29.normal(0.0F, 0.0F, -1.0F);
                var29.addVertexWithUV(var11, var14, -0.5F, var16, var17);
                var29.addVertexWithUV(var27, var14, -0.5F, var15, var17);
                var29.addVertexWithUV(var27, var13, -0.5F, var15, var18);
                var29.addVertexWithUV(var11, var13, -0.5F, var16, var18);
                var29.normal(0.0F, 0.0F, 1.0F);
                var29.addVertexWithUV(var11, var13, 0.5F, 12.0F / 16.0F, 0.0F);
                var29.addVertexWithUV(var27, var13, 0.5F, 13.0F / 16.0F, 0.0F);
                var29.addVertexWithUV(var27, var14, 0.5F, 13.0F / 16.0F, 1.0F / 16.0F);
                var29.addVertexWithUV(var11, var14, 0.5F, 12.0F / 16.0F, 1.0F / 16.0F);
                var29.normal(0.0F, -1.0F, 0.0F);
                var29.addVertexWithUV(var11, var13, -0.5F, 12.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var27, var13, -0.5F, 13.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var27, var13, 0.5F, 13.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var11, var13, 0.5F, 12.0F / 16.0F, 0.001953125F);
                var29.normal(0.0F, 1.0F, 0.0F);
                var29.addVertexWithUV(var11, var14, 0.5F, 12.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var27, var14, 0.5F, 13.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var27, var14, -0.5F, 13.0F / 16.0F, 0.001953125F);
                var29.addVertexWithUV(var11, var14, -0.5F, 12.0F / 16.0F, 0.001953125F);
                var29.normal(-1.0F, 0.0F, 0.0F);
                var29.addVertexWithUV(var11, var13, 0.5F, 385.0F / 512.0F, 0.0F);
                var29.addVertexWithUV(var11, var14, 0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
                var29.addVertexWithUV(var11, var14, -0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
                var29.addVertexWithUV(var11, var13, -0.5F, 385.0F / 512.0F, 0.0F);
                var29.normal(1.0F, 0.0F, 0.0F);
                var29.addVertexWithUV(var27, var13, -0.5F, 385.0F / 512.0F, 0.0F);
                var29.addVertexWithUV(var27, var14, -0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
                var29.addVertexWithUV(var27, var14, 0.5F, 385.0F / 512.0F, 1.0F / 16.0F);
                var29.addVertexWithUV(var27, var13, 0.5F, 385.0F / 512.0F, 0.0F);
                var29.draw();
            }
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        GL11.glPopMatrix();
    }
}
