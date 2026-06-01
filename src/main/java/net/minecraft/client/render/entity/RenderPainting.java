package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.EnumArt;

public class RenderPainting extends Render {
	private EaglercraftRandom rand = new EaglercraftRandom();

	public void renderThePainting(EntityPainting entity, double x, double y, double z, float yaw, float partialTicks) {
		this.rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_RESCALE_NORMAL);
		this.loadTexture("/art/kz.png");
		EnumArt enumArt2 = entity.art;
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
        this.setSizes(entity, enumArt2.sizeX, enumArt2.sizeY, enumArt2.offsetX, enumArt2.offsetY);
        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

    private void setSizes(EntityPainting entity, int sizeX, int sizeY, int offsetX, int offsetY) {
        float f7 = (float)(-sizeX) / 2.0F;
        float f8 = (float)(-sizeY) / 2.0F;

        for(int i26 = 0; i26 < sizeX / 16; ++i26) {
            for(int i10 = 0; i10 < sizeY / 16; ++i10) {
                float f11 = f7 + (float)(i26 + 1 << 4);
                float f27 = f7 + (float)(i26 << 4);
                float f13 = f8 + (float)(i10 + 1 << 4);
                float f14 = f8 + (float)(i10 << 4);
                this.getOffset(entity, (f11 + f27) / 2.0F, (f13 + f14) / 2.0F);
                float f15 = (float)(offsetX + sizeX - (i26 << 4)) / 256.0F;
                float f16 = (float)(offsetX + sizeX - (i26 + 1 << 4)) / 256.0F;
                float f17 = (float)(offsetY + sizeY - (i10 << 4)) / 256.0F;
                float f18 = (float)(offsetY + sizeY - (i10 + 1 << 4)) / 256.0F;
                Tessellator tessellator29 = Tessellator.instance;
                Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator29.setNormal(0.0F, 0.0F, -1.0F);
                tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, (double)f16, (double)f17);
                tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, (double)f15, (double)f17);
                tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, (double)f15, (double)f18);
                tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, (double)f16, (double)f18);
                tessellator29.setNormal(0.0F, 0.0F, 1.0F);
                tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.75D, 0.0D);
                tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.8125D, 0.0D);
                tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.8125D, 0.0625D);
                tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.75D, 0.0625D);
                tessellator29.setNormal(0.0F, -1.0F, 0.0F);
                tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, 0.75D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, 0.8125D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.8125D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.75D, 0.001953125D);
                tessellator29.setNormal(0.0F, 1.0F, 0.0F);
                tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.75D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.8125D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, 0.8125D, 0.001953125D);
                tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, 0.75D, 0.001953125D);
                tessellator29.setNormal(-1.0F, 0.0F, 0.0F);
                tessellator29.addVertexWithUV((double)f11, (double)f13, 0.5D, 0.751953125D, 0.0D);
                tessellator29.addVertexWithUV((double)f11, (double)f14, 0.5D, 0.751953125D, 0.0625D);
                tessellator29.addVertexWithUV((double)f11, (double)f14, -0.5D, 0.751953125D, 0.0625D);
                tessellator29.addVertexWithUV((double)f11, (double)f13, -0.5D, 0.751953125D, 0.0D);
                tessellator29.setNormal(1.0F, 0.0F, 0.0F);
                tessellator29.addVertexWithUV((double)f27, (double)f13, -0.5D, 0.751953125D, 0.0D);
                tessellator29.addVertexWithUV((double)f27, (double)f14, -0.5D, 0.751953125D, 0.0625D);
                tessellator29.addVertexWithUV((double)f27, (double)f14, 0.5D, 0.751953125D, 0.0625D);
                tessellator29.addVertexWithUV((double)f27, (double)f13, 0.5D, 0.751953125D, 0.0D);
                tessellator29.draw();
            }
        }

    }

    private void getOffset(EntityPainting entity, float x, float y) {
        int i4 = MathHelper.floor_double(entity.posX);
        int i5 = MathHelper.floor_double(entity.posY + (double)(y / 16.0F));
        int i6 = MathHelper.floor_double(entity.posZ);
        if(entity.direction == 0) {
            i4 = MathHelper.floor_double(entity.posX + (double)(x / 16.0F));
        }

        if(entity.direction == 1) {
            i6 = MathHelper.floor_double(entity.posZ - (double)(x / 16.0F));
        }

        if(entity.direction == 2) {
            i4 = MathHelper.floor_double(entity.posX - (double)(x / 16.0F));
        }

        if(entity.direction == 3) {
            i6 = MathHelper.floor_double(entity.posZ + (double)(x / 16.0F));
        }

        float f7 = this.renderManager.worldObj.getBrightness(i4, i5, i6);
        GL11.glColor3f(f7, f7, f7);
    }

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.renderThePainting((EntityPainting)entity, x, y, z, yaw, partialTicks);
    }
}
