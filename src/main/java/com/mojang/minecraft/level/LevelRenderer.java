package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.util.Arrays;

public final class LevelRenderer {
	public Level level;
	public Chunk[] chunks;
	private Chunk[] sortedChunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	private Textures textures;
	public int surroundLists;
	public int drawDistance = 0;
	private IntBuffer dummyBuffer = BufferUtils.createIntBuffer(1024);
	private float lX = 0.0F;
	private float lY = 0.0F;
	private float lZ = 0.0F;

	public LevelRenderer(Level level1, Textures textures2) {
		this.level = level1;
		this.textures = textures2;
		level1.levelListeners.add(this);
		this.surroundLists = GL11.glGenLists(2);
		this.allChanged();
	}

	public final void allChanged() {
		this.lX = -900000.0F;
		this.lY = -900000.0F;
		this.lZ = -900000.0F;
		int i1;
		if(this.chunks != null) {
			for(i1 = 0; i1 < this.chunks.length; ++i1) {
				this.chunks[i1].reset2();
			}
		}

		this.xChunks = (this.level.width + 16 - 1) / 16;
		this.yChunks = (this.level.depth + 16 - 1) / 16;
		this.zChunks = (this.level.height + 16 - 1) / 16;
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

		int i4;
		int i5;
		int i6;
		int i7;
		for(i1 = 0; i1 < this.xChunks; ++i1) {
			for(int i2 = 0; i2 < this.yChunks; ++i2) {
				for(int i3 = 0; i3 < this.zChunks; ++i3) {
					i4 = i1 << 4;
					i5 = i2 << 4;
					i6 = i3 << 4;
					i7 = i1 + 1 << 4;
					int i8 = i2 + 1 << 4;
					int i9 = i3 + 1 << 4;
					if(i7 > this.level.width) {
						i7 = this.level.width;
					}

					if(i8 > this.level.depth) {
						i8 = this.level.depth;
					}

					if(i9 > this.level.height) {
						i9 = this.level.height;
					}

					this.chunks[(i1 + i2 * this.xChunks) * this.zChunks + i3] = new Chunk(this.level, i4, i5, i6, i7, i8, i9);
					this.sortedChunks[(i1 + i2 * this.xChunks) * this.zChunks + i3] = this.chunks[(i1 + i2 * this.xChunks) * this.zChunks + i3];
				}
			}
		}

		GL11.glNewList(this.surroundLists, GL11.GL_COMPILE);
		LevelRenderer levelRenderer10 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rock.png", GL11.GL_NEAREST));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tesselator tesselator11 = Tesselator.instance;
		float f13 = 32.0F - 2.0F;
		i4 = 128;
		if(128 > this.level.width) {
			i4 = this.level.width;
		}

		if(i4 > this.level.height) {
			i4 = this.level.height;
		}

		i5 = 2048 / i4;
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		float f15;
		for(i6 = -i4 * i5; i6 < levelRenderer10.level.width + i4 * i5; i6 += i4) {
			for(i7 = -i4 * i5; i7 < levelRenderer10.level.height + i4 * i5; i7 += i4) {
				f15 = f13;
				if(i6 >= 0 && i7 >= 0 && i6 < levelRenderer10.level.width && i7 < levelRenderer10.level.height) {
					f15 = 0.0F;
				}

				tesselator11.vertexUV((float)i6, f15, (float)(i7 + i4), 0.0F, (float)i4);
				tesselator11.vertexUV((float)(i6 + i4), f15, (float)(i7 + i4), (float)i4, (float)i4);
				tesselator11.vertexUV((float)(i6 + i4), f15, (float)i7, (float)i4, 0.0F);
				tesselator11.vertexUV((float)i6, f15, (float)i7, 0.0F, 0.0F);
			}
		}

		tesselator11.end();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer10.textures.loadTexture("/rock.png", GL11.GL_NEAREST));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		for(i6 = 0; i6 < levelRenderer10.level.width; i6 += i4) {
			tesselator11.vertexUV((float)i6, 0.0F, 0.0F, 0.0F, 0.0F);
			tesselator11.vertexUV((float)(i6 + i4), 0.0F, 0.0F, (float)i4, 0.0F);
			tesselator11.vertexUV((float)(i6 + i4), f13, 0.0F, (float)i4, f13);
			tesselator11.vertexUV((float)i6, f13, 0.0F, 0.0F, f13);
			tesselator11.vertexUV((float)i6, f13, (float)levelRenderer10.level.height, 0.0F, f13);
			tesselator11.vertexUV((float)(i6 + i4), f13, (float)levelRenderer10.level.height, (float)i4, f13);
			tesselator11.vertexUV((float)(i6 + i4), 0.0F, (float)levelRenderer10.level.height, (float)i4, 0.0F);
			tesselator11.vertexUV((float)i6, 0.0F, (float)levelRenderer10.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(i6 = 0; i6 < levelRenderer10.level.height; i6 += i4) {
			tesselator11.vertexUV(0.0F, f13, (float)i6, 0.0F, 0.0F);
			tesselator11.vertexUV(0.0F, f13, (float)(i6 + i4), (float)i4, 0.0F);
			tesselator11.vertexUV(0.0F, 0.0F, (float)(i6 + i4), (float)i4, f13);
			tesselator11.vertexUV(0.0F, 0.0F, (float)i6, 0.0F, f13);
			tesselator11.vertexUV((float)levelRenderer10.level.width, 0.0F, (float)i6, 0.0F, f13);
			tesselator11.vertexUV((float)levelRenderer10.level.width, 0.0F, (float)(i6 + i4), (float)i4, f13);
			tesselator11.vertexUV((float)levelRenderer10.level.width, f13, (float)(i6 + i4), (float)i4, 0.0F);
			tesselator11.vertexUV((float)levelRenderer10.level.width, f13, (float)i6, 0.0F, 0.0F);
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, GL11.GL_COMPILE);
		levelRenderer10 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/water.png", GL11.GL_NEAREST));
		float f12 = 32.0F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tesselator tesselator14 = Tesselator.instance;
		i4 = 128;
		if(128 > this.level.width) {
			i4 = this.level.width;
		}

		if(i4 > this.level.height) {
			i4 = this.level.height;
		}

		i5 = 2048 / i4;
		tesselator14.begin(DefaultVertexFormats.POSITION_TEX);

		for(i6 = -i4 * i5; i6 < levelRenderer10.level.width + i4 * i5; i6 += i4) {
			for(i7 = -i4 * i5; i7 < levelRenderer10.level.height + i4 * i5; i7 += i4) {
				f15 = f12 - 0.1F;
				if(i6 < 0 || i7 < 0 || i6 >= levelRenderer10.level.width || i7 >= levelRenderer10.level.height) {
					tesselator14.vertexUV((float)i6, f15, (float)(i7 + i4), 0.0F, (float)i4);
					tesselator14.vertexUV((float)(i6 + i4), f15, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator14.vertexUV((float)(i6 + i4), f15, (float)i7, (float)i4, 0.0F);
					tesselator14.vertexUV((float)i6, f15, (float)i7, 0.0F, 0.0F);
					tesselator14.vertexUV((float)i6, f15, (float)i7, 0.0F, 0.0F);
					tesselator14.vertexUV((float)(i6 + i4), f15, (float)i7, (float)i4, 0.0F);
					tesselator14.vertexUV((float)(i6 + i4), f15, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator14.vertexUV((float)i6, f15, (float)(i7 + i4), 0.0F, (float)i4);
				}
			}
		}

		tesselator14.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();

		for(i1 = 0; i1 < this.chunks.length; ++i1) {
			this.chunks[i1].reset();
		}

	}

	public final void render(Player player1, int i2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png", GL11.GL_NEAREST));
		float f3 = player1.x - this.lX;
		float f4 = player1.y - this.lY;
		float f5 = player1.z - this.lZ;
		if(f3 * f3 + f4 * f4 + f5 * f5 > 64.0F) {
			this.lX = player1.x;
			this.lY = player1.y;
			this.lZ = player1.z;
			Arrays.sort(this.sortedChunks, new DistanceSorter(player1));
		}

		this.dummyBuffer.clear();

		for(int i6 = 0; i6 < this.sortedChunks.length; ++i6) {
			if(this.sortedChunks[i6].visible && !this.sortedChunks[i6].canRender) {
				f4 = (float)(256 / (1 << this.drawDistance));
				if(this.drawDistance == 0 || this.sortedChunks[i6].compare(player1) < f4 * f4) {
					int i7 = this.sortedChunks[i6].render(i2);
					this.dummyBuffer.put(i7);
					if(this.dummyBuffer.remaining() == 0) {
						this.dummyBuffer.flip();
						GL11.glCallLists(this.dummyBuffer);
						this.dummyBuffer.clear();
					}
				}
			}
		}

		if(this.dummyBuffer.position() > 0) {
			this.dummyBuffer.flip();
			GL11.glCallLists(this.dummyBuffer);
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public final void renderHit(Player player1, HitResult hitResult2, int i3, int i4) {
		Tesselator tesselator5 = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)EagRuntime.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		if(i3 == 0) {
			tesselator5.begin(DefaultVertexFormats.POSITION);

			for(i3 = 0; i3 < 6; ++i3) {
				Tile.renderFaceNoTexture(player1, tesselator5, hitResult2.x, hitResult2.y, hitResult2.z, i3);
			}

			tesselator5.end();
		} else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float f8;
			GL11.glColor4f(f8 = (float)Math.sin((double)EagRuntime.currentTimeMillis() / 100.0D) * 0.2F + 0.8F, f8, f8, (float)Math.sin((double)System.currentTimeMillis() / 200.0D) * 0.2F + 0.5F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			int i7 = this.textures.loadTexture("/terrain.png", 9728);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i7);
			i7 = hitResult2.x;
			i3 = hitResult2.y;
			int i6 = hitResult2.z;
			if(hitResult2.f == 0) {
				--i3;
			}

			if(hitResult2.f == 1) {
				++i3;
			}

			if(hitResult2.f == 2) {
				--i6;
			}

			if(hitResult2.f == 3) {
				++i6;
			}

			if(hitResult2.f == 4) {
				--i7;
			}

			if(hitResult2.f == 5) {
				++i7;
			}

			tesselator5.begin(DefaultVertexFormats.POSITION_TEX);
			tesselator5.noColor();
			Tile.tiles[i4].render(tesselator5, this.level, 0, i7, i3, i6);
			Tile.tiles[i4].render(tesselator5, this.level, 1, i7, i3, i6);
			tesselator5.end();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public static void renderHitOutline(HitResult hitResult0, int i1) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		float f2 = (float)hitResult0.x;
		float f3 = (float)hitResult0.y;
		float f4 = (float)hitResult0.z;
		if(i1 == 1) {
			if(hitResult0.f == 0) {
				--f3;
			}

			if(hitResult0.f == 1) {
				++f3;
			}

			if(hitResult0.f == 2) {
				--f4;
			}

			if(hitResult0.f == 3) {
				++f4;
			}

			if(hitResult0.f == 4) {
				--f2;
			}

			if(hitResult0.f == 5) {
				++f2;
			}
		}

		GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		GL11.glVertex3f(f2, f3, f4);
		GL11.glVertex3f(f2 + 1.0F, f3, f4);
		GL11.glVertex3f(f2 + 1.0F, f3, f4 + 1.0F);
		GL11.glVertex3f(f2, f3, f4 + 1.0F);
		GL11.glVertex3f(f2, f3, f4);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		GL11.glVertex3f(f2, f3 + 1.0F, f4);
		GL11.glVertex3f(f2 + 1.0F, f3 + 1.0F, f4);
		GL11.glVertex3f(f2 + 1.0F, f3 + 1.0F, f4 + 1.0F);
		GL11.glVertex3f(f2, f3 + 1.0F, f4 + 1.0F);
		GL11.glVertex3f(f2, f3 + 1.0F, f4);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glVertex3f(f2, f3, f4);
		GL11.glVertex3f(f2, f3 + 1.0F, f4);
		GL11.glVertex3f(f2 + 1.0F, f3, f4);
		GL11.glVertex3f(f2 + 1.0F, f3 + 1.0F, f4);
		GL11.glVertex3f(f2 + 1.0F, f3, f4 + 1.0F);
		GL11.glVertex3f(f2 + 1.0F, f3 + 1.0F, f4 + 1.0F);
		GL11.glVertex3f(f2, f3, f4 + 1.0F);
		GL11.glVertex3f(f2, f3 + 1.0F, f4 + 1.0F);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public final void setDirty(int i1, int i2, int i3, int i4, int i5, int i6) {
		i1 /= 16;
		i4 /= 16;
		i2 /= 16;
		i5 /= 16;
		i3 /= 16;
		i6 /= 16;
		if(i1 < 0) {
			i1 = 0;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i4 >= this.xChunks) {
			i4 = this.xChunks - 1;
		}

		if(i5 >= this.yChunks) {
			i5 = this.yChunks - 1;
		}

		if(i6 >= this.zChunks) {
			i6 = this.zChunks - 1;
		}

		for(i1 = i1; i1 <= i4; ++i1) {
			for(int i7 = i2; i7 <= i5; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					this.chunks[(i1 + i7 * this.xChunks) * this.zChunks + i8].setDirty();
				}
			}
		}

	}
}
