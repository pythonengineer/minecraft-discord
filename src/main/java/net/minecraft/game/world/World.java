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
    private List lightingToUpdate = new ArrayList();
    private List loadedEntityList = new ArrayList();
    private int worldTime = 0;
    private long skyColor = 10079487L;
    private long fogColor = 11587839L;
    private long cloudColor = 16777215L;
    private int skylightSubtracted = 0;
	private static float[] lightBrightnessTable = new float[16];
	public Entity playerEntity;
	public int difficultySetting;
	public final Pathfinder pathFinder = new Pathfinder(this);
	public EaglercraftRandom rand = new EaglercraftRandom();
	public float spawnX = 0.0F;
	public float spawnY = 64.0F;
	public float spawnZ = 0.0F;
	private List worldAccesses = new ArrayList();
    private IChunkProvider chunkProvider = new ChunkProviderLoadOrGenerate(new ChunkProviderGenerate(this));

    public final int getBlockId(int var1, int var2, int var3) {
        return var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000 ? (var2 <= 0 ? Block.lavaStill.blockID : (var2 >= 128 ? 0 : this.getChunkFromChunkCoords(var1 >>> 4, var3 >>> 4).getBlockID(var1 & 15, var2, var3 & 15))) : 0;
    }

    public final boolean blockExists(int var1, int var2) {
        return this.chunkExists(var1 >>> 4, var2 >>> 4);
    }

    private boolean chunkExists(int var1, int var2) {
        return this.chunkProvider.chunkExists(var1, var2);
    }

    private Chunk getChunkFromChunkCoords(int var1, int var2) {
        return this.chunkProvider.provideChunk(var1, var2);
    }

	public final Material getBlockMaterial(int var1, int var2, int var3) {
		var1 = this.getBlockId(var1, var2, var3);
		return var1 == 0 ? Material.air : Block.blocksList[var1].material;
	}

    public final int getBlockMetadata(int var1, int var2, int var3) {
        if(var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if(var2 < 0) {
                return 0;
            } else if(var2 >= 128) {
                return 0;
            } else {
                Chunk var4 = this.getChunkFromChunkCoords(var1 >>> 4, var3 >>> 4);
                var1 &= 15;
                var3 &= 15;
                return var4.getBlockMetadata(var1, var2, var3);
            }
        } else {
            return 0;
        }
    }

    public final void setBlockMetadataWithNotify(int var1, int var2, int var3, int var4) {
        int var5 = var4;
        var4 = var3;
        var3 = var2;
        var2 = var1;
        boolean var10000;
        if(var1 >= -32000000 && var4 >= -32000000 && var1 < 32000000 && var4 <= 32000000) {
            if(var3 < 0) {
                var10000 = false;
            } else if(var3 >= 128) {
                var10000 = false;
            } else {
                Chunk var6 = this.getChunkFromChunkCoords(var1 >>> 4, var4 >>> 4);
                var2 &= 15;
                var4 &= 15;
                var6.setBlockMetadata(var2, var3, var4, var5);
                var10000 = true;
            }
        } else {
            var10000 = false;
        }

    }

    public final boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
        boolean var10000;
        int var6;
        int var8;
        if(var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if(var2 < 0) {
                var10000 = false;
            } else if(var2 >= 128) {
                var10000 = false;
            } else {
                Chunk var10 = this.getChunkFromChunkCoords(var1 >>> 4, var3 >>> 4);
                var6 = var1 & 15;
                var8 = var3 & 15;
                if((var10.getBlockID(var6, var2, var8) & 255) == var4) {
                    var10000 = false;
                } else {
                    var10.setBlockID(var6, var2, var8, var4);
                    var10000 = true;
                }
            }
        } else {
            var10000 = false;
        }

        if(!var10000) {
            return false;
        } else {
            var8 = var3;
            int var7 = var2;
            var6 = var1;
            World var5 = this;

            for(int var11 = 0; var11 < var5.worldAccesses.size(); ++var11) {
                ((IWorldAccess)var5.worldAccesses.get(var11)).markBlockAndNeighborsNeedsUpdate(var6, var7, var8);
            }

            return true;
        }
    }

    public final void markBlocksDirtyVertical(int var1, int var2, int var3, int var4) {
        int var5;
        if(var3 > var4) {
            var5 = var4;
            var4 = var3;
            var3 = var5;
        }

        int var7 = var2;
        int var6 = var4;
        var5 = var1;
        var4 = var2;
        var3 = var3;
        var2 = var1;
        World var9 = this;

        for(int var8 = 0; var8 < var9.worldAccesses.size(); ++var8) {
            ((IWorldAccess)var9.worldAccesses.get(var8)).markBlockRangeNeedsUpdate(var2, var3, var4, var5, var6, var7);
        }

    }

    public final int canExistingBlockSeeTheSky(int var1, int var2, int var3) {
        if(var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if(var2 < 0) {
                return 15;
            } else if(var2 >= 128) {
                return 15;
            } else {
                Chunk var4 = this.getChunkFromChunkCoords(var1 >>> 4, var3 >>> 4);
                var1 &= 15;
                var3 &= 15;
                return var4.getBlockLightValue(var1, var2, var3, this.skylightSubtracted);
            }
        } else {
            return 15;
        }
    }

    public final int getHeightValue(int var1, int var2) {
        if(var1 >= -32000000 && var2 >= -32000000 && var1 < 32000000 && var2 <= 32000000) {
            if(!this.chunkExists(var1 >>> 4, var2 >>> 4)) {
                return 0;
            } else {
                Chunk var3 = this.getChunkFromChunkCoords(var1 >>> 4, var2 >>> 4);
                return var3.getHeightValue(var1 & 15, var2 & 15);
            }
        } else {
            return 0;
        }
    }

    public final void neighborLightPropagationChanged(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
        if(this.blockExists(var2, var4) && this.getSavedLightValue(var1, var2, var3, var4) != var5) {
            this.scheduleLightingUpdate(var1, var2, var3, var4, var2, var3, var4);
        }

    }

    public final int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
        if(var2 >= -32000000 && var4 >= -32000000 && var2 < 32000000 && var4 <= 32000000) {
            if(var3 < 0) {
                return 15;
            } else if(var3 >= 128) {
                return 15;
            } else if(!this.chunkExists(var2 >>> 4, var4 >>> 4)) {
                return 0;
            } else {
                Chunk var5 = this.getChunkFromChunkCoords(var2 >>> 4, var4 >>> 4);
                var2 &= 15;
                var4 &= 15;
                return var5.getSavedLightValue(var1, var2, var3, var4);
            }
        } else {
            return 15;
        }
    }

    public final float getBrightness(int var1, int var2, int var3) {
        return lightBrightnessTable[this.canExistingBlockSeeTheSky(var1, var2, var3)];
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

                    double var10 = 999.0D;
                    double var12 = 999.0D;
                    double var14 = 999.0D;
                    if(var3 > var6) {
                        var10 = (double)var6 + 1.0D;
                    }

                    if(var3 < var6) {
                        var10 = (double)var6;
                    }

                    if(var4 > var7) {
                        var12 = (double)var7 + 1.0D;
                    }

                    if(var4 < var7) {
                        var12 = (double)var7;
                    }

                    if(var5 > var8) {
                        var14 = (double)var8 + 1.0D;
                    }

                    if(var5 < var8) {
                        var14 = (double)var8;
                    }

                    double var16 = 999.0D;
                    double var18 = 999.0D;
                    double var20 = 999.0D;
                    double var22 = var2.xCoord - var1.xCoord;
                    double var24 = var2.yCoord - var1.yCoord;
                    double var26 = var2.zCoord - var1.zCoord;
                    if(var10 != 999.0D) {
                        var16 = (var10 - var1.xCoord) / var22;
                    }

                    if(var12 != 999.0D) {
                        var18 = (var12 - var1.yCoord) / var24;
                    }

                    if(var14 != 999.0D) {
                        var20 = (var14 - var1.zCoord) / var26;
                    }

                    byte var28;
                    if(var16 < var18 && var16 < var20) {
                        if(var3 > var6) {
                            var28 = 4;
                        } else {
                            var28 = 5;
                        }

                        var1.xCoord = var10;
                        var1.yCoord += var24 * var16;
                        var1.zCoord += var26 * var16;
                    } else if(var18 < var20) {
                        if(var4 > var7) {
                            var28 = 0;
                        } else {
                            var28 = 1;
                        }

                        var1.xCoord += var22 * var18;
                        var1.yCoord = var12;
                        var1.zCoord += var26 * var18;
                    } else {
                        if(var5 > var8) {
                            var28 = 2;
                        } else {
                            var28 = 3;
                        }

                        var1.xCoord += var22 * var20;
                        var1.yCoord += var24 * var20;
                        var1.zCoord = var14;
                    }

                    Vec3D var29 = new Vec3D(var1.xCoord, var1.yCoord, var1.zCoord);
                    var6 = (int)(var29.xCoord = (double)MathHelper.floor_double(var1.xCoord));
                    if(var28 == 5) {
                        --var6;
                        ++var29.xCoord;
                    }

                    var7 = (int)(var29.yCoord = (double)MathHelper.floor_double(var1.yCoord));
                    if(var28 == 1) {
                        --var7;
                        ++var29.yCoord;
                    }

                    var8 = (int)(var29.zCoord = (double)MathHelper.floor_double(var1.zCoord));
                    if(var28 == 3) {
                        --var8;
                        ++var29.zCoord;
                    }

                    int var30 = this.getBlockId(var6, var7, var8);
                    Block var11 = Block.blocksList[var30];
                    if(var30 > 0 && var11.isCollidable()) {
                        MovingObjectPosition var31 = var11.collisionRayTrace(this, var6, var7, var8, var1, var2);
                        if(var31 != null) {
                            return var31;
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

	public final void playSoundEffect(double var1, double var3, double var5, String var7, float var8, float var9) {
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

    public final void spawnEntityInWorld(Entity var1) {
        this.loadedEntityList.add(var1);
    }

    public static void setEntityDead(Entity var0) {
        var0.setEntityDead();
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

    public final Vec3D getSkyColor(float var1) {
        var1 = this.getCelestialAngle(var1);
        var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        if(var1 < 0.0F) {
            var1 = 0.0F;
        }

        if(var1 > 1.0F) {
            var1 = 1.0F;
        }

        float var2 = (float)(this.skyColor >> 16 & 255L) / 255.0F;
        float var3 = (float)(this.skyColor >> 8 & 255L) / 255.0F;
        float var4 = (float)(this.skyColor & 255L) / 255.0F;
        var2 *= var1;
        var3 *= var1;
        var4 *= var1;
        return new Vec3D((double)var2, (double)var3, (double)var4);
    }

    public final float getCelestialAngle(float var1) {
        var1 = ((float)this.worldTime + var1) / 24000.0F - 0.15F;
        return var1;
    }

    public final Vec3D getCloudColor(float var1) {
        var1 = this.getCelestialAngle(var1);
        var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        if(var1 < 0.0F) {
            var1 = 0.0F;
        }

        if(var1 > 1.0F) {
            var1 = 1.0F;
        }

        float var2 = (float)(this.cloudColor >> 16 & 255L) / 255.0F;
        float var3 = (float)(this.cloudColor >> 8 & 255L) / 255.0F;
        float var4 = (float)(this.cloudColor & 255L) / 255.0F;
        var2 *= var1 * 0.9F + 0.1F;
        var3 *= var1 * 0.9F + 0.1F;
        var4 *= var1 * 0.85F + 0.15F;
        return new Vec3D((double)var2, (double)var3, (double)var4);
    }

    public final Vec3D getFogColor(float var1) {
        var1 = this.getCelestialAngle(var1);
        var1 = MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        if(var1 < 0.0F) {
            var1 = 0.0F;
        }

        if(var1 > 1.0F) {
            var1 = 1.0F;
        }

        float var2 = (float)(this.fogColor >> 16 & 255L) / 255.0F;
        float var3 = (float)(this.fogColor >> 8 & 255L) / 255.0F;
        float var4 = (float)(this.fogColor & 255L) / 255.0F;
        var2 *= var1 * 0.94F + 0.06F;
        var3 *= var1 * 0.94F + 0.06F;
        var4 *= var1 * 0.91F + 0.09F;
        return new Vec3D((double)var2, (double)var3, (double)var4);
    }

    public final float getStarBrightness(float var1) {
        var1 = this.getCelestialAngle(var1);
        var1 = 1.0F - (MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 12.0F / 16.0F);
        if(var1 < 0.0F) {
            var1 = 0.0F;
        }

        if(var1 > 1.0F) {
            var1 = 1.0F;
        }

        return var1 * var1 * 0.5F;
    }

    public final void scheduleBlockUpdate() {
        for(int var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
            Entity var2 = (Entity)this.loadedEntityList.get(var1);
            if(!var2.isDead) {
                var2.lastTickPosX = var2.posX;
                var2.lastTickPosY = var2.posY;
                var2.lastTickPosZ = var2.posZ;
                var2.prevRotationYaw = var2.rotationYaw;
                var2.prevRotationPitch = var2.rotationPitch;
                var2.onUpdate();
            }

            if(var2.isDead) {
                this.loadedEntityList.remove(var1--);
            }
        }

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

	public final boolean isBlockNormalCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

    public final void updatingLighting() {
        while(this.lightingToUpdate.size() > 0) {
            MetadataChunkBlock var10000 = (MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1);
            World var2 = this;
            MetadataChunkBlock var1 = var10000;

            for(int var3 = var1.x; var3 <= var1.maxX; ++var3) {
                for(int var4 = var1.z; var4 <= var1.maxZ; ++var4) {
                    if(var2.blockExists(var3, var4)) {
                        for(int var5 = var1.y; var5 <= var1.maxY; ++var5) {
                            int var6 = var2.getSavedLightValue(var1.skyBlock, var3, var5, var4);
                            int var7 = var2.getBlockId(var3, var5, var4);
                            int var8 = Block.lightOpacity[var7];
                            if(var8 == 0) {
                                var8 = 1;
                            }

                            int var9 = 0;
                            int var11;
                            int var13;
                            if(var1.skyBlock != EnumSkyBlock.Sky) {
                                if(var1.skyBlock == EnumSkyBlock.Block) {
                                    var9 = Block.lightValue[var7];
                                }
                            } else {
                                boolean var19;
                                if(var3 >= -32000000 && var4 >= -32000000 && var3 < 32000000 && var4 <= 32000000) {
                                    if(var5 < 0) {
                                        var19 = false;
                                    } else if(var5 >= 128) {
                                        var19 = true;
                                    } else if(!var2.chunkExists(var3 >>> 4, var4 >>> 4)) {
                                        var19 = false;
                                    } else {
                                        Chunk var14 = var2.getChunkFromChunkCoords(var3 >>> 4, var4 >>> 4);
                                        var11 = var3 & 15;
                                        var13 = var4 & 15;
                                        var19 = var14.canBlockSeeTheSky(var11, var5, var13);
                                    }
                                } else {
                                    var19 = false;
                                }

                                if(var19) {
                                    var9 = 15;
                                }
                            }

                            int var12;
                            int var18;
                            if(var8 >= 15 && var9 == 0) {
                                var7 = 0;
                            } else {
                                var7 = var2.getSavedLightValue(var1.skyBlock, var3 - 1, var5, var4);
                                int var10 = var2.getSavedLightValue(var1.skyBlock, var3 + 1, var5, var4);
                                var11 = var2.getSavedLightValue(var1.skyBlock, var3, var5 - 1, var4);
                                var12 = var2.getSavedLightValue(var1.skyBlock, var3, var5 + 1, var4);
                                var13 = var2.getSavedLightValue(var1.skyBlock, var3, var5, var4 - 1);
                                var18 = var2.getSavedLightValue(var1.skyBlock, var3, var5, var4 + 1);
                                var7 = var7;
                                if(var10 > var7) {
                                    var7 = var10;
                                }

                                if(var11 > var7) {
                                    var7 = var11;
                                }

                                if(var12 > var7) {
                                    var7 = var12;
                                }

                                if(var13 > var7) {
                                    var7 = var13;
                                }

                                if(var18 > var7) {
                                    var7 = var18;
                                }

                                var7 -= var8;
                                if(var7 < 0) {
                                    var7 = 0;
                                }

                                if(var9 > var7) {
                                    var7 = var9;
                                }
                            }

                            if(var6 != var7) {
                                var18 = var4;
                                var13 = var5;
                                var12 = var3;
                                EnumSkyBlock var17 = var1.skyBlock;
                                World var16 = var2;
                                if(var3 >= -32000000 && var4 >= -32000000 && var3 < 32000000 && var4 <= 32000000 && var5 >= 0 && var5 < 128 && var2.chunkExists(var3 >>> 4, var4 >>> 4)) {
                                    Chunk var15 = var2.getChunkFromChunkCoords(var3 >>> 4, var4 >>> 4);
                                    var15.getBlockLightValue(var17, var3 & 15, var5, var4 & 15, var7);

                                    for(var6 = 0; var6 < var16.worldAccesses.size(); ++var6) {
                                        ((IWorldAccess)var16.worldAccesses.get(var6)).markBlockAndNeighborsNeedsUpdate(var12, var13, var18);
                                    }
                                }

                                --var7;
                                if(var7 < 0) {
                                    var7 = 0;
                                }

                                var2.neighborLightPropagationChanged(var1.skyBlock, var3 - 1, var5, var4, var7);
                                var2.neighborLightPropagationChanged(var1.skyBlock, var3 + 1, var5, var4, var7);
                                var2.neighborLightPropagationChanged(var1.skyBlock, var3, var5 - 1, var4, var7);
                                var2.neighborLightPropagationChanged(var1.skyBlock, var3, var5 + 1, var4, var7);
                                var2.neighborLightPropagationChanged(var1.skyBlock, var3, var5, var4 - 1, var7);
                                var2.neighborLightPropagationChanged(var1.skyBlock, var3, var5, var4 + 1, var7);
                            }
                        }
                    }
                }
            }
        }

    }

    public final void scheduleLightingUpdate(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.lightingToUpdate.add(new MetadataChunkBlock(var1, var2, var3, var4, var5, var6, var7));
    }

    public final void restartTimeOfDay() {
        float var1 = 1.0F;
        var1 = this.getCelestialAngle(1.0F);
        var1 = 1.0F - (MathHelper.cos(var1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
        if(var1 < 0.0F) {
            var1 = 0.0F;
        }

        if(var1 > 1.0F) {
            var1 = 1.0F;
        }

        int var2 = (int)(var1 * 13.0F);
        if(var2 != this.skylightSubtracted) {
            this.skylightSubtracted = var2;

            for(var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
                ((IWorldAccess)this.worldAccesses.get(var2)).updateAllRenderers();
            }
        }

        this.worldTime = (this.worldTime + 1) % 24000;
    }

    public final List getEntitiesWithinAABB(Entity var1, AxisAlignedBB var2) {
        ArrayList var3 = new ArrayList();

        for(int var4 = 0; var4 < this.loadedEntityList.size(); ++var4) {
            Entity var5 = (Entity)this.loadedEntityList.get(var4);
            if(var5 != var1) {
                AxisAlignedBB var6 = var5.boundingBox;
                if(var2.maxX > var6.minX && var2.minX < var6.maxX ? (var2.maxY > var6.minY && var2.minY < var6.maxY ? var2.maxZ > var6.minZ && var2.minZ < var6.maxZ : false) : false) {
                    var3.add(var5);
                }
            }
        }

        return var3;
    }

    public final List getLoadedEntityList() {
        return this.loadedEntityList;
    }

	static {
		for(int var0 = 0; var0 <= 15; ++var0) {
			float var1 = 1.0F - (float)var0 / 15.0F;
			lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

	}
}
