package net.minecraft.client.effect;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;

public class EntityFX extends Entity {
	protected int particleTextureIndex;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge = 0;
	protected int particleMaxAge = 0;
	protected float particleScale;
	protected float particleGravity;
	protected float particleBlue;
	protected float particleGreen;
	protected float particleRed;
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;

	public EntityFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12) {
		super(world1);
		this.setSize(0.2F, 0.2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(d2, d4, d6);
		this.particleBlue = this.particleGreen = this.particleRed = 1.0F;
		this.motionX = d8 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionY = d10 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionZ = d12 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		float f14 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		float f15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.motionX = this.motionX / (double)f15 * (double)f14 * (double)0.4F;
		this.motionY = this.motionY / (double)f15 * (double)f14 * (double)0.4F + (double)0.1F;
		this.motionZ = this.motionZ / (double)f15 * (double)f14 * (double)0.4F;
		this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
		this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
		this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
		this.particleAge = 0;
		this.canTriggerWalking = false;
	}

	public final EntityFX multiplyParticleScaleBy(float scale) {
		this.setSize(0.120000005F, 0.120000005F);
		this.particleScale *= 0.6F;
		return this;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			super.isDead = true;
		}

		this.motionY -= 0.04D * (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}

	public void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f8;
		float f9 = (f8 = (float)(this.particleTextureIndex % 16) / 16.0F) + 0.0624375F;
		float f10;
		float f11 = (f10 = (float)(this.particleTextureIndex / 16) / 16.0F) + 0.0624375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
		float f14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
		float f15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
		partialTicks = this.getBrightness(partialTicks);
		tessellator.setColorOpaque_F(this.particleBlue * partialTicks, this.particleGreen * partialTicks, this.particleRed * partialTicks);
		tessellator.addVertexWithUV((double)(f13 - rotationX * f12 - rotationXY * f12), (double)(f14 - rotationZ * f12), (double)(f15 - rotationYZ * f12 - rotationXZ * f12), (double)f8, (double)f11);
		tessellator.addVertexWithUV((double)(f13 - rotationX * f12 + rotationXY * f12), (double)(f14 + rotationZ * f12), (double)(f15 - rotationYZ * f12 + rotationXZ * f12), (double)f8, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + rotationX * f12 + rotationXY * f12), (double)(f14 + rotationZ * f12), (double)(f15 + rotationYZ * f12 + rotationXZ * f12), (double)f9, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + rotationX * f12 - rotationXY * f12), (double)(f14 - rotationZ * f12), (double)(f15 + rotationYZ * f12 - rotationXZ * f12), (double)f9, (double)f11);
	}

	public int getFXLayer() {
		return 0;
	}

	public final void writeEntityToNBT(NBTTagCompound compoundTag) {
	}

	public final void readEntityFromNBT(NBTTagCompound compoundTag) {
	}
}