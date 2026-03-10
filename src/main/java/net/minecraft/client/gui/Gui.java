package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public class Gui {
	protected float zLevel = 0.0F;

    protected static void drawRect(int var0, int var1, int var2, int var3, int var4) {
        float var5 = (float)(var4 >>> 24) / 255.0F;
        float var6 = (float)(var4 >> 16 & 255) / 255.0F;
        float var7 = (float)(var4 >> 8 & 255) / 255.0F;
        float var9 = (float)(var4 & 255) / 255.0F;
        Tessellator var8 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(var6, var7, var9, var5);
        var8.startDrawingQuads(DefaultVertexFormats.POSITION);
        var8.drawVertex((double)var0, (double)var3, 0.0D);
        var8.drawVertex((double)var2, (double)var3, 0.0D);
        var8.drawVertex((double)var2, (double)var1, 0.0D);
        var8.drawVertex((double)var0, (double)var1, 0.0D);
        var8.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected static void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f6 = (float)(startColor >>> 24) / 255.0F;
        float f7 = (float)(startColor >> 16 & 255) / 255.0F;
        float f8 = (float)(startColor >> 8 & 255) / 255.0F;
        float startColor1 = (float)(startColor & 255) / 255.0F;
        float f9 = (float)(endColor >>> 24) / 255.0F;
        float f10 = (float)(endColor >> 16 & 255) / 255.0F;
        float f11 = (float)(endColor >> 8 & 255) / 255.0F;
        float endColor1 = (float)(endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator12 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
        tessellator12.setColorRGBA_F(f7, f8, startColor1, f6);
        tessellator12.drawVertex((double)right, (double)top, 0.0D);
        tessellator12.drawVertex((double)left, (double)top, 0.0D);
        tessellator12.setColorRGBA_F(f10, f11, endColor1, f9);
        tessellator12.drawVertex((double)left, (double)bottom, 0.0D);
        tessellator12.drawVertex((double)right, (double)bottom, 0.0D);
        tessellator12.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawCenteredString(FontRenderer fontRenderer, String message, int x, int y, int color) {
        fontRenderer.drawStringWithShadow(message, x - fontRenderer.width(message) / 2, y, color);
    }

    public static void drawString(FontRenderer fontRenderer, String message, int x, int y, int color) {
        fontRenderer.drawStringWithShadow(message, x, y, color);
    }

    public final void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        Tessellator tessellator7 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator7.addVertexWithUV((double)x, (double)(y + height), (double)this.zLevel, (double)((float)u * 0.00390625F), (double)((float)(v + height) * 0.00390625F));
        tessellator7.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * 0.00390625F), (double)((float)(v + height) * 0.00390625F));
        tessellator7.addVertexWithUV((double)(x + width), (double)y, (double)this.zLevel, (double)((float)(u + width) * 0.00390625F), (double)((float)v * 0.00390625F));
        tessellator7.addVertexWithUV((double)x, (double)y, (double)this.zLevel, (double)((float)u * 0.00390625F), (double)((float)v * 0.00390625F));
        tessellator7.draw();
    }
}
