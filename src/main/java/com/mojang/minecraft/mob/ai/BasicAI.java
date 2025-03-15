package com.mojang.minecraft.mob.ai;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;

import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.util.List;

public class BasicAI extends AI {
	public EaglercraftRandom random = new EaglercraftRandom();
	public float xxa;
	public float yya;
	private float yRotA;
	public Level level;
	public Mob mob;
	public boolean jumping = false;
	private int attackDelay = 0;

	public final void tick(Level level, Mob mob) {
		this.level = level;
		this.mob = mob;
		if(this.attackDelay > 0) {
			--this.attackDelay;
		}

		if(mob.health <= 0) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.yya = 0.0F;
			this.yRotA = 0.0F;
		} else {
			this.tick();
		}

		boolean z3 = mob.isInWater();
		boolean z4 = mob.isInLava();
		if(this.jumping) {
			if(z3) {
				mob.yd += 0.04F;
			} else if(z4) {
				mob.yd += 0.04F;
			} else if(mob.onGround) {
				mob.yd = 0.42F;
			}
		}

		this.xxa *= 0.98F;
		this.yya *= 0.98F;
		this.yRotA *= 0.9F;
		mob.travel(this.xxa, this.yya);
		List list5;
		if((list5 = level.findEntities(mob, mob.bb.grow(0.2F, 0.0F, 0.2F))) != null && list5.size() > 0) {
			for(int i6 = 0; i6 < list5.size(); ++i6) {
				Entity entity7;
				if((entity7 = (Entity)list5.get(i6)).isPushable()) {
					entity7.push(mob);
				}
			}
		}

	}

	protected void tick() {
		if(this.random.nextFloat() < 0.07F) {
			this.xxa = this.random.nextFloat() - 0.5F;
			this.yya = this.random.nextFloat();
		}

		if(this.random.nextFloat() < 0.04F) {
			this.yRotA = (this.random.nextFloat() - 0.5F) * 60.0F;
		}

		this.mob.yRot += this.yRotA;
		this.mob.xRot = (float)this.defaultLookAngle;
		this.jumping = this.random.nextFloat() < 0.01F;
		boolean z1 = this.mob.isInWater();
		boolean z2 = this.mob.isInLava();
		if(z1 || z2) {
			this.jumping = this.random.nextFloat() < 0.8F;
		}

	}

	protected final void attack(Entity entity) {
		if(entity != null) {
			float f2 = entity.x - this.mob.x;
			float f3 = entity.z - this.mob.z;
			float f4;
			if((f4 = (float)Math.sqrt((double)(f2 * f2 + f3 * f3))) < 8.0F) {
				float f5 = entity.y - this.mob.y;
				this.mob.yRot = (float)(Math.atan2((double)f3, (double)f2) * 180.0D / Math.PI) - 90.0F;
				this.mob.xRot = -((float)(Math.atan2((double)f5, (double)f4) * 180.0D / Math.PI));
				if((float)Math.sqrt((double)(f2 * f2 + f5 * f5 + f3 * f3)) < 2.0F && this.attackDelay == 0) {
					this.hurt(entity);
				}
			}

		}
	}

	public void hurt(Entity entity) {
		if(this.level.clip(new Vec3(this.mob.x, this.mob.y, this.mob.z), new Vec3(entity.x, entity.y, entity.z)) == null) {
			this.mob.attackTime = 5;
			this.attackDelay = this.random.nextInt(20) + 10;
			entity.hurt(this.mob, this.random.nextInt(4) + this.random.nextInt(4) + 1);
		}
	}

	public void beforeRemove() {
	}
}