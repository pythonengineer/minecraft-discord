package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;

public final class EntityFlameFX extends EntityFX {
	private float flameScale;

	public EntityFlameFX(World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ) {
		super(world, posX, posY, posZ, speedX, speedY, speedZ);
        this.motionX = this.motionX * (double)0.01F + speedX;
        this.motionY = this.motionY * (double)0.01F + speedY;
        this.motionZ = this.motionZ * (double)0.01F + speedZ;
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.flameScale = this.particleScale;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.noClip = true;
		this.particleTextureIndex = 48;
	}

	public final void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f8 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f8 * f8 * 0.5F);
		super.renderParticle(tessellator, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	public final float getBrightness(float partialTicks) {
		float f2;
		if((f2 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge) < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return super.getBrightness(partialTicks) * f2 + (1.0F - f2);
	}

	public final void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}