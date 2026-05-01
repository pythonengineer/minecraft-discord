package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

public final class RenderEntity extends Render {
    public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        double d10001 = x - entity.lastTickPosX;
        double d10002 = y - entity.lastTickPosY;
        double d15 = z - entity.lastTickPosZ;
        double d13 = d10002;
        double d11 = d10001;
        AxisAlignedBB axisAlignedBB = entity.boundingBox;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        t.startDrawingQuads(DefaultVertexFormats.POSITION_NORMAL);
        t.setTranslationD(d11, d13, d15);
        t.setNormal(0.0F, 0.0F, -1.0F);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.setNormal(0.0F, 0.0F, 1.0F);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.setNormal(0.0F, -1.0F, 0.0F);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.setNormal(0.0F, 1.0F, 0.0F);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.setNormal(-1.0F, 0.0F, 0.0F);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.setNormal(1.0F, 0.0F, 0.0F);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        t.drawVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        t.setTranslationD(0.0D, 0.0D, 0.0D);
        t.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
