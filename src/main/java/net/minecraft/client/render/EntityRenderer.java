package net.minecraft.client.render;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class EntityRenderer {
    private Minecraft mc;
    private float fogColorMultiplier = 1.0F;
    private boolean displayActive = false;
    private float farPlaneDistance = 0.0F;
    public ItemRenderer itemRenderer;
    private int entityRendererInt1;
    private Entity pointedEntity = null;
    private ByteBuffer entityByteBuffer;
    private FloatBuffer entityFloatBuffer = BufferUtils.createFloatBuffer(16);
    private EaglercraftRandom random = new EaglercraftRandom();
    private volatile int unusedInt0 = 0;
    private volatile int unusedInt1 = 0;
    private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
    private float fogColorRed;
    private float fogColorGreen;
    private float fogColorBlue;
    private float prevFogColor;
    private float fogColor;

    public EntityRenderer(Minecraft var1) {
        this.mc = var1;
        this.itemRenderer = new ItemRenderer(var1);
    }

    public final void updateRenderer() {
        this.prevFogColor = this.fogColor;
        float var1 = this.mc.theWorld.getBlockLightValue((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ);
        float var2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
        var1 = var1 * (1.0F - var2) + var2;
        this.fogColor += (var1 - this.fogColor) * 0.1F;
        ++this.entityRendererInt1;
        this.itemRenderer.updateEquippedItem();
        if(this.mc.renderRain) {
            EntityRenderer var13 = this;
            EntityPlayerSP var14 = this.mc.thePlayer;
            World var3 = this.mc.theWorld;
            int var4 = (int)var14.posX;
            int var5 = (int)var14.posY;
            int var15 = (int)var14.posZ;

            for(int var6 = 0; var6 < 50; ++var6) {
                int var7 = var4 + var13.random.nextInt(9) - 4;
                int var8 = var15 + var13.random.nextInt(9) - 4;
                int var9 = var3.getMapHeight(var7, var8);
                int var10 = var3.getBlockId(var7, var9 - 1, var8);
                if(var9 <= var5 + 4 && var9 >= var5 - 4) {
                    float var11 = var13.random.nextFloat();
                    float var12 = var13.random.nextFloat();
                    if(var10 > 0) {
                        this.mc.effectRenderer.addEffect(new EntityRainFX(var3, (float)var7 + var11, (float)var9 + 0.1F - Block.blocksList[var10].minY, (float)var8 + var12));
                    }
                }
            }
        }

    }

    private Vec3D orientCamera(float var1) {
        EntityPlayerSP var2 = this.mc.thePlayer;
        float var3 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
        float var4 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1;
        var1 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
        return new Vec3D(var3, var4, var1);
    }

    private void hurtCameraEffect(float var1) {
        EntityPlayerSP var2 = this.mc.thePlayer;
        float var3 = (float)var2.hurtTime - var1;
        if(var2.health <= 0) {
            var1 += (float)var2.deathTime;
            GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if(var3 >= 0.0F) {
            var3 /= (float)var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float)Math.PI);
            var1 = var2.attackedAtYaw;
            if(Float.isNaN(var3)) {
                var3 = 0.0F;
            }

            GL11.glRotatef(-var1, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float var1) {
        EntityPlayerSP var2 = this.mc.thePlayer;
        float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
        var3 = var2.distanceWalkedModified + var3 * var1;
        float var4 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
        var1 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
        GL11.glTranslatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 0.5F, -Math.abs(MathHelper.cos(var3 * (float)Math.PI) * var4), 0.0F);
        GL11.glRotatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 3.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(Math.abs(MathHelper.cos(var3 * (float)Math.PI + 0.2F) * var4) * 5.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var1, 1.0F, 0.0F, 0.0F);
    }

    public final void updateCameraAndRender(float var1) {
        if(this.displayActive && !Display.isActive()) {
            this.mc.displayInGameMenu();
        }

        this.displayActive = Display.isActive();
        int var2;
        int var3;
        float var5;
        float var7;
        float var8;
        if(this.mc.ingameFocus) {
            var2 = PointerInputAbstraction.getDX();
            var3 = PointerInputAbstraction.getDY();
            byte var4 = 1;
            if(this.mc.options.invertMouse) {
                var4 = -1;
            }

            float var10001 = (float)var2;
            float var6 = (float)(var3 * var4);
            var5 = var10001;
            EntityPlayerSP var25 = this.mc.thePlayer;
            var7 = var25.rotationPitch;
            var8 = var25.rotationYaw;
            var25.rotationYaw = (float)((double)var25.rotationYaw + (double)var5 * 0.15D);
            var25.rotationPitch = (float)((double)var25.rotationPitch - (double)var6 * 0.15D);
            if(var25.rotationPitch < -90.0F) {
                var25.rotationPitch = -90.0F;
            }

            if(var25.rotationPitch > 90.0F) {
                var25.rotationPitch = 90.0F;
            }

            var25.prevRotationPitch += var25.rotationPitch - var7;
            var25.prevRotationYaw += var25.rotationYaw - var8;
        }

        var2 = this.mc.scaledResolution.getScaledWidth();
        var3 = this.mc.scaledResolution.getScaledHeight();
        int var27 = PointerInputAbstraction.getX() * var2 / this.mc.displayWidth;
        int var28 = var3 - PointerInputAbstraction.getY() * var3 / this.mc.displayHeight - 1;
        if(this.mc.theWorld != null) {
            var5 = var1;
            float var11 = var1;
            EntityPlayerSP var12 = this.mc.thePlayer;
            var1 = var12.prevRotationPitch + (var12.rotationPitch - var12.prevRotationPitch) * var1;
            float var13 = var12.prevRotationYaw + (var12.rotationYaw - var12.prevRotationYaw) * var11;
            Vec3D var14 = this.orientCamera(var11);
            float var15 = MathHelper.cos(-var13 * ((float)Math.PI / 180.0F) - (float)Math.PI);
            float var16 = MathHelper.sin(-var13 * ((float)Math.PI / 180.0F) - (float)Math.PI);
            var13 = MathHelper.cos(-var1 * ((float)Math.PI / 180.0F));
            float var17 = MathHelper.sin(-var1 * ((float)Math.PI / 180.0F));
            var7 = var16 * var13;
            float var9 = var15 * var13;
            float var18 = this.mc.playerController.getBlockReachDistance();
            Vec3D var22 = var14.addVector(var7 * var18, var17 * var18, var9 * var18);
            this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var14, var22);
            if(this.mc.objectMouseOver != null) {
                this.mc.objectMouseOver.hitVec.distanceTo(var14);
            }

            var14 = this.orientCamera(var11);
            var22 = var14.addVector(var7 * 32.0F, var17 * 32.0F, var9 * 32.0F);
            this.pointedEntity = null;
            List var19 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(var12, var12.boundingBox.addCoord(var7 * 32.0F, var17 * 32.0F, var9 * 32.0F));
            float var20 = 0.0F;

            for(int var21 = 0; var21 < var19.size(); ++var21) {
                Entity var281 = (Entity)var19.get(var21);
                if(var281.canBeCollidedWith()) {
                    AxisAlignedBB var30 = var281.boundingBox.expand(0.1F, 0.1F, 0.1F);
                    MovingObjectPosition var31 = var30.calculateIntercept(var14, var22);
                    if(var31 != null) {
                        var7 = var14.distanceTo(var31.hitVec);
                        if(var7 < var20 || var20 == 0.0F) {
                            this.pointedEntity = var281;
                            var20 = var7;
                        }
                    }
                }
            }

            if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerCreative)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
            }

            int var29 = 0;

            while(true) {
                if(var29 >= 2) {
                    GL11.glColorMask(true, true, true, false);
                    break;
                }

                if(this.mc.options.anaglyph) {
                    if(var29 == 0) {
                        GL11.glColorMask(false, true, true, false);
                    } else {
                        GL11.glColorMask(true, false, false, false);
                    }
                }

                EntityPlayerSP var32 = this.mc.thePlayer;
                World var34 = this.mc.theWorld;
                RenderGlobal var23 = this.mc.renderGlobal;
                EffectRenderer var35 = this.mc.effectRenderer;
                GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                this.updateFogColor(var5);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
                this.fogColorMultiplier = 1.0F;
                GL11.glEnable(GL11.GL_CULL_FACE);
                this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                if(this.mc.options.anaglyph) {
                    GL11.glTranslatef((float)(-((var29 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
                }

                EntityPlayerSP var44 = this.mc.thePlayer;
                var16 = 70.0F;
                if(var44.isInsideOfMaterial()) {
                    var16 = 60.0F;
                }

                if(var44.health <= 0) {
                    var13 = (float)var44.deathTime + var5;
                    var16 /= (1.0F - 500.0F / (var13 + 500.0F)) * 2.0F + 1.0F;
                }

                GLU.gluPerspective(var16, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                if(this.mc.options.anaglyph) {
                    GL11.glTranslatef((float)((var29 << 1) - 1) * 0.1F, 0.0F, 0.0F);
                }

                this.hurtCameraEffect(var5);
                if(this.mc.options.viewBobbing) {
                    this.setupViewBobbing(var5);
                }

                var44 = this.mc.thePlayer;
                GL11.glTranslatef(0.0F, 0.0F, -0.1F);
                GL11.glRotatef(var44.prevRotationPitch + (var44.rotationPitch - var44.prevRotationPitch) * var5, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var44.prevRotationYaw + (var44.rotationYaw - var44.prevRotationYaw) * var5, 0.0F, 1.0F, 0.0F);
                var16 = var44.prevPosX + (var44.posX - var44.prevPosX) * var5;
                var13 = var44.prevPosY + (var44.posY - var44.prevPosY) * var5;
                var17 = var44.prevPosZ + (var44.posZ - var44.prevPosZ) * var5;
                GL11.glTranslatef(-var16, -var13, -var17);
                ClippingHelper var38 = ClippingHelperImpl.init();
                this.mc.renderGlobal.clipRenderersByFrustrum(var38);
                this.mc.renderGlobal.updateRenderers(var32);
                this.setupFog();
                GL11.glEnable(GL11.GL_FOG);
                var23.sortAndRender(var32, 0);
                int var39;
                int var41;
                int var45;
                int var47;
                int var49;
                if(var34.isSolid(var32.posX, var32.posY, var32.posZ, 0.1F)) {
                    var39 = (int)var32.posX;
                    int var40 = (int)var32.posY;
                    var41 = (int)var32.posZ;
                    RenderBlocks var42 = new RenderBlocks(Tessellator.instance, var34);

                    for(var45 = var39 - 1; var45 <= var39 + 1; ++var45) {
                        for(int var46 = var40 - 1; var46 <= var40 + 1; ++var46) {
                            for(var47 = var41 - 1; var47 <= var41 + 1; ++var47) {
                                var49 = var34.getBlockId(var45, var46, var47);
                                if(var49 > 0) {
                                    var42.renderBlockAllFaces(Block.blocksList[var49], var45, var46, var47);
                                }
                            }
                        }
                    }
                }

                RenderHelper.enableStandardItemLighting();
                var23.renderEntities(this.orientCamera(var5), var38, var5);
                RenderHelper.disableStandardItemLighting();
                this.setupFog();
                var35.renderParticles(var32, var5);
                var23.oobGroundRenderer();
                this.setupFog();
                var23.renderSky(var5);
                this.setupFog();
                if(this.mc.objectMouseOver != null) {
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    var23.drawBlockBreaking(this.mc.objectMouseOver, 0, var32.inventory.getCurrentItem());
                    var23.drawSelectionBox(this.mc.objectMouseOver, 0);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                this.setupFog();
                var23.oobWaterRenderer();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glColorMask(false, false, false, false);
                var39 = var23.sortAndRender(var32, 1);
                GL11.glColorMask(true, true, true, true);
                if(this.mc.options.anaglyph) {
                    if(var29 == 0) {
                        GL11.glColorMask(false, true, true, false);
                    } else {
                        GL11.glColorMask(true, false, false, false);
                    }
                }

                if(var39 > 0) {
                    var23.renderAllRenderLists();
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_FOG);
                if(this.mc.renderRain) {
                    var11 = var5;
                    var12 = this.mc.thePlayer;
                    World var24 = this.mc.theWorld;
                    var41 = (int)var12.posX;
                    int var43 = (int)var12.posY;
                    var45 = (int)var12.posZ;
                    Tessellator var48 = Tessellator.instance;
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));

                    for(var47 = var41 - 5; var47 <= var41 + 5; ++var47) {
                        for(int var33 = var45 - 5; var33 <= var45 + 5; ++var33) {
                            int var36 = var24.getMapHeight(var47, var33);
                            int var37 = var43 - 5;
                            var49 = var43 + 5;
                            if(var37 < var36) {
                                var37 = var36;
                            }

                            if(var49 < var36) {
                                var49 = var36;
                            }

                            if(var37 != var49) {
                                var8 = ((float)((this.entityRendererInt1 + var47 * 3121 + var33 * 418711) % 32) + var11) / 32.0F;
                                float var50 = (float)var47 + 0.5F - var12.posX;
                                var20 = (float)var33 + 0.5F - var12.posZ;
                                float var51 = MathHelper.sqrt_float(var50 * var50 + var20 * var20) / 5.0F;
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var51 * var51) * 0.7F);
                                var48.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                                var48.addVertexWithUV((float)var47, (float)var37, (float)var33, 0.0F, (float)var37 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)(var47 + 1), (float)var37, (float)(var33 + 1), 2.0F, (float)var37 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)(var47 + 1), (float)var49, (float)(var33 + 1), 2.0F, (float)var49 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)var47, (float)var49, (float)var33, 0.0F, (float)var49 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)var47, (float)var37, (float)(var33 + 1), 0.0F, (float)var37 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)(var47 + 1), (float)var37, (float)var33, 2.0F, (float)var37 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)(var47 + 1), (float)var49, (float)var33, 2.0F, (float)var49 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.addVertexWithUV((float)var47, (float)var49, (float)(var33 + 1), 0.0F, (float)var49 * 2.0F / 8.0F + var8 * 2.0F);
                                var48.draw();
                            }
                        }
                    }

                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_BLEND);
                }

                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glLoadIdentity();
                if(this.mc.options.anaglyph) {
                    GL11.glTranslatef((float)((var29 << 1) - 1) * 0.1F, 0.0F, 0.0F);
                }

                this.hurtCameraEffect(var5);
                if(this.mc.options.viewBobbing) {
                    this.setupViewBobbing(var5);
                }

                this.itemRenderer.renderItemInFirstPerson(var5);
                if(!this.mc.options.anaglyph) {
                    break;
                }

                ++var29;
            }

            this.mc.ingameGUI.renderGameOverlay(var5);
        } else {
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            this.setupOverlayRendering();
        }

        if(this.mc.currentScreen != null) {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            float f = 1.0f;
            final float[] ff = new float[]{1.0f};
            f = this.mc.currentScreen.getEaglerScale();
            int mx, my;
            if (f == 1.0f) {
                mx = var27;
                my = var28;
            } else {
                mx = GuiScreen.applyEaglerScale(f, var27, var2);
                my = GuiScreen.applyEaglerScale(f, var28, var3);
                GL11.glPushMatrix();
                float fff = (1.0f - f) * 0.5f;
                GL11.glTranslatef(fff * var2, fff * var3, 0.0f);
                GL11.glScalef(f, f, f);
            }
            ff[0] = f;
            this.mc.currentScreen.drawScreen(mx, my);
            if (f != 1.0f) {
                GL11.glPopMatrix();
            }
        }
    }

    public final void grabLargeScreenshot() {
    }

    private static ImageData screenshotBuffer(ByteBuffer var0, int var1, int var2) {
        var0.position(0).limit(var1 * var2 << 2);
        ImageData var3 = new ImageData(var1, var2, true);
        int[] var4 = var3.pixels;

        for(int var5 = 0; var5 < var1 * var2; ++var5) {
            int var6 = var0.get(var5 * 3) & 255;
            int var7 = var0.get(var5 * 3 + 1) & 255;
            int var8 = var0.get(var5 * 3 + 2) & 255;
            var4[var5] = var6 << 16 | var7 << 8 | var8;
        }

        return var3;
    }

    public final void setupOverlayRendering() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.mc.scaledResolution.getScaledWidth_double(), this.mc.scaledResolution.getScaledHeight_double(), 
                0.0D, 100.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float var1) {
        World var2 = this.mc.theWorld;
        EntityPlayerSP var3 = this.mc.thePlayer;
        float var4 = 1.0F / (float)(4 - this.mc.options.renderDistance);
        var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
        float var5 = (float)(var2.skyColor >> 16 & 255) / 255.0F;
        float var6 = (float)(var2.skyColor >> 8 & 255) / 255.0F;
        float var7 = (float)(var2.skyColor & 255) / 255.0F;
        this.fogColorRed = (float)(var2.fogColor >> 16 & 255) / 255.0F;
        this.fogColorGreen = (float)(var2.fogColor >> 8 & 255) / 255.0F;
        this.fogColorBlue = (float)(var2.fogColor & 255) / 255.0F;
        this.fogColorRed += (var5 - this.fogColorRed) * var4;
        this.fogColorGreen += (var6 - this.fogColorGreen) * var4;
        this.fogColorBlue += (var7 - this.fogColorBlue) * var4;
        this.fogColorRed *= this.fogColorMultiplier;
        this.fogColorGreen *= this.fogColorMultiplier;
        this.fogColorBlue *= this.fogColorMultiplier;
        Block var8 = Block.blocksList[var2.getBlockId((int)var3.posX, (int)(var3.posY + 0.12F), (int)var3.posZ)];
        if(var8 != null && var8.getBlockMaterial() != Material.air) {
            Material var9 = var8.getBlockMaterial();
            if(var9 == Material.water) {
                this.fogColorRed = 0.02F;
                this.fogColorGreen = 0.02F;
                this.fogColorBlue = 0.2F;
            } else if(var9 == Material.lava) {
                this.fogColorRed = 0.6F;
                this.fogColorGreen = 0.1F;
                this.fogColorBlue = 0.0F;
            }
        }

        float var10 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
        this.fogColorRed *= var10;
        this.fogColorGreen *= var10;
        this.fogColorBlue *= var10;
        if(this.mc.options.anaglyph) {
            var1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            var10 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float var11 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = var1;
            this.fogColorGreen = var10;
            this.fogColorBlue = var11;
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog() {
        World var1 = this.mc.theWorld;
        EntityPlayerSP var2 = this.mc.thePlayer;
        int var10000 = GL11.GL_FOG_COLOR;
        float var3 = 1.0F;
        float var6 = this.fogColorBlue;
        float var5 = this.fogColorGreen;
        float var4 = this.fogColorRed;
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(var4).put(var5).put(var6).put(1.0F);
        this.fogColorBuffer.flip();
        GL11.glFog(var10000, this.fogColorBuffer);
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Block var7 = Block.blocksList[var1.getBlockId((int)var2.posX, (int)(var2.posY + 0.12F), (int)var2.posZ)];
        if(var7 != null && var7.getBlockMaterial() != Material.air) {
            Material var8 = var7.getBlockMaterial();
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            if(var8 == Material.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            } else if(var8 == Material.lava) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
            }
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }
}
