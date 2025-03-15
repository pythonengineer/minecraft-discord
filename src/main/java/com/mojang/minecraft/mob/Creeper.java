package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.model.CreeperModel;

public class Creeper extends Mob {
	private static BaseModel CREEPER_MODEL = new CreeperModel();

	public Creeper(Level level, float x, float y, float z) {
		super(level);
		this.heightOffset = 1.62F;
		this.model = CREEPER_MODEL;
		this.textureName = "/mob/creeper.png";
		this.ai = new CreeperAI(this);
		this.ai.defaultLookAngle = 45;
		this.setPos(x, y, z);
	}

	public float getBrightness(float f1) {
		float f2 = (float)(20 - this.health) / 20.0F;
		return ((float)(Math.sin((double)((float)this.tickCount + f1)) * 0.5D + 0.5D) * f2 * 0.5F + 0.25F + f2 * 0.25F) * super.getBrightness(f1);
	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 250);
		}

		super.die(entity1);
	}
}