package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.EntityLiving;

public final class RenderPlayer extends RenderLiving {
    public RenderPlayer() {
        super(new ModelBiped(), 0.5F);
        new ModelBiped(1.0F);
        new ModelBiped(0.5F);
    }

    protected final boolean shouldRenderPass(EntityLiving var1, int var2) {
        return false;
    }
}
