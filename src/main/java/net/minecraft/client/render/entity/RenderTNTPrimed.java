package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;

public final class RenderTNTPrimed extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks(Tessellator.instance);

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		GL11.glPushMatrix();
		GL11.glTranslatef(var2, var3, var4);
		this.loadTexture("/terrain.png");
		this.renderBlocks.renderBlockOnInventory(Block.tnt);
		GL11.glPopMatrix();
	}
}
