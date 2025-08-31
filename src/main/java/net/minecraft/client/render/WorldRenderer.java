package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
    public boolean isInFrustrum = false;
	private boolean[] skipRenderPass = new boolean[2];
	public boolean needsUpdate;
	private RenderBlocks renderBlocks;

	public WorldRenderer(World var1, int var2, int var3, int var4, int var5, int var6) {
		this.renderBlocks = new RenderBlocks(Tessellator.instance, var1);
		this.worldObj = var1;
		this.posX = var2;
		this.posY = var3;
		this.posZ = var4;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.glRenderList = var6;
		this.setDontDraw();
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int var1 = this.posX;
			int var2 = this.posY;
			int var3 = this.posZ;
			int var4 = this.posX + this.sizeWidth;
			int var5 = this.posY + this.sizeHeight;
			int var6 = this.posZ + this.sizeDepth;

			int var7;
			for(var7 = 0; var7 < 2; ++var7) {
				this.skipRenderPass[var7] = true;
			}

			for(var7 = 0; var7 < 2; ++var7) {
				boolean var8 = false;
				boolean var9 = false;
				tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
				GL11.glNewList(this.glRenderList + var7, GL11.GL_COMPILE);

				for(int var10 = var1; var10 < var4; ++var10) {
					for(int var11 = var2; var11 < var5; ++var11) {
						for(int var12 = var3; var12 < var6; ++var12) {
							int var13 = this.worldObj.getBlockId(var10, var11, var12);
							if(var13 > 0) {
								Block var14 = Block.blocksList[var13];
								if(var14.getRenderBlockPass() != var7) {
									var8 = true;
								} else {
									var9 |= this.renderBlocks.renderBlockByRenderType(var14, var10, var11, var12);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glEndList();
				if(var9) {
					this.skipRenderPass[var7] = false;
				}

				if(!var8) {
					break;
				}
			}

		}
	}

	public final float distanceToEntitySquared(EntityLiving var1) {
		float var2 = var1.posX - (float)this.posX;
		float var3 = var1.posY - (float)this.posY;
		float var4 = var1.posZ - (float)this.posZ;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	private void setDontDraw() {
		for(int var1 = 0; var1 < 2; ++var1) {
			this.skipRenderPass[var1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int[] var1, int var2, int var3) {
        if(!this.isInFrustrum) {
			return var2;
		} else {
			if(!this.skipRenderPass[var3]) {
				var1[var2++] = this.glRenderList + var3;
			}

			return var2;
		}
	}

	public final void updateInFrustrum(ClippingHelper var1) {
	    this.isInFrustrum = var1.checkInFrustrum((float)this.posX, (float)this.posY, (float)this.posZ, (float)(this.posX + this.sizeWidth), (float)(this.posY + this.sizeHeight), (float)(this.posZ + this.sizeDepth));
	}
}
