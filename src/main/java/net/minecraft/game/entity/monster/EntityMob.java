package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.world.World;

public class EntityMob extends EntityCreature {
	protected int attackStrength = 2;

	public EntityMob(World world1) {
		super(world1);
		this.health = 20;
	}

	public void onLivingUpdate() {
		if(this.getBrightness(1.0F) > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.worldObj.difficultySetting == 0) {
            this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		return this.worldObj.playerEntity.getDistanceToEntity(this) < 256.0D && this.getClosestPlayerToEntity(this.worldObj.playerEntity) ? this.worldObj.playerEntity : null;
	}

	public final boolean attackEntityFrom(Entity entity, int damage) {
		if(super.attackEntityFrom(entity, damage)) {
			if(entity != this) {
				this.playerToAttack = entity;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity entity, float damage) {
		if((double)damage < 2.5D && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int x, int y, int z) {
		return 0.5F - this.worldObj.getBrightness(x, y, z);
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
	}

	public final boolean getCanSpawnHere(float x, float y, float z) {
		return this.worldObj.getBlockLightValue(MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z)) <= this.rand.nextInt(8) && super.getCanSpawnHere(x, y, z);
	}
}