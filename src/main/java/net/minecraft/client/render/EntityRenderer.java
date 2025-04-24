package net.minecraft.client.render;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class EntityRenderer {
	public Minecraft mc;
	public float fogColorMultiplier = 1.0F;
	public boolean displayActive = false;
	public float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	public int entityRendererInt1;
	private Entity pointedEntity = null;
	public ByteBuffer entityByteBuffer;
	public FloatBuffer entityFloatBuffer = BufferUtils.createFloatBuffer(16);
	private EaglercraftRandom random = new EaglercraftRandom();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	public float prevFogColor;
	public float fogColor;

	public EntityRenderer(Minecraft var1) {
		this.mc = var1;
		this.itemRenderer = new ItemRenderer(var1);
	}

	public final Vec3D orientCamera(float var1) {
		EntityPlayerSP var4 = this.mc.thePlayer;
		float var2 = var4.prevPosX + (var4.posX - var4.prevPosX) * var1;
		float var3 = var4.prevPosY + (var4.posY - var4.prevPosY) * var1;
		float var5 = var4.prevPosZ + (var4.posZ - var4.prevPosZ) * var1;
		return new Vec3D(var2, var3, var5);
	}

	private void hurtCameraEffect(float var1) {
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var2 = (float)var3.hurtTime - var1;
		if(var3.health <= 0) {
			var1 += (float)var3.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(var2 >= 0.0F) {
			var2 = MathHelper.sin((var2 /= (float)var3.maxHurtTime) * var2 * var2 * var2 * (float)Math.PI);
			var1 = var3.attackedAtYaw;
            if(Float.isNaN(var2)) {
                var2 = 0.0F;
            }

			GL11.glRotatef(-var1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var2 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float var1) {
		EntityPlayerSP var4 = this.mc.thePlayer;
		float var2 = var4.distanceWalkedModified - var4.prevDistanceWalkedModified;
		var2 = var4.distanceWalkedModified + var2 * var1;
		float var3 = var4.prevCameraYaw + (var4.cameraYaw - var4.prevCameraYaw) * var1;
		float var5 = var4.prevCameraPitch + (var4.cameraPitch - var4.prevCameraPitch) * var1;
		GL11.glTranslatef(MathHelper.sin(var2 * (float)Math.PI) * var3 * 0.5F, -Math.abs(MathHelper.cos(var2 * (float)Math.PI) * var3), 0.0F);
		GL11.glRotatef(MathHelper.sin(var2 * (float)Math.PI) * var3 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(Math.abs(MathHelper.cos(var2 * (float)Math.PI + 0.2F) * var3) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
	}

	public static ImageData screenshotBuffer(ByteBuffer var0, int var1, int var2) {
		var0.position(0).limit(var1 * var2 << 2);
		ImageData var3 = new ImageData(var1, var2, true);
		int[] var4 = var3.pixels;

		for(int var5 = 0; var5 < var1 * var2; ++var5) {
			int var6 = var0.get(var5 * 3) & 255;
			int var7 = var0.get(var5 * 3 + 1) & 255;
			int var8 = var0.get(var5 * 3 + 2) & 255;
			var4[var5] = var6 << 16 | var7 << 8 | var8;
		}

		return var3;
	}

	public final void updateCameraAndRender(float var1) {
		EntityRenderer var15 = this;
		EntityPlayerSP var17 = this.mc.thePlayer;
		float var18 = var17.prevRotationPitch + (var17.rotationPitch - var17.prevRotationPitch) * var1;
		float var19 = var17.prevRotationYaw + (var17.rotationYaw - var17.prevRotationYaw) * var1;
		Vec3D var7 = this.orientCamera(var1);
		float var8 = MathHelper.cos(-var19 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		float var9 = MathHelper.sin(-var19 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		float var10 = MathHelper.cos(-var18 * ((float)Math.PI / 180.0F));
		float var11 = MathHelper.sin(-var18 * ((float)Math.PI / 180.0F));
		float var4 = var9 * var10;
		float var12 = var8 * var10;
		float var13 = this.mc.playerController.getBlockReachDistance();
		Vec3D var30 = var7.addVector(var4 * var13, var11 * var13, var12 * var13);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var7, var30);
		var10 = var13;
		if(this.mc.objectMouseOver != null) {
			var10 = this.mc.objectMouseOver.hitVec.distanceTo(var7);
		}

		var7 = this.orientCamera(var1);
		if(this.mc.playerController instanceof PlayerControllerCreative) {
			var13 = 32.0F;
		} else {
			var13 = var10;
		}

		var30 = var7.addVector(var4 * var13, var11 * var13, var12 * var13);
		this.pointedEntity = null;
		List var3 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(var17, var17.boundingBox.addCoord(var4 * var13, var11 * var13, var12 * var13));
		float var5 = 0.0F;

		int var6;
		Vec3D var42;
		for(var6 = 0; var6 < var3.size(); ++var6) {
			Entity var2 = (Entity)var3.get(var6);
			if(var2.canBeCollidedWith()) {
				var4 = 0.1F;
				AxisAlignedBB var10000 = var2.boundingBox.expand(var4, var4, var4);
				AxisAlignedBB var23 = null;
				var23 = var10000;
				Vec3D var40 = var7.getIntermediateWithXValue(var30, var23.x0);
				var42 = var7.getIntermediateWithXValue(var30, var23.x1);
				Vec3D var49 = var7.getIntermediateWithYValue(var30, var23.y0);
				Vec3D var14 = var7.getIntermediateWithYValue(var30, var23.y1);
				Vec3D var16 = var7.getIntermediateWithZValue(var30, var23.z0);
				Vec3D var41 = var7.getIntermediateWithZValue(var30, var23.z1);
				if(!var23.isVecInYZ(var40)) {
					var40 = null;
				}

				if(!var23.isVecInYZ(var42)) {
					var42 = null;
				}

				if(!var23.isVecInXZ(var49)) {
					var49 = null;
				}

				if(!var23.isVecInXZ(var14)) {
					var14 = null;
				}

				if(!var23.isVecInXY(var16)) {
					var16 = null;
				}

				if(!var23.isVecInXY(var41)) {
					var41 = null;
				}

				Vec3D var24 = null;
				if(var40 != null) {
					var24 = var40;
				}

				if(var42 != null && (var24 == null || var7.squaredDistanceTo(var42) < var7.squaredDistanceTo(var24))) {
					var24 = var42;
				}

				if(var49 != null && (var24 == null || var7.squaredDistanceTo(var49) < var7.squaredDistanceTo(var24))) {
					var24 = var49;
				}

				if(var14 != null && (var24 == null || var7.squaredDistanceTo(var14) < var7.squaredDistanceTo(var24))) {
					var24 = var14;
				}

				if(var16 != null && (var24 == null || var7.squaredDistanceTo(var16) < var7.squaredDistanceTo(var24))) {
					var24 = var16;
				}

				if(var41 != null && (var24 == null || var7.squaredDistanceTo(var41) < var7.squaredDistanceTo(var24))) {
					var24 = var41;
				}

				MovingObjectPosition var67;
				if(var24 == null) {
					var67 = null;
				} else {
					byte var34 = -1;
					if(var24 == var40) {
						var34 = 4;
					}

					if(var24 == var42) {
						var34 = 5;
					}

					if(var24 == var49) {
						var34 = 0;
					}

					if(var24 == var14) {
						var34 = 1;
					}

					if(var24 == var16) {
						var34 = 2;
					}

					if(var24 == var41) {
						var34 = 3;
					}

					var67 = new MovingObjectPosition(0, 0, 0, var34, var24);
				}

				MovingObjectPosition var37 = var67;
				if(var37 != null) {
					var4 = var7.distanceTo(var37.hitVec);
					if(var4 < var5 || var5 == 0.0F) {
						var15.pointedEntity = var2;
						var5 = var4;
					}
				}
			}
		}

		if(var15.pointedEntity != null && !(var15.mc.playerController instanceof PlayerControllerCreative)) {
			var15.mc.objectMouseOver = new MovingObjectPosition(var15.pointedEntity);
		}

		for(int var20 = 0; var20 < 2; ++var20) {
			if(this.mc.options.anaglyph) {
				if(var20 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayerSP var21 = this.mc.thePlayer;
			World var38 = this.mc.theWorld;
			RenderGlobal var25 = this.mc.renderGlobal;
			EffectRenderer var26 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(var1);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			this.fogColorMultiplier = 1.0F;
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			var18 = 0.07F;
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)(-((var20 << 1) - 1)) * var18, 0.0F, 0.0F);
			}

			Tessellator var63 = null;
			EntityPlayerSP var32 = this.mc.thePlayer;
			var9 = 70.0F;
			if(var32.health <= 0) {
				var10 = (float)var32.deathTime + var1;
				var9 /= (1.0F - 500.0F / (var10 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(var9, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var20 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			var32 = this.mc.thePlayer;
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			GL11.glRotatef(var32.prevRotationPitch + (var32.rotationPitch - var32.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var32.prevRotationYaw + (var32.rotationYaw - var32.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
			var9 = var32.prevPosX + (var32.posX - var32.prevPosX) * var1;
			var10 = var32.prevPosY + (var32.posY - var32.prevPosY) * var1;
			var11 = var32.prevPosZ + (var32.posZ - var32.prevPosZ) * var1;
			GL11.glTranslatef(-var9, -var10, -var11);
			ClippingHelper var27 = ClippingHelperImplementation.init();
			this.mc.renderGlobal.clipRenderersByFrustrum(var27);
			this.mc.renderGlobal.updateRenderers(var21);
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			var25.sortAndRender(var21, 0);
			int var36;
			int var45;
			int var48;
			int var52;
			if(var38.isSolid(var21.posX, var21.posY, var21.posZ, 0.1F)) {
				var36 = (int)var21.posX;
				int var47 = (int)var21.posY;
				var45 = (int)var21.posZ;
				RenderBlocks var44 = new RenderBlocks(Tessellator.instance, var38);

				for(var48 = var36 - 1; var48 <= var36 + 1; ++var48) {
					for(var52 = var47 - 1; var52 <= var47 + 1; ++var52) {
						for(int var51 = var45 - 1; var51 <= var45 + 1; ++var51) {
							int var53 = var38.getBlockId(var48, var52, var51);
							if(var53 > 0) {
								Block var58 = Block.blocksList[var53];
								var44.flipTexture = true;
								var44.renderBlockByRenderType(var58, var48, var52, var51);
								var44.flipTexture = false;
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			var25.renderEntities(this.orientCamera(var1), var27, var1);
			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			float var59 = var1;
			EffectRenderer var56 = var26;
			var18 = -MathHelper.cos(var21.rotationYaw * (float)Math.PI / 180.0F);
			var19 = -MathHelper.sin(var21.rotationYaw * (float)Math.PI / 180.0F);
			float var29 = -var19 * MathHelper.sin(var21.rotationPitch * (float)Math.PI / 180.0F);
			var8 = var18 * MathHelper.sin(var21.rotationPitch * (float)Math.PI / 180.0F);
			var9 = MathHelper.cos(var21.rotationPitch * (float)Math.PI / 180.0F);

			Tessellator var39;
			int var46;
			for(var45 = 0; var45 < 2; ++var45) {
				if(var56.fxLayers[var45].size() != 0) {
					var46 = 0;
					if(var45 == 0) {
						var46 = var56.renderEngine.getTexture("/particles.png");
					}

					if(var45 == 1) {
						var46 = var56.renderEngine.getTexture("/terrain.png");
					}

					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var46);
					var39 = Tessellator.instance;
					var39.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

					for(var6 = 0; var6 < var56.fxLayers[var45].size(); ++var6) {
						EntityFX var68 = (EntityFX)var56.fxLayers[var45].get(var6);
						var42 = null;
						var68.renderParticle(var39, var59, var18, var9, var19, var29, var8);
					}

					var39.draw();
				}
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var25.renderEngine.getTexture("/rock.png"));
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glCallList(var25.glGenList);
			this.setupFog();
			RenderGlobal var57 = var25;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var25.renderEngine.getTexture("/clouds.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var59 = (float)(var25.worldObj.cloudColor >> 16 & 255) / 255.0F;
			var18 = (float)(var25.worldObj.cloudColor >> 8 & 255) / 255.0F;
			var19 = (float)(var25.worldObj.cloudColor & 255) / 255.0F;
			if(var25.mc.options.anaglyph) {
				var29 = (var59 * 30.0F + var18 * 59.0F + var19 * 11.0F) / 100.0F;
				var8 = (var59 * 30.0F + var18 * 70.0F) / 100.0F;
				var9 = (var59 * 30.0F + var19 * 70.0F) / 100.0F;
				var59 = var29;
				var18 = var8;
				var19 = var9;
			}

			Tessellator var31 = Tessellator.instance;
			var10 = 0.0F;
			var11 = 0.5F / 1024.0F;
			var10 = (float)(var25.worldObj.height + 2);
			var4 = ((float)var25.cloudOffsetX + var1) * var11 * 0.03F;
			float var28 = 0.0F;
			var31.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
			var31.setColorOpaque_F(var59, var18, var19);

			for(var48 = -2048; var48 < var57.worldObj.width + 2048; var48 += 512) {
				for(var52 = -2048; var52 < var57.worldObj.length + 2048; var52 += 512) {
					var31.addVertexWithUV((float)var48, var10, (float)(var52 + 512), (float)var48 * var11 + var4, (float)(var52 + 512) * var11);
					var31.addVertexWithUV((float)(var48 + 512), var10, (float)(var52 + 512), (float)(var48 + 512) * var11 + var4, (float)(var52 + 512) * var11);
					var31.addVertexWithUV((float)(var48 + 512), var10, (float)var52, (float)(var48 + 512) * var11 + var4, (float)var52 * var11);
					var31.addVertexWithUV((float)var48, var10, (float)var52, (float)var48 * var11 + var4, (float)var52 * var11);
					var31.addVertexWithUV((float)var48, var10, (float)var52, (float)var48 * var11 + var4, (float)var52 * var11);
					var31.addVertexWithUV((float)(var48 + 512), var10, (float)var52, (float)(var48 + 512) * var11 + var4, (float)var52 * var11);
					var31.addVertexWithUV((float)(var48 + 512), var10, (float)(var52 + 512), (float)(var48 + 512) * var11 + var4, (float)(var52 + 512) * var11);
					var31.addVertexWithUV((float)var48, var10, (float)(var52 + 512), (float)var48 * var11 + var4, (float)(var52 + 512) * var11);
				}
			}

			var31.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var31.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
			var4 = (float)(var57.worldObj.skyColor >> 16 & 255) / 255.0F;
			var28 = (float)(var57.worldObj.skyColor >> 8 & 255) / 255.0F;
			var12 = (float)(var57.worldObj.skyColor & 255) / 255.0F;
			if(var57.mc.options.anaglyph) {
				var13 = (var4 * 30.0F + var28 * 59.0F + var12 * 11.0F) / 100.0F;
				var8 = (var4 * 30.0F + var28 * 70.0F) / 100.0F;
				var10 = (var4 * 30.0F + var12 * 70.0F) / 100.0F;
				var4 = var13;
				var28 = var8;
				var12 = var10;
			}

			var31.setColorOpaque_F(var4, var28, var12);
			var10 = (float)(var57.worldObj.height + 10);

			for(var52 = -2048; var52 < var57.worldObj.width + 2048; var52 += 512) {
				for(var36 = -2048; var36 < var57.worldObj.length + 2048; var36 += 512) {
					var31.addVertex((float)var52, var10, (float)var36);
					var31.addVertex((float)(var52 + 512), var10, (float)var36);
					var31.addVertex((float)(var52 + 512), var10, (float)(var36 + 512));
					var31.addVertex((float)var52, var10, (float)(var36 + 512));
				}
			}

			var31.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.setupFog();
			int var33;
			int var43;
			World var64;
			int var66;
			if(this.mc.objectMouseOver != null) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var64 = null;
				boolean var62 = false;
				MovingObjectPosition var60 = this.mc.objectMouseOver;
				var63 = Tessellator.instance;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
				if(var25.damagePartialTime > 0.0F) {
					GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
					var33 = var25.renderEngine.getTexture("/terrain.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var33);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
					GL11.glPushMatrix();
					var36 = var25.worldObj.getBlockId(var60.blockX, var60.blockY, var60.blockZ);
					Block var50 = var36 > 0 ? Block.blocksList[var36] : null;
					var63.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
					var63.disableColor();
					if(var50 == null) {
						var50 = Block.stone;
					}

					var52 = 240 + (int)(var25.damagePartialTime * 10.0F);
					var48 = var60.blockZ;
					var6 = var60.blockY;
					var43 = var60.blockX;
					RenderBlocks var54 = var25.globalRenderBlocks;
					var54.overrideBlockTexture = var52;
					var54.renderBlockByRenderType(var50, var43, var6, var48);
					var54.overrideBlockTexture = -1;
					var63.draw();
					GL11.glDepthMask(true);
					GL11.glPopMatrix();
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var60 = this.mc.objectMouseOver;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
				GL11.glLineWidth(2.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDepthMask(false);
				var18 = 0.002F;
				var66 = var25.worldObj.getBlockId(var60.blockX, var60.blockY, var60.blockZ);
				if(var66 > 0) {
					AxisAlignedBB var35 = Block.blocksList[var66].getSelectedBoundingBoxFromPool(var60.blockX, var60.blockY, var60.blockZ).expand(var18, var18, var18);
					GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var35.x0, var35.y0, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y0, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y0, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y0, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y0, var35.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var35.x0, var35.y1, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y1, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y1, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y1, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y1, var35.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var35.x0, var35.y0, var35.z0);
					GL11.glVertex3f(var35.x0, var35.y1, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y0, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y1, var35.z0);
					GL11.glVertex3f(var35.x1, var35.y0, var35.z1);
					GL11.glVertex3f(var35.x1, var35.y1, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y0, var35.z1);
					GL11.glVertex3f(var35.x0, var35.y1, var35.z1);
					GL11.glEnd();
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var25.renderEngine.getTexture("/water.png"));
			GL11.glCallList(var25.glGenList + 1);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColorMask(false, false, false, false);
			var36 = var25.sortAndRender(var21, 1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.options.anaglyph) {
				if(var20 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(var36 > 0) {
				var25.renderAllRenderLists();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);
			float var22;
			if(this.mc.thirdPersonView) {
				float var61 = var1;
				var15 = this;
				var17 = this.mc.thePlayer;
				var64 = this.mc.theWorld;
				var66 = (int)var17.posX;
				var33 = (int)var17.posY;
				var36 = (int)var17.posZ;
				Tessellator var55 = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
				var46 = var66 - 5;

				while(true) {
					if(var46 > var66 + 5) {
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						break;
					}

					for(var43 = var36 - 5; var43 <= var36 + 5; ++var43) {
						var6 = var64.getMapHeight(var46, var43);
						var48 = var33 - 5;
						var52 = var33 + 5;
						if(var48 < var6) {
							var48 = var6;
						}

						if(var52 < var6) {
							var52 = var6;
						}

						if(var48 != var52) {
							var10 = ((float)((var15.entityRendererInt1 + var46 * 3121 + var43 * 418711) % 32) + var61) / 32.0F;
							var22 = (float)var46 + 0.5F - var17.posX;
							var5 = (float)var43 + 0.5F - var17.posZ;
							var28 = MathHelper.sqrt_float(var22 * var22 + var5 * var5) / (float)5;
							GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var28 * var28) * 0.7F);
							var55.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
							var55.addVertexWithUV((float)var46, (float)var48, (float)var43, 0.0F, (float)var48 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)(var46 + 1), (float)var48, (float)(var43 + 1), 2.0F, (float)var48 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)(var46 + 1), (float)var52, (float)(var43 + 1), 2.0F, (float)var52 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)var46, (float)var52, (float)var43, 0.0F, (float)var52 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)var46, (float)var48, (float)(var43 + 1), 0.0F, (float)var48 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)(var46 + 1), (float)var48, (float)var43, 2.0F, (float)var48 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)(var46 + 1), (float)var52, (float)var43, 2.0F, (float)var52 * 2.0F / 8.0F + var10 * 2.0F);
							var55.addVertexWithUV((float)var46, (float)var52, (float)(var43 + 1), 0.0F, (float)var52 * 2.0F / 8.0F + var10 * 2.0F);
							var55.draw();
						}
					}

					++var46;
				}
			}

			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var20 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			ItemRenderer var65 = this.itemRenderer;
			var29 = var65.prevEquippedProgress + (var65.equippedProgress - var65.prevEquippedProgress) * var1;
			var32 = var65.mc.thePlayer;
			GL11.glPushMatrix();
			GL11.glRotatef(var32.prevRotationPitch + (var32.rotationPitch - var32.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var32.prevRotationYaw + (var32.rotationYaw - var32.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			var9 = 0.8F;
			float var69;
			if(var65.itemSwingState) {
				var10 = ((float)var65.swingProgress + var1) / 7.0F;
				var11 = MathHelper.sin(var10 * (float)Math.PI);
				var69 = MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI);
				var4 = 0.0F;
				GL11.glTranslatef(-var69 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI * 2.0F) * 0.2F, -var11 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var9, -0.65F * var9 - (1.0F - var29) * 0.6F, -0.9F * var9);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(var65.itemSwingState) {
				var11 = MathHelper.sin((var10 = ((float)var65.swingProgress + var1) / 7.0F) * var10 * (float)Math.PI);
				var69 = MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI);
				var4 = 0.0F;
				GL11.glRotatef(var69 * 80.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-var11 * 20.0F, 1.0F, 0.0F, 0.0F);
			}

			GL11.glColor4f(var10 = var65.mc.theWorld.getBlockLightValue((int)var32.posX, (int)var32.posY, (int)var32.posZ), var10, var10, 1.0F);
			if(var65.itemToRender != null) {
				var11 = 0.4F;
				GL11.glScalef(0.4F, var11, var11);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var65.mc.renderEngine.getTexture("/terrain.png"));
				var3 = null;
				if(var65.itemToRender.itemID > 0) {
					var65.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[var65.itemToRender.itemID]);
				} else {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var65.mc.renderEngine.getTexture("/gui/items.png"));
					GL11.glDisable(GL11.GL_LIGHTING);
					var39 = Tessellator.instance;
					var28 = (float)(var65.itemToRender.iconIndex % 16 << 4) / 256.0F;
					var12 = (float)((var65.itemToRender.iconIndex % 16 << 4) + 16) / 256.0F;
					var13 = (float)(var65.itemToRender.iconIndex / 16 << 4) / 256.0F;
					var8 = (float)((var65.itemToRender.iconIndex / 16 << 4) + 16) / 256.0F;
					var10 = 0.7F;
					var22 = 0.4F;
					var5 = 0.2F;
					var39.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
					var39.addVertexWithUV(0.0F - var22, 0.0F - var5, 0.0F - var22, var28, var8);
					var39.addVertexWithUV(var10 - var22, 0.0F - var5, var10 - var22, var12, var8);
					var39.addVertexWithUV(var10 - var22, 1.0F - var5, var10 - var22, var12, var13);
					var39.addVertexWithUV(0.0F - var22, 1.0F - var5, 0.0F - var22, var28, var13);
					var39.draw();
					GL11.glEnable(GL11.GL_LIGHTING);
				}
			} else {
				GL11.glScalef(1.0F, -1.0F, -1.0F);
				GL11.glTranslatef(0.0F, 0.2F, 0.0F);
				GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				var11 = 0.0F;
			}

			GL11.glDisable(GL11.GL_NORMALIZE);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			if(!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void addRainParticles() {
		EntityPlayerSP var1 = this.mc.thePlayer;
		World var2 = this.mc.theWorld;
		int var3 = (int)var1.posX;
		int var4 = (int)var1.posY;
		int var12 = (int)var1.posZ;

		for(int var5 = 0; var5 < 50; ++var5) {
			int var6 = var3 + this.random.nextInt(9) - 4;
			int var7 = var12 + this.random.nextInt(9) - 4;
			int var8 = var2.getMapHeight(var6, var7);
			int var9 = var2.getBlockId(var6, var8 - 1, var7);
			if(var8 <= var4 + 4 && var8 >= var4 - 4) {
				float var10 = this.random.nextFloat();
				float var11 = this.random.nextFloat();
				if(var9 > 0) {
					this.mc.effectRenderer.addEffect(new EntityRainFX(var2, (float)var6 + var10, (float)var8 + 0.1F - Block.blocksList[var9].minY, (float)var7 + var11));
				}
			}
		}

	}

	public final void setupOverlayRendering() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.mc.scaledResolution.getScaledWidth_double(), this.mc.scaledResolution.getScaledHeight_double(), 
                0.0D, 100.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	public final void updateFogColor(float var1) {
		World var2 = this.mc.theWorld;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = 1.0F / (float)(4 - this.mc.options.renderDistance);
		var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
		float var5 = (float)(var2.skyColor >> 16 & 255) / 255.0F;
		float var6 = (float)(var2.skyColor >> 8 & 255) / 255.0F;
		float var7 = (float)(var2.skyColor & 255) / 255.0F;
		this.fogColorRed = (float)(var2.fogColor >> 16 & 255) / 255.0F;
		this.fogColorGreen = (float)(var2.fogColor >> 8 & 255) / 255.0F;
		this.fogColorBlue = (float)(var2.fogColor & 255) / 255.0F;
		this.fogColorRed += (var5 - this.fogColorRed) * var4;
		this.fogColorGreen += (var6 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var7 - this.fogColorBlue) * var4;
		this.fogColorRed *= this.fogColorMultiplier;
		this.fogColorGreen *= this.fogColorMultiplier;
		this.fogColorBlue *= this.fogColorMultiplier;
		Block var8 = Block.blocksList[var2.getBlockId((int)var3.posX, (int)(var3.posY + 0.12F), (int)var3.posZ)];
		if(var8 != null && var8.getMaterial() != Material.air) {
			Material var9 = var8.getMaterial();
			if(var9 == Material.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if(var9 == Material.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		float var10 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
		this.fogColorRed *= var10;
		this.fogColorGreen *= var10;
		this.fogColorBlue *= var10;
		if(this.mc.options.anaglyph) {
			var1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			var10 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var11 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var1;
			this.fogColorGreen = var10;
			this.fogColorBlue = var11;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	public final void setupFog() {
		World var1 = this.mc.theWorld;
		EntityPlayerSP var2 = this.mc.thePlayer;
		int var10000 = GL11.GL_FOG_COLOR;
		float var3 = 0.0F;
		float var6 = this.fogColorBlue;
		float var5 = this.fogColorGreen;
		float var4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(var4).put(var5).put(var6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(var10000, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block var8 = Block.blocksList[var1.getBlockId((int)var2.posX, (int)(var2.posY + 0.12F), (int)var2.posZ)];
		if(var8 != null && var8.getMaterial() != Material.air) {
			Material var7 = var8.getMaterial();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(var7 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if(var7 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}
