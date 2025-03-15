package com.mojang.minecraft.model;

import com.mojang.minecraft.character.Cube;

public class HumanoidModel extends BaseModel {
	public Cube head;
	public Cube hair;
	public Cube body;
	public Cube rightArm;
	public Cube leftArm;
	public Cube rightLeg;
	public Cube leftLeg;

	public HumanoidModel() {
		this(0.0F);
	}

	public HumanoidModel(float translation) {
		this.head = new Cube(0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, translation);
		this.hair = new Cube(32, 0);
		this.hair.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, translation + 0.5F);
		this.body = new Cube(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, translation);
		this.rightArm = new Cube(40, 16);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, translation);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.leftArm = new Cube(40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, translation);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.rightLeg = new Cube(0, 16);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, translation);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.leftLeg = new Cube(0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, translation);
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
	}

	public final void render(float x, float y, float z, float xRot, float yRot, float zRot) {
		this.setupAnim(x, y, z, xRot, yRot, zRot);
		this.head.render(zRot);
		this.body.render(zRot);
		this.rightArm.render(zRot);
		this.leftArm.render(zRot);
		this.rightLeg.render(zRot);
		this.leftLeg.render(zRot);
	}

	public void setupAnim(float x, float y, float xRot, float yRot, float zRot, float translation) {
		this.head.yRot = yRot / 57.29578F;
		this.head.xRot = zRot / 57.29578F;
		this.rightArm.xRot = (float)Math.cos((double)x * 0.6662D + Math.PI) * 2.0F * y;
		this.rightArm.zRot = (float)(Math.cos((double)x * 0.2312D) + 1.0D) * y;
		this.leftArm.xRot = (float)Math.cos((double)x * 0.6662D) * 2.0F * y;
		this.leftArm.zRot = (float)(Math.cos((double)x * 0.2812D) - 1.0D) * y;
		this.rightLeg.xRot = (float)Math.cos((double)x * 0.6662D) * 1.4F * y;
		this.leftLeg.xRot = (float)Math.cos((double)x * 0.6662D + Math.PI) * 1.4F * y;
		this.rightArm.zRot += (float)Math.cos((double)xRot * 0.09D) * 0.05F + 0.05F;
		this.leftArm.zRot -= (float)Math.cos((double)xRot * 0.09D) * 0.05F + 0.05F;
		this.rightArm.xRot += (float)Math.sin((double)xRot * 0.067D) * 0.05F;
		this.leftArm.xRot -= (float)Math.sin((double)xRot * 0.067D) * 0.05F;
	}
}