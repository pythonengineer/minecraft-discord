package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntitySkeleton extends EntityMob {
	public EntitySkeleton(World world1) {
		super(world1);
		this.texture = "/mob/skeleton.png";
	}

	public final void onLivingUpdate() {
		float f1;
		if(this.worldObj.isDaytime() && (f1 = this.getBrightness(1.0F)) > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
			this.fire = 300;
		}

		super.onLivingUpdate();
	}

	protected final void attackEntity(Entity entity, float damage) {
		if(damage < 10.0F) {
			double d3 = entity.posX - this.posX;
			double d5 = entity.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow damage1;
				++(damage1 = new EntityArrow(this.worldObj, this)).posY;
				double d8 = entity.posY - (double)0.2F - damage1.posY;
				float entity1 = MathHelper.sqrt_double(d3 * d3 + d5 * d5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(damage1);
				damage1.setArrowHeading(d3, d8 + (double)entity1, d5, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}

	public final void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
	}

	public final void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
	}

	protected final int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}
}