package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.sound.EntitySoundPos;
import com.mojang.minecraft.sound.LevelSoundPos;
import com.mojang.minecraft.sound.Sound;

import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Level implements Serializable {
	public static final long serialVersionUID = 0L;
	public int width;
	public int height;
	public int depth;
    public byte[] blocks;
	public String name;
	public String creator;
	public long createTime;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	private transient ArrayList levelListeners = new ArrayList();
	private transient int[] heightMap;
	private transient EaglercraftRandom random = new EaglercraftRandom();
	private transient int randValue = this.random.nextInt();
	private transient ArrayList tickList = new ArrayList();
	public ArrayList entities = new ArrayList();
    private boolean networkMode = false;
    public transient Minecraft rendererContext;
	int unprocessed = 0;
	private int tickCount = 0;

	public void initTransient() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.levelListeners = new ArrayList();
			this.heightMap = new int[this.width * this.height];
            Arrays.fill(this.heightMap, this.depth);
			this.calcLightDepths(0, 0, this.width, this.height);
			this.random = new EaglercraftRandom();
			this.randValue = this.random.nextInt();
			this.tickList = new ArrayList();
			if(this.entities == null) {
				this.entities = new ArrayList();
			}

			if(this.xSpawn == 0 && this.ySpawn == 0 && this.zSpawn == 0) {
				this.findSpawn();
			}

		}
	}

	public void setData(int i1, int i2, int i3, byte[] b4) {
		this.width = i1;
		this.height = i3;
		this.depth = i2;
		this.blocks = b4;
		this.heightMap = new int[i1 * i3];
        Arrays.fill(this.heightMap, this.depth);
		this.calcLightDepths(0, 0, i1, i3);

		for(i1 = 0; i1 < this.levelListeners.size(); ++i1) {
			((LevelRenderer)this.levelListeners.get(i1)).compileSurroundingGround();
		}

		this.tickList.clear();
		this.findSpawn();
        System.gc();
	}

	public void findSpawn() {
		EaglercraftRandom random1 = new EaglercraftRandom();
		int i2 = 0;

		int i3;
		int i4;
		int i5;
		do {
			++i2;
			i3 = random1.nextInt(this.width / 2) + this.width / 4;
			i4 = random1.nextInt(this.height / 2) + this.height / 4;
			i5 = this.getHighestTile(i3, i4) + 1;
			if(i2 == 10000) {
				this.xSpawn = i3;
				this.ySpawn = -100;
				this.zSpawn = i4;
				return;
			}
		} while((float)i5 <= this.getWaterLevel());

		this.xSpawn = i3;
		this.ySpawn = i5;
		this.zSpawn = i4;
	}

	public void calcLightDepths(int i1, int i2, int i3, int i4) {
		for(int i5 = i1; i5 < i1 + i3; ++i5) {
			for(int i6 = i2; i6 < i2 + i4; ++i6) {
				int i7 = this.heightMap[i5 + i6 * this.width];

				int i8;
				for(i8 = this.depth - 1; i8 > 0 && !this.isLightBlocker(i5, i8, i6); --i8) {
				}

				this.heightMap[i5 + i6 * this.width] = i8 + 1;
				if(i7 != i8) {
					int i9 = i7 < i8 ? i7 : i8;
					i7 = i7 > i8 ? i7 : i8;

					for(i8 = 0; i8 < this.levelListeners.size(); ++i8) {
						((LevelRenderer)this.levelListeners.get(i8)).setDirty(i5 - 1, i9 - 1, i6 - 1, i5 + 1, i7 + 1, i6 + 1);
					}
				}
			}
		}

	}

	public void addListener(LevelRenderer levelRenderer1) {
		this.levelListeners.add(levelRenderer1);
	}

	public void finalize() {
	}

	public void removeListener(LevelRenderer levelRenderer1) {
		this.levelListeners.remove(levelRenderer1);
	}

	public boolean isLightBlocker(int i1, int i2, int i3) {
		Tile tile4;
		return (tile4 = Tile.tiles[this.getTile(i1, i2, i3)]) == null ? false : tile4.blocksLight();
	}

	public ArrayList getCubes(AABB aABB1) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = (int)aABB1.x0;
		int i4 = (int)aABB1.x1 + 1;
		int i5 = (int)aABB1.y0;
		int i6 = (int)aABB1.y1 + 1;
		int i7 = (int)aABB1.z0;
		int i8 = (int)aABB1.z1 + 1;
		if(aABB1.x0 < 0.0F) {
			--i3;
		}

		if(aABB1.y0 < 0.0F) {
			--i5;
		}

		if(aABB1.z0 < 0.0F) {
			--i7;
		}

		for(int i11 = i3; i11 < i4; ++i11) {
			for(i3 = i5; i3 < i6; ++i3) {
				for(int i9 = i7; i9 < i8; ++i9) {
					AABB aABB10;
                    if(i11 >= 0 && i3 >= 0 && i9 >= 0 && i11 < this.width && i3 < this.depth && i9 < this.height) {
                        Tile tile12;
                        if((tile12 = Tile.tiles[this.getTile(i11, i3, i9)]) != null && (aABB10 = tile12.getTileAABB(i11, i3, i9)) != null) {
                            arrayList2.add(aABB10);
                        }
                    } else if((i11 < 0 || i3 < 0 || i9 < 0 || i11 >= this.width || i9 >= this.height) && (aABB10 = Tile.unbreakable.getTileAABB(i11, i3, i9)) != null) {
                        arrayList2.add(aABB10);
                    }
				}
			}
		}

		return arrayList2;
	}

	public void swap(int i1, int i2, int i3, int i4, int i5, int i6) {
        if(!this.networkMode) {
            int i7 = this.getTile(i1, i2, i3);
            int i8 = this.getTile(i4, i5, i6);
            this.setTileNoNeighborChange(i1, i2, i3, i8);
            this.setTileNoNeighborChange(i4, i5, i6, i7);
            this.updateNeighborsAt(i1, i2, i3, i8);
            this.updateNeighborsAt(i4, i5, i6, i7);
        }
	}

	public boolean setTileNoNeighborChange(int i1, int i2, int i3, int i4) {
        return this.networkMode ? false : this.netSetTileNoNeighborChange(i1, i2, i3, i4);
    }

    public boolean netSetTileNoNeighborChange(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height) {
			if(i4 == this.blocks[(i2 * this.height + i3) * this.width + i1]) {
				return false;
			} else {
				if(i4 == 0 && (i1 == 0 || i3 == 0 || i1 == this.width - 1 || i3 == this.height - 1) && (float)i2 >= this.getGroundLevel() && (float)i2 < this.getWaterLevel()) {
					i4 = Tile.water.id;
				}

                byte b5 = this.blocks[(i2 * this.height + i3) * this.width + i1];
                this.blocks[(i2 * this.height + i3) * this.width + i1] = (byte)i4;
                if(b5 != 0) {
                    Tile.tiles[b5].onTileRemoved(this, i1, i2, i3);
                }

                if(i4 != 0) {
                    Tile.tiles[i4].onTileAdded(this, i1, i2, i3);
                }

				this.calcLightDepths(i1, i3, 1, 1);

				for(i4 = 0; i4 < this.levelListeners.size(); ++i4) {
					((LevelRenderer)this.levelListeners.get(i4)).setDirty(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setTile(int i1, int i2, int i3, int i4) {
        if(this.networkMode) {
            return false;
        } else if(this.setTileNoNeighborChange(i1, i2, i3, i4)) {
            this.updateNeighborsAt(i1, i2, i3, i4);
            return true;
        } else {
            return false;
        }
    }

    public boolean netSetTile(int i1, int i2, int i3, int i4) {
        if(this.netSetTileNoNeighborChange(i1, i2, i3, i4)) {
            this.updateNeighborsAt(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
	}

    public void updateNeighborsAt(int i1, int i2, int i3, int i4) {
        this.updateNeighborAt(i1 - 1, i2, i3, i4);
        this.updateNeighborAt(i1 + 1, i2, i3, i4);
        this.updateNeighborAt(i1, i2 - 1, i3, i4);
        this.updateNeighborAt(i1, i2 + 1, i3, i4);
        this.updateNeighborAt(i1, i2, i3 - 1, i4);
        this.updateNeighborAt(i1, i2, i3 + 1, i4);
	}

	public boolean setTileNoUpdate(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height) {
			if(i4 == this.blocks[(i2 * this.height + i3) * this.width + i1]) {
				return false;
			} else {
				this.blocks[(i2 * this.height + i3) * this.width + i1] = (byte)i4;
				return true;
			}
		} else {
			return false;
		}
	}

    private void updateNeighborAt(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height) {
			Tile tile5;
			if((tile5 = Tile.tiles[this.blocks[(i2 * this.height + i3) * this.width + i1]]) != null) {
				tile5.neighborChanged(this, i1, i2, i3, i4);
			}

		}
	}

	public boolean isLit(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height ? i2 >= this.heightMap[i1 + i3 * this.width] : true;
	}

	public int getTile(int i1, int i2, int i3) {
        return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height ? this.blocks[(i2 * this.height + i3) * this.width + i1] & 255 : 0;
	}

	public boolean isSolidTile(int i1, int i2, int i3) {
		Tile tile4;
		return (tile4 = Tile.tiles[this.getTile(i1, i2, i3)]) == null ? false : tile4.isSolid();
	}

    public void tickEntities() {
        for(int i1 = 0; i1 < this.entities.size(); ++i1) {
            ((Entity)this.entities.get(i1)).tick();
            if(((Entity)this.entities.get(i1)).removed) {
                this.entities.remove(i1--);
            }
        }

    }

    public void tick() {
        ++this.tickCount;
        int i1 = 1;

		int i2;
		for(i2 = 1; 1 << i1 < this.width; ++i1) {
		}

		while(1 << i2 < this.height) {
			++i2;
		}

		int i3 = this.height - 1;
		int i4 = this.width - 1;
		int i5 = this.depth - 1;
		int i6;
		int i7;
        if(this.tickCount % 5 == 0) {
            i6 = this.tickList.size();

            for(i7 = 0; i7 < i6; ++i7) {
                Coord coord8;
                if((coord8 = (Coord)this.tickList.remove(0)).scheduledTime > 0) {
                    --coord8.scheduledTime;
                    this.tickList.add(coord8);
                } else {
                    byte b9;
                    if(this.isInLevelBounds(coord8.x, coord8.y, coord8.z) && (b9 = this.blocks[(coord8.y * this.height + coord8.z) * this.width + coord8.x]) == coord8.id && b9 > 0) {
                        Tile.tiles[b9].tick(this, coord8.x, coord8.y, coord8.z, this.random);
                    }
                }
            }
        }

		this.unprocessed += this.width * this.height * this.depth;
		i6 = this.unprocessed / 200;
		this.unprocessed -= i6 * 200;

		for(i7 = 0; i7 < i6; ++i7) {
			this.randValue = this.randValue * 3 + 1013904223;
			int i12;
			int i13 = (i12 = this.randValue >> 2) & i4;
			int i10 = i12 >> i1 & i3;
			i12 = i12 >> i1 + i2 & i5;
			byte b11 = this.blocks[(i12 * this.height + i10) * this.width + i13];
			if(Tile.shouldTick[b11]) {
				Tile.tiles[b11].tick(this, i13, i12, i10, this.random);
			}
		}

	}

	private boolean isInLevelBounds(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height;
	}

	public float getGroundLevel() {
		return (float)(this.depth / 2 - 2);
	}

	public float getWaterLevel() {
		return (float)(this.depth / 2);
	}

	public boolean containsAnyLiquid(AABB aABB1) {
		int i2 = (int)aABB1.x0;
		int i3 = (int)aABB1.x1 + 1;
		int i4 = (int)aABB1.y0;
		int i5 = (int)aABB1.y1 + 1;
		int i6 = (int)aABB1.z0;
		int i7 = (int)aABB1.z1 + 1;
		if(aABB1.x0 < 0.0F) {
			--i2;
		}

		if(aABB1.y0 < 0.0F) {
			--i4;
		}

		if(aABB1.z0 < 0.0F) {
			--i6;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i4 < 0) {
			i4 = 0;
		}

		if(i6 < 0) {
			i6 = 0;
		}

		if(i3 > this.width) {
			i3 = this.width;
		}

		if(i5 > this.depth) {
			i5 = this.depth;
		}

		if(i7 > this.height) {
			i7 = this.height;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Tile tile9;
                    if((tile9 = Tile.tiles[this.getTile(i10, i2, i8)]) != null && tile9.getLiquidType() != Liquid.none) {
                        return true;
                    }
				}
			}
		}

		return false;
	}

    public boolean containsLiquid(AABB aABB1, Liquid liquid2) {
		int i3 = (int)aABB1.x0;
		int i4 = (int)aABB1.x1 + 1;
		int i5 = (int)aABB1.y0;
		int i6 = (int)aABB1.y1 + 1;
		int i7 = (int)aABB1.z0;
		int i8 = (int)aABB1.z1 + 1;
		if(aABB1.x0 < 0.0F) {
			--i3;
		}

		if(aABB1.y0 < 0.0F) {
			--i5;
		}

		if(aABB1.z0 < 0.0F) {
			--i7;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i5 < 0) {
			i5 = 0;
		}

		if(i7 < 0) {
			i7 = 0;
		}

		if(i4 > this.width) {
			i4 = this.width;
		}

		if(i6 > this.depth) {
			i6 = this.depth;
		}

		if(i8 > this.height) {
			i8 = this.height;
		}

		for(int i11 = i3; i11 < i4; ++i11) {
			for(i3 = i5; i3 < i6; ++i3) {
				for(int i9 = i7; i9 < i8; ++i9) {
					Tile tile10;
                    if((tile10 = Tile.tiles[this.getTile(i11, i3, i9)]) != null && tile10.getLiquidType() == liquid2) {
                        return true;
                    }
				}
			}
		}

		return false;
	}

    public void addToTickNextTick(int i1, int i2, int i3, int i4) {
        if(!this.networkMode) {
            Coord coord5 = new Coord(i1, i2, i3, i4);
            if(i4 > 0) {
                i3 = Tile.tiles[i4].getTickDelay();
                coord5.scheduledTime = i3;
            }

            this.tickList.add(coord5);
        }
    }

	public boolean isFree(AABB aABB1) {
		for(int i2 = 0; i2 < this.entities.size(); ++i2) {
			if(((Entity)this.entities.get(i2)).bb.intersects(aABB1)) {
				return false;
			}
		}

		return true;
	}

	public boolean isSolid(float f1, float f2, float f3, float f4) {
		return this.isSolidTile(f1 - f4, f2 - f4, f3 - f4) ? true : (this.isSolidTile(f1 - f4, f2 - f4, f3 + f4) ? true : (this.isSolidTile(f1 - f4, f2 + f4, f3 - f4) ? true : (this.isSolidTile(f1 - f4, f2 + f4, f3 + f4) ? true : (this.isSolidTile(f1 + f4, f2 - f4, f3 - f4) ? true : (this.isSolidTile(f1 + f4, f2 - f4, f3 + f4) ? true : (this.isSolidTile(f1 + f4, f2 + f4, f3 - f4) ? true : this.isSolidTile(f1 + f4, f2 + f4, f3 + f4)))))));
	}

	private boolean isSolidTile(float f1, float f2, float f3) {
		int i4;
		return (i4 = this.getTile((int)f1, (int)f2, (int)f3)) > 0 && Tile.tiles[i4].isSolid();
	}

	public int getHighestTile(int i1, int i2) {
		int i3;
        for(i3 = this.depth; (this.getTile(i1, i3 - 1, i2) == 0 || Tile.tiles[this.getTile(i1, i3 - 1, i2)].getLiquidType() != Liquid.none) && i3 > 0; --i3) {
        }

		return i3;
	}

	public void setSpawnPos(int i1, int i2, int i3, float f4) {
		this.xSpawn = i1;
		this.ySpawn = i2;
		this.zSpawn = i3;
		this.rotSpawn = f4;
	}

	public float getBrightness(int i1, int i2, int i3) {
        return this.isLit(i1, i2, i3) ? 1.0F : 0.6F;
	}

	public float getCaveness(float f1, float f2, float f3, float f4) {
		int i5 = (int)f1;
		int i14 = (int)f2;
		int i6 = (int)f3;
		float f7 = 0.0F;
		float f8 = 0.0F;

		for(int i9 = i5 - 6; i9 <= i5 + 6; ++i9) {
			for(int i10 = i6 - 6; i10 <= i6 + 6; ++i10) {
				if(this.isInLevelBounds(i9, i14, i10) && !this.isSolidTile(i9, i14, i10)) {
					float f11 = (float)i9 + 0.5F - f1;

					float f12;
					float f13;
					for(f13 = (float)(Math.atan2((double)(f12 = (float)i10 + 0.5F - f3), (double)f11) - (double)f4 * Math.PI / 180.0D + Math.PI / 2D); (double)f13 < -3.141592653589793D; f13 = (float)((double)f13 + Math.PI * 2D)) {
					}

					while((double)f13 >= Math.PI) {
						f13 = (float)((double)f13 - Math.PI * 2D);
					}

					if(f13 < 0.0F) {
						f13 = -f13;
					}

					f11 = (float)Math.sqrt((double)(f11 * f11 + 4.0F + f12 * f12));
					f11 = 1.0F / f11;
					if(f13 > 1.0F) {
						f11 = 0.0F;
					}

					if(f11 < 0.0F) {
						f11 = 0.0F;
					}

					f8 += f11;
					if(this.isLit(i9, i14, i10)) {
						f7 += f11;
					}
				}
			}
		}

		if(f8 == 0.0F) {
			return 0.0F;
		} else {
			return f7 / f8;
		}
	}

    public float getCaveness(Entity entity1) {
        float f2 = (float)Math.cos((double)(-entity1.yRot) * Math.PI / 180.0D + Math.PI);
        float f3 = (float)Math.sin((double)(-entity1.yRot) * Math.PI / 180.0D + Math.PI);
        float f4 = (float)Math.cos((double)(-entity1.xRot) * Math.PI / 180.0D);
        float f5 = (float)Math.sin((double)(-entity1.xRot) * Math.PI / 180.0D);
        float f6 = entity1.x;
        float f7 = entity1.y;
        float f21 = entity1.z;
        float f8 = 1.6F;
        float f9 = 0.0F;
        float f10 = 0.0F;

        for(int i11 = 0; i11 <= 200; ++i11) {
            float f12 = ((float)i11 / (float)200 - 0.5F) * 2.0F;

            for(int i13 = 0; i13 <= 200; ++i13) {
                float f14 = ((float)i13 / (float)200 - 0.5F) * f8;
                float f16 = f4 * f14 + f5;
                f14 = f4 - f5 * f14;
                float f17 = f2 * f12 + f3 * f14;
                f16 = f16;
                f14 = f2 * f14 - f3 * f12;

                for(int i15 = 0; i15 < 10; ++i15) {
                    float f18 = f6 + f17 * (float)i15 * 0.8F;
                    float f19 = f7 + f16 * (float)i15 * 0.8F;
                    float f20 = f21 + f14 * (float)i15 * 0.8F;
                    if(this.isSolidTile(f18, f19, f20)) {
                        break;
                    }

                    ++f9;
                    if(this.isLit((int)f18, (int)f19, (int)f20)) {
                        ++f10;
                    }
                }
            }
        }

        if(f9 == 0.0F) {
            return 0.0F;
        } else {
            float f22;
            if((f22 = f10 / f9 / 0.1F) > 1.0F) {
                f22 = 1.0F;
            }

            f22 = 1.0F - f22;
            return 1.0F - f22 * f22 * f22;
        }
    }

    public byte[] copyBlocks() {
        return Arrays.copyOf(this.blocks, this.blocks.length);
    }

    public boolean isWater(int i1, int i2, int i3) {
        int i4;
        return (i4 = this.getTile(i1, i2, i3)) > 0 && Tile.tiles[i4].getLiquidType() == Liquid.water;
    }

    public void setNetworkMode(boolean z1) {
        this.networkMode = z1;
    }

    public HitResult clip(Vec3 vec31, Vec3 vec32) {
        if (!Float.isNaN(vec31.x) && !Float.isNaN(vec31.y) && !Float.isNaN(vec31.z)) {
            if (!Float.isNaN(vec32.x) && !Float.isNaN(vec32.y) && !Float.isNaN(vec32.z)) {
                int i3 = (int)Math.floor((double)vec32.x);
                int i4 = (int)Math.floor((double)vec32.y);
                int i5 = (int)Math.floor((double)vec32.z);
                int i6 = (int)Math.floor((double)vec31.x);
                int i7 = (int)Math.floor((double)vec31.y);
                int i8 = (int)Math.floor((double)vec31.z);
                int i9 = 20;

                byte b21;
                int tile;
                do {
                    if (i9-- < 0) {
                        return null;
                    }

                    if (Float.isNaN(vec31.x) || Float.isNaN(vec31.y) || Float.isNaN(vec31.z)) {
                        return null;
                    }

                    if (i6 == i3 && i7 == i4 && i8 == i5) {
                        return null;
                    }

                    float f10 = 999.0F;
                    float f11 = 999.0F;
                    float f12 = 999.0F;
                    if (i3 > i6) {
                        f10 = (float)i6 + 1.0F;
                    }

                    if (i3 < i6) {
                        f10 = (float)i6;
                    }

                    if (i4 > i7) {
                        f11 = (float)i7 + 1.0F;
                    }

                    if (i4 < i7) {
                        f11 = (float)i7;
                    }

                    if (i5 > i8) {
                        f12 = (float)i8 + 1.0F;
                    }

                    if (i5 < i8) {
                        f12 = (float)i8;
                    }

                    float f13 = 999.0F;
                    float f14 = 999.0F;
                    float f15 = 999.0F;
                    float f16 = vec32.x - vec31.x;
                    float f17 = vec32.y - vec31.y;
                    float f18 = vec32.z - vec31.z;
                    if (f10 != 999.0F) {
                        f13 = (f10 - vec31.x) / f16;
                    }

                    if (f11 != 999.0F) {
                        f14 = (f11 - vec31.y) / f17;
                    }

                    if (f12 != 999.0F) {
                        f15 = (f12 - vec31.z) / f18;
                    }

                    if (f13 < f14 && f13 < f15) {
                        if (i3 > i6) {
                            b21 = 4;
                        } else {
                            b21 = 5;
                        }

                        vec31.x = f10;
                        vec31.y += f17 * f13;
                        vec31.z += f18 * f13;
                    } else if (f14 < f15) {
                        if (i4 > i7) {
                            b21 = 0;
                        } else {
                            b21 = 1;
                        }

                        vec31.x += f16 * f14;
                        vec31.y = f11;
                        vec31.z += f18 * f14;
                    } else {
                        if (i5 > i8) {
                            b21 = 2;
                        } else {
                            b21 = 3;
                        }

                        vec31.x += f16 * f15;
                        vec31.y += f17 * f15;
                        vec31.z = f12;
                    }

                    i6 = (int)Math.floor((double)vec31.x);
                    if (b21 == 5) {
                        --i6;
                    }

                    i7 = (int)Math.floor((double)vec31.y);
                    if (b21 == 1) {
                        --i7;
                    }

                    i8 = (int)Math.floor((double)vec31.z);
                    if (b21 == 3) {
                        --i8;
                    }
                } while ((tile = this.getTile(i6, i7, i8)) <= 0 || Tile.tiles[tile].getLiquidType() != Liquid.none);

                return new HitResult(0, i6, i7, i8, b21);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void playSound(String string1, Entity entity2, float f3, float f4) {
        if(this.rendererContext != null) {
            Minecraft minecraft5;
            if((minecraft5 = this.rendererContext).soundPlayer == null || !minecraft5.options.sound) {
                return;
            }

            Sound audioInfo6;
            if((audioInfo6 = minecraft5.soundManager.getAudioInfo(string1, f3, f4)) != null) {
                minecraft5.soundPlayer.play(audioInfo6, new EntitySoundPos(entity2, minecraft5.player));
            }
        }

    }

    public void playSound(String string1, float f2, float f3, float f4, float f5, float f6) {
        if(this.rendererContext != null) {
            Minecraft minecraft7;
            if((minecraft7 = this.rendererContext).soundPlayer == null || !minecraft7.options.sound) {
                return;
            }

            Sound audioInfo8;
            if((audioInfo8 = minecraft7.soundManager.getAudioInfo(string1, f5, f6)) != null) {
                minecraft7.soundPlayer.play(audioInfo8, new LevelSoundPos(f2, f3, f4, minecraft7.player));
            }
        }

    }
}
