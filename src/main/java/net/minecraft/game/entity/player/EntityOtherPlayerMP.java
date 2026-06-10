package net.minecraft.game.entity.player;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;

public class EntityOtherPlayerMP extends EntityPlayer {
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;
    private int bc;
    private double bd;
    private double be;
    private double bf;
    private double bg;
    private double bh;
    float unusedFloat = 0.0F;

    public EntityOtherPlayerMP(World worldObj, String username) {
        super(worldObj);
        this.username = username;
        this.yOffset = 1.62F;
        this.stepHeight = 0.0F;
        if(username != null && username.length() > 0) {
            this.skinUrl = username;
            System.out.println("Loading texture " + this.skinUrl);
        }

        this.noClip = true;
    }

    public boolean attackEntityFrom(Entity entity1, int i2) {
        return true;
    }

    public void setPositionAndRotation(double x, double y, double z, float rotationYaw, float rotationPitch, int newPosRotationIncrements) {
        this.otherPlayerMPX = x;
        this.otherPlayerMPY = y;
        this.otherPlayerMPZ = z;
        this.otherPlayerMPYaw = (double)rotationYaw;
        this.otherPlayerMPPitch = (double)rotationPitch;
        this.otherPlayerMPPosRotationIncrements = newPosRotationIncrements;
    }

    public void onUpdate() {
        super.onUpdate();
        if(this.otherPlayerMPPosRotationIncrements == 0 && this.bc > 0) {
            this.otherPlayerMPX = this.bd;
            this.otherPlayerMPY = this.be;
            this.otherPlayerMPZ = this.bf;
            this.otherPlayerMPYaw = this.bg;
            this.otherPlayerMPPitch = this.bh;
            this.otherPlayerMPPosRotationIncrements = this.bc;
            this.bc = 0;
        }

        double d1;
        double d3;
        if(this.otherPlayerMPPosRotationIncrements > 0) {
            d1 = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.otherPlayerMPPosRotationIncrements;
            d3 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.otherPlayerMPPosRotationIncrements;
            double d5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.otherPlayerMPPosRotationIncrements;

            double d7;
            for(d7 = this.otherPlayerMPYaw - (double)this.rotationYaw; d7 < -180.0D; d7 += 360.0D) {
            }

            while(d7 >= 180.0D) {
                d7 -= 360.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + d7 / (double)this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(d1, d3, d5);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }

        this.prevLimbYaw = this.limbYaw;
        d1 = this.posX - this.prevPosX;
        d3 = this.posZ - this.prevPosZ;
        float f9 = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 4.0F;
        if(f9 > 1.0F) {
            f9 = 1.0F;
        }

        this.limbYaw += (f9 - this.limbYaw) * 0.4F;
        this.limbSwing += this.limbYaw;
    }

    public float getShadowSize() {
        return -1.0F;
    }

    public void onLivingUpdate() {
        this.prevCameraYaw = this.cameraYaw;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
        if(f1 > 0.1F) {
            f1 = 0.1F;
        }

        if(!this.onGround || this.health <= 0) {
            f1 = 0.0F;
        }

        if(this.onGround || this.health <= 0) {
            f2 = 0.0F;
        }

        this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (f2 - this.cameraPitch) * 0.8F;
    }
}
