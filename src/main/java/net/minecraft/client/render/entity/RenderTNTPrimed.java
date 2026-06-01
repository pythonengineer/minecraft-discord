package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.world.block.Block;

public class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public void doRenderTNT(EntityTNTPrimed entity, double x, double y, double z, float yaw, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		float f10;
		if((float)entity.fuse - partialTicks + 1.0F < 10.0F) {
			f10 = 1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 10.0F;
			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			if(f10 > 1.0F) {
				f10 = 1.0F;
			}

			f10 *= f10;
			f10 *= f10;
			float f11 = 1.0F + f10 * 0.3F;
			GL11.glScalef(f11, f11, f11);
		}

		f10 = (1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt);
		if(entity.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f10);
			this.blockRenderer.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.doRenderTNT((EntityTNTPrimed)entity, x, y, z, yaw, partialTicks);
    }
}
