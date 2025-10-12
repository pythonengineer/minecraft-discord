package net.minecraft.client.render;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.ClippingHelperImplementation;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

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
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
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
        float var1 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        float var2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
        var1 = var1 * (1.0F - var2) + var2;
        this.fogColor += (var1 - this.fogColor) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
    }

    private Vec3D getPlayerPosition(float var1) {
        EntityPlayerSP var2 = this.mc.thePlayer;
        double var3 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var1;
        double var5 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var1;
        double var7 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var1;
        return new Vec3D(var3, var5, var7);
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
        if(!this.mc.options.thirdPersonView) {
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
        EntityRenderer var7 = this;
        EntityPlayerSP var9 = this.mc.thePlayer;
        float var10 = var9.prevRotationPitch + (var9.rotationPitch - var9.prevRotationPitch) * var1;
        float var11 = var9.prevRotationYaw + (var9.rotationYaw - var9.prevRotationYaw) * var1;
        Vec3D var12 = this.getPlayerPosition(var1);
        float var13 = MathHelper.cos(-var11 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        float var23 = MathHelper.sin(-var11 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        float var24 = -MathHelper.cos(-var10 * ((float)Math.PI / 180.0F));
        float var25 = MathHelper.sin(-var10 * ((float)Math.PI / 180.0F));
        float var26 = var23 * var24;
        float var28 = var13 * var24;
        double var29 = (double)this.mc.playerController.getBlockReachDistance();
        Vec3D var31 = var12.addVector((double)var26 * var29, (double)var25 * var29, (double)var28 * var29);
        this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var12, var31);
        double var32 = var29;
        var12 = this.getPlayerPosition(var1);
        if(this.mc.objectMouseOver != null) {
            var32 = this.mc.objectMouseOver.hitVec.distanceTo(var12);
        }

        if(this.mc.playerController instanceof PlayerControllerCreative) {
            var29 = 32.0D;
        } else {
            if(var32 > 3.0D) {
                var32 = 3.0D;
            }

            var29 = var32;
        }

        var31 = var12.addVector((double)var26 * var29, (double)var25 * var29, (double)var28 * var29);
        this.pointedEntity = null;
        List var34 = this.mc.theWorld.entityMap.getEntitiesWithinAABB(var9, var9.boundingBox.addCoord((double)var26 * var29, (double)var25 * var29, (double)var28 * var29));
        double var35 = 0.0D;

        int var8;
        double var42;
        MovingObjectPosition var51;
        for(var8 = 0; var8 < var34.size(); ++var8) {
            Entity var47 = (Entity)var34.get(var8);
            if(var47.canBeCollidedWith()) {
                AxisAlignedBB var50 = var47.boundingBox.expand((double)0.1F, (double)0.1F, (double)0.1F);
                var51 = var50.calculateIntercept(var12, var31);
                if(var51 != null) {
                    var42 = var12.distanceTo(var51.hitVec);
                    if(var42 < var35 || var35 == 0.0D) {
                        var7.pointedEntity = var47;
                        var35 = var42;
                    }
                }
            }
        }

        if(var7.pointedEntity != null && !(var7.mc.playerController instanceof PlayerControllerCreative)) {
            var7.mc.objectMouseOver = new MovingObjectPosition(var7.pointedEntity);
        }

        double var16 = this.mc.thePlayer.lastTickPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX) * (double)var1;
        double var17 = this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)var1;
        double var18 = this.mc.thePlayer.lastTickPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ) * (double)var1;

        for(int var2 = 0; var2 < 2; ++var2) {
            if(this.mc.options.anaglyph) {
                if(var2 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            EntityPlayerSP var3 = this.mc.thePlayer;
            World var4 = this.mc.theWorld;
            RenderGlobal var5 = this.mc.renderGlobal;
            EffectRenderer var6 = this.mc.effectRenderer;
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            World var48 = this.mc.theWorld;
            EntityPlayerSP var53 = this.mc.thePlayer;
            var11 = 1.0F / (float)(4 - this.mc.options.renderDistance);
            var11 = 1.0F - (float)Math.pow((double)var11, 0.25D);
            var12 = var4.getSkyColor(var1);
            var13 = (float)var12.xCoord;
            var23 = (float)var12.yCoord;
            var24 = (float)var12.zCoord;
            Vec3D var61 = var4.getFogColor(var1);
            this.fogColorRed = (float)var61.xCoord;
            this.fogColorGreen = (float)var61.yCoord;
            this.fogColorBlue = (float)var61.zCoord;
            this.fogColorRed += (var13 - this.fogColorRed) * var11;
            this.fogColorGreen += (var23 - this.fogColorGreen) * var11;
            this.fogColorBlue += (var24 - this.fogColorBlue) * var11;
            Block var64 = Block.blocksList[var48.getBlockId(MathHelper.floor_double(var53.posX), MathHelper.floor_double(var53.posY + (double)0.12F), MathHelper.floor_double(var53.posZ))];
            if(var64 != null && var64.material != Material.air) {
                Material var27 = var64.material;
                if(var27 == Material.water) {
                    this.fogColorRed = 0.02F;
                    this.fogColorGreen = 0.02F;
                    this.fogColorBlue = 0.2F;
                } else if(var27 == Material.lava) {
                    this.fogColorRed = 0.6F;
                    this.fogColorGreen = 0.1F;
                    this.fogColorBlue = 0.0F;
                }
            }

            float var65 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
            this.fogColorRed *= var65;
            this.fogColorGreen *= var65;
            this.fogColorBlue *= var65;
            if(this.mc.options.anaglyph) {
                var28 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
                float var67 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
                float var30 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
                this.fogColorRed = var28;
                this.fogColorGreen = var67;
                this.fogColorBlue = var30;
            }

            GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            float var46 = var1;
            this.farPlaneDistance = (float)(256 >> this.mc.options.renderDistance);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)(-((var2 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
            }

            EntityPlayerSP var59 = this.mc.thePlayer;
            var23 = 70.0F;
            if(var59.isInsideOfMaterial()) {
                var23 = 60.0F;
            }

            if(var59.health <= 0) {
                var24 = (float)var59.deathTime + var1;
                var23 /= (1.0F - 500.0F / (var24 + 500.0F)) * 2.0F + 1.0F;
            }

            GLU.gluPerspective(var23, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)((var2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.hurtCameraEffect(var1);
            if(this.mc.options.viewBobbing) {
                this.setupViewBobbing(var1);
            }

            EntityRenderer var54 = this;
            var59 = this.mc.thePlayer;
            double var62 = var59.prevPosX + (var59.posX - var59.prevPosX) * (double)var1;
            double var63 = var59.prevPosY + (var59.posY - var59.prevPosY) * (double)var1;
            double var66 = var59.prevPosZ + (var59.posZ - var59.prevPosZ) * (double)var1;
            if(!this.mc.options.thirdPersonView) {
                GL11.glTranslatef(0.0F, 0.0F, -0.1F);
            } else {
                var29 = 4.0D;
                double var68 = (double)(-MathHelper.sin(var59.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(var59.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
                double var33 = (double)(MathHelper.cos(var59.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(var59.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
                var35 = (double)(-MathHelper.sin(var59.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;

                for(int j = 0; j < 8; ++j) {
                    float var49 = (float)(((j & 1) << 1) - 1);
                    float var44 = (float)(((j >> 1 & 1) << 1) - 1);
                    var10 = (float)(((j >> 2 & 1) << 1) - 1);
                    var49 *= 0.1F;
                    var44 *= 0.1F;
                    var10 *= 0.1F;
                    var51 = var54.mc.theWorld.rayTraceBlocks(new Vec3D(var62 + (double)var49, var63 + (double)var44, var66 + (double)var10), new Vec3D(var62 - var68 + (double)var49 + (double)var10, var63 - var35 + (double)var44, var66 - var33 + (double)var10));
                    if(var51 != null) {
                        var42 = var51.hitVec.distanceTo(new Vec3D(var62, var63, var66));
                        if(var42 < var29) {
                            var29 = var42;
                        }
                    }
                }

                GL11.glTranslatef(0.0F, 0.0F, (float)(-var29));
            }

            GL11.glRotatef(var59.prevRotationPitch + (var59.rotationPitch - var59.prevRotationPitch) * var46, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var59.prevRotationYaw + (var59.rotationYaw - var59.prevRotationYaw) * var46 + 180.0F, 0.0F, 1.0F, 0.0F);
            ClippingHelperImplementation.init();
            this.updateFogColor();
            GL11.glEnable(GL11.GL_FOG);
            var5.renderSky(var1);
            this.updateFogColor();
            Frustrum var45 = new Frustrum();
            var45.setPosition(var16, var17, var18);
            this.mc.renderGlobal.clipRenderersByFrustum(var45);
            this.mc.renderGlobal.updateRenderers(var3);
            this.updateFogColor();
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            var5.sortAndRender(this.mc.thePlayer, 0, (double)var1);
            var8 = MathHelper.floor_double(var3.posX);
            int var52 = MathHelper.floor_double(var3.posY);
            int var55 = MathHelper.floor_double(var3.posZ);
            if(var4.isBlockNormalCube(var8, var52, var55)) {
                RenderBlocks var56 = new RenderBlocks(var4);
                Tessellator t = Tessellator.instance;
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

                for(int var58 = var8 - 1; var58 <= var8 + 1; ++var58) {
                    for(int var60 = var52 - 1; var60 <= var52 + 1; ++var60) {
                        for(int var14 = var55 - 1; var14 <= var55 + 1; ++var14) {
                            int var15 = var4.getBlockId(var58, var60, var14);
                            if(var15 > 0) {
                                var56.renderBlockAllFaces(Block.blocksList[var15], var58, var60, var14);
                            }
                        }
                    }
                }

                t.draw();
            }

            RenderHelper.enableStandardItemLighting();
            var5.renderEntities(this.getPlayerPosition(var1), var45, var1);
            var6.renderLitParticles(var1);
            RenderHelper.disableStandardItemLighting();
            this.updateFogColor();
            var6.renderParticles(var3, var1);
            if(this.mc.objectMouseOver != null && var3.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var5.drawBlockBreaking(var3, this.mc.objectMouseOver, 0, var3.inventory.getCurrentItem(), var1);
                var5.drawSelectionBox(var3, this.mc.objectMouseOver, 0, var1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.updateFogColor();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColorMask(false, false, false, false);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            int var57 = var5.sortAndRender(var3, 1, (double)var1);
            GL11.glColorMask(true, true, true, true);
            if(this.mc.options.anaglyph) {
                if(var2 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            if(var57 > 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
                var5.renderAllRenderLists(1, (double)var1);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if(this.mc.objectMouseOver != null && !var3.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var5.drawBlockBreaking(var3, this.mc.objectMouseOver, 0, var3.inventory.getCurrentItem(), var1);
                var5.drawSelectionBox(var3, this.mc.objectMouseOver, 0, var1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glDisable(GL11.GL_FOG);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();
            if(this.mc.options.anaglyph) {
                GL11.glTranslatef((float)((var2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            GL11.glPushMatrix();
            this.hurtCameraEffect(var1);
            if(this.mc.options.viewBobbing) {
                this.setupViewBobbing(var1);
            }

            if(!this.mc.options.thirdPersonView) {
                this.itemRenderer.renderItemInFirstPerson(var1);
            }

            GL11.glPopMatrix();
            if(!this.mc.options.thirdPersonView) {
                this.itemRenderer.renderOverlays(var1);
                this.hurtCameraEffect(var1);
            }

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

    private void updateFogColor() {
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
        Block var7 = Block.blocksList[var1.getBlockId(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY + (double)0.12F), MathHelper.floor_double(var2.posZ))];
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
            GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
            GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }
}
