package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;
import net.minecraft.game.world.material.Material;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float unusedRotationPitch2;
	public float unusedFloat;
	public float unusedRotationPitch;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	protected float ridingRotUnused;
	protected float prevRidingRotUnused;
	protected float rotationUnused;
	protected float prevRotationUnused;
	protected boolean unusedBool1 = true;
	protected String texture = "/char.png";
	protected boolean unusedBool2 = true;
	protected float unusedRotation = 0.0F;
	protected String entityType = null;
	protected float unusedFloat1 = 1.0F;
	protected int scoreValue = 0;
	protected float unusedFloat2 = 0.0F;
	public int health = 10;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	protected boolean dead = false;
	public int unusedInt = -1;
	public float unusedFloat4 = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float prevLimbYaw;
	public float limbYaw;
	public float limbSwing;
	protected int entityAge = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;
    private Entity currentTarget;
    private int numTicksToChaseTarget = 0;

	public EntityLiving(World world1) {
		super(world1);
		this.preventEntitySpawning = true;
		this.unusedRotationPitch = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.unusedRotationPitch2 = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.unusedFloat = 1.0F;
		this.stepHeight = 0.5F;
	}

	public String getTexture() {
		return this.texture;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}

	protected float getEyeHeight() {
		return this.height * 0.85F;
	}

    public void onEntityUpdate() {
        super.onEntityUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -80;
			String string1;
			if((string1 = this.getLivingSound()) != null) {
				this.worldObj.playSoundAtEntity(this, string1, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

        if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
            this.attackEntityFrom((Entity)null, 1);
        }

        int i9;
		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(i9 = 0; i9 < 8; ++i9) {
					float f2 = this.rand.nextFloat() - this.rand.nextFloat();
					float f3 = this.rand.nextFloat() - this.rand.nextFloat();
					float f4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)f2, this.posY + (double)f3, this.posZ + (double)f4, this.motionX, this.motionY, this.motionZ);
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
				this.onEntityDeath();
                this.setEntityDead();

                for(i9 = 0; i9 < 20; ++i9) {
                    double d10 = this.rand.nextGaussian() * 0.02D;
                    double d15 = this.rand.nextGaussian() * 0.02D;
                    double d6 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d10, d15, d6);
                }
			}
		}

		this.prevRotationUnused = this.rotationUnused;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
    }

    public void spawnExplosionParticle() {
        for(int i1 = 0; i1 < 20; ++i1) {
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d4 = this.rand.nextGaussian() * 0.02D;
            double d6 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d2 * 10.0D, this.posY + (double)(this.rand.nextFloat() * this.height) - d4 * 10.0D, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d6 * 10.0D, d2, d4, d6);
        }

    }

    public void updateRidden() {
        super.updateRidden();
        this.ridingRotUnused = this.prevRidingRotUnused;
        this.prevRidingRotUnused = 0.0F;
    }

    public void onUpdate() {
        super.onUpdate();
		this.onLivingUpdate();
        double d11 = this.posX - this.prevPosX;
        double d14 = this.posZ - this.prevPosZ;
        float f5 = MathHelper.sqrt_double(d11 * d11 + d14 * d14);
        float f16 = this.renderYawOffset;
        float f7 = 0.0F;
        this.ridingRotUnused = this.prevRidingRotUnused;
        float f8 = 0.0F;
        if(f5 > 0.05F) {
            f8 = 1.0F;
            f7 = f5 * 3.0F;
            f16 = (float)Math.atan2(d14, d11) * 180.0F / (float)Math.PI - 90.0F;
        }

        if(!this.onGround) {
            f8 = 0.0F;
        }

        this.prevRidingRotUnused += (f8 - this.prevRidingRotUnused) * 0.3F;

        float f13;
        for(f13 = f16 - this.renderYawOffset; f13 < -180.0F; f13 += 360.0F) {
        }

        while(f13 >= 180.0F) {
            f13 -= 360.0F;
        }

        this.renderYawOffset += f13 * 0.3F;

        for(f13 = this.rotationYaw - this.renderYawOffset; f13 < -180.0F; f13 += 360.0F) {
        }

        while(f13 >= 180.0F) {
            f13 -= 360.0F;
        }

        boolean z12 = f13 < -90.0F || f13 >= 90.0F;
        if(f13 < -75.0F) {
            f13 = -75.0F;
        }

        if(f13 >= 75.0F) {
            f13 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - f13;
        if(f13 * f13 > 2500.0F) {
            this.renderYawOffset += f13 * 0.2F;
        }

		if(z12) {
			f7 = -f7;
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

		this.rotationUnused += f7;
	}

	protected void setSize(float width, float height) {
		super.setSize(width, height);
	}

	public void heal(int healAmount) {
		if(this.health > 0) {
			this.health += healAmount;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else {
			this.limbYaw = 1.5F;
			if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
				if(this.prevHealth - damage >= this.health) {
					return false;
				}

				this.health = this.prevHealth - damage;
			} else {
				this.prevHealth = this.health;
				this.heartsLife = this.heartsHalvesLife;
				this.health -= damage;
				this.hurtTime = this.maxHurtTime = 10;
			}

			this.attackedAtYaw = 0.0F;
			if(entity != null) {
				double d3 = entity.posX - this.posX;

                double d5;
                for(d5 = entity.posZ - this.posZ; d3 * d3 + d5 * d5 < 1.0E-4D; d5 = (Math.random() - Math.random()) * 0.01D) {
                    d3 = (Math.random() - Math.random()) * 0.01D;
                }

				this.attackedAtYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
				this.knockBack(entity, damage, d3, d5);
			} else {
				this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
			}

			if(this.health <= 0) {
				this.worldObj.playSoundAtEntity(this, this.getDeathSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				this.onDeath(entity);
			} else {
				this.worldObj.playSoundAtEntity(this, this.getHurtSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			return true;
		}
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void knockBack(Entity entity, int i2, double d3, double d5) {
		float f7 = MathHelper.sqrt_double(d3 * d3 + d5 * d5);
		float f8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= d3 / (double)f7 * (double)f8;
		this.motionY += (double)0.4F;
		this.motionZ -= d5 / (double)f7 * (double)f8;
		if(this.motionY > (double)0.4F) {
			this.motionY = (double)0.4F;
		}

	}

	public void onDeath(Entity entity) {
		if(this.scoreValue > 0 && entity != null) {
			entity.addToPlayerScore(this, this.scoreValue);
		}

		this.dead = true;
		int i2 = this.getDropItemId();
		if(i2 > 0) {
			int i3 = this.rand.nextInt(3);

			for(int i4 = 0; i4 < i3; ++i4) {
				this.dropItem(i2, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float fallDistance) {
		int fallDistance1;
		if((fallDistance1 = (int)Math.ceil((double)(fallDistance - 3.0F))) > 0) {
			this.attackEntityFrom((Entity)null, fallDistance1);
			if((fallDistance1 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset), MathHelper.floor_double(this.posZ))) > 0) {
				StepSound fallDistance2 = Block.blocksList[fallDistance1].stepSound;
				this.worldObj.playSoundAtEntity(this, fallDistance2.getStepSound(), fallDistance2.getVolume() * 0.5F, fallDistance2.getPitch() * 0.75F);
			}
		}

	}

	public void moveEntityWithHeading(float moveX, float moveZ) {
		double d3;
		if(this.handleWaterMovement()) {
			d3 = this.posY;
			this.moveFlying(moveX, moveZ, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			d3 = this.posY;
			this.moveFlying(moveX, moveZ, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
            float f8 = 0.91F;
            if(this.onGround) {
                f8 = 0.54600006F;
                int i4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
                if(i4 > 0) {
                    f8 = Block.blocksList[i4].slipperiness * 0.91F;
                }
            }

            float f9 = 0.16277136F / (f8 * f8 * f8);
            this.moveFlying(moveX, moveZ, this.onGround ? 0.1F * f9 : 0.02F);
            f8 = 0.91F;
            if(this.onGround) {
                f8 = 0.54600006F;
                int i5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
                if(i5 > 0) {
                    f8 = Block.blocksList[i5].slipperiness * 0.91F;
                }
            }

            if(this.isOnLadder()) {
                this.fallDistance = 0.0F;
                if(this.motionY < -0.15D) {
                    this.motionY = -0.15D;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if(this.isCollidedHorizontally && this.isOnLadder()) {
                this.motionY = 0.2D;
            }

            this.motionY -= 0.08D;
            this.motionY *= (double)0.98F;
            this.motionX *= (double)f8;
            this.motionZ *= (double)f8;
		}

		this.prevLimbYaw = this.limbYaw;
		d3 = this.posX - this.prevPosX;
		double d5 = this.posZ - this.prevPosZ;
		float f7 = MathHelper.sqrt_double(d3 * d3 + d5 * d5) * 4.0F;
		if(f7 > 1.0F) {
			f7 = 1.0F;
		}

		this.limbYaw += (f7 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
	}

	public boolean isOnLadder() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i1, i2, i3) == Block.ladder.blockID || this.worldObj.getBlockId(i1, i2 + 1, i3) == Block.ladder.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		compoundTag.setShort("Health", (short)this.health);
		compoundTag.setShort("HurtTime", (short)this.hurtTime);
		compoundTag.setShort("DeathTime", (short)this.deathTime);
		compoundTag.setShort("AttackTime", (short)this.attackTime);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		this.health = compoundTag.getShort("Health");
		if(!compoundTag.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = compoundTag.getShort("HurtTime");
		this.deathTime = compoundTag.getShort("DeathTime");
		this.attackTime = compoundTag.getShort("AttackTime");
	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		++this.entityAge;
        EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
        if(entityPlayer1 != null) {
			double d2 = entityPlayer1.posX - this.posX;
			double d4 = entityPlayer1.posY - this.posY;
			double d6 = entityPlayer1.posZ - this.posZ;
			double d8;
			if((d8 = d2 * d2 + d4 * d4 + d6 * d6) > 16384.0D) {
                this.setEntityDead();
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
				if(d8 < 1024.0D) {
					this.entityAge = 0;
				} else {
	                this.setEntityDead();
				}
			}
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updateEntityActionState();
		}

		boolean z17 = this.handleWaterMovement();
		boolean z3 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z17) {
				this.motionY += (double)0.04F;
			} else if(z3) {
				this.motionY += (double)0.04F;
			} else if(this.onGround) {
				this.jump();
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		List list11 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(list11 != null && list11.size() > 0) {
			for(int i5 = 0; i5 < list11.size(); ++i5) {
				Entity entity12 = (Entity)list11.get(i5);
				if(entity12.canBePushed()) {
					entity12.applyEntityCollision(this);
				}
			}
		}

	}

	protected void jump() {
		this.motionY = (double)0.42F;
	}

	protected void updateEntityActionState() {
        this.moveStrafing = 0.0F;
        this.moveForward = 0.0F;
        float f1 = 8.0F;
        if(this.rand.nextFloat() < 0.02F) {
            EntityPlayer entityPlayer2 = this.worldObj.getClosestPlayerToEntity(this, (double)f1);
            if(entityPlayer2 != null) {
                this.currentTarget = entityPlayer2;
                this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
            } else {
                this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if(this.currentTarget != null) {
            this.faceEntity(this.currentTarget, 10.0F);
            if(this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(f1 * f1)) {
                this.currentTarget = null;
            }
        } else {
            if(this.rand.nextFloat() < 0.05F) {
                this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
            }

            this.rotationYaw += this.randomYawVelocity;
            this.rotationPitch = this.defaultPitch;
        }

        boolean z4 = this.handleWaterMovement();
        boolean z3 = this.handleLavaMovement();
        if(z4 || z3) {
            this.isJumping = this.rand.nextFloat() < 0.8F;
        }

	}

    public void faceEntity(Entity entity, float f2) {
        double d3 = entity.posX - this.posX;
        double d7 = entity.posZ - this.posZ;
        double d5;
        if(entity instanceof EntityLiving) {
            EntityLiving entityLiving9 = (EntityLiving)entity;
            d5 = entityLiving9.posY + (double)entityLiving9.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
        } else {
            d5 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
        }

        double d13 = (double)MathHelper.sqrt_double(d3 * d3 + d7 * d7);
        float f11 = (float)(Math.atan2(d7, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
        float f12 = (float)(Math.atan2(d5, d13) * 180.0D / (double)(float)Math.PI);
        this.rotationPitch = this.updateRotation(this.rotationPitch, f12, f2);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f11, f2);
    }

    private float updateRotation(float f1, float f2, float f3) {
        float f4;
        for(f4 = f2 - f1; f4 < -180.0F; f4 += 360.0F) {
        }

        while(f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        if(f4 > f3) {
            f4 = f3;
        }

        if(f4 < -f3) {
            f4 = -f3;
        }

        return f1 + f4;
    }

	public void onEntityDeath() {
	}

	public boolean getCanSpawnHere(double x, double y, double z) {
		this.setPosition(x, y + (double)(this.height / 2.0F), z);
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

    protected void kill() {
        this.attackEntityFrom((Entity)null, 4);
    }
}
