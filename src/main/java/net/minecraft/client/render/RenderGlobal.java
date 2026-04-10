package net.minecraft.client.render;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
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
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IWorldAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntity;

public final class RenderGlobal implements IWorldAccess {
    private List tileEntities = new ArrayList();
    private World worldObj;
    private RenderEngine renderEngine;
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
    private int rstarGLCallList;
    private int glSkyList;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int renderDistance = -1;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private java.nio.IntBuffer occlusionResult = java.nio.ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
    private int renderersLoaded;
    private int renderersSkippingRenderPass;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private List glRenderLists = new ArrayList();
    private FloatBuffer cloudBuffer = BufferUtils.createFloatBuffer(16);
    private double prevSortX = -9999.0D;
    private double prevSortY = -9999.0D;
    private double prevSortZ = -9999.0D;
	public float damagePartialTime;

    public RenderGlobal(Minecraft mc, RenderEngine renderEngine2) {
        this.mc = mc;
        this.renderEngine = renderEngine2;
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

        this.rstarGLCallList = GL11.glGenLists(1);
        GL11.glNewList(this.rstarGLCallList, GL11.GL_COMPILE);
        EaglercraftRandom var5 = new EaglercraftRandom(10842L);

        Tessellator var3;
        int var7;
        for(var7 = 0; var7 < 500; ++var7) {
            GL11.glRotatef(var5.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var5.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            var3 = Tessellator.instance;
            float var4 = 0.25F + var5.nextFloat() * 0.25F;
            var3.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            var3.addVertexWithUV((double)(-var4), -100.0D, (double)var4, 1.0D, 1.0D);
            var3.addVertexWithUV((double)var4, -100.0D, (double)var4, 0.0D, 1.0D);
            var3.addVertexWithUV((double)var4, -100.0D, (double)(-var4), 0.0D, 0.0D);
            var3.addVertexWithUV((double)(-var4), -100.0D, (double)(-var4), 1.0D, 0.0D);
            var3.draw();
        }

        GL11.glEndList();
        this.glSkyList = GL11.glGenLists(1);
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        var3 = Tessellator.instance;
        var3.startDrawingQuads(DefaultVertexFormats.POSITION);

        for(int var6 = -256; var6 <= 256; var6 += 32) {
            for(var7 = -256; var7 <= 256; var7 += 32) {
                var3.drawVertex((double)var6, 16.0D, (double)var7);
                var3.drawVertex((double)(var6 + 32), 16.0D, (double)var7);
                var3.drawVertex((double)(var6 + 32), 16.0D, (double)(var7 + 32));
                var3.drawVertex((double)var6, 16.0D, (double)(var7 + 32));
            }
        }

        var3.draw();

        GL11.glEndList();
	}

    public final void changeWorld(World world) {
        if(this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.set(world);
        this.worldObj = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if(world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }

    }

    private void loadRenderers() {
        this.renderDistance = this.mc.gameSettings.renderDistance;
        int i1;
        if(this.worldRenderers != null) {
            for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
                this.worldRenderers[i1].stopRendering();
            }
        }

        i1 = 5 << 3 - this.renderDistance;
        if(i1 > 28) {
            i1 = 28;
        }

        this.renderChunksWide = i1;
        this.renderChunksTall = 8;
        this.renderChunksDeep = i1;
        this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        i1 = 0;
        int i2 = 0;
        this.minBlockX = 0;
        this.minBlockY = 0;
        this.minBlockZ = 0;
        this.maxBlockX = this.renderChunksWide;
        this.maxBlockY = this.renderChunksTall;
        this.maxBlockZ = this.renderChunksDeep;

        int i3;
        for(i3 = 0; i3 < this.worldRenderersToUpdate.size(); ++i3) {
            ((WorldRenderer)this.worldRenderersToUpdate.get(i3)).needsUpdate = false;
        }

        this.worldRenderersToUpdate.clear();

        for(i3 = 0; i3 < this.renderChunksWide; ++i3) {
            for(int i4 = 0; i4 < this.renderChunksTall; ++i4) {
                for(int i5 = 0; i5 < this.renderChunksDeep; ++i5) {
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = new WorldRenderer(this.worldObj, i3 << 4, i4 << 4, i5 << 4, 16, this.glRenderListBase + i1);
                    if(this.occlusionEnabled) {
                        this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].glOcclusionQuery = this.glOcclusionQueryBase.get(i2);
                    }

                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isVisible = true;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isInFrustrum = true;
                    ++i2;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].needsUpdate = true;
                    this.sortedWorldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3];
                    this.worldRenderersToUpdate.add(this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3]);
                    i1 += 3;
                }
            }
        }

        Entity entity = this.worldObj.playerEntity;
        this.markRenderersForNewPosition(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
        try {
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entity));
        } catch (IllegalArgumentException ex) {
        }
    }

    public final void renderEntities(Vec3D lookVector, Frustrum frustrum, float partialTicks) {
        TileEntityRenderer.instance.renderTileEntity(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, partialTicks);
        RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, partialTicks);
        this.countEntitiesTotal = 0;
        this.countEntitiesRendered = 0;
        this.countEntitiesHidden = 0;
        Entity entity4 = this.worldObj.playerEntity;
        RenderManager.renderPosX = this.worldObj.playerEntity.lastTickPosX + (entity4.posX - entity4.lastTickPosX) * (double)partialTicks;
        RenderManager.renderPosY = entity4.lastTickPosY + (entity4.posY - entity4.lastTickPosY) * (double)partialTicks;
        RenderManager.renderPosZ = entity4.lastTickPosZ + (entity4.posZ - entity4.lastTickPosZ) * (double)partialTicks;
        TileEntityRenderer.posX = entity4.lastTickPosX + (entity4.posX - entity4.lastTickPosX) * (double)partialTicks;
        TileEntityRenderer.posY = entity4.lastTickPosY + (entity4.posY - entity4.lastTickPosY) * (double)partialTicks;
        TileEntityRenderer.posZ = entity4.lastTickPosZ + (entity4.posZ - entity4.lastTickPosZ) * (double)partialTicks;
        List list30 = this.worldObj.getLoadedEntityList();
        this.countEntitiesTotal = list30.size();

        int i5;
        for(i5 = 0; i5 < list30.size(); ++i5) {
            Entity entity6;
            Entity entity7;
            double d10 = (entity7 = entity6 = (Entity)list30.get(i5)).posX - lookVector.xCoord;
            double d12 = entity7.posY - lookVector.yCoord;
            double d14 = entity7.posZ - lookVector.zCoord;
            double d16 = d10 * d10 + d12 * d12 + d14 * d14;
            AxisAlignedBB axisAlignedBB31;
            double d24 = (axisAlignedBB31 = entity7.boundingBox).maxX - axisAlignedBB31.minX;
            double d26 = axisAlignedBB31.maxY - axisAlignedBB31.minY;
            double d28 = axisAlignedBB31.maxZ - axisAlignedBB31.minZ;
            double d21 = (d24 + d26 + d28) / 3.0D * 64.0D;
            if(d16 < d21 * d21 && frustrum.isBoundingBoxInFrustum(entity6.boundingBox) && (entity6 != this.worldObj.playerEntity || this.mc.gameSettings.thirdPersonView)) {
                ++this.countEntitiesRendered;
                RenderManager.instance.renderEntity(entity6, partialTicks);
            }
        }

        for(i5 = 0; i5 < this.tileEntities.size(); ++i5) {
            TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(i5), partialTicks);
        }

    }

    public final String getDebugInfoRenders() {
        return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersSkippingRenderPass + ", O: " + this.renderersBeingOccluded;
    }

    public final String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    private void markRenderersForNewPosition(int x, int y, int z) {
        x -= 8;
        z -= 8;
        this.minBlockX = Integer.MAX_VALUE;
        this.minBlockY = Integer.MAX_VALUE;
        this.minBlockZ = Integer.MAX_VALUE;
        this.maxBlockX = Integer.MIN_VALUE;
        this.maxBlockY = Integer.MIN_VALUE;
        this.maxBlockZ = Integer.MIN_VALUE;
        int i4 = (y = this.renderChunksWide << 4) / 2;

        for(int i5 = 0; i5 < this.renderChunksWide; ++i5) {
            int i6;
            int i7;
            if((i7 = (i6 = i5 << 4) + i4 - x) < 0) {
                i7 -= y - 1;
            }

            i7 /= y;
            if((i6 -= i7 * y) < this.minBlockX) {
                this.minBlockX = i6;
            }

            if(i6 > this.maxBlockX) {
                this.maxBlockX = i6;
            }

            for(i7 = 0; i7 < this.renderChunksDeep; ++i7) {
                int i8;
                int i9;
                if((i9 = (i8 = i7 << 4) + i4 - z) < 0) {
                    i9 -= y - 1;
                }

                i9 /= y;
                if((i8 -= i9 * y) < this.minBlockZ) {
                    this.minBlockZ = i8;
                }

                if(i8 > this.maxBlockZ) {
                    this.maxBlockZ = i8;
                }

                for(i9 = 0; i9 < this.renderChunksTall; ++i9) {
                    int i10;
                    if((i10 = i9 << 4) < this.minBlockY) {
                        this.minBlockY = i10;
                    }

                    if(i10 > this.maxBlockY) {
                        this.maxBlockY = i10;
                    }

                    WorldRenderer worldRenderer11;
                    boolean z12 = (worldRenderer11 = this.worldRenderers[(i7 * this.renderChunksTall + i9) * this.renderChunksWide + i5]).needsUpdate;
                    worldRenderer11.setPosition(i6, i10, i8);
                    if(!z12 && worldRenderer11.needsUpdate) {
                        this.worldRenderersToUpdate.add(worldRenderer11);
                    }
                }
            }
        }

    }

    public final int sortAndRender(EntityPlayer playerEntity, int callListId, double partialTime) {
        if(this.mc.gameSettings.renderDistance != this.renderDistance) {
            this.loadRenderers();
        }

        if(callListId == 0) {
            this.renderersLoaded = 0;
            this.renderersSkippingRenderPass = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
        }

        double d5 = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * partialTime;
        double d7 = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * partialTime;
        double d9 = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * partialTime;
        double d11 = playerEntity.posX - this.prevSortX;
        double d13 = playerEntity.posY - this.prevSortY;
        double d15 = playerEntity.posZ - this.prevSortZ;
        if(d11 * d11 + d13 * d13 + d15 * d15 > 16.0D) {
            this.prevSortX = playerEntity.posX;
            this.prevSortY = playerEntity.posY;
            this.prevSortZ = playerEntity.posZ;
            this.markRenderersForNewPosition(MathHelper.floor_double(playerEntity.posX), MathHelper.floor_double(playerEntity.posY), MathHelper.floor_double(playerEntity.posZ));
            try {
                Arrays.sort(this.sortedWorldRenderers, new EntitySorter(playerEntity));
            } catch (IllegalArgumentException ex) {
            }
        }

        this.tileEntities.clear();
        int i21;
        if(this.occlusionEnabled && !this.mc.gameSettings.anaglyph && callListId == 0) {
            int i22 = 16;
            this.checkOcclusionQueryResult(0, 16);

            for(int i14 = 0; i14 < 16; ++i14) {
                this.sortedWorldRenderers[i14].isVisible = true;
            }

            i21 = 0 + this.renderSortedRenderers(0, 16, callListId, partialTime);

            do {
                int i12 = i22;
                i22 <<= 1;
                if(i22 > this.sortedWorldRenderers.length) {
                    i22 = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(i12, i22);
                GL11.glPushMatrix();
                float f23 = 0.0F;
                float f24 = 0.0F;
                float f16 = 0.0F;

                for(int i17 = i12; i17 < i22; ++i17) {
                    if(this.sortedWorldRenderers[i17].skipAllRenderPasses()) {
                        this.sortedWorldRenderers[i17].isInFrustrum = false;
                    } else {
                        if(!this.sortedWorldRenderers[i17].isInFrustrum) {
                            this.sortedWorldRenderers[i17].isVisible = true;
                        }

                        if(this.sortedWorldRenderers[i17].isInFrustrum && !this.sortedWorldRenderers[i17].isWaitingOnOcclusionQuery) {
                            float f18 = MathHelper.sqrt_float(this.sortedWorldRenderers[i17].distanceToEntitySquared(playerEntity));
                            int i25 = (int)(1.0F + f18 / 64.0F);
                            if(this.cloudOffsetX % i25 == i17 % i25) {
                                WorldRenderer worldRenderer = this.sortedWorldRenderers[i17];
                                float f19 = (float)((double)worldRenderer.posXMinus - d5);
                                float f20 = (float)((double)worldRenderer.posYMinus - d7);
                                f18 = (float)((double)worldRenderer.posZMinus - d9);
                                f19 -= f23;
                                f20 -= f24;
                                f18 -= f16;
                                if(f19 != 0.0F || f20 != 0.0F || f18 != 0.0F) {
                                    GL11.glTranslatef(f19, f20, f18);
                                    f23 += f19;
                                    f24 += f20;
                                    f16 += f18;
                                }

                                GL11.glBeginQueryARB(GL11.GL_ANY_SAMPLES_PASSED, this.sortedWorldRenderers[i17].glOcclusionQuery);
                                this.sortedWorldRenderers[i17].callOcclusionQueryList();
                                GL11.glEndQueryARB(GL11.GL_ANY_SAMPLES_PASSED);
                                this.sortedWorldRenderers[i17].isWaitingOnOcclusionQuery = true;
                            }
                        }
                    }
                }

                GL11.glPopMatrix();
                GL11.glColorMask(true, true, true, true);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_FOG);
                i21 += this.renderSortedRenderers(i12, i22, callListId, partialTime);
            } while(i22 < this.sortedWorldRenderers.length);
        } else {
            i21 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, callListId, partialTime);
        }

        return i21;
    }

    private void checkOcclusionQueryResult(int worldRendererId, int rendererId) {
        for(worldRendererId = worldRendererId; worldRendererId < rendererId; ++worldRendererId) {
            if(this.sortedWorldRenderers[worldRendererId].isWaitingOnOcclusionQuery) {
                this.occlusionResult.clear();
                GL11.glGetQueryObjectuARB(this.sortedWorldRenderers[worldRendererId].glOcclusionQuery, GL11.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
                if(this.occlusionResult.get(0) != 0) {
                    this.sortedWorldRenderers[worldRendererId].isWaitingOnOcclusionQuery = false;
                    this.occlusionResult.clear();
                    GL11.glGetQueryObjectuARB(this.sortedWorldRenderers[worldRendererId].glOcclusionQuery, GL11.GL_QUERY_RESULT, this.occlusionResult);
                    this.sortedWorldRenderers[worldRendererId].isVisible = this.occlusionResult.get(0) != 0;
                }
            }
        }

    }

    private int renderSortedRenderers(int worldRendererId, int rendererId, int callListId, double partialTime) {
        this.glRenderLists.clear();
        int i6 = 0;

        for(worldRendererId = worldRendererId; worldRendererId < rendererId; ++worldRendererId) {
            if(callListId == 0) {
                ++this.renderersLoaded;
                if(!this.sortedWorldRenderers[worldRendererId].isInFrustrum) {
                    ++this.renderersSkippingRenderPass;
                }

                if(this.sortedWorldRenderers[worldRendererId].isInFrustrum && !this.sortedWorldRenderers[worldRendererId].isVisible) {
                    ++this.renderersBeingOccluded;
                }

                if(this.sortedWorldRenderers[worldRendererId].isInFrustrum && this.sortedWorldRenderers[worldRendererId].isVisible) {
                    ++this.renderersBeingRendered;
                }
            }

            if(this.sortedWorldRenderers[worldRendererId].isInFrustrum && this.sortedWorldRenderers[worldRendererId].isVisible) {
                int i7 = this.sortedWorldRenderers[worldRendererId].getGLCallListForPass(callListId);
                if(callListId == 0) {
                    this.tileEntities.addAll(this.sortedWorldRenderers[worldRendererId].tileEntityRenderers);
                }

                if(i7 >= 0) {
                    this.glRenderLists.add(this.sortedWorldRenderers[worldRendererId]);
                    ++i6;
                }
            }
        }

        this.renderAllRenderLists(callListId, partialTime);
        return i6;
    }

    public final void renderAllRenderLists(int callListId, double partialTime) {
        EntityPlayerSP entityPlayerSP4 = this.mc.thePlayer;
        double d5 = this.mc.thePlayer.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * partialTime;
        double d7 = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * partialTime;
        double d9 = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * partialTime;
        GL11.glPushMatrix();
        float f16 = 0.0F;
        float f3 = 0.0F;
        float f17 = 0.0F;

        for(int i11 = 0; i11 < this.glRenderLists.size(); ++i11) {
            WorldRenderer worldRenderer12;
            float f13 = (float)((double)(worldRenderer12 = (WorldRenderer)this.glRenderLists.get(i11)).posXMinus - d5);
            float f14 = (float)((double)worldRenderer12.posYMinus - d7);
            float f15 = (float)((double)worldRenderer12.posZMinus - d9);
            f13 -= f16;
            f14 -= f3;
            f15 -= f17;
            if(f13 != 0.0F || f14 != 0.0F || f15 != 0.0F) {
                GL11.glTranslatef(f13, f14, f15);
                f16 += f13;
                f3 += f14;
                f17 += f15;
            }

            GL11.glCallList(worldRenderer12.getGLCallListForPass(callListId));
        }

        GL11.glPopMatrix();
    }

    public final void updateClouds() {
        ++this.cloudOffsetX;
    }

    public final void renderSky(float partialTime) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3D vec3D2;
        float f3 = (float)(vec3D2 = this.worldObj.getSkyColor(partialTime)).xCoord;
        float f4 = (float)vec3D2.yCoord;
        float f6 = (float)vec3D2.zCoord;
        if(this.mc.gameSettings.anaglyph) {
            float f5 = (f3 * 30.0F + f4 * 59.0F + f6 * 11.0F) / 100.0F;
            f4 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
            f6 = (f3 * 30.0F + f6 * 70.0F) / 100.0F;
            f3 = f5;
            f4 = f4;
            f6 = f6;
        }

        GL11.glColor3f(f3, f4, f6);
        Tessellator t = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glCallList(this.glSkyList);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(this.worldObj.getCelestialAngle(partialTime) * 360.0F, 1.0F, 0.0F, 0.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
        t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        t.addVertexWithUV(-30.0D, 100.0D, -30.0D, 0.0D, 0.0D);
        t.addVertexWithUV(30.0D, 100.0D, -30.0D, 1.0D, 0.0D);
        t.addVertexWithUV(30.0D, 100.0D, 30.0D, 1.0D, 1.0D);
        t.addVertexWithUV(-30.0D, 100.0D, 30.0D, 0.0D, 1.0D);
        t.draw();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
        t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        t.addVertexWithUV(-20.0D, -100.0D, 20.0D, 1.0D, 1.0D);
        t.addVertexWithUV(20.0D, -100.0D, 20.0D, 0.0D, 1.0D);
        t.addVertexWithUV(20.0D, -100.0D, -20.0D, 0.0D, 0.0D);
        t.addVertexWithUV(-20.0D, -100.0D, -20.0D, 1.0D, 0.0D);
        t.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(partialTime = this.worldObj.calculateFogLight(partialTime), partialTime, partialTime, partialTime);
        GL11.glCallList(this.rstarGLCallList);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
    }

    public final void renderClouds(float partialTime) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        float f3 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)partialTime);
        Tessellator tessellator4 = Tessellator.instance;
        double d6 = (this.worldObj.playerEntity.prevPosX + (this.worldObj.playerEntity.posX - this.worldObj.playerEntity.prevPosX) * (double)partialTime + (double)(((float)this.cloudOffsetX + partialTime) * 0.03F)) / 12.0D;
        double d8 = (this.worldObj.playerEntity.prevPosZ + (this.worldObj.playerEntity.posZ - this.worldObj.playerEntity.prevPosZ) * (double)partialTime) / 12.0D + (double)0.33F;
        f3 = 108.0F - f3 + 0.33F;
        int i5 = MathHelper.floor_double(d6 / 2048.0D);
        int i10 = MathHelper.floor_double(d8 / 2048.0D);
        d6 -= (double)(i5 << 11);
        d8 -= (double)(i10 << 11);
        GL11.glActiveTexture(GL11.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/fluff.png"));
        GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
        GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.flipBuffer(1.0F, 0.0F, 0.0F, 0.0F));
        GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
        GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.flipBuffer(0.0F, 0.0F, 1.0F, 0.0F));
        GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        GL11.glScalef(0.25F, 0.25F, 0.25F);
        GL11.glTranslatef((float)d6, (float)d8, 0.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glActiveTexture(GL11.GL_TEXTURE0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3D vec3D2;
        float f19 = (float)(vec3D2 = this.worldObj.getCloudColor(partialTime)).xCoord;
        float f22 = (float)vec3D2.yCoord;
        float f18 = (float)vec3D2.zCoord;
        float f11;
        if(this.mc.gameSettings.anaglyph) {
            partialTime = (f19 * 30.0F + f22 * 59.0F + f18 * 11.0F) / 100.0F;
            f11 = (f19 * 30.0F + f22 * 70.0F) / 100.0F;
            f18 = (f19 * 30.0F + f18 * 70.0F) / 100.0F;
            f19 = partialTime;
            f22 = f11;
            f18 = f18;
        }

        partialTime = (float)MathHelper.floor_double(d6) * 0.00390625F;
        f11 = (float)MathHelper.floor_double(d8) * 0.00390625F;
        float f20 = (float)(d6 - (double)MathHelper.floor_double(d6));
        float f7 = (float)(d8 - (double)MathHelper.floor_double(d8));
        GL11.glScalef(12.0F, 1.0F, 12.0F);

        for(int i21 = 0; i21 < 2; ++i21) {
            if(i21 == 0) {
                GL11.glColorMask(false, false, false, false);
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            for(int i9 = -2; i9 <= 3; ++i9) {
                for(int i12 = -2; i12 <= 3; ++i12) {
                    tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f13 = (float)(i9 << 3);
                    float f14 = (float)(i12 << 3);
                    float f15 = f13 - f20;
                    float f16 = f14 - f7;
                    if(f3 > -5.0F) {
                        tessellator4.setColorRGBA_F(f19 * 0.7F, f22 * 0.7F, f18 * 0.7F, 0.8F);
                        tessellator4.setNormal(0.0F, -1.0F, 0.0F);
                        tessellator4.addVertexWithUV((double)f15, (double)f3, (double)(f16 + 8.0F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)f3, (double)(f16 + 8.0F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)f3, (double)f16, (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)f15, (double)f3, (double)f16, (double)(f13 * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                    }

                    if(f3 <= 5.0F) {
                        tessellator4.setColorRGBA_F(f19, f22, f18, 0.8F);
                        tessellator4.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator4.addVertexWithUV((double)f15, (double)(f3 + 4.0F - 9.765625E-4F), (double)(f16 + 8.0F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)(f16 + 8.0F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)f16, (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                        tessellator4.addVertexWithUV((double)f15, (double)(f3 + 4.0F - 9.765625E-4F), (double)f16, (double)(f13 * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                    }

                    tessellator4.setColorRGBA_F(f19 * 0.9F, f22 * 0.9F, f18 * 0.9F, 0.8F);
                    int i17;
                    if(i9 >= 0) {
                        tessellator4.setNormal(-1.0F, 0.0F, 0.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17), (double)f3, (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17), (double)(f3 + 4.0F), (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17), (double)(f3 + 4.0F), (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17), (double)f3, (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                        }
                    }

                    if(i9 <= 1) {
                        tessellator4.setNormal(1.0F, 0.0F, 0.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)f3, (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f3 + 4.0F), (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f3 + 4.0F), (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)f3, (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f11));
                        }
                    }

                    tessellator4.setColorRGBA_F(f19 * 0.8F, f22 * 0.8F, f18 * 0.8F, 0.8F);
                    if(i12 >= 0) {
                        tessellator4.setNormal(0.0F, 0.0F, -1.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator4.addVertexWithUV((double)f15, (double)(f3 + 4.0F), (double)(f16 + (float)i17), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)(f3 + 4.0F), (double)(f16 + (float)i17), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)f3, (double)(f16 + (float)i17), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)f15, (double)f3, (double)(f16 + (float)i17), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                        }
                    }

                    if(i12 <= 1) {
                        tessellator4.setNormal(0.0F, 0.0F, 1.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator4.addVertexWithUV((double)f15, (double)(f3 + 4.0F), (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)(f3 + 4.0F), (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)(f15 + 8.0F), (double)f3, (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                            tessellator4.addVertexWithUV((double)f15, (double)f3, (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f11));
                        }
                    }

                    tessellator4.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glActiveTexture(GL11.GL_TEXTURE1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glActiveTexture(GL11.GL_TEXTURE0);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
    }

    private FloatBuffer flipBuffer(float r, float g, float b, float a) {
        this.cloudBuffer.clear();
        this.cloudBuffer.put(r).put(0.0F).put(b).put(0.0F);
        this.cloudBuffer.flip();
        return this.cloudBuffer;
    }

    public final void updateRenderers(EntityPlayer playerEntity) {
        try {
            Collections.sort(this.worldRenderersToUpdate, new RenderSorter(playerEntity));
        } catch (IllegalArgumentException ex) {
        }

        int i2 = this.worldRenderersToUpdate.size() - 1;
        int i3 = this.worldRenderersToUpdate.size();

        for(int i4 = 0; i4 < i3; ++i4) {
            WorldRenderer worldRenderer5;
            if((worldRenderer5 = (WorldRenderer)this.worldRenderersToUpdate.get(i2 - i4)).distanceToEntitySquared(playerEntity) > 2500.0F && i4 > 2) {
                return;
            }

            this.worldRenderersToUpdate.remove(worldRenderer5);
            worldRenderer5.updateRenderer();
            worldRenderer5.needsUpdate = false;
        }

    }


    public final void drawBlockBreaking(EntityPlayer playerEntity, MovingObjectPosition blockPosition, int blockId, ItemStack stack, float partialTime) {
        Tessellator t = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        if(this.damagePartialTime > 0.0F) {
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
            int i17 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i17);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            i17 = this.worldObj.getBlockId(blockPosition.blockX, blockPosition.blockY, blockPosition.blockZ);
            Block block = i17 > 0 ? Block.blocksList[i17] : null;
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPolygonOffset(-1.0F, -1.0F);
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            double d10 = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTime;
            double d12 = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTime;
            double d14 = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTime;
            t.setTranslationD(-d10, -d12, -d14);
            t.disableColor();
            if(block == null) {
                block = Block.stone;
            }

            this.globalRenderBlocks.renderBlockUsingTexture(block, blockPosition.blockX, blockPosition.blockY, blockPosition.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
            t.draw();
            t.setTranslationD(0.0D, 0.0D, 0.0D);
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    public final void drawSelectionBox(EntityPlayer playerEntity, MovingObjectPosition position, int blockId, float partialTime) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        blockId = this.worldObj.getBlockId(position.blockX, position.blockY, position.blockZ);
        if(blockId > 0) {
            double d6 = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTime;
            double d8 = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTime;
            double d10 = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTime;
            AxisAlignedBB axisAlignedBB = Block.blocksList[blockId].getSelectedBoundingBoxFromPool(this.worldObj, position.blockX, position.blockY, position.blockZ).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).getOffsetBoundingBox(-d6, -d8, -d10);
            Tessellator t = Tessellator.instance;
            t.startDrawing(3, DefaultVertexFormats.POSITION);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            t.draw();
            t.startDrawing(3, DefaultVertexFormats.POSITION);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            t.draw();
            t.startDrawing(1, DefaultVertexFormats.POSITION);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            t.draw();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        minX >>= 4;
        minY >>= 4;
        minZ >>= 4;
        maxX >>= 4;
        maxY >>= 4;
        maxZ >>= 4;

        for(minX = minX; minX <= maxX; ++minX) {
            int i7;
            if((i7 = minX % this.renderChunksWide) < 0) {
                i7 += this.renderChunksWide;
            }

            for(int i8 = minY; i8 <= maxY; ++i8) {
                int i9;
                if((i9 = i8 % this.renderChunksTall) < 0) {
                    i9 += this.renderChunksTall;
                }

                for(int i10 = minZ; i10 <= maxZ; ++i10) {
                    int i11;
                    if((i11 = i10 % this.renderChunksDeep) < 0) {
                        i11 += this.renderChunksDeep;
                    }

                    i11 = (i11 * this.renderChunksTall + i9) * this.renderChunksWide + i7;
                    WorldRenderer worldRenderer12;
                    if(!(worldRenderer12 = this.worldRenderers[i11]).needsUpdate) {
                        worldRenderer12.needsUpdate = true;
                        this.worldRenderersToUpdate.add(worldRenderer12);
                    }
                }
            }
        }

    }

    public final void markBlockNeedsUpdate(int x, int y, int z) {
        this.markBlocksForUpdate(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    public final void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.markBlocksForUpdate(minX - 1, minY - 1, minZ - 1, maxX + 1, maxY + 1, maxZ + 1);
    }

    public final void clipRenderersByFrustrum(Frustrum frustrum) {
        for(int i2 = 0; i2 < this.worldRenderers.length; ++i2) {
            this.worldRenderers[i2].updateInFrustrum(frustrum);
        }

    }

    public final void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
        this.mc.sndManager.playSound(soundName, (float)x, (float)y, (float)z, volume, pitch);
    }

    public final void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
        double d14 = this.worldObj.playerEntity.posX - x;
        double d16 = this.worldObj.playerEntity.posY - y;
        double d18 = this.worldObj.playerEntity.posZ - z;
        if(d14 * d14 + d16 * d16 + d18 * d18 <= 256.0D) {
            if(particleName == "bubble") {
                this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, x, y, z, motionX, motionY, motionZ));
            } else if(particleName == "smoke") {
                this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, x, y, z));
            } else if(particleName == "explode") {
                this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, x, y, z, motionX, motionY, motionZ));
            } else if(particleName == "flame") {
                this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, x, y, z));
            } else if(particleName == "lava") {
                this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, x, y, z));
            } else if(particleName == "splash") {
                this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, x, y, z));
            } else {
                if(particleName == "largesmoke") {
                    this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, x, y, z, 2.5F));
                }

            }
        }
    }

    public final void obtainEntitySkin(Entity entity) {
        if(entity.skinUrl != null) {
            this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }

    }

    public final void releaseEntitySkin(Entity entity) {
        if(entity.skinUrl != null) {
            this.renderEngine.releaseImageData(entity.skinUrl);
        }

    }

    public final void updateAllRenderers() {
        for(int i1 = 0; i1 < this.worldRenderers.length; ++i1) {
            if(!this.worldRenderers[i1].needsUpdate && this.worldRenderers[i1].isChunkLit) {
                this.worldRenderers[i1].needsUpdate = true;
                this.worldRenderersToUpdate.add(this.worldRenderers[i1]);
            }
        }

    }
}
