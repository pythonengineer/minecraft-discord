package net.minecraft.game.entity;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public abstract class Entity {
	public World worldObj;
	public float prevPosX;
	public float prevPosY;
	public float prevPosZ;
	public float posX;
	public float posY;
	public float posZ;
	public float motionX;
	public float motionY;
	public float motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	private boolean collision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	private float bbWidth = 0.6F;
	public float bbHeight = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	public boolean makeStepSound = true;
	protected float fallDistance = 0.0F;
	private int nextStep = 1;
	public float lastTickPosX;
	public float lastTickPosY;
	public float lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
    public EaglercraftRandom rand = new EaglercraftRandom();
    public int ticksExisted = 0;

	public Entity(World var1) {
		this.worldObj = var1;
		this.setPosition(0.0F, 0.0F, 0.0F);
	}

	public void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			float var1 = (float)this.worldObj.xSpawn + 0.5F;
			float var2 = (float)this.worldObj.ySpawn;

			for(float var3 = (float)this.worldObj.zSpawn + 0.5F; var2 > 0.0F; ++var2) {
				this.setPosition(var1, var2, var3);
				if(this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0) {
					break;
				}
			}

			this.motionX = this.motionY = this.motionZ = 0.0F;
			this.rotationYaw = this.worldObj.rotSpawn;
			this.rotationPitch = 0.0F;
		}
	}

    public void setEntityDead() {
		this.isDead = true;
	}

	public final void setSize(float var1, float var2) {
		this.bbWidth = var1;
		this.bbHeight = var2;
	}

	public final void setPosition(float var1, float var2, float var3) {
		this.posX = var1;
		this.posY = var2;
		this.posZ = var3;
		float var4 = this.bbWidth / 2.0F;
		float var5 = this.bbHeight / 2.0F;
		this.boundingBox = new AxisAlignedBB(var1 - var4, var2 - var5, var3 - var4, var1 + var4, var2 + var5, var3 + var4);
	}

	public void onEntityUpdate() {
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
	}

	public final boolean isOffsetPositionInLiquid(float var1, float var2, float var3) {
		float var4 = var3;
		var3 = var2;
		var2 = var1;
		AxisAlignedBB var5 = this.boundingBox;
		var5 = new AxisAlignedBB(var5.x0 + var4, var5.y0 + var3, var5.z0 + var4, var5.x1 + var2, var5.y1 + var3, var5.z1 + var4);
		return this.worldObj.getCollidingBoundingBoxes(var5).size() > 0 ? false : !this.worldObj.getIsAnyLiquid(var5);
	}

	public final void moveEntity(float var1, float var2, float var3) {
		float var4 = this.posX;
		float var5 = this.posZ;
		float var6 = var1;
		float var7 = var2;
		float var8 = var3;
		AxisAlignedBB var9 = this.boundingBox.copy();
		ArrayList var10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var1, var2, var3));

		for(int var11 = 0; var11 < var10.size(); ++var11) {
			var2 = ((AxisAlignedBB)var10.get(var11)).clipYCollide(this.boundingBox, var2);
		}

		this.boundingBox.offset(0.0F, var2, 0.0F);
		if(!this.collision && var7 != var2) {
			var3 = 0.0F;
			var2 = var3;
			var1 = var3;
		}

		boolean var16 = this.onGround || var7 != var2 && var7 < 0.0F;

		int var12;
		for(var12 = 0; var12 < var10.size(); ++var12) {
			var1 = ((AxisAlignedBB)var10.get(var12)).clipXCollide(this.boundingBox, var1);
		}

		this.boundingBox.offset(var1, 0.0F, 0.0F);
		if(!this.collision && var6 != var1) {
			var3 = 0.0F;
			var2 = var3;
			var1 = var3;
		}

		for(var12 = 0; var12 < var10.size(); ++var12) {
			var3 = ((AxisAlignedBB)var10.get(var12)).clipZCollide(this.boundingBox, var3);
		}

		this.boundingBox.offset(0.0F, 0.0F, var3);
		if(!this.collision && var8 != var3) {
			var3 = 0.0F;
			var2 = var3;
			var1 = var3;
		}

		float var17;
		float var18;
		if(this.stepHeight > 0.0F && var16 && this.ySize < 0.05F && (var6 != var1 || var8 != var3)) {
			var18 = var1;
			var17 = var2;
			float var13 = var3;
			var1 = var6;
			var2 = this.stepHeight;
			var3 = var8;
			AxisAlignedBB var14 = this.boundingBox.copy();
			this.boundingBox = var9.copy();
			var10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var6, var2, var8));

			int var15;
			for(var15 = 0; var15 < var10.size(); ++var15) {
				var2 = ((AxisAlignedBB)var10.get(var15)).clipYCollide(this.boundingBox, var2);
			}

			this.boundingBox.offset(0.0F, var2, 0.0F);
			if(!this.collision && var7 != var2) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			for(var15 = 0; var15 < var10.size(); ++var15) {
				var1 = ((AxisAlignedBB)var10.get(var15)).clipXCollide(this.boundingBox, var1);
			}

			this.boundingBox.offset(var1, 0.0F, 0.0F);
			if(!this.collision && var6 != var1) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			for(var15 = 0; var15 < var10.size(); ++var15) {
				var3 = ((AxisAlignedBB)var10.get(var15)).clipZCollide(this.boundingBox, var3);
			}

			this.boundingBox.offset(0.0F, 0.0F, var3);
			if(!this.collision && var8 != var3) {
				var3 = 0.0F;
				var2 = var3;
				var1 = var3;
			}

			if(var18 * var18 + var13 * var13 >= var1 * var1 + var3 * var3) {
				var1 = var18;
				var2 = var17;
				var3 = var13;
				this.boundingBox = var14.copy();
			} else {
				this.ySize = (float)((double)this.ySize + 0.5D);
			}
		}

		this.horizontalCollision = var6 != var1 || var8 != var3;
		this.onGround = var7 != var2 && var7 < 0.0F;
		if(this.onGround) {
			if(this.fallDistance > 0.0F) {
				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else if(var2 < 0.0F) {
			this.fallDistance -= var2;
		}

		if(var6 != var1) {
			this.motionX = 0.0F;
		}

		if(var7 != var2) {
			this.motionY = 0.0F;
		}

		if(var8 != var3) {
			this.motionZ = 0.0F;
		}

		this.posX = (this.boundingBox.x0 + this.boundingBox.x1) / 2.0F;
		this.posY = this.boundingBox.y0 + this.yOffset - this.ySize;
		this.posZ = (this.boundingBox.z0 + this.boundingBox.z1) / 2.0F;
		var18 = this.posX - var4;
		var17 = this.posZ - var5;
		this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_float(var18 * var18 + var17 * var17) * 0.6D);
		if(this.makeStepSound) {
			int var19 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
			if(this.distanceWalkedModified > (float)this.nextStep && var19 > 0) {
				++this.nextStep;
			}
		}

		this.ySize *= 0.4F;
	}

	protected void fall(float var1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.water);
	}

	public final boolean isInsideOfMaterial() {
		int var1 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY + 0.12F), (int)this.posZ);
		return var1 != 0 ? Block.blocksList[var1].getMaterial().equals(Material.water) : false;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.lava);
	}

	public final void moveFlying(float var1, float var2, float var3) {
		float var4 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		if(var4 >= 0.01F) {
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			var3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			var4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += var1 * var4 - var2 * var3;
			this.motionZ += var2 * var4 + var1 * var3;
		}
	}

	public final float getBrightness() {
		int var1 = (int)this.posX;
		int var2 = (int)(this.posY + this.yOffset / 2.0F);
		int var3 = (int)this.posZ;
		return this.worldObj.getBlockLightValue(var1, var2, var3);
	}

	void addVelocity(float var1, float var2) {
		this.motionX += var1;
		this.motionY = this.motionY;
		this.motionZ += var2;
	}

	public void attackEntityFrom(Entity var1, int var2) {
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}
}
