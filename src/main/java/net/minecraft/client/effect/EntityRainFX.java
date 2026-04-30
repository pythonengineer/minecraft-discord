package net.minecraft.client.effect;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.material.Material;

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.motionZ *= (double)0.3F;
		this.motionY = (double)((float)Math.random() * 0.2F + 0.1F);
		this.motionX *= (double)0.3F;
		this.particleBlue = 1.0F;
		this.particleGreen = 1.0F;
		this.particleRed = 1.0F;
		this.particleTextureIndex = 16;
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		super.renderParticle(tessellator, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	public final void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)this.particleGravity;
		this.moveEntity(this.motionZ, this.motionY, this.motionX);
		this.motionZ *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionX *= (double)0.98F;
		if(this.particleMaxAge-- <= 0) {
            this.setEntityDead();
		}

		if(this.onGround) {
			if(Math.random() < 0.5D) {
	            this.setEntityDead();
			}

			this.motionZ *= (double)0.7F;
			this.motionX *= (double)0.7F;
		}

		Material material1;
		if((material1 = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getIsLiquid() || material1.isSolid()) {
			double d2 = (double)((float)(MathHelper.floor_double(this.posY) + 1) - BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));
			if(this.posY < d2) {
	            this.setEntityDead();
			}
		}

	}
}