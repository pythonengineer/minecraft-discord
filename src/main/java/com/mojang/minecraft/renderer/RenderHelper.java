package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public final class RenderHelper {
    public Minecraft minecraft;
    public float fogColorMultiplier = 1.0F;
    public boolean displayActive = false;
    public float fogColorRed = 0.5F;
    public float fogColorGreen = 0.8F;
    public float fogColorBlue = 1.0F;
    public float renderDistance = 0.0F;
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
    private FloatBuffer lb = BufferUtils.createFloatBuffer(16);

    public RenderHelper(Minecraft minecraft1) {
        this.minecraft = minecraft1;
    }

    public void toggleLight(boolean z1) {
        if(!z1) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_LIGHT0);
        } else {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
            float f4 = 0.7F;
            float f2 = 0.3F;
            Vec3 vec33 = (new Vec3(0.0F, -1.0F, 0.5F)).normalize();
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.getBuffer(vec33.x, vec33.y, vec33.z, 0.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.getBuffer(f2, f2, f2, 1.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f4, f4, f4, 1.0F));
        }
    }

    public final void initGui() {
        this.minecraft.setupOrthoCamera();
    }

    public void setupFog() {
        Level level1 = this.minecraft.level;
        Player player2 = this.minecraft.player;
        GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tile tile3;
        if((tile3 = Tile.tiles[level1.getTile((int)player2.x, (int)(player2.y + 0.12F), (int)player2.z)]) != null && tile3.getLiquidType() != Liquid.none) {
            Liquid liquid4 = tile3.getLiquidType();
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            if(liquid4 == Liquid.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.4F, 0.9F, 1.0F));
            } else if(liquid4 == Liquid.lava) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
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

    private FloatBuffer getBuffer(float f1, float f2, float f3, float f4) {
        this.lb.clear();
        this.lb.put(f1).put(f2).put(f3).put(f4);
        this.lb.flip();
        return this.lb;
    }
}