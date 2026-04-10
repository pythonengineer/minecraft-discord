package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class EntityItem extends Entity {
	public ItemStack item;
	private int age2;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World world, double x, double y, double z, ItemStack stack) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.item = stack;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.canTriggerWalking = false;
	}

	public EntityItem(World world1) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		if(this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		double d6 = this.posZ;
		double d4 = this.posY;
		double d2 = this.posX;
		int i8 = MathHelper.floor_double(d2);
		int i9 = MathHelper.floor_double(d4);
		int i10 = MathHelper.floor_double(d6);
		double d11 = d2 - (double)i8;
		double d13 = d4 - (double)i9;
		double d15 = d6 - (double)i10;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10)]) {
			boolean z26 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8 - 1, i9, i10)];
			boolean z3 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8 + 1, i9, i10)];
			boolean z28 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9 - 1, i10)];
			boolean z5 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9 + 1, i10)];
			boolean z29 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10 - 1)];
			boolean z7 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10 + 1)];
			byte b30 = -1;
			double d24 = 9999.0D;
			if(z26 && d11 < 9999.0D) {
				d24 = d11;
				b30 = 0;
			}

			if(z3 && 1.0D - d11 < d24) {
				d24 = 1.0D - d11;
				b30 = 1;
			}

			if(z28 && d13 < d24) {
				d24 = d13;
				b30 = 2;
			}

			if(z5 && 1.0D - d13 < d24) {
				d24 = 1.0D - d13;
				b30 = 3;
			}

			if(z29 && d15 < d24) {
				d24 = d15;
				b30 = 4;
			}

			if(z7 && 1.0D - d15 < d24) {
				b30 = 5;
			}

			float f27 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(b30 == 0) {
				this.motionZ = (double)(-f27);
			}

			if(b30 == 1) {
				this.motionZ = (double)f27;
			}

			if(b30 == 2) {
				this.motionY = (double)(-f27);
			}

			if(b30 == 3) {
				this.motionY = (double)f27;
			}

			if(b30 == 4) {
				this.motionX = (double)(-f27);
			}

			if(b30 == 5) {
				this.motionX = (double)f27;
			}
		}

		boolean z10000 = false;
        this.handleWaterMovement();
		this.moveEntity(this.motionZ, this.motionY, this.motionX);
		this.motionZ *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionX *= (double)0.98F;
		if(this.onGround) {
			this.motionZ *= (double)0.7F;
			this.motionX *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		++this.age2;
		++this.age;
		if(this.age >= 6000) {
			super.isDead = true;
		}

	}

    public final boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

	protected final void dealFireDamage(int fireDamage) {
		this.attackEntityFrom((Entity)null, 1);
	}

	public final boolean attackEntityFrom(Entity entity, int damage) {
		this.health -= damage;
		if(this.health <= 0) {
			super.isDead = true;
		}

		return false;
	}

	public final void writeEntityToNBT(NBTTagCompound compoundTag) {
		compoundTag.setShort("Health", (byte)this.health);
		compoundTag.setShort("Age", (short)this.age);
		compoundTag.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	public final void readEntityFromNBT(NBTTagCompound compoundTag) {
		this.health = compoundTag.getShort("Health") & 255;
		this.age = compoundTag.getShort("Age");
		compoundTag = compoundTag.getCompoundTag("Item");
		this.item = new ItemStack(compoundTag);
	}

	public final void onCollideWithPlayer(EntityPlayer playerEntity) {
		if(this.delayBeforeCanPickup == 0 && playerEntity.inventory.addItemStackToInventory(this.item)) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			playerEntity.onItemPickup(this);
			super.isDead = true;
		}

	}
}