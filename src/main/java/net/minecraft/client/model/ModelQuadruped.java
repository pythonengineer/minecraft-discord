package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public class ModelQuadruped extends ModelBase {
    public ModelRenderer bipedHead = new ModelRenderer(0, 0);
    public ModelRenderer bipedBody;
    private ModelRenderer bipedRightLegFront;
    private ModelRenderer bipedLeftLegFront;
    private ModelRenderer bipedRightLegBack;
    private ModelRenderer bipedLeftLegBcak;

    public ModelQuadruped(int var1, float var2) {
        this.bipedHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        this.bipedHead.setRotationPoint(0.0F, (float)(18 - var1), -6.0F);
        this.bipedBody = new ModelRenderer(28, 8);
        this.bipedBody.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, 0.0F);
        this.bipedBody.setRotationPoint(0.0F, (float)(17 - var1), 2.0F);
        this.bipedRightLegFront = new ModelRenderer(0, 16);
        this.bipedRightLegFront.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, 0.0F);
        this.bipedRightLegFront.setRotationPoint(-3.0F, (float)(24 - var1), 7.0F);
        this.bipedLeftLegFront = new ModelRenderer(0, 16);
        this.bipedLeftLegFront.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, 0.0F);
        this.bipedLeftLegFront.setRotationPoint(3.0F, (float)(24 - var1), 7.0F);
        this.bipedRightLegBack = new ModelRenderer(0, 16);
        this.bipedRightLegBack.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, 0.0F);
        this.bipedRightLegBack.setRotationPoint(-3.0F, (float)(24 - var1), -5.0F);
        this.bipedLeftLegBcak = new ModelRenderer(0, 16);
        this.bipedLeftLegBcak.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, 0.0F);
        this.bipedLeftLegBcak.setRotationPoint(3.0F, (float)(24 - var1), -5.0F);
    }

    public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
        this.setRotationAngles(var1, var2, 0.0F, var4, var5, 1.0F);
        this.bipedHead.render(1.0F);
        this.bipedBody.render(1.0F);
        this.bipedRightLegFront.render(1.0F);
        this.bipedLeftLegFront.render(1.0F);
        this.bipedRightLegBack.render(1.0F);
        this.bipedLeftLegBcak.render(1.0F);
    }

    public final void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
        this.bipedHead.rotateAngleY = var4 / (180.0F / (float)Math.PI);
        this.bipedHead.rotateAngleX = var5 / (180.0F / (float)Math.PI);
        this.bipedBody.rotateAngleX = (float)Math.PI * 0.5F;
        this.bipedRightLegFront.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
        this.bipedLeftLegFront.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
        this.bipedRightLegBack.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
        this.bipedLeftLegBcak.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
    }
}
