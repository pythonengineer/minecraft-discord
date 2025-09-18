package net.minecraft.game.entity;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.level.World;

public class AILiving extends AI {
	private EaglercraftRandom rand = new EaglercraftRandom();
	protected float moveStrafing;
	protected float moveForward;
	private float randomYawVelocity;
    private EntityLiving entityLiving;
	protected boolean isJumping = false;
	private int fire = 0;
	private float moveSpeed = 0.7F;
	private int entityAge = 0;
	private Entity playerToAttack = null;

	public final void onLivingUpdate(World var1, EntityLiving var2) {
		++this.entityAge;
		float var4;
		float var5;
        if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
            Entity var3 = var1.getPlayerEntity();
			if(var3 != null) {
				var4 = var3.posX - var2.posX;
				var5 = var3.posY - var2.posY;
				float var7 = var3.posZ - var2.posZ;
				var4 = var4 * var4 + var5 * var5 + var7 * var7;
				if(var4 < 1024.0F) {
					this.entityAge = 0;
				} else {
					var2.setEntityDead();
				}
			}
		}

        this.entityLiving = var2;
		if(this.fire > 0) {
			--this.fire;
		}

		if(var2.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updatePlayerActionState();
		}

		boolean var8 = var2.handleWaterMovement();
		boolean var10 = var2.handleLavaMovement();
		if(this.isJumping) {
			if(var8) {
				var2.motionY += 0.04F;
			} else if(var10) {
				var2.motionY += 0.04F;
			} else if(var2.onGround) {
                this.entityLiving.motionY = 0.42F;
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		var5 = this.moveForward;
		var4 = this.moveStrafing;
		float var6;
		if(var2.handleWaterMovement()) {
			var6 = var2.posY;
			var2.moveFlying(var4, var5, 0.02F);
			var2.moveEntity(var2.motionX, var2.motionY, var2.motionZ);
			var2.motionX *= 0.8F;
			var2.motionY *= 0.8F;
			var2.motionZ *= 0.8F;
			var2.motionY = (float)((double)var2.motionY - 0.02D);
            if(var2.isCollidedHorizontally && var2.isOffsetPositionInLiquid(var2.motionX, var2.motionY + 0.6F - var2.posY + var6, var2.motionZ)) {
                var2.motionY = 0.3F;
            }
        } else if(var2.handleLavaMovement()) {
            var6 = var2.posY;
            var2.moveFlying(var4, var5, 0.02F);
            var2.moveEntity(var2.motionX, var2.motionY, var2.motionZ);
            var2.motionX *= 0.5F;
            var2.motionY *= 0.5F;
            var2.motionZ *= 0.5F;
            var2.motionY = (float)((double)var2.motionY - 0.02D);
            if(var2.isCollidedHorizontally && var2.isOffsetPositionInLiquid(var2.motionX, var2.motionY + 0.6F - var2.posY + var6, var2.motionZ)) {
                var2.motionY = 0.3F;
            }
		} else {
			var2.moveFlying(var4, var5, var2.onGround ? 0.1F : 0.02F);
			var2.moveEntity(var2.motionX, var2.motionY, var2.motionZ);
			var2.motionX *= 0.91F;
			var2.motionY *= 0.98F;
			var2.motionZ *= 0.91F;
			var2.motionY = (float)((double)var2.motionY - 0.08D);
			if(var2.onGround) {
				var2.motionX *= 0.6F;
				var2.motionZ *= 0.6F;
			}
		}

        var2.moveStrafing = var2.moveForward;
        var6 = var2.posX - var2.prevPosX;
        var4 = var2.posZ - var2.prevPosZ;
        var4 = MathHelper.sqrt_float(var6 * var6 + var4 * var4) * 4.0F;
        if(var4 > 1.0F) {
            var4 = 1.0F;
        }

        var2.moveForward += (var4 - var2.moveForward) * 0.4F;
        var2.randomYawVelocity += var2.moveForward;
        List var11 = var1.getEntitiesWithinAABBExcludingEntity(var2, var2.boundingBox.expand(0.2F, 0.0F, 0.2F));
        if(var11 != null && var11.size() > 0) {
            for(int var9 = 0; var9 < var11.size(); ++var9) {
                Entity var12 = (Entity)var11.get(var9);
                if(var12.canBePushed()) {
                    var12.applyEntityCollision(var2);
                }
            }
        }

	}

	protected void updatePlayerActionState() {
        if(this.rand.nextFloat() < 0.07F) {
            this.moveStrafing = (this.rand.nextFloat() - 0.5F) * this.moveSpeed;
            this.moveForward = this.rand.nextFloat() * this.moveSpeed;
        }

        this.isJumping = this.rand.nextFloat() < 0.01F;
        if(this.rand.nextFloat() < 0.04F) {
            this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 60.0F;
        }

        this.entityLiving.rotationYaw += this.randomYawVelocity;
        this.entityLiving.rotationPitch = 0.0F;
        boolean var1 = this.entityLiving.handleWaterMovement();
        boolean var2 = this.entityLiving.handleLavaMovement();
        if(var1 || var2) {
            this.isJumping = this.rand.nextFloat() < 0.8F;
        }

	}
}
