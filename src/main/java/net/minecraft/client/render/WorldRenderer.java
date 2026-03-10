package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockContainer;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunkUpdates = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	private int posXClip;
	private int posYClip;
	private int posZClip;
	public boolean isInFrustrum = false;
	private boolean[] skipRenderPass = new boolean[2];
	private int posXPlus;
	private int posYPlus;
	private int posZPlus;
	public boolean needsUpdate;
	private AxisAlignedBB rendererBoundingBox;
	private RenderBlocks renderBlocks;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	public List tileEntityRenderers = new ArrayList();

	public WorldRenderer(World world, int x, int y, int z, int unusedInt, int glRenderList) {
		this.renderBlocks = new RenderBlocks(world);
		this.worldObj = world;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.glRenderList = glRenderList;
		this.posX = -999;
		this.setPosition(x, y, z);
		this.needsUpdate = false;
	}

	public final void setPosition(int x, int y, int z) {
		if(x != this.posX || y != this.posY || z != this.posZ) {
			this.setDontDraw();
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			this.posXPlus = x + this.sizeWidth / 2;
			this.posYPlus = y + this.sizeHeight / 2;
			this.posZPlus = z + this.sizeDepth / 2;
			this.posXClip = x & 511;
			this.posYClip = y & 511;
			this.posZClip = z & 511;
			this.posXMinus = x - this.posXClip;
			this.posYMinus = y - this.posYClip;
			this.posZMinus = z - this.posZClip;
			this.rendererBoundingBox = (new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + this.sizeWidth), (double)(y + this.sizeHeight), (double)(z + this.sizeDepth))).expand(2.0D, 2.0D, 2.0D);
			GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
			AxisAlignedBB x1 = new AxisAlignedBB((double)((float)this.posXClip - 2.0F), (double)((float)this.posYClip - 2.0F), (double)((float)this.posZClip - 2.0F), (double)((float)(this.posXClip + this.sizeWidth) + 2.0F), (double)((float)(this.posYClip + this.sizeHeight) + 2.0F), (double)((float)(this.posZClip + this.sizeDepth) + 2.0F));
			Tessellator y1 = Tessellator.instance;
			Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION);
			y1.drawVertex(x1.minX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.minZ);
			y1.drawVertex(x1.minX, x1.minY, x1.minZ);
			y1.drawVertex(x1.minX, x1.minY, x1.maxZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.maxZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.minY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.minY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.minX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.minX, x1.minY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.minX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.minX, x1.minY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.minZ);
			y1.drawVertex(x1.maxX, x1.maxY, x1.maxZ);
			y1.drawVertex(x1.maxX, x1.minY, x1.maxZ);
			y1.draw();
			GL11.glEndList();
			this.needsUpdate = true;
		}
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunkUpdates;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

			int i7;
			for(i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			Chunk.isLit = false;
			this.tileEntityRenderers.clear();

			for(i7 = 0; i7 < 2; ++i7) {
				boolean z8 = false;
				boolean z9 = false;
				GL11.glNewList(this.glRenderList + i7, GL11.GL_COMPILE);
				GL11.glPushMatrix();
				GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
				tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
				tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));

				for(int i10 = i2; i10 < i5; ++i10) {
					for(int i11 = i3; i11 < i6; ++i11) {
						for(int i12 = i1; i12 < i4; ++i12) {
							int i13;
							if((i13 = this.worldObj.getBlockId(i12, i10, i11)) > 0) {
								if(i7 == 0 && Block.blocksList[i13] instanceof BlockContainer) {
									TileEntity tileEntity14 = this.worldObj.getBlockTileEntity(i12, i10, i11);
									if(TileEntityRenderer.instance.hasSpecialRenderer(tileEntity14)) {
										this.tileEntityRenderers.add(tileEntity14);
									}
								}

								Block block15;
								if((block15 = Block.blocksList[i13]).getRenderBlockPass() != i7) {
									z8 = true;
								} else {
									z9 |= this.renderBlocks.renderBlockByRenderType(block15, i12, i10, i11);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glPopMatrix();
				GL11.glEndList();
				tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
				if(z9) {
					this.skipRenderPass[i7] = false;
				}

				if(!z8) {
					break;
				}
			}

			this.isChunkLit = Chunk.isLit;
		}
	}

	public final float distanceToEntitySquared(Entity entity) {
		float f2 = (float)(entity.posX - (double)this.posXPlus);
		float f3 = (float)(entity.posY - (double)this.posYPlus);
		float entity1 = (float)(entity.posZ - (double)this.posZPlus);
		return f2 * f2 + f3 * f3 + entity1 * entity1;
	}

	private void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int pass) {
		return !this.isInFrustrum ? -1 : (!this.skipRenderPass[pass] ? this.glRenderList + pass : -1);
	}

	public final void updateInFrustrum(Frustrum frustrum) {
		this.isInFrustrum = frustrum.isBoundingBoxInFrustum(this.rendererBoundingBox);
	}

	public final void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public final boolean skipAllRenderPasses() {
		return this.skipRenderPass[0] && this.skipRenderPass[1];
	}
}