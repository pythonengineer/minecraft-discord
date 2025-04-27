package net.minecraft.game.entity.misc;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;

public final class EntityTNTPrimed extends Entity {
	private float tntMotionX;
	private float tntMotionY;
	private float tntMotionZ;
	public int fuse = 0;

	public EntityTNTPrimed(World var1, float var2, float var3, float var4) {
		super(var1);
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var3, var4);
		float var5 = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.tntMotionX = -MathHelper.sin(var5 * (float)Math.PI / 180.0F) * 0.02F;
		this.tntMotionY = 0.2F;
		this.tntMotionZ = -MathHelper.cos(var5 * (float)Math.PI / 180.0F) * 0.02F;
		this.canTriggerWalking = false;
		this.fuse = 40;
		this.prevPosX = var2;
		this.prevPosY = var3;
		this.prevPosZ = var4;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.tntMotionY -= 0.04F;
		this.moveEntity(this.tntMotionX, this.tntMotionY, this.tntMotionZ);
		this.tntMotionX *= 0.98F;
		this.tntMotionY *= 0.98F;
		this.tntMotionZ *= 0.98F;
		if(this.onGround) {
			this.tntMotionX *= 0.7F;
			this.tntMotionZ *= 0.7F;
			this.tntMotionY *= -0.5F;
		}

		if(this.fuse-- <= 0) {
			this.worldObj.playSoundAtEntity(this, "random.explode", 4.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F) * 0.7F);
			this.setEntityDead();
			this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 4.0F);
		}

	}
}
