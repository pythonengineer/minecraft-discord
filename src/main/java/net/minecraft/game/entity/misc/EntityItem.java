package net.minecraft.game.entity.misc;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;

public final class EntityItem extends Entity {
	public float itemMotionX;
	public float itemMotionY;
	public float itemMotionZ;
	public ItemStack item;
	private int unknownEntityItemInt;
	private int age = 0;
	public int delayBeforeCanPickup;

	public EntityItem(World var1, float var2, float var3, float var4, ItemStack var5) {
		super(var1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var3, var4);
		this.item = var5;
		Math.random();
		this.itemMotionX = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.itemMotionY = 0.2F;
		this.itemMotionZ = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.canTriggerWalking = false;
	}

	public final void onEntityUpdate() {
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.itemMotionY -= 0.04F;
		this.moveEntity(this.itemMotionX, this.itemMotionY, this.itemMotionZ);
		this.itemMotionX *= 0.98F;
		this.itemMotionY *= 0.98F;
		this.itemMotionZ *= 0.98F;
		if(this.onGround) {
			this.itemMotionX *= 0.7F;
			this.itemMotionZ *= 0.7F;
			this.itemMotionY *= -0.5F;
		}

		++this.unknownEntityItemInt;
		++this.age;
		if(this.age >= 6000) {
			this.setEntityDead();
		}

	}
}
