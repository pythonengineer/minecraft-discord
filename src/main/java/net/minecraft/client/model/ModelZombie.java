package net.minecraft.client.model;

import net.lax1dude.eaglercraft.util.MathHelper;

public class ModelZombie extends ModelBiped {
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        limbSwing = MathHelper.sin(this.swingProgress * (float)Math.PI);
        limbSwingAmount = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - limbSwing * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - limbSwing * 0.6F;
        this.bipedRightArm.rotateAngleX = -1.5707964F;
        this.bipedLeftArm.rotateAngleX = -1.5707964F;
        this.bipedRightArm.rotateAngleX -= limbSwing * 1.2F - limbSwingAmount * 0.4F;
        this.bipedLeftArm.rotateAngleX -= limbSwing * 1.2F - limbSwingAmount * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }
}
