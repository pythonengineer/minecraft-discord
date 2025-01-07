package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Level {
	private static final int TILE_UPDATE_INTERVAL = 400;
	public final int width;
	public final int height;
	public final int depth;
	private byte[] blocks;
	private int[] lightDepths;
	private ArrayList<LevelListener> levelListeners = new ArrayList();
	private EaglercraftRandom random = new EaglercraftRandom();
	int unprocessed = 0;
	int randValue = this.random.nextInt();

	public Level(int w, int h, int d) {
		this.width = w;
		this.height = h;
		this.depth = d;
		this.blocks = new byte[w * h * d];
		this.lightDepths = new int[w * h];
		boolean mapLoaded = this.load();
		if(!mapLoaded) {
            this.blocks = (new LevelGen(w, h, d)).generateMap();
		}

		this.calcLightDepths(0, 0, w, h);
	}

	public boolean load() {
		try {
            VFile2 f = new VFile2("level.dat");
            if (f.exists()) {
                DataInputStream e = new DataInputStream(
                PlatformRuntime.newGZIPInputStream(new VFile2("level.dat").getInputStream()));
                e.readFully(this.blocks);
                e.close();
            } else {
                return false;
            }

			this.calcLightDepths(0, 0, this.width, this.height);

			for(int i = 0; i < this.levelListeners.size(); ++i) {
				((LevelListener)this.levelListeners.get(i)).allChanged();
			}

			return true;
		} catch (IOException var3) {
			var3.printStackTrace();
			return false;
		}
	}

	public void save() {
		try {
            DataOutputStream e = new DataOutputStream(
            PlatformRuntime.newGZIPOutputStream(new VFile2("level.dat").getOutputStream()));
            e.write(this.blocks);
            e.close();
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public void delete() {
        VFile2 f = new VFile2("level.dat");
        if (f.exists()) {
            f.delete();
        }
	}

	public void calcLightDepths(int x0, int y0, int x1, int y1) {
		for(int x = x0; x < x0 + x1; ++x) {
			for(int z = y0; z < y0 + y1; ++z) {
				int oldDepth = this.lightDepths[x + z * this.width];

				int y;
				for(y = this.depth - 1; y > 0 && !this.isLightBlocker(x, y, z); --y) {
				}

				this.lightDepths[x + z * this.width] = y;
				if(oldDepth != y) {
					int yl0 = oldDepth < y ? oldDepth : y;
					int yl1 = oldDepth > y ? oldDepth : y;

					for(int i = 0; i < this.levelListeners.size(); ++i) {
						((LevelListener)this.levelListeners.get(i)).lightColumnChanged(x, z, yl0, yl1);
					}
				}
			}
		}

	}

	public void addListener(LevelListener levelListener) {
		this.levelListeners.add(levelListener);
	}

	public void removeListener(LevelListener levelListener) {
		this.levelListeners.remove(levelListener);
	}

	public boolean isLightBlocker(int x, int y, int z) {
		Tile tile = Tile.tiles[this.getTile(x, y, z)];
		return tile == null ? false : tile.blocksLight();
	}

	public ArrayList<AABB> getCubes(AABB aABB) {
		ArrayList aABBs = new ArrayList();
		int x0 = (int)aABB.x0;
		int x1 = (int)(aABB.x1 + 1.0F);
		int y0 = (int)aABB.y0;
		int y1 = (int)(aABB.y1 + 1.0F);
		int z0 = (int)aABB.z0;
		int z1 = (int)(aABB.z1 + 1.0F);
		if(x0 < 0) {
			x0 = 0;
		}

		if(y0 < 0) {
			y0 = 0;
		}

		if(z0 < 0) {
			z0 = 0;
		}

		if(x1 > this.width) {
			x1 = this.width;
		}

		if(y1 > this.depth) {
			y1 = this.depth;
		}

		if(z1 > this.height) {
			z1 = this.height;
		}

		for(int x = x0; x < x1; ++x) {
			for(int y = y0; y < y1; ++y) {
				for(int z = z0; z < z1; ++z) {
					Tile tile = Tile.tiles[this.getTile(x, y, z)];
					if(tile != null) {
                        AABB aabb = tile.getAABB(x, y, z);
                        if(aabb != null) {
                            aABBs.add(aabb);
                        }
					}
				}
			}
		}

		return aABBs;
	}

	public boolean setTile(int x, int y, int z, int type) {
		if(x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
			if(type == this.blocks[(y * this.height + z) * this.width + x]) {
				return false;
			} else {
				this.blocks[(y * this.height + z) * this.width + x] = (byte)type;
				this.calcLightDepths(x, z, 1, 1);

				for(int i = 0; i < this.levelListeners.size(); ++i) {
					((LevelListener)this.levelListeners.get(i)).tileChanged(x, y, z);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public boolean isLit(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height ? y >= this.lightDepths[x + z * this.width] : true;
	}

	public int getTile(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height ? this.blocks[(y * this.height + z) * this.width + x] : 0;
	}

	public boolean isSolidTile(int x, int y, int z) {
		Tile tile = Tile.tiles[this.getTile(x, y, z)];
		return tile == null ? false : tile.isSolid();
	}

	public void tick() {
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

		this.unprocessed += this.width * this.height * this.depth;
		int ticks = this.unprocessed / TILE_UPDATE_INTERVAL;
		this.unprocessed -= ticks * TILE_UPDATE_INTERVAL;

		for(int i = 0; i < ticks; ++i) {
		    this.randValue = this.randValue * 3 + 1013904223;
		    int y = this.randValue >> 2;
		    int x = y & i4;
		    int z = y >> i1 & i3;
		    y = y >> i1 + i2 & i5;
			Tile tile = Tile.tiles[this.getTile(x, y, z)];
			if(tile != null) {
				tile.tick(this, x, y, z, this.random);
			}
		}
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
                } while (this.getTile(i6, i7, i8) == 0);

                return new HitResult(0, i6, i7, i8, b21);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
