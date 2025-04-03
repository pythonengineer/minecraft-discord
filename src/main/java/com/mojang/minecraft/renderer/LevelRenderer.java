package com.mojang.minecraft.renderer;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LevelRenderer {
	public Level level;
	public Textures textures;
	public int surroundLists;
	public IntBuffer ib = BufferUtils.createIntBuffer(65536);
	public List<Chunk> allDirtyChunks = new ArrayList<Chunk>();
	private Chunk[] sortedChunks;
	public Chunk[] chunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	private int glLists;
	private Minecraft minecraft;
	private int[] chunkBuffer = new int[50000];
	public int cloudTickCounter = 0;
	private float lX = -9999.0F;
	private float lY = -9999.0F;
	private float lZ = -9999.0F;
	public float hurtTime;

	public LevelRenderer(Minecraft minecraft, Textures textures) {
		this.minecraft = minecraft;
		this.textures = textures;
		this.surroundLists = GL11.glGenLists(2);
		this.glLists = GL11.glGenLists(4096 << 6 << 3);
	}

	public final void compileSurroundingGround() {
		int i1;
		if(this.chunks != null) {
			for(i1 = 0; i1 < this.chunks.length; ++i1) {
				this.chunks[i1].clear();
			}
		}

		this.xChunks = this.level.width / 16;
		this.yChunks = this.level.depth / 16;
		this.zChunks = this.level.height / 16;
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		i1 = 0;

		int i4;
		for(int i2 = 0; i2 < this.xChunks; ++i2) {
			for(int i3 = 0; i3 < this.yChunks; ++i3) {
				for(i4 = 0; i4 < this.zChunks; ++i4) {
					this.chunks[(i4 * this.yChunks + i3) * this.xChunks + i2] = new Chunk(this.level, i2 << 4, i3 << 4, i4 << 4, 16, this.glLists + i1);
					this.sortedChunks[(i4 * this.yChunks + i3) * this.xChunks + i2] = this.chunks[(i4 * this.yChunks + i3) * this.xChunks + i2];
					i1 += 8;
				}
			}
		}

		this.allDirtyChunks.clear();
		GL11.glNewList(this.surroundLists, GL11.GL_COMPILE);
		LevelRenderer levelRenderer9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rock.png"));
		float f10 = 0.5F;
		GL11.glColor4f(0.5F, f10, f10, 1.0F);
		Tesselator tesselator11 = Tesselator.instance;
		float f12 = this.level.getGroundLevel();
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
				f10 = f12;
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer9.textures.loadTexture("/rock.png"));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		for(i7 = 0; i7 < levelRenderer9.level.width; i7 += i5) {
			tesselator11.vertexUV((float)i7, 0.0F, 0.0F, 0.0F, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, 0.0F, (float)i5, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), f12, 0.0F, (float)i5, f12);
			tesselator11.vertexUV((float)i7, f12, 0.0F, 0.0F, f12);
			tesselator11.vertexUV((float)i7, f12, (float)levelRenderer9.level.height, 0.0F, f12);
			tesselator11.vertexUV((float)(i7 + i5), f12, (float)levelRenderer9.level.height, (float)i5, f12);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, (float)levelRenderer9.level.height, (float)i5, 0.0F);
			tesselator11.vertexUV((float)i7, 0.0F, (float)levelRenderer9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(i7 = 0; i7 < levelRenderer9.level.height; i7 += i5) {
			tesselator11.vertexUV(0.0F, f12, (float)i7, 0.0F, 0.0F);
			tesselator11.vertexUV(0.0F, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV(0.0F, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tesselator11.vertexUV(0.0F, 0.0F, (float)i7, 0.0F, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)i7, 0.0F, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f12, (float)i7, 0.0F, 0.0F);
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, GL11.GL_COMPILE);
		levelRenderer9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/water.png"));
		f10 = this.level.getWaterLevel();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tesselator11 = Tesselator.instance;
		i4 = 128;
		if(128 > this.level.width) {
			i4 = this.level.width;
		}

		if(i4 > this.level.height) {
			i4 = this.level.height;
		}

		i5 = 2048 / i4;
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX);

		for(i6 = -i4 * i5; i6 < levelRenderer9.level.width + i4 * i5; i6 += i4) {
			for(i7 = -i4 * i5; i7 < levelRenderer9.level.height + i4 * i5; i7 += i4) {
				float f13 = f10 - 0.1F;
				if(i6 < 0 || i7 < 0 || i6 >= levelRenderer9.level.width || i7 >= levelRenderer9.level.height) {
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
				}
			}
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEndList();
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}

	public final int render(Player player, int layer) {
		float f3 = player.x - this.lX;
		float f4 = player.y - this.lY;
		float f5 = player.z - this.lZ;
		if(f3 * f3 + f4 * f4 + f5 * f5 > 64.0F) {
			this.lX = player.x;
			this.lY = player.y;
			this.lZ = player.z;
			Arrays.sort(this.sortedChunks, new DistanceSorter(player));
		}

		int i6 = 0;

		for(int i7 = 0; i7 < this.sortedChunks.length; ++i7) {
			i6 = this.sortedChunks[i7].render(this.chunkBuffer, i6, layer, player.x, player.y, player.z);
		}

		this.ib.clear();
		this.ib.put(this.chunkBuffer, 0, i6);
		this.ib.flip();
		if(this.ib.remaining() > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
			GL11.glCallLists(this.ib);
		}

		return this.ib.remaining();
	}

	public final void renderClouds(float partialTicks) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/clouds.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f2 = (float)(this.level.cloudColor >> 16 & 255) / 255.0F;
		float f3 = (float)(this.level.cloudColor >> 8 & 255) / 255.0F;
		float f4 = (float)(this.level.cloudColor & 255) / 255.0F;
		if(this.minecraft.options.anaglyph3d) {
			float f5 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
			f3 = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
			f4 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
			f2 = f5;
			f3 = f3;
			f4 = f4;
		}

		Tesselator tesselator11 = Tesselator.instance;
		float f6 = 0.0F;
		float f7 = 4.8828125E-4F;
		f6 = (float)(this.level.depth + 2);
		partialTicks = ((float)this.cloudTickCounter + partialTicks) * f7 * 0.03F;
		float f8 = 0.0F;
		tesselator11.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
		tesselator11.color(f2, f3, f4);

		int i10;
		for(int i9 = -2048; i9 < this.level.width + 2048; i9 += 512) {
			for(i10 = -2048; i10 < this.level.height + 2048; i10 += 512) {
				tesselator11.vertexUV((float)i9, f6, (float)(i10 + 512), (float)i9 * f7 + partialTicks, (float)(i10 + 512) * f7);
				tesselator11.vertexUV((float)(i9 + 512), f6, (float)(i10 + 512), (float)(i9 + 512) * f7 + partialTicks, (float)(i10 + 512) * f7);
				tesselator11.vertexUV((float)(i9 + 512), f6, (float)i10, (float)(i9 + 512) * f7 + partialTicks, (float)i10 * f7);
				tesselator11.vertexUV((float)i9, f6, (float)i10, (float)i9 * f7 + partialTicks, (float)i10 * f7);
				tesselator11.vertexUV((float)i9, f6, (float)i10, (float)i9 * f7 + partialTicks, (float)i10 * f7);
				tesselator11.vertexUV((float)(i9 + 512), f6, (float)i10, (float)(i9 + 512) * f7 + partialTicks, (float)i10 * f7);
				tesselator11.vertexUV((float)(i9 + 512), f6, (float)(i10 + 512), (float)(i9 + 512) * f7 + partialTicks, (float)(i10 + 512) * f7);
				tesselator11.vertexUV((float)i9, f6, (float)(i10 + 512), (float)i9 * f7 + partialTicks, (float)(i10 + 512) * f7);
			}
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tesselator11.begin(DefaultVertexFormats.POSITION_COLOR);
		partialTicks = (float)(this.level.skyColor >> 16 & 255) / 255.0F;
		f8 = (float)(this.level.skyColor >> 8 & 255) / 255.0F;
		f2 = (float)(this.level.skyColor & 255) / 255.0F;
		if(this.minecraft.options.anaglyph3d) {
			f3 = (partialTicks * 30.0F + f8 * 59.0F + f2 * 11.0F) / 100.0F;
			f4 = (partialTicks * 30.0F + f8 * 70.0F) / 100.0F;
			f2 = (partialTicks * 30.0F + f2 * 70.0F) / 100.0F;
			partialTicks = f3;
			f8 = f4;
			f2 = f2;
		}

		tesselator11.color(partialTicks, f8, f2);
		f6 = (float)(this.level.depth + 10);

		for(i10 = -2048; i10 < this.level.width + 2048; i10 += 512) {
			for(int i12 = -2048; i12 < this.level.height + 2048; i12 += 512) {
				tesselator11.vertex((float)i10, f6, (float)i12);
				tesselator11.vertex((float)(i10 + 512), f6, (float)i12);
				tesselator11.vertex((float)(i10 + 512), f6, (float)(i12 + 512));
				tesselator11.vertex((float)i10, f6, (float)(i12 + 512));
			}
		}

		tesselator11.end();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public final void render(int x, int y, int z) {
		int i6;
		if((i6 = this.level.getTile(x, y, z)) != 0 && Tile.tiles[i6].isSolid()) {
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
			GL11.glDepthFunc(GL11.GL_LESS);
			Tesselator tesselator4 = Tesselator.instance;
			Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);

			int i5;
			for(i5 = 0; i5 < 6; ++i5) {
				Tile.tiles[i6].renderFace(tesselator4, x, y, z, i5);
			}

			tesselator4.end();
			GL11.glCullFace(GL11.GL_FRONT);
			tesselator4.begin(DefaultVertexFormats.POSITION_TEX);

			for(i5 = 0; i5 < 6; ++i5) {
				Tile.tiles[i6].renderFace(tesselator4, x, y, z, i5);
			}

			tesselator4.end();
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

    public final void renderHit(HitResult h, int mode, int id) {
        Tesselator tesselator8 = Tesselator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)EagRuntime.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
        if(this.hurtTime > 0.0F) {
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
            id = this.textures.loadTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            Tile tile10000 = (id = this.level.getTile(h.x, h.y, h.z)) > 0 ? Tile.tiles[id] : null;
            Tile tile9 = tile10000;
            float f4 = (tile10000.xx0 + tile9.xx1) / 2.0F;
            float f5 = (tile9.yy0 + tile9.yy1) / 2.0F;
            float f6 = (tile9.zz0 + tile9.zz1) / 2.0F;
            GL11.glTranslatef((float)h.x + f4, (float)h.y + f5, (float)h.z + f6);
            float f7 = 1.01F;
            GL11.glScalef(1.01F, f7, f7);
            GL11.glTranslatef(-((float)h.x + f4), -((float)h.y + f5), -((float)h.z + f6));
            tesselator8.begin(DefaultVertexFormats.POSITION_TEX);
            tesselator8.noColor();
            GL11.glDepthMask(false);
            if(tile9 == null) {
                tile9 = Tile.rock;
            }

            for(int i10 = 0; i10 < 6; ++i10) {
                tile9.renderFaceNoTexture(tesselator8, h.x, h.y, h.z, i10, 240 + (int)(this.hurtTime * 10.0F));
            }

            tesselator8.end();
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

	public final void setDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
		x0 /= 16;
		y0 /= 16;
		z0 /= 16;
		x1 /= 16;
		y1 /= 16;
		z1 /= 16;
		if(x0 < 0) {
			x0 = 0;
		}

		if(y0 < 0) {
			y0 = 0;
		}

		if(z0 < 0) {
			z0 = 0;
		}

		if(x1 > this.xChunks - 1) {
			x1 = this.xChunks - 1;
		}

		if(y1 > this.yChunks - 1) {
			y1 = this.yChunks - 1;
		}

		if(z1 > this.zChunks - 1) {
			z1 = this.zChunks - 1;
		}

		for(x0 = x0; x0 <= x1; ++x0) {
			for(int i7 = y0; i7 <= y1; ++i7) {
				for(int i8 = z0; i8 <= z1; ++i8) {
					this.allDirtyChunks.add(this.chunks[(i8 * this.yChunks + i7) * this.xChunks + x0]);
				}
			}
		}

	}
}