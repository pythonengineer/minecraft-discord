package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityCreeper extends EntityMob {
	private int timeSinceIgnited;
	private int lastActiveTime;
	private int maxTime = 30;
	private int creeperState = -1;

	public EntityCreeper(World world1) {
		super(world1);
		this.texture = "/mob/creeper.png";
	}

	public final void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
	}

	public final void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
	}

	protected final void updatePlayerActionState() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
			--this.timeSinceIgnited;
		}

		if(this.creeperState >= 0) {
			this.creeperState = 2;
		}

		super.updatePlayerActionState();
		if(this.creeperState != 1) {
			this.creeperState = -1;
		}

	}

	protected final void attackEntity(Entity entity, float damage) {
		if(this.creeperState <= 0 && damage < 3.0F || this.creeperState > 0 && damage < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.maxTime) {
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
	            this.setEntityDead();
			}

			this.powered = true;
		}

	}

	public final float getCreeperFlashTime(float partialTime) {
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * partialTime) / (float)(this.maxTime - 2);
	}

	protected final int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}
}