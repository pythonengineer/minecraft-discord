package net.minecraft.game.world;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public final class EntityMap extends EntityFX {
	private Entity Y;
	private EntityLiving Z;
	private int aa = 0;
	private int ab = 0;
	private float ac;

	public EntityMap(World var1, Entity var2, EntityLiving var3, float var4) {
		super(var1, var2.posX, var2.posY, var2.posZ, var2.motionX, var2.motionY, var2.motionZ);
		this.Y = var2;
		this.Z = var3;
		this.ab = 3;
		this.ac = -0.5F;
	}

	public final void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var27 = ((float)this.aa + var2) / (float)this.ab;
		var27 *= var27;
		double var9 = this.Y.posX;
		double var11 = this.Y.posY;
		double var13 = this.Y.posZ;
		double var15 = this.Z.lastTickPosX + (this.Z.posX - this.Z.lastTickPosX) * (double)var2;
		double var17 = this.Z.lastTickPosY + (this.Z.posY - this.Z.lastTickPosY) * (double)var2 + (double)this.ac;
		double var19 = this.Z.lastTickPosZ + (this.Z.posZ - this.Z.lastTickPosZ) * (double)var2;
		double var21 = var9 + (var15 - var9) * (double)var27;
		double var23 = var11 + (var17 - var11) * (double)var27;
		double var25 = var13 + (var19 - var13) * (double)var27;
		int var28 = MathHelper.floor_double(var21);
		int var29 = MathHelper.floor_double(var23 + (double)(this.yOffset / 2.0F));
		int var30 = MathHelper.floor_double(var25);
		var27 = this.worldObj.getBrightness(var28, var29, var30);
		var21 -= V;
		var23 -= W;
		var25 -= X;
		GL11.glColor4f(var27, var27, var27, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.Y, (double)((float)var21), (double)((float)var23), (double)((float)var25), this.Y.rotationYaw, var2);
	}

	public final void onUpdate() {
		++this.aa;
		if(this.aa == this.ab) {
			this.setEntityDead();
		}

	}

	public final int getFXLayer() {
		return 2;
	}
}
