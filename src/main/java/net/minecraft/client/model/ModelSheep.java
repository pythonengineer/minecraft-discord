package net.minecraft.client.model;

public final class ModelSheep extends ModelQuadruped {
    public ModelSheep() {
        super(12, 0.0F);
        this.bipedHead = new ModelRenderer(0, 0);
        this.bipedHead.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.bipedHead.setRotationPoint(0.0F, 6.0F, -8.0F);
        this.bipedBody = new ModelRenderer(28, 8);
        this.bipedBody.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
        this.bipedBody.setRotationPoint(0.0F, 5.0F, 2.0F);
    }
}
