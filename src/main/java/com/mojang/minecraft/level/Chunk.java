package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class Chunk {
	public AABB aabb;
	private Level level;
	private int x0;
	private int y0;
	private int z0;
	private int x1;
	private int y1;
	private int z1;
	private float x;
	private float y;
	private float z;
	private boolean dirty = true;
	private int lists = -1;
	public boolean visible;
	public boolean canRender;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private static int totalUpdates = 0;

	public Chunk(Level level1, int i2, int i3, int i4, int i5, int i6, int i7) {
		this.level = level1;
		this.x0 = i2;
		this.y0 = i3;
		this.z0 = i4;
		this.x1 = i5;
		this.y1 = i6;
		this.z1 = i7;
		this.x = (float)(i2 + i5) / 2.0F;
		this.y = (float)(i3 + i6) / 2.0F;
		this.z = (float)(i4 + i7) / 2.0F;
		this.aabb = new AABB((float)i2, (float)i3, (float)i4, (float)i5, (float)i6, (float)i7);
		this.lists = GL11.glGenLists(3);
	}

	private void rebuild(int i1) {
		GL11.glNewList(this.lists + i1, GL11.GL_COMPILE);
        t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
		int i2 = 0;
		boolean z3 = false;

		for(int i4 = this.x0; i4 < this.x1; ++i4) {
			for(int i5 = this.y0; i5 < this.y1; ++i5) {
				for(int i6 = this.z0; i6 < this.z1; ++i6) {
					int i7;
					if((i7 = this.level.getTile(i4, i5, i6)) > 0) {
						z3 |= Tile.tiles[i7].render(t, this.level, i1, i4, i5, i6);
						++i2;
					}
				}
			}
		}

		if(z3) {
			this.canRender = false;
		}

		t.end();
		GL11.glEndList();
		if(i2 > 0) {
			++totalUpdates;
		}

	}

	public final void rebuild() {
		this.canRender = true;
		++updates;
		this.rebuild(0);
		this.rebuild(1);
		this.rebuild(2);
		this.dirty = false;
	}

	public final int render(int i1) {
		return this.lists + i1;
	}

	public final void setDirty() {
		if(!this.dirty) {
			EagRuntime.currentTimeMillis();
		}

		this.dirty = true;
	}

	public final boolean isDirty() {
		return this.dirty;
	}

	public final float compare(Player player1) {
		float f2 = player1.x - this.x;
		float f3 = player1.y - this.y;
		float f4 = player1.z - this.z;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	public final void reset() {
		this.dirty = true;

		for(int i1 = 0; i1 < 3; ++i1) {
			GL11.glNewList(this.lists + i1, GL11.GL_COMPILE);
			GL11.glEndList();
		}

	}

	public final void reset2() {
		GL11.glDeleteLists(this.lists, 3);
	}
}
