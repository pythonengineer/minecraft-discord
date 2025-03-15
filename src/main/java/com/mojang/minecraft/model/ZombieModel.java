package com.mojang.minecraft.model;

public class ZombieModel extends HumanoidModel {
	public final void setupAnim(float x, float y, float xRot, float yRot, float zRot, float translation) {
		super.setupAnim(x, y, xRot, yRot, zRot, translation);
		x = (float)Math.sin((double)this.rot * Math.PI);
		y = (float)Math.sin((double)(1.0F - (1.0F - this.rot) * (1.0F - this.rot)) * Math.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - x * 0.6F);
		this.leftArm.yRot = 0.1F - x * 0.6F;
		this.rightArm.xRot = -1.5707964F;
		this.leftArm.xRot = -1.5707964F;
		this.rightArm.xRot -= x * 1.2F - y * 0.4F;
		this.leftArm.xRot -= x * 1.2F - y * 0.4F;
		this.rightArm.zRot += (float)Math.cos((double)xRot * 0.09D) * 0.05F + 0.05F;
		this.leftArm.zRot -= (float)Math.cos((double)xRot * 0.09D) * 0.05F + 0.05F;
		this.rightArm.xRot += (float)Math.sin((double)xRot * 0.067D) * 0.05F;
		this.leftArm.xRot -= (float)Math.sin((double)xRot * 0.067D) * 0.05F;
	}
}