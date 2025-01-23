package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LevelRenderer {
	public Level level;
	private Textures textures;
	public int surroundLists;
	public int drawDistance = 0;
	private IntBuffer dummyBuffer = BufferUtils.createIntBuffer(65536);
	public List<Chunk> dirtyChunks = new ArrayList<Chunk>();
	private Chunk[] chunks;
	public Chunk[] sortedChunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	public int cloudTickCounter = 0;
	private float lX = -9999.0F;
	private float lY = -9999.0F;
	private float lZ = -9999.0F;

	public LevelRenderer(Textures textures1) {
		this.textures = textures1;
		this.surroundLists = GL11.glGenLists(2);
	}

	public final void setLevel(Level level1) {
		if(this.level != null) {
			this.level.removeListener(this);
		}

		this.level = level1;
		level1.addListener(this);
		this.compileSurroundingGround();
	}

	public final void compileSurroundingGround() {
		int i1;
		if(this.sortedChunks != null) {
			for(i1 = 0; i1 < this.sortedChunks.length; ++i1) {
				this.sortedChunks[i1].clear();
			}
		}

		this.xChunks = this.level.width / 16;
		this.yChunks = this.level.depth / 16;
		this.zChunks = this.level.height / 16;
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

		for(i1 = 0; i1 < this.xChunks; ++i1) {
			for(int i2 = 0; i2 < this.yChunks; ++i2) {
				for(int i3 = 0; i3 < this.zChunks; ++i3) {
					this.sortedChunks[(i3 * this.yChunks + i2) * this.xChunks + i1] = new Chunk(this.level, i1 << 4, i2 << 4, i3 << 4, 16);
					this.chunks[(i3 * this.yChunks + i2) * this.xChunks + i1] = this.sortedChunks[(i3 * this.yChunks + i2) * this.xChunks + i1];
				}
			}
		}

		this.dirtyChunks.clear();
		GL11.glNewList(this.surroundLists, GL11.GL_COMPILE);
		LevelRenderer levelRenderer9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rock.png", GL11.GL_NEAREST));
		float f10 = 0.5F;
		GL11.glColor4f(0.5F, f10, f10, 1.0F);
		Tesselator tesselator11 = Tesselator.instance;
		float f4 = this.level.getGroundLevel();
		int i5 = 128;
		if(128 > this.level.width) {
			i5 = this.level.width;
		}

		if(i5 > this.level.height) {
			i5 = this.level.height;
		}

		int i6 = 2048 / i5;
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		int i7;
		for(i7 = -i5 * i6; i7 < levelRenderer9.level.width + i5 * i6; i7 += i5) {
			for(int i8 = -i5 * i6; i8 < levelRenderer9.level.height + i5 * i6; i8 += i5) {
				f10 = f4;
				if(i7 >= 0 && i8 >= 0 && i7 < levelRenderer9.level.width && i8 < levelRenderer9.level.height) {
					f10 = 0.0F;
				}

				tesselator11.vertexUV((float)i7, f10, (float)(i8 + i5), 0.0F, (float)i5);
				tesselator11.vertexUV((float)(i7 + i5), f10, (float)(i8 + i5), (float)i5, (float)i5);
				tesselator11.vertexUV((float)(i7 + i5), f10, (float)i8, (float)i5, 0.0F);
				tesselator11.vertexUV((float)i7, f10, (float)i8, 0.0F, 0.0F);
			}
		}

		tesselator11.end();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer9.textures.loadTexture("/rock.png", GL11.GL_NEAREST));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		for(i7 = 0; i7 < levelRenderer9.level.width; i7 += i5) {
			tesselator11.vertexUV((float)i7, 0.0F, 0.0F, 0.0F, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, 0.0F, (float)i5, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), f4, 0.0F, (float)i5, f4);
			tesselator11.vertexUV((float)i7, f4, 0.0F, 0.0F, f4);
			tesselator11.vertexUV((float)i7, f4, (float)levelRenderer9.level.height, 0.0F, f4);
			tesselator11.vertexUV((float)(i7 + i5), f4, (float)levelRenderer9.level.height, (float)i5, f4);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, (float)levelRenderer9.level.height, (float)i5, 0.0F);
			tesselator11.vertexUV((float)i7, 0.0F, (float)levelRenderer9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(i7 = 0; i7 < levelRenderer9.level.height; i7 += i5) {
			tesselator11.vertexUV(0.0F, f4, (float)i7, 0.0F, 0.0F);
			tesselator11.vertexUV(0.0F, f4, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV(0.0F, 0.0F, (float)(i7 + i5), (float)i5, f4);
			tesselator11.vertexUV(0.0F, 0.0F, (float)i7, 0.0F, f4);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)i7, 0.0F, f4);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)(i7 + i5), (float)i5, f4);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f4, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f4, (float)i7, 0.0F, 0.0F);
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, GL11.GL_COMPILE);
		levelRenderer9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/water.png", GL11.GL_NEAREST));
		f10 = this.level.getWaterLevel();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tesselator11 = Tesselator.instance;
		int i12 = 128;
		if(128 > this.level.width) {
			i12 = this.level.width;
		}

		if(i12 > this.level.height) {
			i12 = this.level.height;
		}

		i5 = 2048 / i12;
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		for(i6 = -i12 * i5; i6 < levelRenderer9.level.width + i12 * i5; i6 += i12) {
			for(i7 = -i12 * i5; i7 < levelRenderer9.level.height + i12 * i5; i7 += i12) {
				float f13 = f10 - 0.1F;
				if(i6 < 0 || i7 < 0 || i6 >= levelRenderer9.level.width || i7 >= levelRenderer9.level.height) {
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i12), 0.0F, (float)i12);
					tesselator11.vertexUV((float)(i6 + i12), f13, (float)(i7 + i12), (float)i12, (float)i12);
					tesselator11.vertexUV((float)(i6 + i12), f13, (float)i7, (float)i12, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)(i6 + i12), f13, (float)i7, (float)i12, 0.0F);
					tesselator11.vertexUV((float)(i6 + i12), f13, (float)(i7 + i12), (float)i12, (float)i12);
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i12), 0.0F, (float)i12);
				}
			}
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}

	public final void renderEntities(Frustum frustum1, float f2) {
		for(int i3 = 0; i3 < this.level.entities.size(); ++i3) {
			Entity entity4 = (Entity)this.level.entities.get(i3);
			if(frustum1.isVisible(entity4.bb)) {
				((Entity)this.level.entities.get(i3)).render(this.textures, f2);
			}
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
			Arrays.sort(this.chunks, new DistanceSorter(player1));
		}

		this.dummyBuffer.clear();

		for(int i6 = 0; i6 < this.chunks.length; ++i6) {
			this.chunks[i6].render(this.dummyBuffer, i2);
		}

		if(this.dummyBuffer.position() > 0) {
			this.dummyBuffer.flip();
			GL11.glCallLists(this.dummyBuffer);
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public final void renderClouds(float f1) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/clouds.png", GL11.GL_NEAREST));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tesselator tesselator2 = Tesselator.instance;
		float f3 = 0.0F;
		float f4 = 4.8828125E-4F;
		f3 = (float)(this.level.depth + 2);
		f1 = ((float)this.cloudTickCounter + f1) * f4 * 0.03F;
		float f5 = 0.0F;
		tesselator2.begin(DefaultVertexFormats.POSITION_TEX);

		int i8;
		for(i8 = -2048; i8 < this.level.width + 2048; i8 += 512) {
			for(int i6 = -2048; i6 < this.level.height + 2048; i6 += 512) {
				tesselator2.vertexUV((float)i8, f3, (float)(i6 + 512), (float)i8 * f4 + f1, (float)(i6 + 512) * f4);
				tesselator2.vertexUV((float)(i8 + 512), f3, (float)(i6 + 512), (float)(i8 + 512) * f4 + f1, (float)(i6 + 512) * f4);
				tesselator2.vertexUV((float)(i8 + 512), f3, (float)i6, (float)(i8 + 512) * f4 + f1, (float)i6 * f4);
				tesselator2.vertexUV((float)i8, f3, (float)i6, (float)i8 * f4 + f1, (float)i6 * f4);
				tesselator2.vertexUV((float)i8, f3, (float)i6, (float)i8 * f4 + f1, (float)i6 * f4);
				tesselator2.vertexUV((float)(i8 + 512), f3, (float)i6, (float)(i8 + 512) * f4 + f1, (float)i6 * f4);
				tesselator2.vertexUV((float)(i8 + 512), f3, (float)(i6 + 512), (float)(i8 + 512) * f4 + f1, (float)(i6 + 512) * f4);
				tesselator2.vertexUV((float)i8, f3, (float)(i6 + 512), (float)i8 * f4 + f1, (float)(i6 + 512) * f4);
			}
		}

		tesselator2.end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tesselator2.begin(DefaultVertexFormats.POSITION_COLOR);
		tesselator2.color(0.5F, 0.8F, 1.0F);
		f3 = (float)(this.level.depth + 10);

		for(int i7 = -2048; i7 < this.level.width + 2048; i7 += 512) {
			for(i8 = -2048; i8 < this.level.height + 2048; i8 += 512) {
				tesselator2.vertex((float)i7, f3, (float)i8);
				tesselator2.vertex((float)(i7 + 512), f3, (float)i8);
				tesselator2.vertex((float)(i7 + 512), f3, (float)(i8 + 512));
				tesselator2.vertex((float)i7, f3, (float)(i8 + 512));
			}
		}

		tesselator2.end();
	}

	public final void render(int i1, int i2, int i3) {
		int i6;
		if((i6 = this.level.getTile(i1, i2, i3)) != 0 && Tile.tiles[i6].isSolid()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
			GL11.glDepthFunc(GL11.GL_LESS);
			Tesselator tesselator4 = Tesselator.instance;
			Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);

			int i5;
			for(i5 = 0; i5 < 6; ++i5) {
				Tile.tiles[i6].renderFace(tesselator4, i1, i2, i3, i5);
			}

			tesselator4.end();
			GL11.glCullFace(GL11.GL_FRONT);
			tesselator4.begin(DefaultVertexFormats.POSITION_TEX);

			for(i5 = 0; i5 < 6; ++i5) {
				Tile.tiles[i6].renderFace(tesselator4, i1, i2, i3, i5);
			}

			tesselator4.end();
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

	public final void renderHit(Player player1, HitResult hitResult2, int i3, int i4) {
		Tesselator tesselator5 = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		if(i3 == 0) {
			tesselator5.begin(DefaultVertexFormats.POSITION);

			for(i3 = 0; i3 < 6; ++i3) {
				Tile.renderFaceNoTexture(player1, tesselator5, hitResult2.x, hitResult2.y, hitResult2.z, i3);
			}

			tesselator5.end();
		} else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float f8;
			GL11.glColor4f(f8 = (float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.8F, f8, f8, (float)Math.sin((double)System.currentTimeMillis() / 200.0D) * 0.2F + 0.5F);
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
		i2 /= 16;
		i3 /= 16;
		i4 /= 16;
		i5 /= 16;
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

		if(i4 > this.xChunks - 1) {
			i4 = this.xChunks - 1;
		}

		if(i5 > this.yChunks - 1) {
			i5 = this.yChunks - 1;
		}

		if(i6 > this.zChunks - 1) {
			i6 = this.zChunks - 1;
		}

		for(i1 = i1; i1 <= i4; ++i1) {
			for(int i7 = i2; i7 <= i5; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					this.dirtyChunks.add(this.sortedChunks[(i8 * this.yChunks + i7) * this.xChunks + i1]);
				}
			}
		}

	}
}
