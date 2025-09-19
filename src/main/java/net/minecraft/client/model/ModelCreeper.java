package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public final class ModelCreeper extends ModelBase {
    private ModelRenderer creeperHead = new ModelRenderer(0, 0);
    private ModelRenderer creeperHeadWear;
    private ModelRenderer creeperBody;
    private ModelRenderer creeperLeg1;
    private ModelRenderer creeperLeg2;
    private ModelRenderer creeperLeg3;
    private ModelRenderer creeperLeg4;

    public ModelCreeper() {
        this.creeperHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.creeperHead.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.creeperHeadWear = new ModelRenderer(32, 0);
        this.creeperHeadWear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.creeperHeadWear.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.creeperBody = new ModelRenderer(16, 16);
        this.creeperBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.creeperBody.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.creeperLeg1 = new ModelRenderer(0, 16);
        this.creeperLeg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.creeperLeg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
        this.creeperLeg2 = new ModelRenderer(0, 16);
        this.creeperLeg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.creeperLeg2.setRotationPoint(2.0F, 16.0F, 4.0F);
        this.creeperLeg3 = new ModelRenderer(0, 16);
        this.creeperLeg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.creeperLeg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
        this.creeperLeg4 = new ModelRenderer(0, 16);
        this.creeperLeg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.creeperLeg4.setRotationPoint(2.0F, 16.0F, -4.0F);
    }

    public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
        this.setRotationAngles(var1, var2, 0.0F, var4, var5, 1.0F);
        this.creeperHead.render(1.0F);
        this.creeperBody.render(1.0F);
        this.creeperLeg1.render(1.0F);
        this.creeperLeg2.render(1.0F);
        this.creeperLeg3.render(1.0F);
        this.creeperLeg4.render(1.0F);
    }

    public final void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
        this.creeperHead.rotateAngleY = var4 / (180.0F / (float)Math.PI);
        this.creeperHead.rotateAngleX = var5 / (180.0F / (float)Math.PI);
        this.creeperLeg1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
        this.creeperLeg2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
        this.creeperLeg3.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float)Math.PI) * 1.4F * var2;
        this.creeperLeg4.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
    }
}
