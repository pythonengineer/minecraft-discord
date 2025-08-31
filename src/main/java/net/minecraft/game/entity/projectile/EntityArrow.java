package net.minecraft.game.entity.projectile;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class EntityArrow extends Entity {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private boolean inGround = false;
    public int arrowShake = 0;
    private EntityLiving owner;

    public EntityArrow(World var1, EntityLiving var2) {
        super(var1);
        this.owner = var2;
        this.setSize(0.5F, 0.5F);
        this.setPositionAndRotation(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.posX += MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.posY -= 0.1F;
        this.posZ += MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 1.5F;
        this.motionZ = -MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 1.5F;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * 1.5F;
        this.motionX = (float)((double)this.motionX + this.rand.nextGaussian() * (double)0.01F);
        this.motionY = (float)((double)this.motionY + this.rand.nextGaussian() * (double)0.01F);
        this.motionZ = (float)((double)this.motionZ + this.rand.nextGaussian() * (double)0.01F);
        float var3 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2((double)this.motionX, (double)this.motionZ) * 180.0D / (double)((float)Math.PI));
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2((double)this.motionY, (double)var3) * 180.0D / (double)((float)Math.PI));
    }

    public final void onEntityUpdate() {
        super.onEntityUpdate();
        if(this.arrowShake > 0) {
            --this.arrowShake;
        }

        int var1;
        if(this.inGround) {
            var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            if(var1 == this.inTile) {
                return;
            }

            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
        }

        Vec3D var3 = new Vec3D(this.posX, this.posY, this.posZ);
        Vec3D var2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var4 = this.worldObj.rayTraceBlocks(var3, var2);
        float var5;
        if(var4 != null) {
            this.xTile = var4.blockX;
            this.yTile = var4.blockY;
            this.zTile = var4.blockZ;
            this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            this.motionX = var4.hitVec.xCoord - this.posX;
            this.motionY = var4.hitVec.yCoord - this.posY;
            this.motionZ = var4.hitVec.zCoord - this.posZ;
            var5 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / var5 * 0.05F;
            this.posY -= this.motionY / var5 * 0.05F;
            this.posZ -= this.motionZ / var5 * 0.05F;
            this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        var5 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2((double)this.motionX, (double)this.motionZ) * 180.0D / (double)((float)Math.PI));

        for(this.rotationPitch = (float)(Math.atan2((double)this.motionY, (double)var5) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
        }

        while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        var5 = 0.99F;
        if(this.handleWaterMovement()) {
            for(var1 = 0; var1 < 4; ++var1) {
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25F, this.posY - this.motionY * 0.25F, this.posZ - this.motionZ * 0.25F, this.motionX, this.motionY, this.motionZ);
            }

            var5 = 0.85F;
        }

        this.motionX *= var5;
        this.motionY *= var5;
        this.motionZ *= var5;
        this.motionY -= 0.03F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected final void writeEntityToNBT(NBTTagCompound var1) {
        var1.setShort("xTile", (short)this.xTile);
        var1.setShort("yTile", (short)this.yTile);
        var1.setShort("zTile", (short)this.zTile);
        var1.setByte("inTile", (byte)this.inTile);
        var1.setByte("shake", (byte)this.arrowShake);
        var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    protected final void readEntityFromNBT(NBTTagCompound var1) {
        this.xTile = var1.getShort("xTile");
        this.yTile = var1.getShort("yTile");
        this.zTile = var1.getShort("zTile");
        this.inTile = var1.getByte("inTile") & 255;
        this.arrowShake = var1.getByte("shake") & 255;
        this.inGround = var1.getByte("inGround") == 1;
    }

    protected final String getEntityString() {
        return "Arrow";
    }

    public final void onCollideWithPlayer(EntityPlayer var1) {
        if(this.inGround && this.owner == var1 && this.arrowShake <= 0 && var1.inventory.storePartialItemStack(new ItemStack(Item.arrow.shiftedIndex, 1))) {
            this.setEntityDead();
        }

    }

    public final float getShadowSize() {
        return 0.0F;
    }
}
