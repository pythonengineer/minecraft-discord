package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityPig;

public final class RenderPig extends RenderLiving {
    public RenderPig(ModelBase modelBase1, ModelBase modelBase2, float f3) {
        super(modelBase1, 0.7F);
        this.setRenderPassModel(modelBase2);
    }

    protected final boolean shouldRenderPass(EntityLiving entityLiving1, int i2) {
        EntityPig entityPig10001 = (EntityPig)entityLiving1;
        int i3 = i2;
        EntityPig entityPig4 = entityPig10001;
        this.loadTexture("/mob/saddle.png");
        return i3 == 0 && entityPig4.saddled;
    }
}