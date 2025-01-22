package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;

import net.lax1dude.eaglercraft.EaglercraftRandom;

import java.util.ArrayList;

public final class Level {
	public int width;
	public int height;
	public int depth;
	public byte[] blocks;
	private int[] heightMap;
	ArrayList levelListeners = new ArrayList();
	public EaglercraftRandom random = new EaglercraftRandom();
	public int randValue = this.random.nextInt();
	public String name;
	public String creator;
	public long createTime;
	public int unprocessed = 0;

	public final void setData(int i1, int i2, int i3, byte[] b4) {
		this.width = i1;
		this.height = i3;
		this.depth = i2;
		this.blocks = b4;
		this.heightMap = new int[i1 * i3];
		this.calcLightDepths(0, 0, i1, i3);

		for(i1 = 0; i1 < this.levelListeners.size(); ++i1) {
			((LevelRenderer)this.levelListeners.get(i1)).allChanged();
		}

	}

	private void calcLightDepths(int i1, int i2, int i3, int i4) {
		for(int i5 = i1; i5 < i1 + i3; ++i5) {
			for(int i6 = i2; i6 < i2 + i4; ++i6) {
				int i7 = this.heightMap[i5 + i6 * this.width];

				int i8;
				Tile tile14;
				for(i8 = this.depth - 1; i8 > 0 && !((tile14 = Tile.tiles[this.getTile(i5, i8, i6)]) == null ? false : tile14.blocksLight()); --i8) {
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

	public final ArrayList getCubes(AABB aABB1) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = (int)Math.floor((double)aABB1.x0);
		int i4 = (int)Math.floor((double)(aABB1.x1 + 1.0F));
		int i5 = (int)Math.floor((double)aABB1.y0);
		int i6 = (int)Math.floor((double)(aABB1.y1 + 1.0F));
		int i7 = (int)Math.floor((double)aABB1.z0);
		int i12 = (int)Math.floor((double)(aABB1.z1 + 1.0F));

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i8 = i5; i8 < i6; ++i8) {
				for(int i9 = i7; i9 < i12; ++i9) {
					AABB aABB10;
					if(i3 >= 0 && i8 >= 0 && i9 >= 0 && i3 < this.width && i8 < this.depth && i9 < this.height) {
						Tile tile11;
						if((tile11 = Tile.tiles[this.getTile(i3, i8, i9)]) != null && (aABB10 = tile11.getAABB(i3, i8, i9)) != null) {
							arrayList2.add(aABB10);
						}
					} else if((i3 < 0 || i8 < 0 || i9 < 0 || i3 >= this.width || i9 >= this.height) && (aABB10 = Tile.unbreakable.getAABB(i3, i8, i9)) != null) {
						arrayList2.add(aABB10);
					}
				}
			}
		}

		return arrayList2;
	}

	public final boolean setTile(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height) {
			if(i4 == this.blocks[(i2 * this.height + i3) * this.width + i1]) {
				return false;
			} else {
				this.blocks[(i2 * this.height + i3) * this.width + i1] = (byte)i4;
				this.updateNeighborAt(i1 - 1, i2, i3, i4);
				this.updateNeighborAt(i1 + 1, i2, i3, i4);
				this.updateNeighborAt(i1, i2 - 1, i3, i4);
				this.updateNeighborAt(i1, i2 + 1, i3, i4);
				this.updateNeighborAt(i1, i2, i3 - 1, i4);
				this.updateNeighborAt(i1, i2, i3 + 1, i4);
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

	public final boolean setTileNoUpdate(int i1, int i2, int i3, int i4) {
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

	public final boolean isLit(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height ? i2 >= this.heightMap[i1 + i3 * this.width] : true;
	}

	public final int getTile(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.depth && i3 < this.height ? this.blocks[(i2 * this.height + i3) * this.width + i1] : 0;
	}

	public final boolean containsLiquid(AABB aABB1, int i2) {
		int i3 = (int)Math.floor((double)aABB1.x0);
		int i4 = (int)Math.floor((double)(aABB1.x1 + 1.0F));
		int i5 = (int)Math.floor((double)aABB1.y0);
		int i6 = (int)Math.floor((double)(aABB1.y1 + 1.0F));
		int i7 = (int)Math.floor((double)aABB1.z0);
		int i11 = (int)Math.floor((double)(aABB1.z1 + 1.0F));
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

		if(i11 > this.height) {
			i11 = this.height;
		}

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i8 = i5; i8 < i6; ++i8) {
				for(int i9 = i7; i9 < i11; ++i9) {
					Tile tile10;
					if((tile10 = Tile.tiles[this.getTile(i3, i8, i9)]) != null && tile10.getLiquidType() == i2) {
						return true;
					}
				}
			}
		}

		return false;
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
                } while ((tile = this.getTile(i6, i7, i8)) <= 0 || Tile.tiles[tile].getLiquidType() != 0);

                return new HitResult(0, i6, i7, i8, b21);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
