package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public class EntityLiving extends Entity {
    public int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
    private int scoreValue = 0;
	public int health = 20;
	public int prevHealth;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	private int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	protected AI entityAI = null;
	public int unused0 = -1;
	public float unused1 = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public int af = this.rand.nextInt(2);

	public EntityLiving(World var1) {
		super(var1);
        this.preventEntitySpawning = true;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
        this.rotationYaw = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
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
        float var2;
        float var3;
        float var4;
        if(this.isInsideOfMaterial()) {
            --this.air;
            if(this.air == -20) {
                this.air = 0;

                for(int var1 = 0; var1 < 8; ++var1) {
                    var2 = this.rand.nextFloat() - this.rand.nextFloat();
                    var3 = this.rand.nextFloat() - this.rand.nextFloat();
                    var4 = this.rand.nextFloat() - this.rand.nextFloat();
                    this.worldObj.spawnParticle("bubble", this.posX + var2, this.posY + var3, this.posZ + var4, this.motionX, this.motionY, this.motionZ);
                }

                this.attackEntityFrom((Entity)null, 2);
			}

            this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

        this.prevCameraPitch = this.cameraPitch;
        if(this.attackTime > 0) {
            --this.attackTime;
        }

        if(this.hurtTime > 0) {
            --this.hurtTime;
        }

        if(this.heartsLife > 0) {
            --this.heartsLife;
        }

        if(this.health <= 0) {
            ++this.deathTime;
            if(this.deathTime > 20) {
                this.setEntityDead();
            }
        }

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		++this.ticksExisted;
		this.onLivingUpdate();
		float var1 = this.posX - this.prevPosX;
		var2 = this.posZ - this.prevPosZ;
		var3 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		var4 = this.renderYawOffset;
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

	protected void onLivingUpdate() {
		if(this.entityAI != null) {
			this.entityAI.onLivingUpdate(this.worldObj, this);
		}

	}

	public final void attackEntityFrom(Entity var1, int var2) {
		if(this.worldObj.survivalWorld) {
			if(this.health > 0) {
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(this.prevHealth - var2 >= this.health) {
						return;
					}

					this.health = this.prevHealth - var2;
				} else {
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.health -= var2;
					this.hurtTime = this.maxHurtTime = 10;
				}

                this.worldObj.playSoundAtEntity(this, "random.hurt", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.attackedAtYaw = 0.0F;
				if(var1 != null) {
					float var6 = var1.posX - this.posX;
					float var3 = var1.posZ - this.posZ;
					this.attackedAtYaw = (float)(Math.atan2((double)var3, (double)var6) * 180.0D / (double)((float)Math.PI)) - this.rotationYaw;
					float var5 = MathHelper.sqrt_float(var6 * var6 + var3 * var3);
					this.motionX /= 2.0F;
					this.motionY /= 2.0F;
					this.motionZ /= 2.0F;
					this.motionX -= var6 / var5 * 0.4F;
					this.motionY += 0.4F;
					this.motionZ -= var3 / var5 * 0.4F;
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
		int var3 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var3 > 0) {
			this.attackEntityFrom((Entity)null, var3);
            var3 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
            if(var3 > 0) {
                StepSound var4 = Block.blocksList[var3].stepSound;
                this.worldObj.playSoundAtEntity(this, "step." + var4.soundDir, var4.soundVolume * 0.5F, var4.soundPitch * (12.0F / 16.0F));
            }
		}

	}

    public final void setEntityAI(AI var1) {
        this.entityAI = var1;
    }

    protected void writeEntityToNBT(NBTTagCompound var1) {
        var1.setShort("Health", (short)this.health);
        var1.setShort("HurtTime", (short)this.hurtTime);
        var1.setShort("DeathTime", (short)this.deathTime);
        var1.setShort("AttackTime", (short)this.attackTime);
    }

    protected void readEntityFromNBT(NBTTagCompound var1) {
        this.health = var1.getShort("Health");
        this.hurtTime = var1.getShort("HurtTime");
        this.deathTime = var1.getShort("DeathTime");
        this.attackTime = var1.getShort("AttackTime");
    }

    protected String getEntityString() {
        return "Mob";
    }
}
