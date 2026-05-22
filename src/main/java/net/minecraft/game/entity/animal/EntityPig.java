package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityPig extends EntityAnimal {
	public boolean saddled = false;

	public EntityPig(World world1) {
		super(world1);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
		this.saddled = false;
	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setBoolean("Saddle", this.saddled);
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.saddled = nBTTagCompound1.getBoolean("Saddle");
	}

	protected final String getLivingSound() {
		return "mob.pig";
	}

	protected final String getHurtSound() {
		return "mob.pig";
	}

	protected final String getDeathSound() {
		return "mob.pigdeath";
	}

	public final boolean interact(EntityPlayer entityPlayer) {
		if(this.saddled) {
			entityPlayer.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	protected final int getDropItemId() {
		return Item.porkRaw.shiftedIndex;
	}
}