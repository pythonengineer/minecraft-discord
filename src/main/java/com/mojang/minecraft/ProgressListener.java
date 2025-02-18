package com.mojang.minecraft;

import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class ProgressListener {
    private String title = "";
    private Minecraft minecraft;
    private String text = "";

    public ProgressListener(Minecraft minecraft1) {
        this.minecraft = minecraft1;
    }

    public final void beginLevelLoading(String string1) {
        if(!this.minecraft.running) {
            throw new StopGameException();
        } else {
            this.text = string1;
            int i3 = Minecraft.scaledResolution.getScaledWidth();
            int i2 = Minecraft.scaledResolution.getScaledHeight();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, (double)i3, (double)i2, 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        }
    }

    public final void levelLoadUpdate(String string1) {
        if(!this.minecraft.running) {
            throw new StopGameException();
        } else {
            this.title = string1;
            this.setLoadingProgress(-1);
        }
    }

    public final void setLoadingProgress(int i1) {
        if(!this.minecraft.running) {
            throw new StopGameException();
        } else {
            if(i1 >= 0) {
                return;
            }
            int i2 = Minecraft.scaledResolution.getScaledWidth();
            int i3 = Minecraft.scaledResolution.getScaledHeight();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            Tesselator tesselator4 = Tesselator.instance;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int i5 = this.minecraft.textures.getTextureId("/dirt.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
            float f8 = 32.0F;
            tesselator4.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            tesselator4.color(4210752);
            tesselator4.vertexUV(0.0F, (float)i3, 0.0F, 0.0F, (float)i3 / f8);
            tesselator4.vertexUV((float)i2, (float)i3, 0.0F, (float)i2 / f8, (float)i3 / f8);
            tesselator4.vertexUV((float)i2, 0.0F, 0.0F, (float)i2 / f8, 0.0F);
            tesselator4.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            tesselator4.end();
            if(i1 >= 0) {
                i5 = i2 / 2 - 50;
                int i6 = i3 / 2 + 16;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tesselator4.begin(DefaultVertexFormats.POSITION_COLOR);
                tesselator4.color(8421504);
                tesselator4.vertex((float)i5, (float)i6, 0.0F);
                tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + 100), (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + 100), (float)i6, 0.0F);
                tesselator4.color(8454016);
                tesselator4.vertex((float)i5, (float)i6, 0.0F);
                tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + i1), (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + i1), (float)i6, 0.0F);
                tesselator4.end();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            this.minecraft.font.drawShadow(this.text, (i2 - this.minecraft.font.width(this.text)) / 2, i3 / 2 - 4 - 16, 0xFFFFFF);
            this.minecraft.font.drawShadow(this.title, (i2 - this.minecraft.font.width(this.title)) / 2, i3 / 2 - 4 + 8, 0xFFFFFF);
            Display.update();

            try {
                Thread.yield();
            } catch (Exception exception7) {
            }
        }
    }
}