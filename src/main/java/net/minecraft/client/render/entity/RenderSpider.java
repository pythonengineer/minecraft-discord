package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.game.entity.EntityLiving;

public final class RenderSpider extends RenderLiving {
    public RenderSpider() {
        super(new ModelSpider(), 1.0F);
    }

    protected final float getDeathMaxRotation(EntityLiving var1) {
        return 180.0F;
    }
}
