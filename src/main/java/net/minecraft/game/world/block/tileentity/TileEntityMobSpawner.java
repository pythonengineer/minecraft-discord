package net.minecraft.game.world.block.tileentity;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.EntityList;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.physics.AxisAlignedBB;

public class TileEntityMobSpawner extends TileEntity {
    private int delay = -1;
    public String mobID = "Pig";
    public double yaw;
    public double prevYaw = 0.0D;

    public TileEntityMobSpawner() {
        this.delay = 20;
    }

    public boolean anyPlayerInRange() {
        double d1 = this.worldObj.playerEntity.getDistanceSq((double)this.xCoord, (double)this.yCoord, (double)this.zCoord);
        return d1 <= 256.0D;
    }

    public void updateEntity() {
        this.prevYaw = this.yaw;
        if(this.anyPlayerInRange()) {
            double d33 = (double)((float)this.xCoord + this.worldObj.rand.nextFloat());
            double d3 = (double)((float)this.yCoord + this.worldObj.rand.nextFloat());
            double d5 = (double)((float)this.zCoord + this.worldObj.rand.nextFloat());
            this.worldObj.spawnParticle("smoke", d33, d3, d5, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("flame", d33, d3, d5, 0.0D, 0.0D, 0.0D);

            for(this.yaw += (double)(1000.0F / ((float)this.delay + 200.0F)); this.yaw > 360.0D; this.prevYaw -= 360.0D) {
                this.yaw -= 360.0D;
            }

            if(this.delay == -1) {
                this.updateDelay();
            }

            if(this.delay > 0) {
                --this.delay;
            } else {
                for(int i7 = 0; i7 < 4; ++i7) {
                    EntityLiving entityLiving8;
                    if((entityLiving8 = (EntityLiving)EntityList.createEntityByName(this.mobID, this.worldObj)) == null) {
                        return;
                    }

                    if(this.worldObj.getEntitiesWithinAABB(entityLiving8.getClass(), (AxisAlignedBB.getBoundingBoxFromPool((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1))).expand(8.0D, 4.0D, 8.0D)).size() >= 6) {
                        this.updateDelay();
                        return;
                    }

                    if(entityLiving8 != null) {
                        double d10 = (double)this.xCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 4.0D;
                        double d12 = (double)(this.yCoord + this.worldObj.rand.nextInt(3) - 1);
                        double d14 = (double)this.zCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 4.0D;
                        entityLiving8.setLocationAndAngles(d10, d12, d14, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
                        if(entityLiving8.getCanSpawnHere(d10, d12, d14)) {
                            this.worldObj.spawnEntityInWorld(entityLiving8);

                            for(int i9 = 0; i9 < 20; ++i9) {
                                d33 = (double)this.xCoord + 0.5D + ((double)this.worldObj.rand.nextFloat() - 0.5D) * 2.0D;
                                d3 = (double)this.yCoord + 0.5D + ((double)this.worldObj.rand.nextFloat() - 0.5D) * 2.0D;
                                d5 = (double)this.zCoord + 0.5D + ((double)this.worldObj.rand.nextFloat() - 0.5D) * 2.0D;
                                this.worldObj.spawnParticle("smoke", d33, d3, d5, 0.0D, 0.0D, 0.0D);
                                this.worldObj.spawnParticle("flame", d33, d3, d5, 0.0D, 0.0D, 0.0D);
                            }

                            entityLiving8.spawnExplosionParticle();
                            this.updateDelay();
                        }
                    }
                }

                super.updateEntity();
            }
        }
    }

    private void updateDelay() {
        this.delay = 200 + this.worldObj.rand.nextInt(600);
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        this.mobID = nBTTagCompound1.getString("EntityId");
        this.delay = nBTTagCompound1.getShort("Delay");
    }

    public void writeToNBT(NBTTagCompound nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.setString("EntityId", this.mobID);
        nBTTagCompound1.setShort("Delay", (short)this.delay);
    }
}
