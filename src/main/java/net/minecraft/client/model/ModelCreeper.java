package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public final class ModelCreeper extends ModelBase {
    private ModelRenderer head = new ModelRenderer(0, 0);
    private ModelRenderer headwear;
    private ModelRenderer body;
    private ModelRenderer leg1;
    private ModelRenderer leg2;
    private ModelRenderer leg3;
    private ModelRenderer leg4;

    public ModelCreeper() {
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.headwear = new ModelRenderer(32, 0);
        this.headwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.headwear.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.body = new ModelRenderer(16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.body.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.leg1 = new ModelRenderer(0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
        this.leg2 = new ModelRenderer(0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
        this.leg3 = new ModelRenderer(0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
        this.leg4 = new ModelRenderer(0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
    }

    public final void render(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F);
        this.head.render(0.0625F);
        this.body.render(0.0625F);
        this.leg1.render(0.0625F);
        this.leg2.render(0.0625F);
        this.leg3.render(0.0625F);
        this.leg4.render(0.0625F);
    }

    public final void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.head.rotateAngleY = netHeadYaw / 57.295776F;
        this.head.rotateAngleX = headPitch / 57.295776F;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}