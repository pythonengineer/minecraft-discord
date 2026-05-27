package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntityGiantZombie;

public class RenderGiantZombie extends RenderLiving {
	private float scale;

	public RenderGiantZombie(ModelBase baseModel, float width, float height) {
		super(baseModel, width * height);
		this.scale = height;
	}

	protected void preRenderCallback(EntityGiantZombie livingEntity, float partialTicks) {
		GL11.glScalef(this.scale, this.scale, this.scale);
	}

    protected void preRenderCallback(EntityLiving entityLiving, float partialTicks) {
        this.preRenderCallback((EntityGiantZombie)entityLiving, partialTicks);
    }
}
