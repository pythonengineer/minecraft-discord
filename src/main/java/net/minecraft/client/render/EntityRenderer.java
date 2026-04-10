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
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.ClippingHelperImplementation;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.game.entity.Entity;
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
    private int mouseMovement;
    private int mouseMoved;
    private EaglercraftRandom random = new EaglercraftRandom();
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
    private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
    private float fogColorRed;
    private float fogColorGreen;
    private float fogColorBlue;
    private float prevFogColor;
    private float fogColor;

    public EntityRenderer(Minecraft mc) {
        this.mc = mc;
        this.itemRenderer = new ItemRenderer(mc);
    }

    public final void updateRenderer() {
        this.prevFogColor = this.fogColor;
        float f1 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        float f2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
        f1 = f1 * (1.0F - f2) + f2;
        this.fogColor += (f1 - this.fogColor) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        if(this.mc.isRaining) {
            int i4 = MathHelper.floor_double(this.mc.thePlayer.posX);
            int i5 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int i14 = MathHelper.floor_double(this.mc.thePlayer.posZ);

            for(int i6 = 0; i6 < 50; ++i6) {
                int i7 = i4 + this.random.nextInt(9) - 4;
                int i8 = i14 + this.random.nextInt(9) - 4;
                int i9 = this.mc.theWorld.getBlockId(i7, 63, i8);
                if(64 <= i5 + 4 && 64 >= i5 - 4) {
                    float f10 = this.random.nextFloat();
                    float f11 = this.random.nextFloat();
                    if(i9 > 0) {
                        this.mc.effectRenderer.addEffect(new EntityRainFX(this.mc.theWorld, (double)((float)i7 + f10), (double)64.1F - Block.blocksList[i9].minY, (double)((float)i8 + f11)));
                    }
                }
            }
        }

    }

    private Vec3D getMouseOver(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        double d3 = this.mc.thePlayer.prevPosX + (entityPlayerSP2.posX - entityPlayerSP2.prevPosX) * (double)partialTicks;
        double d5 = entityPlayerSP2.prevPosY + (entityPlayerSP2.posY - entityPlayerSP2.prevPosY) * (double)partialTicks;
        double d7 = entityPlayerSP2.prevPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.prevPosZ) * (double)partialTicks;
        return new Vec3D(d3, d5, d7);
    }

    private void hurtCameraEffect(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        float f3 = (float)this.mc.thePlayer.hurtTime - partialTicks;
        if(entityPlayerSP2.health <= 0) {
            partialTicks += (float)entityPlayerSP2.deathTime;
            GL11.glRotatef(40.0F - 8000.0F / (partialTicks + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if(f3 >= 0.0F) {
            f3 = MathHelper.sin((f3 /= (float)entityPlayerSP2.maxHurtTime) * f3 * f3 * f3 * (float)Math.PI);
            partialTicks = entityPlayerSP2.attackedAtYaw;
            if(Float.isNaN(f3)) {
                f3 = 0.0F;
            }

            GL11.glRotatef(-entityPlayerSP2.attackedAtYaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(partialTicks, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if(!this.mc.gameSettings.thirdPersonView) {
            EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
            float f3 = this.mc.thePlayer.distanceWalkedModified - entityPlayerSP2.prevDistanceWalkedModified;
            f3 = entityPlayerSP2.distanceWalkedModified + f3 * partialTicks;
            float f4 = entityPlayerSP2.prevCameraYaw + (entityPlayerSP2.cameraYaw - entityPlayerSP2.prevCameraYaw) * partialTicks;
            partialTicks = entityPlayerSP2.prevCameraPitch + (entityPlayerSP2.cameraPitch - entityPlayerSP2.prevCameraPitch) * partialTicks;
            GL11.glTranslatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5F, -Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0F);
            GL11.glRotatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(f3 * (float)Math.PI + 0.2F) * f4) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(partialTicks, 1.0F, 0.0F, 0.0F);
        }
    }

    public final void updateCameraAndRender(float partialTicks) {
        if(this.displayActive && !Display.isActive()) {
            this.mc.displayInGameMenu();
        }

        this.displayActive = Display.isActive();
        int i5;
        int i6;
        if(this.mc.inGameHasFocus) {
            i5 = PointerInputAbstraction.getDX();
            i6 = PointerInputAbstraction.getDY();
            byte b4 = 1;
            if(this.mc.gameSettings.invertMouse) {
                b4 = -1;
            }

            if(this.mouseMovement != 0) {
                System.out.println("xxo: " + 0 + ", " + this.mouseMovement + ": " + this.mouseMovement + ", xo: " + i5);
            }

            if(this.mouseMovement != 0) {
                this.mouseMovement = 0;
            }

            if(this.mouseMoved != 0) {
                this.mouseMoved = 0;
            }

            float f10001 = (float)i5;
            float f11 = (float)(i6 * b4);
            float f9 = f10001;
            float f13 = this.mc.thePlayer.rotationPitch;
            float f14 = this.mc.thePlayer.rotationYaw;
            this.mc.thePlayer.rotationYaw = (float)((double)this.mc.thePlayer.rotationYaw + (double)f9 * 0.15D);
            this.mc.thePlayer.rotationPitch = (float)((double)this.mc.thePlayer.rotationPitch - (double)f11 * 0.15D);
            if(this.mc.thePlayer.rotationPitch < -90.0F) {
                this.mc.thePlayer.rotationPitch = -90.0F;
            }

            if(this.mc.thePlayer.rotationPitch > 90.0F) {
                this.mc.thePlayer.rotationPitch = 90.0F;
            }

            this.mc.thePlayer.prevRotationPitch += this.mc.thePlayer.rotationPitch - f13;
            this.mc.thePlayer.prevRotationYaw += this.mc.thePlayer.rotationYaw - f14;
        }

        int i10 = this.mc.scaledResolution.getScaledWidth();
        int i12 = this.mc.scaledResolution.getScaledHeight();
        i5 = PointerInputAbstraction.getX() * i10 / this.mc.displayWidth;
        i6 = i12 - PointerInputAbstraction.getY() * i12 / this.mc.displayHeight - 1;
        if(this.mc.theWorld != null) {
            this.renderWorld(partialTicks);
            this.mc.ingameGUI.renderGameOverlay(partialTicks);
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
                mx = i5;
                my = i6;
            } else {
                mx = GuiScreen.applyEaglerScale(f, i5, i10);
                my = GuiScreen.applyEaglerScale(f, i6, i12);
                GL11.glPushMatrix();
                float fff = (1.0f - f) * 0.5f;
                GL11.glTranslatef(fff * i10, fff * i12, 0.0f);
                GL11.glScalef(f, f, f);
            }
            ff[0] = f;
            this.mc.currentScreen.drawScreen(mx, my, partialTicks);
            if (f != 1.0f) {
                GL11.glPopMatrix();
            }
        }
    }

    private void renderWorld(float partialTicks) {
        float f16 = this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * partialTicks;
        float f17 = this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * partialTicks;
        Vec3D vec3D18 = this.getMouseOver(partialTicks);
        float f19 = MathHelper.cos(-f17 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        float f29 = MathHelper.sin(-f17 * ((float)Math.PI / 180.0F) - (float)Math.PI);
        float f30 = -MathHelper.cos(-f16 * ((float)Math.PI / 180.0F));
        float f31 = MathHelper.sin(-f16 * ((float)Math.PI / 180.0F));
        float f32 = f29 * f30;
        float f34 = f19 * f30;
        double d35 = (double)this.mc.playerController.getBlockReachDistance();
        Vec3D vec3D37 = vec3D18.addVector((double)f32 * d35, (double)f31 * d35, (double)f34 * d35);
        this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks_do(vec3D18, vec3D37);
        double d38 = d35;
        vec3D18 = this.getMouseOver(partialTicks);
        if(this.mc.objectMouseOver != null) {
            d38 = this.mc.objectMouseOver.hitVec.distanceTo(vec3D18);
        }

        if(this.mc.playerController instanceof PlayerControllerCreative) {
            d35 = 32.0D;
        } else {
            if(d38 > 3.0D) {
                d38 = 3.0D;
            }

            d35 = d38;
        }

        vec3D37 = vec3D18.addVector((double)f32 * d35, (double)f31 * d35, (double)f34 * d35);
        this.pointedEntity = null;
        List list40 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, this.mc.thePlayer.boundingBox.addCoord((double)f32 * d35, (double)f31 * d35, (double)f34 * d35));
        double d41 = 0.0D;

        int i14;
        double d48;
        MovingObjectPosition movingObjectPosition;
        for(i14 = 0; i14 < list40.size(); ++i14) {
            Entity entity53;
            if((entity53 = (Entity)list40.get(i14)).canBeCollidedWith() && (movingObjectPosition = entity53.boundingBox.expand((double)0.1F, (double)0.1F, (double)0.1F).calculateIntercept(vec3D18, vec3D37)) != null && ((d48 = vec3D18.distanceTo(movingObjectPosition.hitVec)) < d41 || d41 == 0.0D)) {
                this.pointedEntity = entity53;
                d41 = d48;
            }
        }

        if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerCreative)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
        }

        double d6 = this.mc.thePlayer.lastTickPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX) * (double)partialTicks;
        double d8 = this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)partialTicks;
        double d10 = this.mc.thePlayer.lastTickPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ) * (double)partialTicks;

        for(int i12 = 0; i12 < 2; ++i12) {
            if(this.mc.gameSettings.anaglyph) {
                if(i12 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            f17 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
            f17 = 1.0F - (float)Math.pow((double)f17, 0.25D);
            vec3D18 = this.mc.theWorld.getSkyColor(partialTicks);
            f19 = (float)vec3D18.xCoord;
            f29 = (float)vec3D18.yCoord;
            f30 = (float)vec3D18.zCoord;
            Vec3D vec3D66 = this.mc.theWorld.getFogColor(partialTicks);
            this.fogColorRed = (float)vec3D66.xCoord;
            this.fogColorGreen = (float)vec3D66.yCoord;
            this.fogColorBlue = (float)vec3D66.zCoord;
            this.fogColorRed += (f19 - this.fogColorRed) * f17;
            this.fogColorGreen += (f29 - this.fogColorGreen) * f17;
            this.fogColorBlue += (f30 - this.fogColorBlue) * f17;
            Block block71 = Block.blocksList[this.mc.theWorld.getBlockId(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY + (double)0.12F), MathHelper.floor_double(this.mc.thePlayer.posZ))];
            if(block71 != null && block71.blockMaterial != Material.air) {
                Material material = block71.blockMaterial;
                if(material == Material.water) {
                    this.fogColorRed = 0.02F;
                    this.fogColorGreen = 0.02F;
                    this.fogColorBlue = 0.2F;
                } else if(material == Material.lava) {
                    this.fogColorRed = 0.6F;
                    this.fogColorGreen = 0.1F;
                    this.fogColorBlue = 0.0F;
                }
            }

            float f73 = this.prevFogColor + (this.fogColor - this.prevFogColor) * partialTicks;
            this.fogColorRed *= f73;
            this.fogColorGreen *= f73;
            this.fogColorBlue *= f73;
            if(this.mc.gameSettings.anaglyph) {
                f34 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
                float f76 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
                float f36 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
                this.fogColorRed = f34;
                this.fogColorGreen = f76;
                this.fogColorBlue = f36;
            }

            GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            float f52 = partialTicks;
            this.farPlaneDistance = (float)(256 >> this.mc.gameSettings.renderDistance);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            if(this.mc.gameSettings.anaglyph) {
                GL11.glTranslatef((float)(-((i12 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
            }

            f29 = 70.0F;
            if(this.mc.thePlayer.isInsideOfMaterial()) {
                f29 = 60.0F;
            }

            if(this.mc.thePlayer.health <= 0) {
                f30 = (float)this.mc.thePlayer.deathTime + partialTicks;
                f29 /= (1.0F - 500.0F / (f30 + 500.0F)) * 2.0F + 1.0F;
            }

            GLU.gluPerspective(f29, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            if(this.mc.gameSettings.anaglyph) {
                GL11.glTranslatef((float)((i12 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.hurtCameraEffect(partialTicks);
            if(this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(partialTicks);
            }

            double d67 = this.mc.thePlayer.prevPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (double)partialTicks;
            double d68 = this.mc.thePlayer.prevPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.prevPosY) * (double)partialTicks;
            double d74 = this.mc.thePlayer.prevPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)partialTicks;
            if(!this.mc.gameSettings.thirdPersonView) {
                GL11.glTranslatef(0.0F, 0.0F, -0.1F);
            } else {
                d35 = 4.0D;
                double d78 = (double)(-MathHelper.sin(this.mc.thePlayer.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.mc.thePlayer.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
                double d39 = (double)(MathHelper.cos(this.mc.thePlayer.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.mc.thePlayer.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
                d41 = (double)(-MathHelper.sin(this.mc.thePlayer.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;

                for(int j = 0; j < 8; ++j) {
                    float f55 = (float)(((j & 1) << 1) - 1);
                    float f50 = (float)(((j >> 1 & 1) << 1) - 1);
                    f16 = (float)(((j >> 2 & 1) << 1) - 1);
                    f55 *= 0.1F;
                    f50 *= 0.1F;
                    f16 *= 0.1F;
                    if((movingObjectPosition = this.mc.theWorld.rayTraceBlocks_do(new Vec3D(d67 + (double)f55, d68 + (double)f50, d74 + (double)f16), new Vec3D(d67 - d78 + (double)f55 + (double)f16, d68 - d41 + (double)f50, d74 - d39 + (double)f16))) != null && (d48 = movingObjectPosition.hitVec.distanceTo(new Vec3D(d67, d68, d74))) < d35) {
                        d35 = d48;
                    }
                }

                GL11.glTranslatef(0.0F, 0.0F, (float)(-d35));
            }

            GL11.glRotatef(this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * f52, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * f52 + 180.0F, 0.0F, 1.0F, 0.0F);
            ClippingHelperImplementation.getInstance();
            this.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            this.mc.renderGlobal.renderSky(partialTicks);
            this.setupFog();
            Frustrum frustrum51;
            (frustrum51 = new Frustrum()).setPosition(d6, d8, d10);
            this.mc.renderGlobal.clipRenderersByFrustrum(frustrum51);
            this.mc.renderGlobal.updateRenderers(this.mc.thePlayer);
            this.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            this.mc.renderGlobal.sortAndRender(this.mc.thePlayer, 0, (double)partialTicks);
            i14 = MathHelper.floor_double(this.mc.thePlayer.posX);
            int i58 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int i60 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            if(this.mc.theWorld.isBlockNormalCube(i14, i58, i60)) {
                RenderBlocks renderBlocks = new RenderBlocks(this.mc.theWorld);
                Tessellator t = Tessellator.instance;
                t.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

                for(int i63 = i14 - 1; i63 <= i14 + 1; ++i63) {
                    for(int i65 = i58 - 1; i65 <= i58 + 1; ++i65) {
                        for(int i20 = i60 - 1; i20 <= i60 + 1; ++i20) {
                            int i21 = this.mc.theWorld.getBlockId(i63, i65, i20);
                            if(i21 > 0) {
                                renderBlocks.renderBlockAllFaces(Block.blocksList[i21], i63, i65, i20);
                            }
                        }
                    }
                }

                t.draw();
            }

            RenderHelper.enableStandardItemLighting();
            this.mc.renderGlobal.renderEntities(this.getMouseOver(partialTicks), frustrum51, partialTicks);
            this.mc.effectRenderer.renderLitParticles(partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog();
            this.mc.effectRenderer.renderParticles(this.mc.thePlayer, partialTicks);
            if(this.mc.objectMouseOver != null && this.mc.thePlayer.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                this.mc.renderGlobal.drawBlockBreaking(this.mc.thePlayer, this.mc.objectMouseOver, 0, this.mc.thePlayer.inventory.getCurrentItem(), partialTicks);
                this.mc.renderGlobal.drawSelectionBox(this.mc.thePlayer, this.mc.objectMouseOver, 0, partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.setupFog();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColorMask(false, false, false, false);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            int i62 = this.mc.renderGlobal.sortAndRender(this.mc.thePlayer, 1, (double)partialTicks);
            GL11.glColorMask(true, true, true, true);
            if(this.mc.gameSettings.anaglyph) {
                if(i12 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            if(i62 > 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
                this.mc.renderGlobal.renderAllRenderLists(1, (double)partialTicks);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if(this.mc.objectMouseOver != null && !this.mc.thePlayer.isInsideOfMaterial()) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                this.mc.renderGlobal.drawBlockBreaking(this.mc.thePlayer, this.mc.objectMouseOver, 0, this.mc.thePlayer.inventory.getCurrentItem(), partialTicks);
                this.mc.renderGlobal.drawSelectionBox(this.mc.thePlayer, this.mc.objectMouseOver, 0, partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glDisable(GL11.GL_FOG);
            if(this.mc.isRaining) {
                i62 = MathHelper.floor_double(this.mc.thePlayer.posX);
                int i63 = MathHelper.floor_double(this.mc.thePlayer.posY);
                int i65 = MathHelper.floor_double(this.mc.thePlayer.posZ);
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
                int i70 = i62 - 5;

                while(true) {
                    if(i70 > i62 + 5) {
                        GL11.glEnable(GL11.GL_CULL_FACE);
                        GL11.glDisable(GL11.GL_BLEND);
                        break;
                    }

                    for(int i72 = i65 - 5; i72 <= i65 + 5; ++i72) {
                        int i75 = i63 - 5;
                        int i77 = i63 + 5;
                        if(i75 < 64) {
                            i75 = 64;
                        }

                        if(i77 < 64) {
                            i77 = 64;
                        }

                        if(i75 != i77) {
                            float f79 = ((float)((this.rendererUpdateCount + i70 * 3121 + i72 * 418711) % 32) + partialTicks) / 32.0F;
                            d38 = (double)((float)i70 + 0.5F) - this.mc.thePlayer.posX;
                            double d80 = (double)((float)i72 + 0.5F) - this.mc.thePlayer.posZ;
                            float f42 = MathHelper.sqrt_double(d38 * d38 + d80 * d80) / 5.0F;
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f42 * f42) * 0.7F);
                            tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                            tessellator.addVertexWithUV((double)i70, (double)i75, (double)i72, 0.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)(i70 + 1), (double)i75, (double)(i72 + 1), 2.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)(i70 + 1), (double)i77, (double)(i72 + 1), 2.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)i70, (double)i77, (double)i72, 0.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)i70, (double)i75, (double)(i72 + 1), 0.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)(i70 + 1), (double)i75, (double)i72, 2.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)(i70 + 1), (double)i77, (double)i72, 2.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.addVertexWithUV((double)i70, (double)i77, (double)(i72 + 1), 0.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
                            tessellator.draw();
                        }
                    }

                    ++i70;
                }
            }

            this.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            this.mc.renderGlobal.renderClouds(partialTicks);
            GL11.glDisable(GL11.GL_FOG);
            this.setupFog();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();
            if(this.mc.gameSettings.anaglyph) {
                GL11.glTranslatef((float)((i12 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            GL11.glPushMatrix();
            this.hurtCameraEffect(partialTicks);
            if(this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(partialTicks);
            }

            if(!this.mc.gameSettings.thirdPersonView) {
                this.itemRenderer.renderItemInFirstPerson(partialTicks);
            }

            GL11.glPopMatrix();
            if(!this.mc.gameSettings.thirdPersonView) {
                this.itemRenderer.renderOverlays(partialTicks);
                this.hurtCameraEffect(partialTicks);
            }

            if(this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(partialTicks);
            }

            if(!this.mc.gameSettings.anaglyph) {
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

    private void setupFog() {
        int var10000 = GL11.GL_FOG_COLOR;
        float f3 = 1.0F;
        float f6 = this.fogColorBlue;
        float f5 = this.fogColorGreen;
        float f4 = this.fogColorRed;
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(f4).put(f5).put(f6).put(1.0F);
        this.fogColorBuffer.flip();
        GL11.glFog(var10000, this.fogColorBuffer);
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Block block7 = Block.blocksList[this.mc.theWorld.getBlockId(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY + (double)0.12F), MathHelper.floor_double(this.mc.thePlayer.posZ))];
        if(block7 != null && block7.blockMaterial.getIsLiquid()) {
            Material material8 = block7.blockMaterial;
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            if(material8 == Material.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            } else if(material8 == Material.lava) {
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
