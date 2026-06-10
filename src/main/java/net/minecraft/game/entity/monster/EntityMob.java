package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;

public class EntityMob extends EntityCreature implements IMobs {
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
        EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
        return entityPlayer1 != null && this.updateEntityActionState(entityPlayer1) ? entityPlayer1 : null;
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

    public boolean getCanSpawnHere() {
        int i1 = MathHelper.floor_double(this.posX);
        int i2 = MathHelper.floor_double(this.boundingBox.minY);
        int i3 = MathHelper.floor_double(this.posZ);
        if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i1, i2, i3) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i4 = this.worldObj.getBlockLightValue(i1, i2, i3);
            return i4 <= this.rand.nextInt(8) && super.getCanSpawnHere();
        }
    }
}
