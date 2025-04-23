package net.minecraft.client.render;

import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class EntityRenderer {
	public Minecraft mc;
	private float fogColorMultiplier = 1.0F;
	public boolean displayActive = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	public int entityRendererInt1;
	private Entity pointedEntity = null;
	public EaglercraftRandom random = new EaglercraftRandom();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;

	public EntityRenderer(Minecraft var1) {
		this.mc = var1;
		this.itemRenderer = new ItemRenderer(var1);
	}

	private Vec3D orientCamera(float var1) {
		EntityPlayer var4 = this.mc.thePlayer;
		float var2 = var4.prevPosX + (var4.posX - var4.prevPosX) * var1;
		float var3 = var4.prevPosY + (var4.posY - var4.prevPosY) * var1;
		float var5 = var4.prevPosZ + (var4.posZ - var4.prevPosZ) * var1;
		return new Vec3D(var2, var3, var5);
	}

	private void hurtCameraEffect(float var1) {
		EntityPlayer var3 = this.mc.thePlayer;
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
		EntityPlayer var4 = this.mc.thePlayer;
		float var2 = var4.distanceWalkedModified - var4.prevDistanceWalkedModified;
		var2 = var4.distanceWalkedModified + var2 * var1;
		float var3 = var4.prevCameraYaw + (var4.cameraYaw - var4.prevCameraYaw) * var1;
		float var5 = var4.prevCameraPitch + (var4.cameraPitch - var4.prevCameraPitch) * var1;
		GL11.glTranslatef(MathHelper.sin(var2 * (float)Math.PI) * var3 * 0.5F, -Math.abs(MathHelper.cos(var2 * (float)Math.PI) * var3), 0.0F);
		GL11.glRotatef(MathHelper.sin(var2 * (float)Math.PI) * var3 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(Math.abs(MathHelper.cos(var2 * (float)Math.PI + 0.2F) * var3) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
	}

	public final void updateCameraAndRender(float var1) {
		EntityRenderer var15 = this;
		EntityPlayer var17 = this.mc.thePlayer;
		float var18 = var17.prevRotationPitch + (var17.rotationPitch - var17.prevRotationPitch) * var1;
		float var19 = var17.prevRotationYaw + (var17.rotationYaw - var17.prevRotationYaw) * var1;
		Vec3D var8 = this.orientCamera(var1);
		float var7 = MathHelper.cos(-var19 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		float var9 = MathHelper.sin(-var19 * ((float)Math.PI / 180.0F) - (float)Math.PI);
		float var10 = MathHelper.cos(-var18 * ((float)Math.PI / 180.0F));
		float var11 = MathHelper.sin(-var18 * ((float)Math.PI / 180.0F));
		float var12 = var9 * var10;
		float var13 = var7 * var10;
		float var14 = this.mc.playerController.getBlockReachDistance();
		Vec3D var20 = var8.addVector(var12 * var14, var11 * var14, var13 * var14);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(var8, var20);
		float var21 = var14;
		if(this.mc.objectMouseOver != null) {
			var21 = this.mc.objectMouseOver.hitVec.distanceTo(var8);
		}

		var8 = this.orientCamera(var1);
		if(this.mc.playerController instanceof PlayerControllerCreative) {
			var14 = 32.0F;
		} else {
			var14 = var21;
		}

		var20 = var8.addVector(var12 * var14, var11 * var14, var13 * var14);
		this.pointedEntity = null;
		List var22 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(var17, var17.boundingBox.addCoord(var12 * var14, var11 * var14, var13 * var14));
		float var23 = 0.0F;

		float var109;
		for(int var24 = 0; var24 < var22.size(); ++var24) {
			Entity var70 = (Entity)var22.get(var24);
			if(var70.canBeCollidedWith()) {
				var21 = 0.1F;
				AxisAlignedBB var10000 = var70.boundingBox.expand(var21, var21, var21);
				Vec3D var25 = null;
				AxisAlignedBB var28 = var10000;
				Vec3D var93 = var8.getIntermediateWithXValue(var20, var28.x0);
				Vec3D var26 = var8.getIntermediateWithXValue(var20, var28.x1);
				var25 = var8.getIntermediateWithYValue(var20, var28.y0);
				Vec3D var27 = var8.getIntermediateWithYValue(var20, var28.y1);
				Vec3D var31 = var8.getIntermediateWithZValue(var20, var28.z0);
				Vec3D var30 = var8.getIntermediateWithZValue(var20, var28.z1);
				if(!var28.isVecInYZ(var93)) {
					var93 = null;
				}

				if(!var28.isVecInYZ(var26)) {
					var26 = null;
				}

				if(!var28.isVecInXZ(var25)) {
					var25 = null;
				}

				if(!var28.isVecInXZ(var27)) {
					var27 = null;
				}

				if(!var28.isVecInXY(var31)) {
					var31 = null;
				}

				if(!var28.isVecInXY(var30)) {
					var30 = null;
				}

				Vec3D var111 = null;
				if(var93 != null) {
					var111 = var93;
				}

				if(var26 != null && (var111 == null || var8.squaredDistanceTo(var26) < var8.squaredDistanceTo(var111))) {
					var111 = var26;
				}

				if(var25 != null && (var111 == null || var8.squaredDistanceTo(var25) < var8.squaredDistanceTo(var111))) {
					var111 = var25;
				}

				if(var27 != null && (var111 == null || var8.squaredDistanceTo(var27) < var8.squaredDistanceTo(var111))) {
					var111 = var27;
				}

				if(var31 != null && (var111 == null || var8.squaredDistanceTo(var31) < var8.squaredDistanceTo(var111))) {
					var111 = var31;
				}

				if(var30 != null && (var111 == null || var8.squaredDistanceTo(var30) < var8.squaredDistanceTo(var111))) {
					var111 = var30;
				}

				MovingObjectPosition var123;
				if(var111 == null) {
					var123 = null;
				} else {
					byte var32 = -1;
					if(var111 == var93) {
						var32 = 4;
					}

					if(var111 == var26) {
						var32 = 5;
					}

					if(var111 == var25) {
						var32 = 0;
					}

					if(var111 == var27) {
						var32 = 1;
					}

					if(var111 == var31) {
						var32 = 2;
					}

					if(var111 == var30) {
						var32 = 3;
					}

					var123 = new MovingObjectPosition(0, 0, 0, var32, var111);
				}

				MovingObjectPosition var106 = var123;
				if(var106 != null) {
					var109 = var8.distanceTo(var106.hitVec);
					if(var109 < var23 || var23 == 0.0F) {
						var15.pointedEntity = var70;
						var23 = var109;
					}
				}
			}
		}

		if(var15.pointedEntity != null && !(var15.mc.playerController instanceof PlayerControllerCreative)) {
			var15.mc.objectMouseOver = new MovingObjectPosition(var15.pointedEntity);
		}

		for(int var2 = 0; var2 < 2; ++var2) {
			if(this.mc.options.anaglyph) {
				if(var2 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayer var3 = this.mc.thePlayer;
			World var4 = this.mc.theWorld;
			RenderGlobal var5 = this.mc.renderGlobal;
			EffectRenderer var6 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			World var16 = this.mc.theWorld;
			var17 = this.mc.thePlayer;
			var18 = 1.0F / (float)(4 - this.mc.options.renderDistance);
			var18 = 1.0F - (float)Math.pow((double)var18, 0.25D);
			var19 = (float)(var16.skyColor >> 16 & 255) / 255.0F;
			float var52 = (float)(var16.skyColor >> 8 & 255) / 255.0F;
			var7 = (float)(var16.skyColor & 255) / 255.0F;
			this.fogColorRed = (float)(var16.fogColor >> 16 & 255) / 255.0F;
			this.fogColorGreen = (float)(var16.fogColor >> 8 & 255) / 255.0F;
			this.fogColorBlue = (float)(var16.fogColor & 255) / 255.0F;
			this.fogColorRed += (var19 - this.fogColorRed) * var18;
			this.fogColorGreen += (var52 - this.fogColorGreen) * var18;
			this.fogColorBlue += (var7 - this.fogColorBlue) * var18;
			this.fogColorRed *= this.fogColorMultiplier;
			this.fogColorGreen *= this.fogColorMultiplier;
			this.fogColorBlue *= this.fogColorMultiplier;
			Block var54 = Block.blocksList[var16.getBlockId((int)var17.posX, (int)(var17.posY + 0.12F), (int)var17.posZ)];
			if(var54 != null && var54.getMaterial() != Material.air) {
				Material var57 = var54.getMaterial();
				if(var57 == Material.water) {
					this.fogColorRed = 0.02F;
					this.fogColorGreen = 0.02F;
					this.fogColorBlue = 0.2F;
				} else if(var57 == Material.lava) {
					this.fogColorRed = 0.6F;
					this.fogColorGreen = 0.1F;
					this.fogColorBlue = 0.0F;
				}
			}

			if(this.mc.options.anaglyph) {
				var10 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
				var11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
				var12 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
				this.fogColorRed = var10;
				this.fogColorGreen = var11;
				this.fogColorBlue = var12;
			}

			GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			this.fogColorMultiplier = 1.0F;
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			var18 = 0.07F;
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)(-((var2 << 1) - 1)) * var18, 0.0F, 0.0F);
			}

			EntityMap var86 = null;
			EntityPlayer var50 = this.mc.thePlayer;
			var9 = 70.0F;
			if(var50.health <= 0) {
				var10 = (float)var50.deathTime + var1;
				var9 /= (1.0F - 500.0F / (var10 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(var9, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			var50 = this.mc.thePlayer;
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			GL11.glRotatef(var50.prevRotationPitch + (var50.rotationPitch - var50.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var50.prevRotationYaw + (var50.rotationYaw - var50.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
			var9 = var50.prevPosX + (var50.posX - var50.prevPosX) * var1;
			var10 = var50.prevPosY + (var50.posY - var50.prevPosY) * var1;
			var11 = var50.prevPosZ + (var50.posZ - var50.prevPosZ) * var1;
			GL11.glTranslatef(-var9, -var10, -var11);
			ClippingHelper var51 = ClippingHelperImplementation.init();
			ClippingHelper var74 = var51;
			RenderGlobal var71 = this.mc.renderGlobal;

			int var81;
			for(var81 = 0; var81 < var71.worldRenderers.length; ++var81) {
				var71.worldRenderers[var81].updateInFrustrum(var74);
			}

			var71 = this.mc.renderGlobal;
			Collections.sort(var71.worldRenderersToUpdate, new RenderSorter(var3));
			var81 = var71.worldRenderersToUpdate.size() - 1;
			int var85 = var71.worldRenderersToUpdate.size();
			if(var85 > 3) {
				var85 = 3;
			}

			int var87;
			for(var87 = 0; var87 < var85; ++var87) {
				WorldRenderer var53 = (WorldRenderer)var71.worldRenderersToUpdate.remove(var81 - var87);
				var53.updateRenderer();
				var53.needsUpdate = false;
			}

			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			var5.sortAndRender(var3, 0);
			int var55;
			int var60;
			int var63;
			int var65;
			int var72;
			if(var4.isSolid(var3.posX, var3.posY, var3.posZ, 0.1F)) {
				var55 = (int)var3.posX;
				int var58 = (int)var3.posY;
				var60 = (int)var3.posZ;
				RenderBlocks var62 = new RenderBlocks(Tessellator.instance, var4);

				for(var63 = var55 - 1; var63 <= var55 + 1; ++var63) {
					for(var65 = var58 - 1; var65 <= var58 + 1; ++var65) {
						for(var72 = var60 - 1; var72 <= var60 + 1; ++var72) {
							int var73 = var4.getBlockId(var63, var65, var72);
							if(var73 > 0) {
								Block var76 = Block.blocksList[var73];
								var62.flipTexture = true;
								var62.renderBlockByRenderType(var76, var63, var65, var72);
								var62.flipTexture = false;
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			Vec3D var10001 = this.orientCamera(var1);
			var18 = var1;
			ClippingHelper var83 = var51;
			Vec3D var78 = var10001;
			var71 = var5;
			var86 = var5.worldObj.entityMap;

			int var48;
			List var68;
			float var89;
			float var104;
			for(var55 = 0; var55 < var86.xSlot; ++var55) {
				var7 = (float)((var55 << 4) - 2);
				var9 = (float)((var55 + 1 << 4) + 2);

				for(var60 = 0; var60 < var86.ySlot; ++var60) {
					var11 = (float)((var60 << 4) - 2);
					var12 = (float)((var60 + 1 << 4) + 2);

					for(var48 = 0; var48 < var86.zSlot; ++var48) {
						var68 = var86.entityGrid[(var48 * var86.ySlot + var60) * var86.xSlot + var55];
						if(var68.size() != 0) {
							var14 = (float)((var48 << 4) - 2);
							var89 = (float)((var48 + 1 << 4) + 2);
							boolean var124 = var83.isBoundingBoxInFrustrum(var7, var11, var14, var9, var12, var89);
							boolean var99 = false;
							if(var124) {
								float var29 = var89;
								float var117 = var12;
								var109 = var9;
								float var107 = var14;
								float var105 = var11;
								var21 = var7;
								ClippingHelper var77 = var83;
								int var113 = 0;

								while(true) {
									if(var113 >= 6) {
										var124 = true;
										break;
									}

									if(var77.frustrum[var113][0] * var21 + var77.frustrum[var113][1] * var105 + var77.frustrum[var113][2] * var107 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var109 + var77.frustrum[var113][1] * var105 + var77.frustrum[var113][2] * var107 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var21 + var77.frustrum[var113][1] * var117 + var77.frustrum[var113][2] * var107 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var109 + var77.frustrum[var113][1] * var117 + var77.frustrum[var113][2] * var107 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var21 + var77.frustrum[var113][1] * var105 + var77.frustrum[var113][2] * var29 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var109 + var77.frustrum[var113][1] * var105 + var77.frustrum[var113][2] * var29 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var21 + var77.frustrum[var113][1] * var117 + var77.frustrum[var113][2] * var29 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									if(var77.frustrum[var113][0] * var109 + var77.frustrum[var113][1] * var117 + var77.frustrum[var113][2] * var29 + var77.frustrum[var113][3] <= 0.0F) {
										var124 = false;
										break;
									}

									++var113;
								}

								boolean var94 = var124;

								for(int var100 = 0; var100 < var68.size(); ++var100) {
									Entity var101 = (Entity)var68.get(var100);
									var105 = var101.posX - var78.xCoord;
									var107 = var101.posY - var78.yCoord;
									var109 = var101.posZ - var78.zCoord;
									var117 = var105 * var105 + var107 * var107 + var109 * var109;
									Object var112 = null;
									AxisAlignedBB var110 = var101.boundingBox;
									var105 = var110.x1 - var110.x0;
									var109 = var110.y1 - var110.y0;
									float var115 = var110.z1 - var110.z0;
									float var125 = (var105 + var109 + var115) / 3.0F;
									var21 = 0.0F;
									var21 = var125 * 64.0F;
									if(var117 < var21 * var21) {
										if(!var94) {
											AxisAlignedBB var102 = var101.boundingBox;
											if(!var83.isBoundingBoxInFrustrum(var102.x0, var102.y0, var102.z0, var102.x1, var102.y1, var102.z1)) {
												continue;
											}
										}

										var107 = var18;
										RenderEngine var108 = var71.renderEngine;
										RenderManager var79 = var71.renderManager;
										if(!(var101 instanceof EntityPlayer)) {
											var109 = var101.lastTickPosX + (var101.posX - var101.lastTickPosX) * var18;
											var117 = var101.lastTickPosY + (var101.posY - var101.lastTickPosY) * var18;
											var29 = var101.lastTickPosZ + (var101.posZ - var101.lastTickPosZ) * var18;
											var115 = var29;
											float var114 = var117;
											var104 = var109;
											RenderManager var119 = var79;
											GL11.glEnable(GL11.GL_BLEND);
											Object var33 = null;
											var108.clampTexture = true;
											int var118 = var108.getTexture("/shadow.png");
											GL11.glBindTexture(GL11.GL_TEXTURE_2D, var118);
											var33 = null;
											var108.clampTexture = false;
											GL11.glDepthMask(false);
											var89 = 0.5F;

											for(var118 = (int)(var109 - var89); var118 <= (int)(var104 + var89); ++var118) {
												for(int var34 = (int)(var114 - 2.0F); var34 <= (int)var114; ++var34) {
													for(int var35 = (int)(var115 - var89); var35 <= (int)(var115 + var89); ++var35) {
														int var36 = var119.worldObj.getBlockId(var118, var34 - 1, var35);
														if(var36 > 0 && var119.worldObj.isHalfLit(var118, var34, var35)) {
															Block var121 = Block.blocksList[var36];
															Tessellator var44 = Tessellator.instance;
															float var38 = (1.0F - (var114 - (float)var34) / 2.0F) * 0.5F;
															if(var38 >= 0.0F) {
																GL11.glColor4f(1.0F, 1.0F, 1.0F, var38);
																var44.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
																var38 = (float)var118 + var121.minX;
																float var40 = (float)var118 + var121.maxX;
																float var41 = (float)var34 + var121.minY;
																float var45 = (float)var35 + var121.minZ;
																float var122 = (float)var35 + var121.maxZ;
																float var42 = (var104 - var38) / 2.0F / var89 + 0.5F;
																float var37 = (var104 - var40) / 2.0F / var89 + 0.5F;
																float var46 = (var115 - var45) / 2.0F / var89 + 0.5F;
																float var39 = (var115 - var122) / 2.0F / var89 + 0.5F;
																var44.addVertexWithUV(var38, var41, var45, var42, var46);
																var44.addVertexWithUV(var38, var41, var122, var42, var39);
																var44.addVertexWithUV(var40, var41, var122, var37, var39);
																var44.addVertexWithUV(var40, var41, var45, var37, var46);
																var44.draw();
															}
														}
													}
												}
											}

											GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
											GL11.glDisable(GL11.GL_BLEND);
											GL11.glDepthMask(true);
											if(var101 instanceof EntityLiving) {
												EntityLiving var116 = (EntityLiving)var101;
												GL11.glPushMatrix();

												try {
													var107 = var116.prevRenderYawOffset + (var116.renderYawOffset - var116.prevRenderYawOffset) * var107;
													GL11.glTranslatef(var109, var117, var29);
													var118 = var108.getTexture("/cube-nes.png");
													GL11.glBindTexture(GL11.GL_TEXTURE_2D, var118);
													GL11.glRotatef(-var107 + 180.0F, 0.0F, 1.0F, 0.0F);
													GL11.glColor3f(var114 = var79.worldObj.getBlockLightValue((int)var109, (int)var117, (int)var29), var114, var114);
													var117 = 0.02F;
													GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
													GL11.glScalef(var117, -var117, var117);
													GL11.glEnable(GL11.GL_NORMALIZE);
													var79.model[0].renderModelVertices(0, 0, 0.0F);
													GL11.glDisable(GL11.GL_NORMALIZE);
												} catch (Exception var47) {
													var47.printStackTrace();
												}

												GL11.glPopMatrix();
											} else {
												AxisAlignedBB var120 = var101.boundingBox;
												GL11.glDisable(GL11.GL_TEXTURE_2D);
												Tessellator var90 = Tessellator.instance;
												GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
												var90.startDrawingQuads(DefaultVertexFormats.POSITION_NORMAL);
												var90.normal(0.0F, 0.0F, -1.0F);
												var90.addVertex(var120.x0, var120.y1, var120.z0);
												var90.addVertex(var120.x1, var120.y1, var120.z0);
												var90.addVertex(var120.x1, var120.y0, var120.z0);
												var90.addVertex(var120.x0, var120.y0, var120.z0);
												var90.normal(0.0F, 0.0F, 1.0F);
												var90.addVertex(var120.x0, var120.y0, var120.z1);
												var90.addVertex(var120.x1, var120.y0, var120.z1);
												var90.addVertex(var120.x1, var120.y1, var120.z1);
												var90.addVertex(var120.x0, var120.y1, var120.z1);
												var90.normal(0.0F, -1.0F, 0.0F);
												var90.addVertex(var120.x0, var120.y0, var120.z0);
												var90.addVertex(var120.x1, var120.y0, var120.z0);
												var90.addVertex(var120.x1, var120.y0, var120.z1);
												var90.addVertex(var120.x0, var120.y0, var120.z1);
												var90.normal(0.0F, 1.0F, 0.0F);
												var90.addVertex(var120.x0, var120.y1, var120.z1);
												var90.addVertex(var120.x1, var120.y1, var120.z1);
												var90.addVertex(var120.x1, var120.y1, var120.z0);
												var90.addVertex(var120.x0, var120.y1, var120.z0);
												var90.normal(-1.0F, 0.0F, 0.0F);
												var90.addVertex(var120.x0, var120.y0, var120.z1);
												var90.addVertex(var120.x0, var120.y1, var120.z1);
												var90.addVertex(var120.x0, var120.y1, var120.z0);
												var90.addVertex(var120.x0, var120.y0, var120.z0);
												var90.normal(1.0F, 0.0F, 0.0F);
												var90.addVertex(var120.x1, var120.y0, var120.z0);
												var90.addVertex(var120.x1, var120.y1, var120.z0);
												var90.addVertex(var120.x1, var120.y1, var120.z1);
												var90.addVertex(var120.x1, var120.y0, var120.z1);
												var90.draw();
												GL11.glEnable(GL11.GL_TEXTURE_2D);
												GL11.glPushMatrix();
												GL11.glTranslatef(var109, var117, var29);
												var114 = 0.02F;
												GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
												GL11.glScalef(var114, -var114, var114);
												var79.model[0].renderModelVertices(0, 0, var18);
												GL11.glPopMatrix();
											}
										}
									}
								}
							}
						}
					}
				}
			}

			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			float var84 = var1;
			EffectRenderer var75 = var6;
			var18 = -MathHelper.cos(var3.rotationYaw * (float)Math.PI / 180.0F);
			var19 = -MathHelper.sin(var3.rotationYaw * (float)Math.PI / 180.0F);
			var52 = -var19 * MathHelper.sin(var3.rotationPitch * (float)Math.PI / 180.0F);
			var7 = var18 * MathHelper.sin(var3.rotationPitch * (float)Math.PI / 180.0F);
			var9 = MathHelper.cos(var3.rotationPitch * (float)Math.PI / 180.0F);

			int var64;
			for(var60 = 0; var60 < 2; ++var60) {
				if(var75.fxLayers[var60].size() != 0) {
					var64 = 0;
					if(var60 == 0) {
						var64 = var75.renderEngine.getTexture("/particles.png");
					}

					if(var60 == 1) {
						var64 = var75.renderEngine.getTexture("/terrain.png");
					}

					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var64);
					Tessellator var67 = Tessellator.instance;
					var67.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

					for(var48 = 0; var48 < var75.fxLayers[var60].size(); ++var48) {
						EntityFX var126 = (EntityFX)var75.fxLayers[var60].get(var48);
						var68 = null;
						var126.renderParticle(var67, var84, var18, var9, var19, var52, var7);
					}

					var67.draw();
				}
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5.renderEngine.getTexture("/rock.png"));
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glCallList(var5.glGenList);
			this.setupFog();
			var71 = var5;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5.renderEngine.getTexture("/clouds.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var84 = (float)(var5.worldObj.cloudColor >> 16 & 255) / 255.0F;
			var18 = (float)(var5.worldObj.cloudColor >> 8 & 255) / 255.0F;
			var19 = (float)(var5.worldObj.cloudColor & 255) / 255.0F;
			if(var5.mc.options.anaglyph) {
				var52 = (var84 * 30.0F + var18 * 59.0F + var19 * 11.0F) / 100.0F;
				var7 = (var84 * 30.0F + var18 * 70.0F) / 100.0F;
				var9 = (var84 * 30.0F + var19 * 70.0F) / 100.0F;
				var84 = var52;
				var18 = var7;
				var19 = var9;
			}

			Tessellator var59 = Tessellator.instance;
			var10 = 0.0F;
			var11 = 0.5F / 1024.0F;
			var10 = (float)(var5.worldObj.height + 2);
			var12 = ((float)var5.cloudOffsetX + var1) * var11 * 0.03F;
			float var49 = 0.0F;
			var59.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
			var59.setColorOpaque_F(var84, var18, var19);

			for(var65 = -2048; var65 < var71.worldObj.width + 2048; var65 += 512) {
				for(var72 = -2048; var72 < var71.worldObj.length + 2048; var72 += 512) {
					var59.addVertexWithUV((float)var65, var10, (float)(var72 + 512), (float)var65 * var11 + var12, (float)(var72 + 512) * var11);
					var59.addVertexWithUV((float)(var65 + 512), var10, (float)(var72 + 512), (float)(var65 + 512) * var11 + var12, (float)(var72 + 512) * var11);
					var59.addVertexWithUV((float)(var65 + 512), var10, (float)var72, (float)(var65 + 512) * var11 + var12, (float)var72 * var11);
					var59.addVertexWithUV((float)var65, var10, (float)var72, (float)var65 * var11 + var12, (float)var72 * var11);
					var59.addVertexWithUV((float)var65, var10, (float)var72, (float)var65 * var11 + var12, (float)var72 * var11);
					var59.addVertexWithUV((float)(var65 + 512), var10, (float)var72, (float)(var65 + 512) * var11 + var12, (float)var72 * var11);
					var59.addVertexWithUV((float)(var65 + 512), var10, (float)(var72 + 512), (float)(var65 + 512) * var11 + var12, (float)(var72 + 512) * var11);
					var59.addVertexWithUV((float)var65, var10, (float)(var72 + 512), (float)var65 * var11 + var12, (float)(var72 + 512) * var11);
				}
			}

			var59.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var59.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
			var12 = (float)(var71.worldObj.skyColor >> 16 & 255) / 255.0F;
			var49 = (float)(var71.worldObj.skyColor >> 8 & 255) / 255.0F;
			var13 = (float)(var71.worldObj.skyColor & 255) / 255.0F;
			if(var71.mc.options.anaglyph) {
				var14 = (var12 * 30.0F + var49 * 59.0F + var13 * 11.0F) / 100.0F;
				var89 = (var12 * 30.0F + var49 * 70.0F) / 100.0F;
				var21 = (var12 * 30.0F + var13 * 70.0F) / 100.0F;
				var12 = var14;
				var49 = var89;
				var13 = var21;
			}

			var59.setColorOpaque_F(var12, var49, var13);
			var10 = (float)(var71.worldObj.height + 10);

			for(var72 = -2048; var72 < var71.worldObj.width + 2048; var72 += 512) {
				for(int var91 = -2048; var91 < var71.worldObj.length + 2048; var91 += 512) {
					var59.addVertex((float)var72, var10, (float)var91);
					var59.addVertex((float)(var72 + 512), var10, (float)var91);
					var59.addVertex((float)(var72 + 512), var10, (float)(var91 + 512));
					var59.addVertex((float)var72, var10, (float)(var91 + 512));
				}
			}

			var59.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.setupFog();
			int var56;
			if(this.mc.objectMouseOver != null) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				boolean var92 = false;
				boolean var88 = false;
				MovingObjectPosition var80 = this.mc.objectMouseOver;
				Tessellator var95 = Tessellator.instance;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
				if(var5.damagePartialTime > 0.0F) {
					GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
					var55 = var5.renderEngine.getTexture("/terrain.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var55);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
					GL11.glPushMatrix();
					var56 = var5.worldObj.getBlockId(var80.blockX, var80.blockY, var80.blockZ);
					var54 = var56 > 0 ? Block.blocksList[var56] : null;
					var95.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
					var95.disableColor();
					if(var54 == null) {
						var54 = Block.stone;
					}

					var72 = 240 + (int)(var5.damagePartialTime * 10.0F);
					var65 = var80.blockZ;
					var48 = var80.blockY;
					var63 = var80.blockX;
					RenderBlocks var69 = var5.globalRenderBlocks;
					var69.overrideBlockTexture = var72;
					var69.renderBlockByRenderType(var54, var63, var48, var65);
					var69.overrideBlockTexture = -1;
					var95.draw();
					GL11.glDepthMask(true);
					GL11.glPopMatrix();
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var80 = this.mc.objectMouseOver;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
				GL11.glLineWidth(2.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDepthMask(false);
				var18 = 0.002F;
				var87 = var5.worldObj.getBlockId(var80.blockX, var80.blockY, var80.blockZ);
				if(var87 > 0) {
					AxisAlignedBB var61 = Block.blocksList[var87].getSelectedBoundingBoxFromPool(var80.blockX, var80.blockY, var80.blockZ).expand(var18, var18, var18);
					GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var61.x0, var61.y0, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y0, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y0, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y0, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y0, var61.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var61.x0, var61.y1, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y1, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y1, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y1, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y1, var61.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
					GL11.glVertex3f(var61.x0, var61.y0, var61.z0);
					GL11.glVertex3f(var61.x0, var61.y1, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y0, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y1, var61.z0);
					GL11.glVertex3f(var61.x1, var61.y0, var61.z1);
					GL11.glVertex3f(var61.x1, var61.y1, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y0, var61.z1);
					GL11.glVertex3f(var61.x0, var61.y1, var61.z1);
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
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5.renderEngine.getTexture("/water.png"));
			GL11.glCallList(var5.glGenList + 1);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColorMask(false, false, false, false);
			var55 = var5.sortAndRender(var3, 1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.options.anaglyph) {
				if(var2 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(var55 > 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5.renderEngine.getTexture("/terrain.png"));
				GL11.glCallLists(var5.renderIntBuffer);
			}

			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);
			if(this.mc.thirdPersonView) {
				float var82 = var1;
				var15 = this;
				var17 = this.mc.thePlayer;
				World var96 = this.mc.theWorld;
				var87 = (int)var17.posX;
				var55 = (int)var17.posY;
				var56 = (int)var17.posZ;
				Tessellator var66 = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
				var64 = var87 - 5;

				while(true) {
					if(var64 > var87 + 5) {
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						break;
					}

					for(var63 = var56 - 5; var63 <= var56 + 5; ++var63) {
						var48 = var96.getMapHeight(var64, var63);
						var65 = var55 - 5;
						var72 = var55 + 5;
						if(var65 < var48) {
							var65 = var48;
						}

						if(var72 < var48) {
							var72 = var48;
						}

						if(var65 != var72) {
							var21 = ((float)((var15.entityRendererInt1 + var64 * 3121 + var63 * 418711) % 32) + var82) / 32.0F;
							float var97 = (float)var64 + 0.5F - var17.posX;
							var23 = (float)var63 + 0.5F - var17.posZ;
							var104 = MathHelper.sqrt_float(var97 * var97 + var23 * var23) / (float)5;
							GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var104 * var104) * 0.7F);
							var66.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
							var66.addVertexWithUV((float)var64, (float)var65, (float)var63, 0.0F, (float)var65 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)(var64 + 1), (float)var65, (float)(var63 + 1), 2.0F, (float)var65 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)(var64 + 1), (float)var72, (float)(var63 + 1), 2.0F, (float)var72 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)var64, (float)var72, (float)var63, 0.0F, (float)var72 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)var64, (float)var65, (float)(var63 + 1), 0.0F, (float)var65 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)(var64 + 1), (float)var65, (float)var63, 2.0F, (float)var65 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)(var64 + 1), (float)var72, (float)var63, 2.0F, (float)var72 * 2.0F / 8.0F + var21 * 2.0F);
							var66.addVertexWithUV((float)var64, (float)var72, (float)(var63 + 1), 0.0F, (float)var72 * 2.0F / 8.0F + var21 * 2.0F);
							var66.draw();
						}
					}

					++var64;
				}
			}

			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((var2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(var1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(var1);
			}

			ItemRenderer var98 = this.itemRenderer;
			var52 = var98.prevEquippedProgress + (var98.equippedProgress - var98.prevEquippedProgress) * var1;
			var50 = var98.minecraft.thePlayer;
			GL11.glPushMatrix();
			GL11.glRotatef(var50.prevRotationPitch + (var50.rotationPitch - var50.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var50.prevRotationYaw + (var50.rotationYaw - var50.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			var9 = 0.8F;
			if(var98.itemSwingState) {
				var10 = ((float)var98.swingProgress + var1) / 7.0F;
				var11 = MathHelper.sin(var10 * (float)Math.PI);
				GL11.glTranslatef(-MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI) * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI * 2.0F) * 0.2F, -var11 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var9, -0.65F * var9 - (1.0F - var52) * 0.6F, -0.9F * var9);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(var98.itemSwingState) {
				var11 = MathHelper.sin((var10 = ((float)var98.swingProgress + var1) / 7.0F) * var10 * (float)Math.PI);
				GL11.glRotatef(MathHelper.sin(MathHelper.sqrt_float(var10) * (float)Math.PI) * 80.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-var11 * 20.0F, 1.0F, 0.0F, 0.0F);
			}

			GL11.glColor4f(var10 = var98.minecraft.theWorld.getBlockLightValue((int)var50.posX, (int)var50.posY, (int)var50.posZ), var10, var10, 1.0F);
			if(var98.itemToRender != null) {
				var11 = 0.4F;
				GL11.glScalef(0.4F, var11, var11);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var98.minecraft.renderEngine.getTexture("/terrain.png"));
				var98.renderBlocksInstance.renderBlockOnInventory(var98.itemToRender);
			} else {
				EntityPlayer.setupSkinImage(var98.minecraft.renderEngine);
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

	private void setupFog() {
		World var1 = this.mc.theWorld;
		EntityPlayer var2 = this.mc.thePlayer;
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
