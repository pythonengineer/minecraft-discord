package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class EntityMinecart extends Entity implements IInventory {
	private ItemStack[] cargoItems;
	public int damageTaken;
	public int timeSinceHit;
	public int forwardDirection;
    private boolean isInReverse;
	private static final int[][][] matrix = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

	public EntityMinecart(World world1) {
		super(world1);
		this.cargoItems = new ItemStack[36];
		this.damageTaken = 0;
		this.timeSinceHit = 0;
		this.forwardDirection = 1;
        this.isInReverse = false;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.7F);
		this.yOffset = this.height / 2.0F;
		this.canTriggerWalking = false;
	}

	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityMinecart(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y + (double)this.yOffset, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

    public double getMountedYOffset() {
        return (double)this.height * 0.2D;
    }

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.forwardDirection = -this.forwardDirection;
		this.timeSinceHit = 10;
		this.damageTaken += i2 * 10;
		if(this.damageTaken > 40) {
			this.entityDropItem(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
			this.setEntityDead();
		}

		return true;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setEntityDead() {
		for(int i1 = 0; i1 < this.getSizeInventory(); ++i1) {
			ItemStack itemStack2 = this.getStackInSlot(i1);
			if(itemStack2 != null) {
				float f3 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f4 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f5 = this.rand.nextFloat() * 0.8F + 0.1F;

				while(itemStack2.stackSize > 0) {
					int i6 = this.rand.nextInt(21) + 10;
					if(i6 > itemStack2.stackSize) {
						i6 = itemStack2.stackSize;
					}

					itemStack2.stackSize -= i6;
					EntityItem entityItem7 = new EntityItem(this.worldObj, this.posX + (double)f3, this.posY + (double)f4, this.posZ + (double)f5, new ItemStack(itemStack2.itemID, i6, itemStack2.itemDmg));
					float f8 = 0.05F;
					entityItem7.motionX = (double)((float)this.rand.nextGaussian() * f8);
					entityItem7.motionY = (double)((float)this.rand.nextGaussian() * f8 + 0.2F);
					entityItem7.motionZ = (double)((float)this.rand.nextGaussian() * f8);
					this.worldObj.spawnEntityInWorld(entityItem7);
				}
			}
		}

		super.setEntityDead();
	}

	public void onUpdate() {
		if(this.timeSinceHit > 0) {
			--this.timeSinceHit;
		}

		if(this.damageTaken > 0) {
			--this.damageTaken;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.posY);
		int i3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.getBlockId(i1, i2 - 1, i3) == Block.minecartTrack.blockID) {
			--i2;
		}

		double d4 = 0.4D;
		double d6 = 2.0D / 256D;
		if(this.worldObj.getBlockId(i1, i2, i3) == Block.minecartTrack.blockID) {
			Vec3D vec3D4 = this.getPos(this.posX, this.posY, this.posZ);
			int i5 = this.worldObj.getBlockMetadata(i1, i2, i3);
			this.posY = (double)i2;
			if(i5 >= 2 && i5 <= 5) {
				this.posY = (double)(i2 + 1);
			}

			if(i5 == 2) {
				this.motionX -= d6;
			}

			if(i5 == 3) {
				this.motionX += d6;
			}

			if(i5 == 4) {
				this.motionZ += d6;
			}

			if(i5 == 5) {
				this.motionZ -= d6;
			}

			int[][] i35 = matrix[i5];
			double d7 = (double)(i35[1][0] - i35[0][0]);
			double d9 = (double)(i35[1][2] - i35[0][2]);
			double d11 = Math.sqrt(d7 * d7 + d9 * d9);
			double d17 = this.motionX * d7 + this.motionZ * d9;
			if(d17 < 0.0D) {
				d7 = -d7;
				d9 = -d9;
			}

			double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.motionX = d15 * d7 / d11;
			this.motionZ = d15 * d9 / d11;
			double d21 = 0.0D;
			double d19 = (double)i1 + 0.5D + (double)i35[0][0] * 0.5D;
			double d22 = (double)i3 + 0.5D + (double)i35[0][2] * 0.5D;
			double d23 = (double)i1 + 0.5D + (double)i35[1][0] * 0.5D;
			double d25 = (double)i3 + 0.5D + (double)i35[1][2] * 0.5D;
			d7 = d23 - d19;
			d9 = d25 - d22;
			double d27;
			double d29;
			if(d7 == 0.0D) {
				this.posX = (double)i1 + 0.5D;
				d21 = this.posZ - (double)i3;
			} else if(d9 == 0.0D) {
				this.posZ = (double)i3 + 0.5D;
				d21 = this.posX - (double)i1;
			} else {
				d27 = this.posX - d19;
				d29 = this.posZ - d22;
				double d35 = (d27 * d7 + d29 * d9) * 2.0D;
				d21 = d35;
			}

			this.posX = d19 + d7 * d21;
			this.posZ = d22 + d9 * d21;
			this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
			d27 = this.motionX;
			d29 = this.motionZ;
			if(this.riddenByEntity != null) {
				d27 *= 0.75D;
				d29 *= 0.75D;
			}

			if(d27 < -d4) {
				d27 = -d4;
			}

			if(d27 > d4) {
				d27 = d4;
			}

			if(d29 < -d4) {
				d29 = -d4;
			}

			if(d29 > d4) {
				d29 = d4;
			}

			this.moveEntity(d27, 0.0D, d29);
			if(i35[0][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i35[0][0] && MathHelper.floor_double(this.posZ) - i3 == i35[0][2]) {
				this.setPosition(this.posX, this.posY + (double)i35[0][1], this.posZ);
			} else if(i35[1][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i35[1][0] && MathHelper.floor_double(this.posZ) - i3 == i35[1][2]) {
				this.setPosition(this.posX, this.posY + (double)i35[1][1], this.posZ);
			}

			if(this.riddenByEntity != null) {
				this.motionX *= (double)0.997F;
				this.motionY *= 0.0D;
				this.motionZ *= (double)0.997F;
			} else {
				this.motionX *= (double)0.96F;
				this.motionY *= 0.0D;
				this.motionZ *= (double)0.96F;
			}

			Vec3D vec3D31 = this.getPos(this.posX, this.posY, this.posZ);
			if(vec3D31 != null && vec3D4 != null) {
				double d32 = (vec3D4.yCoord - vec3D31.yCoord) * 0.05D;
				d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
				if(d15 > 0.0D) {
					this.motionX = this.motionX / d15 * (d15 + d32);
					this.motionZ = this.motionZ / d15 * (d15 + d32);
				}

				this.setPosition(this.posX, vec3D31.yCoord, this.posZ);
            }

            int i39 = MathHelper.floor_double(this.posX);
            int i33 = MathHelper.floor_double(this.posZ);
            if(i39 != i1 || i33 != i3) {
                d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.motionX = d15 * (double)(i39 - i1);
                this.motionZ = d15 * (double)(i33 - i3);
			}
		} else {
			if(this.motionX < -d4) {
				this.motionX = -d4;
			}

			if(this.motionX > d4) {
				this.motionX = d4;
			}

			if(this.motionZ < -d4) {
				this.motionZ = -d4;
			}

			if(this.motionZ > d4) {
				this.motionZ = d4;
			}

			if(this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(!this.onGround) {
				this.motionX *= (double)0.95F;
				this.motionY *= (double)0.95F;
				this.motionZ *= (double)0.95F;
			}
		}

        this.rotationPitch = 0.0F;
        double d36 = this.prevPosX - this.posX;
        double d37 = this.prevPosZ - this.posZ;
        if(d36 * d36 + d37 * d37 > 0.001D) {
            this.rotationYaw = (float)(Math.atan2(d37, d36) * 180.0D / Math.PI);
            if(this.isInReverse) {
                this.rotationYaw += 180.0F;
            }
        }

        double d38;
        for(d38 = (double)(this.rotationYaw - this.prevRotationYaw); d38 >= 180.0D; d38 -= 360.0D) {
        }

        while(d38 < -180.0D) {
            d38 += 360.0D;
        }

        if(d38 < -170.0D || d38 >= 170.0D) {
            this.rotationYaw += 180.0F;
            this.isInReverse = !this.isInReverse;
        }

        this.setRotation(this.rotationYaw, this.rotationPitch);
        List list41 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
        if(list41 != null && list41.size() > 0) {
            for(int i42 = 0; i42 < list41.size(); ++i42) {
                Entity entity8 = (Entity)list41.get(i42);
                if(entity8 != this.riddenByEntity && entity8.canBePushed() && entity8 instanceof EntityMinecart) {
                    entity8.applyEntityCollision(this);
                }
            }
        }

		if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
			this.riddenByEntity = null;
		}

	}

	public Vec3D getPosOffset(double x, double y, double z, double offsetMultiplier) {
		int i9 = MathHelper.floor_double(x);
		int i10 = MathHelper.floor_double(y);
		int i11 = MathHelper.floor_double(z);
		if(this.worldObj.getBlockId(i9, i10 - 1, i11) == Block.minecartTrack.blockID) {
			--i10;
		}

		if(this.worldObj.getBlockId(i9, i10, i11) == Block.minecartTrack.blockID) {
			int i12 = this.worldObj.getBlockMetadata(i9, i10, i11);
			y = (double)i10;
			if(i12 >= 2 && i12 <= 5) {
				y = (double)(i10 + 1);
			}

			int[][] i13 = matrix[i12];
			double d14 = (double)(i13[1][0] - i13[0][0]);
			double d16 = (double)(i13[1][2] - i13[0][2]);
			double d18 = Math.sqrt(d14 * d14 + d16 * d16);
			d14 /= d18;
			d16 /= d18;
			x += d14 * offsetMultiplier;
			z += d16 * offsetMultiplier;
			if(i13[0][1] != 0 && MathHelper.floor_double(x) - i9 == i13[0][0] && MathHelper.floor_double(z) - i11 == i13[0][2]) {
				y += (double)i13[0][1];
			} else if(i13[1][1] != 0 && MathHelper.floor_double(x) - i9 == i13[1][0] && MathHelper.floor_double(z) - i11 == i13[1][2]) {
				y += (double)i13[1][1];
			}

			return this.getPos(x, y, z);
		} else {
			return null;
		}
	}

	public Vec3D getPos(double x, double y, double z) {
		int i7 = MathHelper.floor_double(x);
		int i8 = MathHelper.floor_double(y);
		int i9 = MathHelper.floor_double(z);
		if(this.worldObj.getBlockId(i7, i8 - 1, i9) == Block.minecartTrack.blockID) {
			--i8;
		}

		if(this.worldObj.getBlockId(i7, i8, i9) == Block.minecartTrack.blockID) {
			int i10 = this.worldObj.getBlockMetadata(i7, i8, i9);
			y = (double)i8;
			if(i10 >= 2 && i10 <= 5) {
				y = (double)(i8 + 1);
			}

			int[][] i11 = matrix[i10];
			double d12 = 0.0D;
			double d14 = (double)i7 + 0.5D + (double)i11[0][0] * 0.5D;
			double d16 = (double)i8 + 0.5D + (double)i11[0][1] * 0.5D;
			double d18 = (double)i9 + 0.5D + (double)i11[0][2] * 0.5D;
			double d20 = (double)i7 + 0.5D + (double)i11[1][0] * 0.5D;
			double d22 = (double)i8 + 0.5D + (double)i11[1][1] * 0.5D;
			double d24 = (double)i9 + 0.5D + (double)i11[1][2] * 0.5D;
			double d26 = d20 - d14;
			double d28 = (d22 - d16) * 2.0D;
			double d30 = d24 - d18;
			if(d26 == 0.0D) {
				x = (double)i7 + 0.5D;
				d12 = z - (double)i9;
			} else if(d30 == 0.0D) {
				z = (double)i9 + 0.5D;
				d12 = x - (double)i7;
			} else {
				double d32 = x - d14;
				double d34 = z - d18;
				double d36 = (d32 * d26 + d34 * d30) * 2.0D;
				d12 = d36;
			}

			x = d14 + d26 * d12;
			y = d16 + d28 * d12;
			z = d18 + d30 * d12;
			if(d28 < 0.0D) {
				++y;
			}

			if(d28 > 0.0D) {
				y += 0.5D;
			}

			return Vec3D.createVector(x, y, z);
		} else {
			return null;
		}
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.cargoItems.length; ++i3) {
			if(this.cargoItems[i3] != null) {
				NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
				nBTTagCompound4.setByte("Slot", (byte)i3);
				this.cargoItems[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nBTTagCompound1.setTag("Items", nBTTagList2);
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Items");
		this.cargoItems = new ItemStack[this.getSizeInventory()];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
			int i5 = nBTTagCompound4.getByte("Slot") & 255;
			if(i5 >= 0 && i5 < this.cargoItems.length) {
				this.cargoItems[i5] = new ItemStack(nBTTagCompound4);
			}
		}

	}

	public void applyEntityCollision(Entity entity1) {
		if(entity1 != this.riddenByEntity) {
			double d2 = entity1.posX - this.posX;
			double d4 = entity1.posZ - this.posZ;
			double d6 = d2 * d2 + d4 * d4;
			if(d6 >= 9.999999747378752E-5D) {
				d6 = (double)MathHelper.sqrt_double(d6);
				d2 /= d6;
				d4 /= d6;
				double d8 = 1.0D / d6;
				if(d8 > 1.0D) {
					d8 = 1.0D;
				}

				d2 *= d8;
				d4 *= d8;
				d2 *= (double)0.1F;
				d4 *= (double)0.1F;
				d2 *= (double)(1.0F - this.entityCollisionReduction);
				d4 *= (double)(1.0F - this.entityCollisionReduction);
				d2 *= 0.5D;
				d4 *= 0.5D;
				if(entity1 instanceof EntityMinecart) {
					double d10 = (entity1.motionX + this.motionX) / 2.0D;
					double d12 = (entity1.motionZ + this.motionZ) / 2.0D;
					this.motionX = this.motionZ = 0.0D;
					this.addVelocity(d10 - d2, 0.0D, d12 - d4);
					entity1.motionX = entity1.motionZ = 0.0D;
					entity1.addVelocity(d10 + d2, 0.0D, d12 + d4);
				} else {
					this.addVelocity(-d2, 0.0D, -d4);
					entity1.addVelocity(d2 / 4.0D, 0.0D, d4 / 4.0D);
				}
			}

		}
	}

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int i1) {
		return this.cargoItems[i1];
	}

	public ItemStack decrStackSize(int i1, int i2) {
		if(this.cargoItems[i1] != null) {
			ItemStack itemStack3;
			if(this.cargoItems[i1].stackSize <= i2) {
				itemStack3 = this.cargoItems[i1];
				this.cargoItems[i1] = null;
				return itemStack3;
			} else {
				itemStack3 = this.cargoItems[i1].splitStack(i2);
				if(this.cargoItems[i1].stackSize == 0) {
					this.cargoItems[i1] = null;
				}

				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i1, ItemStack itemStack2) {
		this.cargoItems[i1] = itemStack2;
		if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
			itemStack2.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Minecart";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}

    public boolean interact(EntityPlayer entityPlayer) {
        entityPlayer.mountEntity(this);
        return true;
    }
}
