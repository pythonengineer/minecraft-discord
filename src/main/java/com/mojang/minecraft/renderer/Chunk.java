package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
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
	private boolean[] skipRenderPass = new boolean[2];
	public boolean isInFrustum = false;

	public Chunk(Level level1, int i2, int i3, int i4, int i5) {
		this.level = level1;
		this.x0 = i2;
		this.y0 = i3;
		this.z0 = i4;
		this.x1 = this.y1 = this.z1 = 16;
		Math.sqrt((double)(this.x1 * this.x1 + this.y1 * this.y1 + this.z1 * this.z1));
		this.lists = GL11.glGenLists(2);
		this.reset();
	}

	public final float a(Player player1) {
		float f2 = player1.x - (float)this.x0;
		float f3 = player1.y - (float)this.y0;
		float f4 = player1.z - (float)this.z0;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void reset() {
		for(int i1 = 0; i1 < 2; ++i1) {
			GL11.glNewList(this.lists + i1, GL11.GL_COMPILE);
			GL11.glEndList();
		}

	}

	public final void clear() {
		GL11.glDeleteLists(this.lists, 2);
		this.level = null;
	}

	public final void rebuild() {
		++updates;

		for(int i1 = 0; i1 < 2; ++i1) {
			int i3 = i1;
			Chunk chunk2 = this;
			int i4 = this.x0;
			int i5 = this.y0;
			int i6 = this.z0;
			int i7 = this.x0 + this.x1;
			int i8 = this.y0 + this.y1;
			int i9 = this.z0 + this.z1;
			GL11.glNewList(this.lists + i1, GL11.GL_COMPILE);
			t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
			boolean z10 = false;

			for(i4 = i4; i4 < i7; ++i4) {
				for(int i11 = i5; i11 < i8; ++i11) {
					for(int i12 = i6; i12 < i9; ++i12) {
						int i13;
						if((i13 = chunk2.level.getTile(i4, i11, i12)) > 0) {
							z10 |= Tile.tiles[i13].render(t, chunk2.level, i3, i4, i11, i12);
						}
					}
				}
			}

			t.end();
			GL11.glEndList();
			if(chunk2.skipRenderPass[i3] != !z10) {
				chunk2.skipRenderPass[i3] = !z10;
			}
		}

	}

	public final void render(IntBuffer intBuffer1, int i2) {
		if(this.isInFrustum && !this.skipRenderPass[i2]) {
			intBuffer1.put(this.lists + i2);
		}
	}

	public final void isInFrustum(Frustum frustum1) {
		this.isInFrustum = frustum1.cubeInFrustum((float)this.x0, (float)this.y0, (float)this.z0, (float)(this.x0 + this.x1), (float)(this.y0 + this.y1), (float)(this.z0 + this.z1));
	}
}
