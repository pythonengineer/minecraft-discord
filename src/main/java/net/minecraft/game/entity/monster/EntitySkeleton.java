package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

public class EntitySkeleton extends EntityMob {
	public EntitySkeleton(World var1) {
		super(var1);
		this.texture = "/mob/skeleton.png";
	}

	protected final void attackEntity(Entity var1, float var2) {
		if(var2 < 10.0F) {
			var2 = var1.posX - this.posX;
			float var3 = var1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow var4 = new EntityArrow(this.worldObj, this);
				++var4.posY;
				float var6 = var1.posY - var4.posY;
				float var5 = MathHelper.sqrt_float(var2 * var2 + var3 * var3) * 0.4F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(var4);
				var4.setArrowHeading(var2, var6 + var5, var3, 0.6F, 4.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2((double)var3, (double)var2) * 180.0D / (double)((float)Math.PI)) - 90.0F;
			this.hasAttacked = true;
		}

	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return "Skeleton";
	}
}
