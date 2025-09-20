package net.minecraft.game.entity.monster;

import net.minecraft.game.level.World;

public class EntityZombie extends EntityMob {
	public EntityZombie(World var1) {
		super(var1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
	}

	protected final String getEntityString() {
		return "Zombie";
	}
}
