package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntitySpider;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float getMaxDeathRotation(EntitySpider spider) {
		return 180.0F;
	}

	protected boolean shouldRenderPass(EntitySpider spider, int flag) {
		if(flag != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float f4 = (1.0F - spider.getBrightness(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
			return true;
		}
	}

    protected float getMaxDeathRotation(EntityLiving eVar) {
        return this.getMaxDeathRotation((EntitySpider)eVar);
    }

    protected boolean shouldRenderPass(EntityLiving eVar, int flag) {
        return this.shouldRenderPass((EntitySpider)eVar, flag);
    }
}
