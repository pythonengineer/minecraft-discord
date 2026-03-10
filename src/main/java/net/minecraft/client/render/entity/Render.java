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

	public final void doRenderShadowAndFire(Entity entity, double x, double y, double z, float partialTicks) {
		int i68;
		float f11;
		if(this.shadowSize > 0.0F) {
			double d9 = this.renderManager.getDistanceToCamera(x, y, z);
			if((partialTicks = (float)((1.0D - d9 / 256.0D) * (double)this.shadowOpaque)) > 0.0F) {
				float f19 = partialTicks;
				double d17 = z;
				double d15 = y;
				double d13 = x;
				Render render66 = this;
				GL11.glEnable(GL11.GL_BLEND);
				RenderEngine renderEngine67 = this.renderManager.renderEngine;
				RenderEngine.bindTexture(this.renderManager.renderEngine.getTexture("%%/shadow.png"));
				World world10 = this.renderManager.worldObj;
				GL11.glDepthMask(false);
				f11 = this.shadowSize;
				i68 = MathHelper.floor_double(x - (double)f11);
				int i12 = MathHelper.floor_double(x + (double)f11);
				int i20 = MathHelper.floor_double(y - (double)f11);
				int i21 = MathHelper.floor_double(y);
				int i22 = MathHelper.floor_double(z - (double)f11);
				int i23 = MathHelper.floor_double(z + (double)f11);

				for(i68 = i68; i68 <= i12; ++i68) {
					for(int i24 = i20; i24 <= i21; ++i24) {
						for(int i25 = i22; i25 <= i23; ++i25) {
							int i26;
							if((i26 = world10.getBlockId(i68, i24 - 1, i25)) > 0 && world10.getBlockLightValue(i68, i24, i25) > 3) {
								Block block27 = Block.blocksList[i26];
								Tessellator tessellator33 = Tessellator.instance;
								double d48;
								if((d48 = ((double)f19 - (d15 - (double)i24) / 2.0D) * 0.5D * (double)render66.renderManager.worldObj.getBrightness(i68, i24, i25)) >= 0.0D) {
									GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)d48);
									tessellator33.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
									double d50 = (double)i68 + block27.minX;
									double d52 = (double)i68 + block27.maxX;
									double d54 = (double)i24 + block27.minY;
									double d56 = (double)i25 + block27.minZ;
									double d58 = (double)i25 + block27.maxZ;
									float f79 = (float)((d13 - d50) / 2.0D / (double)f11 + 0.5D);
									float f80 = (float)((d13 - d52) / 2.0D / (double)f11 + 0.5D);
									float f28 = (float)((d17 - d56) / 2.0D / (double)f11 + 0.5D);
									float f29 = (float)((d17 - d58) / 2.0D / (double)f11 + 0.5D);
									tessellator33.addVertexWithUV(d50, d54, d56, (double)f79, (double)f28);
									tessellator33.addVertexWithUV(d50, d54, d58, (double)f79, (double)f29);
									tessellator33.addVertexWithUV(d52, d54, d58, (double)f80, (double)f29);
									tessellator33.addVertexWithUV(d52, d54, d56, (double)f80, (double)f28);
									tessellator33.draw();
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

		if(entity.fire > 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
			int i65 = Block.fire.blockIndexInTexture;
			i68 = (Block.fire.blockIndexInTexture & 15) << 4;
			int i69 = i65 & 240;
			f11 = (float)i68 / 256.0F;
			float f70 = ((float)i68 + 15.99F) / 256.0F;
			float f71 = (float)i69 / 256.0F;
			float f73 = ((float)i69 + 15.99F) / 256.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x, (float)y, (float)z);
			float f74;
			GL11.glScalef(f74 = entity.width * 1.4F, f74, f74);
			this.loadTexture("/terrain.png");
			Tessellator tessellator75 = Tessellator.instance;
			float f76 = 1.0F;
			float f77 = 0.0F;
			float f78 = entity.height / entity.width;
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)f78) * 0.02F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tessellator75.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

			while(f78 > 0.0F) {
				tessellator75.addVertexWithUV((double)(f76 - 0.5F), (double)(0.0F - f77), 0.0D, (double)f70, (double)f73);
				tessellator75.addVertexWithUV(-0.5D, (double)(0.0F - f77), 0.0D, (double)f11, (double)f73);
				tessellator75.addVertexWithUV(-0.5D, (double)(1.4F - f77), 0.0D, (double)f11, (double)f71);
				tessellator75.addVertexWithUV((double)(f76 - 0.5F), (double)(1.4F - f77), 0.0D, (double)f70, (double)f71);
				--f78;
				--f77;
				f76 *= 0.9F;
				GL11.glTranslatef(0.0F, 0.0F, -0.04F);
			}

			tessellator75.draw();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}

	}
}