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
	protected static EaglercraftRandom random = new EaglercraftRandom();
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock = (new Tile(1, 1)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile grass = (new GrassTile(2)).setSoundAndGravity(Tile$SoundType.grass, 0.9F, 1.0F);
	public static final Tile dirt = (new DirtTile(3, 2)).setSoundAndGravity(Tile$SoundType.grass, 0.8F, 1.0F);
	public static final Tile wood = (new Tile(4, 16)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile stoneBrick = (new Tile(5, 4)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F);
	public static final Tile bush = (new Bush(6, 15)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F);
	public static final Tile unbreakable = (new Tile(7, 17)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile water = (new LiquidTile(8, Liquid.water)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F);
	public static final Tile calmWater = (new CalmLiquidTile(9, Liquid.water)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F);
	public static final Tile lava = (new LiquidTile(10, Liquid.lava)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F);
	public static final Tile calmLava = (new CalmLiquidTile(11, Liquid.lava)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F);
	public static final Tile sand = (new FallingTile(12, 18)).setSoundAndGravity(Tile$SoundType.gravel, 0.8F, 1.0F);
	public static final Tile gravel = (new FallingTile(13, 19)).setSoundAndGravity(Tile$SoundType.gravel, 0.8F, 1.0F);
	public static final Tile oreGold = (new Tile(14, 32)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile oreIron = (new Tile(15, 33)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile oreCoal = (new Tile(16, 34)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F);
	public static final Tile log = (new LogTile(17)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F);
	public static final Tile leaf = (new LeafTile(18, 22, true)).setSoundAndGravity(Tile$SoundType.grass, 1.0F, 0.4F);
	public static final Tile sponge = (new SpongeTile(19)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 0.9F);
	public static final Tile glass = (new GlassTile(20, 49, false)).setSoundAndGravity(Tile$SoundType.metal, 1.0F, 1.0F);
	public static final Tile clothRed = (new Tile(21, 64)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothOrange = (new Tile(22, 65)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothYellow = (new Tile(23, 66)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothChartreuse = (new Tile(24, 67)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothGreen = (new Tile(25, 68)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothSpringGreen = (new Tile(26, 69)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothCyan = (new Tile(27, 70)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothCapri = (new Tile(28, 71)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothUltramarine = (new Tile(29, 72)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothViolet = (new Tile(30, 73)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothPurple = (new Tile(31, 74)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothMagenta = (new Tile(32, 75)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothRose = (new Tile(33, 76)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothDarkGray = (new Tile(34, 77)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothGray = (new Tile(35, 78)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile clothWhite = (new Tile(36, 79)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F);
	public static final Tile plantYellow = (new Bush(37, 13)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F);
	public static final Tile plantRed = (new Bush(38, 12)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F);
	public static final Tile mushroomBrown = (new Bush(39, 29)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F);
	public static final Tile mushroomRed = (new Bush(40, 28)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F);
	public static final Tile blockGold = (new Tile(41, 40)).setSoundAndGravity(Tile$SoundType.metal, 0.7F, 1.0F);
	public int tex;
	public final int id;
	public Tile$SoundType soundType;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;
	public float particleGravity;

	protected Tile(int i1) {
		tiles[i1] = this;
		this.id = i1;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	protected final Tile setSoundAndGravity(Tile$SoundType tile$SoundType1, float f2, float f3) {
		this.particleGravity = f3;
		this.soundType = tile$SoundType1;
		return this;
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

	public AABB getTileAABB(int i1, int i2, int i3) {
		return new AABB((float)i1, (float)i2, (float)i3, (float)(i1 + 1), (float)(i2 + 1), (float)(i3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
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
					Particle particle12 = new Particle(level1, f9, f10, f11, f9 - (float)i2 - 0.5F, f10 - (float)i3 - 0.5F, f11 - (float)i4 - 0.5F, this);
					particleEngine5.particles.add(particle12);
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
}