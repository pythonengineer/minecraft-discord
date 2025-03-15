package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.QuadrupedModel;

public class QuadrupedMob extends Mob {
	private static QuadrupedModel QUADRUPED_MODEL = new QuadrupedModel();

	public QuadrupedMob(Level level, float x, float y, float z) {
		super(level);
		this.setSize(1.4F, 1.2F);
		this.setPos(x, y, z);
		this.model = QUADRUPED_MODEL;
	}
}