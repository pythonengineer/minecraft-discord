package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class Chunk {
	private boolean skippedRenderPass = true;
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
			int i9 = i8;
			if(!this.skippedRenderPass || i8 == 0 || i8 == 7) {
				this.skipRenderPass[i8] = true;
				GL11.glNewList(this.lists + i8, GL11.GL_COMPILE);
				t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
			}

			if(this.skippedRenderPass && i8 > 0 && i8 < 7) {
				i9 = 0;
			}

			byte b10 = 0;
			byte b11 = 0;
			byte b12 = 0;
			float f13 = 1.0F;
			if(i8 == 2 || i8 == 3) {
				f13 = 0.6F;
			}

			if(i8 == 4 || i8 == 5) {
				f13 = 0.8F;
			}

			if(i8 == 0) {
				f13 = 0.5F;
			}

			if(i8 == 0) {
				b11 = -1;
			}

			if(i8 == 1) {
				b11 = 1;
			}

			if(i8 == 2) {
				b12 = -1;
			}

			if(i8 == 3) {
				b12 = 1;
			}

			if(i8 == 4) {
				b10 = -1;
			}

			if(i8 == 5) {
				b10 = 1;
			}

			for(int i14 = i3; i14 < i6; ++i14) {
				int i15;
				int i16;
				int i17;
				int i18;
				int i20;
				int i21;
				float f22;
				Tile tile24;
				if(i8 != 5 && i8 != 4) {
					for(i15 = i4; i15 < i7; ++i15) {
						for(i16 = i2; i16 < i5; ++i16) {
							i17 = this.level.getTile(i16, i14, i15);
							if(i8 == 6) {
								if(i17 > 0 && !Tile.isOpaque[i17] && !Tile.isLiquid[i17] && Tile.tiles[i17].render(t, this.level, 0, i16, i14, i15)) {
									this.skipRenderPass[i9] = false;
								}
							} else if(i8 == 7) {
								if(Tile.isLiquid[i17] && Tile.tiles[i17].render(t, this.level, 1, i16, i14, i15)) {
									this.skipRenderPass[i9] = false;
								}
							} else {
								i18 = this.level.getTile(i16 + b10, i14 + b11, i15 + b12);
								if(Tile.isOpaque[i17] && !Tile.isLiquid[i17] && !Tile.isSolid[i18]) {
									i18 = 0;
									float f25 = f13 * this.level.getBrightness(i16 + b10, i14 + b11, i15 + b12);
									if(isLit) {
										while(i16 + i18 < i5) {
											++i18;
											i20 = this.level.getTile(i16 + i18, i14, i15);
											i21 = this.level.getTile(i16 + i18 + b10, i14 + b11, i15 + b12);
											f22 = f13 * this.level.getBrightness(i16 + i18 + b10, i14 + b11, i15 + b12);
											if(f25 != f22 || i20 != i17 || !Tile.isOpaque[i20] || Tile.isSolid[i21]) {
												break;
											}
										}
									} else {
										i18 = 1;
									}

									tile24 = Tile.tiles[i17];
									t.color(f25, f25, f25);
									tile24.renderFace(t, i16, i14, i15, i8, i18 - 1);
									this.skipRenderPass[i9] = false;
									i16 += i18 - 1;
								}
							}
						}
					}
				} else {
					for(i15 = i2; i15 < i5; ++i15) {
						for(i16 = i4; i16 < i7; ++i16) {
							i17 = this.level.getTile(i15, i14, i16);
							i18 = this.level.getTile(i15 + b10, i14 + b11, i16 + b12);
							if(Tile.isOpaque[i17] && !Tile.isLiquid[i17] && !Tile.isSolid[i18]) {
								float f23 = f13 * this.level.getBrightness(i15 + b10, i14 + b11, i16 + b12);
								int i19 = 0;
								if(isLit) {
									while(i16 + i19 < i7) {
										++i19;
										i20 = this.level.getTile(i15, i14, i16 + i19);
										i21 = this.level.getTile(i15 + b10, i14 + b11, i16 + i19 + b12);
										f22 = f13 * this.level.getBrightness(i15 + b10, i14 + b11, i16 + i19 + b12);
										if(f23 != f22 || i20 != i17 || !Tile.isOpaque[i20] || Tile.isSolid[i21]) {
											break;
										}
									}
								} else {
									i19 = 1;
								}

								tile24 = Tile.tiles[i17];
								t.color(f23, f23, f23);
								tile24.renderFace(t, i15, i14, i16, i8, i19 - 1);
								this.skipRenderPass[i9] = false;
								i16 += i19 - 1;
							}
						}
					}
				}
			}

			if(!this.skippedRenderPass || i8 == 6 || i8 == 7) {
				t.end();
				GL11.glEndList();
			}
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
				if(this.skippedRenderPass) {
					if(!this.skipRenderPass[0]) {
						chunkBuffer[startingIndex++] = this.lists;
					}
				} else {
					if(!this.skipRenderPass[0] && y < (float)(this.y0 + this.y1) + 0.1F) {
						chunkBuffer[startingIndex++] = this.lists;
					}

					if(!this.skipRenderPass[1] && y > (float)this.y0 - 0.1F) {
						chunkBuffer[startingIndex++] = this.lists + 1;
					}

					if(!this.skipRenderPass[2] && z < (float)(this.z0 + this.z1) + 0.1F) {
						chunkBuffer[startingIndex++] = this.lists + 2;
					}

					if(!this.skipRenderPass[3] && z > (float)this.z0 - 0.1F) {
						chunkBuffer[startingIndex++] = this.lists + 3;
					}

					if(!this.skipRenderPass[4] && x < (float)(this.x0 + this.x1) + 0.1F) {
						chunkBuffer[startingIndex++] = this.lists + 4;
					}

					if(!this.skipRenderPass[5] && x > (float)this.x0 - 0.1F) {
						chunkBuffer[startingIndex++] = this.lists + 5;
					}

					if(!this.skipRenderPass[6]) {
						chunkBuffer[startingIndex++] = this.lists + 6;
					}
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