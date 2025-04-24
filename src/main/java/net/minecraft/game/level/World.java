package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class World {
	private int MAX_TICKS = 100;
	private static float[] lightBrightnessTable = new float[9];
	public int width;
	public int length;
	public int height;
	private byte[] blocks;
	private byte[] data;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	private ArrayList worldAccesses = new ArrayList();
	private ArrayList ticksList = new ArrayList();
	private int[] heightMap;
	public EaglercraftRandom random = new EaglercraftRandom();
	private int randId = this.random.nextInt();
	public EntityMap entityMap;
	public int waterLevel;
	public int skyColor;
	public int fogColor;
	public int cloudColor;
	private int updateLCG = 0;
	private int playTime = 0;
	public Entity playerEntity;
	public boolean multiplayerWorld = false;
	public boolean survivalWorld = true;
    private int[] floodFillCounters = new int[295936];

	public final void load() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.worldAccesses = new ArrayList();
			this.heightMap = new int[this.width * this.length];
			Arrays.fill(this.heightMap, this.height);
			this.updateSkylight(0, 0, this.width, this.length);
			this.random = new EaglercraftRandom();
			this.randId = this.random.nextInt();
			this.ticksList = new ArrayList();
			if(this.waterLevel == 0) {
				this.waterLevel = this.height / 2;
			}

			if(this.skyColor == 0) {
				this.skyColor = 10079487;
			}

			if(this.fogColor == 0) {
				this.fogColor = 16777215;
			}

			if(this.cloudColor == 0) {
				this.cloudColor = 16777215;
			}

			if(this.xSpawn == 0 && this.ySpawn == 0 && this.zSpawn == 0) {
				this.findSpawn();
			}

			if(this.entityMap == null) {
				this.entityMap = new EntityMap(this.width, this.height, this.length);
			}

		}
	}

	public final void generate(int var1, int var2, int var3, byte[] var4) {
		this.width = var1;
		this.length = var3;
		this.height = var2;
		this.blocks = var4;
		this.data = new byte[var4.length];
		this.heightMap = new int[var1 * var3];
		Arrays.fill(this.heightMap, this.height);
		this.updateSkylight(0, 0, var1, var3);

		for(var1 = 0; var1 < this.worldAccesses.size(); ++var1) {
			((RenderGlobal)this.worldAccesses.get(var1)).loadRenderers();
		}

		this.ticksList.clear();
		this.findSpawn();
		this.load();
		System.gc();
	}

	private void findSpawn() {
	    EaglercraftRandom var1 = new EaglercraftRandom();
		int var2 = 0;

		int var3;
		int var4;
		int var5;
		do {
			++var2;
			var3 = var1.nextInt(this.width / 2) + this.width / 4;
			var4 = var1.nextInt(this.length / 2) + this.length / 4;
			var5 = this.getFirstUncoveredBlock(var3, var4) + 1;
			if(var2 == 10000) {
				this.xSpawn = var3;
				this.ySpawn = -100;
				this.zSpawn = var4;
				return;
			}
		} while((float)var5 <= (float)this.waterLevel);

		this.xSpawn = var3;
		this.ySpawn = var5;
		this.zSpawn = var4;
	}

	private void updateSkylight(int var1, int var2, int var3, int var4) {
		for(int var5 = var1; var5 < var1 + var3; ++var5) {
			for(int var6 = var2; var6 < var2 + var4; ++var6) {
				int var7 = this.heightMap[var5 + var6 * this.width];

				int var8;
				for(var8 = this.height - 1; var8 > 0 && Block.lightOpacity[this.getBlockId(var5, var8, var6)] == 0; --var8) {
				}

				this.heightMap[var5 + var6 * this.width] = var8 + 1;
				if(var7 != var8) {
					int var9 = var7 < var8 ? var7 : var8;
					var7 = var7 > var8 ? var7 : var8;
					this.updateLight(var5, var9, var6, var5 + 1, var7, var6 + 1);
				}
			}
		}

	}

    private void updateLight(int var1, int var2, int var3, int var4, int var5, int var6) {
        int var7 = 0;

        int var8;
        int var9;
        int var10;
        for(var8 = var1; var8 < var4; ++var8) {
            for(var9 = var3; var9 < var6; ++var9) {
                for(var10 = var2; var10 < var5; ++var10) {
                    this.floodFillCounters[var7++] = var8 << 20 | var10 << 10 | var9;
                }
            }
        }

        while(var7 > 0) {
            --var7;
            var8 = this.floodFillCounters[var7];
            var9 = var8 >> 20 & 1023;
            var10 = var8 >> 10 & 1023;
            var8 &= 1023;
            int var11 = this.heightMap[var9 + var8 * this.width];
            var11 = var10 >= var11 ? 8 : 0;
            byte var12 = this.blocks[(var10 * this.length + var8) * this.width + var9];
            int var13 = Block.lightOpacity[var12];
            if(var13 > 100) {
                var11 = 0;
            } else if(var11 < 7) {
                var13 = var13;
                if(var13 == 0) {
                    var13 = 1;
                }

                int var14;
                if(var9 > 0) {
                    var14 = (this.data[(var10 * this.length + var8) * this.width + (var9 - 1)] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }

                if(var9 < this.width - 1) {
                    var14 = (this.data[(var10 * this.length + var8) * this.width + var9 + 1] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }

                if(var10 > 0) {
                    var14 = (this.data[((var10 - 1) * this.length + var8) * this.width + var9] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }

                if(var10 < this.height - 1) {
                    var14 = (this.data[((var10 + 1) * this.length + var8) * this.width + var9] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }

                if(var8 > 0) {
                    var14 = (this.data[(var10 * this.length + (var8 - 1)) * this.width + var9] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }

                if(var8 < this.length - 1) {
                    var14 = (this.data[(var10 * this.length + var8 + 1) * this.width + var9] & 255) - var13;
                    if(var14 > var11) {
                        var11 = var14;
                    }
                }
            }

            if(var11 < Block.lightValue[var12]) {
                var11 = Block.lightValue[var12];
            }

            if(var9 < var1) {
                var1 = var9;
            } else if(var9 > var4) {
                var4 = var9;
            }

            if(var10 > var5) {
                var5 = var10;
            } else if(var10 < var2) {
                var2 = var10;
            }

            if(var8 < var3) {
                var3 = var8;
            } else if(var8 > var6) {
                var6 = var8;
            }

            if((this.data[(var10 * this.length + var8) * this.width + var9] & 255) != var11) {
                this.data[(var10 * this.length + var8) * this.width + var9] = (byte)var11;
                if(var9 > 0 && (this.data[(var10 * this.length + var8) * this.width + (var9 - 1)] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 - 1 << 20 | var10 << 10 | var8;
                }

                if(var9 < this.width - 1 && (this.data[(var10 * this.length + var8) * this.width + var9 + 1] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 + 1 << 20 | var10 << 10 | var8;
                }

                if(var10 > 0 && (this.data[((var10 - 1) * this.length + var8) * this.width + var9] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 << 20 | var10 - 1 << 10 | var8;
                }

                if(var10 < this.height - 1 && (this.data[((var10 + 1) * this.length + var8) * this.width + var9] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 << 20 | var10 + 1 << 10 | var8;
                }

                if(var8 > 0 && (this.data[(var10 * this.length + (var8 - 1)) * this.width + var9] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 << 20 | var10 << 10 | var8 - 1;
                }

                if(var8 < this.length - 1 && (this.data[(var10 * this.length + var8 + 1) * this.width + var9] & 255) != var11 - 1) {
                    this.floodFillCounters[var7++] = var9 << 20 | var10 << 10 | var8 + 1;
                }
            }
        }

        Iterator var15 = this.worldAccesses.iterator();

        while(var15.hasNext()) {
            RenderGlobal var10000 = (RenderGlobal)var15.next();
            Object var16 = null;
            var10000.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
        }

    }

	public final void addRenderer(RenderGlobal var1) {
		this.worldAccesses.add(var1);
	}

	public final void finalize() {
	}

	public final void removeRenderer(RenderGlobal var1) {
		this.worldAccesses.remove(var1);
	}

	public final ArrayList getCollidingBoundingBoxes(AxisAlignedBB var1) {
		ArrayList var2 = new ArrayList();
		int var3 = (int)var1.x0;
		int var4 = (int)var1.x1 + 1;
		int var5 = (int)var1.y0;
		int var6 = (int)var1.y1 + 1;
		int var7 = (int)var1.z0;
		int var8 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var3;
		}

		if(var1.y0 < 0.0F) {
			--var5;
		}

		if(var1.z0 < 0.0F) {
			--var7;
		}

		while(var3 < var4) {
			for(int var9 = var5; var9 < var6; ++var9) {
				for(int var10 = var7; var10 < var8; ++var10) {
					AxisAlignedBB var11;
					if(var3 >= 0 && var9 >= 0 && var10 >= 0 && var3 < this.width && var9 < this.height && var10 < this.length) {
						Block var12 = Block.blocksList[this.getBlockId(var3, var9, var10)];
						if(var12 != null) {
							var11 = var12.getCollisionBoundingBoxFromPool(var3, var9, var10);
							if(var11 != null && var1.intersectsWith(var11)) {
								var2.add(var11);
							}
						}
					} else if(var3 < 0 || var9 < 0 || var10 < 0 || var3 >= this.width || var10 >= this.length) {
						var11 = Block.bedrock.getCollisionBoundingBoxFromPool(var3, var9, var10);
						if(var11 != null && var1.intersectsWith(var11)) {
							var2.add(var11);
						}
					}
				}
			}

			++var3;
		}

		return var2;
	}

	public final void swap(int var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = this.getBlockId(var1, var2, var3);
		int var8 = this.getBlockId(var4, var5, var6);
		this.setBlock(var1, var2, var3, var8);
		this.setBlock(var4, var5, var6, var7);
		this.notifyBlocksOfNeighborChange(var1, var2, var3, var8);
		this.notifyBlocksOfNeighborChange(var4, var5, var6, var7);
	}

	public final boolean setBlock(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
				return false;
			} else {
				if(var4 == 0 && (var1 == 0 || var3 == 0 || var1 == this.width - 1 || var3 == this.length - 1)) {
					float var10000 = (float)var2;
					Object var5 = null;
					if(var10000 >= (float)this.waterLevel - 2.0F && (float)var2 < (float)this.waterLevel) {
						var4 = Block.waterMoving.blockID;
					}
				}

				byte var8 = this.blocks[(var2 * this.length + var3) * this.width + var1];
				this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
				if(var8 != 0) {
					Block.blocksList[var8].onBlockRemoval(this, var1, var2, var3);
				}

				if(var4 != 0) {
					Block.blocksList[var4].onBlockAdded(this, var1, var2, var3);
				}

				this.updateSkylight(var1, var3, 1, 1);
				this.updateLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);

				for(var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
					((RenderGlobal)this.worldAccesses.get(var4)).markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
		if(this.setBlock(var1, var2, var3, var4)) {
			this.notifyBlocksOfNeighborChange(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public final void notifyBlocksOfNeighborChange(int var1, int var2, int var3, int var4) {
		this.notifyBlockOfNeighborChange(var1 - 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1 + 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 - 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 + 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 - 1, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 + 1, var4);
	}

	public final boolean setTileNoUpdate(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			if(var4 == this.blocks[(var2 * this.length + var3) * this.width + var1]) {
				return false;
			} else {
				this.blocks[(var2 * this.length + var3) * this.width + var1] = (byte)var4;
				this.updateLight(var1, var2, var3, var1 + 1, var2 + 1, var3 + 1);
				return true;
			}
		} else {
			return false;
		}
	}

	private void notifyBlockOfNeighborChange(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length) {
			Block var5 = Block.blocksList[this.blocks[(var2 * this.length + var3) * this.width + var1]];
			if(var5 != null) {
				var5.onNeighborBlockChange(this, var1, var2, var3, var4);
			}

		}
	}

	public final boolean isHalfLit(int var1, int var2, int var3) {
        return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length ? (var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length ? this.data[(var2 * this.length + var3) * this.width + var1] : 0) > 3 : true;
	}

	public final int getBlockId(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length ? this.blocks[(var2 * this.length + var3) * this.width + var1] & 255 : 0;
	}

	public final boolean isBlockNormalCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public final void updateEntities() {
		EntityMap var9 = this.entityMap;

		for(int var1 = 0; var1 < var9.entities.size(); ++var1) {
			Entity var2;
			(var2 = (Entity)var9.entities.get(var1)).lastTickPosX = var2.posX;
			var2.lastTickPosY = var2.posY;
			var2.lastTickPosZ = var2.posZ;
			var2.onEntityUpdate();
			++var2.ticksExisted;
			if(var2.isDead) {
				var9.entities.remove(var1--);
				var9.slot0.init(var2.lastTickPosX, var2.lastTickPosY, var2.lastTickPosZ).remove(var2);
			} else {
				int var3 = (int)(var2.lastTickPosX / 16.0F);
				int var4 = (int)(var2.lastTickPosY / 16.0F);
				int var5 = (int)(var2.lastTickPosZ / 16.0F);
				int var6 = (int)(var2.posX / 16.0F);
				int var7 = (int)(var2.posY / 16.0F);
				int var8 = (int)(var2.posZ / 16.0F);
				if(var3 != var6 || var4 != var7 || var5 != var8) {
					EntityMapSlot var12 = var9.slot0.init(var2.lastTickPosX, var2.lastTickPosY, var2.lastTickPosZ);
					EntityMapSlot var10 = var9.slot1.init(var2.posX, var2.posY, var2.posZ);
					if(!var12.equals(var10)) {
						var12.remove(var2);
						var10.add(var2);
					}
				}
			}
		}

	}

	public final void tick() {
		++this.playTime;
		int var1 = 1;

		int var2;
		for(var2 = 1; 1 << var1 < this.width; ++var1) {
		}

		while(1 << var2 < this.length) {
			++var2;
		}

		int var3 = this.length - 1;
		int var4 = this.width - 1;
		int var5 = this.height - 1;
		int var6 = this.ticksList.size();
		if(var6 > this.MAX_TICKS) {
			var6 = this.MAX_TICKS;
		}

		int var7;
		int var10;
		for(var7 = 0; var7 < var6; ++var7) {
			NextTickListEntry var8 = (NextTickListEntry)this.ticksList.remove(0);
			if(var8.scheduledTime > 0) {
				--var8.scheduledTime;
				this.ticksList.add(var8);
			} else {
				int var12 = var8.zCoord;
				int var11 = var8.yCoord;
				var10 = var8.xCoord;
				if(var10 >= 0 && var11 >= 0 && var12 >= 0 && var10 < this.width && var11 < this.height && var12 < this.length) {
					byte var9 = this.blocks[(var8.yCoord * this.length + var8.zCoord) * this.width + var8.xCoord];
					if(var9 == var8.blockID && var9 > 0) {
						Block.blocksList[var9].updateTick(this, var8.xCoord, var8.yCoord, var8.zCoord, this.random);
					}
				}
			}
		}

		this.updateLCG += this.width * this.length * this.height;
		var6 = this.updateLCG / 200;
		this.updateLCG -= var6 * 200;

		for(var7 = 0; var7 < var6; ++var7) {
			this.randId = this.randId * 3 + 1013904223;
			int var13 = this.randId >> 2;
			int var14 = var13 & var4;
			var10 = var13 >> var1 & var3;
			var13 = var13 >> var1 + var2 & var5;
			byte var15 = this.blocks[(var13 * this.length + var10) * this.width + var14];
			if(Block.tickOnLoad[var15]) {
				Block.blocksList[var15].updateTick(this, var14, var13, var10, this.random);
			}
		}

	}

	public final int entitiesInLevelList(Class var1) {
		int var2 = 0;

		for(int var3 = 0; var3 < this.entityMap.entities.size(); ++var3) {
			Entity var4 = (Entity)this.entityMap.entities.get(var3);
			if(var1.isAssignableFrom(var4.getClass())) {
				++var2;
			}
		}

		return var2;
	}

	public final float getGroundLevel() {
		return (float)this.waterLevel - 2.0F;
	}

	public final float rgetGroundLevel() {
		return (float)this.waterLevel;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB var1) {
		int var2 = (int)var1.x0;
		int var3 = (int)var1.x1 + 1;
		int var4 = (int)var1.y0;
		int var5 = (int)var1.y1 + 1;
		int var6 = (int)var1.z0;
		int var7 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var2;
		}

		if(var1.y0 < 0.0F) {
			--var4;
		}

		if(var1.z0 < 0.0F) {
			--var6;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var4 < 0) {
			var4 = 0;
		}

		if(var6 < 0) {
			var6 = 0;
		}

		if(var3 > this.width) {
			var3 = this.width;
		}

		if(var5 > this.height) {
			var5 = this.height;
		}

		if(var7 > this.length) {
			var7 = this.length;
		}

		for(int var10 = var2; var10 < var3; ++var10) {
			for(var2 = var4; var2 < var5; ++var2) {
				for(int var8 = var6; var8 < var7; ++var8) {
					Block var9 = Block.blocksList[this.getBlockId(var10, var2, var8)];
					if(var9 != null && var9.getMaterial() != Material.air) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2) {
		int var3 = (int)var1.x0;
		int var4 = (int)var1.x1 + 1;
		int var5 = (int)var1.y0;
		int var6 = (int)var1.y1 + 1;
		int var7 = (int)var1.z0;
		int var8 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var3;
		}

		if(var1.y0 < 0.0F) {
			--var5;
		}

		if(var1.z0 < 0.0F) {
			--var7;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var5 < 0) {
			var5 = 0;
		}

		if(var7 < 0) {
			var7 = 0;
		}

		if(var4 > this.width) {
			var4 = this.width;
		}

		if(var6 > this.height) {
			var6 = this.height;
		}

		if(var8 > this.length) {
			var8 = this.length;
		}

		for(int var11 = var3; var11 < var4; ++var11) {
			for(var3 = var5; var3 < var6; ++var3) {
				for(int var9 = var7; var9 < var8; ++var9) {
					Block var10 = Block.blocksList[this.getBlockId(var11, var3, var9)];
					if(var10 != null && var10.getMaterial() == var2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void scheduleBlockUpdate(int var1, int var2, int var3, int var4) {
		NextTickListEntry var5 = new NextTickListEntry(var1, var2, var3, var4);
		if(var4 > 0) {
			var3 = Block.blocksList[var4].tickRate();
			var5.scheduledTime = var3;
		}

		this.ticksList.add(var5);
	}

	public final boolean checkIfAABBIsClear(AxisAlignedBB var1) {
		return this.entityMap.getEntitiesWithinAABBExcludingEntity((Entity)null, var1).size() == 0;
	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
		return this.entityMap.getEntitiesWithinAABBExcludingEntity(var1, var2);
	}

	public final boolean isSolid(float var1, float var2, float var3, float var4) {
		return this.isSolid(var1 - var4, var2 - var4, var3 - var4) ? true : (this.isSolid(var1 - var4, var2 - var4, var3 + var4) ? true : (this.isSolid(var1 - var4, var2 + var4, var3 - var4) ? true : (this.isSolid(var1 - var4, var2 + var4, var3 + var4) ? true : (this.isSolid(var1 + var4, var2 - var4, var3 - var4) ? true : (this.isSolid(var1 + var4, var2 - var4, var3 + var4) ? true : (this.isSolid(var1 + var4, var2 + var4, var3 - var4) ? true : this.isSolid(var1 + var4, var2 + var4, var3 + var4)))))));
	}

	private boolean isSolid(float var1, float var2, float var3) {
		int var4 = this.getBlockId((int)var1, (int)var2, (int)var3);
		return var4 > 0 && Block.blocksList[var4].isOpaqueCube();
	}

	private int getFirstUncoveredBlock(int var1, int var2) {
		int var3;
		for(var3 = this.height; (this.getBlockId(var1, var3 - 1, var2) == 0 || Block.blocksList[this.getBlockId(var1, var3 - 1, var2)].getMaterial() != Material.air) && var3 > 0; --var3) {
		}

		return var3;
	}

	public final void setSpawnLocation(int var1, int var2, int var3, float var4) {
		this.xSpawn = var1;
		this.ySpawn = var2;
		this.zSpawn = var3;
		this.rotSpawn = var4;
	}

	public final float getBlockLightValue(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.length ? lightBrightnessTable[this.data[(var2 * this.length + var3) * this.width + var1]] : 0.0F;
	}

	public final Material getBlockMaterial(int var1, int var2, int var3) {
		int var4 = this.getBlockId(var1, var2, var3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].getMaterial();
	}

	public final boolean isWater(int var1, int var2, int var3) {
		int var4 = this.getBlockId(var1, var2, var3);
		return var4 > 0 && Block.blocksList[var4].getMaterial() == Material.water;
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
		if(!Float.isNaN(var1.xCoord) && !Float.isNaN(var1.yCoord) && !Float.isNaN(var1.zCoord)) {
			if(!Float.isNaN(var2.xCoord) && !Float.isNaN(var2.yCoord) && !Float.isNaN(var2.zCoord)) {
				int var3 = (int)Math.floor((double)var2.xCoord);
				int var4 = (int)Math.floor((double)var2.yCoord);
				int var5 = (int)Math.floor((double)var2.zCoord);
				int var6 = (int)Math.floor((double)var1.xCoord);
				int var7 = (int)Math.floor((double)var1.yCoord);
				int var8 = (int)Math.floor((double)var1.zCoord);
				int var9 = 20;

				while(var9-- >= 0) {
					if(Float.isNaN(var1.xCoord) || Float.isNaN(var1.yCoord) || Float.isNaN(var1.zCoord)) {
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

					float var13 = 999.0F;
					float var14 = 999.0F;
					float var15 = 999.0F;
					float var16 = var2.xCoord - var1.xCoord;
					float var17 = var2.yCoord - var1.yCoord;
					float var18 = var2.zCoord - var1.zCoord;
					if(var10 != 999.0F) {
						var13 = (var10 - var1.xCoord) / var16;
					}

					if(var11 != 999.0F) {
						var14 = (var11 - var1.yCoord) / var17;
					}

					if(var12 != 999.0F) {
						var15 = (var12 - var1.zCoord) / var18;
					}

					boolean var19 = false;
					byte var24;
					if(var13 < var14 && var13 < var15) {
						if(var3 > var6) {
							var24 = 4;
						} else {
							var24 = 5;
						}

						var1.xCoord = var10;
						var1.yCoord += var17 * var13;
						var1.zCoord += var18 * var13;
					} else if(var14 < var15) {
						if(var4 > var7) {
							var24 = 0;
						} else {
							var24 = 1;
						}

						var1.xCoord += var16 * var14;
						var1.yCoord = var11;
						var1.zCoord += var18 * var14;
					} else {
						if(var5 > var8) {
							var24 = 2;
						} else {
							var24 = 3;
						}

						var1.xCoord += var16 * var15;
						var1.yCoord += var17 * var15;
						var1.zCoord = var12;
					}

					Vec3D var20 = new Vec3D(var1.xCoord, var1.yCoord, var1.zCoord);
					var6 = (int)(var20.xCoord = (float)Math.floor((double)var1.xCoord));
					if(var24 == 5) {
						--var6;
						++var20.xCoord;
					}

					var7 = (int)(var20.yCoord = (float)Math.floor((double)var1.yCoord));
					if(var24 == 1) {
						--var7;
						++var20.yCoord;
					}

					var8 = (int)(var20.zCoord = (float)Math.floor((double)var1.zCoord));
					if(var24 == 3) {
						--var8;
						++var20.zCoord;
					}

					int var21 = this.getBlockId(var6, var7, var8);
					Block var23 = Block.blocksList[var21];
					if(var21 > 0 && var23.getMaterial() == Material.air) {
						MovingObjectPosition var22;
						if(var23.renderAsNormalBlock()) {
							var22 = var23.collisionRayTrace(var6, var7, var8, var1, var2);
							if(var22 != null) {
								return var22;
							}
						} else {
							var22 = var23.collisionRayTrace(var6, var7, var8, var1, var2);
							if(var22 != null) {
								return var22;
							}
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

	public final boolean growTrees(int var1, int var2, int var3) {
		int var4 = this.random.nextInt(3) + 4;
		boolean var5 = true;

		int var6;
		int var8;
		int var9;
		for(var6 = var2; var6 <= var2 + 1 + var4; ++var6) {
			byte var7 = 1;
			if(var6 == var2) {
				var7 = 0;
			}

			if(var6 >= var2 + 1 + var4 - 2) {
				var7 = 2;
			}

			for(var8 = var1 - var7; var8 <= var1 + var7 && var5; ++var8) {
				for(var9 = var3 - var7; var9 <= var3 + var7 && var5; ++var9) {
					if(var8 >= 0 && var6 >= 0 && var9 >= 0 && var8 < this.width && var6 < this.height && var9 < this.length) {
						int var10000 = this.blocks[(var6 * this.length + var9) * this.width + var8] & 255;
						boolean var10 = false;
						if(var10000 != 0) {
							var5 = false;
						}
					} else {
						var5 = false;
					}
				}
			}
		}

		if(!var5) {
			return false;
		} else if((this.blocks[((var2 - 1) * this.length + var3) * this.width + var1] & 255) == Block.grass.blockID && var2 < this.height - var4 - 1) {
			this.setBlockWithNotify(var1, var2 - 1, var3, Block.dirt.blockID);

			int var13;
			for(var13 = var2 - 3 + var4; var13 <= var2 + var4; ++var13) {
				var8 = var13 - (var2 + var4);
				var9 = 1 - var8 / 2;

				for(int var14 = var1 - var9; var14 <= var1 + var9; ++var14) {
					int var12 = var14 - var1;

					for(var6 = var3 - var9; var6 <= var3 + var9; ++var6) {
						int var11 = var6 - var3;
						if(Math.abs(var12) != var9 || Math.abs(var11) != var9 || this.random.nextInt(2) != 0 && var8 != 0) {
							this.setBlockWithNotify(var14, var13, var6, Block.leaves.blockID);
						}
					}
				}
			}

			for(var13 = 0; var13 < var4; ++var13) {
				this.setBlockWithNotify(var1, var2 + var13, var3, Block.log.blockID);
			}

			return true;
		} else {
			return false;
		}
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final void spawnEntityInWorld(Entity var1) {
		EntityMap var2 = this.entityMap;
		var2.entities.add(var1);
		var2.slot0.init(var1.posX, var1.posY, var1.posZ).add(var1);
		var1.lastTickPosX = var1.posX;
		var1.lastTickPosY = var1.posY;
		var1.lastTickPosZ = var1.posZ;
		var1.worldObj = this;
	}

	public final void releaseEntitySkin(Entity var1) {
		EntityMap var2 = this.entityMap;
		var2.slot0.init(var1.lastTickPosX, var1.lastTickPosY, var1.lastTickPosZ).remove(var1);
		var2.entities.remove(var1);
	}

    public final void createExplosion(Entity var1, float var2, float var3, float var4, float var5) {
        int var18 = (int)(var2 - var5 - 1.0F);
        int var6 = (int)(var2 + var5 + 1.0F);
        int var7 = (int)(var3 - var5 - 1.0F);
        int var8 = (int)(var3 + var5 + 1.0F);
        int var9 = (int)(var4 - var5 - 1.0F);
        int var10 = (int)(var4 + var5 + 1.0F);

        int var12;
        float var14;
        float var15;
        for(int var11 = var18; var11 < var6; ++var11) {
            for(var12 = var8 - 1; var12 >= var7; --var12) {
                for(int var13 = var9; var13 < var10; ++var13) {
                    var14 = (float)var11 + 0.5F - var2;
                    var15 = (float)var12 + 0.5F - var3;
                    float var16 = (float)var13 + 0.5F - var4;
                    if(var11 >= 0 && var12 >= 0 && var13 >= 0 && var11 < this.width && var12 < this.height && var13 < this.length && var14 * var14 + var15 * var15 + var16 * var16 < var5 * var5) {
                        int var27 = this.getBlockId(var11, var12, var13);
                        if(var27 > 0 && Block.blocksList[var27].canDrop()) {
                            Block.blocksList[var27].dropBlockAsItemWithChance(this, var11, var12, var13, 0.3F);
                            this.setBlockWithNotify(var11, var12, var13, 0);
                            Block.blocksList[var27].onBlockDestroyedByExplosion(this, var11, var12, var13);
                        }
                    }
                }
            }
        }

        float var10001 = (float)var18;
        float var10002 = (float)var7;
        float var10003 = (float)var9;
        float var10004 = (float)var6;
        float var10005 = (float)var8;
        float var24 = (float)var10;
        float var23 = var10005;
        float var22 = var10004;
        float var21 = var10003;
        float var20 = var10002;
        float var19 = var10001;
        var1 = null;
        EntityMap var17 = this.entityMap;
        var17.entitiesExcludingEntity.clear();
        List var25 = var17.getEntitiesWithinAABBExcludingEntity((Entity)null, var19, var20, var21, var22, var23, var24, var17.entitiesExcludingEntity);

        for(var12 = 0; var12 < var25.size(); ++var12) {
            Entity var26 = (Entity)var25.get(var12);
            var21 = var26.posX - var2;
            var22 = var26.posY - var3;
            var23 = var26.posZ - var4;
            var14 = MathHelper.sqrt_float(var21 * var21 + var22 * var22 + var23 * var23) / var5;
            if(var14 <= 1.0F) {
                var15 = 1.0F - var14;
                var26.attackEntityFrom((Entity)null, (int)(var15 * 15.0F + 1.0F));
            }
        }

    }

    public final Entity findSubclassOf(Class var1) {
        for(int var2 = 0; var2 < this.entityMap.entities.size(); ++var2) {
            Entity var3 = (Entity)this.entityMap.entities.get(var2);
            if(var1.isAssignableFrom(var3.getClass())) {
                return var3;
            }
        }

        return null;
    }

    public final int getMapHeight(int var1, int var2) {
        return this.heightMap[var1 + var2 * this.width];
    }

	static {
		for(int var0 = 0; var0 <= 8; ++var0) {
			float var1 = 1.0F - (float)var0 / 8.0F;
			lightBrightnessTable[var0] = (1.0F - var1) / (var1 * 3.0F + 1.0F) * 0.9F + 0.1F;
		}

	}
}
