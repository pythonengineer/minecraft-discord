package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
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
	private static final int[][][] matrix = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

	public EntityMinecart(World world1) {
		super(world1);
		this.cargoItems = new ItemStack[36];
		this.damageTaken = 0;
		this.timeSinceHit = 0;
		this.forwardDirection = 1;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.7F);
		this.yOffset = this.height / 2.0F;
		this.canTriggerWalking = false;
	}

	public final AxisAlignedBB getCollisionBox(Entity entity1) {
		return entity1.boundingBox;
	}

	public final AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public final boolean canBePushed() {
		return true;
	}

	public EntityMinecart(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y + (double)this.yOffset, z);
		this.motionZ = 0.0D;
		this.motionY = 0.0D;
		this.motionX = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	public final boolean attackEntityFrom(Entity entity1, int i2) {
		this.forwardDirection = -this.forwardDirection;
		this.timeSinceHit = 10;
		this.damageTaken += i2 * 10;
		if(this.damageTaken > 40) {
			this.entityDropItem(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
			this.setEntityDead();
		}

		return true;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final void setEntityDead() {
		for(int i1 = 0; i1 < 27; ++i1) {
			ItemStack itemStack2;
			if((itemStack2 = this.cargoItems[i1]) != null) {
				float f3 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f4 = this.rand.nextFloat() * 0.8F + 0.1F;
				float f5 = this.rand.nextFloat() * 0.8F + 0.1F;

				while(itemStack2.stackSize > 0) {
					int i6;
					if((i6 = this.rand.nextInt(21) + 10) > itemStack2.stackSize) {
						i6 = itemStack2.stackSize;
					}

					itemStack2.stackSize -= i6;
					EntityItem entityItem7;
					(entityItem7 = new EntityItem(this.worldObj, this.posX + (double)f3, this.posY + (double)f4, this.posZ + (double)f5, new ItemStack(itemStack2.itemID, i6, itemStack2.itemDamage))).motionZ = (double)((float)this.rand.nextGaussian() * 0.05F);
					entityItem7.motionY = (double)((float)this.rand.nextGaussian() * 0.05F + 0.2F);
					entityItem7.motionX = (double)((float)this.rand.nextGaussian() * 0.05F);
					this.worldObj.entityJoinedWorld(entityItem7);
				}
			}
		}

		super.setEntityDead();
	}

	public final void onUpdate() {
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

		int i5;
		if(this.worldObj.getBlockId(i1, i2, i3) == Block.minecartTrack.blockID) {
			Vec3D vec3D4 = this.getPos(this.posX, this.posY, this.posZ);
			i5 = this.worldObj.getBlockMetadata(i1, i2, i3);
			this.posY = (double)i2;
			if(i5 >= 2 && i5 <= 5) {
				this.posY = (double)(i2 + 1);
			}

			if(i5 == 2) {
				this.motionZ -= 2.0D / 256D;
			}

			if(i5 == 3) {
				this.motionZ += 2.0D / 256D;
			}

			if(i5 == 4) {
				this.motionX += 2.0D / 256D;
			}

			if(i5 == 5) {
				this.motionX -= 2.0D / 256D;
			}

			int[][] i35;
			double d7 = (double)((i35 = matrix[i5])[1][0] - i35[0][0]);
			double d9 = (double)(i35[1][2] - i35[0][2]);
			double d11 = Math.sqrt(d7 * d7 + d9 * d9);
			if(this.motionZ * d7 + this.motionX * d9 < 0.0D) {
				d7 = -d7;
				d9 = -d9;
			}

			double d15 = Math.sqrt(this.motionZ * this.motionZ + this.motionX * this.motionX);
			this.motionZ = d15 * d7 / d11;
			this.motionX = d15 * d9 / d11;
			double d19 = (double)i1 + 0.5D + (double)i35[0][0] * 0.5D;
			double d21 = (double)i3 + 0.5D + (double)i35[0][2] * 0.5D;
			double d23 = (double)i1 + 0.5D + (double)i35[1][0] * 0.5D;
			double d25 = (double)i3 + 0.5D + (double)i35[1][2] * 0.5D;
			d7 = d23 - d19;
			d9 = d25 - d21;
			double d17;
			double d27;
			double d29;
			if(d7 == 0.0D) {
				this.posX = (double)i1 + 0.5D;
				d17 = this.posZ - (double)i3;
			} else if(d9 == 0.0D) {
				this.posZ = (double)i3 + 0.5D;
				d17 = this.posX - (double)i1;
			} else {
				d27 = this.posX - d19;
				d29 = this.posZ - d21;
				d17 = (d27 * d7 + d29 * d9) * 2.0D;
			}

			this.posX = d19 + d7 * d17;
			this.posZ = d21 + d9 * d17;
			this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
			d27 = this.motionZ;
			d29 = this.motionX;
			if(this.riddenByEntity != null) {
				d27 *= 0.75D;
				d29 *= 0.75D;
			}

			if(d27 < -0.4D) {
				d27 = -0.4D;
			}

			if(d27 > 0.4D) {
				d27 = 0.4D;
			}

			if(d29 < -0.4D) {
				d29 = -0.4D;
			}

			if(d29 > 0.4D) {
				d29 = 0.4D;
			}

			this.moveEntity(d27, 0.0D, d29);
			if(i35[0][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i35[0][0] && MathHelper.floor_double(this.posZ) - i3 == i35[0][2]) {
				this.setPosition(this.posX, this.posY + (double)i35[0][1], this.posZ);
			} else if(i35[1][1] != 0 && MathHelper.floor_double(this.posX) - i1 == i35[1][0] && MathHelper.floor_double(this.posZ) - i3 == i35[1][2]) {
				this.setPosition(this.posX, this.posY + (double)i35[1][1], this.posZ);
			}

			if(this.riddenByEntity != null) {
				this.motionZ *= (double)0.997F;
				this.motionY *= 0.0D;
				this.motionX *= (double)0.997F;
			} else {
				this.motionZ *= (double)0.96F;
				this.motionY *= 0.0D;
				this.motionX *= (double)0.96F;
			}

			Vec3D vec3D31;
			if((vec3D31 = this.getPos(this.posX, this.posY, this.posZ)) != null && vec3D4 != null) {
				double d32 = (vec3D4.yCoord - vec3D31.yCoord) * 0.05D;
				if((d15 = Math.sqrt(this.motionZ * this.motionZ + this.motionX * this.motionX)) > 0.0D) {
					this.motionZ = this.motionZ / d15 * (d15 + d32);
					this.motionX = this.motionX / d15 * (d15 + d32);
				}

				this.setPosition(this.posX, vec3D31.yCoord, this.posZ);
				if(vec3D31 != null) {
					Vec3D vec3D36 = this.getPosOffset(this.posX, this.posY, this.posZ, (double)0.3F);
					Vec3D vec3D37 = this.getPosOffset(this.posX, this.posY, this.posZ, -0.30000001192092896D);
					if(vec3D36 == null) {
						vec3D36 = vec3D31;
					}

					if(vec3D37 == null) {
						vec3D37 = vec3D31;
					}

					Vec3D vec3D34;
					if((vec3D34 = vec3D37.addVector(-vec3D36.xCoord, -vec3D36.yCoord, -vec3D36.zCoord)).lengthVector() != 0.0D) {
						vec3D34 = vec3D34.normalize();
						this.rotationYaw = -((float)(Math.atan2(vec3D34.zCoord, vec3D34.xCoord) * 180.0D / Math.PI));
						this.rotationPitch = 0.0F;
					}
				}
			}
		} else {
			if(this.motionZ < -0.4D) {
				this.motionZ = -0.4D;
			}

			if(this.motionZ > 0.4D) {
				this.motionZ = 0.4D;
			}

			if(this.motionX < -0.4D) {
				this.motionX = -0.4D;
			}

			if(this.motionX > 0.4D) {
				this.motionX = 0.4D;
			}

			if(this.onGround) {
				this.motionZ *= 0.5D;
				this.motionY *= 0.5D;
				this.motionX *= 0.5D;
			}

			this.moveEntity(this.motionZ, this.motionY, this.motionX);
			if(this.motionZ * this.motionZ + this.motionX * this.motionX > 0.001D) {
				this.rotationYaw = -((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI));
			}

			if(!this.onGround) {
				this.motionZ *= (double)0.95F;
				this.motionY *= (double)0.95F;
				this.motionX *= (double)0.95F;
			}
		}

		float f38 = this.rotationPitch;
		float f39 = this.rotationYaw;
		super.rotationYaw = f39;
		super.rotationPitch = f38;
		List list40;
		if((list40 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F))) != null && list40.size() > 0) {
			for(i5 = 0; i5 < list40.size(); ++i5) {
				Entity entity41;
				if((entity41 = (Entity)list40.get(i5)) != this.riddenByEntity && entity41.canBePushed() && entity41 instanceof EntityMinecart) {
					entity41.applyEntityCollision(this);
				}
			}
		}

		if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
			this.riddenByEntity = null;
		}

	}

	public final Vec3D getPosOffset(double x, double y, double z, double offsetMultiplier) {
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

			int[][] i20;
			double d14 = (double)((i20 = matrix[i12])[1][0] - i20[0][0]);
			double d16 = (double)(i20[1][2] - i20[0][2]);
			double d18 = Math.sqrt(d14 * d14 + d16 * d16);
			d14 /= d18;
			d16 /= d18;
			x += d14 * offsetMultiplier;
			z += d16 * offsetMultiplier;
			if(i20[0][1] != 0 && MathHelper.floor_double(x) - i9 == i20[0][0] && MathHelper.floor_double(z) - i11 == i20[0][2]) {
				y += (double)i20[0][1];
			} else if(i20[1][1] != 0 && MathHelper.floor_double(x) - i9 == i20[1][0] && MathHelper.floor_double(z) - i11 == i20[1][2]) {
				y += (double)i20[1][1];
			}

			return this.getPos(x, y, z);
		} else {
			return null;
		}
	}

	public final Vec3D getPos(double x, double y, double z) {
		int i7 = MathHelper.floor_double(x);
		int i38 = MathHelper.floor_double(y);
		int i4 = MathHelper.floor_double(z);
		if(this.worldObj.getBlockId(i7, i38 - 1, i4) == Block.minecartTrack.blockID) {
			--i38;
		}

		if(this.worldObj.getBlockId(i7, i38, i4) == Block.minecartTrack.blockID) {
			int i8 = this.worldObj.getBlockMetadata(i7, i38, i4);
			int[][] i39 = matrix[i8];
			double d14 = (double)i7 + 0.5D + (double)i39[0][0] * 0.5D;
			double d16 = (double)i38 + 0.5D + (double)i39[0][1] * 0.5D;
			double d18 = (double)i4 + 0.5D + (double)i39[0][2] * 0.5D;
			double d20 = (double)i7 + 0.5D + (double)i39[1][0] * 0.5D;
			double d22 = (double)i38 + 0.5D + (double)i39[1][1] * 0.5D;
			double d24 = (double)i4 + 0.5D + (double)i39[1][2] * 0.5D;
			double d26 = d20 - d14;
			double d28 = (d22 - d16) * 2.0D;
			double d30 = d24 - d18;
			double d12;
			if(d26 == 0.0D) {
				d12 = z - (double)i4;
			} else if(d30 == 0.0D) {
				d12 = x - (double)i7;
			} else {
				double d32 = x - d14;
				double d34 = z - d18;
				d12 = (d32 * d26 + d34 * d30) * 2.0D;
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

			return new Vec3D(x, y, z);
		} else {
			return null;
		}
	}

	protected final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.cargoItems.length; ++i3) {
			if(this.cargoItems[i3] != null) {
				NBTTagCompound nBTTagCompound4;
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				this.cargoItems[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nBTTagCompound1.setTag("Items", nBTTagList2);
	}

	protected final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList5 = nBTTagCompound1.getTagList("Items");
		this.cargoItems = new ItemStack[27];

		for(int i2 = 0; i2 < nBTTagList5.tagCount(); ++i2) {
			NBTTagCompound nBTTagCompound3;
			int i4;
			if((i4 = (nBTTagCompound3 = (NBTTagCompound)nBTTagList5.tagAt(i2)).getByte("Slot") & 255) >= 0 && i4 < this.cargoItems.length) {
				this.cargoItems[i4] = new ItemStack(nBTTagCompound3);
			}
		}

	}

	public final void applyEntityCollision(Entity entity1) {
		if(entity1 != this.riddenByEntity) {
			double d2 = entity1.posX - this.posX;
			double d4 = entity1.posZ - this.posZ;
			double d6;
			if((d6 = d2 * d2 + d4 * d4) >= 9.999999747378752E-5D) {
				d6 = (double)MathHelper.sqrt_double(d6);
				d2 /= d6;
				d4 /= d6;
				double d8;
				if((d8 = 1.0D / d6) > 1.0D) {
					d8 = 1.0D;
				}

				d2 *= d8;
				d4 *= d8;
				d2 *= (double)0.1F;
				d4 *= (double)0.1F;
				d2 *= 0.5D;
				d4 *= 0.5D;
				if(entity1 instanceof EntityMinecart) {
					double d10 = (entity1.motionZ + this.motionZ) / 2.0D;
					double d12 = (entity1.motionX + this.motionX) / 2.0D;
					this.motionZ = this.motionX = 0.0D;
					this.addVelocity(d10 - d2, 0.0D, d12 - d4);
					entity1.motionZ = entity1.motionX = 0.0D;
					entity1.addVelocity(d10 + d2, 0.0D, d12 + d4);
					return;
				}

				this.addVelocity(-d2, 0.0D, -d4);
				entity1.addVelocity(d2 / 4.0D, 0.0D, d4 / 4.0D);
			}

		}
	}

	public final int getSizeInventory() {
		return 27;
	}

	public final ItemStack getStackInSlot(int i1) {
		return this.cargoItems[i1];
	}

	public final ItemStack decrStackSize(int i1, int i2) {
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

	public final void setInventorySlotContents(int i1, ItemStack itemStack2) {
		this.cargoItems[i1] = itemStack2;
		if(itemStack2 != null && itemStack2.stackSize > 64) {
			itemStack2.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Minecart";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final void onInventoryChanged() {
	}

	public final void interact(Entity entity1) {
		this.riddenByEntity = entity1;
	}
}