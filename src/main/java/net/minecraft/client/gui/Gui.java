package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;

public class Gui {
	protected float zLevel = 0.0F;

    protected void drawRect(int i1, int i2, int i3, int i4, int i5) {
        float f6 = (float)(i5 >> 24 & 255) / 255.0F;
        float f7 = (float)(i5 >> 16 & 255) / 255.0F;
        float f8 = (float)(i5 >> 8 & 255) / 255.0F;
        float f9 = (float)(i5 & 255) / 255.0F;
        Tessellator tessellator10 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f7, f8, f9, f6);
        tessellator10.startDrawingQuads(DefaultVertexFormats.POSITION);
        tessellator10.addVertex((double)i1, (double)i4, 0.0D);
        tessellator10.addVertex((double)i3, (double)i4, 0.0D);
        tessellator10.addVertex((double)i3, (double)i2, 0.0D);
        tessellator10.addVertex((double)i1, (double)i2, 0.0D);
        tessellator10.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
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
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator12 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
        tessellator12.setColorRGBA_F(f7, f8, startColor1, f6);
        tessellator12.addVertex((double)right, (double)top, 0.0D);
        tessellator12.addVertex((double)left, (double)top, 0.0D);
        tessellator12.setColorRGBA_F(f10, f11, endColor1, f9);
        tessellator12.addVertex((double)left, (double)bottom, 0.0D);
        tessellator12.addVertex((double)right, (double)bottom, 0.0D);
        tessellator12.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void drawCenteredString(FontRenderer fontRenderer, String message, int x, int y, int color) {
        fontRenderer.drawStringWithShadow(message, x - fontRenderer.getStringWidth(message) / 2, y, color);
    }

    public void drawString(FontRenderer fontRenderer, String message, int x, int y, int color) {
        fontRenderer.drawStringWithShadow(message, x, y, color);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        Tessellator tessellator7 = Tessellator.instance;
        Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator7.addVertexWithUV((double)x, (double)(y + height), (double)this.zLevel, (double)((float)u * 0.00390625F), (double)((float)(v + height) * 0.00390625F));
        tessellator7.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * 0.00390625F), (double)((float)(v + height) * 0.00390625F));
        tessellator7.addVertexWithUV((double)(x + width), (double)y, (double)this.zLevel, (double)((float)(u + width) * 0.00390625F), (double)((float)v * 0.00390625F));
        tessellator7.addVertexWithUV((double)x, (double)y, (double)this.zLevel, (double)((float)u * 0.00390625F), (double)((float)v * 0.00390625F));
        tessellator7.draw();
    }
}
