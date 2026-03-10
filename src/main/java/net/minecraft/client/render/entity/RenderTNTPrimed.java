package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.world.block.Block;

public final class RenderTNTPrimed extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		EntityTNTPrimed entityTNTPrimed10001 = (EntityTNTPrimed)entity;
		double d12 = x;
		EntityTNTPrimed x1 = entityTNTPrimed10001;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d12, (float)y, (float)z);
		float y1;
		if((float)x1.fuse - partialTicks + 1.0F < 10.0F) {
			if((y1 = 1.0F - ((float)x1.fuse - partialTicks + 1.0F) / 10.0F) < 0.0F) {
				y1 = 0.0F;
			}

			if(y1 > 1.0F) {
				y1 = 1.0F;
			}

			y1 = (y1 *= y1) * y1;
			GL11.glScalef(y1 = 1.0F + y1 * 0.3F, y1, y1);
		}

		y1 = (1.0F - ((float)x1.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.renderBlocks.renderBlockOnInventory(Block.tnt);
		if(x1.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, y1);
			this.renderBlocks.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}
}