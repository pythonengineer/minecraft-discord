package net.minecraft.client.render;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

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
    private int posXPlus;
    private int posYPlus;
    private int posZPlus;
	public boolean needsUpdate;
    private AxisAlignedBB rendererBoundingBox;
	private RenderBlocks renderBlocks;
    public boolean isVisible = true;
    public boolean isWaitingOnOcclusionQuery;
    public int glOcclusionQuery;

    public WorldRenderer(World var1, int var2, int var3, int var4, int var5, int var6) {
        this.renderBlocks = new RenderBlocks(var1);
        this.worldObj = var1;
        this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
        MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
        this.glRenderList = var6;
        this.posX = -999;
        this.setPosition(var2, var3, var4);
        this.needsUpdate = false;
    }

    public final void setPosition(int var1, int var2, int var3) {
        if(var1 != this.posX || var2 != this.posY || var3 != this.posZ) {
            this.setDontDraw();
            this.posX = var1;
            this.posY = var2;
            this.posZ = var3;
            this.posXPlus = var1 + this.sizeWidth / 2;
            this.posYPlus = var2 + this.sizeHeight / 2;
            this.posZPlus = var3 + this.sizeDepth / 2;
            this.rendererBoundingBox = (new AxisAlignedBB((double)var1, (double)var2, (double)var3, (double)(var1 + this.sizeWidth), (double)(var2 + this.sizeHeight), (double)(var3 + this.sizeDepth))).expand(2.0D, 2.0D, 2.0D);
            GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
            RenderItem.renderOffsetAABB(this.rendererBoundingBox);
            GL11.glEndList();
            this.needsUpdate = true;
        }
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
				GL11.glNewList(this.glRenderList + var7, GL11.GL_COMPILE);
                GL11.glPushMatrix();
                tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
                GL11.glTranslatef((float)this.posX, (float)this.posY, (float)this.posZ);
                this.renderBlocks.posX = this.posX;
                this.renderBlocks.posY = this.posY;
                this.renderBlocks.posZ = this.posZ;

                for(int var10 = var2; var10 < var5; ++var10) {
                    for(int var11 = var3; var11 < var6; ++var11) {
                        for(int var12 = var1; var12 < var4; ++var12) {
                            int var13 = this.worldObj.getBlockId(var12, var10, var11);
                            if(var13 > 0) {
                                Block var14 = Block.blocksList[var13];
                                if(var14.getRenderBlockPass() != var7) {
                                    var8 = true;
                                } else {
                                    var9 |= this.renderBlocks.renderBlockByRenderType(var14, var12, var10, var11);
                                }
                            }
                        }
                    }
                }

				tessellator.draw();
                GL11.glPopMatrix();
                GL11.glEndList();
                this.renderBlocks.posX = 0;
                this.renderBlocks.posY = 0;
                this.renderBlocks.posZ = 0;
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
        float var2 = (float)(var1.posX - (double)this.posXPlus);
        float var3 = (float)(var1.posY - (double)this.posYPlus);
        float var4 = (float)(var1.posZ - (double)this.posZPlus);
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

    public final void updateInFrustrum(Frustrum var1) {
        this.isInFrustrum = var1.isBoundingBoxInFrustrum(this.rendererBoundingBox);
    }

    public final void callOcclusionQueryList() {
        GL11.glCallList(this.glRenderList + 2);
    }

    public final boolean skipAllRenderPasses() {
        return this.skipRenderPass[0] && this.skipRenderPass[1];
    }
}
