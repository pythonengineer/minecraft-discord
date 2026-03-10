package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;

public final class RenderGiantZombie extends RenderLiving {
	private float scale = 6.0F;

	public RenderGiantZombie(ModelBase baseModel, float width, float height) {
		super(baseModel, 3.0F);
	}

	protected final void preRenderCallback(EntityLiving livingEntity, float partialTicks) {
		GL11.glScalef(this.scale, this.scale, this.scale);
	}
}