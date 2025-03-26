package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {
	public static final long serialVersionUID = 0L;
	private boolean helmet = Math.random() < 0.20000000298023224D;
	private boolean armor = Math.random() < 0.20000000298023224D;

	public HumanoidMob(Level level, float x, float y, float z) {
		super(level);
		this.setPos(x, y, z);
	}

	public void renderModel(Textures textures, float x, float y, float z, float xr, float yr, float zr) {
		BaseModel baseModel8;
		(baseModel8 = modelCache.getModel(this.modelName)).render(x, z, (float)this.tickCount + y, xr, yr, zr);
		GL11.glEnable(3008);
		if(this.allowAlpha) {
			GL11.glEnable(2884);
		}

		if(this.hasHair) {
			GL11.glDisable(2884);
			HumanoidModel x1;
			(x1 = (HumanoidModel)baseModel8).hair.yRot = x1.head.yRot;
			x1.hair.xRot = x1.head.xRot;
			x1.hair.render(zr);
			GL11.glEnable(2884);
		}

		if(this.armor || this.helmet) {
			GL11.glBindTexture(3553, textures.loadTexture("/armor/plate.png"));
			GL11.glDisable(2884);
			HumanoidModel textures1;
			(textures1 = (HumanoidModel)modelCache.getModel("humanoid.armor")).head.showModel = this.helmet;
			textures1.body.showModel = this.armor;
			textures1.rightArm.showModel = this.armor;
			textures1.leftArm.showModel = this.armor;
			textures1.rightLeg.showModel = false;
			textures1.leftLeg.showModel = false;
			HumanoidModel y1 = (HumanoidModel)baseModel8;
			textures1.head.yRot = y1.head.yRot;
			textures1.head.xRot = y1.head.xRot;
			textures1.rightArm.xRot = y1.rightArm.xRot;
			textures1.rightArm.zRot = y1.rightArm.zRot;
			textures1.leftArm.xRot = y1.leftArm.xRot;
			textures1.leftArm.zRot = y1.leftArm.zRot;
			textures1.rightLeg.xRot = y1.rightLeg.xRot;
			textures1.leftLeg.xRot = y1.leftLeg.xRot;
			textures1.head.render(zr);
			textures1.body.render(zr);
			textures1.rightArm.render(zr);
			textures1.leftArm.render(zr);
			textures1.rightLeg.render(zr);
			textures1.leftLeg.render(zr);
			GL11.glEnable(2884);
		}

		GL11.glDisable(3008);
	}
}
