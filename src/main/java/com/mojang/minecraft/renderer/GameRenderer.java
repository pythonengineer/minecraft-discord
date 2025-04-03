package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class GameRenderer {
    public Minecraft minecraft;
    public float fogColorMultiplier = 1.0F;
    public boolean displayActive = false;
    public float renderDistance = 0.0F;
    public TileRenderer tileRenderer;
    public int rainTicks;
    public EaglercraftRandom random = new EaglercraftRandom();
    private volatile int u1 = 0;
    private volatile int u2 = 0;
    private FloatBuffer lb = BufferUtils.createFloatBuffer(16);
    public float fogRed;
    public float fogGreen;
    public float fogBlue;

    public GameRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.tileRenderer = new TileRenderer(minecraft);
    }

    public Vec3 getPlayerRotVec(float rot) {
        Player player4;
        float f2 = (player4 = this.minecraft.player).xo + (player4.x - player4.xo) * rot;
        float f3 = player4.yo + (player4.y - player4.yo) * rot;
        float f5 = player4.zo + (player4.z - player4.zo) * rot;
        return new Vec3(f2, f3, f5);
    }

    public void renderHurtFrames(float a) {
        Player player3;
        float f2 = (float)(player3 = this.minecraft.player).hurtTime - a;
        if(player3.health <= 0) {
            a += (float)player3.deathTime;
            GL11.glRotatef(40.0F - 8000.0F / (a + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if(f2 >= 0.0F) {
            f2 = (float)Math.sin((double)((f2 /= (float)player3.hurtDuration) * f2 * f2 * f2) * Math.PI);
            a = player3.hurtDir;
            if(Float.isNaN(f2)) {
                f2 = 0.0F;
            }
            GL11.glRotatef(-player3.hurtDir, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f2 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(a, 0.0F, 1.0F, 0.0F);
        }
    }

    public void cameraBob(float a) {
        Player player4;
        float f2 = (player4 = this.minecraft.player).walkDist - player4.walkDistO;
        f2 = player4.walkDist + f2 * a;
        float f3 = player4.oBob + (player4.bob - player4.oBob) * a;
        float f5 = player4.oTilt + (player4.tilt - player4.oTilt) * a;
        GL11.glTranslatef((float)Math.sin((double)f2 * Math.PI) * f3 * 0.5F, -((float)Math.abs(Math.cos((double)f2 * Math.PI) * (double)f3)), 0.0F);
        GL11.glRotatef((float)Math.sin((double)f2 * Math.PI) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef((float)Math.abs(Math.cos((double)f2 * Math.PI + (double)0.2F) * (double)f3) * 5.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(f5, 1.0F, 0.0F, 0.0F);
    }

    public void renderRain(float a) {
        Player player2 = this.minecraft.player;
        Level level3 = this.minecraft.level;
        int i4 = (int)player2.x;
        int i5 = (int)player2.y;
        int i6 = (int)player2.z;
        Tesselator tesselator7 = Tesselator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/rain.png"));

        for(int i8 = i4 - 5; i8 <= i4 + 5; ++i8) {
            for(int i9 = i6 - 5; i9 <= i6 + 5; ++i9) {
                int i10 = level3.getHighestTile(i8, i9);
                int i11 = i5 - 5;
                int i12 = i5 + 5;
                if(i11 < i10) {
                    i11 = i10;
                }

                if(i12 < i10) {
                    i12 = i10;
                }

                if(i11 != i12) {
                    float f15 = ((float)((this.rainTicks + i8 * 3121 + i9 * 418711) % 32) + a) / 32.0F;
                    float f13 = (float)i8 + 0.5F - player2.x;
                    float f14 = (float)i9 + 0.5F - player2.z;
                    f13 = (float)Math.sqrt((double)(f13 * f13 + f14 * f14)) / (float)5;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f13 * f13) * 0.7F);
                    tesselator7.begin(DefaultVertexFormats.POSITION_TEX);
                    tesselator7.vertexUV((float)i8, (float)i11, (float)i9, 0.0F, (float)i11 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)(i8 + 1), (float)i11, (float)(i9 + 1), 2.0F, (float)i11 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)(i8 + 1), (float)i12, (float)(i9 + 1), 2.0F, (float)i12 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)i8, (float)i12, (float)i9, 0.0F, (float)i12 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)i8, (float)i11, (float)(i9 + 1), 0.0F, (float)i11 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)(i8 + 1), (float)i11, (float)i9, 2.0F, (float)i11 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)(i8 + 1), (float)i12, (float)i9, 2.0F, (float)i12 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.vertexUV((float)i8, (float)i12, (float)(i9 + 1), 0.0F, (float)i12 * 2.0F / 8.0F + f15 * 2.0F);
                    tesselator7.end();
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public final void toggleLight(boolean light) {
        if(!light) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_LIGHT0);
        } else {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
            float light1 = 0.7F;
            float f2 = 0.3F;
            Vec3 vec33 = (new Vec3(0.0F, -1.0F, 0.5F)).normalize();
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.getBuffer(vec33.x, vec33.y, vec33.z, 0.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.getBuffer(f2, f2, f2, 1.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(light1, light1, light1, 1.0F));
        }
    }

    public final void render() {
        this.minecraft.setupOrthoCamera();
    }

    public void setupFog() {
        Level level1 = this.minecraft.level;
        Player player2 = this.minecraft.player;
        GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogRed, this.fogGreen, this.fogBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tile tile5;
        if((tile5 = Tile.tiles[level1.getTile((int)player2.x, (int)(player2.y + 0.12F), (int)player2.z)]) != null && tile5.getLiquidType() != Liquid.none) {
            Liquid liquid6 = tile5.getLiquidType();
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            float f3;
            float f4;
            float f7;
            float f8;
            if(liquid6 == Liquid.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
                f7 = 0.4F;
                f8 = 0.4F;
                f3 = 0.9F;
                if(this.minecraft.options.anaglyph3d) {
                    f4 = (f7 * 30.0F + f8 * 59.0F + f3 * 11.0F) / 100.0F;
                    f8 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
                    f3 = (f7 * 30.0F + f3 * 70.0F) / 100.0F;
                    f7 = f4;
                    f8 = f8;
                    f3 = f3;
                }

                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f7, f8, f3, 1.0F));
            } else if(liquid6 == Liquid.lava) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
                f7 = 0.4F;
                f8 = 0.3F;
                f3 = 0.3F;
                if(this.minecraft.options.anaglyph3d) {
                    f4 = (f7 * 30.0F + f8 * 59.0F + f3 * 11.0F) / 100.0F;
                    f8 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
                    f3 = (f7 * 30.0F + f3 * 70.0F) / 100.0F;
                    f7 = f4;
                    f8 = f8;
                    f3 = f3;
                }

                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f7, f8, f3, 1.0F));
            }
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer getBuffer(float r, float g, float b, float a) {
        this.lb.clear();
        this.lb.put(r).put(g).put(b).put(a);
        this.lb.flip();
        return this.lb;
    }
}