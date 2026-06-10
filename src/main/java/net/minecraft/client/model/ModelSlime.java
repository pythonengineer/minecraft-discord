package net.minecraft.client.model;

public class ModelSlime extends ModelBase {
    ModelRenderer slimeBodies = new ModelRenderer(0, 0);

    public ModelSlime() {
        this.slimeBodies.addBox(-4.0F, 16.0F, -4.0F, 8, 8, 8);
    }

    public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
    }

    public void render(float f1, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f1, f2, f3, f4, f5, f6);
        this.slimeBodies.render(f6);
    }
}
