package net.minecraft.game.world;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;
import net.minecraft.game.world.path.Pathfinder;

public final class World {
	private static float[] lightBrightnessTable = new float[16];
	public EntityMap entityMap = new EntityMap(256, 256, 256);
	public Entity playerEntity;
	public int difficultySetting;
	public final Pathfinder pathFinder = new Pathfinder(this);
	public EaglercraftRandom rand = new EaglercraftRandom();
	public float spawnX = 0.0F;
	public float spawnY = 64.0F;
	public float spawnZ = 0.0F;
	private List worldAccesses = new ArrayList();
	private ChunkProviderGenerate chunkProvider = new ChunkProviderGenerate();

	public final int getBlockId(int var1, int var2, int var3) {
		return this.chunkProvider.getBlockAt(var1, var2, var3);
	}

	public final Material getBlockMaterial(int var1, int var2, int var3) {
		var1 = this.getBlockId(var1, var2, var3);
		return var1 == 0 ? Material.air : Block.blocksList[var1].material;
	}

	public final boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
		if(!this.chunkProvider.setBlockAt(var1, var2, var3, var4)) {
			return false;
		} else {
			int var8 = var3;
			int var7 = var2;
			int var6 = var1;
			World var5 = this;

			for(var1 = 0; var1 < var5.worldAccesses.size(); ++var1) {
				((IWorldAccess)var5.worldAccesses.get(var1)).markBlockAndNeighborsNeedsUpdate(var6, var7, var8);
			}

			return true;
		}
	}

	public final float getBrightness(int var1, int var2, int var3) {
		return lightBrightnessTable[15];
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
		if(!Double.isNaN(var1.xCoord) && !Double.isNaN(var1.yCoord) && !Double.isNaN(var1.zCoord)) {
			if(!Double.isNaN(var2.xCoord) && !Double.isNaN(var2.yCoord) && !Double.isNaN(var2.zCoord)) {
				int var3 = MathHelper.floor_double(var2.xCoord);
				int var4 = MathHelper.floor_double(var2.yCoord);
				int var5 = MathHelper.floor_double(var2.zCoord);
				int var6 = MathHelper.floor_double(var1.xCoord);
				int var7 = MathHelper.floor_double(var1.yCoord);
				int var8 = MathHelper.floor_double(var1.zCoord);
				int var9 = 20;

				while(var9-- >= 0) {
					if(Double.isNaN(var1.xCoord) || Double.isNaN(var1.yCoord) || Double.isNaN(var1.zCoord)) {
						return null;
					}

					if(var6 == var3 && var7 == var4 && var8 == var5) {
						return null;
					}

					float var10 = 999.0F;
					float var11 = 999.0F;
					float var12 = 999.0F;
					if(var3 > var6) {
						var10 = (float)var6 + 1.0F;
					}

					if(var3 < var6) {
						var10 = (float)var6;
					}

					if(var4 > var7) {
						var11 = (float)var7 + 1.0F;
					}

					if(var4 < var7) {
						var11 = (float)var7;
					}

					if(var5 > var8) {
						var12 = (float)var8 + 1.0F;
					}

					if(var5 < var8) {
						var12 = (float)var8;
					}

					double var13 = 999.0D;
					double var15 = 999.0D;
					double var17 = 999.0D;
					double var19 = var2.xCoord - var1.xCoord;
					double var21 = var2.yCoord - var1.yCoord;
					double var23 = var2.zCoord - var1.zCoord;
					if(var10 != 999.0F) {
						var13 = ((double)var10 - var1.xCoord) / var19;
					}

					if(var11 != 999.0F) {
						var15 = ((double)var11 - var1.yCoord) / var21;
					}

					if(var12 != 999.0F) {
						var17 = ((double)var12 - var1.zCoord) / var23;
					}

					byte var25;
					if(var13 < var15 && var13 < var17) {
						if(var3 > var6) {
							var25 = 4;
						} else {
							var25 = 5;
						}

						var1.xCoord = (double)var10;
						var1.yCoord += var21 * var13;
						var1.zCoord += var23 * var13;
					} else if(var15 < var17) {
						if(var4 > var7) {
							var25 = 0;
						} else {
							var25 = 1;
						}

						var1.xCoord += var19 * var15;
						var1.yCoord = (double)var11;
						var1.zCoord += var23 * var15;
					} else {
						if(var5 > var8) {
							var25 = 2;
						} else {
							var25 = 3;
						}

						var1.xCoord += var19 * var17;
						var1.yCoord += var21 * var17;
						var1.zCoord = (double)var12;
					}

					Vec3D var26 = new Vec3D(var1.xCoord, var1.yCoord, var1.zCoord);
					var6 = (int)(var26.xCoord = (double)MathHelper.floor_double(var1.xCoord));
					if(var25 == 5) {
						--var6;
						++var26.xCoord;
					}

					var7 = (int)(var26.yCoord = (double)MathHelper.floor_double(var1.yCoord));
					if(var25 == 1) {
						--var7;
						++var26.yCoord;
					}

					var8 = (int)(var26.zCoord = (double)MathHelper.floor_double(var1.zCoord));
					if(var25 == 3) {
						--var8;
						++var26.zCoord;
					}

					int var27 = this.getBlockId(var6, var7, var8);
					Block var29 = Block.blocksList[var27];
					if(var27 > 0 && var29.isCollidable()) {
						MovingObjectPosition var28 = var29.collisionRayTrace(this, var6, var7, var8, var1, var2);
						if(var28 != null) {
							return var28;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
		for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			float var6 = 16.0F;
			if(var3 > 1.0F) {
				var6 = 16.0F * var3;
			}

			if(this.playerEntity.getDistanceToEntity(var1) < (double)(var6 * var6)) {
				((IWorldAccess)this.worldAccesses.get(var5)).playSound(var2, var1.posX, var1.posY - (double)var1.yOffset, var1.posZ, var3, var4);
			}
		}

	}

	public final void playSoundAtPlayer(double var1, double var3, double var5, String var7, float var8, float var9) {
		try {
			for(int var10 = 0; var10 < this.worldAccesses.size(); ++var10) {
				float var11 = 16.0F;
				if(var8 > 1.0F) {
					var11 = 16.0F * var8;
				}

				double var12 = var1 - this.playerEntity.posX;
				double var14 = var3 - this.playerEntity.posY;
				double var16 = var5 - this.playerEntity.posZ;
				if(var12 * var12 + var14 * var14 + var16 * var16 < (double)(var11 * var11)) {
					((IWorldAccess)this.worldAccesses.get(var10)).playSound(var7, var1, var3, var5, var8, var9);
				}
			}

		} catch (Exception var18) {
			var18.printStackTrace();
		}
	}

	public final void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		for(int var14 = 0; var14 < this.worldAccesses.size(); ++var14) {
			((IWorldAccess)this.worldAccesses.get(var14)).spawnParticle(var1, var2, var4, var6, var8, var10, var12);
		}

	}

	public final void addWorldAccess(IWorldAccess var1) {
		this.worldAccesses.add(var1);
	}

	public final void removeWorldAccess(IWorldAccess var1) {
		this.worldAccesses.remove(var1);
	}

	public final List getCollidingBoundingBoxes(AxisAlignedBB var1) {
		ArrayList var2 = new ArrayList();
		int var3 = MathHelper.floor_double(var1.minX);
		int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var5 = MathHelper.floor_double(var1.minY);
		int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var7 = MathHelper.floor_double(var1.minZ);
		int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);

		for(var3 = var3; var3 < var4; ++var3) {
			for(int var9 = var5; var9 < var6; ++var9) {
				for(int var10 = var7; var10 < var8; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var3, var9, var10)];
					if(var11 != null) {
						AxisAlignedBB var14 = var11.getCollisionBoundingBoxFromPool(var3, var9, var10);
						if(var14 != null && (var14.maxX >= var1.minX && var14.minX <= var1.maxX ? (var14.maxY >= var1.minY && var14.minY <= var1.maxY ? var14.maxZ >= var1.minZ && var14.minZ <= var1.maxZ : false) : false)) {
							var2.add(var14);
						}
					}
				}
			}
		}

		return var2;
	}

	public static Vec3D getSkyColor() {
		return new Vec3D(0.0D, 0.0D, 1.0D);
	}

	public static Vec3D getCloudColor() {
		return new Vec3D(1.0D, 1.0D, 1.0D);
	}

	public static Vec3D getFogColor() {
		return new Vec3D(1.0D, 1.0D, 1.0D);
	}

	public final void updateEntities() {
		this.playerEntity.lastTickPosX = this.playerEntity.posX;
		this.playerEntity.lastTickPosY = this.playerEntity.posY;
		this.playerEntity.lastTickPosZ = this.playerEntity.posZ;
		this.playerEntity.onUpdate();
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB var1) {
		int var2 = MathHelper.floor_double(var1.minX);
		int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var4 = MathHelper.floor_double(var1.minY);
		int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var6 = MathHelper.floor_double(var1.minZ);
		int var7 = MathHelper.floor_double(var1.maxZ + 1.0D);
		if(var1.minX < 0.0D) {
			--var2;
		}

		if(var1.minY < 0.0D) {
			--var4;
		}

		if(var1.minZ < 0.0D) {
			--var6;
		}

		for(int var10 = var2; var10 < var3; ++var10) {
			for(var2 = var4; var2 < var5; ++var2) {
				for(int var8 = var6; var8 < var7; ++var8) {
					Block var9 = Block.blocksList[this.getBlockId(var10, var2, var8)];
					if(var9 != null && var9.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isBoundingBoxBurning(AxisAlignedBB var1) {
		int var2 = MathHelper.floor_double(var1.minX);
		int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var4 = MathHelper.floor_double(var1.minY);
		int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var6 = MathHelper.floor_double(var1.minZ);
		int var10 = MathHelper.floor_double(var1.maxZ + 1.0D);

		for(var2 = var2; var2 < var3; ++var2) {
			for(int var7 = var4; var7 < var5; ++var7) {
				for(int var8 = var6; var8 < var10; ++var8) {
					int var9 = this.getBlockId(var2, var7, var8);
					if(var9 == Block.fire.blockID || var9 == Block.lavaMoving.blockID || var9 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isMaterialInBB(AxisAlignedBB var1, Material var2) {
		int var3 = MathHelper.floor_double(var1.minX);
		int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var5 = MathHelper.floor_double(var1.minY);
		int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var7 = MathHelper.floor_double(var1.minZ);
		int var11 = MathHelper.floor_double(var1.maxZ + 1.0D);

		for(var3 = var3; var3 < var4; ++var3) {
			for(int var8 = var5; var8 < var6; ++var8) {
				for(int var9 = var7; var9 < var11; ++var9) {
					Block var10 = Block.blocksList[this.getBlockId(var3, var8, var9)];
					if(var10 != null && var10.material == var2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static String debugSkylightUpdates() {
		return "";
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final boolean isSolid(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	static {
		for(int var0 = 0; var0 <= 15; ++var0) {
			float var1 = 1.0F - (float)var0 / 15.0F;
			lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

	}
}
