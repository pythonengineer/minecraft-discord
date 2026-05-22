package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
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

	public abstract void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9);

	protected final void loadTexture(String textureName) {
		RenderEngine renderEngine2 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(this.renderManager.renderEngine.getTexture(textureName));
	}

	protected final void loadDownloadableImageTexture(String url, String textureName) {
		RenderEngine renderEngine3 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(this.renderManager.renderEngine.getTextureForDownloadableImage(url, textureName));
	}

	public final void setRenderManager(RenderManager renderManager) {
		this.renderManager = renderManager;
	}

	public final void renderShadow(Entity entity, double x, double y, double z, float partialTicks) {
        float f20;
        int i88;
        int i89;
        if(this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
            double d9 = this.renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ);
            float f86;
            if((f86 = (float)((1.0D - d9 / 256.0D) * (double)this.shadowOpaque)) > 0.0F) {
                float f10 = f86;
                double d18 = z;
                double d16 = y;
                double d14 = x;
                Render render85 = this;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderEngine renderEngine12 = this.renderManager.renderEngine;
                RenderEngine.bindTexture(this.renderManager.renderEngine.getTexture("%%/shadow.png"));
                World world13 = this.renderManager.worldObj;
                GL11.glDepthMask(false);
                f20 = this.shadowSize;
                double d25 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                double d27 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                double d29 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                int i87 = MathHelper.floor_double(d25 - (double)f20);
                i88 = MathHelper.floor_double(d25 + (double)f20);
                i89 = MathHelper.floor_double(d27 - (double)f20);
                int i21 = MathHelper.floor_double(d27);
                int i22 = MathHelper.floor_double(d29 - (double)f20);
                int i23 = MathHelper.floor_double(d29 + (double)f20);
                double d37 = x - d25;
                double d39 = y - d27;
                double d41 = z - d29;
                Tessellator tessellator24 = Tessellator.instance;
                Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

                for(i87 = i87; i87 <= i88; ++i87) {
                    for(int i92 = i89; i92 <= i21; ++i92) {
                        for(int i26 = i22; i26 <= i23; ++i26) {
                            int i95;
                            if((i95 = world13.getBlockId(i87, i92 - 1, i26)) > 0 && world13.getBlockLightValue(i87, i92, i26) > 3) {
                                Block block28 = Block.blocksList[i95];
                                Tessellator tessellator67 = Tessellator.instance;
                                double d68;
                                if(block28.renderAsNormalBlock() && (d68 = ((double)f10 - (d16 - ((double)i92 + d39)) / 2.0D) * 0.5D * (double)render85.renderManager.worldObj.getBrightness(i87, i92, i26)) >= 0.0D) {
                                    if(d68 > 1.0D) {
                                        d68 = 1.0D;
                                    }

                                    tessellator67.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d68);
                                    double d70 = (double)i87 + block28.minX + d37;
                                    double d72 = (double)i87 + block28.maxX + d37;
                                    double d74 = (double)i92 + block28.minY + d39 + 0.015625D;
                                    double d76 = (double)i26 + block28.minZ + d41;
                                    double d78 = (double)i26 + block28.maxZ + d41;
                                    float f80 = (float)((d14 - d70) / 2.0D / (double)f20 + 0.5D);
                                    float f81 = (float)((d14 - d72) / 2.0D / (double)f20 + 0.5D);
                                    float f82 = (float)((d18 - d76) / 2.0D / (double)f20 + 0.5D);
                                    float f83 = (float)((d18 - d78) / 2.0D / (double)f20 + 0.5D);
                                    tessellator67.addVertexWithUV(d70, d74, d76, (double)f80, (double)f82);
                                    tessellator67.addVertexWithUV(d70, d74, d78, (double)f80, (double)f83);
                                    tessellator67.addVertexWithUV(d72, d74, d78, (double)f81, (double)f83);
                                    tessellator67.addVertexWithUV(d72, d74, d76, (double)f81, (double)f82);
                                }
                            }
                        }
                    }
                }

                tessellator24.draw();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
            }
        }

        if(entity.fire > 0) {
            GL11.glDisable(GL11.GL_LIGHTING);
            i88 = Block.fire.blockIndexInTexture;
            i89 = (Block.fire.blockIndexInTexture & 15) << 4;
            int i91 = i88 & 240;
            f20 = (float)i89 / 256.0F;
            float f93 = ((float)i89 + 15.99F) / 256.0F;
            float f94 = (float)i91 / 256.0F;
            float f96 = ((float)i91 + 15.99F) / 256.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            float f97;
            GL11.glScalef(f97 = entity.width * 1.4F, f97, f97);
            this.loadTexture("/terrain.png");
            Tessellator tessellator98 = Tessellator.instance;
            float f30 = 1.0F;
            float f11 = 0.0F;
            float f90 = entity.height / entity.width;
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)f90) * 0.02F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator98.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

            while(f90 > 0.0F) {
                tessellator98.addVertexWithUV((double)(f30 - 0.5F), (double)(0.0F - f11), 0.0D, (double)f93, (double)f96);
                tessellator98.addVertexWithUV(-0.5D, (double)(0.0F - f11), 0.0D, (double)f20, (double)f96);
                tessellator98.addVertexWithUV(-0.5D, (double)(1.4F - f11), 0.0D, (double)f20, (double)f94);
                tessellator98.addVertexWithUV((double)(f30 - 0.5F), (double)(1.4F - f11), 0.0D, (double)f93, (double)f94);
                --f90;
                --f11;
                f30 *= 0.9F;
                GL11.glTranslatef(0.0F, 0.0F, -0.04F);
            }

            tessellator98.draw();
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

    }
}