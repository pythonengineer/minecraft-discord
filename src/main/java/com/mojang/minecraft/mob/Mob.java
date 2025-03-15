package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.AI;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class Mob extends Entity {
	public static final int ATTACK_DURATION = 5;
	public static final int TOTAL_AIR_SUPPLY = 300;
	public int invulnerableDuration = 30;
	public float rot;
	public float timeOffs;
	public float speed;
	public float rotA = (float)(Math.random() + 1.0D) * 0.01F;
	protected float yBodyRot = 0.0F;
	protected float yBodyRotO = 0.0F;
	protected float oRun;
	protected float run;
	protected float animStep;
	protected float animStepO;
	protected int tickCount = 0;
	public boolean hasHair = true;
	protected String textureName = "/char.png";
	public boolean allowAlpha = true;
	public BaseModel model = null;
	public float rotOffs = 0.0F;
	public int health = 20;
	public int lastHealth;
	public int invulnerableTime = 0;
	public int airSupply = 300;
	public int hurtTime;
	public int hurtDuration;
	public float hurtDir = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float oTilt;
	public float tilt;
	protected boolean dead = false;
	public AI ai;

	public Mob(Level level1) {
		super(level1);
		this.setPos(this.x, this.y, this.z);
		this.timeOffs = (float)Math.random() * 12398.0F;
		this.rot = (float)(Math.random() * Math.PI * 2.0D);
		this.speed = 1.0F;
		this.ai = new BasicAI();
	}

	public boolean isPickable() {
		return !this.removed;
	}

	public boolean isPushable() {
		return !this.removed;
	}

	public final void tick() {
		super.tick();
		this.oTilt = this.tilt;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.invulnerableTime > 0) {
			--this.invulnerableTime;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				if(this.ai != null) {
					this.ai.beforeRemove();
				}

				this.remove();
			}
		}

		if(this.isUnderWater()) {
			if(this.airSupply > 0) {
				--this.airSupply;
			} else {
				this.hurt((Entity)null, 2);
			}
		} else {
			this.airSupply = 300;
		}

		if(this.isInWater()) {
			this.fallDistance = 0.0F;
		}

		if(this.isInLava()) {
			this.hurt((Entity)null, 10);
		}

		this.animStepO = this.animStep;
		this.yBodyRotO = this.yBodyRot;
		this.yRotO = this.yRot;
		this.xRotO = this.xRot;
		++this.tickCount;
		this.aiStep();
		float f1 = this.x - this.xo;
		float f2 = this.z - this.zo;
		float f3 = (float)Math.sqrt((double)(f1 * f1 + f2 * f2));
		float f4 = this.yBodyRot;
		float f5 = 0.0F;
		this.oRun = this.run;
		float f6 = 0.0F;
		if(f3 > 0.05F) {
			f6 = 1.0F;
			f5 = f3 * 3.0F;
			f4 = (float)Math.atan2((double)f2, (double)f1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			f6 = 0.0F;
		}

		this.run += (f6 - this.run) * 0.3F;

		for(f1 = f4 - this.yBodyRot; f1 < -180.0F; f1 += 360.0F) {
		}

		while(f1 >= 180.0F) {
			f1 -= 360.0F;
		}

		this.yBodyRot += f1 * 0.1F;

		for(f1 = this.yRot - this.yBodyRot; f1 < -180.0F; f1 += 360.0F) {
		}

		while(f1 >= 180.0F) {
			f1 -= 360.0F;
		}

		boolean z7 = f1 < -90.0F || f1 >= 90.0F;
		if(f1 < -75.0F) {
			f1 = -75.0F;
		}

		if(f1 >= 75.0F) {
			f1 = 75.0F;
		}

		this.yBodyRot = this.yRot - f1;
		this.yBodyRot += f1 * 0.1F;
		if(z7) {
			f5 = -f5;
		}

		while(this.yRot - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while(this.yRot - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO < -180.0F) {
			this.yBodyRotO -= 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
			this.yBodyRotO += 360.0F;
		}

		while(this.xRot - this.xRotO < -180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.xRot - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		this.animStep += f5;
	}

	public void aiStep() {
		if(this.ai != null) {
			this.ai.tick(this.level, this);
		}

	}

	protected void bindTexture(Textures texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.loadTexture(this.textureName));
	}

	public void render(Textures texture, float translation) {
		if(this.model != null) {
			float f3;
			if((f3 = (float)this.attackTime - translation) < 0.0F) {
				f3 = 0.0F;
			}

			while(this.yBodyRotO - this.yBodyRot < -180.0F) {
				this.yBodyRotO += 360.0F;
			}

			while(this.yBodyRotO - this.yBodyRot >= 180.0F) {
				this.yBodyRotO -= 360.0F;
			}

			float f4;
			for(f4 = this.yBodyRotO + (this.yBodyRot - this.yBodyRotO) * translation; this.xRotO - this.xRot < -180.0F; this.xRotO += 360.0F) {
			}

			while(this.xRotO - this.xRot >= 180.0F) {
				this.xRotO -= 360.0F;
			}

			while(this.yRotO - this.yRot < -180.0F) {
				this.yRotO += 360.0F;
			}

			while(this.yRotO - this.yRot >= 180.0F) {
				this.yRotO -= 360.0F;
			}

			float f5 = this.oRun + (this.run - this.oRun) * translation;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			float f6 = this.yRotO + (this.yRot - this.yRotO) * translation;
			float f7 = this.xRotO + (this.xRot - this.xRotO) * translation;
			f6 -= f4;
			GL11.glPushMatrix();
			float f8 = this.animStepO + (this.animStep - this.animStepO) * translation;
			float f9;
			GL11.glColor3f(f9 = this.getBrightness(translation), f9, f9);
			f9 = 0.0625F;
			float f10 = (float)(-Math.abs(Math.cos((double)f8 * 0.6662D)) * 5.0D * (double)f5 - 23.0D);
			GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation - 1.62F, this.zo + (this.z - this.zo) * translation);
			float f11;
			if((f11 = (float)this.hurtTime - translation) > 0.0F || this.health <= 0) {
				if(f11 < 0.0F) {
					f11 = 0.0F;
				} else {
					f11 = (float)Math.sin((double)((f11 /= (float)this.hurtDuration) * f11 * f11 * f11) * Math.PI) * 14.0F;
				}

				float f12;
				if(this.health <= 0) {
					f12 = ((float)this.deathTime + translation) / 20.0F;
					if((f11 += f12 * f12 * 800.0F) > 90.0F) {
						f11 = 90.0F;
					}
				}

				f12 = this.hurtDir;
				GL11.glRotatef(180.0F - f4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				GL11.glRotatef(-f12, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-f11, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(f12, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-(180.0F - f4 + this.rotOffs), 0.0F, 1.0F, 0.0F);
			}

			GL11.glScalef(1.0F, -1.0F, 1.0F);
			GL11.glTranslatef(0.0F, f10 * f9, 0.0F);
			GL11.glRotatef(180.0F - f4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
			if(!this.allowAlpha) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
			} else {
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glScalef(-1.0F, 1.0F, 1.0F);
			this.model.rot = f3 / 5.0F;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.bindTexture(texture);
			this.renderModel(texture, f8, translation, f5, f6, f7, f9);
			if(this.invulnerableTime > this.invulnerableDuration - 10) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				this.bindTexture(texture);
				this.renderModel(texture, f8, translation, f5, f6, f7, f9);
				GL11.glDisable(GL11.GL_BLEND);
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			if(this.allowAlpha) {
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}

	public void renderModel(Textures texture, float x, float y, float z, float rotX, float rotY, float rotZ) {
		this.model.render(x, z, (float)this.tickCount + y, rotX, rotY, rotZ);
	}

	public void heal(int amount) {
		if(this.health > 0) {
			this.health += amount;
			if(this.health > 20) {
				this.health = 20;
			}

			this.invulnerableTime = this.invulnerableDuration / 2;
		}
	}

	public void hurt(Entity entity1, int i2) {
		if(this.health > 0) {
			if((float)this.invulnerableTime > (float)this.invulnerableDuration / 2.0F) {
				if(this.lastHealth - i2 >= this.health) {
					return;
				}

				this.health = this.lastHealth - i2;
			} else {
				this.lastHealth = this.health;
				this.invulnerableTime = this.invulnerableDuration;
				this.health -= i2;
				this.hurtTime = this.hurtDuration = 10;
			}

			this.hurtDir = 0.0F;
			if(entity1 != null) {
				float f3 = entity1.x - this.x;
				float f4 = entity1.z - this.z;
				this.hurtDir = (float)(Math.atan2((double)f4, (double)f3) * 180.0D / Math.PI) - this.yRot;
				this.knockback(entity1, i2, f3, f4);
			} else {
				this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
			}

			if(this.health <= 0) {
				this.die(entity1);
			}

		}
	}

	public void knockback(Entity entity, int id, float x, float z) {
		float entity1 = (float)Math.sqrt((double)(x * x + z * z));
		float id1 = 0.4F;
		this.xd /= 2.0F;
		this.yd /= 2.0F;
		this.zd /= 2.0F;
		this.xd -= x / entity1 * id1;
		this.yd += 0.4F;
		this.zd -= z / entity1 * id1;
		if(this.yd > 0.4F) {
			this.yd = 0.4F;
		}

	}

	public void die(Entity entity) {
		this.dead = true;
	}

	protected void causeFallDamage(float f1) {
		int i2;
		if((i2 = (int)Math.ceil((double)(f1 - 3.0F))) > 0) {
			this.hurt((Entity)null, i2);
		}

	}

	public void travel(float xa, float za) {
		float f3;
		if(this.isInWater()) {
			f3 = this.y;
			this.moveRelative(xa, za, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.8F;
			this.yd *= 0.8F;
			this.zd *= 0.8F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f3, this.zd)) {
				this.yd = 0.3F;
			}

		} else if(this.isInLava()) {
			f3 = this.y;
			this.moveRelative(xa, za, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.5F;
			this.yd *= 0.5F;
			this.zd *= 0.5F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f3, this.zd)) {
				this.yd = 0.3F;
			}

		} else {
			this.moveRelative(xa, za, this.onGround ? 0.1F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.91F;
			this.yd *= 0.98F;
			this.zd *= 0.91F;
			this.yd = (float)((double)this.yd - 0.08D);
			if(this.onGround) {
				f3 = 0.6F;
				this.xd *= f3;
				this.zd *= f3;
			}

		}
	}

	public boolean isShootable() {
		return true;
	}
}