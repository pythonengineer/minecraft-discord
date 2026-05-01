package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagDouble;
import com.mojang.nbt.NBTTagFloat;
import com.mojang.nbt.NBTTagList;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.block.StepSound;
import net.minecraft.game.world.material.Material;

public abstract class Entity {
	public boolean preventEntitySpawning = false;
    public Entity riddenByEntity;
    public Entity ridingEntity;
	protected World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionZ;
	public double motionY;
	public double motionX;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
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
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	public float entityCollisionReduction = 0.0F;
	protected EaglercraftRandom rand = new EaglercraftRandom();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	private boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean firstUpdate = true;
	public String skinUrl;
    public double S = 0.0D;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

	public Entity(World world) {
		this.worldObj = world;
		this.setPosition(0.0D, 0.0D, 0.0D);
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) {
					break;
				}

				++this.posY;
			}

			this.motionZ = this.motionY = this.motionX = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

    public void setEntityDead() {
        this.isDead = true;
    }

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected final void setPosition(double x, double y, double z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		float f7 = this.width / 2.0F;
		float f8 = this.height / 2.0F;
		double d10001 = x - (double)f7;
		double d10002 = y - (double)f8;
		double d10003 = z - (double)f7;
		double d10004 = x + (double)f7;
		double d10005 = y + (double)f8;
		double d20 = z + (double)f7;
		double d18 = d10005;
		double d16 = d10004;
		double d14 = d10003;
		double d12 = d10002;
		double d10 = d10001;
		AxisAlignedBB x1 = this.boundingBox;
		this.boundingBox.minX = d10;
		x1.minY = d12;
		x1.minZ = d14;
		x1.maxX = d16;
		x1.maxY = d18;
		x1.maxZ = d20;
	}

	public void onUpdate() {
        this.onEntityUpdate();
    }

    public void onEntityUpdate() {
        if(this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }

		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.firstUpdate) {
				float f1;
				if((f1 = MathHelper.sqrt_double(this.motionZ * this.motionZ * (double)0.2F + this.motionY * this.motionY + this.motionX * this.motionX * (double)0.2F) * 0.2F) > 1.0F) {
					f1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				f1 = (float)MathHelper.floor_double(this.boundingBox.minY);

				int i2;
				float f3;
				float f4;
				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)f3, (double)(f1 + 1.0F), this.posZ + (double)f4, this.motionZ, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionX);
				}

				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)f3, (double)(f1 + 1.0F), this.posZ + (double)f4, this.motionZ, this.motionY, this.motionX);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.fire > 0) {
			if(this.fire % 20 == 0) {
				this.attackEntityFrom((Entity)null, 1);
			}

			--this.fire;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
			this.fire = 600;
		}

        if(this.posY < -64.0D) {
            this.kill();
        }

		this.firstUpdate = false;
	}

    protected void kill() {
        this.setEntityDead();
    }

	public final boolean isOffsetPositionInLiquid(double x, double y, double z) {
		AxisAlignedBB x1 = this.boundingBox.getOffsetBoundingBox(x, y, z);
		return this.worldObj.getCollidingBoundingBoxes(this, x1).size() > 0 ? false : !this.worldObj.getIsAnyLiquid(x1);
	}

	public final void moveEntity(double x, double y, double z) {
		if(this.noClip) {
			this.boundingBox.offset(x, y, z);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			double d7 = this.posX;
			double d9 = this.posZ;
			double d11 = x;
			double d13 = y;
			double d15 = z;
			AxisAlignedBB axisAlignedBB17 = this.boundingBox.copy();
			List list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(x, y, z));

			int i19;
			for(i19 = 0; i19 < list18.size(); ++i19) {
				y = ((AxisAlignedBB)list18.get(i19)).calculateYOffset(this.boundingBox, y);
			}

			this.boundingBox.offset(0.0D, y, 0.0D);
			if(!this.surfaceCollision && d13 != y) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			boolean z28 = this.onGround || d13 != y && d13 < 0.0D;

			int i20;
			for(i20 = 0; i20 < list18.size(); ++i20) {
				x = ((AxisAlignedBB)list18.get(i20)).calculateXOffset(this.boundingBox, x);
			}

			this.boundingBox.offset(x, 0.0D, 0.0D);
			if(!this.surfaceCollision && d11 != x) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			for(i20 = 0; i20 < list18.size(); ++i20) {
				z = ((AxisAlignedBB)list18.get(i20)).calculateZOffset(this.boundingBox, z);
			}

			this.boundingBox.offset(0.0D, 0.0D, z);
			if(!this.surfaceCollision && d15 != z) {
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}

			double d22;
			int i27;
			double d30;
			if(this.stepHeight > 0.0F && z28 && this.ySize < 0.05F && (d11 != x || d15 != z)) {
				d30 = x;
				d22 = y;
				double d24 = z;
				x = d11;
				y = (double)this.stepHeight;
				z = d15;
				AxisAlignedBB axisAlignedBB29 = this.boundingBox.copy();
				this.boundingBox = axisAlignedBB17.copy();
				list18 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d11, y, d15));

				for(i27 = 0; i27 < list18.size(); ++i27) {
					y = ((AxisAlignedBB)list18.get(i27)).calculateYOffset(this.boundingBox, y);
				}

				this.boundingBox.offset(0.0D, y, 0.0D);
				if(!this.surfaceCollision && d13 != y) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					x = ((AxisAlignedBB)list18.get(i27)).calculateXOffset(this.boundingBox, x);
				}

				this.boundingBox.offset(x, 0.0D, 0.0D);
				if(!this.surfaceCollision && d11 != x) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					z = ((AxisAlignedBB)list18.get(i27)).calculateZOffset(this.boundingBox, z);
				}

				this.boundingBox.offset(0.0D, 0.0D, z);
				if(!this.surfaceCollision && d15 != z) {
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}

				if(d30 * d30 + d24 * d24 >= x * x + z * z) {
					x = d30;
					y = d22;
					z = d24;
					this.boundingBox = axisAlignedBB29.copy();
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = d11 != x || d15 != z;
			this.onGround = d13 != y && d13 < 0.0D;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.updateFallen(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(y < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}

			if(d11 != x) {
				this.motionZ = 0.0D;
			}

			if(d13 != y) {
				this.motionY = 0.0D;
			}

			if(d15 != z) {
				this.motionX = 0.0D;
			}

			d30 = this.posX - d7;
			d22 = this.posZ - d9;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d30 * d30 + d22 * d22) * 0.6D);
			if(this.canTriggerWalking) {
				int i31 = MathHelper.floor_double(this.posX);
				int i25 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				i19 = MathHelper.floor_double(this.posZ);
				i27 = this.worldObj.getBlockId(i31, i25, i19);
				if(this.distanceWalkedModified > (float)this.nextStepDistance && i27 > 0) {
					++this.nextStepDistance;
					StepSound stepSound26 = Block.blocksList[i27].stepSound;
					if(!Block.blocksList[i27].blockMaterial.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound26.getStepSound(), stepSound26.stepSoundVolume * 0.15F, stepSound26.stepSoundPitch);
					}

					Block.blocksList[i27].onEntityWalking(this.worldObj, i31, i25, i19, this);
				}
			}

			this.ySize *= 0.4F;
			boolean z32 = this.handleWaterMovement();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
				this.dealFireDamage(1);
				if(!z32) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(z32 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

    public AxisAlignedBB getBoundingBox() {
        return null;
    }

	protected void dealFireDamage(int fireDamage) {
		this.attackEntityFrom((Entity)null, 1);
	}

	protected void updateFallen(float fallDistance) {
	}

    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.water, this);
    }

    public final boolean isInsideOfMaterial(Material material) {
        double d2 = this.posY + (double)this.getEyeHeight();
        int i4 = MathHelper.floor_double(this.posX);
        int i5 = MathHelper.floor_float((float)MathHelper.floor_double(d2));
        int i6 = MathHelper.floor_double(this.posZ);
        int i7;
        if((i7 = this.worldObj.getBlockId(i4, i5, i6)) != 0 && Block.blocksList[i7].blockMaterial == material) {
            float material1 = BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(i4, i5, i6)) - 0.11111111F;
            material1 = (float)(i5 + 1) - material1;
            return d2 < (double)material1;
        } else {
            return false;
        }
    }

	protected float getEyeHeight() {
		return 0.0F;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.lava);
	}

	public final void moveFlying(float x, float y, float z) {
		float f4;
		if((f4 = MathHelper.sqrt_float(x * x + y * y)) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = z / f4;
			x *= f4;
			y *= f4;
			z = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			f4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionZ += (double)(x * f4 - y * z);
			this.motionX += (double)(y * f4 + x * z);
		}
	}

	public float getBrightness(float partialTicks) {
		int partialTicks1 = MathHelper.floor_double(this.posX);
        double d3 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
        int i2 = MathHelper.floor_double(this.posY - (double)this.yOffset + d3);
        int i6 = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBrightness(partialTicks1, i2, i6);
	}

    public final void setWorld(World world) {
        this.worldObj = world;
    }

	public final void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		this.prevPosX = this.posX = x;
		this.prevPosY = this.posY = y + (double)this.yOffset;
		this.prevPosZ = this.posZ = z;
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final double getDistanceToEntity(Entity entity) {
		double d2 = this.posX - entity.posX;
		double d4 = this.posY - entity.posY;
		double d6 = this.posZ - entity.posZ;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public void onCollideWithPlayer(EntityPlayer playerEntity) {
	}

    public void applyEntityCollision(Entity entity) {
        double d2 = entity.posX - this.posX;
        double d4 = entity.posZ - this.posZ;
        double d6;
        if((d6 = MathHelper.abs_max(d2, d4)) >= (double)0.01F) {
            d6 = (double)MathHelper.sqrt_double(d6);
            d2 /= d6;
            d4 /= d6;
            double d8;
            if((d8 = 1.0D / d6) > 1.0D) {
                d8 = 1.0D;
            }

            d2 *= d8;
            d4 *= d8;
            d2 *= (double)0.05F;
            d4 *= (double)0.05F;
            this.addVelocity(-d2, 0.0D, -d4);
            entity.addVelocity(d2, 0.0D, d4);
        }

    }

    public final void addVelocity(double motionX, double motionY, double motionZ) {
        this.motionZ += motionX;
        this.motionY = this.motionY;
        this.motionX += motionZ;
    }

	public boolean attackEntityFrom(Entity entity, int damage) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public String getEntityTexture() {
		return null;
	}

	public final boolean addEntityID(NBTTagCompound compoundTag) {
		String string2 = EntityList.getEntityString(this);
		if(!this.isDead && string2 != null) {
			compoundTag.setString("id", string2);
			this.writeToNBT(compoundTag);
			return true;
		} else {
			return false;
		}
	}

	public final void writeToNBT(NBTTagCompound compoundTag) {
		compoundTag.setTag("Pos", newDoubleNBTList(new double[]{this.posX, this.posY, this.posZ}));
		compoundTag.setTag("Motion", newDoubleNBTList(new double[]{this.motionZ, this.motionY, this.motionX}));
		float[] f2 = new float[]{this.rotationYaw, this.rotationPitch};
		NBTTagList nBTTagList3 = new NBTTagList();
		int i4 = (f2 = f2).length;

		for(int i5 = 0; i5 < i4; ++i5) {
			float f6 = f2[i5];
			nBTTagList3.setTag(new NBTTagFloat(f6));
		}

		compoundTag.setTag("Rotation", nBTTagList3);
		compoundTag.setFloat("FallDistance", this.fallDistance);
		compoundTag.setShort("Fire", (short)this.fire);
		compoundTag.setShort("Air", (short)this.air);
        compoundTag.setBoolean("OnGround", this.onGround);
		this.writeEntityToNBT(compoundTag);
	}

	public final void readFromNBT(NBTTagCompound compoundTag) {
		NBTTagList nBTTagList2 = compoundTag.getTagList("Pos");
		NBTTagList nBTTagList3 = compoundTag.getTagList("Motion");
		NBTTagList nBTTagList4 = compoundTag.getTagList("Rotation");
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.motionZ = ((NBTTagDouble)nBTTagList3.tagAt(0)).doubleValue;
        this.motionY = ((NBTTagDouble)nBTTagList3.tagAt(1)).doubleValue;
        this.motionX = ((NBTTagDouble)nBTTagList3.tagAt(2)).doubleValue;
		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)nBTTagList2.tagAt(0)).doubleValue;
        this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)nBTTagList2.tagAt(1)).doubleValue;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)nBTTagList2.tagAt(2)).doubleValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)nBTTagList4.tagAt(0)).floatValue;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)nBTTagList4.tagAt(1)).floatValue;
		this.fallDistance = compoundTag.getFloat("FallDistance");
		this.fire = compoundTag.getShort("Fire");
		this.air = compoundTag.getShort("Air");
        this.onGround = compoundTag.getBoolean("OnGround");
        this.setPosition(this.posX, this.posY, this.posZ);
		this.readEntityFromNBT(compoundTag);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	private static NBTTagList newDoubleNBTList(double... doubleArray) {
		NBTTagList nBTTagList1 = new NBTTagList();
		int i2 = (doubleArray = doubleArray).length;

		for(int i3 = 0; i3 < i2; ++i3) {
			double d5 = doubleArray[i3];
			nBTTagList1.setTag(new NBTTagDouble(d5));
		}

		return nBTTagList1;
	}

	public final EntityItem dropItemWithOffset(int itemID, int offset) {
		return this.entityDropItem(itemID, 1, 0.0F);
	}

	public final EntityItem entityDropItem(int blockID, int amount, float offsetY) {
		EntityItem blockID1;
		(blockID1 = new EntityItem(this.worldObj, this.posX, this.posY + (double)offsetY, this.posZ, new ItemStack(blockID, amount))).delayBeforeCanPickup = 10;
		this.worldObj.entityJoinedWorld(blockID1);
		return blockID1;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}

    public final boolean isEntityInsideOpaqueBlock() {
        int i1 = MathHelper.floor_double(this.posX);
        int i2 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight());
        int i3 = MathHelper.floor_double(this.posZ);
        return this.worldObj.isBlockNormalCube(i1, i2, i3);
    }

    public boolean interact(EntityPlayer entityPlayer1) {
        return false;
    }

    public AxisAlignedBB getCollisionBox(Entity entity1) {
        return null;
    }

    public void updateRidden() {
        if(this.ridingEntity.isDead) {
            this.ridingEntity = null;
        } else {
            this.motionZ = 0.0D;
            this.motionY = 0.0D;
            this.motionX = 0.0D;
            this.onEntityUpdate();
            this.setPosition(this.ridingEntity.posX, this.ridingEntity.posY + (double)this.yOffset + this.ridingEntity.S, this.ridingEntity.posZ);
            this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

            for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
            }

            while(this.entityRiderYawDelta < -180.0D) {
                this.entityRiderYawDelta += 360.0D;
            }

            while(this.entityRiderPitchDelta >= 180.0D) {
                this.entityRiderPitchDelta -= 360.0D;
            }

            while(this.entityRiderPitchDelta < -180.0D) {
                this.entityRiderPitchDelta += 360.0D;
            }

            double d1 = this.entityRiderYawDelta * 0.5D;
            double d3 = this.entityRiderPitchDelta * 0.5D;
            if(d1 > 10.0D) {
                d1 = 10.0D;
            }

            if(d1 < -10.0D) {
                d1 = -10.0D;
            }

            if(d3 > 10.0D) {
                d3 = 10.0D;
            }

            if(d3 < -10.0D) {
                d3 = -10.0D;
            }

            this.entityRiderYawDelta -= d1;
            this.entityRiderPitchDelta -= d3;
            this.rotationYaw = (float)((double)this.rotationYaw + d1);
            this.rotationPitch = (float)((double)this.rotationPitch + d3);
        }
    }

    public final void mountEntity(Entity entity1) {
        this.entityRiderPitchDelta = 0.0D;
        this.entityRiderYawDelta = 0.0D;
        if(this.ridingEntity == entity1) {
            this.ridingEntity.riddenByEntity = null;
            this.ridingEntity = null;
        } else {
            if(this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }

            if(entity1.riddenByEntity != null) {
                entity1.riddenByEntity.ridingEntity = null;
            }

            this.ridingEntity = entity1;
            entity1.riddenByEntity = this;
        }
    }
}