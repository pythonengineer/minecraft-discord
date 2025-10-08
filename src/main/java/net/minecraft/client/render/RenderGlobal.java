package net.minecraft.client.render;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EntityBubbleFX;
import net.minecraft.client.effect.EntityExplodeFX;
import net.minecraft.client.effect.EntityFlameFX;
import net.minecraft.client.effect.EntityLavaFX;
import net.minecraft.client.effect.EntitySmokeFX;
import net.minecraft.client.effect.EntitySplashFX;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.EntityMap;
import net.minecraft.game.world.IWorldAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

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
    private java.nio.IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled = false;
    private int cloudOffsetX = 0;
    private int glSkyList;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
	private int[] dummyBuf50k = new int['\uc350'];
    private java.nio.IntBuffer occlusionResult = java.nio.ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private double prevSortX = -9999.0D;
    private double prevSortY = -9999.0D;
    private double prevSortZ = -9999.0D;
	public float damagePartialTime;

	public RenderGlobal(Minecraft var1, RenderEngine var2) {
		this.mc = var1;
		this.renderEngine = var2;
		this.glGenList = GL11.glGenLists(2);
        this.glRenderListBase = GL11.glGenLists(786432);
        this.occlusionEnabled = GL11.checkOcclusionQuerySupport();
        if(this.occlusionEnabled) {
            this.occlusionResult.clear();
            this.glOcclusionQueryBase = java.nio.ByteBuffer.allocateDirect(262144 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
            this.glOcclusionQueryBase.clear();
            this.glOcclusionQueryBase.position(0);
            this.glOcclusionQueryBase.limit(262144);
            GL11.glGenQueriesARB(this.glOcclusionQueryBase);
        }

        this.glSkyList = GL11.glGenLists(1);
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        EaglercraftRandom var5 = new EaglercraftRandom(10842L);

        for(int var6 = 0; var6 < 500; ++var6) {
            GL11.glRotatef(var5.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            Tessellator var3 = Tessellator.instance;
            float var4 = 0.25F + var5.nextFloat() * 0.25F;
            var3.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            var3.addVertexWithUV(-var4, -100.0F, var4, 1.0F, 1.0F);
            var3.addVertexWithUV(var4, -100.0F, var4, 0.0F, 1.0F);
            var3.addVertexWithUV(var4, -100.0F, -var4, 0.0F, 0.0F);
            var3.addVertexWithUV(-var4, -100.0F, -var4, 1.0F, 0.0F);
            var3.draw();
        }

        GL11.glEndList();
	}

    public final void changeWorld(World var1) {
        if(this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.set(var1);
        this.worldObj = var1;
        this.globalRenderBlocks = new RenderBlocks(var1);
        if(var1 != null) {
            var1.addWorldAccess(this);
            RenderGlobal var7 = this;
            int var2;
            if(this.worldRenderers != null) {
                for(var2 = 0; var2 < var7.worldRenderers.length; ++var2) {
                    var7.worldRenderers[var2].stopRendering();
                }
            }

            var7.renderChunksWide = 16;
            var7.renderChunksTall = 8;
            var7.renderChunksDeep = 16;
            var7.worldRenderers = new WorldRenderer[var7.renderChunksWide * var7.renderChunksTall * var7.renderChunksDeep];
            var7.sortedWorldRenderers = new WorldRenderer[var7.renderChunksWide * var7.renderChunksTall * var7.renderChunksDeep];
            var2 = 0;
            int var3 = 0;
            var7.minBlockX = 0;
            var7.minBlockY = 0;
            var7.minBlockZ = 0;
            var7.maxBlockX = var7.renderChunksWide;
            var7.maxBlockY = var7.renderChunksTall;
            var7.maxBlockZ = var7.renderChunksDeep;

            int var4;
            for(var4 = 0; var4 < var7.renderChunksWide; ++var4) {
                for(int var5 = 0; var5 < var7.renderChunksTall; ++var5) {
                    for(int var6 = 0; var6 < var7.renderChunksDeep; ++var6) {
                        var7.worldRenderers[(var6 * var7.renderChunksTall + var5) * var7.renderChunksWide + var4] = new WorldRenderer(var7.worldObj, var4 << 4, var5 << 4, var6 << 4, 16, var7.glRenderListBase + var2);
                        if(var7.occlusionEnabled) {
                            var7.worldRenderers[(var6 * var7.renderChunksTall + var5) * var7.renderChunksWide + var4].glOcclusionQuery = var7.glOcclusionQueryBase.get(var3);
                        }

                        ++var3;
                        var7.sortedWorldRenderers[(var6 * var7.renderChunksTall + var5) * var7.renderChunksWide + var4] = var7.worldRenderers[(var6 * var7.renderChunksTall + var5) * var7.renderChunksWide + var4];
                        var2 += 3;
                    }
                }
            }

            for(var4 = 0; var4 < var7.worldRenderersToUpdate.size(); ++var4) {
                ((WorldRenderer)var7.worldRenderersToUpdate.get(var4)).needsUpdate = false;
            }

            var7.worldRenderersToUpdate.clear();
            GL11.glNewList(var7.glGenList, GL11.GL_COMPILE);
            GL11.glEndList();
            GL11.glNewList(var7.glGenList + 1, GL11.GL_COMPILE);
            GL11.glEndList();
            var7.markBlocksForUpdate(0, 0, 0, 256, 256, 256);
        }

    }

    public final void renderEntities(Vec3D var1, Frustrum var2, float var3) {
        EntityMap var4 = this.worldObj.entityMap;
        RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.thePlayer, var3);
        this.countEntitiesTotal = 0;
        this.countEntitiesRendered = 0;
        this.countEntitiesHidden = 0;

        for(int var5 = 0; var5 < var4.width; ++var5) {
            for(int var6 = 0; var6 < var4.depth; ++var6) {
                for(int var7 = 0; var7 < var4.height; ++var7) {
                    List var8 = var4.entityGrid[(var7 * var4.depth + var6) * var4.width + var5];
                    if(var8.size() != 0) {
                        int var9 = (var5 << 3) + 4;
                        int var10 = (var6 << 3) + 4;
                        int var11 = (var7 << 3) + 4;
                        this.countEntitiesTotal += var8.size();
                        float var10001 = (float)var9;
                        float var10002 = (float)var10;
                        float var17 = (float)var11;
                        float var16 = var10002;
                        float var12 = var10001;
                        int var18 = MathHelper.floor_float(var12) >>> 4;
                        int var19 = MathHelper.floor_float(var16) >>> 4;
                        int var20 = MathHelper.floor_float(var17) >>> 4;
                        if(!this.worldRenderers[(var20 * this.renderChunksTall + var19) * this.renderChunksWide + var18].isInFrustrum || !this.worldRenderers[(var20 * this.renderChunksTall + var19) * this.renderChunksWide + var18].isVisible) {
                            this.countEntitiesHidden += var8.size();
                        } else {
                            for(var9 = 0; var9 < var8.size(); ++var9) {
                                Entity var36 = (Entity)var8.get(var9);
                                double var38 = var36.posX - var1.xCoord;
                                double var39 = var36.posY - var1.yCoord;
                                double var40 = var36.posZ - var1.zCoord;
                                double var22 = var38 * var38 + var39 * var39 + var40 * var40;
                                AxisAlignedBB var37 = var36.boundingBox;
                                double var30 = var37.maxX - var37.minX;
                                double var32 = var37.maxY - var37.minY;
                                double var34 = var37.maxZ - var37.minZ;
                                double var27 = (var30 + var32 + var34) / 3.0D;
                                var27 *= 64.0D;
                                if(var22 < var27 * var27 && var2.isBoundingBoxInFrustrum(var36.boundingBox) && (var36 != this.worldObj.playerEntity || this.mc.options.thirdPersonView)) {
                                    ++this.countEntitiesRendered;
                                    RenderManager.instance.renderEntity(var36, var3);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public final String getDebugInfoRenders() {
        return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded;
    }

    public final String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

	public final int sortAndRender(EntityPlayer var1, int var2) {
        if(var2 == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
        }

        double var3 = var1.posX - this.prevSortX;
        double var5 = var1.posY - this.prevSortY;
        double var7 = var1.posZ - this.prevSortZ;
        int var4;
        int var6;
        int var16;
        int var17;
        if(var3 * var3 + var5 * var5 + var7 * var7 > 16.0D) {
            this.prevSortX = var1.posX;
            this.prevSortY = var1.posY;
            this.prevSortZ = var1.posZ;
            int var10001 = MathHelper.floor_double(var1.posX);
            var16 = MathHelper.floor_double(var1.posZ);
            var4 = var10001;
            RenderGlobal var14 = this;
            this.minBlockX = Integer.MAX_VALUE;
            this.minBlockY = Integer.MAX_VALUE;
            this.minBlockZ = Integer.MAX_VALUE;
            this.maxBlockX = Integer.MIN_VALUE;
            this.maxBlockY = Integer.MIN_VALUE;
            this.maxBlockZ = Integer.MIN_VALUE;

            for(var6 = 0; var6 < var14.renderChunksWide; ++var6) {
                for(var17 = var6 << 4; var17 - var4 < -128; var17 += 256) {
                }

                while(var17 - var4 >= 128) {
                    var17 -= 256;
                }

                if(var17 < var14.minBlockX) {
                    var14.minBlockX = var17;
                }

                if(var17 > var14.maxBlockX) {
                    var14.maxBlockX = var17;
                }

                for(int var8 = 0; var8 < var14.renderChunksDeep; ++var8) {
                    int var9;
                    for(var9 = var8 << 4; var9 - var16 < -128; var9 += 256) {
                    }

                    while(var9 - var16 >= 128) {
                        var9 -= 256;
                    }

                    if(var9 < var14.minBlockZ) {
                        var14.minBlockZ = var9;
                    }

                    if(var9 > var14.maxBlockZ) {
                        var14.maxBlockZ = var9;
                    }

                    for(int var10 = 0; var10 < var14.renderChunksTall; ++var10) {
                        int var11 = var10 << 4;
                        if(var11 < var14.minBlockY) {
                            var14.minBlockY = var9;
                        }

                        if(var11 > var14.maxBlockY) {
                            var14.maxBlockY = var9;
                        }

                        WorldRenderer var12 = var14.worldRenderers[(var8 * var14.renderChunksTall + var10) * var14.renderChunksWide + var6];
                        boolean var13 = var12.needsUpdate;
                        var12.setPosition(var17, var11, var9);
                        if(!var13 && var12.needsUpdate) {
                            var14.worldRenderersToUpdate.add(var12);
                        }
                    }
                }
            }

            try {
			    Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
            } catch (IllegalArgumentException ex) {
			}
		}

        int var15;
        if(this.occlusionEnabled && var2 == 0) {
            var16 = 16;
            this.checkOcclusionQueryResult(0, 16);

            for(var6 = 0; var6 < 16; ++var6) {
                this.sortedWorldRenderers[var6].isVisible = true;
            }

            var15 = 0 + this.renderSortedRenderers(0, 16, var2);

            do {
                var4 = var16;
                var16 <<= 1;
                if(var16 > this.sortedWorldRenderers.length) {
                    var16 = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(var4, var16);

                for(var6 = var4; var6 < var16; ++var6) {
                    if(this.sortedWorldRenderers[var6].skipAllRenderPasses()) {
                        this.sortedWorldRenderers[var6].isInFrustrum = false;
                    } else {
                        if(!this.sortedWorldRenderers[var6].isInFrustrum) {
                            this.sortedWorldRenderers[var6].isVisible = true;
                        }

                        if(this.sortedWorldRenderers[var6].isInFrustrum && !this.sortedWorldRenderers[var6].isWaitingOnOcclusionQuery) {
                            float var18 = MathHelper.sqrt_float(this.sortedWorldRenderers[var6].distanceToEntitySquared(var1));
                            var17 = (int)(1.0F + var18 / 64.0F);
                            if(this.cloudOffsetX % var17 == var6 % var17) {
                                GL11.glBeginQueryARB(GL11.GL_ANY_SAMPLES_PASSED, this.sortedWorldRenderers[var6].glOcclusionQuery);
                                this.sortedWorldRenderers[var6].callOcclusionQueryList();
                                GL11.glEndQueryARB(GL11.GL_ANY_SAMPLES_PASSED);
                                this.sortedWorldRenderers[var6].isWaitingOnOcclusionQuery = true;
                            }
                        }
                    }
                }

                GL11.glColorMask(true, true, true, true);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_FOG);
                var15 += this.renderSortedRenderers(var4, var16, var2);
            } while(var16 < this.sortedWorldRenderers.length);
        } else {
            var15 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var2);
        }

        return var15;
    }

    private void checkOcclusionQueryResult(int var1, int var2) {
        for(var1 = var1; var1 < var2; ++var1) {
            if(this.sortedWorldRenderers[var1].isWaitingOnOcclusionQuery) {
                this.occlusionResult.clear();
                GL11.glGetQueryObjectuARB(this.sortedWorldRenderers[var1].glOcclusionQuery, GL11.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
                if(this.occlusionResult.get(0) != 0) {
                    this.sortedWorldRenderers[var1].isWaitingOnOcclusionQuery = false;
                    this.occlusionResult.clear();
                    GL11.glGetQueryObjectuARB(this.sortedWorldRenderers[var1].glOcclusionQuery, GL11.GL_QUERY_RESULT, this.occlusionResult);
                    this.sortedWorldRenderers[var1].isVisible = this.occlusionResult.get(0) != 0;
                }
            }
        }

    }

    private int renderSortedRenderers(int var1, int var2, int var3) {
        int var4 = 0;

        for(var1 = var1; var1 < var2; ++var1) {
            if(var3 == 0) {
                ++this.renderersLoaded;
                if(!this.sortedWorldRenderers[var1].isInFrustrum) {
                    ++this.renderersBeingClipped;
                }

                if(this.sortedWorldRenderers[var1].isInFrustrum && !this.sortedWorldRenderers[var1].isVisible) {
                    ++this.renderersBeingOccluded;
                }

                if(this.sortedWorldRenderers[var1].isInFrustrum && this.sortedWorldRenderers[var1].isVisible) {
                    ++this.renderersBeingRendered;
                }
            }

            if(this.sortedWorldRenderers[var1].isInFrustrum && this.sortedWorldRenderers[var1].isVisible) {
                var4 = this.sortedWorldRenderers[var1].getGLCallListForPass(this.dummyBuf50k, var4, var3);
            }
        }

        this.renderIntBuffer.clear();
        this.renderIntBuffer.put(this.dummyBuf50k, 0, var4);
        this.renderIntBuffer.flip();
        if(this.renderIntBuffer.remaining() > 0) {
            GL11.glCallLists(this.renderIntBuffer);
        }

        return this.renderIntBuffer.remaining();
    }

    public final void bindTerrainTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
        GL11.glCallLists(this.renderIntBuffer);
    }

    public final void renderAllRenderLists() {
        ++this.cloudOffsetX;
    }

    public final void renderSky(float var1) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3D var2 = World.getSkyColor();
        float var3 = (float)var2.xCoord;
        float var4 = (float)var2.yCoord;
        float var9 = (float)var2.zCoord;
        if(this.mc.options.anaglyph) {
            float var5 = (var3 * 30.0F + var4 * 59.0F + var9 * 11.0F) / 100.0F;
            var4 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            var9 = (var3 * 30.0F + var9 * 70.0F) / 100.0F;
            var3 = var5;
            var4 = var4;
            var9 = var9;
        }

        GL11.glDepthMask(false);
        Tessellator var12 = Tessellator.instance;
        var12.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
        var12.setColorOpaque_F(var3, var4, var9);

        int var10;
        for(var10 = -512; var10 < 1024; var10 += 32) {
            for(int var11 = -512; var11 < 1024; var11 += 32) {
                var12.addVertex((float)var10, 160.0F, (float)var11);
                var12.addVertex((float)(var10 + 32), 160.0F, (float)var11);
                var12.addVertex((float)(var10 + 32), 160.0F, (float)(var11 + 32));
                var12.addVertex((float)var10, 160.0F, (float)(var11 + 32));
            }
        }

        var12.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glPushMatrix();
        var9 = (float)(this.worldObj.playerEntity.lastTickPosX + (this.worldObj.playerEntity.posX - this.worldObj.playerEntity.lastTickPosX) * (double)var1);
        var3 = (float)(this.worldObj.playerEntity.lastTickPosY + (this.worldObj.playerEntity.posY - this.worldObj.playerEntity.lastTickPosY) * (double)var1);
        var4 = (float)(this.worldObj.playerEntity.lastTickPosZ + (this.worldObj.playerEntity.posZ - this.worldObj.playerEntity.lastTickPosZ) * (double)var1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(var9, var3, var4);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(360.0F, 1.0F, 0.0F, 0.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
        var12.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        var12.addVertexWithUV(-30.0F, 100.0F, -30.0F, 0.0F, 0.0F);
        var12.addVertexWithUV(30.0F, 100.0F, -30.0F, 1.0F, 0.0F);
        var12.addVertexWithUV(30.0F, 100.0F, 30.0F, 1.0F, 1.0F);
        var12.addVertexWithUV(-30.0F, 100.0F, 30.0F, 0.0F, 1.0F);
        var12.draw();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
        var12.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        var12.addVertexWithUV(-20.0F, -100.0F, 20.0F, 1.0F, 1.0F);
        var12.addVertexWithUV(20.0F, -100.0F, 20.0F, 0.0F, 1.0F);
        var12.addVertexWithUV(20.0F, -100.0F, -20.0F, 0.0F, 0.0F);
        var12.addVertexWithUV(-20.0F, -100.0F, -20.0F, 1.0F, 0.0F);
        var12.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glCallList(this.glSkyList);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var2 = World.getCloudColor();
        var3 = (float)var2.xCoord;
        var4 = (float)var2.yCoord;
        var9 = (float)var2.zCoord;
        float var7;
        if(this.mc.options.anaglyph) {
            float var6 = (var3 * 30.0F + var4 * 59.0F + var9 * 11.0F) / 100.0F;
            var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            var9 = (var3 * 30.0F + var9 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var9 = var9;
        }

        var7 = ((float)this.cloudOffsetX + var1) * (0.5F / 1024.0F) * 0.03F;
        var12.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        var12.setColorOpaque_F(var3, var4, var9);

        for(int var8 = -512; var8 < 512; var8 += 32) {
            for(var10 = -512; var10 < 512; var10 += 32) {
                var12.addVertexWithUV((float)var8, 128.0F, (float)(var10 + 32), (float)var8 * (0.5F / 1024.0F) + var7, (float)(var10 + 32) * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)(var8 + 32), 128.0F, (float)(var10 + 32), (float)(var8 + 32) * (0.5F / 1024.0F) + var7, (float)(var10 + 32) * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)(var8 + 32), 128.0F, (float)var10, (float)(var8 + 32) * (0.5F / 1024.0F) + var7, (float)var10 * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)var8, 128.0F, (float)var10, (float)var8 * (0.5F / 1024.0F) + var7, (float)var10 * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)var8, 128.0F, (float)var10, (float)var8 * (0.5F / 1024.0F) + var7, (float)var10 * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)(var8 + 32), 128.0F, (float)var10, (float)(var8 + 32) * (0.5F / 1024.0F) + var7, (float)var10 * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)(var8 + 32), 128.0F, (float)(var10 + 32), (float)(var8 + 32) * (0.5F / 1024.0F) + var7, (float)(var10 + 32) * (0.5F / 1024.0F));
                var12.addVertexWithUV((float)var8, 128.0F, (float)(var10 + 32), (float)var8 * (0.5F / 1024.0F) + var7, (float)(var10 + 32) * (0.5F / 1024.0F));
            }
        }

        var12.draw();
    }

    public final void updateRenderers(EntityPlayer var1) {
        try {
            Collections.sort(this.worldRenderersToUpdate, new RenderSorter(var1));
        } catch (IllegalArgumentException ex) {
        }

        int var2 = this.worldRenderersToUpdate.size() - 1;
        int var3 = this.worldRenderersToUpdate.size();

        for(int var4 = 0; var4 < var3; ++var4) {
            WorldRenderer var5 = (WorldRenderer)this.worldRenderersToUpdate.get(var2 - var4);
            if(var5.distanceToEntitySquared(var1) > 2500.0F && var4 > 4) {
                return;
            }

            this.worldRenderersToUpdate.remove(var5);
            var5.updateRenderer();
            var5.needsUpdate = false;
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
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            var4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            var4.disableColor();
            if(var6 == null) {
                var6 = Block.stone;
            }

            this.globalRenderBlocks.renderBlockUsingTexture(var6, var1.blockX, var1.blockY, var1.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
            var4.draw();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
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
            AxisAlignedBB var3 = Block.blocksList[var2].getSelectedBoundingBoxFromPool(var1.blockX, var1.blockY, var1.blockZ).expand((double)0.002F, (double)0.002F, (double)0.002F);
            Tessellator var4 = Tessellator.instance;
            var4.startDrawing(3, DefaultVertexFormats.POSITION);
            var4.addVertex(var3.minX, var3.minY, var3.minZ);
            var4.addVertex(var3.maxX, var3.minY, var3.minZ);
            var4.addVertex(var3.maxX, var3.minY, var3.maxZ);
            var4.addVertex(var3.minX, var3.minY, var3.maxZ);
            var4.addVertex(var3.minX, var3.minY, var3.minZ);
            var4.draw();
            var4.startDrawing(3, DefaultVertexFormats.POSITION);
            var4.addVertex(var3.minX, var3.maxY, var3.minZ);
            var4.addVertex(var3.maxX, var3.maxY, var3.minZ);
            var4.addVertex(var3.maxX, var3.maxY, var3.maxZ);
            var4.addVertex(var3.minX, var3.maxY, var3.maxZ);
            var4.addVertex(var3.minX, var3.maxY, var3.minZ);
            var4.draw();
            var4.startDrawing(1, DefaultVertexFormats.POSITION);
            var4.addVertex(var3.minX, var3.minY, var3.minZ);
            var4.addVertex(var3.minX, var3.maxY, var3.minZ);
            var4.addVertex(var3.maxX, var3.minY, var3.minZ);
            var4.addVertex(var3.maxX, var3.maxY, var3.minZ);
            var4.addVertex(var3.maxX, var3.minY, var3.maxZ);
            var4.addVertex(var3.maxX, var3.maxY, var3.maxZ);
            var4.addVertex(var3.minX, var3.minY, var3.maxZ);
            var4.addVertex(var3.minX, var3.maxY, var3.maxZ);
            var4.draw();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
        var1 >>= 4;
        var2 >>= 4;
        var3 >>= 4;
        var4 >>= 4;
        var5 >>= 4;
        var6 >>= 4;

        for(var1 = var1; var1 <= var4; ++var1) {
            for(int var7 = var2; var7 <= var5; ++var7) {
                for(int var8 = var3; var8 <= var6; ++var8) {
                    int var9 = ((var8 & this.renderChunksDeep - 1) * this.renderChunksTall + (var7 & this.renderChunksTall - 1)) * this.renderChunksWide + (var1 & this.renderChunksWide - 1);
                    WorldRenderer var10 = this.worldRenderers[var9];
                    if(!var10.needsUpdate) {
                        var10.needsUpdate = true;
                        this.worldRenderersToUpdate.add(var10);
                    }
                }
            }
        }

    }

    public final void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
        this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
    }

    public final void clipRenderersByFrustum(Frustrum var1) {
        for(int var2 = 0; var2 < this.worldRenderers.length; ++var2) {
            this.worldRenderers[var2].updateInFrustrum(var1);
        }

    }

    public final void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
        this.mc.sndManager.playSound(var1, (float)var2, (float)var4, (float)var6, var8, var9);
    }

    public final void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        double var14 = this.worldObj.playerEntity.posX - var2;
        double var16 = this.worldObj.playerEntity.posY - var4;
        double var18 = this.worldObj.playerEntity.posZ - var6;
        if(var14 * var14 + var16 * var16 + var18 * var18 <= 256.0D) {
            if(var1 == "bubble") {
                this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if(var1 == "smoke") {
                this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6));
            } else if(var1 == "explode") {
                this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if(var1 == "flame") {
                this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, var2, var4, var6));
            } else if(var1 == "lava") {
                this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, var2, var4, var6));
            } else if(var1 == "splash") {
                this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, var2, var4, var6));
            } else {
                if(var1 == "largesmoke") {
                    this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6, 2.5F));
                }

            }
        }
    }
}
