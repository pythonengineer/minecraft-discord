package com.mojang.minecraft.gui;

import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public class GuiComponent {
    protected float blitOffset = 0.0F;

    protected static void fill(int x0, int y0, int x1, int y1, int color) {
        float f5 = (float)(color >>> 24) / 255.0F;
        float f6 = (float)(color >> 16 & 255) / 255.0F;
        float f7 = (float)(color >> 8 & 255) / 255.0F;
        float color1 = (float)(color & 255) / 255.0F;
        Tesselator tesselator8 = Tesselator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f6, f7, color1, f5);
        tesselator8.begin(DefaultVertexFormats.POSITION);
        tesselator8.vertex((float)x0, (float)y1, 0.0F);
        tesselator8.vertex((float)x1, (float)y1, 0.0F);
        tesselator8.vertex((float)x1, (float)y0, 0.0F);
        tesselator8.vertex((float)x0, (float)y0, 0.0F);
        tesselator8.end();
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected static void fillGradient(int x0, int y0, int x1, int y1, int color1, int color2) {
        float x01 = (float)(color1 >>> 24) / 255.0F;
        float y01 = (float)(color1 >> 16 & 255) / 255.0F;
        float f6 = (float)(color1 >> 8 & 255) / 255.0F;
        float color11 = (float)(color1 & 255) / 255.0F;
        float f7 = (float)(color2 >>> 24) / 255.0F;
        float f8 = (float)(color2 >> 16 & 255) / 255.0F;
        float f9 = (float)(color2 >> 8 & 255) / 255.0F;
        float color21 = (float)(color2 & 255) / 255.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        GL11.glColor4f(y01, f6, color11, x01);
        GL11.glVertex2f((float)x1, 0.0F);
        GL11.glVertex2f(0.0F, 0.0F);
        GL11.glColor4f(f8, f9, color21, f7);
        GL11.glVertex2f(0.0F, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawCenteredString(Font font, String str, int x, int y, int color) {
        font.drawShadow(str, x - font.width(str) / 2, y, color);
    }

    public static void drawString(Font font, String str, int x, int y, int color) {
        font.drawShadow(str, x, y, color);
    }

    public final void blit(int x0, int y0, int x1, int y1, int w, int h) {
        float f7 = 0.00390625F;
        float f8 = 0.00390625F;
        Tesselator tesselator9 = Tesselator.instance;
        Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);
        tesselator9.vertexUV((float)x0, (float)(y0 + h), this.blitOffset, (float)x1 * f7, (float)(y1 + h) * f8);
        tesselator9.vertexUV((float)(x0 + w), (float)(y0 + h), this.blitOffset, (float)(x1 + w) * f7, (float)(y1 + h) * f8);
        tesselator9.vertexUV((float)(x0 + w), (float)y0, this.blitOffset, (float)(x1 + w) * f7, (float)y1 * f8);
        tesselator9.vertexUV((float)x0, (float)y0, this.blitOffset, (float)x1 * f7, (float)y1 * f8);
        tesselator9.end();
    }
}