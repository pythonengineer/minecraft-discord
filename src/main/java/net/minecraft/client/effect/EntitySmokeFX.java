package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;

public final class EntitySmokeFX extends EntityFX {
	private float smokeParticleScale;

	public EntitySmokeFX(World world, double posX, double posY, double posZ) {
		this(world, posX, posY, posZ, 1.0F);
	}

	public EntitySmokeFX(World world, double posX, double posY, double posZ, float scaleMultiplier) {
		super(world, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.motionX *= (double)0.1F;
		this.motionY *= (double)0.1F;
		this.motionZ *= (double)0.1F;
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.3F);
		this.particleScale *= 0.75F;
		this.particleScale *= scaleMultiplier;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * scaleMultiplier);
		this.noClip = false;
	}

	public final void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f8;
		if((f8 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F) < 0.0F) {
			f8 = 0.0F;
		}

		if(f8 > 1.0F) {
			f8 = 1.0F;
		}

		this.particleScale = this.smokeParticleScale * f8;
		super.renderParticle(tessellator, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	public final void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
		}

		this.particleTextureIndex = 7 - (this.particleAge << 3) / this.particleMaxAge;
		this.motionY += 0.004D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if(this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}