package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class EffectRenderer {
	private World worldObj;
	private List[] fxLayers = new List[3];
	private RenderEngine renderer;
	private EaglercraftRandom rand = new EaglercraftRandom();

	public EffectRenderer(World world, RenderEngine renderEngine) {
		if(world != null) {
			this.worldObj = world;
		}

		this.renderer = renderEngine;

		for(int i3 = 0; i3 < 3; ++i3) {
			this.fxLayers[i3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX fxEntity) {
		int i2 = fxEntity.getFXLayer();
		this.fxLayers[i2].add(fxEntity);
	}

	public final void updateEffects() {
		for(int i1 = 0; i1 < 3; ++i1) {
			for(int i2 = 0; i2 < this.fxLayers[i1].size(); ++i2) {
				EntityFX entityFX3;
				(entityFX3 = (EntityFX)this.fxLayers[i1].get(i2)).onUpdate();
				if(entityFX3.isDead) {
					this.fxLayers[i1].remove(i2--);
				}
			}
		}

	}

	public final void renderParticles(Entity entity, float partialTime) {
		float f3 = MathHelper.cos(entity.rotationYaw * (float)Math.PI / 180.0F);
		float f4;
		float f5 = -(f4 = MathHelper.sin(entity.rotationYaw * (float)Math.PI / 180.0F)) * MathHelper.sin(entity.rotationPitch * (float)Math.PI / 180.0F);
		float f6 = f3 * MathHelper.sin(entity.rotationPitch * (float)Math.PI / 180.0F);
		float f7 = MathHelper.cos(entity.rotationPitch * (float)Math.PI / 180.0F);
		EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTime;
		EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTime;
		EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTime;

		for(int i11 = 0; i11 < 2; ++i11) {
			if(this.fxLayers[i11].size() != 0) {
				int i8 = 0;
				if(i11 == 0) {
					i8 = this.renderer.getTexture("/particles.png");
				}

				if(i11 == 1) {
					i8 = this.renderer.getTexture("/terrain.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
				Tessellator tessellator12 = Tessellator.instance;
				Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

				for(int i9 = 0; i9 < this.fxLayers[i11].size(); ++i9) {
					((EntityFX)this.fxLayers[i11].get(i9)).renderParticle(tessellator12, partialTime, f3, f7, f4, f5, f6);
				}

				tessellator12.draw();
			}
		}

	}

	public final void renderLitParticles(float partialTime) {
		if(this.fxLayers[2].size() != 0) {
			Tessellator tessellator2 = Tessellator.instance;

			for(int i3 = 0; i3 < this.fxLayers[2].size(); ++i3) {
				((EntityFX)this.fxLayers[2].get(i3)).renderParticle(tessellator2, partialTime, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public final void clearEffects(World world) {
		this.worldObj = world;

		for(int i2 = 0; i2 < 3; ++i2) {
			this.fxLayers[i2].clear();
		}

	}

	public final void addBlockDestroyEffects(int x, int y, int z) {
		int i4;
		if((i4 = this.worldObj.getBlockId(x, y, z)) != 0) {
			Block block15 = Block.blocksList[i4];

			for(int i5 = 0; i5 < 4; ++i5) {
				for(int i6 = 0; i6 < 4; ++i6) {
					for(int i7 = 0; i7 < 4; ++i7) {
						double d9 = (double)x + ((double)i5 + 0.5D) / 4.0D;
						double d11 = (double)y + ((double)i6 + 0.5D) / 4.0D;
						double d13 = (double)z + ((double)i7 + 0.5D) / 4.0D;
						this.addEffect(new EntityDiggingFX(this.worldObj, d9, d11, d13, d9 - (double)x - 0.5D, d11 - (double)y - 0.5D, d13 - (double)z - 0.5D, block15));
					}
				}
			}

		}
	}

	public final void addBlockHitEffects(int x, int y, int z, int side) {
		int i5;
		if((i5 = this.worldObj.getBlockId(x, y, z)) != 0) {
			Block block15 = Block.blocksList[i5];
			double d7 = (double)x + this.rand.nextDouble() * (block15.maxX - block15.minX - (double)0.2F) + (double)0.1F + block15.minX;
			double d9 = (double)y + this.rand.nextDouble() * (block15.maxY - block15.minY - (double)0.2F) + (double)0.1F + block15.minY;
			double d11 = (double)z + this.rand.nextDouble() * (block15.maxZ - block15.minZ - (double)0.2F) + (double)0.1F + block15.minZ;
			if(side == 0) {
				d9 = (double)y + block15.minY - (double)0.1F;
			}

			if(side == 1) {
				d9 = (double)y + block15.maxY + (double)0.1F;
			}

			if(side == 2) {
				d11 = (double)z + block15.minZ - (double)0.1F;
			}

			if(side == 3) {
				d11 = (double)z + block15.maxZ + (double)0.1F;
			}

			if(side == 4) {
				d7 = (double)x + block15.minX - (double)0.1F;
			}

			if(side == 5) {
				d7 = (double)x + block15.maxX + (double)0.1F;
			}

			EntityDiggingFX entityDiggingFX10001 = new EntityDiggingFX(this.worldObj, d7, d9, d11, 0.0D, 0.0D, 0.0D, block15);
			float x1 = 0.2F;
			EntityDiggingFX x2 = entityDiggingFX10001;
			entityDiggingFX10001.motionX *= (double)0.2F;
			x2.motionY = (x2.motionY - (double)0.1F) * (double)0.2F + (double)0.1F;
			x2.motionZ *= (double)0.2F;
			this.addEffect(x2.multiplyParticleScaleBy(0.6F));
		}
	}

	public final String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}