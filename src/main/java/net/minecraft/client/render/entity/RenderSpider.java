package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntitySpider;

public final class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected final float getMaxDeathRotation(EntityLiving livingEntity) {
		return 180.0F;
	}

	protected final boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
		EntitySpider entitySpider10001 = (EntitySpider)livingEntity;
		int i3 = flag;
		EntitySpider flag1 = entitySpider10001;
		if(i3 != 0) {
			return false;
		} else if(i3 != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float livingEntity1 = (1.0F - flag1.getBrightness(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, livingEntity1);
			return true;
		}
	}
}