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
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.camera.IsomCamera;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class EntityRenderer {
    private Minecraft mc;
    private boolean displayActive = false;
    private float farPlaneDistance = 0.0F;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private int entityRendererInt1;
    private int entityRendererInt2;
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
        float var1 = this.mc.theWorld.getBrightness((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ);
        float var2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
        var1 = var1 * (1.0F - var2) + var2;
        this.fogColor += (var1 - this.fogColor) * 0.1F;
        ++this.rendererUpdateCount;
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
        int var5;
        int var6;
        if(this.mc.inGameHasFocus) {
            int var2 = PointerInputAbstraction.getDX();
            int var3 = PointerInputAbstraction.getDY();
            byte var4 = 1;
            if(this.mc.options.invertMouse) {
                var4 = -1;
            }

            if(this.entityRendererInt1 != 0) {
                System.out.println("xxo: " + 0 + ", " + this.entityRendererInt1 + ": " + this.entityRendererInt1 + ", xo: " + var2);
            }

            if(this.entityRendererInt1 != 0) {
                this.entityRendererInt1 = 0;
            }

            if(this.entityRendererInt2 != 0) {
                this.entityRendererInt2 = 0;
            }

            float var10001 = (float)var2;
            float var11 = (float)(var3 * var4);
            float var9 = var10001;
            EntityPlayerSP var25 = this.mc.thePlayer;
            float var13 = var25.rotationPitch;
            float var14 = var25.rotationYaw;
            var25.rotationYaw = (float)((double)var25.rotationYaw + (double)var9 * 0.15D);
            var25.rotationPitch = (float)((double)var25.rotationPitch - (double)var11 * 0.15D);
            if(var25.rotationPitch < -90.0F) {
                var25.rotationPitch = -90.0F;
            }

            if(var25.rotationPitch > 90.0F) {
                var25.rotationPitch = 90.0F;
            }

            var25.prevRotationPitch += var25.rotationPitch - var13;
            var25.prevRotationYaw += var25.rotationYaw - var14;
        }

        int var10 = this.mc.scaledResolution.getScaledWidth();
        int var12 = this.mc.scaledResolution.getScaledHeight();
        var5 = PointerInputAbstraction.getX() * var10 / this.mc.displayWidth;
        var6 = var12 - PointerInputAbstraction.getY() * var12 / this.mc.displayHeight - 1;
        if(this.mc.theWorld != null) {
            this.renderWorld(var1);
            this.mc.ingameGUI.renderGameOverlay(var1);
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
                mx = var5;
                my = var6;
            } else {
                mx = GuiScreen.applyEaglerScale(f, var5, var10);
                my = GuiScreen.applyEaglerScale(f, var6, var12);
                GL11.glPushMatrix();
                float fff = (1.0f - f) * 0.5f;
                GL11.glTranslatef(fff * var10, fff * var12, 0.0f);
                GL11.glScalef(f, f, f);
            }
            ff[0] = f;
            this.mc.currentScreen.drawScreen(mx, my, var1);
            if (f != 1.0f) {
                GL11.glPopMatrix();
            }
        }
    }

    private void renderWorld(float var1) {
        EntityPlayerSP var13 = this.mc.thePlayer;
        float var7 = var13.prevRotationPitch + (var13.rotationPitch - var13.prevRotationPitch) * var1;
        float var14 = var13.prevRotationYaw + (var13.rotationYaw - var13.prevRotationYaw) * var1;
        Vec3D var15 = this.orientCamera(var1);
        float var16 = MathHelper.cos(-var14 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        float var17 = MathHelper.sin(-var14 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        var14 = MathHelper.cos(-var7 * ((float)Math.PI / 180.0F));
        float var18 = MathHelper.sin(-var7 * ((float)Math.PI / 180.0F));
        float var8 = var17 * var14;
        float var10 = var16 * var14;
        float var19 = this.mc.playerController.getBlockReachDistance();
        Vec3D var27 = var15.addVector(var8 * var19, var18 * var19, var10 * var19);
        this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var15, var27);
        float var9 = var19;
        var15 = this.orientCamera(var1);
        if(this.mc.objectMouseOver != null) {
            var9 = this.mc.objectMouseOver.hitVec.distanceTo(var15);
        }

        if(this.mc.playerController instanceof PlayerControllerCreative) {
            var19 = 32.0F;
        } else {
            if(var9 > 3.0F) {
                var9 = 3.0F;
            }

            var19 = var9;
        }

        var27 = var15.addVector(var8 * var19, var18 * var19, var10 * var19);
        this.pointedEntity = null;
        List var20 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(var13, var13.boundingBox.addCoord(var8 * var19, var18 * var19, var10 * var19));
        float var21 = 0.0F;

        for(int var22 = 0; var22 < var20.size(); ++var22) {
            Entity var29 = (Entity)var20.get(var22);
            if(var29.canBeCollidedWith()) {
                AxisAlignedBB var32 = var29.boundingBox.expand(0.1F, 0.1F, 0.1F);
                MovingObjectPosition var33 = var32.calculateIntercept(var15, var27);
                if(var33 != null) {
                    var8 = var15.distanceTo(var33.hitVec);
                    if(var8 < var21 || var21 == 0.0F) {
                        this.pointedEntity = var29;
                        var21 = var8;
                    }
                }
            }
        }

        if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerCreative)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
        }

        for(int var28 = 0; var28 < 2; ++var28) {
            if(this.mc.options.anaglyph) {
                if(var28 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            EntityPlayerSP var30 = this.mc.thePlayer;
            World var34 = this.mc.theWorld;
            RenderGlobal var23 = this.mc.renderGlobal;
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(var1);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)(-((var28 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
            }

            var17 = 70.0F;
            if(this.mc.thePlayer.isInsideOfMaterial()) {
                var17 = 60.0F;
            }

            if(this.mc.thePlayer.health <= 0) {
                var14 = (float)this.mc.thePlayer.deathTime + var1;
                var17 /= (1.0F - 500.0F / (var14 + 500.0F)) * 2.0F + 1.0F;
            }

            GLU.gluPerspective(var17, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)((var28 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.hurtCameraEffect(var1);
            if(this.mc.options.viewBobbing) {
                this.setupViewBobbing(var1);
            }

            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
            GL11.glRotatef(this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
            var17 = this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * var1;
            var14 = this.mc.thePlayer.prevPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.prevPosY) * var1;
            var18 = this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * var1;
            GL11.glTranslatef(-var17, -var14, -var18);
            this.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            this.mc.renderGlobal.renderSky(var1);
            this.setupFog();
            Frustrum var40 = new Frustrum(var30, this.farPlaneDistance, var1);
            this.mc.renderGlobal.clipRenderersByFrustrum(var40);
            this.mc.renderGlobal.updateRenderers(var30);
            this.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            var23.sortAndRender(var30, 0);
            int var12;
            int var43;
            int var47;
            int var48;
            int var51;
            if(var34.isSolid(var30.posX, var30.posY, var30.posZ, 0.1F)) {
                var12 = (int)var30.posX;
                int var42 = (int)var30.posY;
                var43 = (int)var30.posZ;
                RenderBlocks var44 = new RenderBlocks(var34);
                Tessellator t = Tessellator.instance;
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

                for(var47 = var12 - 1; var47 <= var12 + 1; ++var47) {
                    for(int var49 = var42 - 1; var49 <= var42 + 1; ++var49) {
                        for(var48 = var43 - 1; var48 <= var43 + 1; ++var48) {
                            var51 = var34.getBlockId(var47, var49, var48);
                            if(var51 > 0) {
                                var44.renderBlockAllFaces(Block.blocksList[var51], var47, var49, var48);
                            }
                        }
                    }
                }

                t.draw();
            }

            RenderHelper.enableStandardItemLighting();
            var23.renderEntities(this.orientCamera(var1), var40, var1);
            this.mc.effectRenderer.renderLitParticles(var1);
            RenderHelper.disableStandardItemLighting();
            this.setupFog();
            this.mc.effectRenderer.renderParticles(var30, var1);
            var23.oobGroundRenderer();
            if(this.mc.objectMouseOver != null && var30.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var23.drawBlockBreaking(this.mc.objectMouseOver, 0, var30.inventory.getCurrentItem());
                var23.drawSelectionBox(this.mc.objectMouseOver, 0);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.setupFog();
            var23.oobWaterRenderer();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColorMask(false, false, false, false);
            var12 = var23.sortAndRender(var30, 1);
            GL11.glColorMask(true, true, true, true);
            if(this.mc.options.anaglyph) {
                if(var28 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            if(var12 > 0) {
                var23.renderAllRenderLists();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if(this.mc.objectMouseOver != null && !var30.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var23.drawBlockBreaking(this.mc.objectMouseOver, 0, var30.inventory.getCurrentItem());
                var23.drawSelectionBox(this.mc.objectMouseOver, 0);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glDisable(GL11.GL_FOG);
            if(this.mc.renderRain) {
                float var41 = var1;
                World var31 = this.mc.theWorld;
                var43 = (int)this.mc.thePlayer.posX;
                int var45 = (int)this.mc.thePlayer.posY;
                var47 = (int)this.mc.thePlayer.posZ;
                Tessellator var50 = Tessellator.instance;
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
                var48 = var43 - 5;

                while(true) {
                    if(var48 > var43 + 5) {
                        GL11.glEnable(GL11.GL_CULL_FACE);
                        GL11.glDisable(GL11.GL_BLEND);
                        break;
                    }

                    for(int var35 = var47 - 5; var35 <= var47 + 5; ++var35) {
                        int var39 = var31.getMapHeight(var48, var35);
                        int var38 = var45 - 5;
                        var51 = var45 + 5;
                        if(var38 < var39) {
                            var38 = var39;
                        }

                        if(var51 < var39) {
                            var51 = var39;
                        }

                        if(var38 != var51) {
                            var9 = ((float)((this.rendererUpdateCount + var48 * 3121 + var35 * 418711) % 32) + var41) / 32.0F;
                            float var52 = (float)var48 + 0.5F - var13.posX;
                            var21 = (float)var35 + 0.5F - var13.posZ;
                            float var53 = MathHelper.sqrt_float(var52 * var52 + var21 * var21) / 5.0F;
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var53 * var53) * 0.7F);
                            var50.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                            var50.addVertexWithUV((float)var48, (float)var38, (float)var35, 0.0F, (float)var38 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)(var48 + 1), (float)var38, (float)(var35 + 1), 2.0F, (float)var38 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)(var48 + 1), (float)var51, (float)(var35 + 1), 2.0F, (float)var51 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)var48, (float)var51, (float)var35, 0.0F, (float)var51 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)var48, (float)var38, (float)(var35 + 1), 0.0F, (float)var38 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)(var48 + 1), (float)var38, (float)var35, 2.0F, (float)var38 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)(var48 + 1), (float)var51, (float)var35, 2.0F, (float)var51 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.addVertexWithUV((float)var48, (float)var51, (float)(var35 + 1), 0.0F, (float)var51 * 2.0F / 8.0F + var9 * 2.0F);
                            var50.draw();
                        }
                    }

                    ++var48;
                }
            }

            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)((var28 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            GL11.glPushMatrix();
            this.hurtCameraEffect(var1);
            if(this.mc.options.viewBobbing) {
                this.setupViewBobbing(var1);
            }

            this.itemRenderer.renderItemInFirstPerson(var1);
            GL11.glPopMatrix();
            this.itemRenderer.renderOverlays(var1);
            this.hurtCameraEffect(var1);
            if(this.mc.options.viewBobbing) {
                this.setupViewBobbing(var1);
            }

            if(!this.mc.options.anaglyph) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
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
        Vec3D var5 = var2.getSkyColor(var1);
        float var6 = var5.xCoord;
        float var7 = var5.yCoord;
        float var13 = var5.zCoord;
        Vec3D var8 = var2.getFogColor(var1);
        this.fogColorRed = var8.xCoord;
        this.fogColorGreen = var8.yCoord;
        this.fogColorBlue = var8.zCoord;
        this.fogColorRed += (var6 - this.fogColorRed) * var4;
        this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
        this.fogColorBlue += (var13 - this.fogColorBlue) * var4;
        Block var9 = Block.blocksList[var2.getBlockId((int)var3.posX, (int)(var3.posY + 0.12F), (int)var3.posZ)];
        if(var9 != null && var9.material != Material.air) {
            Material var10 = var9.material;
            if(var10 == Material.water) {
                this.fogColorRed = 0.02F;
                this.fogColorGreen = 0.02F;
                this.fogColorBlue = 0.2F;
            } else if(var10 == Material.lava) {
                this.fogColorRed = 0.6F;
                this.fogColorGreen = 0.1F;
                this.fogColorBlue = 0.0F;
            }
        }

        float var11 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
        this.fogColorRed *= var11;
        this.fogColorGreen *= var11;
        this.fogColorBlue *= var11;
        if(this.mc.options.anaglyph) {
            var1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            var11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float var12 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = var1;
            this.fogColorGreen = var11;
            this.fogColorBlue = var12;
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
        if(var7 != null && var7.material.getIsLiquid()) {
            Material var8 = var7.material;
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            if(var8 == Material.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            } else if(var8 == Material.lava) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
            }
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance / 4.0F);
            GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }
}
