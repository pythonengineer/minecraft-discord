package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntitySheep;

public final class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase baseModel, ModelBase renderPassModel, float shadowSize) {
		super(baseModel, 0.7F);
		this.setRenderPassModel(renderPassModel);
	}

	protected final boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
		EntitySheep entitySheep10001 = (EntitySheep)livingEntity;
		int i3 = flag;
		EntitySheep flag1 = entitySheep10001;
		this.loadTexture("/mob/sheep_fur.png");
		return i3 == 0 && !flag1.sheared;
	}
}