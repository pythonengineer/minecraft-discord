package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.camera.Frustum;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.ChunkCache;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockContainer;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;

public class WorldRenderer {
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
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	private int posXClip;
	private int posYClip;
	private int posZClip;
	public boolean isInFrustum = false;
    public boolean[] skipRenderPass = new boolean[2];
	private int posXPlus;
	private int posYPlus;
	private int posZPlus;
    public float rendererRadius;
	public boolean needsUpdate;
	private AxisAlignedBB rendererBoundingBox;
    public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
    private boolean isInitialized = false;
	public List tileEntityRenderers = new ArrayList();
    private List tileEntities;

	public WorldRenderer(World world, List tileEntities, int x, int y, int z, int unusedInt, int glRenderList) {
		this.worldObj = world;
        this.tileEntities = tileEntities;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		this.rendererRadius = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.glRenderList = glRenderList;
		this.posX = -999;
		this.setPosition(x, y, z);
		this.needsUpdate = false;
	}

	public void setPosition(int x, int y, int z) {
		if(x != this.posX || y != this.posY || z != this.posZ) {
			this.setDontDraw();
			this.posX = x;
			this.posY = y;
			this.posZ = z;
			this.posXPlus = x + this.sizeWidth / 2;
			this.posYPlus = y + this.sizeHeight / 2;
			this.posZPlus = z + this.sizeDepth / 2;
			this.posXClip = x & 1023;
			this.posYClip = y;
			this.posZClip = z & 1023;
			this.posXMinus = x - this.posXClip;
			this.posYMinus = y - this.posYClip;
			this.posZMinus = z - this.posZClip;
            float f4 = 2.0F;
            this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)x - 2.0F), (double)((float)y - 2.0F), (double)((float)z - 2.0F), (double)((float)(x + this.sizeWidth) + 2.0F), (double)((float)(y + this.sizeHeight) + 2.0F), (double)((float)(z + this.sizeDepth) + 2.0F));
            GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
            RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - f4), (double)((float)this.posYClip - f4), (double)((float)this.posZClip - f4), (double)((float)(this.posXClip + this.sizeWidth) + f4), (double)((float)(this.posYClip + this.sizeHeight) + f4), (double)((float)(this.posZClip + this.sizeDepth) + f4)));
			GL11.glEndList();
            this.markDirty();
		}
	}

    private void setupGLTranslation() {
        GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
    }

	public void updateRenderer() {
        if(this.needsUpdate) {
			++chunksUpdated;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

            for(int i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			Chunk.isLit = false;
            HashSet hashSet19 = new HashSet();
            hashSet19.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
            byte b8 = 1;
            ChunkCache chunkCache18 = new ChunkCache(this.worldObj, i1 - b8, i2 - b8, i3 - b8, i4 + b8, i5 + b8, i6 + b8);
            RenderBlocks renderBlocks8 = new RenderBlocks(chunkCache18);

            for(int i9 = 0; i9 < 2; ++i9) {
                boolean z10 = false;
                boolean z11 = false;
                boolean z12 = false;

                for(int i13 = i2; i13 < i5; ++i13) {
                    for(int i14 = i3; i14 < i6; ++i14) {
                        for(int i15 = i1; i15 < i4; ++i15) {
                            int i16 = chunkCache18.getBlockId(i15, i13, i14);
                            if(i16 > 0) {
                                if(!z12) {
                                    z12 = true;
                                    GL11.glNewList(this.glRenderList + i9, GL11.GL_COMPILE);
                                    GL11.glPushMatrix();
                                    this.setupGLTranslation();
                                    GL11.glTranslatef((float)(-this.sizeDepth) / 2.0F, (float)(-this.sizeHeight) / 2.0F, (float)(-this.sizeDepth) / 2.0F);
                                    GL11.glScalef(1.000001F, 1.000001F, 1.000001F);
                                    GL11.glTranslatef((float)this.sizeDepth / 2.0F, (float)this.sizeHeight / 2.0F, (float)this.sizeDepth / 2.0F);
                                    tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
                                    tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
                                }

                                if(i9 == 0 && Block.blocksList[i16] instanceof BlockContainer) {
                                    TileEntity tileEntity17 = chunkCache18.getBlockTileEntity(i15, i13, i14);
                                    if(TileEntityRenderer.instance.hasSpecialRenderer(tileEntity17)) {
                                        this.tileEntityRenderers.add(tileEntity17);
                                    }
                                }

                                Block block19 = Block.blocksList[i16];
                                int i20 = block19.getRenderBlockPass();
                                if(i20 != i9) {
                                    z10 = true;
                                } else if(i20 == i9) {
                                    z11 |= renderBlocks8.renderBlockByRenderType(block19, i15, i13, i14);
                                }
                            }
                        }
                    }
                }

                if(z12) {
                    tessellator.draw();
                    GL11.glPopMatrix();
                    GL11.glEndList();
                    tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                } else {
                    z11 = false;
                }

                if(z11) {
                    this.skipRenderPass[i9] = false;
                }

                if(!z10) {
                    break;
                }
            }

            HashSet hashSet20 = new HashSet();
            hashSet20.addAll(this.tileEntityRenderers);
            hashSet20.removeAll(hashSet19);
            this.tileEntities.addAll(hashSet20);
            hashSet19.removeAll(this.tileEntityRenderers);
            this.tileEntities.removeAll(hashSet19);
            this.isChunkLit = Chunk.isLit;
            this.isInitialized = true;
		}
	}

	public float distanceToEntitySquared(Entity entity) {
		float f2 = (float)(entity.posX - (double)this.posXPlus);
		float f3 = (float)(entity.posY - (double)this.posYPlus);
		float entity1 = (float)(entity.posZ - (double)this.posZPlus);
		return f2 * f2 + f3 * f3 + entity1 * entity1;
	}

	private void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

        this.isInFrustum = false;
        this.isInitialized = false;
	}

	public void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int pass) {
		return !this.isInFrustum ? -1 : (!this.skipRenderPass[pass] ? this.glRenderList + pass : -1);
	}

	public void updateInFrustum(Frustum frustum) {
		this.isInFrustum = frustum.isBoundingBoxInFrustum(this.rendererBoundingBox);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
        return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1];
    }

    public void markDirty() {
        this.needsUpdate = true;
    }
}
