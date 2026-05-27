package net.minecraft.client.render;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.GLAllocation;
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
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class EntityRenderer {
    private Minecraft mc;
    private boolean displayActive = false;
    private float farPlaneDistance = 0.0F;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private int mouseDX;
    private int mouseDY;
    private EaglercraftRandom random = new EaglercraftRandom();
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
    private FloatBuffer fogColorBuffer = GLAllocation.createFloatBuffer(16);
    private float fogColorRed;
    private float fogColorGreen;
    private float fogColorBlue;
    private float prevFogColor;
    private float fogColor;

    public EntityRenderer(Minecraft mc) {
        this.mc = mc;
        this.itemRenderer = new ItemRenderer(mc);
    }

    public void updateRenderer() {
        this.prevFogColor = this.fogColor;
        float f1 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
        float f2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
        f1 = f1 * (1.0F - f2) + f2;
        this.fogColor += (f1 - this.fogColor) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        if(this.mc.isRaining) {
            this.addRainParticles();
        }

    }

    private Vec3D getPlayerPosition(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        double d3 = entityPlayerSP2.prevPosX + (entityPlayerSP2.posX - entityPlayerSP2.prevPosX) * (double)partialTicks;
        double d5 = entityPlayerSP2.prevPosY + (entityPlayerSP2.posY - entityPlayerSP2.prevPosY) * (double)partialTicks;
        double d7 = entityPlayerSP2.prevPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.prevPosZ) * (double)partialTicks;
        return Vec3D.createVector(d3, d5, d7);
    }

    private void getMouseOver(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        float f3 = entityPlayerSP2.prevRotationPitch + (entityPlayerSP2.rotationPitch - entityPlayerSP2.prevRotationPitch) * partialTicks;
        float f4 = entityPlayerSP2.prevRotationYaw + (entityPlayerSP2.rotationYaw - entityPlayerSP2.prevRotationYaw) * partialTicks;
        Vec3D vec3D5 = this.getPlayerPosition(partialTicks);
        float f6 = MathHelper.cos(-f4 * 0.017453292F - (float)Math.PI);
        float f7 = MathHelper.sin(-f4 * 0.017453292F - (float)Math.PI);
        float f8 = -MathHelper.cos(-f3 * 0.017453292F);
        float f9 = MathHelper.sin(-f3 * 0.017453292F);
        float f10 = f7 * f8;
        float f12 = f6 * f8;
        double d13 = (double)this.mc.playerController.getBlockReachDistance();
        Vec3D vec3D15 = vec3D5.addVector((double)f10 * d13, (double)f9 * d13, (double)f12 * d13);
        this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(vec3D5, vec3D15);
        double d16 = d13;
        vec3D5 = this.getPlayerPosition(partialTicks);
        if(this.mc.objectMouseOver != null) {
            d16 = this.mc.objectMouseOver.hitVec.distanceTo(vec3D5);
        }

        if(this.mc.playerController instanceof PlayerControllerCreative) {
            d13 = 32.0D;
            d16 = 32.0D;
        } else {
            if(d16 > 3.0D) {
                d16 = 3.0D;
            }

            d13 = d16;
        }

        vec3D15 = vec3D5.addVector((double)f10 * d13, (double)f9 * d13, (double)f12 * d13);
        this.pointedEntity = null;
        List list18 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(entityPlayerSP2, entityPlayerSP2.boundingBox.addCoord((double)f10 * d13, (double)f9 * d13, (double)f12 * d13));
        double d19 = 0.0D;

        for(int i21 = 0; i21 < list18.size(); ++i21) {
            Entity entity22 = (Entity)list18.get(i21);
            if(entity22.canBeCollidedWith()) {
                float f23 = 0.1F;
                AxisAlignedBB axisAlignedBB24 = entity22.boundingBox.expand((double)f23, (double)f23, (double)f23);
                MovingObjectPosition movingObjectPosition25 = axisAlignedBB24.calculateIntercept(vec3D5, vec3D15);
                if(movingObjectPosition25 != null) {
                    double d26 = vec3D5.distanceTo(movingObjectPosition25.hitVec);
                    if(d26 < d19 || d19 == 0.0D) {
                        this.pointedEntity = entity22;
                        d19 = d26;
                    }
                }
            }
        }

        if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerCreative)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
        }

    }

    private float getFOVModifier(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        float f3 = 70.0F;
        if(entityPlayerSP2.isInsideOfMaterial(Material.water)) {
            f3 = 60.0F;
        }

        if(entityPlayerSP2.health <= 0) {
            float f4 = (float)entityPlayerSP2.deathTime + partialTicks;
            f3 /= (1.0F - 500.0F / (f4 + 500.0F)) * 2.0F + 1.0F;
        }

        return f3;
    }

    private void hurtCameraEffect(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        float f3 = (float)entityPlayerSP2.hurtTime - partialTicks;
        float f4;
        if(entityPlayerSP2.health <= 0) {
            f4 = (float)entityPlayerSP2.deathTime + partialTicks;
            GL11.glRotatef(40.0F - 8000.0F / (f4 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if(f3 >= 0.0F) {
            f3 /= (float)entityPlayerSP2.maxHurtTime;
            f3 = MathHelper.sin(f3 * f3 * f3 * f3 * (float)Math.PI);
            if(Float.isNaN(f3)) {
                f3 = 0.0F;
            }

            f4 = entityPlayerSP2.attackedAtYaw;
            GL11.glRotatef(-f4, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if(!this.mc.gameSettings.thirdPersonView) {
            EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
            float f3 = entityPlayerSP2.distanceWalkedModified - entityPlayerSP2.prevDistanceWalkedModified;
            float f4 = entityPlayerSP2.distanceWalkedModified + f3 * partialTicks;
            float f5 = entityPlayerSP2.prevCameraYaw + (entityPlayerSP2.cameraYaw - entityPlayerSP2.prevCameraYaw) * partialTicks;
            float f6 = entityPlayerSP2.prevCameraPitch + (entityPlayerSP2.cameraPitch - entityPlayerSP2.prevCameraPitch) * partialTicks;
            GL11.glTranslatef(MathHelper.sin(f4 * (float)Math.PI) * f5 * 0.5F, -Math.abs(MathHelper.cos(f4 * (float)Math.PI) * f5), 0.0F);
            GL11.glRotatef(MathHelper.sin(f4 * (float)Math.PI) * f5 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(f4 * (float)Math.PI + 0.2F) * f5) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f6, 1.0F, 0.0F, 0.0F);
        }
    }

    private void orientCamera(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        double d3 = entityPlayerSP2.prevPosX + (entityPlayerSP2.posX - entityPlayerSP2.prevPosX) * (double)partialTicks;
        double d5 = entityPlayerSP2.prevPosY + (entityPlayerSP2.posY - entityPlayerSP2.prevPosY) * (double)partialTicks;
        double d7 = entityPlayerSP2.prevPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.prevPosZ) * (double)partialTicks;
        if(this.mc.gameSettings.thirdPersonView) {
            double d9 = 4.0D;
            double d11 = (double)(-MathHelper.sin(entityPlayerSP2.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP2.rotationPitch / 180.0F * (float)Math.PI)) * d9;
            double d13 = (double)(MathHelper.cos(entityPlayerSP2.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP2.rotationPitch / 180.0F * (float)Math.PI)) * d9;
            double d15 = (double)(-MathHelper.sin(entityPlayerSP2.rotationPitch / 180.0F * (float)Math.PI)) * d9;

            for(int i17 = 0; i17 < 8; ++i17) {
                float f18 = (float)((i17 & 1) * 2 - 1);
                float f19 = (float)((i17 >> 1 & 1) * 2 - 1);
                float f20 = (float)((i17 >> 2 & 1) * 2 - 1);
                f18 *= 0.1F;
                f19 *= 0.1F;
                f20 *= 0.1F;
                MovingObjectPosition movingObjectPosition21 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d3 + (double)f18, d5 + (double)f19, d7 + (double)f20), Vec3D.createVector(d3 - d11 + (double)f18 + (double)f20, d5 - d15 + (double)f19, d7 - d13 + (double)f20));
                if(movingObjectPosition21 != null) {
                    double d22 = movingObjectPosition21.hitVec.distanceTo(Vec3D.createVector(d3, d5, d7));
                    if(d22 < d9) {
                        d9 = d22;
                    }
                }
            }

            GL11.glTranslatef(0.0F, 0.0F, (float)(-d9));
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        GL11.glRotatef(entityPlayerSP2.prevRotationPitch + (entityPlayerSP2.rotationPitch - entityPlayerSP2.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(entityPlayerSP2.prevRotationYaw + (entityPlayerSP2.rotationYaw - entityPlayerSP2.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
    }

    private void setupCameraTransform(float partialTicks, int anaglyphPass) {
        this.farPlaneDistance = (float)(256 >> this.mc.gameSettings.renderDistance);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f3 = 0.07F;
        if(this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(-(anaglyphPass * 2 - 1)) * f3, 0.0F, 0.0F);
        }

        GLU.gluPerspective(this.getFOVModifier(partialTicks), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        if(this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(anaglyphPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(partialTicks);
        if(this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        this.orientCamera(partialTicks);
    }

    private void renderHand(float partialTicks, int anaglyphPass) {
        GL11.glLoadIdentity();
        if(this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(anaglyphPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
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

    }

    public void updateCameraAndRender(float partialTicks) {
        if(this.displayActive && !Display.isActive()) {
            this.mc.displayInGameMenu();
        }

        this.displayActive = Display.isActive();
        if(this.mc.inGameHasFocus) {
            int i5 = PointerInputAbstraction.getDX();
            int i6 = PointerInputAbstraction.getDY();
            byte b4 = 1;
            if(this.mc.gameSettings.invertMouse) {
                b4 = -1;
            }

            if(this.mouseDX != 0) {
                System.out.println("xxo: " + 0 + ", " + this.mouseDX + ": " + this.mouseDX + ", xo: " + i5);
            }

            if(this.mouseDX != 0) {
                this.mouseDX = 0;
            }

            if(this.mouseDY != 0) {
                this.mouseDY = 0;
            }

            this.mc.thePlayer.setAngles((float)i5, (float)(i6 * b4));
        }

        if(!this.mc.skipRenderWorld) {
            int i10 = this.mc.scaledResolution.getScaledWidth();
            int i12 = this.mc.scaledResolution.getScaledHeight();
            int i5 = PointerInputAbstraction.getX() * i10 / this.mc.displayWidth;
            int i6 = i12 - PointerInputAbstraction.getY() * i12 / this.mc.displayHeight - 1;
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
    }

    private void renderWorld(float partialTicks) {
        this.getMouseOver(partialTicks);
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        RenderGlobal renderGlobal3 = this.mc.renderGlobal;
        EffectRenderer effectRenderer4 = this.mc.effectRenderer;
        double d5 = entityPlayerSP2.lastTickPosX + (entityPlayerSP2.posX - entityPlayerSP2.lastTickPosX) * (double)partialTicks;
        double d7 = entityPlayerSP2.lastTickPosY + (entityPlayerSP2.posY - entityPlayerSP2.lastTickPosY) * (double)partialTicks;
        double d9 = entityPlayerSP2.lastTickPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.lastTickPosZ) * (double)partialTicks;

        for(int i11 = 0; i11 < 2; ++i11) {
            if(this.mc.gameSettings.anaglyph) {
                if(i11 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(partialTicks);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.setupCameraTransform(partialTicks, i11);
            ClippingHelperImplementation.getInstance();
            if(this.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1);
                renderGlobal3.renderSky(partialTicks);
            }

            GL11.glEnable(GL11.GL_FOG);
            this.setupFog(1);
            Frustrum frustrum12 = new Frustrum();
            frustrum12.setPosition(d5, d7, d9);
            this.mc.renderGlobal.clipRenderersByFrustrum(frustrum12, partialTicks);
            this.mc.renderGlobal.updateRenderers(entityPlayerSP2, false);
            this.setupFog(0);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            renderGlobal3.sortAndRender(entityPlayerSP2, 0, (double)partialTicks);
            RenderHelper.enableStandardItemLighting();
            renderGlobal3.renderEntities(this.getPlayerPosition(partialTicks), frustrum12, partialTicks);
            effectRenderer4.renderLitParticles(this.mc.thePlayer, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0);
            effectRenderer4.renderParticles(entityPlayerSP2, partialTicks);
            if(this.mc.objectMouseOver != null && entityPlayerSP2.isInsideOfMaterial(Material.water)) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                renderGlobal3.drawBlockBreaking(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), partialTicks);
                renderGlobal3.drawSelectionBox(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.setupFog(0);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            if(this.mc.gameSettings.fancyGraphics) {
                GL11.glColorMask(false, false, false, false);
                int i13 = renderGlobal3.sortAndRender(entityPlayerSP2, 1, (double)partialTicks);
                GL11.glColorMask(true, true, true, true);
                if(this.mc.gameSettings.anaglyph) {
                    if(i11 == 0) {
                        GL11.glColorMask(false, true, true, false);
                    } else {
                        GL11.glColorMask(true, false, false, false);
                    }
                }

                if(i13 > 0) {
                    renderGlobal3.renderAllRenderLists(1, (double)partialTicks);
                }
            } else {
                renderGlobal3.sortAndRender(entityPlayerSP2, 1, (double)partialTicks);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if(this.mc.objectMouseOver != null && !entityPlayerSP2.isInsideOfMaterial(Material.water)) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                renderGlobal3.drawBlockBreaking(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), partialTicks);
                renderGlobal3.drawSelectionBox(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glDisable(GL11.GL_FOG);
            if(this.mc.isRaining) {
                this.renderRain(partialTicks);
            }

            if(this.pointedEntity != null) {
                ;
            }

            this.setupFog(0);
            GL11.glEnable(GL11.GL_FOG);
            renderGlobal3.renderClouds(partialTicks);
            GL11.glDisable(GL11.GL_FOG);
            this.setupFog(1);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            this.renderHand(partialTicks, i11);
            if(!this.mc.gameSettings.anaglyph) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles() {
        EntityPlayerSP entityPlayerSP1 = this.mc.thePlayer;
        World world2 = this.mc.theWorld;
        int i3 = MathHelper.floor_double(entityPlayerSP1.posX);
        int i4 = MathHelper.floor_double(entityPlayerSP1.posY);
        int i5 = MathHelper.floor_double(entityPlayerSP1.posZ);
        byte b6 = 4;

        for(int i7 = 0; i7 < 50; ++i7) {
            int i8 = i3 + this.random.nextInt(b6 * 2 + 1) - b6;
            int i9 = i5 + this.random.nextInt(b6 * 2 + 1) - b6;
            int i10 = world2.getTopSolidOrLiquidBlock(i8, i9);
            int i11 = world2.getBlockId(i8, i10 - 1, i9);
            if(i10 <= i4 + b6 && i10 >= i4 - b6) {
                float f12 = this.random.nextFloat();
                float f13 = this.random.nextFloat();
                if(i11 > 0) {
                    this.mc.effectRenderer.addEffect(new EntityRainFX(world2, (double)((float)i8 + f12), (double)((float)i10 + 0.1F) - Block.blocksList[i11].minY, (double)((float)i9 + f13)));
                }
            }
        }

    }

    private void renderRain(float partialTicks) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        World world3 = this.mc.theWorld;
        int i4 = MathHelper.floor_double(entityPlayerSP2.posX);
        int i5 = MathHelper.floor_double(entityPlayerSP2.posY);
        int i6 = MathHelper.floor_double(entityPlayerSP2.posZ);
        Tessellator tessellator7 = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
        byte b8 = 5;

        for(int i9 = i4 - b8; i9 <= i4 + b8; ++i9) {
            for(int i10 = i6 - b8; i10 <= i6 + b8; ++i10) {
                int i11 = world3.getTopSolidOrLiquidBlock(i9, i10);
                int i12 = i5 - b8;
                int i13 = i5 + b8;
                if(i12 < i11) {
                    i12 = i11;
                }

                if(i13 < i11) {
                    i13 = i11;
                }

                float f14 = 2.0F;
                if(i12 != i13) {
                    float f15 = ((float)((this.rendererUpdateCount + i9 * 3121 + i10 * 418711) % 32) + partialTicks) / 32.0F;
                    double d16 = (double)((float)i9 + 0.5F) - entityPlayerSP2.posX;
                    double d18 = (double)((float)i10 + 0.5F) - entityPlayerSP2.posZ;
                    float f20 = MathHelper.sqrt_double(d16 * d16 + d18 * d18) / (float)b8;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f20 * f20) * 0.7F);
                    tessellator7.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                    tessellator7.addVertexWithUV((double)(i9 + 0), (double)i12, (double)(i10 + 0), (double)(0.0F * f14), (double)((float)i12 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 1), (double)i12, (double)(i10 + 1), (double)(1.0F * f14), (double)((float)i12 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 1), (double)i13, (double)(i10 + 1), (double)(1.0F * f14), (double)((float)i13 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 0), (double)i13, (double)(i10 + 0), (double)(0.0F * f14), (double)((float)i13 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 0), (double)i12, (double)(i10 + 1), (double)(0.0F * f14), (double)((float)i12 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 1), (double)i12, (double)(i10 + 0), (double)(1.0F * f14), (double)((float)i12 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 1), (double)i13, (double)(i10 + 0), (double)(1.0F * f14), (double)((float)i13 * f14 / 8.0F + f15 * f14));
                    tessellator7.addVertexWithUV((double)(i9 + 0), (double)i13, (double)(i10 + 1), (double)(0.0F * f14), (double)((float)i13 * f14 / 8.0F + f15 * f14));
                    tessellator7.draw();
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void setupOverlayRendering() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.mc.scaledResolution.getScaledWidth_double(), this.mc.scaledResolution.getScaledHeight_double(),
                0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float partialTicks) {
        World world2 = this.mc.theWorld;
        EntityPlayerSP entityPlayerSP3 = this.mc.thePlayer;
        float f4 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
        f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
        Vec3D vec3D5 = world2.getSkyColor(partialTicks);
        float f6 = (float)vec3D5.xCoord;
        float f7 = (float)vec3D5.yCoord;
        float f8 = (float)vec3D5.zCoord;
        Vec3D vec3D9 = world2.getFogColor(partialTicks);
        this.fogColorRed = (float)vec3D9.xCoord;
        this.fogColorGreen = (float)vec3D9.yCoord;
        this.fogColorBlue = (float)vec3D9.zCoord;
        this.fogColorRed += (f6 - this.fogColorRed) * f4;
        this.fogColorGreen += (f7 - this.fogColorGreen) * f4;
        this.fogColorBlue += (f8 - this.fogColorBlue) * f4;
        if(entityPlayerSP3.isInsideOfMaterial(Material.water)) {
            this.fogColorRed = 0.02F;
            this.fogColorGreen = 0.02F;
            this.fogColorBlue = 0.2F;
        } else if(entityPlayerSP3.isInsideOfMaterial(Material.lava)) {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
        }

        float f10 = this.prevFogColor + (this.fogColor - this.prevFogColor) * partialTicks;
        this.fogColorRed *= f10;
        this.fogColorGreen *= f10;
        this.fogColorBlue *= f10;
        if(this.mc.gameSettings.anaglyph) {
            float f11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float f12 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float f13 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = f11;
            this.fogColorGreen = f12;
            this.fogColorBlue = f13;
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int fogFlag) {
        EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
        GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(entityPlayerSP2.isInsideOfMaterial(Material.water)) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
        } else if(entityPlayerSP2.isInsideOfMaterial(Material.lava)) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
            GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
            if(fogFlag < 0) {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance * 0.8F);
            }
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }

    private FloatBuffer setFogColorBuffer(float f1, float f2, float f3, float f4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(f1).put(f2).put(f3).put(f4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}
