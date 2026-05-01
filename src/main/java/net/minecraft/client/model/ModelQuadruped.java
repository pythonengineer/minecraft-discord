package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public class ModelQuadruped extends ModelBase {
    public ModelRenderer head = new ModelRenderer(0, 0);
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;

    public ModelQuadruped(int height, float scale) {
        this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, scale);
        this.head.setRotationPoint(0.0F, (float)(18 - height), -6.0F);
        this.body = new ModelRenderer(28, 8);
        this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, scale);
        this.body.setRotationPoint(0.0F, (float)(17 - height), 2.0F);
        this.leg1 = new ModelRenderer(0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        this.leg1.setRotationPoint(-3.0F, (float)(24 - height), 7.0F);
        this.leg2 = new ModelRenderer(0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        this.leg2.setRotationPoint(3.0F, (float)(24 - height), 7.0F);
        this.leg3 = new ModelRenderer(0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        this.leg3.setRotationPoint(-3.0F, (float)(24 - height), -5.0F);
        this.leg4 = new ModelRenderer(0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        this.leg4.setRotationPoint(3.0F, (float)(24 - height), -5.0F);
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
        this.body.rotateAngleX = (float)Math.PI / 2F;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}