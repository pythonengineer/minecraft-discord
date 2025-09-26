package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.EntityLiving;

public final class RenderPlayer extends RenderLiving {
    private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;

    public RenderPlayer() {
        super(new ModelBiped(), 0.5F);
        new ModelBiped(1.0F);
        new ModelBiped(0.5F);
    }

    public final void drawFirstPersonHand() {
        this.modelBipedMain.bipedRightArm.render(1.0F);
    }

    protected final boolean shouldRenderPass(EntityLiving var1, int var2) {
        return false;
    }
}
