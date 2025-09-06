package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;

public abstract class Render {
	protected RenderManager renderManager;
	protected float shadowSize;

	public Render() {
		new ModelBiped();
		new RenderBlocks(Tessellator.instance);
		this.shadowSize = 0.0F;
	}

	public abstract void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6);

	protected final void loadTexture(String var1) {
		RenderEngine var2 = this.renderManager.renderEngine;
		int var3 = var2.getTexture(var1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
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

	public final void renderShadow(Entity var1, float var2, float var3, float var4, float var5) {
		float var15;
		float var17;
		float var18;
		float var19;
		float var32;
		if(this.shadowSize > 0.0F) {
			float var8 = var4;
			float var7 = var3;
			float var6 = var2;
			Render var25 = this;
			GL11.glEnable(GL11.GL_BLEND);
			RenderEngine var9 = this.renderManager.renderEngine;
			var9.setClampTexture(true);
			int var14 = var9.getTexture("/shadow.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var14);
			var9.setClampTexture(false);
			World var10 = this.renderManager.worldObj;
			GL11.glDepthMask(false);
			float var26 = this.shadowSize;

			for(int var11 = (int)(var2 - var26); var11 <= (int)(var6 + var26); ++var11) {
				for(int var12 = (int)(var7 - 2.0F); var12 <= (int)var7; ++var12) {
					for(int var13 = (int)(var8 - var26); var13 <= (int)(var8 + var26); ++var13) {
						var14 = var10.getBlockId(var11, var12 - 1, var13);
						if(var14 > 0 && var10.isHalfLit(var11, var12, var13)) {
							Block var16 = Block.blocksList[var14];
							Tessellator var23 = Tessellator.instance;
							var32 = (1.0F - (var7 - (float)var12) / 2.0F) * 0.5F * var25.renderManager.worldObj.getBlockLightValue(var11, var12, var13);
							if(var32 >= 0.0F) {
								GL11.glColor4f(1.0F, 1.0F, 1.0F, var32);
								var23.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
								var32 = (float)var11 + var16.minX;
								var15 = (float)var11 + var16.maxX;
								var19 = (float)var12 + var16.minY;
								float var20 = (float)var13 + var16.minZ;
								float var33 = (float)var13 + var16.maxZ;
								float var21 = (var6 - var32) / 2.0F / var26 + 0.5F;
								var17 = (var6 - var15) / 2.0F / var26 + 0.5F;
								float var24 = (var8 - var20) / 2.0F / var26 + 0.5F;
								var18 = (var8 - var33) / 2.0F / var26 + 0.5F;
								var23.addVertexWithUV(var32, var19, var20, var21, var24);
								var23.addVertexWithUV(var32, var19, var33, var21, var18);
								var23.addVertexWithUV(var15, var19, var33, var17, var18);
								var23.addVertexWithUV(var15, var19, var20, var17, var24);
								var23.draw();
							}
						}
					}
				}
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
		}

		if(var1.fire > 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
			int var27 = Block.fire.blockIndexInTexture;
			int var28 = (var27 & 15) << 4;
			var27 &= 240;
			float var29 = (float)var28 / 256.0F;
			float var30 = ((float)var28 + 15.99F) / 256.0F;
			float var31 = (float)var27 / 256.0F;
			var32 = ((float)var27 + 15.99F) / 256.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(var2, var3, var4);
			var15 = var1.width * 1.4F;
			GL11.glScalef(var15, var15, var15);
			this.loadTexture("/terrain.png");
			Tessellator var34 = Tessellator.instance;
			var17 = 1.0F;
			var18 = 0.0F;
			var19 = var1.height / var1.width;
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)var19) * 0.02F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var34.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

			while(var19 > 0.0F) {
				var34.addVertexWithUV(-0.5F, 0.0F - var18, 0.0F, var29, var32);
				var34.addVertexWithUV(var17 - 0.5F, 0.0F - var18, 0.0F, var30, var32);
				var34.addVertexWithUV(var17 - 0.5F, 1.4F - var18, 0.0F, var30, var31);
				var34.addVertexWithUV(-0.5F, 1.4F - var18, 0.0F, var29, var31);
				--var19;
				--var18;
				var17 *= 0.9F;
				GL11.glTranslatef(0.0F, 0.0F, -0.04F);
			}

			var34.draw();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}

	}
}
