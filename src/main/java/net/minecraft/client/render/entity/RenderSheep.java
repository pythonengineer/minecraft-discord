package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntitySheep;

public class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase baseModel, ModelBase renderPassModel, float shadowSize) {
		super(baseModel, 0.7F);
		this.setRenderPassModel(renderPassModel);
	}

	protected boolean shouldRenderPass(EntitySheep sheep, int flag) {
		this.loadTexture("/mob/sheep_fur.png");
		return flag == 0 && !sheep.sheared;
	}

    protected boolean shouldRenderPass(EntityLiving eVar, int flag) {
        return this.shouldRenderPass((EntitySheep)eVar, flag);
    }
}
