package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public final class ModelBiped extends ModelBase {
    private ModelRenderer bipedHead;
    private ModelRenderer bipedHeadWear;
    private ModelRenderer bipedBody;
    private ModelRenderer bipedRightArm;
    private ModelRenderer bipedLeftArm;
    private ModelRenderer bipedRightLeg;
    private ModelRenderer bipedLeftLeg;

    public ModelBiped() {
        this(0.0F);
    }

    public ModelBiped(float var1) {
        this.bipedHead = new ModelRenderer(0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedHeadWear = new ModelRenderer(32, 0);
        this.bipedHeadWear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.bipedBody = new ModelRenderer(16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.bipedRightArm = new ModelRenderer(40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
    }

    public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
        var6 = var5;
        var5 = var4;
        var4 = 0.0F;
        this.bipedHead.rotateAngleY = var5 / (180.0F / (float)Math.PI);
        this.bipedHead.rotateAngleX = var6 / (180.0F / (float)Math.PI);
        this.bipedRightArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 2.0F * var2;
        this.bipedRightArm.rotateAngleZ = (MathHelper.cos(var1 * 0.2312F) + 1.0F) * var2;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 2.0F * var2;
        this.bipedLeftArm.rotateAngleZ = (MathHelper.cos(var1 * 0.2812F) - 1.0F) * var2;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(var4 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(var4 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(var4 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(var4 * 0.067F) * 0.05F;
        this.bipedHead.render(1.0F);
        this.bipedBody.render(1.0F);
        this.bipedRightArm.render(1.0F);
        this.bipedLeftArm.render(1.0F);
        this.bipedRightLeg.render(1.0F);
        this.bipedLeftLeg.render(1.0F);
    }
}
