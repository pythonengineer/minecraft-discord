package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class RenderEntity extends Render {
	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		GL11.glPushMatrix();
		GL11.glTranslatef(var2 - var1.lastTickPosX, var3 - var1.lastTickPosY, var4 - var1.lastTickPosZ);
		AxisAlignedBB var7 = var1.boundingBox;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator var8 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var8.startDrawingQuads(DefaultVertexFormats.POSITION_NORMAL);
		var8.normal(0.0F, 0.0F, -1.0F);
		var8.addVertex(var7.minX, var7.maxY, var7.minZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.minZ);
		var8.addVertex(var7.maxX, var7.minY, var7.minZ);
		var8.addVertex(var7.minX, var7.minY, var7.minZ);
		var8.normal(0.0F, 0.0F, 1.0F);
		var8.addVertex(var7.minX, var7.minY, var7.maxZ);
		var8.addVertex(var7.maxX, var7.minY, var7.maxZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.maxZ);
		var8.addVertex(var7.minX, var7.maxY, var7.maxZ);
		var8.normal(0.0F, -1.0F, 0.0F);
		var8.addVertex(var7.minX, var7.minY, var7.minZ);
		var8.addVertex(var7.maxX, var7.minY, var7.minZ);
		var8.addVertex(var7.maxX, var7.minY, var7.maxZ);
		var8.addVertex(var7.minX, var7.minY, var7.maxZ);
		var8.normal(0.0F, 1.0F, 0.0F);
		var8.addVertex(var7.minX, var7.maxY, var7.maxZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.maxZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.minZ);
		var8.addVertex(var7.minX, var7.maxY, var7.minZ);
		var8.normal(-1.0F, 0.0F, 0.0F);
		var8.addVertex(var7.minX, var7.minY, var7.maxZ);
		var8.addVertex(var7.minX, var7.maxY, var7.maxZ);
		var8.addVertex(var7.minX, var7.maxY, var7.minZ);
		var8.addVertex(var7.minX, var7.minY, var7.minZ);
		var8.normal(1.0F, 0.0F, 0.0F);
		var8.addVertex(var7.maxX, var7.minY, var7.minZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.minZ);
		var8.addVertex(var7.maxX, var7.maxY, var7.maxZ);
		var8.addVertex(var7.maxX, var7.minY, var7.maxZ);
		var8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
}
