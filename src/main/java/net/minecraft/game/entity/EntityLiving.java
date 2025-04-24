package net.minecraft.game.entity;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.level.World;

public class EntityLiving extends Entity {
    private int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
    private int maxAir = 300;
	public int health;
	public int prevHealth;
	public int scoreValue = 0;
	public int air = 300;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	private int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	public AI entityAI = null;

	public EntityLiving(World var1) {
		super(var1);
		Math.random();
		this.health = 20;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
		Math.random();
		this.stepHeight = 0.5F;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final boolean canBePushed() {
		return !this.isDead;
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.scoreValue > 0) {
			--this.scoreValue;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
                this.setEntityDead();
			}
		}

		if(this.isInsideOfMaterial()) {
			if(this.air > 0) {
				--this.air;
			} else {
				this.attackEntityFrom((Entity)null, 2);
			}
		} else {
			this.air = this.maxAir;
		}

		if(this.handleWaterMovement()) {
			this.fallDistance = 0.0F;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
		}

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		++this.ticksExisted;
		this.onLivingUpdate();
		float var1 = this.posX - this.prevPosX;
		float var2 = this.posZ - this.prevPosZ;
		float var3 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		float var4 = this.renderYawOffset;
		float var5 = 0.0F;
		float var6 = 0.0F;
		if(var3 > 0.05F) {
			var6 = 1.0F;
			var5 = var3 * 3.0F;
			var4 = (float)Math.atan2((double)var2, (double)var1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			var6 = 0.0F;
		}

		this.rotationYawHead += (var6 - this.rotationYawHead) * 0.3F;

		for(var1 = var4 - this.renderYawOffset; var1 < -180.0F; var1 += 360.0F) {
		}

		while(var1 >= 180.0F) {
			var1 -= 360.0F;
		}

		this.renderYawOffset += var1 * 0.1F;

		for(var1 = this.rotationYaw - this.renderYawOffset; var1 < -180.0F; var1 += 360.0F) {
		}

		while(var1 >= 180.0F) {
			var1 -= 360.0F;
		}

		boolean var7 = var1 < -90.0F || var1 >= 90.0F;
		if(var1 < -75.0F) {
			var1 = -75.0F;
		}

		if(var1 >= 75.0F) {
			var1 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - var1;
		this.renderYawOffset += var1 * 0.1F;
		if(var7) {
			var5 = -var5;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.prevRotationYawHead += var5;
	}

	public void onLivingUpdate() {
		if(this.entityAI != null) {
			this.entityAI.onLivingUpdate(this.worldObj, this);
		}

	}

	public final void attackEntityFrom(Entity var1, int var2) {
		if(this.worldObj.survivalWorld) {
			if(this.health > 0) {
				if((float)this.scoreValue > (float)this.heartsHalvesLife / 2.0F) {
					if(this.prevHealth - var2 >= this.health) {
						return;
					}

					this.health = this.prevHealth - var2;
				} else {
					this.prevHealth = this.health;
					this.scoreValue = this.heartsHalvesLife;
					this.health -= var2;
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				if(var1 != null) {
					float var7 = var1.posX - this.posX;
					float var3 = var1.posZ - this.posZ;
					this.attackedAtYaw = (float)(Math.atan2((double)var3, (double)var7) * 180.0D / (double)((float)Math.PI)) - this.rotationYaw;
					float var5 = MathHelper.sqrt_float(var7 * var7 + var3 * var3);
					float var6 = 0.4F;
					this.motionX /= 2.0F;
					this.motionY /= 2.0F;
					this.motionZ /= 2.0F;
					this.motionX -= var7 / var5 * var6;
					this.motionY += 0.4F;
					this.motionZ -= var3 / var5 * var6;
					if(this.motionY > 0.4F) {
						this.motionY = 0.4F;
					}
				} else {
					this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
				}

				if(this.health <= 0) {
					this.onDeath(var1);
				}

			}
		}
	}

	public void onDeath(Entity var1) {
	}

	protected final void fall(float var1) {
		int var2 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var2 > 0) {
			this.attackEntityFrom((Entity)null, var2);
		}

	}
}
