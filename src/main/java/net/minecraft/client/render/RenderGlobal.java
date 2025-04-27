package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.IWorldAccess;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.level.World;

public final class RenderGlobal implements IWorldAccess {
    private World worldObj;
    private RenderEngine renderEngine;
    private int glGenList;
    private IntBuffer renderIntBuffer = BufferUtils.createIntBuffer(65536);
    private List worldRenderersToUpdate = new ArrayList();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
    private Minecraft mc;
    private RenderBlocks globalRenderBlocks;
	public RenderManager renderManager = new RenderManager();
	private int[] dummyBuf50k = new int['\uc350'];
    private int cloudOffsetX = 0;
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

    public final void changeWorld(World var1) {
        if(this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.renderManager.changeWorld(var1);
        this.worldObj = var1;
        this.globalRenderBlocks = new RenderBlocks(Tessellator.instance, var1);
        if(var1 != null) {
            var1.addWorldAccess(this);
            this.loadRenderers();
        }

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
					this.worldRenderers[(var4 * this.renderChunksTall + var3) * this.renderChunksWide + var2] = new WorldRenderer(this.worldObj, var2 << 4, var3 << 4, var4 << 4, 16, this.glRenderListBase + var1);
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
		RenderGlobal var11 = this;
        Tessellator var12 = Tessellator.instance;
        float var13 = (float)this.worldObj.getGroundLevel();
        var4 = 128;
        if(128 > this.worldObj.width) {
            var4 = this.worldObj.width;
        }

        if(var4 > this.worldObj.length) {
            var4 = this.worldObj.length;
        }

        int var5 = 2048 / var4;
        var12.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

        for(int var6 = -var4 * var5; var6 < var11.worldObj.width + var4 * var5; var6 += var4) {
            for(int var7 = -var4 * var5; var7 < var11.worldObj.length + var4 * var5; var7 += var4) {
                if(var13 < 0.0F || var6 < 0 || var7 < 0 || var6 >= var11.worldObj.width || var7 >= var11.worldObj.length) {
                    var12.addVertexWithUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
                    var12.addVertexWithUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
                    var12.addVertexWithUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
                    var12.addVertexWithUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
                }
            }
        }

        var12.draw();
        GL11.glEndList();
        GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
        var11 = this;
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        float var14 = (float)this.worldObj.getWaterLevel();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator var15 = Tessellator.instance;
        var4 = 128;
        if(128 > this.worldObj.width) {
            var4 = this.worldObj.width;
        }

        if(var4 > this.worldObj.length) {
            var4 = this.worldObj.length;
        }

        var5 = 2048 / var4;
        var15.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        float var16 = Block.waterMoving.minX;
        float var17 = Block.waterMoving.minZ;

        for(int var8 = -var4 * var5; var8 < var11.worldObj.width + var4 * var5; var8 += var4) {
            for(int var9 = -var4 * var5; var9 < var11.worldObj.length + var4 * var5; var9 += var4) {
                float var10 = var14 + Block.waterMoving.minY;
                if(var14 < 0.0F || var8 < 0 || var9 < 0 || var8 >= var11.worldObj.width || var9 >= var11.worldObj.length) {
                    var15.addVertexWithUV((float)var8 + var16, var10, (float)(var9 + var4) + var17, 0.0F, (float)var4);
                    var15.addVertexWithUV((float)(var8 + var4) + var16, var10, (float)(var9 + var4) + var17, (float)var4, (float)var4);
                    var15.addVertexWithUV((float)(var8 + var4) + var16, var10, (float)var9 + var17, (float)var4, 0.0F);
                    var15.addVertexWithUV((float)var8 + var16, var10, (float)var9 + var17, 0.0F, 0.0F);
                    var15.addVertexWithUV((float)var8 + var16, var10, (float)var9 + var17, 0.0F, 0.0F);
                    var15.addVertexWithUV((float)(var8 + var4) + var16, var10, (float)var9 + var17, (float)var4, 0.0F);
                    var15.addVertexWithUV((float)(var8 + var4) + var16, var10, (float)(var9 + var4) + var17, (float)var4, (float)var4);
                    var15.addVertexWithUV((float)var8 + var16, var10, (float)(var9 + var4) + var17, 0.0F, (float)var4);
                }
            }
        }

        var15.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEndList();
		this.markBlocksForUpdate(0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
	}

    public final void renderEntities(Vec3D var1, ClippingHelper var2, float var3) {
        EntityMap var4 = this.worldObj.entityMap;
        this.renderManager.setPlayerViewY(var3);

        for(int var5 = 0; var5 < var4.width; ++var5) {
            float var6 = (float)((var5 << 4) - 2);
            float var7 = (float)((var5 + 1 << 4) + 2);

            for(int var8 = 0; var8 < var4.depth; ++var8) {
                float var9 = (float)((var8 << 4) - 2);
                float var10 = (float)((var8 + 1 << 4) + 2);

                for(int var11 = 0; var11 < var4.height; ++var11) {
                    List var12 = var4.entityGrid[(var11 * var4.depth + var8) * var4.width + var5];
                    if(var12.size() != 0) {
                        float var13 = (float)((var11 << 4) - 2);
                        float var14 = (float)((var11 + 1 << 4) + 2);
                        boolean var15 = var2.isVisible(var6, var9, var13, var7, var10, var14);
                        if(var15) {
                            float var22 = var14;
                            float var21 = var10;
                            float var20 = var7;
                            float var19 = var13;
                            float var18 = var9;
                            float var17 = var6;
                            ClippingHelper var16 = var2;
                            int var23 = 0;

                            boolean var10000;
                            while(true) {
                                if(var23 >= 6) {
                                    var10000 = true;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var17 + var16.frustrum[var23][1] * var18 + var16.frustrum[var23][2] * var19 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var20 + var16.frustrum[var23][1] * var18 + var16.frustrum[var23][2] * var19 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var17 + var16.frustrum[var23][1] * var21 + var16.frustrum[var23][2] * var19 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var20 + var16.frustrum[var23][1] * var21 + var16.frustrum[var23][2] * var19 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var17 + var16.frustrum[var23][1] * var18 + var16.frustrum[var23][2] * var22 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var20 + var16.frustrum[var23][1] * var18 + var16.frustrum[var23][2] * var22 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var17 + var16.frustrum[var23][1] * var21 + var16.frustrum[var23][2] * var22 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                if(var16.frustrum[var23][0] * var20 + var16.frustrum[var23][1] * var21 + var16.frustrum[var23][2] * var22 + var16.frustrum[var23][3] <= 0.0F) {
                                    var10000 = false;
                                    break;
                                }

                                ++var23;
                            }

                            boolean var24 = var10000;

                            for(int var25 = 0; var25 < var12.size(); ++var25) {
                                Entity var26 = (Entity)var12.get(var25);
                                var18 = var26.posX - var1.xCoord;
                                var19 = var26.posY - var1.yCoord;
                                var20 = var26.posZ - var1.zCoord;
                                var21 = var18 * var18 + var19 * var19 + var20 * var20;
                                AxisAlignedBB var27 = var26.boundingBox;
                                var17 = var27.maxX - var27.minX;
                                var18 = var27.maxY - var27.minY;
                                float var28 = var27.maxZ - var27.minZ;
                                var28 = (var17 + var18 + var28) / 3.0F;
                                var28 *= 64.0F;
                                if(var21 < var28 * var28) {
                                    if(!var24) {
                                        AxisAlignedBB var29 = var26.boundingBox;
                                        if(!var2.isVisible(var29.minX, var29.minY, var29.minZ, var29.maxX, var29.maxY, var29.maxZ)) {
                                            continue;
                                        }
                                    }

                                    if(!(var26 instanceof EntityPlayer)) {
                                        this.renderManager.renderShadow(var26, this.renderEngine, var3);
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

    public final void updateClouds() {
        ++this.cloudOffsetX;
    }

    public final void renderSky(float var1) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var2 = (float)(this.worldObj.cloudColor >> 16 & 255) / 255.0F;
        float var3 = (float)(this.worldObj.cloudColor >> 8 & 255) / 255.0F;
        float var4 = (float)(this.worldObj.cloudColor & 255) / 255.0F;
        if(this.mc.options.anaglyph) {
            float var5 = (var2 * 30.0F + var3 * 59.0F + var4 * 11.0F) / 100.0F;
            var3 = (var2 * 30.0F + var3 * 70.0F) / 100.0F;
            var4 = (var2 * 30.0F + var4 * 70.0F) / 100.0F;
            var2 = var5;
            var3 = var3;
            var4 = var4;
        }

        Tessellator var9 = Tessellator.instance;
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
        float var6 = (float)this.worldObj.cloudHeight;
        var1 = ((float)this.cloudOffsetX + var1) * (0.5F / 1024.0F) * 0.03F;
        var9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        var9.setColorOpaque_F(var2, var3, var4);

        int var10;
        for(int var7 = -2048; var7 < this.worldObj.width + 2048; var7 += 512) {
            for(var10 = -2048; var10 < this.worldObj.length + 2048; var10 += 512) {
                var9.addVertexWithUV((float)var7, var6, (float)(var10 + 512), (float)var7 * (0.5F / 1024.0F) + var1, (float)(var10 + 512) * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)(var7 + 512), var6, (float)(var10 + 512), (float)(var7 + 512) * (0.5F / 1024.0F) + var1, (float)(var10 + 512) * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)(var7 + 512), var6, (float)var10, (float)(var7 + 512) * (0.5F / 1024.0F) + var1, (float)var10 * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)var7, var6, (float)var10, (float)var7 * (0.5F / 1024.0F) + var1, (float)var10 * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)var7, var6, (float)var10, (float)var7 * (0.5F / 1024.0F) + var1, (float)var10 * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)(var7 + 512), var6, (float)var10, (float)(var7 + 512) * (0.5F / 1024.0F) + var1, (float)var10 * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)(var7 + 512), var6, (float)(var10 + 512), (float)(var7 + 512) * (0.5F / 1024.0F) + var1, (float)(var10 + 512) * (0.5F / 1024.0F));
                var9.addVertexWithUV((float)var7, var6, (float)(var10 + 512), (float)var7 * (0.5F / 1024.0F) + var1, (float)(var10 + 512) * (0.5F / 1024.0F));
            }
        }

        var9.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
        var9.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
        var1 = (float)(this.worldObj.skyColor >> 16 & 255) / 255.0F;
        var2 = (float)(this.worldObj.skyColor >> 8 & 255) / 255.0F;
        var3 = (float)(this.worldObj.skyColor & 255) / 255.0F;
        if(this.mc.options.anaglyph) {
            var4 = (var1 * 30.0F + var2 * 59.0F + var3 * 11.0F) / 100.0F;
            var2 = (var1 * 30.0F + var2 * 70.0F) / 100.0F;
            var3 = (var1 * 30.0F + var3 * 70.0F) / 100.0F;
            var1 = var4;
            var2 = var2;
            var3 = var3;
        }

        var9.setColorOpaque_F(var1, var2, var3);
        var6 = (float)(this.worldObj.height + 10);

        for(var10 = -2048; var10 < this.worldObj.width + 2048; var10 += 512) {
            for(int var8 = -2048; var8 < this.worldObj.length + 2048; var8 += 512) {
                var9.addVertex((float)var10, var6, (float)var8);
                var9.addVertex((float)(var10 + 512), var6, (float)var8);
                var9.addVertex((float)(var10 + 512), var6, (float)(var8 + 512));
                var9.addVertex((float)var10, var6, (float)(var8 + 512));
            }
        }

        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public final void oobGroundRenderer() {
        float var1 = this.worldObj.getBlockLightValue(0, this.worldObj.getGroundLevel(), 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/dirt.png"));
        if(this.worldObj.getGroundLevel() > this.worldObj.getWaterLevel() && this.worldObj.defaultFluid == Block.waterMoving.blockID) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/grass.png"));
        }

        GL11.glColor4f(var1, var1, var1, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glCallList(this.glGenList);
    }

    public final void oobWaterRenderer() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/water.png"));
        GL11.glCallList(this.glGenList + 1);
        GL11.glDisable(GL11.GL_BLEND);
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


    public final void drawBlockBreaking(MovingObjectPosition var1, int var2, ItemStack var3) {
        Tessellator var4 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        if(this.damagePartialTime > 0.0F) {
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
            int var5 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            var5 = this.worldObj.getBlockId(var1.blockX, var1.blockY, var1.blockZ);
            Block var6 = var5 > 0 ? Block.blocksList[var5] : null;
            var4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            var4.disableColor();
            if(var6 == null) {
                var6 = Block.stone;
            }

            this.globalRenderBlocks.renderBlockAllFaces(var6, var1.blockX, var1.blockY, var1.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
            var4.draw();
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    public final void drawSelectionBox(MovingObjectPosition var1, int var2) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        var2 = this.worldObj.getBlockId(var1.blockX, var1.blockY, var1.blockZ);
        if(var2 > 0) {
            AxisAlignedBB var3 = Block.blocksList[var2].getSelectedBoundingBoxFromPool(var1.blockX, var1.blockY, var1.blockZ).expand(0.002F, 0.002F, 0.002F);
            GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            GL11.glVertex3f(var3.minX, var3.minY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.minY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.minY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.minY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.minY, var3.minZ);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            GL11.glVertex3f(var3.minX, var3.maxY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.maxY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.maxY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.maxY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.maxY, var3.minZ);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            GL11.glVertex3f(var3.minX, var3.minY, var3.minZ);
            GL11.glVertex3f(var3.minX, var3.maxY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.minY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.maxY, var3.minZ);
            GL11.glVertex3f(var3.maxX, var3.minY, var3.maxZ);
            GL11.glVertex3f(var3.maxX, var3.maxY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.minY, var3.maxZ);
            GL11.glVertex3f(var3.minX, var3.maxY, var3.maxZ);
            GL11.glEnd();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
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

        for(var1 = var1; var1 <= var4; ++var1) {
			for(int var7 = var2; var7 <= var5; ++var7) {
				for(int var8 = var3; var8 <= var6; ++var8) {
					WorldRenderer var9 = this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide + var1];
					if(!var9.needsUpdate) {
						var9.needsUpdate = true;
						this.worldRenderersToUpdate.add(this.worldRenderers[(var8 * this.renderChunksTall + var7) * this.renderChunksWide + var1]);
					}
				}
			}
		}

	}

    public final void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
        this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
    }

    public final void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
        this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
    }

    public final void clipRenderersByFrustrum(ClippingHelper var1) {
        for(int var2 = 0; var2 < this.worldRenderers.length; ++var2) {
            this.worldRenderers[var2].updateInFrustrum(var1);
        }

    }

    public final void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
        this.mc.sndManager.playSound(var1, var2, var3, var4, var5, var6);
    }
}
