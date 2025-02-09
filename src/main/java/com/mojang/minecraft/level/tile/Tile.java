package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class Tile {
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock;
	public static final Tile grass;
	public static final Tile dirt;
	public static final Tile wood;
	public static final Tile stoneBrick;
	public static final Tile bush;
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public static final Tile sand;
	public static final Tile gravel;
	public static final Tile oreGold;
	public static final Tile oreIron;
	public static final Tile oreCoal;
	public static final Tile log;
	public static final Tile leaf;
	public static final Tile sponge;
	public static final Tile glass;
	public static final Tile clothRed;
	public static final Tile clothOrange;
	public static final Tile clothYellow;
	public static final Tile clothChartreuse;
	public static final Tile clothGreen;
	public static final Tile clothSpringGreen;
	public static final Tile clothCyan;
	public static final Tile clothCapri;
	public static final Tile clothUltramarine;
	public static final Tile clothViolet;
	public static final Tile clothPurple;
	public static final Tile clothMagenta;
	public static final Tile clothRose;
	public static final Tile clothDarkGray;
	public static final Tile clothGray;
	public static final Tile clothWhite;
	public static final Tile plantYellow;
	public static final Tile plantRed;
	public static final Tile mushroomBrown;
	public static final Tile mushroomRed;
	public static final Tile blockGold;
	public int tex;
	public final int id;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;
	public float particleGravity;

	protected Tile(int i1) {
		new EaglercraftRandom();
		tiles[i1] = this;
		this.id = i1;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	protected final void setTicking(boolean z1) {
		shouldTick[this.id] = z1;
	}

	protected final void setShape(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.xx0 = f1;
		this.yy0 = f2;
		this.zz0 = f3;
		this.xx1 = f4;
		this.yy1 = f5;
		this.zz1 = f6;
	}

	protected Tile(int i1, int i2) {
		this(i1);
		this.tex = i2;
	}

	public final void setTickSpeed(int i1) {
		tickSpeed[this.id] = 16;
	}

	public boolean render(Tesselator tesselator1, Level level2, int i3, int i4, int i5, int i6) {
		boolean z7 = false;
		float f8 = 0.5F;
		float f9 = 0.8F;
		float f10 = 0.6F;
		float f11;
		if(this.shouldRenderFace(level2, i4, i5 - 1, i6, i3, 0)) {
			f11 = this.getBrightness(level2, i4, i5 - 1, i6);
			tesselator1.color(f8 * f11, f8 * f11, f8 * f11);
			this.renderFace(tesselator1, i4, i5, i6, 0);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5 + 1, i6, i3, 1)) {
			f11 = this.getBrightness(level2, i4, i5 + 1, i6);
			tesselator1.color(f11 * 1.0F, f11 * 1.0F, f11 * 1.0F);
			this.renderFace(tesselator1, i4, i5, i6, 1);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5, i6 - 1, i3, 2)) {
			f11 = this.getBrightness(level2, i4, i5, i6 - 1);
			tesselator1.color(f9 * f11, f9 * f11, f9 * f11);
			this.renderFace(tesselator1, i4, i5, i6, 2);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4, i5, i6 + 1, i3, 3)) {
			f11 = this.getBrightness(level2, i4, i5, i6 + 1);
			tesselator1.color(f9 * f11, f9 * f11, f9 * f11);
			this.renderFace(tesselator1, i4, i5, i6, 3);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4 - 1, i5, i6, i3, 4)) {
			f11 = this.getBrightness(level2, i4 - 1, i5, i6);
			tesselator1.color(f10 * f11, f10 * f11, f10 * f11);
			this.renderFace(tesselator1, i4, i5, i6, 4);
			z7 = true;
		}

		if(this.shouldRenderFace(level2, i4 + 1, i5, i6, i3, 5)) {
			f11 = this.getBrightness(level2, i4 + 1, i5, i6);
			tesselator1.color(f10 * f11, f10 * f11, f10 * f11);
			this.renderFace(tesselator1, i4, i5, i6, 5);
			z7 = true;
		}

		return z7;
	}

	protected float getBrightness(Level level1, int i2, int i3, int i4) {
		return level1.getBrightness(i2, i3, i4);
	}

	public static boolean cullFace(Level level0, int i1, int i2, int i3, int i4) {
		if(i4 == 0) {
			--i2;
		}

		if(i4 == 1) {
			++i2;
		}

		if(i4 == 2) {
			--i3;
		}

		if(i4 == 3) {
			++i3;
		}

		if(i4 == 4) {
			--i1;
		}

		if(i4 == 5) {
			++i1;
		}

		return !level0.isSolidTile(i1, i2, i3);
	}

	protected boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
		return i5 == 1 ? false : !level1.isSolidTile(i2, i3, i4);
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

	public static void renderFaceNoTexture(Entity entity0, Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		float f6 = (float)i2;
		float f7 = (float)i2 + 1.0F;
		float f8 = (float)i3;
		float f9 = (float)i3 + 1.0F;
		float f10 = (float)i4;
		float f11 = (float)i4 + 1.0F;
		if(i5 == 0 && (float)i3 > entity0.y) {
			tesselator1.vertex(f6, f8, f11);
			tesselator1.vertex(f6, f8, f10);
			tesselator1.vertex(f7, f8, f10);
			tesselator1.vertex(f7, f8, f11);
		}

		if(i5 == 1 && (float)i3 < entity0.y) {
			tesselator1.vertex(f7, f9, f11);
			tesselator1.vertex(f7, f9, f10);
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f6, f9, f11);
		}

		if(i5 == 2 && (float)i4 > entity0.z) {
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f7, f9, f10);
			tesselator1.vertex(f7, f8, f10);
			tesselator1.vertex(f6, f8, f10);
		}

		if(i5 == 3 && (float)i4 < entity0.z) {
			tesselator1.vertex(f6, f9, f11);
			tesselator1.vertex(f6, f8, f11);
			tesselator1.vertex(f7, f8, f11);
			tesselator1.vertex(f7, f9, f11);
		}

		if(i5 == 4 && (float)i2 > entity0.x) {
			tesselator1.vertex(f6, f9, f11);
			tesselator1.vertex(f6, f9, f10);
			tesselator1.vertex(f6, f8, f10);
			tesselator1.vertex(f6, f8, f11);
		}

		if(i5 == 5 && (float)i2 < entity0.x) {
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

	public boolean mayPick() {
		return true;
	}

	public void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
	}

	public final void destroy(Level level1, int i2, int i3, int i4, ParticleEngine particleEngine) {
		for(int i6 = 0; i6 < 4; ++i6) {
			for(int i7 = 0; i7 < 4; ++i7) {
				for(int i8 = 0; i8 < 4; ++i8) {
					float f9 = (float)i2 + ((float)i6 + 0.5F) / (float)4;
					float f10 = (float)i3 + ((float)i7 + 0.5F) / (float)4;
					float f11 = (float)i4 + ((float)i8 + 0.5F) / (float)4;
					Particle particle12 = new Particle(level1, f9, f10, f11, f9 - (float)i2 - 0.5F, f10 - (float)i3 - 0.5F, f11 - (float)i4 - 0.5F, this);
					particleEngine.particles.add(particle12);
				}
			}
		}

	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
	}

	public void onBlockAdded(Level level1, int i2, int i3, int i4) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level level1, int i2, int i3, int i4) {
	}

	public void onTileRemoved(Level level1, int i2, int i3, int i4) {
	}

	static {
		Tile tile10000 = new Tile(1, 1);
		float f1 = 1.0F;
		float f0 = 1.0F;
		Tile tile2 = tile10000;
		tile10000.particleGravity = f1;
		rock = tile2;
		GrassTile grassTile13 = new GrassTile(2);
		f1 = 1.0F;
		f0 = 0.9F;
		GrassTile grassTile3 = grassTile13;
		grassTile13.particleGravity = f1;
		grass = grassTile3;
		DirtTile dirtTile14 = new DirtTile(3, 2);
		f1 = 1.0F;
		f0 = 0.8F;
		DirtTile dirtTile4 = dirtTile14;
		dirtTile14.particleGravity = f1;
		dirt = dirtTile4;
		tile10000 = new Tile(4, 16);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		wood = tile2;
		tile10000 = new Tile(5, 4);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		stoneBrick = tile2;
		Bush bush15 = new Bush(6, 15);
		f1 = 1.0F;
		f0 = 0.7F;
		Bush bush5 = bush15;
		bush15.particleGravity = f1;
		bush = bush5;
		tile10000 = new Tile(7, 17);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		unbreakable = tile2;
		LiquidTile liquidTile16 = new LiquidTile(8, Liquid.water);
		f1 = 1.0F;
		f0 = 1.0F;
		LiquidTile liquidTile6 = liquidTile16;
		liquidTile16.particleGravity = f1;
		water = liquidTile6;
		CalmLiquidTile calmLiquidTile17 = new CalmLiquidTile(9, Liquid.water);
		f1 = 1.0F;
		f0 = 1.0F;
		CalmLiquidTile calmLiquidTile7 = calmLiquidTile17;
		calmLiquidTile17.particleGravity = f1;
		calmWater = calmLiquidTile7;
		liquidTile16 = new LiquidTile(10, Liquid.lava);
		f1 = 1.0F;
		f0 = 1.0F;
		liquidTile6 = liquidTile16;
		liquidTile16.particleGravity = f1;
		lava = liquidTile6;
		calmLiquidTile17 = new CalmLiquidTile(11, Liquid.lava);
		f1 = 1.0F;
		f0 = 1.0F;
		calmLiquidTile7 = calmLiquidTile17;
		calmLiquidTile17.particleGravity = f1;
		calmLava = calmLiquidTile7;
		FallingTile fallingTile18 = new FallingTile(12, 18);
		f1 = 1.0F;
		f0 = 0.8F;
		FallingTile fallingTile8 = fallingTile18;
		fallingTile18.particleGravity = f1;
		sand = fallingTile8;
		fallingTile18 = new FallingTile(13, 19);
		f1 = 1.0F;
		f0 = 0.8F;
		fallingTile8 = fallingTile18;
		fallingTile18.particleGravity = f1;
		gravel = fallingTile8;
		tile10000 = new Tile(14, 32);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		oreGold = tile2;
		tile10000 = new Tile(15, 33);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		oreIron = tile2;
		tile10000 = new Tile(16, 34);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		oreCoal = tile2;
		LogTile logTile19 = new LogTile(17);
		f1 = 1.0F;
		f0 = 1.0F;
		LogTile logTile9 = logTile19;
		logTile19.particleGravity = f1;
		log = logTile9;
		LeafTile leafTile20 = new LeafTile(18, 22, true);
		f1 = 0.4F;
		f0 = 1.0F;
		LeafTile leafTile10 = leafTile20;
		leafTile20.particleGravity = f1;
		leaf = leafTile10;
		SpongeTile spongeTile21 = new SpongeTile(19);
		f1 = 0.9F;
		f0 = 1.0F;
		SpongeTile spongeTile11 = spongeTile21;
		spongeTile21.particleGravity = f1;
		sponge = spongeTile11;
		GlassTile glassTile22 = new GlassTile(20, 49, false);
		f1 = 1.0F;
		f0 = 1.0F;
		GlassTile glassTile12 = glassTile22;
		glassTile22.particleGravity = f1;
		glass = glassTile12;
		tile10000 = new Tile(21, 64);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothRed = tile2;
		tile10000 = new Tile(22, 65);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothOrange = tile2;
		tile10000 = new Tile(23, 66);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothYellow = tile2;
		tile10000 = new Tile(24, 67);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothChartreuse = tile2;
		tile10000 = new Tile(25, 68);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothGreen = tile2;
		tile10000 = new Tile(26, 69);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothSpringGreen = tile2;
		tile10000 = new Tile(27, 70);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothCyan = tile2;
		tile10000 = new Tile(28, 71);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothCapri = tile2;
		tile10000 = new Tile(29, 72);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothUltramarine = tile2;
		tile10000 = new Tile(30, 73);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothViolet = tile2;
		tile10000 = new Tile(31, 74);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothPurple = tile2;
		tile10000 = new Tile(32, 75);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothMagenta = tile2;
		tile10000 = new Tile(33, 76);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothRose = tile2;
		tile10000 = new Tile(34, 77);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothDarkGray = tile2;
		tile10000 = new Tile(35, 78);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothGray = tile2;
		tile10000 = new Tile(36, 79);
		f1 = 1.0F;
		f0 = 1.0F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		clothWhite = tile2;
		bush15 = new Bush(37, 13);
		f1 = 1.0F;
		f0 = 0.7F;
		bush5 = bush15;
		bush15.particleGravity = f1;
		plantYellow = bush5;
		bush15 = new Bush(38, 12);
		f1 = 1.0F;
		f0 = 0.7F;
		bush5 = bush15;
		bush15.particleGravity = f1;
		plantRed = bush5;
		bush15 = new Bush(39, 29);
		f1 = 1.0F;
		f0 = 0.7F;
		bush5 = bush15;
		bush15.particleGravity = f1;
		mushroomBrown = bush5;
		bush15 = new Bush(40, 28);
		f1 = 1.0F;
		f0 = 0.7F;
		bush5 = bush15;
		bush15.particleGravity = f1;
		mushroomRed = bush5;
		tile10000 = new Tile(41, 40);
		f1 = 1.0F;
		f0 = 0.7F;
		tile2 = tile10000;
		tile10000.particleGravity = f1;
		blockGold = tile2;
	}
}
