package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.QuadrupedModel;

public class Pig extends QuadrupedMob {
	public static final long serialVersionUID = 77479605454997290L;
	private static QuadrupedModel PIG_MODEL = new QuadrupedModel();

	public Pig(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.heightOffset = 1.72F;
		this.model = PIG_MODEL;
		this.textureName = "/mob/pig.png";
	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 10);
		}

		super.die(entity1);
	}
}