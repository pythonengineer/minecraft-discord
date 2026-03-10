package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public abstract class EntityAnimal extends EntityCreature {
	public EntityAnimal(World world1) {
		super(world1);
	}

	protected final float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBlockId(x, y - 1, z) == Block.grass.blockID ? 10.0F : this.worldObj.getBrightness(x, y, z) - 0.5F;
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
	}

	public final boolean getCanSpawnHere(float x, float y, float z) {
		return this.worldObj.getBlockLightValue(MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z)) > 8 && super.getCanSpawnHere(x, y, z);
	}
}