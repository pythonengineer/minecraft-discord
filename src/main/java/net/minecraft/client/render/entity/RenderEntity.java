package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.entity.Entity;

public final class RenderEntity extends Render {
    public final void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(var2 - var1.lastTickPosX), (float)(var4 - var1.lastTickPosY), (float)(var6 - var1.lastTickPosZ));
        renderOffsetAABB(var1.boundingBox);
        GL11.glPopMatrix();
    }
}
