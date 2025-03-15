package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public class Tile {
	public static boolean isNormalTile = true;
	protected static EaglercraftRandom random = new EaglercraftRandom();
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	public static final boolean[] isSolid = new boolean[256];
	public static final boolean[] isOpaque = new boolean[256];
	public static final boolean[] isLiquid = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock = (new Tile(1, 1)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 1.0F);
	public static final Tile grass = (new GrassTile(2)).setSoundAndGravity(Tile.SoundType.grass, 0.9F, 1.0F, 0.6F);
	public static final Tile dirt = (new DirtTile(3, 2)).setSoundAndGravity(Tile.SoundType.grass, 0.8F, 1.0F, 0.5F);
	public static final Tile stoneBrick = (new Tile(4, 16)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 2.0F);
	public static final Tile wood = (new Tile(5, 4)).setSoundAndGravity(Tile.SoundType.wood, 1.0F, 1.0F, 2.0F);
	public static final Tile bush = (new Bush(6, 15)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile unbreakable = (new Tile(7, 17)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 100.0F);
	public static final Tile water = (new LiquidTile(8, Liquid.water)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile calmWater = (new CalmLiquidTile(9, Liquid.water)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile lava = (new LiquidTile(10, Liquid.lava)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile calmLava = (new CalmLiquidTile(11, Liquid.lava)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile sand = (new FallingTile(12, 18)).setSoundAndGravity(Tile.SoundType.gravel, 0.8F, 1.0F, 0.5F);
	public static final Tile gravel = (new FallingTile(13, 19)).setSoundAndGravity(Tile.SoundType.gravel, 0.8F, 1.0F, 0.6F);
	public static final Tile goldOre = (new Tile(14, 32)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile ironOre = (new Tile(15, 33)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile coalOre = (new Tile(16, 34)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile log = (new LogTile(17)).setSoundAndGravity(Tile.SoundType.wood, 1.0F, 1.0F, 2.5F);
	public static final Tile leaf = (new LeafTile(18, 22)).setSoundAndGravity(Tile.SoundType.grass, 1.0F, 0.4F, 0.2F);
	public static final Tile sponge = (new SpongeTile(19)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 0.9F, 0.6F);
	public static final Tile glass = (new GlassTile(20, 49, false)).setSoundAndGravity(Tile.SoundType.metal, 1.0F, 1.0F, 0.3F);
	public static final Tile clothRed = (new Tile(21, 64)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothOrange = (new Tile(22, 65)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothYellow = (new Tile(23, 66)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothChartreuse = (new Tile(24, 67)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothGreen = (new Tile(25, 68)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothSpringGreen = (new Tile(26, 69)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothCyan = (new Tile(27, 70)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothCapri = (new Tile(28, 71)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothUltramarine = (new Tile(29, 72)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothViolet = (new Tile(30, 73)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothPurple = (new Tile(31, 74)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothMagenta = (new Tile(32, 75)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothRose = (new Tile(33, 76)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothDarkGray = (new Tile(34, 77)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothGray = (new Tile(35, 78)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothWhite = (new Tile(36, 79)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile flower = (new Flower(37, 13)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile rose = (new Flower(38, 12)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile mushroom1 = (new Mushroom(39, 29)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile mushroom2 = (new Mushroom(40, 28)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile goldBlock = (new Tile(41, 40)).setSoundAndGravity(Tile.SoundType.metal, 0.7F, 1.0F, 3.0F);
	public int tex;
	public final int id;
	public Tile.SoundType soundType;
	private int destroyProgress;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;
	public float particleGravity;

	protected Tile(int id) {
		tiles[id] = this;
		this.id = id;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		isSolid[id] = this.isSolid();
		isOpaque[id] = this.isOpaque();
		isLiquid[id] = false;
	}

	public boolean isOpaque() {
		return true;
	}

	protected final Tile setSoundAndGravity(Tile.SoundType soundType, float volume, float gravity, float pitch) {
		this.particleGravity = gravity;
		this.soundType = soundType;
		this.destroyProgress = (int)(pitch * 20.0F);
		return this;
	}

	protected final void setTicking(boolean tick) {
		shouldTick[this.id] = tick;
	}

	protected final void setShape(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.xx0 = x0;
		this.yy0 = y0;
		this.zz0 = z0;
		this.xx1 = x1;
		this.yy1 = y1;
		this.zz1 = z1;
	}

	protected Tile(int id, int tex) {
		this(id);
		this.tex = tex;
	}

	public final void setTickSpeed(int ts) {
		tickSpeed[this.id] = 16;
	}

	public boolean render(Tesselator t, Level level, int layer, int x, int y, int z) {
		boolean z7 = false;
		float f8 = 0.5F;
		float f9 = 0.8F;
		float f10 = 0.6F;
		float f11;
		if(this.shouldRenderFace(level, x, y - 1, z, layer, 0)) {
			f11 = this.getBrightness(level, x, y - 1, z);
			t.color(f8 * f11, f8 * f11, f8 * f11);
			this.renderFace(t, x, y, z, 0);
			z7 = true;
		}

		if(this.shouldRenderFace(level, x, y + 1, z, layer, 1)) {
			f11 = this.getBrightness(level, x, y + 1, z);
			t.color(f11 * 1.0F, f11 * 1.0F, f11 * 1.0F);
			this.renderFace(t, x, y, z, 1);
			z7 = true;
		}

		if(this.shouldRenderFace(level, x, y, z - 1, layer, 2)) {
			f11 = this.getBrightness(level, x, y, z - 1);
			t.color(f9 * f11, f9 * f11, f9 * f11);
			this.renderFace(t, x, y, z, 2);
			z7 = true;
		}

		if(this.shouldRenderFace(level, x, y, z + 1, layer, 3)) {
			f11 = this.getBrightness(level, x, y, z + 1);
			t.color(f9 * f11, f9 * f11, f9 * f11);
			this.renderFace(t, x, y, z, 3);
			z7 = true;
		}

		if(this.shouldRenderFace(level, x - 1, y, z, layer, 4)) {
			f11 = this.getBrightness(level, x - 1, y, z);
			t.color(f10 * f11, f10 * f11, f10 * f11);
			this.renderFace(t, x, y, z, 4);
			z7 = true;
		}

		if(this.shouldRenderFace(level, x + 1, y, z, layer, 5)) {
			f11 = this.getBrightness(level, x + 1, y, z);
			t.color(f10 * f11, f10 * f11, f10 * f11);
			this.renderFace(t, x, y, z, 5);
			z7 = true;
		}

		return z7;
	}

	protected float getBrightness(Level level, int x, int y, int z) {
		return level.getBrightness(x, y, z);
	}

	public boolean shouldRenderFace(Level level, int x, int y, int z, int layer, int face) {
		return layer == 1 ? false : !level.isSolidTile(x, y, z);
	}

	protected int getTexture(int face) {
		return this.tex;
	}

	public void renderFace(Tesselator t, int x, int y, int z, int face) {
		int i6 = this.getTexture(face);
		this.renderFaceNoTexture(t, x, y, z, face, i6);
	}

	public final void renderFaceNoTexture(Tesselator t, int x, int y, int z, int face, int tex) {
		int i7 = tex % 16 << 4;
		tex = tex / 16 << 4;
		float f8 = (float)i7 / 256.0F;
		float f17 = ((float)i7 + 15.99F) / 256.0F;
		float f9 = (float)tex / 256.0F;
		float tex1 = ((float)tex + 15.99F) / 256.0F;
		float f10 = (float)x + this.xx0;
		float x1 = (float)x + this.xx1;
		float f11 = (float)y + this.yy0;
		float y1 = (float)y + this.yy1;
		float f12 = (float)z + this.zz0;
		float f13 = (float)z + this.zz1;
		if(face == 0) {
			t.vertexUV(f10, f11, f13, f8, tex1);
			t.vertexUV(f10, f11, f12, f8, f9);
			t.vertexUV(x1, f11, f12, f17, f9);
			t.vertexUV(x1, f11, f13, f17, tex1);
		} else if(face == 1) {
			t.vertexUV(x1, y1, f13, f17, tex1);
			t.vertexUV(x1, y1, f12, f17, f9);
			t.vertexUV(f10, y1, f12, f8, f9);
			t.vertexUV(f10, y1, f13, f8, tex1);
		} else if(face == 2) {
			t.vertexUV(f10, y1, f12, f17, f9);
			t.vertexUV(x1, y1, f12, f8, f9);
			t.vertexUV(x1, f11, f12, f8, tex1);
			t.vertexUV(f10, f11, f12, f17, tex1);
		} else if(face == 3) {
			t.vertexUV(f10, y1, f13, f8, f9);
			t.vertexUV(f10, f11, f13, f8, tex1);
			t.vertexUV(x1, f11, f13, f17, tex1);
			t.vertexUV(x1, y1, f13, f17, f9);
		} else if(face == 4) {
			t.vertexUV(f10, y1, f13, f17, f9);
			t.vertexUV(f10, y1, f12, f8, f9);
			t.vertexUV(f10, f11, f12, f8, tex1);
			t.vertexUV(f10, f11, f13, f17, tex1);
		} else if(face == 5) {
			t.vertexUV(x1, f11, f13, f8, tex1);
			t.vertexUV(x1, f11, f12, f17, tex1);
			t.vertexUV(x1, y1, f12, f17, f9);
			t.vertexUV(x1, y1, f13, f8, f9);
		}
	}

	public final void renderFace(Tesselator t, int x, int y, int z, int face, int tex) {
		int i7 = this.getTexture(face);
		int i10006 = i7;
		i7 = tex;
		tex = i10006;
		float f8;
		float f9;
		float f10;
		int i11;
		int i12;
		float tex1;
		if(!isNormalTile) {
			i11 = tex % 16 << 4;
			i12 = tex / 16 << 4;
			tex1 = (float)i11 / 256.0F;
			f8 = ((float)i11 + 15.99F) / 256.0F;
			f9 = (float)i12 / 256.0F;
			f10 = ((float)i12 + 15.99F) / 256.0F;
		} else {
			i12 = ((i11 = tex % 16) << 4) + tex / 16 << 4;
			tex1 = 0.0F;
			f8 = 0.0F;
			f9 = (float)i12 / 4096.0F;
			f10 = ((float)i12 + 15.99F) / 4096.0F;
			f8 = 1.0F + (float)i7;
		}

		float f19 = 0.001F;
		float f20 = (float)x + this.xx0 - f19;
		float x1 = (float)x + this.xx1 + f19;
		float f13 = (float)y + this.yy0 - f19;
		float y1 = (float)y + this.yy1 + f19;
		float f14 = (float)z + this.zz0 - f19;
		float f15 = (float)z + this.zz1 - f19;
		if(face == 0) {
			x1 += (float)i7;
			t.vertexUV(f20, f13, f15, tex1, f10);
			t.vertexUV(f20, f13, f14, tex1, f9);
			t.vertexUV(x1, f13, f14, f8, f9);
			t.vertexUV(x1, f13, f15, f8, f10);
		} else if(face == 1) {
			x1 += (float)i7;
			t.vertexUV(x1, y1, f15, f8, f10);
			t.vertexUV(x1, y1, f14, f8, f9);
			t.vertexUV(f20, y1, f14, tex1, f9);
			t.vertexUV(f20, y1, f15, tex1, f10);
		} else if(face == 2) {
			x1 += (float)i7;
			t.vertexUV(f20, y1, f14, f8, f9);
			t.vertexUV(x1, y1, f14, tex1, f9);
			t.vertexUV(x1, f13, f14, tex1, f10);
			t.vertexUV(f20, f13, f14, f8, f10);
		} else if(face == 3) {
			x1 += (float)i7;
			t.vertexUV(f20, y1, f15, tex1, f9);
			t.vertexUV(f20, f13, f15, tex1, f10);
			t.vertexUV(x1, f13, f15, f8, f10);
			t.vertexUV(x1, y1, f15, f8, f9);
		} else if(face == 4) {
			f15 += (float)i7;
			t.vertexUV(f20, y1, f15, f8, f9);
			t.vertexUV(f20, y1, f14, tex1, f9);
			t.vertexUV(f20, f13, f14, tex1, f10);
			t.vertexUV(f20, f13, f15, f8, f10);
		} else {
			if(face == 5) {
				f15 += (float)i7;
				t.vertexUV(x1, f13, f15, tex1, f10);
				t.vertexUV(x1, f13, f14, f8, f10);
				t.vertexUV(x1, y1, f14, f8, f9);
				t.vertexUV(x1, y1, f15, tex1, f9);
			}

		}
	}

	public final void renderBackFace(Tesselator t, int x, int y, int z, int face) {
		int i6;
		float f7;
		float f8 = (f7 = (float)((i6 = this.getTexture(face)) % 16) / 16.0F) + 0.0624375F;
		float f16;
		float f9 = (f16 = (float)(i6 / 16) / 16.0F) + 0.0624375F;
		float f10 = (float)x + this.xx0;
		float x1 = (float)x + this.xx1;
		float f11 = (float)y + this.yy0;
		float y1 = (float)y + this.yy1;
		float f12 = (float)z + this.zz0;
		float f13 = (float)z + this.zz1;
		if(face == 0) {
			t.vertexUV(x1, f11, f13, f8, f9);
			t.vertexUV(x1, f11, f12, f8, f16);
			t.vertexUV(f10, f11, f12, f7, f16);
			t.vertexUV(f10, f11, f13, f7, f9);
		}

		if(face == 1) {
			t.vertexUV(f10, y1, f13, f7, f9);
			t.vertexUV(f10, y1, f12, f7, f16);
			t.vertexUV(x1, y1, f12, f8, f16);
			t.vertexUV(x1, y1, f13, f8, f9);
		}

		if(face == 2) {
			t.vertexUV(f10, f11, f12, f8, f9);
			t.vertexUV(x1, f11, f12, f7, f9);
			t.vertexUV(x1, y1, f12, f7, f16);
			t.vertexUV(f10, y1, f12, f8, f16);
		}

		if(face == 3) {
			t.vertexUV(x1, y1, f13, f8, f16);
			t.vertexUV(x1, f11, f13, f8, f9);
			t.vertexUV(f10, f11, f13, f7, f9);
			t.vertexUV(f10, y1, f13, f7, f16);
		}

		if(face == 4) {
			t.vertexUV(f10, f11, f13, f8, f9);
			t.vertexUV(f10, f11, f12, f7, f9);
			t.vertexUV(f10, y1, f12, f7, f16);
			t.vertexUV(f10, y1, f13, f8, f16);
		}

		if(face == 5) {
			t.vertexUV(x1, y1, f13, f7, f16);
			t.vertexUV(x1, y1, f12, f8, f16);
			t.vertexUV(x1, f11, f12, f8, f9);
			t.vertexUV(x1, f11, f13, f7, f9);
		}

	}

	public AABB getTileAABB(int x, int y, int z) {
		return new AABB((float)x, (float)y, (float)z, (float)(x + 1), (float)(y + 1), (float)(z + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level level, int x, int y, int z, EaglercraftRandom random) {
	}

	public final void destroy(Level level, int x, int y, int z, ParticleEngine particleEngine) {
		for(int i6 = 0; i6 < 4; ++i6) {
			for(int i7 = 0; i7 < 4; ++i7) {
				for(int i8 = 0; i8 < 4; ++i8) {
					float f9 = (float)x + ((float)i6 + 0.5F) / (float)4;
					float f10 = (float)y + ((float)i7 + 0.5F) / (float)4;
					float f11 = (float)z + ((float)i8 + 0.5F) / (float)4;
					particleEngine.addParticle(new Particle(level, f9, f10, f11, f9 - (float)x - 0.5F, f10 - (float)y - 0.5F, f11 - (float)z - 0.5F, this));
				}
			}
		}

	}

	public final void destroy(Level level, int x, int y, int z, int face, ParticleEngine particleEngine) {
		float f7 = 0.1F;
		float f8 = (float)x + random.nextFloat() * (1.0F - f7 * 2.0F) + f7;
		float f9 = (float)y + random.nextFloat() * (1.0F - f7 * 2.0F) + f7;
		float f10 = (float)z + random.nextFloat() * (1.0F - f7 * 2.0F) + f7;
		if(face == 0) {
			f9 = (float)y - f7;
		}

		if(face == 1) {
			f9 = (float)(y + 1) + f7;
		}

		if(face == 2) {
			f10 = (float)z - f7;
		}

		if(face == 3) {
			f10 = (float)(z + 1) + f7;
		}

		if(face == 4) {
			f8 = (float)x - f7;
		}

		if(face == 5) {
			f8 = (float)(x + 1) + f7;
		}

		particleEngine.addParticle((new Particle(level, f8, f9, f10, 0.0F, 0.0F, 0.0F, this)).setPower(0.2F).scale(0.6F));
	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level level, int x, int y, int z, int type) {
	}

	public void onPlace(Level level, int x, int y, int z) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level level, int x, int y, int z) {
	}

	public void onTileRemoved(Level level, int x, int y, int z) {
	}

	public int getResourceCount() {
		return 1;
	}

	public int getId() {
		return this.id;
	}

	public final int getDestroyProgress() {
		return this.destroyProgress;
	}

	public void spawnResources(Level level, int x, int y, int z) {
		this.wasExploded(level, x, y, z, 1.0F);
	}

	public void wasExploded(Level level, int x, int y, int z, float chance) {
		int i6 = this.getResourceCount();

		for(int i7 = 0; i7 < i6; ++i7) {
			if(random.nextFloat() <= chance) {
				float f8 = 0.7F;
				float f9 = random.nextFloat() * f8 + (1.0F - f8) * 0.5F;
				float f10 = random.nextFloat() * f8 + (1.0F - f8) * 0.5F;
				f8 = random.nextFloat() * f8 + (1.0F - f8) * 0.5F;
				level.addEntity(new Item(level, (float)x + f9, (float)y + f10, (float)z + f8, this.getId()));
			}
		}

	}

	public void renderGuiTile(Tesselator t) {
		t.begin(DefaultVertexFormats.POSITION_TEX);

		for(int i2 = 0; i2 < 6; ++i2) {
			if(i2 == 0) {
				t.normal(0.0F, 1.0F, 0.0F);
			}

			if(i2 == 1) {
				t.normal(0.0F, -1.0F, 0.0F);
			}

			if(i2 == 2) {
				t.normal(0.0F, 0.0F, 1.0F);
			}

			if(i2 == 3) {
				t.normal(0.0F, 0.0F, -1.0F);
			}

			if(i2 == 4) {
				t.normal(1.0F, 0.0F, 0.0F);
			}

			if(i2 == 5) {
				t.normal(-1.0F, 0.0F, 0.0F);
			}

			this.renderFace(t, 0, 0, 0, i2);
		}

		t.end();
	}

	public static enum SoundType {
		none("-", 0.0F, 0.0F),
		grass("grass", 0.6F, 1.0F),
		cloth("grass", 0.7F, 1.2F),
		gravel("gravel", 1.0F, 1.0F),
		stone("stone", 1.0F, 1.0F),
		metal("stone", 1.0F, 2.0F),
		wood("wood", 1.0F, 1.0F);

		public final String name;
		private final float volume;
		private final float pitch;

		private SoundType(String string3, float f4, float f5) {
			this.name = string3;
			this.volume = f4;
			this.pitch = f5;
		}

		public final float getVolume() {
			return this.volume / (Tile.random.nextFloat() * 0.4F + 1.0F) * 0.5F;
		}

		public final float getPitch() {
			return this.pitch / (Tile.random.nextFloat() * 0.2F + 0.9F);
		}
	}
}