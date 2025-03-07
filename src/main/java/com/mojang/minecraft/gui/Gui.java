package com.mojang.minecraft.gui;

import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public class Gui {
    protected float zLevel = 0.0F;

    protected static void fill(int i0, int i1, int i2, int i3, int i4) {
        float f5 = (float)(i4 >>> 24) / 255.0F;
        float f6 = (float)(i4 >> 16 & 255) / 255.0F;
        float f7 = (float)(i4 >> 8 & 255) / 255.0F;
        float f9 = (float)(i4 & 255) / 255.0F;
        Tesselator tesselator8 = Tesselator.instance;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f6, f7, f9, f5);
        tesselator8.begin(DefaultVertexFormats.POSITION);
        tesselator8.vertex((float)i0, (float)i3, 0.0F);
        tesselator8.vertex((float)i2, (float)i3, 0.0F);
        tesselator8.vertex((float)i2, (float)i1, 0.0F);
        tesselator8.vertex((float)i0, (float)i1, 0.0F);
        tesselator8.end();
        GL11.glDisable(3042);
    }

    protected static void fillGradient(int i0, int i1, int i2, int i3, int i4, int i5) {
        float f6 = (float)(i4 >>> 24) / 255.0F;
        float f7 = (float)(i4 >> 16 & 255) / 255.0F;
        float f8 = (float)(i4 >> 8 & 255) / 255.0F;
        float f12 = (float)(i4 & 255) / 255.0F;
        float f9 = (float)(i5 >>> 24) / 255.0F;
        float f10 = (float)(i5 >> 16 & 255) / 255.0F;
        float f11 = (float)(i5 >> 8 & 255) / 255.0F;
        float f13 = (float)(i5 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(7, DefaultVertexFormats.POSITION);
        GL11.glColor4f(f7, f8, f12, f6);
        GL11.glVertex2f((float)i2, (float)i1);
        GL11.glVertex2f((float)i0, (float)i1);
        GL11.glColor4f(f10, f11, f13, f9);
        GL11.glVertex2f((float)i0, (float)i3);
        GL11.glVertex2f((float)i2, (float)i3);
        GL11.glEnd();
        GL11.glDisable(3042);
    }

    public static void drawCenteredString(Font font0, String string1, int i2, int i3, int i4) {
        font0.drawShadow(string1, i2 - font0.width(string1) / 2, i3, i4);
    }

    public static void drawString(Font font0, String string1, int i2, int i3, int i4) {
        font0.drawShadow(string1, i2, i3, i4);
    }

    public final void blit(int i1, int i2, int i3, int i4, int i5, int i6) {
        float f7 = 0.00390625F;
        float f8 = 0.00390625F;
        Tesselator tesselator9 = Tesselator.instance;
        Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);
        tesselator9.vertexUV((float)i1, (float)(i2 + i6), this.zLevel, (float)i3 * f7, (float)(i4 + i6) * f8);
        tesselator9.vertexUV((float)(i1 + i5), (float)(i2 + i6), this.zLevel, (float)(i3 + i5) * f7, (float)(i4 + i6) * f8);
        tesselator9.vertexUV((float)(i1 + i5), (float)i2, this.zLevel, (float)(i3 + i5) * f7, (float)i4 * f8);
        tesselator9.vertexUV((float)i1, (float)i2, this.zLevel, (float)i3 * f7, (float)i4 * f8);
        tesselator9.end();
    }
}
