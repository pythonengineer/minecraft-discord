package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class Tile {
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	public static final Tile rock = new Tile(1, 1);
	public static final Tile grass = new GrassTile(2);
	public static final Tile dirt = new DirtTile(3, 2);
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public int tex;
	public final int id;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;

	protected Tile(int i1) {
		tiles[i1] = this;
		this.id = i1;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	protected final void setTicking(boolean z1) {
		shouldTick[this.id] = z1;
	}

	protected final void setShape(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.xx0 = 0.0F;
		this.yy0 = f2;
		this.zz0 = 0.0F;
		this.xx1 = 1.0F;
		this.yy1 = f5;
		this.zz1 = 1.0F;
	}

	protected Tile(int i1, int i2) {
		this(i1);
		this.tex = i2;
	}

	public boolean render(Tesselator tesselator1, Level level2, int i3, int i4, int i5, int i6) {
		boolean z7 = false;
		if(this.shouldRenderFace(level2, i4, i5 - 1, i6, i3, 0)) {
			tesselator1.color((byte)-1, (byte)-1, (byte)-1);
			this.renderFace(tesselator1, i4, i5, i6, 0);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5 + 1, i6, i3, 1)) {
			tesselator1.color((byte)-1, (byte)-1, (byte)-1);
			this.renderFace(tesselator1, i4, i5, i6, 1);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5, i6 - 1, i3, 2)) {
			tesselator1.color((byte)-52, (byte)-52, (byte)-52);
			this.renderFace(tesselator1, i4, i5, i6, 2);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5, i6 + 1, i3, 3)) {
			tesselator1.color((byte)-52, (byte)-52, (byte)-52);
			this.renderFace(tesselator1, i4, i5, i6, 3);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4 - 1, i5, i6, i3, 4)) {
			tesselator1.color((byte)-103, (byte)-103, (byte)-103);
			this.renderFace(tesselator1, i4, i5, i6, 4);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4 + 1, i5, i6, i3, 5)) {
			tesselator1.color((byte)-103, (byte)-103, (byte)-103);
			this.renderFace(tesselator1, i4, i5, i6, 5);
			z7 = true;
		}

		return z7;
	}

	protected boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
		boolean z7 = true;
		if(i5 == 2) {
			return false;
		} else {
			if(i5 >= 0) {
				z7 = level1.isLit(i2, i3, i4) ^ i5 == 1;
			}

			Tile tile8;
			return !((tile8 = tiles[level1.getTile(i2, i3, i4)]) == null ? false : tile8.isSolid()) && z7;
		}
	}

	protected int getTexture(int i1) {
		return this.tex;
	}

	public void renderFace(Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		int i6;
		int i7 = (i6 = this.getTexture(i5)) % 16 << 4;
		i6 = i6 / 16 << 4;
		float f8 = (float)i7 / 256.0F;
		float f17 = ((float)i7 + 15.99F) / 256.0F;
		float f9 = (float)i6 / 256.0F;
		float f16 = ((float)i6 + 15.99F) / 256.0F;
		float f10 = (float)i2 + this.xx0;
		float f14 = (float)i2 + this.xx1;
		float f11 = (float)i3 + this.yy0;
		float f15 = (float)i3 + this.yy1;
		float f12 = (float)i4 + this.zz0;
		float f13 = (float)i4 + this.zz1;
		if(i5 == 0) {
			tesselator1.vertexUV(f10, f11, f13, f8, f16);
			tesselator1.vertexUV(f10, f11, f12, f8, f9);
			tesselator1.vertexUV(f14, f11, f12, f17, f9);
			tesselator1.vertexUV(f14, f11, f13, f17, f16);
		} else if(i5 == 1) {
			tesselator1.vertexUV(f14, f15, f13, f17, f16);
			tesselator1.vertexUV(f14, f15, f12, f17, f9);
			tesselator1.vertexUV(f10, f15, f12, f8, f9);
			tesselator1.vertexUV(f10, f15, f13, f8, f16);
		} else if(i5 == 2) {
			tesselator1.vertexUV(f10, f15, f12, f17, f9);
			tesselator1.vertexUV(f14, f15, f12, f8, f9);
			tesselator1.vertexUV(f14, f11, f12, f8, f16);
			tesselator1.vertexUV(f10, f11, f12, f17, f16);
		} else if(i5 == 3) {
			tesselator1.vertexUV(f10, f15, f13, f8, f9);
			tesselator1.vertexUV(f10, f11, f13, f8, f16);
			tesselator1.vertexUV(f14, f11, f13, f17, f16);
			tesselator1.vertexUV(f14, f15, f13, f17, f9);
		} else if(i5 == 4) {
			tesselator1.vertexUV(f10, f15, f13, f17, f9);
			tesselator1.vertexUV(f10, f15, f12, f8, f9);
			tesselator1.vertexUV(f10, f11, f12, f8, f16);
			tesselator1.vertexUV(f10, f11, f13, f17, f16);
		} else if(i5 == 5) {
			tesselator1.vertexUV(f14, f11, f13, f8, f16);
			tesselator1.vertexUV(f14, f11, f12, f17, f16);
			tesselator1.vertexUV(f14, f15, f12, f17, f9);
			tesselator1.vertexUV(f14, f15, f13, f8, f9);
		}
	}

	public final void renderBackFace(Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		int i6;
		float f7;
		float f8 = (f7 = (float)((i6 = this.getTexture(i5)) % 16) / 16.0F) + 0.0624375F;
		float f16;
		float f9 = (f16 = (float)(i6 / 16) / 16.0F) + 0.0624375F;
		float f10 = (float)i2 + this.xx0;
		float f14 = (float)i2 + this.xx1;
		float f11 = (float)i3 + this.yy0;
		float f15 = (float)i3 + this.yy1;
		float f12 = (float)i4 + this.zz0;
		float f13 = (float)i4 + this.zz1;
		if(i5 == 0) {
			tesselator1.vertexUV(f14, f11, f13, f8, f9);
			tesselator1.vertexUV(f14, f11, f12, f8, f16);
			tesselator1.vertexUV(f10, f11, f12, f7, f16);
			tesselator1.vertexUV(f10, f11, f13, f7, f9);
		}

		if(i5 == 1) {
			tesselator1.vertexUV(f10, f15, f13, f7, f9);
			tesselator1.vertexUV(f10, f15, f12, f7, f16);
			tesselator1.vertexUV(f14, f15, f12, f8, f16);
			tesselator1.vertexUV(f14, f15, f13, f8, f9);
		}

		if(i5 == 2) {
			tesselator1.vertexUV(f10, f11, f12, f8, f9);
			tesselator1.vertexUV(f14, f11, f12, f7, f9);
			tesselator1.vertexUV(f14, f15, f12, f7, f16);
			tesselator1.vertexUV(f10, f15, f12, f8, f16);
		}

		if(i5 == 3) {
			tesselator1.vertexUV(f14, f15, f13, f8, f16);
			tesselator1.vertexUV(f14, f11, f13, f8, f9);
			tesselator1.vertexUV(f10, f11, f13, f7, f9);
			tesselator1.vertexUV(f10, f15, f13, f7, f16);
		}

		if(i5 == 4) {
			tesselator1.vertexUV(f10, f11, f13, f8, f9);
			tesselator1.vertexUV(f10, f11, f12, f7, f9);
			tesselator1.vertexUV(f10, f15, f12, f7, f16);
			tesselator1.vertexUV(f10, f15, f13, f8, f16);
		}

		if(i5 == 5) {
			tesselator1.vertexUV(f14, f15, f13, f7, f16);
			tesselator1.vertexUV(f14, f15, f12, f8, f16);
			tesselator1.vertexUV(f14, f11, f12, f8, f9);
			tesselator1.vertexUV(f14, f11, f13, f7, f9);
		}

	}

	public static void renderFaceNoTexture(Player player0, Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		float f6 = (float)i2;
		float f7 = (float)i2 + 1.0F;
		float f8 = (float)i3;
		float f9 = (float)i3 + 1.0F;
		float f10 = (float)i4;
		float f11 = (float)i4 + 1.0F;
		if(i5 == 0 && (float)i3 > player0.y) {
			tesselator1.vertex(f6, f8, f11);
			tesselator1.vertex(f6, f8, f10);
			tesselator1.vertex(f7, f8, f10);
			tesselator1.vertex(f7, f8, f11);
		}

		if(i5 == 1 && (float)i3 < player0.y) {
			tesselator1.vertex(f7, f9, f11);
			tesselator1.vertex(f7, f9, f10);
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f6, f9, f11);
		}

		if(i5 == 2 && (float)i4 > player0.z) {
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f7, f9, f10);
			tesselator1.vertex(f7, f8, f10);
			tesselator1.vertex(f6, f8, f10);
		}

		if(i5 == 3 && (float)i4 < player0.z) {
			tesselator1.vertex(f6, f9, f11);
			tesselator1.vertex(f6, f8, f11);
			tesselator1.vertex(f7, f8, f11);
			tesselator1.vertex(f7, f9, f11);
		}

		if(i5 == 4 && (float)i2 > player0.x) {
			tesselator1.vertex(f6, f9, f11);
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f6, f8, f10);
			tesselator1.vertex(f6, f8, f11);
		}

		if(i5 == 5 && (float)i2 < player0.x) {
			tesselator1.vertex(f7, f8, f11);
			tesselator1.vertex(f7, f8, f10);
			tesselator1.vertex(f7, f9, f10);
			tesselator1.vertex(f7, f9, f11);
		}

	}

	public static AABB getTileAABB(int i0, int i1, int i2) {
		return new AABB((float)i0, (float)i1, (float)i2, (float)(i0 + 1), (float)(i1 + 1), (float)(i2 + 1));
	}

	public AABB getAABB(int i1, int i2, int i3) {
		return new AABB((float)i1, (float)i2, (float)i3, (float)(i1 + 1), (float)(i2 + 1), (float)(i3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public boolean mayTick() {
		return true;
	}

	public void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
	}

	public final void destroy(Level level1, int i2, int i3, int i4, ParticleEngine particleEngine5) {
		for(int i6 = 0; i6 < 4; ++i6) {
			for(int i7 = 0; i7 < 4; ++i7) {
				for(int i8 = 0; i8 < 4; ++i8) {
					float f9 = (float)i2 + ((float)i6 + 0.5F) / (float)4;
					float f10 = (float)i3 + ((float)i7 + 0.5F) / (float)4;
					float f11 = (float)i4 + ((float)i8 + 0.5F) / (float)4;
                    particleEngine5.add(new Particle(level1, f9, f10, f11, f9 - (float)i2 - 0.5F, f10 - (float)i3 - 0.5F, f11 - (float)i4 - 0.5F, this.tex));
				}
			}
		}

	}

	public int getLiquidType() {
		return 0;
	}

	public void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
	}

	static {
		new Tile(4, 16);
		new Tile(5, 4);
		new Bush(6);
		unbreakable = new Tile(7, 17);
		water = new LiquidTile(8, 1);
		calmWater = new CalmLiquidTile(9, 1);
		lava = new LiquidTile(10, 2);
		calmLava = new CalmLiquidTile(11, 2);
	}
}
