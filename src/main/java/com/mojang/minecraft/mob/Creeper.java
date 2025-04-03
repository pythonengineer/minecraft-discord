package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.particle.TerrainParticle;

public class Creeper extends Mob {
	public static final long serialVersionUID = 0L;

	public Creeper(Level level, float x, float y, float z) {
		super(level);
		this.heightOffset = 1.62F;
		this.modelName = "creeper";
		this.textureName = "/mob/creeper.png";
		this.ai = new BasicAttackAI() {
			public final boolean attack(Entity entity1) {
				if(!super.attack(entity1)) {
					return false;
				} else {
					this.mob.hurt(entity1, 6);
					return true;
				}
			}

			public final void beforeRemove() {
				float f1 = 4.0F;
				this.level.explode(this.mob, this.mob.x, this.mob.y, this.mob.z, f1);

				for(int i2 = 0; i2 < 500; ++i2) {
					float f3 = (float)this.random.nextGaussian() * f1 / 4.0F;
					float f4 = (float)this.random.nextGaussian() * f1 / 4.0F;
					float f5 = (float)this.random.nextGaussian() * f1 / 4.0F;
					float f6 = (float)Math.sqrt((double)(f3 * f3 + f4 * f4 + f5 * f5));
					float f7 = f3 / f6 / f6;
					float f8 = f4 / f6 / f6;
					f6 = f5 / f6 / f6;
                    this.level.particleEngine.addParticle(new TerrainParticle(this.level, this.mob.x + f3, this.mob.y + f4, this.mob.z + f5, f7, f8, f6, Tile.leaf));
				}

			}
		};
		this.ai.defaultLookAngle = 45;
        this.deathScore = 200;
		this.setPos(x, y, z);
	}

	public float getBrightness(float f1) {
		float f2 = (float)(20 - this.health) / 20.0F;
		return ((float)(Math.sin((double)((float)this.tickCount + f1)) * 0.5D + 0.5D) * f2 * 0.5F + 0.25F + f2 * 0.25F) * super.getBrightness(f1);
	}
}
