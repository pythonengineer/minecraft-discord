package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.vector.Matrix4f;
import net.lax1dude.eaglercraft.vector.Vector4f;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EntityBubbleFX;
import net.minecraft.client.effect.EntityExplodeFX;
import net.minecraft.client.effect.EntityFlameFX;
import net.minecraft.client.effect.EntityLavaFX;
import net.minecraft.client.effect.EntityReddustFX;
import net.minecraft.client.effect.EntitySmokeFX;
import net.minecraft.client.effect.EntitySplashFX;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.Frustum;
import net.minecraft.client.render.camera.ICamera;
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

public class RenderGlobal implements IWorldAccess {
    private List tileEntities = new ArrayList();
    private World theWorld;
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
    private IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled = false;
    private int cloudTickCounter = 0;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
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
    int[] dummyBuf50k = new int[50000];
    IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private int renderersSkippingRenderPass;
    private List glRenderLists = new ArrayList();
    private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};
    int dummyRenderInt = 0;
    int unusedGLCallList = GLAllocation.generateDisplayLists(1);
    double prevSortX = -9999.0D;
    double prevSortY = -9999.0D;
    double prevSortZ = -9999.0D;
	public float damagePartialTime;
    int frustumCheckOffset = 0;

    public RenderGlobal(Minecraft mc, RenderEngine renderEngine) {
        this.mc = mc;
        this.renderEngine = renderEngine;
        this.glRenderListBase = GLAllocation.generateDisplayLists(786432);
        this.occlusionEnabled = mc.getOpenGlCapsChecker().checkARBOcclusion();
        if(this.occlusionEnabled) {
            this.occlusionResult.clear();
            this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(262144);
            this.glOcclusionQueryBase.clear();
            this.glOcclusionQueryBase.position(0);
            this.glOcclusionQueryBase.limit(262144);
            GL11.glGenQueriesARB(this.glOcclusionQueryBase);
        }

        this.starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tessellator10 = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);

        int i3;
        int i5;
        for(i5 = -384; i5 <= 384; i5 += 64) {
            for(i3 = -384; i3 <= 384; i3 += 64) {
                tessellator10.startDrawingQuads(DefaultVertexFormats.POSITION);
                tessellator10.addVertex((double)i5, 16.0D, (double)i3);
                tessellator10.addVertex((double)(i5 + 64), 16.0D, (double)i3);
                tessellator10.addVertex((double)(i5 + 64), 16.0D, (double)(i3 + 64));
                tessellator10.addVertex((double)i5, 16.0D, (double)(i3 + 64));
                tessellator10.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        tessellator10.startDrawingQuads(DefaultVertexFormats.POSITION);

        for(i5 = -384; i5 <= 384; i5 += 64) {
            for(i3 = -384; i3 <= 384; i3 += 64) {
                tessellator10.addVertex((double)(i5 + 64), -16.0D, (double)i3);
                tessellator10.addVertex((double)i5, -16.0D, (double)i3);
                tessellator10.addVertex((double)i5, -16.0D, (double)(i3 + 64));
                tessellator10.addVertex((double)(i5 + 64), -16.0D, (double)(i3 + 64));
            }
        }

        tessellator10.draw();
        GL11.glEndList();
	}

    private void renderStars() {
        EaglercraftRandom random0 = new EaglercraftRandom(10842L);
        Tessellator tessellator1 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION);

        for(int i2 = 0; i2 < 1500; ++i2) {
            double d3 = (double)(random0.nextFloat() * 2.0F - 1.0F);
            double d5 = (double)(random0.nextFloat() * 2.0F - 1.0F);
            double d7 = (double)(random0.nextFloat() * 2.0F - 1.0F);
            double d9 = (double)(0.25F + random0.nextFloat() * 0.25F);
            double d11;
            if((d11 = d3 * d3 + d5 * d5 + d7 * d7) < 1.0D && d11 > 0.01D) {
                d11 = 1.0D / Math.sqrt(d11);
                d3 *= d11;
                d5 *= d11;
                d7 *= d11;
                double d13 = d3 * 100.0D;
                double d15 = d5 * 100.0D;
                double d17 = d7 * 100.0D;
                double d19;
                double d21 = Math.sin(d19 = Math.atan2(d3, d7));
                double d23 = Math.cos(d19);
                double d25;
                double d27 = Math.sin(d25 = Math.atan2(Math.sqrt(d3 * d3 + d7 * d7), d5));
                double d29 = Math.cos(d25);
                double d31;
                double d33 = Math.sin(d31 = random0.nextDouble() * Math.PI * 2.0D);
                double d35 = Math.cos(d31);

                for(int i58 = 0; i58 < 4; ++i58) {
                    double d38 = (double)((i58 & 2) - 1) * d9;
                    double d40 = (double)((i58 + 1 & 2) - 1) * d9;
                    double d42 = d38 * d35 - d40 * d33;
                    double d46 = d40 * d35 + d38 * d33;
                    double d48 = d42 * d27 + d29 * 0.0D;
                    double d50;
                    double d52 = (d50 = d27 * 0.0D - d42 * d29) * d21 - d46 * d23;
                    double d56 = d46 * d21 + d50 * d23;
                    tessellator1.addVertex(d13 + d52, d15 + d48, d17 + d56);
                }
            }
        }

        tessellator1.draw();
    }

    public void changeWorld(World world) {
        if(this.theWorld != null) {
            this.theWorld.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.set(world);
        this.theWorld = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if(world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }

    }

    public void loadRenderers() {
        Block.leaves.setGraphicsLevel(this.mc.options.fancyGraphics);
        this.renderDistance = this.mc.options.renderDistance;
        int i1;
        if(this.worldRenderers != null) {
            for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
                this.worldRenderers[i1].stopRendering();
            }
        }

        if((i1 = 64 << 3 - this.renderDistance) > 400) {
            i1 = 400;
        }

        this.renderChunksWide = i1 / 16 + 1;
        this.renderChunksTall = 8;
        this.renderChunksDeep = i1 / 16 + 1;
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
        this.tileEntities.clear();

        for(i3 = 0; i3 < this.renderChunksWide; ++i3) {
            for(int i4 = 0; i4 < this.renderChunksTall; ++i4) {
                for(int i5 = 0; i5 < this.renderChunksDeep; ++i5) {
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = new WorldRenderer(this.theWorld, this.tileEntities, i3 << 4, i4 << 4, i5 << 4, 16, this.glRenderListBase + i1);
                    if(this.occlusionEnabled) {
                        this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].glOcclusionQuery = this.glOcclusionQueryBase.get(i2);
                    }

                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isVisible = true;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isInFrustum = true;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].chunkIndex = i2++;
                    this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].markDirty();
                    this.sortedWorldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3];
                    this.worldRenderersToUpdate.add(this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3]);
                    i1 += 3;
                }
            }
        }

        if(this.theWorld != null) {
            EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
            this.markRenderersForNewPosition(MathHelper.floor_double(entityPlayerSP7.posX), MathHelper.floor_double(entityPlayerSP7.posY), MathHelper.floor_double(entityPlayerSP7.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayerSP7));
        }
    }

    public void renderEntities(Vec3D lookVector, ICamera frustrum, float partialTicks) {
        TileEntityRenderer.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, partialTicks);
        RenderManager.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, this.mc.options, partialTicks);
        this.countEntitiesTotal = 0;
        this.countEntitiesRendered = 0;
        this.countEntitiesHidden = 0;
        EntityPlayerSP entityPlayerSP4 = this.mc.thePlayer;
        RenderManager.renderPosX = entityPlayerSP4.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * (double)partialTicks;
        RenderManager.renderPosY = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * (double)partialTicks;
        RenderManager.renderPosZ = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * (double)partialTicks;
        TileEntityRenderer.staticPlayerX = entityPlayerSP4.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * (double)partialTicks;
        TileEntityRenderer.staticPlayerY = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * (double)partialTicks;
        TileEntityRenderer.staticPlayerZ = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * (double)partialTicks;
        List list30 = this.theWorld.getLoadedEntityList();
        this.countEntitiesTotal = list30.size();

        int i5;
        for(i5 = 0; i5 < list30.size(); ++i5) {
            Entity entity6 = (Entity)list30.get(i5);
            if(entity6.isInRangeToRenderVec3D(lookVector) && frustrum.isBoundingBoxInFrustum(entity6.boundingBox) && (entity6 != this.mc.thePlayer || this.mc.options.thirdPersonView)) {
                ++this.countEntitiesRendered;
                RenderManager.instance.renderEntity(entity6, partialTicks);
            }
        }

        for(i5 = 0; i5 < this.tileEntities.size(); ++i5) {
            TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(i5), partialTicks);
        }

    }

    public String getDebugInfoRenders() {
        return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    private void markRenderersForNewPosition(int x, int y, int z) {
        x -= 8;
        y -= 8;
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

    public int sortAndRender(EntityPlayer playerEntity, int callListId, double partialTime) {
        if(this.mc.options.renderDistance != this.renderDistance) {
            this.loadRenderers();
        }

        if(callListId == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
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

        int i21;
        if(this.occlusionEnabled && !this.mc.options.anaglyph && callListId == 0) {
            int i22 = 16;
            this.checkOcclusionQueryResult(0, 16);

            for(int i14 = 0; i14 < 16; ++i14) {
                this.sortedWorldRenderers[i14].isVisible = true;
            }

            i21 = 0 + this.renderSortedRenderers(0, 16, callListId, partialTime);

            do {
                int i12 = i22;
                if((i22 <<= 1) > this.sortedWorldRenderers.length) {
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
                        this.sortedWorldRenderers[i17].isInFrustum = false;
                    } else {
                        if(!this.sortedWorldRenderers[i17].isInFrustum) {
                            this.sortedWorldRenderers[i17].isVisible = true;
                        }

                        if(this.sortedWorldRenderers[i17].isInFrustum && !this.sortedWorldRenderers[i17].isWaitingOnOcclusionQuery) {
                            float f18 = MathHelper.sqrt_float(this.sortedWorldRenderers[i17].distanceToEntitySquared(playerEntity));
                            int i25 = (int)(1.0F + f18 / 128.0F);
                            if(this.cloudTickCounter % i25 == i17 % i25) {
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
                if(this.sortedWorldRenderers[worldRendererId].skipRenderPass[callListId]) {
                    ++this.renderersSkippingRenderPass;
                } else if(!this.sortedWorldRenderers[worldRendererId].isInFrustum) {
                    ++this.renderersBeingClipped;
                } else if(this.occlusionEnabled && !this.sortedWorldRenderers[worldRendererId].isVisible) {
                    ++this.renderersBeingOccluded;
                } else {
                    ++this.renderersBeingRendered;
                }
            }

            if(!this.sortedWorldRenderers[worldRendererId].skipRenderPass[callListId] && this.sortedWorldRenderers[worldRendererId].isInFrustum && this.sortedWorldRenderers[worldRendererId].isVisible && this.sortedWorldRenderers[worldRendererId].getGLCallListForPass(callListId) >= 0) {
                this.glRenderLists.add(this.sortedWorldRenderers[worldRendererId]);
                ++i6;
            }
        }

        EntityPlayerSP entityPlayerSP14 = this.mc.thePlayer;
        double d16 = this.mc.thePlayer.lastTickPosX + (entityPlayerSP14.posX - entityPlayerSP14.lastTickPosX) * partialTime;
        double d10 = entityPlayerSP14.lastTickPosY + (entityPlayerSP14.posY - entityPlayerSP14.lastTickPosY) * partialTime;
        double d12 = entityPlayerSP14.lastTickPosZ + (entityPlayerSP14.posZ - entityPlayerSP14.lastTickPosZ) * partialTime;
        worldRendererId = 0;

        for(rendererId = 0; rendererId < this.allRenderLists.length; ++rendererId) {
            this.allRenderLists[rendererId].reset();
        }

        for(rendererId = 0; rendererId < this.glRenderLists.size(); ++rendererId) {
            WorldRenderer worldRenderer15 = (WorldRenderer)this.glRenderLists.get(rendererId);
            int i5 = -1;

            for(int i7 = 0; i7 < worldRendererId; ++i7) {
                if(this.allRenderLists[i7].isRenderedAt(worldRenderer15.posXMinus, worldRenderer15.posYMinus, worldRenderer15.posZMinus)) {
                    i5 = i7;
                }
            }

            if(i5 < 0) {
                i5 = worldRendererId++;
                this.allRenderLists[i5].setLocation(worldRenderer15.posXMinus, worldRenderer15.posYMinus, worldRenderer15.posZMinus, d16, d10, d12);
            }

            this.allRenderLists[i5].render(worldRenderer15.getGLCallListForPass(callListId));
        }

        this.renderAllRenderLists(callListId, partialTime);
        return i6;
    }

    public void renderAllRenderLists(int callListId, double partialTime) {
        for(int i1 = 0; i1 < this.allRenderLists.length; ++i1) {
            this.allRenderLists[i1].render();
        }

    }

    public void updateClouds() {
        ++this.cloudTickCounter;
    }

    public void renderSky(float partialTime) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3D vec3D2;
        float f3 = (float)(vec3D2 = this.theWorld.getSkyColor(partialTime)).xCoord;
        float f4 = (float)vec3D2.yCoord;
        float f6 = (float)vec3D2.zCoord;
        if(this.mc.options.anaglyph) {
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
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(f3, f4, f6);
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
        GL11.glRotatef(this.theWorld.getCelestialAngle(partialTime) * 360.0F, 1.0F, 0.0F, 0.0F);
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
        float f1;
        if((f1 = this.theWorld.getStarBrightness(partialTime)) > 0.0F) {
            GL11.glColor4f(f1, f1, f1, f1);
            GL11.glCallList(this.starGLCallList);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glColor3f(f3 * 0.2F + 0.04F, f4 * 0.2F + 0.04F, f6 * 0.6F + 0.1F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glCallList(this.glSkyList2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float partialTime) {
        float f2;
        float f5;
        float f6;
        float f7;
        float f9;
        int i34;
        if(this.mc.options.fancyGraphics) {
            this.renderCloudsFancy(partialTime);
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE);
            f2 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)partialTime);
            Tessellator tessellator3 = Tessellator.instance;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Vec3D vec3D4;
            f5 = (float)(vec3D4 = this.theWorld.getCloudColor(partialTime)).xCoord;
            f6 = (float)vec3D4.yCoord;
            float f31 = (float)vec3D4.zCoord;
            if(this.mc.options.anaglyph) {
                f7 = (f5 * 30.0F + f6 * 59.0F + f31 * 11.0F) / 100.0F;
                f9 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
                float f10 = (f5 * 30.0F + f31 * 70.0F) / 100.0F;
                f5 = f7;
                f6 = f9;
                f31 = f10;
            }

            double d37 = this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)partialTime + (double)(((float)this.cloudTickCounter + partialTime) * 0.03F);
            double d11 = this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)partialTime;
            int i28 = MathHelper.floor_double(d37 / 2048.0D);
            int i36 = MathHelper.floor_double(d11 / 2048.0D);
            d37 -= (double)(i28 << 11);
            d11 -= (double)(i36 << 11);
            partialTime = 120.0F - f2 + 0.33F;
            f2 = (float)(d37 * 4.8828125E-4D);
            f7 = (float)(d11 * 4.8828125E-4D);
            tessellator3.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
            tessellator3.setColorRGBA_F(f5, f6, f31, 0.8F);

            for(int i32 = -256; i32 < 256; i32 += 32) {
                for(i34 = -256; i34 < 256; i34 += 32) {
                    tessellator3.addVertexWithUV((double)i32, (double)partialTime, (double)(i34 + 32), (double)((float)i32 * 4.8828125E-4F + f2), (double)((float)(i34 + 32) * 4.8828125E-4F + f7));
                    tessellator3.addVertexWithUV((double)(i32 + 32), (double)partialTime, (double)(i34 + 32), (double)((float)(i32 + 32) * 4.8828125E-4F + f2), (double)((float)(i34 + 32) * 4.8828125E-4F + f7));
                    tessellator3.addVertexWithUV((double)(i32 + 32), (double)partialTime, (double)i34, (double)((float)(i32 + 32) * 4.8828125E-4F + f2), (double)((float)i34 * 4.8828125E-4F + f7));
                    tessellator3.addVertexWithUV((double)i32, (double)partialTime, (double)i34, (double)((float)i32 * 4.8828125E-4F + f2), (double)((float)i34 * 4.8828125E-4F + f7));
                }
            }

            tessellator3.draw();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    public void renderCloudsFancy(float partialTime) {
        float f2;
        float f5;
        float f6;
        float f7;
        float f9;
        int i34;
        GL11.glDisable(GL11.GL_CULL_FACE);
        float f30 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)partialTime);
        Tessellator tessellator33 = Tessellator.instance;
        float f4 = 12.0F;
        double d24 = (this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)partialTime + (double)(((float)this.cloudTickCounter + partialTime) * 0.03F)) / (double)f4;
        double d26 = (this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)partialTime) / (double)f4 + (double)0.33F;
        f30 = 108.0F - f30 + 0.33F;
        i34 = MathHelper.floor_double(d24 / 2048.0D);
        int i35 = MathHelper.floor_double(d26 / 2048.0D);
        d24 -= (double)(i34 << 11);
        d26 -= (double)(i35 << 11);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3D vec3D29;
        f5 = (float)(vec3D29 = this.theWorld.getCloudColor(partialTime)).xCoord;
        f6 = (float)vec3D29.yCoord;
        f2 = (float)vec3D29.zCoord;
        if(this.mc.options.anaglyph) {
            partialTime = (f5 * 30.0F + f6 * 59.0F + f2 * 11.0F) / 100.0F;
            f7 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
            f2 = (f5 * 30.0F + f2 * 70.0F) / 100.0F;
            f5 = partialTime;
            f6 = f7;
            f2 = f2;
        }

        partialTime = (float)MathHelper.floor_double(d24) * 0.00390625F;
        f7 = (float)MathHelper.floor_double(d26) * 0.00390625F;
        float f8 = (float)(d24 - (double)MathHelper.floor_double(d24));
        f9 = (float)(d26 - (double)MathHelper.floor_double(d26));
        GL11.glScalef(12.0F, 1.0F, 12.0F);

        for(int i38 = 0; i38 < 2; ++i38) {
            if(i38 == 0) {
                GL11.glColorMask(false, false, false, false);
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            for(int i39 = -2; i39 <= 3; ++i39) {
                for(int i12 = -2; i12 <= 3; ++i12) {
                    tessellator33.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f13 = (float)(i39 << 3);
                    float f14 = (float)(i12 << 3);
                    float f15 = f13 - f8;
                    float f16 = f14 - f9;
                    if(f30 > -5.0F) {
                        tessellator33.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f2 * 0.7F, 0.8F);
                        tessellator33.setNormal(0.0F, -1.0F, 0.0F);
                        tessellator33.addVertexWithUV((double)f15, (double)f30, (double)(f16 + 8.0F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)f30, (double)(f16 + 8.0F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)f30, (double)f16, (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)f15, (double)f30, (double)f16, (double)(f13 * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                    }

                    if(f30 <= 5.0F) {
                        tessellator33.setColorRGBA_F(f5, f6, f2, 0.8F);
                        tessellator33.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator33.addVertexWithUV((double)f15, (double)(f30 + 4.0F - 9.765625E-4F), (double)(f16 + 8.0F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)(f30 + 4.0F - 9.765625E-4F), (double)(f16 + 8.0F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)(f30 + 4.0F - 9.765625E-4F), (double)f16, (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                        tessellator33.addVertexWithUV((double)f15, (double)(f30 + 4.0F - 9.765625E-4F), (double)f16, (double)(f13 * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                    }

                    tessellator33.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f2 * 0.9F, 0.8F);
                    int i17;
                    if(i39 >= 0) {
                        tessellator33.setNormal(-1.0F, 0.0F, 0.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17), (double)f30, (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17), (double)(f30 + 4.0F), (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17), (double)(f30 + 4.0F), (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17), (double)f30, (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                        }
                    }

                    if(i39 <= 1) {
                        tessellator33.setNormal(1.0F, 0.0F, 0.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)f30, (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f30 + 4.0F), (double)(f16 + 8.0F), (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)((f14 + 8.0F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f30 + 4.0F), (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + (float)i17 + 1.0F - 9.765625E-4F), (double)f30, (double)f16, (double)((f13 + (float)i17 + 0.5F) * 0.00390625F + partialTime), (double)(f14 * 0.00390625F + f7));
                        }
                    }

                    tessellator33.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f2 * 0.8F, 0.8F);
                    if(i12 >= 0) {
                        tessellator33.setNormal(0.0F, 0.0F, -1.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator33.addVertexWithUV((double)f15, (double)(f30 + 4.0F), (double)(f16 + (float)i17), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)(f30 + 4.0F), (double)(f16 + (float)i17), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)f30, (double)(f16 + (float)i17), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)f15, (double)f30, (double)(f16 + (float)i17), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                        }
                    }

                    if(i12 <= 1) {
                        tessellator33.setNormal(0.0F, 0.0F, 1.0F);

                        for(i17 = 0; i17 < 8; ++i17) {
                            tessellator33.addVertexWithUV((double)f15, (double)(f30 + 4.0F), (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)(f30 + 4.0F), (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)(f15 + 8.0F), (double)f30, (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)((f13 + 8.0F) * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                            tessellator33.addVertexWithUV((double)f15, (double)f30, (double)(f16 + (float)i17 + 1.0F - 9.765625E-4F), (double)(f13 * 0.00390625F + partialTime), (double)((f14 + (float)i17 + 0.5F) * 0.00390625F + f7));
                        }
                    }

                    tessellator33.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public boolean updateRenderers(EntityPlayer playerEntity, boolean z2) {
        try {
            Collections.sort(this.worldRenderersToUpdate, new RenderSorter(playerEntity));
        } catch (IllegalArgumentException ex) {
        }

        int i3 = this.worldRenderersToUpdate.size() - 1;
        int i4 = this.worldRenderersToUpdate.size();

        for(int i5 = 0; i5 < i4; ++i5) {
            WorldRenderer worldRenderer6 = (WorldRenderer)this.worldRenderersToUpdate.get(i3 - i5);
            if(!z2) {
                if(worldRenderer6.distanceToEntitySquared(playerEntity) > 1024.0F) {
                    if(worldRenderer6.isInFrustum) {
                        if(i5 >= 3) {
                            return false;
                        }
                    } else if(i5 >= 1) {
                        return false;
                    }
                }
            } else if(!worldRenderer6.isInFrustum) {
                continue;
            }

            worldRenderer6.updateRenderer();
            this.worldRenderersToUpdate.remove(worldRenderer6);
            worldRenderer6.needsUpdate = false;
        }

        return this.worldRenderersToUpdate.size() == 0;
    }


    public void drawBlockBreaking(EntityPlayer playerEntity, MovingObjectPosition blockPosition, int blockId, ItemStack stack, float partialTime) {
        Tessellator t = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        int i8;
        if(blockId == 0) {
            if(this.damagePartialTime > 0.0F) {
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
                int i17 = this.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, i17);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                i17 = this.theWorld.getBlockId(blockPosition.blockX, blockPosition.blockY, blockPosition.blockZ);
                Block block = i17 > 0 ? Block.blocksList[i17] : null;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glPolygonOffset(-1.0F, -1.0F);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                double d10 = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTime;
                double d12 = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTime;
                double d14 = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTime;
                double yOffsetBug = 0.01;
                if (blockPosition.blockY > playerEntity.posY) {
                    yOffsetBug = -yOffsetBug;
                }

                t.setTranslationD(-d10, -d12 + yOffsetBug, -d14);
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
        } else if(stack != null) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            float f16 = MathHelper.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
            GL11.glColor4f(f16, f16, f16, MathHelper.sin((float)EagRuntime.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
            i8 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
            int i17 = blockPosition.blockX;
            int i18 = blockPosition.blockY;
            int i11 = blockPosition.blockZ;
            if(blockPosition.sideHit == 0) {
                --i18;
            }

            if(blockPosition.sideHit == 1) {
                ++i18;
            }

            if(blockPosition.sideHit == 2) {
                --i11;
            }

            if(blockPosition.sideHit == 3) {
                ++i11;
            }

            if(blockPosition.sideHit == 4) {
                --i17;
            }

            if(blockPosition.sideHit == 5) {
                ++i17;
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    public void drawSelectionBox(EntityPlayer playerEntity, MovingObjectPosition position, int blockId, ItemStack itemStack, float partialTime) {
        if(position.typeOfHit == 0) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            float f6 = 0.002F;
            blockId = this.theWorld.getBlockId(position.blockX, position.blockY, position.blockZ);
            if(blockId > 0) {
                Block.blocksList[blockId].setBlockBoundsBasedOnState(this.theWorld, position.blockX, position.blockY, position.blockZ);
                double d6 = playerEntity.lastTickPosX + (playerEntity.posX - playerEntity.lastTickPosX) * (double)partialTime;
                double d8 = playerEntity.lastTickPosY + (playerEntity.posY - playerEntity.lastTickPosY) * (double)partialTime;
                double d10 = playerEntity.lastTickPosZ + (playerEntity.posZ - playerEntity.lastTickPosZ) * (double)partialTime;
                this.drawOutlinedBoundingBox(Block.blocksList[blockId].getSelectedBoundingBoxFromPool(this.theWorld, position.blockX, position.blockY, position.blockZ).expand((double)f6, (double)f6, (double)f6).getOffsetBoundingBox(-d6, -d8, -d10));
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }

    }

    private void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
        final float lineWidth = 2.0F;
        final float halfWidth = lineWidth * 0.5F;

        double minX = axisAlignedBB.minX;
        double minY = axisAlignedBB.minY;
        double minZ = axisAlignedBB.minZ;
        double maxX = axisAlignedBB.maxX;
        double maxY = axisAlignedBB.maxY;
        double maxZ = axisAlignedBB.maxZ;

        double[][] edges = new double[][] {
            { minX, minY, minZ, maxX, minY, minZ },
            { maxX, minY, minZ, maxX, minY, maxZ },
            { maxX, minY, maxZ, minX, minY, maxZ },
            { minX, minY, maxZ, minX, minY, minZ },
            { minX, maxY, minZ, maxX, maxY, minZ },
            { maxX, maxY, minZ, maxX, maxY, maxZ },
            { maxX, maxY, maxZ, minX, maxY, maxZ },
            { minX, maxY, maxZ, minX, maxY, minZ },
            { minX, minY, minZ, minX, maxY, minZ },
            { maxX, minY, minZ, maxX, maxY, minZ },
            { maxX, minY, maxZ, maxX, maxY, maxZ },
            { minX, minY, maxZ, minX, maxY, maxZ },
        };

        int[] vp = new int[4];
        GL11.glGetInteger(GL11.GL_VIEWPORT, vp);
        float vpW = (float) vp[2];
        float vpH = (float) vp[3];
        if (vpW <= 0.0F || vpH <= 0.0F) {
            return;
        }

        Matrix4f mvp = Matrix4f.mul(GL11.getProjectionReference(), GL11.getModelViewReference(), null);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glDisable(GL11.GL_CULL_FACE);

        Tessellator t = Tessellator.instance;
        t.startDrawingQuads(DefaultVertexFormats.POSITION);

        Vector4f p0 = new Vector4f();
        Vector4f p1 = new Vector4f();

        for (int i = 0; i < edges.length; ++i) {
            double[] e = edges[i];
            p0.set((float) e[0], (float) e[1], (float) e[2], 1.0F);
            p1.set((float) e[3], (float) e[4], (float) e[5], 1.0F);
            Matrix4f.transform(mvp, p0, p0);
            Matrix4f.transform(mvp, p1, p1);

            float d0 = p0.z + p0.w;
            float d1 = p1.z + p1.w;
            if (d0 < 0.0F && d1 < 0.0F) {
                continue;
            }
            if (d0 < 0.0F) {
                float ct = d0 / (d0 - d1);
                p0.x = p0.x + ct * (p1.x - p0.x);
                p0.y = p0.y + ct * (p1.y - p0.y);
                p0.z = p0.z + ct * (p1.z - p0.z);
                p0.w = p0.w + ct * (p1.w - p0.w);
            } else if (d1 < 0.0F) {
                float ct = d0 / (d0 - d1);
                p1.x = p0.x + ct * (p1.x - p0.x);
                p1.y = p0.y + ct * (p1.y - p0.y);
                p1.z = p0.z + ct * (p1.z - p0.z);
                p1.w = p0.w + ct * (p1.w - p0.w);
            }

            float invW0 = 1.0F / p0.w;
            float invW1 = 1.0F / p1.w;
            float ndc0x = p0.x * invW0;
            float ndc0y = p0.y * invW0;
            float ndc0z = p0.z * invW0;
            float ndc1x = p1.x * invW1;
            float ndc1y = p1.y * invW1;
            float ndc1z = p1.z * invW1;

            float dxScreen = (ndc1x - ndc0x) * vpW * 0.5F;
            float dyScreen = (ndc1y - ndc0y) * vpH * 0.5F;
            float lenScreen = (float) Math.sqrt(dxScreen * dxScreen + dyScreen * dyScreen);
            if (lenScreen < 1.0e-6F) {
                continue;
            }
            float invLen = 1.0F / lenScreen;
            float perpScreenX = -dyScreen * invLen * halfWidth;
            float perpScreenY = dxScreen * invLen * halfWidth;
            float ndcOffX = perpScreenX * 2.0F / vpW;
            float ndcOffY = perpScreenY * 2.0F / vpH;

            t.addVertex(ndc0x - ndcOffX, ndc0y - ndcOffY, ndc0z);
            t.addVertex(ndc0x + ndcOffX, ndc0y + ndcOffY, ndc0z);
            t.addVertex(ndc1x + ndcOffX, ndc1y + ndcOffY, ndc1z);
            t.addVertex(ndc1x - ndcOffX, ndc1y - ndcOffY, ndc1z);
        }

        t.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        minX = MathHelper.bucketInt(minX, 16);
        minY = MathHelper.bucketInt(minY, 16);
        minZ = MathHelper.bucketInt(minZ, 16);
        maxX = MathHelper.bucketInt(maxX, 16);
        maxY = MathHelper.bucketInt(maxY, 16);
        maxZ = MathHelper.bucketInt(maxZ, 16);

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
                        this.worldRenderersToUpdate.add(worldRenderer12);
                    }

                    worldRenderer12.markDirty();
                }
            }
        }

    }

    public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
        this.markBlocksForUpdate(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    public void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.markBlocksForUpdate(minX - 1, minY - 1, minZ - 1, maxX + 1, maxY + 1, maxZ + 1);
    }

    public void clipRenderersByFrustum(Frustum frustrum, float partialTicks) {
        for(int i2 = 0; i2 < this.worldRenderers.length; ++i2) {
            if(!this.worldRenderers[i2].skipAllRenderPasses() && (!this.worldRenderers[i2].isInFrustum || (i2 + this.frustumCheckOffset & 15) == 0)) {
                this.worldRenderers[i2].updateInFrustum(frustrum);
            }
        }

        ++this.frustumCheckOffset;
    }

    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
        float f10 = 16.0F;
        if(volume > 1.0F) {
            f10 *= volume;
        }

        if(this.mc.thePlayer.getDistanceSq(x, y, z) < (double)(f10 * f10)) {
            this.mc.sndManager.playSound(soundName, (float)x, (float)y, (float)z, volume, pitch);
        }

    }

    public void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
        double d14 = this.mc.thePlayer.posX - x;
        double d16 = this.mc.thePlayer.posY - y;
        double d18 = this.mc.thePlayer.posZ - z;
        if(d14 * d14 + d16 * d16 + d18 * d18 <= 256.0D) {
            if(particleName == "bubble") {
                this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.theWorld, x, y, z, motionX, motionY, motionZ));
            } else if(particleName == "smoke") {
                this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.theWorld, x, y, z));
            } else if(particleName == "explode") {
                this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.theWorld, x, y, z, motionX, motionY, motionZ));
            } else if(particleName == "flame") {
                this.mc.effectRenderer.addEffect(new EntityFlameFX(this.theWorld, x, y, z, motionX, motionY, motionZ));
            } else if(particleName == "lava") {
                this.mc.effectRenderer.addEffect(new EntityLavaFX(this.theWorld, x, y, z));
            } else if(particleName == "largesmoke") {
                this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.theWorld, x, y, z, 2.5F));
            } else if(particleName == "reddust") {
                this.mc.effectRenderer.addEffect(new EntityReddustFX(this.theWorld, x, y, z));
            }

        }
    }

    public void obtainEntitySkin(Entity entity) {
        if(entity.skinUrl != null) {
            this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }

    }

    public void releaseEntitySkin(Entity entity) {
        if(entity.skinUrl != null) {
            this.renderEngine.releaseImageData(entity.skinUrl);
        }

    }

    public void updateAllRenderers() {
        for(int i1 = 0; i1 < this.worldRenderers.length; ++i1) {
            if(this.worldRenderers[i1].isChunkLit) {
                if(!this.worldRenderers[i1].needsUpdate) {
                    this.worldRenderersToUpdate.add(this.worldRenderers[i1]);
                }

                this.worldRenderers[i1].markDirty();
            }
        }

    }
}
