package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityCow extends EntityAnimal {
    public boolean unusedBool2 = false;

    public EntityCow(World world1) {
        super(world1);
        this.texture = "/mob/cow.png";
        this.setSize(0.9F, 1.3F);
    }

    public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
        super.writeEntityToNBT(nBTTagCompound1);
    }

    public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readEntityFromNBT(nBTTagCompound1);
    }

    protected String getLivingSound() {
        return "mob.cow";
    }

    protected String getHurtSound() {
        return "mob.cowhurt";
    }

    protected String getDeathSound() {
        return "mob.cowhurt";
    }

    protected int getDropItemId() {
        return Item.leather.shiftedIndex;
    }
}
