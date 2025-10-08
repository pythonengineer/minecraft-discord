package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public abstract class Render {
	protected RenderManager renderManager;
	protected float shadowSize;
	protected float shadowOpaque;

	public Render() {
		new ModelBiped();
		new RenderBlocks();
		this.shadowSize = 0.0F;
        this.shadowOpaque = 1.0F;
	}

    public abstract void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9);

    protected final void loadTexture(String var1) {
        RenderEngine var2 = this.renderManager.renderEngine;
        RenderEngine.bindTexture(var2.getTexture(var1));
    }

    protected final void loadDownloadableImageTexture(String var1, String var2) {
        RenderEngine var3 = this.renderManager.renderEngine;
        RenderEngine.bindTexture(var3.getTextureForDownloadableImage(var1, var2));
    }

    public static void renderOffsetAABB(AxisAlignedBB var0) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator var1 = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var1.startDrawingQuads(DefaultVertexFormats.POSITION_NORMAL);
        var1.normal(0.0F, 0.0F, -1.0F);
        var1.addVertex(var0.minX, var0.maxY, var0.minZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
        var1.addVertex(var0.maxX, var0.minY, var0.minZ);
        var1.addVertex(var0.minX, var0.minY, var0.minZ);
        var1.normal(0.0F, 0.0F, 1.0F);
        var1.addVertex(var0.minX, var0.minY, var0.maxZ);
        var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
        var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
        var1.normal(0.0F, -1.0F, 0.0F);
        var1.addVertex(var0.minX, var0.minY, var0.minZ);
        var1.addVertex(var0.maxX, var0.minY, var0.minZ);
        var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
        var1.addVertex(var0.minX, var0.minY, var0.maxZ);
        var1.normal(0.0F, 1.0F, 0.0F);
        var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
        var1.addVertex(var0.minX, var0.maxY, var0.minZ);
        var1.normal(-1.0F, 0.0F, 0.0F);
        var1.addVertex(var0.minX, var0.minY, var0.maxZ);
        var1.addVertex(var0.minX, var0.maxY, var0.maxZ);
        var1.addVertex(var0.minX, var0.maxY, var0.minZ);
        var1.addVertex(var0.minX, var0.minY, var0.minZ);
        var1.normal(1.0F, 0.0F, 0.0F);
        var1.addVertex(var0.maxX, var0.minY, var0.minZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.minZ);
        var1.addVertex(var0.maxX, var0.maxY, var0.maxZ);
        var1.addVertex(var0.maxX, var0.minY, var0.maxZ);
        var1.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

	public final void setRenderManager(RenderManager var1) {
		this.renderManager = var1;
	}

    public final void doRenderShadowAndFire(Entity var1, double var2, double var4, double var6, float var8) {
        float var11;
        int var53;
        if(this.shadowSize > 0.0F) {
            double var9 = this.renderManager.getDistanceToCamera(var2, var4, var6);
            var8 = (float)((1.0D - var9 / 256.0D) * (double)this.shadowOpaque);
            if(var8 > 0.0F) {
                float var19 = var8;
                double var17 = var6;
                double var15 = var4;
                double var13 = var2;
                Render var51 = this;
                GL11.glEnable(GL11.GL_BLEND);
                RenderEngine var52 = this.renderManager.renderEngine;
                RenderEngine.bindTexture(var52.getTexture("%%/shadow.png"));
                World var10 = this.renderManager.worldObj;
                GL11.glDepthMask(false);
                var11 = this.shadowSize;
                var53 = MathHelper.floor_double(var2 - (double)var11);
                int var12 = MathHelper.floor_double(var2 + (double)var11);
                int var20 = MathHelper.floor_double(var4 - (double)var11);
                int var21 = MathHelper.floor_double(var4);
                int var22 = MathHelper.floor_double(var6 - (double)var11);
                int var23 = MathHelper.floor_double(var6 + (double)var11);

                for(var53 = var53; var53 <= var12; ++var53) {
                    for(int var24 = var20; var24 <= var21; ++var24) {
                        for(int var25 = var22; var25 <= var23; ++var25) {
                            int var26 = var10.getBlockId(var53, var24 - 1, var25);
                            if(var26 > 0) {
                                Block var27 = Block.blocksList[var26];
                                Tessellator var33 = Tessellator.instance;
                                double var48 = ((double)var19 - (var15 - (double)var24) / 2.0D) * 0.5D * (double)var51.renderManager.worldObj.getBrightness(var53, var24, var25);
                                if(var48 >= 0.0D) {
                                    GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)var48);
                                    var33.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                                    float var64 = (float)var53 + var27.minX;
                                    float var28 = (float)var53 + var27.maxX;
                                    float var29 = (float)var24 + var27.minY;
                                    float var31 = (float)var25 + var27.minZ;
                                    float var65 = (float)var25 + var27.maxZ;
                                    float var30 = (float)((var13 - (double)var64) / 2.0D / (double)var11 + 0.5D);
                                    float var34 = (float)((var13 - (double)var28) / 2.0D / (double)var11 + 0.5D);
                                    float var35 = (float)((var17 - (double)var31) / 2.0D / (double)var11 + 0.5D);
                                    float var32 = (float)((var17 - (double)var65) / 2.0D / (double)var11 + 0.5D);
                                    var33.addVertexWithUV(var64, var29, var31, var30, var35);
                                    var33.addVertexWithUV(var64, var29, var65, var30, var32);
                                    var33.addVertexWithUV(var28, var29, var65, var34, var32);
                                    var33.addVertexWithUV(var28, var29, var31, var34, var35);
                                    var33.draw();
                                }
                            }
                        }
                    }
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
            }
        }

        if(var1.fire > 0) {
            GL11.glDisable(GL11.GL_LIGHTING);
            int var50 = Block.fire.blockIndexInTexture;
            var53 = (var50 & 15) << 4;
            int var54 = var50 & 240;
            var11 = (float)var53 / 256.0F;
            float var55 = ((float)var53 + 15.99F) / 256.0F;
            float var56 = (float)var54 / 256.0F;
            float var58 = ((float)var54 + 15.99F) / 256.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)var2, (float)var4, (float)var6);
            float var59 = var1.width * 1.4F;
            GL11.glScalef(var59, var59, var59);
            this.loadTexture("/terrain.png");
            Tessellator var60 = Tessellator.instance;
            float var61 = 1.0F;
            float var62 = 0.0F;
            float var63 = var1.height / var1.width;
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)var63) * 0.02F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var60.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

            while(var63 > 0.0F) {
                var60.addVertexWithUV(var61 - 0.5F, 0.0F - var62, 0.0F, var55, var58);
                var60.addVertexWithUV(-0.5F, 0.0F - var62, 0.0F, var11, var58);
                var60.addVertexWithUV(-0.5F, 1.4F - var62, 0.0F, var11, var56);
                var60.addVertexWithUV(var61 - 0.5F, 1.4F - var62, 0.0F, var55, var56);
                --var63;
                --var62;
                var61 *= 0.9F;
                GL11.glTranslatef(0.0F, 0.0F, -0.04F);
            }

            var60.draw();
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

    }
}
