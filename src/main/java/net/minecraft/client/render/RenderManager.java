package net.minecraft.client.render;

import java.io.IOException;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.md3.MD3Buffers;
import net.minecraft.client.model.md3.MD3Loader;
import net.minecraft.client.model.md3.MD3Model;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class RenderManager {
	private MD3Model[] model = new MD3Model[1];
	public World worldObj;
	private RenderBlocks blockRenderer;
	float playerViewY;

	public RenderManager() {
		new ModelBiped();
		this.blockRenderer = new RenderBlocks(Tessellator.instance);

		try {
			this.model[0] = new MD3Model((new MD3Loader()).loadModel("/test2.md3"));
		} catch (IOException var1) {
			var1.printStackTrace();
		}
	}

	public final void renderEntityWithPosYaw(Entity var1, RenderEngine var2, float var3, float var4, float var5, float var6, float var7) {
		float var9;
		Object var13;
		float var29;
		int var41;
		if(!(var1 instanceof EntityLiving)) {
			if(var1 instanceof EntityTNTPrimed) {
				GL11.glPushMatrix();
				GL11.glTranslatef(var3, var4, var5);
				var41 = var2.getTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var41);
				this.blockRenderer.renderBlockOnInventory(Block.tnt);
				GL11.glPopMatrix();
			} else {
				if(var1 instanceof EntityItem) {
					EntityItem var31 = (EntityItem)var1;
					GL11.glPushMatrix();
					GL11.glTranslatef(var3, var4, var5);
					GL11.glEnable(GL11.GL_NORMALIZE);
					var13 = null;
					if(var31.item.itemID > 0) {
						GL11.glPushMatrix();
						GL11.glScalef(0.25F, 0.25F, 0.25F);
						var41 = var2.getTexture("/terrain.png");
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, var41);
						var13 = null;
						this.blockRenderer.renderBlockOnInventory(Block.blocksList[var31.item.itemID]);
						GL11.glPopMatrix();
					} else {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
						var41 = var2.getTexture("/gui/items.png");
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, var41);
						Tessellator var37 = Tessellator.instance;
						int var33 = var31.item.iconIndex;
						var7 = (float)(var33 % 16 << 4) / 256.0F;
						var9 = (float)((var33 % 16 << 4) + 16) / 256.0F;
						float var32 = (float)(var33 / 16 << 4) / 256.0F;
						var29 = (float)((var33 / 16 << 4) + 16) / 256.0F;
						var3 = 0.5F;
						var4 = 0.25F;
						GL11.glRotatef(-this.playerViewY, 0.0F, 1.0F, 0.0F);
						var37.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
						var37.normal(0.0F, 1.0F, 0.0F);
						var37.addVertexWithUV(0.0F - var3, 0.0F - var4, 0.0F, var7, var29);
						var37.addVertexWithUV(1.0F - var3, 0.0F - var4, 0.0F, var9, var29);
						var37.addVertexWithUV(1.0F - var3, 1.0F - var4, 0.0F, var9, var32);
						var37.addVertexWithUV(0.0F - var3, 1.0F - var4, 0.0F, var7, var32);
						var37.draw();
					}

					GL11.glDisable(GL11.GL_NORMALIZE);
					GL11.glPopMatrix();
				}

			}
		} else {
			float var12 = var5;
			float var11 = var4;
			float var10 = var3;
			RenderManager var8 = this;
			GL11.glEnable(GL11.GL_BLEND);
			boolean var14 = false;
			var13 = null;
			var2.clampTexture = true;
			var41 = var2.getTexture("/shadow.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var41);
			var13 = null;
			var2.clampTexture = false;
			GL11.glDepthMask(false);
			var9 = 0.5F;

			for(var41 = (int)(var3 - var9); var41 <= (int)(var10 + var9); ++var41) {
				for(int var42 = (int)(var11 - 2.0F); var42 <= (int)var11; ++var42) {
					for(int var15 = (int)(var12 - var9); var15 <= (int)(var12 + var9); ++var15) {
						int var16 = var8.worldObj.getBlockId(var41, var42 - 1, var15);
						if(var16 > 0 && var8.worldObj.isHalfLit(var41, var42, var15)) {
							Block var17 = Block.blocksList[var16];
							Tessellator var24 = Tessellator.instance;
							float var18 = (1.0F - (var11 - (float)var42) / 2.0F) * 0.5F;
							if(var18 >= 0.0F) {
								GL11.glColor4f(1.0F, 1.0F, 1.0F, var18);
								var24.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
								var18 = (float)var41 + var17.minX;
								float var20 = (float)var41 + var17.maxX;
								float var21 = (float)var42 + var17.minY;
								float var25 = (float)var15 + var17.minZ;
								float var47 = (float)var15 + var17.maxZ;
								float var22 = (var10 - var18) / 2.0F / var9 + 0.5F;
								float var45 = (var10 - var20) / 2.0F / var9 + 0.5F;
								float var26 = (var12 - var25) / 2.0F / var9 + 0.5F;
								float var19 = (var12 - var47) / 2.0F / var9 + 0.5F;
								var24.addVertexWithUV(var18, var21, var25, var22, var26);
								var24.addVertexWithUV(var18, var21, var47, var22, var19);
								var24.addVertexWithUV(var20, var21, var47, var45, var19);
								var24.addVertexWithUV(var20, var21, var25, var45, var26);
								var24.draw();
							}
						}
					}
				}
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			EntityLiving var28 = (EntityLiving)var1;
			GL11.glPushMatrix();
			boolean var35 = false;

			try {
				var29 = var28.prevRenderYawOffset + (var28.renderYawOffset - var28.prevRenderYawOffset) * var7;
				var29 *= var6;
				GL11.glTranslatef(var3, var4, var5);
				var41 = var2.getTexture("/cube-nes.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var41);
				GL11.glRotatef(-var29 + 180.0F, 0.0F, 1.0F, 0.0F);
				boolean var30 = false;
				var29 = 0.02F;
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glScalef(var29, -var29, var29);
				boolean var34 = false;
				GL11.glEnable(GL11.GL_NORMALIZE);
				MD3Model var10000 = this.model[0];
				boolean var38 = false;
				MD3Model var36 = var10000;
				if(var36.displayList == 0) {
					MD3Model var40 = var36;
					var36.displayList = GL11.glGenLists(var36.vertices.totalFrames);

					for(int var39 = 0; var39 < var40.vertices.totalFrames; ++var39) {
						GL11.glNewList(var40.displayList + var39, GL11.GL_COMPILE);
		                Tessellator tessellator = Tessellator.instance;

						for(var41 = 0; var41 < var40.vertices.buffersMD3.length; ++var41) {
							MD3Buffers var44 = var40.vertices.buffersMD3[var41];
							boolean var46 = false;
							MD3Buffers var43 = var44;
							var43.triangles.position(0).limit(var43.triangles.capacity());
							var43.xBuffer.position(0).limit(var43.xBuffer.capacity());
							var43.vertices.clear().position(0 * var43.verts * 3).limit(1 * var43.verts * 3);
							var43.normals.clear().position(0 * var43.verts * 3).limit(1 * var43.verts * 3);
							var43.vertices.position(0);
							var43.triangles.position(0);
							var43.normals.position(0);
							var43.xBuffer.position(0);
		                    tessellator.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
		                    for (int i = 0; i < var43.verts; ++i) {
		                        tessellator.normal(var43.normals.get(), var43.normals.get(), var43.normals.get());
		                        tessellator.addVertexWithUV(var43.vertices.get(), var43.vertices.get(), var43.vertices.get(), var43.xBuffer.get(), var43.xBuffer.get());
		                    }
		                    GL11.glDrawElements(GL11.GL_TRIANGLES, var43.triangles);
		                    tessellator.draw();
						}

						GL11.glEndList();
					}
				}

				GL11.glCallList(var36.displayList);
				GL11.glDisable(GL11.GL_NORMALIZE);
			} catch (Exception var27) {
				var27.printStackTrace();
			}

			GL11.glPopMatrix();
		}
	}
}
