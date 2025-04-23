package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.level.World;

public final class RenderGlobal {
	public World worldObj;
	RenderEngine renderEngine;
	int glGenList;
	IntBuffer renderIntBuffer = BufferUtils.createIntBuffer(65536);
	List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	Minecraft mc;
	public RenderBlocks globalRenderBlocks;
	public RenderManager renderManager = new RenderManager();
	private int[] dummyBuf50k = new int['\uc350'];
	public int cloudOffsetX = 0;
	private float prevSortX = -9999.0F;
	private float prevSortY = -9999.0F;
	private float prevSortZ = -9999.0F;
	public float damagePartialTime;

	public RenderGlobal(Minecraft var1, RenderEngine var2) {
		this.mc = var1;
		this.renderEngine = var2;
		this.glGenList = GL11.glGenLists(2);
		this.glRenderListBase = GL11.glGenLists(4096 << 6 << 1);
	}

	public final void loadRenderers() {
		int var1;
		if(this.worldRenderers != null) {
			for(var1 = 0; var1 < this.worldRenderers.length; ++var1) {
				this.worldRenderers[var1].stopRendering();
			}
		}

		this.renderChunksWide = this.worldObj.width / 16;
		this.renderChunksTall = this.worldObj.height / 16;
		this.renderChunksDeep = this.worldObj.length / 16;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		var1 = 0;

		int var2;
		int var4;
		for(var2 = 0; var2 < this.renderChunksWide; ++var2) {
			for(int var3 = 0; var3 < this.renderChunksTall; ++var3) {
				for(var4 = 0; var4 < this.renderChunksDeep; ++var4) {
					this.worldRenderers[(var4 * this.renderChunksTall + var3) * this.renderChunksWide + var2] = new WorldRenderer(this.worldObj, var2 << 4, var3 << 4, var4 << 4, this.glRenderListBase + var1);
					this.sortedWorldRenderers[(var4 * this.renderChunksTall + var3) * this.renderChunksWide + var2] = this.worldRenderers[(var4 * this.renderChunksTall + var3) * this.renderChunksWide + var2];
					var1 += 2;
				}
			}
		}

		for(var2 = 0; var2 < this.worldRenderersToUpdate.size(); ++var2) {
			((WorldRenderer)this.worldRenderersToUpdate.get(var2)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		RenderGlobal var9 = this;
		float var10 = 0.5F;
		GL11.glColor4f(0.5F, var10, var10, 1.0F);
		Tessellator var11 = Tessellator.instance;
		float var12 = this.worldObj.getGroundLevel();
		int var5 = 128;
		if(128 > this.worldObj.width) {
			var5 = this.worldObj.width;
		}

		if(var5 > this.worldObj.length) {
			var5 = this.worldObj.length;
		}

		int var6 = 2048 / var5;
		var11.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

		int var7;
		for(var7 = -var5 * var6; var7 < var9.worldObj.width + var5 * var6; var7 += var5) {
			for(int var8 = -var5 * var6; var8 < var9.worldObj.length + var5 * var6; var8 += var5) {
				var10 = var12;
				if(var7 >= 0 && var8 >= 0 && var7 < var9.worldObj.width && var8 < var9.worldObj.length) {
					var10 = 0.0F;
				}

				var11.addVertexWithUV((float)var7, var10, (float)(var8 + var5), 0.0F, (float)var5);
				var11.addVertexWithUV((float)(var7 + var5), var10, (float)(var8 + var5), (float)var5, (float)var5);
				var11.addVertexWithUV((float)(var7 + var5), var10, (float)var8, (float)var5, 0.0F);
				var11.addVertexWithUV((float)var7, var10, (float)var8, 0.0F, 0.0F);
			}
		}

		var11.draw();
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		var11.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

		for(var7 = 0; var7 < var9.worldObj.width; var7 += var5) {
			var11.addVertexWithUV((float)var7, 0.0F, 0.0F, 0.0F, 0.0F);
			var11.addVertexWithUV((float)(var7 + var5), 0.0F, 0.0F, (float)var5, 0.0F);
			var11.addVertexWithUV((float)(var7 + var5), var12, 0.0F, (float)var5, var12);
			var11.addVertexWithUV((float)var7, var12, 0.0F, 0.0F, var12);
			var11.addVertexWithUV((float)var7, var12, (float)var9.worldObj.length, 0.0F, var12);
			var11.addVertexWithUV((float)(var7 + var5), var12, (float)var9.worldObj.length, (float)var5, var12);
			var11.addVertexWithUV((float)(var7 + var5), 0.0F, (float)var9.worldObj.length, (float)var5, 0.0F);
			var11.addVertexWithUV((float)var7, 0.0F, (float)var9.worldObj.length, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(var7 = 0; var7 < var9.worldObj.length; var7 += var5) {
			var11.addVertexWithUV(0.0F, var12, (float)var7, 0.0F, 0.0F);
			var11.addVertexWithUV(0.0F, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.addVertexWithUV(0.0F, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.addVertexWithUV(0.0F, 0.0F, (float)var7, 0.0F, var12);
			var11.addVertexWithUV((float)var9.worldObj.width, 0.0F, (float)var7, 0.0F, var12);
			var11.addVertexWithUV((float)var9.worldObj.width, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.addVertexWithUV((float)var9.worldObj.width, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.addVertexWithUV((float)var9.worldObj.width, var12, (float)var7, 0.0F, 0.0F);
		}

		var11.draw();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		var9 = this;
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		var10 = this.worldObj.rgetGroundLevel();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		var11 = Tessellator.instance;
		var4 = 128;
		if(128 > this.worldObj.width) {
			var4 = this.worldObj.width;
		}

		if(var4 > this.worldObj.length) {
			var4 = this.worldObj.length;
		}

		var5 = 2048 / var4;
		var11.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

		for(var6 = -var4 * var5; var6 < var9.worldObj.width + var4 * var5; var6 += var4) {
			for(var7 = -var4 * var5; var7 < var9.worldObj.length + var4 * var5; var7 += var4) {
				float var13 = var10 - 0.1F;
				if(var6 < 0 || var7 < 0 || var6 >= var9.worldObj.width || var7 >= var9.worldObj.length) {
					var11.addVertexWithUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
					var11.addVertexWithUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.addVertexWithUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.addVertexWithUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.addVertexWithUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.addVertexWithUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.addVertexWithUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.addVertexWithUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
				}
			}
		}

		var11.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEndList();
		this.markBlocksForUpdate(0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
	}

	public final int sortAndRender(EntityPlayer var1, int var2) {
		float var3 = var1.posX - this.prevSortX;
		float var4 = var1.posY - this.prevSortY;
		float var5 = var1.posZ - this.prevSortZ;
		if(var3 * var3 + var4 * var4 + var5 * var5 > 64.0F) {
			this.prevSortX = var1.posX;
			this.prevSortY = var1.posY;
			this.prevSortZ = var1.posZ;
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
		}

		int var6 = 0;

		for(int var7 = 0; var7 < this.sortedWorldRenderers.length; ++var7) {
			var6 = this.sortedWorldRenderers[var7].getGLCallListForPass(this.dummyBuf50k, var6, var2);
		}

		this.renderIntBuffer.clear();
		this.renderIntBuffer.put(this.dummyBuf50k, 0, var6);
		this.renderIntBuffer.flip();
		if(this.renderIntBuffer.remaining() > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
			GL11.glCallLists(this.renderIntBuffer);
		}

		return this.renderIntBuffer.remaining();
	}

	public final void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
		var1 /= 16;
		var2 /= 16;
		var3 /= 16;
		var4 /= 16;
		var5 /= 16;
		var6 /= 16;
		if(var1 < 0) {
			var1 = 0;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var4 > this.renderChunksWide - 1) {
			var4 = this.renderChunksWide - 1;
		}

		if(var5 > this.renderChunksTall - 1) {
			var5 = this.renderChunksTall - 1;
		}

		if(var6 > this.renderChunksDeep - 1) {
			var6 = this.renderChunksDeep - 1;
		}

		while(var1 <= var4) {
			for(int var7 = var2; var7 <= var5; ++var7) {
				for(int var8 = var3; var8 <= var6; ++var8) {
					WorldRenderer var9 = this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide + var1];
					if(!var9.needsUpdate) {
						var9.needsUpdate = true;
						this.worldRenderersToUpdate.add(this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide + var1]);
					}
				}
			}

			++var1;
		}

	}
}
