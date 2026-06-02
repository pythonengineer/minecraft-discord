package net.minecraft.client.model;

public abstract class ModelBase {
    public float swingProgress;
    public boolean isRiding = false;

    public void render(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    }
}
