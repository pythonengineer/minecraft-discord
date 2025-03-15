package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ZombieModel;

public class Zombie extends HumanoidMob {
	private static HumanoidModel ZOMBIE_MODEL = new ZombieModel();

	public Zombie(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.model = this.humanoidModel = ZOMBIE_MODEL;
		this.textureName = "/mob/zombie.png";
		this.heightOffset = 1.62F;
		this.ai = new BasicAttackAI();
		this.ai.defaultLookAngle = 30;
	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 100);
		}

		super.die(entity1);
	}
}