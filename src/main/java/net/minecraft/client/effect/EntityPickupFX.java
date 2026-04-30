package net.minecraft.client.effect;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.World;

public final class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private EntityLiving entityPickingUp;
	private int age = 0;
	private int maxAge = 0;
	private float yOffs;

	public EntityPickupFX(World world, Entity entity, EntityLiving livingEntity, float yOffset) {
		super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
		this.entityToPickUp = entity;
		this.entityPickingUp = livingEntity;
		this.maxAge = 3;
		this.yOffs = -0.5F;
	}

	public final void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float tessellator1 = (tessellator1 = ((float)this.age + partialTicks) / (float)this.maxAge) * tessellator1;
		double d9 = this.entityToPickUp.posX;
		double d11 = this.entityToPickUp.posY;
		double d13 = this.entityToPickUp.posZ;
		double d15 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * (double)partialTicks;
		double d17 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * (double)partialTicks + (double)this.yOffs;
		double d19 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * (double)partialTicks;
		double d21 = d9 + (d15 - d9) * (double)tessellator1;
		double d23 = d11 + (d17 - d11) * (double)tessellator1;
		double d25 = d13 + (d19 - d13) * (double)tessellator1;
		int tessellator2 = MathHelper.floor_double(d21);
		int rotationX1 = MathHelper.floor_double(d23 + (double)(this.yOffset / 2.0F));
		int rotationZ1 = MathHelper.floor_double(d25);
		tessellator1 = this.worldObj.getBrightness(tessellator2, rotationX1, rotationZ1);
		d21 -= interpPosX;
		d23 -= interpPosY;
		d25 -= interpPosZ;
		GL11.glColor4f(tessellator1, tessellator1, tessellator1, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, (double)((float)d21), (double)((float)d23), (double)((float)d25), this.entityToPickUp.rotationYaw, partialTicks);
	}

	public final void onUpdate() {
		++this.age;
		if(this.age == this.maxAge) {
            this.setEntityDead();
		}

	}

	public final int getFXLayer() {
		return 2;
	}
}