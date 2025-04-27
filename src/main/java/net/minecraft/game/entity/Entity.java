package net.minecraft.game.entity;

import java.util.ArrayList;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public abstract class Entity {
	protected World worldObj;
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
    public boolean isCollidedHorizontally = false;
    private boolean surfaceCollision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
    public float width = 0.6F;
    public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
    protected boolean canTriggerWalking = true;
    protected float fallDistance = 0.0F;
    private int nextStepDistance = 1;
	public float lastTickPosX;
	public float lastTickPosY;
	public float lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
    private boolean noClip = false;
    private float entityCollisionReduction = 0.0F;
    protected EaglercraftRandom rand = new EaglercraftRandom();
    public int ticksExisted = 0;
    public int fireResistance = 1;
    public int fire = 0;

	public Entity(World var1) {
		this.worldObj = var1;
		this.setPosition(0.0F, 0.0F, 0.0F);
	}

	protected void preparePlayerToSpawn() {
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

    protected final void setSize(float var1, float var2) {
        this.width = var1;
        this.height = var2;
	}

    protected final void setPosition(float var1, float var2, float var3) {
		this.posX = var1;
		this.posY = var2;
		this.posZ = var3;
        float var4 = this.width / 2.0F;
        float var5 = this.height / 2.0F;
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
        var5 = new AxisAlignedBB(var5.minX + var4, var5.minY + var3, var5.minZ + var4, var5.maxX + var2, var5.maxY + var3, var5.maxZ + var4);
        ArrayList var6 = this.worldObj.getCollidingBoundingBoxes(var5);
        return var6.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(var5);
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
            var2 = ((AxisAlignedBB)var10.get(var11)).calculateYOffset(this.boundingBox, var2);
        }

        this.boundingBox.offset(0.0F, var2, 0.0F);
        if(!this.surfaceCollision && var7 != var2) {
            var3 = 0.0F;
            var2 = var3;
            var1 = var3;
        }

        boolean var16 = this.onGround || var7 != var2 && var7 < 0.0F;

        int var12;
        for(var12 = 0; var12 < var10.size(); ++var12) {
            var1 = ((AxisAlignedBB)var10.get(var12)).calculateXOffset(this.boundingBox, var1);
        }

        this.boundingBox.offset(var1, 0.0F, 0.0F);
        if(!this.surfaceCollision && var6 != var1) {
            var3 = 0.0F;
            var2 = var3;
            var1 = var3;
        }

        for(var12 = 0; var12 < var10.size(); ++var12) {
            var3 = ((AxisAlignedBB)var10.get(var12)).calculateZOffset(this.boundingBox, var3);
        }

        this.boundingBox.offset(0.0F, 0.0F, var3);
        if(!this.surfaceCollision && var8 != var3) {
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
                var2 = ((AxisAlignedBB)var10.get(var15)).calculateYOffset(this.boundingBox, var2);
            }

            this.boundingBox.offset(0.0F, var2, 0.0F);
            if(!this.surfaceCollision && var7 != var2) {
                var3 = 0.0F;
                var2 = var3;
                var1 = var3;
            }

            for(var15 = 0; var15 < var10.size(); ++var15) {
                var1 = ((AxisAlignedBB)var10.get(var15)).calculateXOffset(this.boundingBox, var1);
            }

            this.boundingBox.offset(var1, 0.0F, 0.0F);
            if(!this.surfaceCollision && var6 != var1) {
                var3 = 0.0F;
                var2 = var3;
                var1 = var3;
            }

            for(var15 = 0; var15 < var10.size(); ++var15) {
                var3 = ((AxisAlignedBB)var10.get(var15)).calculateZOffset(this.boundingBox, var3);
            }

            this.boundingBox.offset(0.0F, 0.0F, var3);
            if(!this.surfaceCollision && var8 != var3) {
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

        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
        this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
        this.isCollidedHorizontally = var6 != var1 || var8 != var3;
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

        var18 = this.posX - var4;
        var17 = this.posZ - var5;
        this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_float(var18 * var18 + var17 * var17) * 0.6D);
        if(this.canTriggerWalking) {
            int var19 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
            if(this.distanceWalkedModified > (float)this.nextStepDistance && var19 > 0) {
                ++this.nextStepDistance;
                StepSound var21 = Block.blocksList[var19].stepSound;
                this.worldObj.playSoundAtEntity(this, "step." + var21.soundDir, var21.soundVolume * 0.15F, var21.soundPitch);
            }
        }

        this.ySize *= 0.4F;
        boolean var20 = this.handleWaterMovement();
        if(this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
            this.attackEntityFrom((Entity)null, 1);
            if(!var20) {
                ++this.fire;
                if(this.fire == 0) {
                    this.fire = 300;
                }
            }
        } else if(this.fire <= 0) {
            this.fire = -this.fireResistance;
        }

        if(var20 && this.fire > 0) {
            this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            this.fire = -this.fireResistance;
        }

    }

	protected void fall(float var1) {
	}

    public final boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.water);
    }

    public final boolean isInsideOfMaterial() {
        int var1 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY + 0.12F), (int)this.posZ);
        return var1 != 0 ? Block.blocksList[var1].getBlockMaterial().equals(Material.water) : false;
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

    public final void setWorld(World var1) {
        this.worldObj = var1;
    }

    public final void setPositionAndRotation(float var1, float var2, float var3, float var4, float var5) {
        this.prevPosX = this.posX = var1;
        this.prevPosY = this.posY = var2;
        this.prevPosZ = this.posZ = var3;
        this.rotationYaw = var4;
        this.rotationPitch = 0.0F;
        this.setPosition(var1, var2, var3);
    }

    public final void applyEntityCollision(Entity var1) {
        float var2 = var1.posX - this.posX;
        float var3 = var1.posZ - this.posZ;
        float var4 = var2 * var2 + var3 * var3;
        if(var4 >= 0.01F) {
            var4 = MathHelper.sqrt_float(var4);
            var2 /= var4;
            var3 /= var4;
            var2 /= var4;
            var3 /= var4;
            var2 *= 0.05F;
            var3 *= 0.05F;
            this.addVelocity(-var2, 0.0F, -var3);
            var1.addVelocity(var2, 0.0F, var3);
        }

	}

    private void addVelocity(float var1, float var2, float var3) {
        this.motionX += var1;
        this.motionY = this.motionY;
        this.motionZ += var3;
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
