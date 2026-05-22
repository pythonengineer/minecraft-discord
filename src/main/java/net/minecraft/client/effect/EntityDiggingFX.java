package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class EntityDiggingFX extends EntityFX {
	public EntityDiggingFX(World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, Block block) {
		super(world, posX, posY, posZ, speedX, speedY, speedZ);
		this.particleTextureIndex = block.blockIndexInTexture;
		this.particleGravity = block.blockParticleGravity;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
	}

	public final int getFXLayer() {
		return 1;
	}

	public final void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f8;
		float f9 = (f8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F) + 0.015609375F;
		float f10;
		float f11 = (f10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F) + 0.015609375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
		float f14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
		float f15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
		partialTicks = this.getBrightness(partialTicks);
		tessellator.setColorOpaque_F(partialTicks * this.particleRed, partialTicks * this.particleGreen, partialTicks * this.particleBlue);
		tessellator.addVertexWithUV((double)(f13 - rotationX * f12 - rotationXY * f12), (double)(f14 - rotationZ * f12), (double)(f15 - rotationYZ * f12 - rotationXZ * f12), (double)f8, (double)f11);
		tessellator.addVertexWithUV((double)(f13 - rotationX * f12 + rotationXY * f12), (double)(f14 + rotationZ * f12), (double)(f15 - rotationYZ * f12 + rotationXZ * f12), (double)f8, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + rotationX * f12 + rotationXY * f12), (double)(f14 + rotationZ * f12), (double)(f15 + rotationYZ * f12 + rotationXZ * f12), (double)f9, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + rotationX * f12 - rotationXY * f12), (double)(f14 - rotationZ * f12), (double)(f15 + rotationYZ * f12 - rotationXZ * f12), (double)f9, (double)f11);
	}
}