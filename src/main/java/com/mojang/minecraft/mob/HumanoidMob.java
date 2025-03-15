package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {
	public static final long serialVersionUID = 77479605454997290L;
	private static HumanoidModel HUMANOID_MODEL = new HumanoidModel(0.0F);
	private static HumanoidModel ARMOR_MODEL = new HumanoidModel(1.0F);
	protected HumanoidModel humanoidModel = HUMANOID_MODEL;
	private boolean helmet = Math.random() < (double)0.2F;
	private boolean armor = Math.random() < (double)0.2F;

	public HumanoidMob(Level level, float x, float y, float z) {
		super(level);
		this.setPos(x, y, z);
	}

	public void renderModel(Textures texture, float x, float y, float z, float rotX, float rotY, float rotZ) {
		this.model.render(x, z, (float)this.tickCount + y, rotX, rotY, rotZ);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if(this.allowAlpha) {
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		HumanoidModel x1;
		if(this.hasHair) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			x1 = this.humanoidModel;
			this.humanoidModel.hair.yRot = x1.head.yRot;
			x1.hair.xRot = x1.head.xRot;
			x1.hair.render(rotZ);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.armor || this.helmet) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.loadTexture("/armor/plate.png"));
			GL11.glDisable(GL11.GL_CULL_FACE);
			ARMOR_MODEL.head.showModel = this.helmet;
			ARMOR_MODEL.body.showModel = this.armor;
			ARMOR_MODEL.rightArm.showModel = this.armor;
			ARMOR_MODEL.leftArm.showModel = this.armor;
			ARMOR_MODEL.rightLeg.showModel = false;
			ARMOR_MODEL.leftLeg.showModel = false;
			HumanoidModel y1 = this.humanoidModel;
			x1 = ARMOR_MODEL;
			ARMOR_MODEL.head.yRot = y1.head.yRot;
			x1.head.xRot = y1.head.xRot;
			x1.rightArm.xRot = y1.rightArm.xRot;
			x1.rightArm.zRot = y1.rightArm.zRot;
			x1.leftArm.xRot = y1.leftArm.xRot;
			x1.leftArm.zRot = y1.leftArm.zRot;
			x1.rightLeg.xRot = y1.rightLeg.xRot;
			x1.leftLeg.xRot = y1.leftLeg.xRot;
			x1.head.render(rotZ);
			x1.body.render(rotZ);
			x1.rightArm.render(rotZ);
			x1.leftArm.render(rotZ);
			x1.rightLeg.render(rotZ);
			x1.leftLeg.render(rotZ);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
}