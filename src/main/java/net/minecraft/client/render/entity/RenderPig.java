package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityPig;

public class RenderPig extends RenderLiving {
    public RenderPig(ModelBase modelBase1, ModelBase modelBase2, float f3) {
        super(modelBase1, f3);
        this.setRenderPassModel(modelBase2);
    }

    protected boolean shouldRenderPass(EntityPig entity, int i2) {
        this.loadTexture("/mob/saddle.png");
        return i2 == 0 && entity.saddled;
    }

    protected boolean shouldRenderPass(EntityLiving eVar, int i) {
        return this.shouldRenderPass((EntityPig)eVar, i);
    }
}
