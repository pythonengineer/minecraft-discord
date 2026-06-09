package net.minecraft.game.entity.misc;

import java.util.List;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class EntityBoat extends Entity {
    public int damageTaken;
    public int timeSinceHit;
    public int forwardDirection;
    private boolean d;

    public EntityBoat(World world1) {
        super(world1);
        this.damageTaken = 0;
        this.timeSinceHit = 0;
        this.forwardDirection = 1;
        this.d = false;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.6F);
        this.yOffset = this.height / 2.0F;
        this.canTriggerWalking = false;
    }

    public AxisAlignedBB getCollisionBox(Entity entity1) {
        return entity1.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public boolean canBePushed() {
        return true;
    }

    public EntityBoat(World worldObj, double x, double y, double z) {
        this(worldObj);
        this.setPosition(x, y + (double)this.yOffset, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public double getMountedYOffset() {
        return (double)this.height * 0.0D - (double)0.3F;
    }

    public boolean attackEntityFrom(Entity entity1, int i2) {
        this.forwardDirection = -this.forwardDirection;
        this.timeSinceHit = 10;
        this.damageTaken += i2 * 10;
        if(this.damageTaken > 40) {
            int i3;
            for(i3 = 0; i3 < 3; ++i3) {
                this.entityDropItem(Block.planks.blockID, 1, 0.0F);
            }

            for(i3 = 0; i3 < 2; ++i3) {
                this.entityDropItem(Item.stick.shiftedIndex, 1, 0.0F);
            }

            this.setEntityDead();
        }

        return true;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        super.onUpdate();
        if(this.timeSinceHit > 0) {
            --this.timeSinceHit;
        }

        if(this.damageTaken > 0) {
            --this.damageTaken;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte b1 = 5;
        double d2 = 0.0D;

        for(int i4 = 0; i4 < b1; ++i4) {
            double d5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 0) / (double)b1 - 0.125D;
            double d7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 1) / (double)b1 - 0.125D;
            AxisAlignedBB axisAlignedBB9 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, d5, this.boundingBox.minZ, this.boundingBox.maxX, d7, this.boundingBox.maxZ);
            if(this.worldObj.isAABBInMaterial(axisAlignedBB9, Material.water)) {
                d2 += 1.0D / (double)b1;
            }
        }

        double d23 = d2 * 2.0D - 1.0D;
        this.motionY += (double)0.04F * d23;
        if(this.riddenByEntity != null) {
            this.motionX += this.riddenByEntity.motionX * 0.2D;
            this.motionZ += this.riddenByEntity.motionZ * 0.2D;
        }

        double d6 = 0.4D;
        if(this.motionX < -d6) {
            this.motionX = -d6;
        }

        if(this.motionX > d6) {
            this.motionX = d6;
        }

        if(this.motionZ < -d6) {
            this.motionZ = -d6;
        }

        if(this.motionZ > d6) {
            this.motionZ = d6;
        }

        if(this.onGround) {
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        double d8;
        double d10;
        double d12;
        if(this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
            d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            d10 = -this.motionX / d8;
            d12 = -this.motionZ / d8;

            for(int i14 = 0; (double)i14 < 1.0D + d8 * 60.0D; ++i14) {
                double d15 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
                double d17 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
                double d19;
                double d21;
                if(this.rand.nextBoolean()) {
                    d19 = this.posX - d10 * d15 * 0.8D + d12 * d17;
                    d21 = this.posZ - d12 * d15 * 0.8D - d10 * d17;
                    d19 -= d10 * 6.0D / 16.0D;
                    d21 -= d12 * 6.0D / 16.0D;
                    this.worldObj.spawnParticle("splash", d19, this.posY - 0.125D, d21, this.motionX, this.motionY, this.motionZ);
                } else {
                    d19 = this.posX + d10 + d12 * d15 * 0.7D;
                    d21 = this.posZ + d12 - d10 * d15 * 0.7D;
                    d19 -= d10 * 6.0D / 16.0D;
                    d21 -= d12 * 6.0D / 16.0D;
                    this.worldObj.spawnParticle("splash", d19, this.posY - 0.125D, d21, this.motionX, this.motionY, this.motionZ);
                }
            }
        }

        if(!this.onGround && !this.isCollidedHorizontally) {
            this.motionX *= (double)0.99F;
            this.motionY *= (double)0.95F;
            this.motionZ *= (double)0.99F;
        } else {
            this.setEntityDead();

            int i24;
            for(i24 = 0; i24 < 3; ++i24) {
                this.entityDropItem(Block.planks.blockID, 1, 0.0F);
            }

            for(i24 = 0; i24 < 2; ++i24) {
                this.entityDropItem(Item.stick.shiftedIndex, 1, 0.0F);
            }
        }

        this.rotationPitch = 0.0F;
        d8 = this.prevPosX - this.posX;
        d10 = this.prevPosZ - this.posZ;
        if(d8 * d8 + d10 * d10 > 0.001D) {
            this.rotationYaw = (float)(Math.atan2(d10, d8) * 180.0D / Math.PI);
            if(this.d) {
                this.rotationYaw += 180.0F;
            }
        }

        for(d12 = (double)(this.rotationYaw - this.prevRotationYaw); d12 >= 180.0D; d12 -= 360.0D) {
        }

        while(d12 < -180.0D) {
            d12 += 360.0D;
        }

        if(d12 < -170.0D || d12 >= 170.0D) {
            this.rotationYaw += 180.0F;
            this.d = !this.d;
        }

        this.setRotation(this.rotationYaw, this.rotationPitch);
        List list25 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
        if(list25 != null && list25.size() > 0) {
            for(int i26 = 0; i26 < list25.size(); ++i26) {
                Entity entity16 = (Entity)list25.get(i26);
                if(entity16 != this.riddenByEntity && entity16.canBePushed() && entity16 instanceof EntityBoat) {
                    entity16.applyEntityCollision(this);
                }
            }
        }

        if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
            this.riddenByEntity = null;
        }

    }

    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
    }

    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
    }

    public boolean interact(EntityPlayer entityPlayer1) {
        entityPlayer1.mountEntity(this);
        return true;
    }
}
