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

public class RenderMinecart extends Render {
	protected ModelBase modelMinecart;

	public RenderMinecart() {
		this.shadowSize = 0.5F;
		this.modelMinecart = new ModelMinecart();
	}

	public void renderMinecart(EntityMinecart entity, double x, double y, double z, float yaw, float partialTicks) {
		GL11.glPushMatrix();
		double d10 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
		double d12 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
		double d14 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
		double d16 = (double)0.3F;
		Vec3D vec3D18 = entity.getPos(d10, d12, d14);
		float f19 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		if(vec3D18 != null) {
			Vec3D vec3D20 = entity.getPosOffset(d10, d12, d14, d16);
			Vec3D vec3D21 = entity.getPosOffset(d10, d12, d14, -d16);
			if(vec3D20 == null) {
				vec3D20 = vec3D18;
			}

			if(vec3D21 == null) {
				vec3D21 = vec3D18;
			}

			x += vec3D18.xCoord - d10;
			y += (vec3D20.yCoord + vec3D21.yCoord) / 2.0D - d12;
			z += vec3D18.zCoord - d14;
			Vec3D vec3D22 = vec3D21.addVector(-vec3D20.xCoord, -vec3D20.yCoord, -vec3D20.zCoord);
			if(vec3D22.lengthVector() != 0.0D) {
				vec3D22 = vec3D22.normalize();
				yaw = (float)(Math.atan2(vec3D22.zCoord, vec3D22.xCoord) * 180.0D / Math.PI);
				f19 = (float)(Math.atan(vec3D22.yCoord) * 73.0D);
			}
		}

		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glRotatef(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f19, 0.0F, 0.0F, 1.0F);
		float f23 = (float)entity.timeSinceHit - partialTicks;
		float f24 = (float)entity.damageTaken - partialTicks;
		if(f24 < 0.0F) {
			f24 = 0.0F;
		}

		if(f23 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f23) * f23 * f24 / 10.0F * (float)entity.forwardDirection, 1.0F, 0.0F, 0.0F);
		}

        if(entity.minecartType != 0) {
            this.loadTexture("/terrain.png");
            float f25 = 0.75F;
            GL11.glScalef(f25, f25, f25);
            GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            if(entity.minecartType == 1) {
                (new RenderBlocks()).renderBlockOnInventory(Block.chest);
            } else if(entity.minecartType == 2) {
                (new RenderBlocks()).renderBlockOnInventory(Block.stoneOvenIdle);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
            GL11.glScalef(1.0F / f25, 1.0F / f25, 1.0F / f25);
        }

		this.loadTexture("/item/cart.png");
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.renderMinecart((EntityMinecart)entity, x, y, z, yaw, partialTicks);
    }
}
