package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.level.World;

public final class RenderGlobal {
	public World worldObj;
	RenderEngine renderEngine;
	int glGenList;
    private IntBuffer renderIntBuffer = BufferUtils.createIntBuffer(65536);
    private List worldRenderersToUpdate = new ArrayList();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
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

    public final void renderEntities(Vec3D var1, ClippingHelper var2, float var3) {
        EntityMap var4 = this.worldObj.entityMap;
        RenderManager var16 = this.renderManager;
        EntityPlayer var18 = (EntityPlayer)var16.worldObj.getPlayerEntity();
        var16.playerViewY = var18.prevRotationYaw + (var18.rotationYaw - var18.prevRotationYaw) * var3;

        for(int var5 = 0; var5 < var4.xSlot; ++var5) {
            float var6 = (float)((var5 << 4) - 2);
            float var7 = (float)((var5 + 1 << 4) + 2);

            for(int var8 = 0; var8 < var4.ySlot; ++var8) {
                float var9 = (float)((var8 << 4) - 2);
                float var10 = (float)((var8 + 1 << 4) + 2);

                for(int var11 = 0; var11 < var4.zSlot; ++var11) {
                    List var12 = var4.entityGrid[(var11 * var4.ySlot + var8) * var4.xSlot + var5];
                    if(var12.size() != 0) {
                        float var13 = (float)((var11 << 4) - 2);
                        float var14 = (float)((var11 + 1 << 4) + 2);
                        boolean var10000 = var2.isBoundingBoxInFrustrum(var6, var9, var13, var7, var10, var14);
                        boolean var15 = false;
                        if(var10000) {
                            float var22 = var14;
                            float var21 = var10;
                            float var20 = var7;
                            float var19 = var13;
                            float var31 = var9;
                            float var17 = var6;
                            ClippingHelper var27 = var2;
                            int var23 = 0;

                            while(true) {
                                if(var23 >= 6) {
                                    var10000 = true;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var17 + var27.frustrum[var23][1] * var31 + var27.frustrum[var23][2] * var19 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var20 + var27.frustrum[var23][1] * var31 + var27.frustrum[var23][2] * var19 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var17 + var27.frustrum[var23][1] * var21 + var27.frustrum[var23][2] * var19 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var20 + var27.frustrum[var23][1] * var21 + var27.frustrum[var23][2] * var19 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var17 + var27.frustrum[var23][1] * var31 + var27.frustrum[var23][2] * var22 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var20 + var27.frustrum[var23][1] * var31 + var27.frustrum[var23][2] * var22 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var17 + var27.frustrum[var23][1] * var21 + var27.frustrum[var23][2] * var22 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var27.frustrum[var23][0] * var20 + var27.frustrum[var23][1] * var21 + var27.frustrum[var23][2] * var22 + var27.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                ++var23;
                            }

                            boolean var24 = var10000;

                            for(int var25 = 0; var25 < var12.size(); ++var25) {
                                Entity var26 = (Entity)var12.get(var25);
                                var31 = var26.posX - var1.xCoord;
                                var19 = var26.posY - var1.yCoord;
                                var20 = var26.posZ - var1.zCoord;
                                var21 = var31 * var31 + var19 * var19 + var20 * var20;
                                Object var33 = null;
                                AxisAlignedBB var28 = var26.boundingBox;
                                var17 = var28.x1 - var28.x0;
                                var31 = var28.y1 - var28.y0;
                                float var29 = var28.z1 - var28.z0;
                                var29 = (var17 + var31 + var29) / 3.0F * 64.0F;
                                if(var21 < var29 * var29) {
                                    if(!var24) {
                                        AxisAlignedBB var30 = var26.boundingBox;
                                        var16 = null;
                                        if(!var2.isBoundingBoxInFrustrum(var30.x0, var30.y0, var30.z0, var30.x1, var30.y1, var30.z1)) {
                                            continue;
                                        }
                                    }

                                    if(!(var26 instanceof EntityPlayer)) {
                                        RenderEngine var32 = this.renderEngine;
                                        var16 = this.renderManager;
                                        var20 = var26.lastTickPosX + (var26.posX - var26.lastTickPosX) * var3;
                                        var21 = var26.lastTickPosY + (var26.posY - var26.lastTickPosY) * var3;
                                        var22 = var26.lastTickPosZ + (var26.posZ - var26.lastTickPosZ) * var3;
                                        float var34;
                                        GL11.glColor3f(var34 = var16.worldObj.getBlockLightValue((int)var20, (int)(var21 + var26.bbHeight * 2.0F / 3.0F), (int)var22), var34, var34);
                                        var16.renderEntityWithPosYaw(var26, var32, var20, var21, var22, 1.0F, var3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

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

    public final void renderAllRenderLists() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
        GL11.glCallLists(this.renderIntBuffer);
    }

    public final void updateRenderers(EntityPlayer var1) {
        Collections.sort(this.worldRenderersToUpdate, new RenderSorter(var1));
        int var5 = this.worldRenderersToUpdate.size() - 1;
        int var2 = this.worldRenderersToUpdate.size();
        if(var2 > 3) {
            var2 = 3;
        }

        for(int var3 = 0; var3 < var2; ++var3) {
            WorldRenderer var4 = (WorldRenderer)this.worldRenderersToUpdate.remove(var5 - var3);
            var4.updateRenderer();
            var4.needsUpdate = false;
        }

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

    public final void clipRenderersByFrustrum(ClippingHelper var1) {
        for(int var2 = 0; var2 < this.worldRenderers.length; ++var2) {
            this.worldRenderers[var2].updateInFrustrum(var1);
        }

    }
}
