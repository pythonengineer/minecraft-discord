package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;

public class RenderLiving extends Render {
	protected ModelBase mainModel;
	protected ModelBase renderPassModel;

	public RenderLiving(ModelBase baseModel, float shadowSize) {
		this.mainModel = baseModel;
		this.shadowSize = shadowSize;
	}

	public void setRenderPassModel(ModelBase renderPassModel) {
		this.renderPassModel = renderPassModel;
	}

	public void renderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);

		try {
			f8 = entityLiving1.prevRenderYawOffset + (entityLiving1.renderYawOffset - entityLiving1.prevRenderYawOffset) * f9;
			float f10 = entityLiving1.prevRotationYaw + (entityLiving1.rotationYaw - entityLiving1.prevRotationYaw) * f9;
			float f11 = entityLiving1.prevRotationPitch + (entityLiving1.rotationPitch - entityLiving1.prevRotationPitch) * f9;
			GL11.glTranslatef((float)d2, (float)d4, (float)d6);
			float f14 = (float)entityLiving1.ticksExisted + f9;
			GL11.glRotatef(180.0F - f8, 0.0F, 1.0F, 0.0F);
			float f3;
			if(entityLiving1.deathTime > 0) {
				f3 = ((float)entityLiving1.deathTime + f9 - 1.0F) / 20.0F * 1.6F;
				f3 = MathHelper.sqrt_float(f3);
				if(f3 > 1.0F) {
					f3 = 1.0F;
				}

				GL11.glRotatef(f3 * this.getMaxDeathRotation(entityLiving1), 0.0F, 0.0F, 1.0F);
			}

            GL11.glEnable(GL11.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(entityLiving1, f9);
            GL11.glTranslatef(0.0F, -1.5078125F, 0.0F);
			f3 = entityLiving1.prevLimbYaw + (entityLiving1.limbYaw - entityLiving1.prevLimbYaw) * f9;
			float f15 = entityLiving1.limbSwing - entityLiving1.limbYaw * (1.0F - f9);
			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			this.loadDownloadableImageTexture(entityLiving1.skinUrl, entityLiving1.getTexture());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);

			for(int i5 = 0; i5 < 4; ++i5) {
				if(this.shouldRenderPass(entityLiving1, i5)) {
                    this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			float f16 = entityLiving1.getBrightness(f9);
			int i17 = this.getColorMultiplier(entityLiving1, f16, f9);
			if((i17 >> 24 & 255) > 0 || entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(entityLiving1.hurtTime > 0 || entityLiving1.deathTime > 0) {
					GL11.glColor4f(f16, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);

					for(int i7 = 0; i7 < 4; ++i7) {
						if(this.shouldRenderPass(entityLiving1, i7)) {
							GL11.glColor4f(f16, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);
						}
					}
				}

				if((i17 >> 24 & 255) > 0) {
					float f19 = (float)(i17 >> 16 & 255) / 255.0F;
					float f20 = (float)(i17 >> 8 & 255) / 255.0F;
					float f21 = (float)(i17 & 255) / 255.0F;
					float f22 = (float)(i17 >> 24 & 255) / 255.0F;
					GL11.glColor4f(f19, f20, f21, f22);
                    this.mainModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);

					for(int i12 = 0; i12 < 4; ++i12) {
						if(this.shouldRenderPass(entityLiving1, i12)) {
							GL11.glColor4f(f19, f20, f21, f22);
                            this.renderPassModel.render(f15, f3, f14, f10 - f8, f11, 0.0625F);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

            GL11.glDisable(GL11.GL_RESCALE_NORMAL);
		} catch (Exception exception13) {
			exception13.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
		return false;
	}

	protected float getMaxDeathRotation(EntityLiving livingEntity) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving livingEntity, float brightness, float partialTicks) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving livingEntity, float partialTicks) {
	}

	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		this.renderLiving((EntityLiving)entity, x, y, z, yaw, partialTicks);
	}
}
