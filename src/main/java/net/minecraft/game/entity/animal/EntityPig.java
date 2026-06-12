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

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
		compoundTag.setBoolean("Saddle", this.saddled);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
		this.saddled = compoundTag.getBoolean("Saddle");
	}

	protected String getLivingSound() {
		return "mob.pig";
	}

	protected String getHurtSound() {
		return "mob.pig";
	}

	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	public boolean interact(EntityPlayer entityPlayer) {
		if(this.saddled) {
			entityPlayer.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	protected int getDropItemId() {
		return Item.porkRaw.shiftedIndex;
	}
}
