package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.BasicAttackAI;

public class Zombie extends HumanoidMob {
	public static final long serialVersionUID = 0L;

	public Zombie(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.modelName = "zombie";
		this.textureName = "/mob/zombie.png";
		this.heightOffset = 1.62F;
		BasicAttackAI basicAttackAI5;
		(basicAttackAI5 = new BasicAttackAI()).defaultLookAngle = 30;
		basicAttackAI5.runSpeed = 1.0F;
		this.ai = basicAttackAI5;
	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 100);
		}

		super.die(entity1);
	}
}
