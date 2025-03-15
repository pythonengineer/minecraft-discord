package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.SkeletonModel;

public class Skeleton extends Zombie {
	private static HumanoidModel skeletonModel = new SkeletonModel();

	public Skeleton(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.model = this.humanoidModel = skeletonModel;
		this.textureName = "/mob/skeleton.png";
	}
}