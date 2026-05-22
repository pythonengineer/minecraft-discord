package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.block.Block;

public final class RenderMinecart extends Render {
	private ModelBase modelMinecart;

	public RenderMinecart() {
		this.shadowSize = 0.5F;
		this.modelMinecart = new ModelMinecart();
	}

	public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		EntityMinecart entityMinecart10001 = (EntityMinecart)entity;
		float f3 = yaw;
		double d16 = z;
		double d14 = y;
		double d12 = x;
		EntityMinecart entityMinecart26 = entityMinecart10001;
		GL11.glPushMatrix();
		double d20 = entityMinecart26.lastTickPosX + (entityMinecart26.posX - entityMinecart26.lastTickPosX) * (double)partialTicks;
		double d22 = entityMinecart26.lastTickPosY + (entityMinecart26.posY - entityMinecart26.lastTickPosY) * (double)partialTicks;
		double d24 = entityMinecart26.lastTickPosZ + (entityMinecart26.posZ - entityMinecart26.lastTickPosZ) * (double)partialTicks;
		Vec3D vec3D5 = entityMinecart26.getPos(d20, d22, d24);
		float f27 = entityMinecart26.prevRotationPitch + (entityMinecart26.rotationPitch - entityMinecart26.prevRotationPitch) * partialTicks;
		if(vec3D5 != null) {
			Vec3D vec3D7 = entityMinecart26.getPosOffset(d20, d22, d24, (double)0.3F);
			Vec3D vec3D29 = entityMinecart26.getPosOffset(d20, d22, d24, -0.30000001192092896D);
			if(vec3D7 == null) {
				vec3D7 = vec3D5;
			}

			if(vec3D29 == null) {
				vec3D29 = vec3D5;
			}

			d12 += vec3D5.xCoord - d20;
			d14 = y + ((vec3D7.yCoord + vec3D29.yCoord) / 2.0D - d22);
			d16 += vec3D5.zCoord - d24;
			if((vec3D5 = vec3D29.addVector(-vec3D7.xCoord, -vec3D7.yCoord, -vec3D7.zCoord)).lengthVector() != 0.0D) {
				f3 = -((float)(Math.atan2((vec3D5 = vec3D5.normalize()).zCoord, vec3D5.xCoord) * 180.0D / Math.PI));
				f27 = (float)(Math.atan(vec3D5.yCoord) * 73.0D);
			}
		}

		GL11.glTranslatef((float)d12, (float)d14, (float)d16);
		GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(f27, 0.0F, 0.0F, 1.0F);
		float f28 = (float)entityMinecart26.timeSinceHit - partialTicks;
		if((yaw = (float)entityMinecart26.damageTaken - partialTicks) < 0.0F) {
			yaw = 0.0F;
		}

		if(f28 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f28) * f28 * yaw / 10.0F * (float)entityMinecart26.forwardDirection, 1.0F, 0.0F, 0.0F);
		}

		this.loadTexture("/terrain.png");
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		(new RenderBlocks()).renderBlockOnInventory(Block.chest);
		GL11.glScalef(1.3333334F, 1.3333334F, 1.3333334F);
		this.loadTexture("/item/cart.png");
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}