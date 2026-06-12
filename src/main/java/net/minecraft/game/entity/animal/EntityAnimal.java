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

	protected float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBlockId(x, y - 1, z) == Block.grass.blockID ? 10.0F : this.worldObj.getBrightness(x, y, z) - 0.5F;
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
        return this.worldObj.getBlockId(i1, i2 - 1, i3) == Block.grass.blockID && this.worldObj.getBlockLightValue(i1, i2, i3) > 8 && super.getCanSpawnHere();
    }

    public int getTalkInterval() {
        return 120;
    }
}
