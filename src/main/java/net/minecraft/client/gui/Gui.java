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
		var8.addVertex((float)var0, (float)var3, 0.0F);
		var8.addVertex((float)var2, (float)var3, 0.0F);
		var8.addVertex((float)var2, (float)var1, 0.0F);
		var8.addVertex((float)var0, (float)var1, 0.0F);
		var8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected static void drawGradientRect(int var0, int var1, int var2, int var3, int var4, int var5) {
		float var6 = (float)(var4 >>> 24) / 255.0F;
		float var7 = (float)(var4 >> 16 & 255) / 255.0F;
		float var8 = (float)(var4 >> 8 & 255) / 255.0F;
		float var12 = (float)(var4 & 255) / 255.0F;
		float var9 = (float)(var5 >>> 24) / 255.0F;
		float var10 = (float)(var5 >> 16 & 255) / 255.0F;
		float var11 = (float)(var5 >> 8 & 255) / 255.0F;
		float var13 = (float)(var5 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		GL11.glColor4f(var7, var8, var12, var6);
		GL11.glVertex2f((float)var2, (float)var1);
		GL11.glVertex2f((float)var0, (float)var1);
		GL11.glColor4f(var10, var11, var13, var9);
		GL11.glVertex2f((float)var0, (float)var3);
		GL11.glVertex2f((float)var2, (float)var3);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawCenteredString(FontRenderer var0, String var1, int var2, int var3, int var4) {
		var0.drawStringWithShadow(var1, var2 - var0.getStringWidth(var1) / 2, var3, var4);
	}

	public final void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
		var9.addVertexWithUV((float)var1, (float)(var2 + var6), this.zLevel, (float)var3 * var7, (float)(var4 + var6) * var8);
		var9.addVertexWithUV((float)(var1 + var5), (float)(var2 + var6), this.zLevel, (float)(var3 + var5) * var7, (float)(var4 + var6) * var8);
		var9.addVertexWithUV((float)(var1 + var5), (float)var2, this.zLevel, (float)(var3 + var5) * var7, (float)var4 * var8);
		var9.addVertexWithUV((float)var1, (float)var2, this.zLevel, (float)var3 * var7, (float)var4 * var8);
		var9.draw();
	}
}
