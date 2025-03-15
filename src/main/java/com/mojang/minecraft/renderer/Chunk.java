package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class Chunk {
	private Level level;
	private int lists = -1;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private int x0;
	private int y0;
	private int z0;
	private int x1;
	private int y1;
	private int z1;
	public boolean isInFrustum = false;
	private boolean[] skipRenderPass = new boolean[8];

	public Chunk(Level level, int x, int y, int z, int id1, int id2) {
		this.level = level;
		this.x0 = x;
		this.y0 = y;
		this.z0 = z;
		this.x1 = this.y1 = this.z1 = 16;
		Math.sqrt((double)(this.x1 * this.x1 + this.y1 * this.y1 + this.z1 * this.z1));
		this.lists = id2;
		this.reset();
	}

	public final void rebuild(boolean isLit) {
		Tile.isNormalTile = isLit;
		++updates;
		int i2 = this.x0;
		int i3 = this.y0;
		int i4 = this.z0;
		int i5 = this.x0 + this.x1;
		int i6 = this.y0 + this.y1;
		int i7 = this.z0 + this.z1;

		for(int i8 = 0; i8 < 8; ++i8) {
			this.skipRenderPass[i8] = true;
			GL11.glNewList(this.lists + i8, GL11.GL_COMPILE);
			t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
			byte b9 = 0;
			byte b10 = 0;
			byte b11 = 0;
			float f12 = 1.0F;
			if(i8 == 2 || i8 == 3) {
				f12 = 0.6F;
			}

			if(i8 == 4 || i8 == 5) {
				f12 = 0.8F;
			}

			if(i8 == 0) {
				f12 = 0.5F;
			}

			if(i8 == 0) {
				b10 = -1;
			}

			if(i8 == 1) {
				b10 = 1;
			}

			if(i8 == 2) {
				b11 = -1;
			}

			if(i8 == 3) {
				b11 = 1;
			}

			if(i8 == 4) {
				b9 = -1;
			}

			if(i8 == 5) {
				b9 = 1;
			}

			for(int i13 = i3; i13 < i6; ++i13) {
				int i14;
				int i15;
				int i16;
				int i17;
				int i19;
				int i20;
				float f21;
				Tile tile23;
				if(i8 != 5 && i8 != 4) {
					for(i14 = i4; i14 < i7; ++i14) {
						for(i15 = i2; i15 < i5; ++i15) {
							i16 = this.level.getTile(i15, i13, i14);
							if(i8 == 6) {
								if(i16 > 0 && !Tile.isOpaque[i16] && !Tile.isLiquid[i16] && Tile.tiles[i16].render(t, this.level, 0, i15, i13, i14)) {
									this.skipRenderPass[i8] = false;
								}
							} else if(i8 == 7) {
								if(Tile.isLiquid[i16] && Tile.tiles[i16].render(t, this.level, 1, i15, i13, i14)) {
									this.skipRenderPass[i8] = false;
								}
							} else {
								i17 = this.level.getTile(i15 + b9, i13 + b10, i14 + b11);
								if(Tile.isOpaque[i16] && !Tile.isLiquid[i16] && !Tile.isSolid[i17]) {
									i17 = 0;
									float f24 = f12 * this.level.getBrightness(i15 + b9, i13 + b10, i14 + b11);
									if(isLit) {
										while(i15 + i17 < i5) {
											++i17;
											i19 = this.level.getTile(i15 + i17, i13, i14);
											i20 = this.level.getTile(i15 + i17 + b9, i13 + b10, i14 + b11);
											f21 = f12 * this.level.getBrightness(i15 + i17 + b9, i13 + b10, i14 + b11);
											if(f24 != f21 || i19 != i16 || !Tile.isOpaque[i19] || Tile.isSolid[i20]) {
												break;
											}
										}
									} else {
										i17 = 1;
									}

									tile23 = Tile.tiles[i16];
									t.color(f24, f24, f24);
									tile23.renderFace(t, i15, i13, i14, i8, i17 - 1);
									this.skipRenderPass[i8] = false;
									i15 += i17 - 1;
								}
							}
						}
					}
				} else {
					for(i14 = i2; i14 < i5; ++i14) {
						for(i15 = i4; i15 < i7; ++i15) {
							i16 = this.level.getTile(i14, i13, i15);
							i17 = this.level.getTile(i14 + b9, i13 + b10, i15 + b11);
							if(Tile.isOpaque[i16] && !Tile.isLiquid[i16] && !Tile.isSolid[i17]) {
								float f22 = f12 * this.level.getBrightness(i14 + b9, i13 + b10, i15 + b11);
								int i18 = 0;
								if(isLit) {
									while(i15 + i18 < i7) {
										++i18;
										i19 = this.level.getTile(i14, i13, i15 + i18);
										i20 = this.level.getTile(i14 + b9, i13 + b10, i15 + i18 + b11);
										f21 = f12 * this.level.getBrightness(i14 + b9, i13 + b10, i15 + i18 + b11);
										if(f22 != f21 || i19 != i16 || !Tile.isOpaque[i19] || Tile.isSolid[i20]) {
											break;
										}
									}
								} else {
									i18 = 1;
								}

								tile23 = Tile.tiles[i16];
								t.color(f22, f22, f22);
								tile23.renderFace(t, i14, i13, i15, i8, i18 - 1);
								this.skipRenderPass[i8] = false;
								i15 += i18 - 1;
							}
						}
					}
				}
			}

			t.end();
			GL11.glEndList();
		}

		Tile.isNormalTile = false;
	}

	public final float compare(Player player) {
		float f2 = player.x - (float)this.x0;
		float f3 = player.y - (float)this.y0;
		float f4 = player.z - (float)this.z0;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void reset() {
		for(int i1 = 0; i1 < 8; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void clear() {
		this.reset();
		this.level = null;
	}

	public final int render(int[] chunkBuffer, int startingIndex, int renderPass, float x, float y, float z) {
		if(!this.isInFrustum) {
			return startingIndex;
		} else {
			if(renderPass == 0) {
				if(!this.skipRenderPass[0] && y < (float)(this.y0 + this.y1) + 0.5F) {
					chunkBuffer[startingIndex++] = this.lists;
				}

				if(!this.skipRenderPass[1] && y > (float)this.y0 - 0.5F) {
					chunkBuffer[startingIndex++] = this.lists + 1;
				}

				if(!this.skipRenderPass[2] && z < (float)(this.z0 + this.z1) + 0.5F) {
					chunkBuffer[startingIndex++] = this.lists + 2;
				}

				if(!this.skipRenderPass[3] && z > (float)this.z0 - 0.5F) {
					chunkBuffer[startingIndex++] = this.lists + 3;
				}

				if(!this.skipRenderPass[4] && x < (float)(this.x0 + this.x1) + 0.5F) {
					chunkBuffer[startingIndex++] = this.lists + 4;
				}

				if(!this.skipRenderPass[5] && x > (float)this.x0 - 0.5F) {
					chunkBuffer[startingIndex++] = this.lists + 5;
				}

				if(!this.skipRenderPass[6]) {
					chunkBuffer[startingIndex++] = this.lists + 6;
				}
			} else if(!this.skipRenderPass[7]) {
				chunkBuffer[startingIndex++] = this.lists + 7;
			}

			return startingIndex;
		}
	}

	public final void isInFrustum(Frustum frustum) {
		this.isInFrustum = frustum.cubeInFrustum((float)this.x0, (float)this.y0, (float)this.z0, (float)(this.x0 + this.x1), (float)(this.y0 + this.y1), (float)(this.z0 + this.z1));
	}
}