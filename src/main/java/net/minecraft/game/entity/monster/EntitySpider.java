package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntitySpider extends EntityMob {
    public EntitySpider(World world1) {
        super(world1);
        this.texture = "/mob/spider.png";
        this.setSize(1.4F, 0.9F);
        this.moveSpeed = 0.8F;
    }

    protected final Entity findPlayerToAttack() {
        return this.getBrightness(1.0F) < 0.5F && this.worldObj.playerEntity.getDistanceSqToEntity(this) < 256.0D ? this.worldObj.playerEntity : null;
    }

    protected final void attackEntity(Entity entity, float damage) {
        if(this.getBrightness(1.0F) > 0.5F && this.rand.nextInt(100) == 0) {
            this.playerToAttack = null;
        } else {
            if(damage > 2.0F && damage < 6.0F && this.rand.nextInt(10) == 0) {
                if(this.onGround) {
                    double d4 = entity.posX - this.posX;
                    double d6 = entity.posZ - this.posZ;
                    float entity1 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
                    this.motionX = d4 / (double)entity1 * 0.5D * (double)0.8F + this.motionX * (double)0.2F;
                    this.motionZ = d6 / (double)entity1 * 0.5D * (double)0.8F + this.motionZ * (double)0.2F;
                    this.motionY = (double)0.4F;
                    return;
                }
            } else {
                super.attackEntity(entity, damage);
            }

        }
    }

    public final void writeEntityToNBT(NBTTagCompound compoundTag) {
        super.writeEntityToNBT(compoundTag);
    }

    public final void readEntityFromNBT(NBTTagCompound compoundTag) {
        super.readEntityFromNBT(compoundTag);
    }

    protected final int getDropItemId() {
        return Item.silk.shiftedIndex;
    }
}