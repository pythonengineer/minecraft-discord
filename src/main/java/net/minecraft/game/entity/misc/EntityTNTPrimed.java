package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;

public class EntityTNTPrimed extends Entity {
    public int fuse;

    public EntityTNTPrimed(World world) {
        super(world);
        this.fuse = 0;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityTNTPrimed(World world, float x, float y, float z) {
        this(world);
		this.setPosition((double)x, (double)y, (double)z);
		float world1 = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.motionX = (double)(-MathHelper.sin(world1 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionY = (double)0.2F;
		this.motionZ = (double)(-MathHelper.cos(world1 * (float)Math.PI / 180.0F) * 0.02F);
		this.canTriggerWalking = false;
		this.fuse = 80;
		this.prevPosX = (double)x;
		this.prevPosY = (double)y;
		this.prevPosZ = (double)z;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		if(this.fuse-- <= 0) {
            this.setEntityDead();
			this.explode();
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

	}

	private void explode() {
		float f1 = 4.0F;
		this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, f1);
	}

	protected void writeEntityToNBT(NBTTagCompound compoundTag) {
		compoundTag.setByte("Fuse", (byte)this.fuse);
	}

	protected void readEntityFromNBT(NBTTagCompound compoundTag) {
		this.fuse = compoundTag.getByte("Fuse");
	}
}
