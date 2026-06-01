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
		float f1 = this.getBrightness(1.0F);
		if(f1 > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.worldObj.difficultySetting == 0) {
            this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		double d1 = this.worldObj.playerEntity.getDistanceSqToEntity(this);
		double d3 = 16.0D;
		return d1 < d3 * d3 && this.updateEntityActionState(this.worldObj.playerEntity) ? this.worldObj.playerEntity : null;
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		if(super.attackEntityFrom(entity, damage)) {
			if(entity != this) {
				this.entityToAttack = entity;
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

	public boolean getCanSpawnHere(double x, double y, double z) {
		int i7 = this.worldObj.getBlockLightValue(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
		return i7 <= this.rand.nextInt(8) && super.getCanSpawnHere(x, y, z);
	}
}
