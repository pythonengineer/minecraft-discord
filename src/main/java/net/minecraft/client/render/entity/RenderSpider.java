package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelSpider;

public final class RenderSpider extends RenderLiving {
    public RenderSpider() {
        super(new ModelSpider(), 1.0F);
    }

    protected final float getDeathMaxRotation() {
        return 180.0F;
    }
}
