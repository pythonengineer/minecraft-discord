package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public class Flower extends Tile {
	protected Flower(int i1, int i2) {
		super(i1);
		this.tex = i2;
		this.setTicking(true);
		float f3 = 0.2F;
		this.setShape(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 2.0F, f3 + 0.5F);
	}

	public void tick(Level level, int x, int y, int z, EaglercraftRandom random) {
		int i6 = level.getTile(x, y - 1, z);
		if(!level.isLit(x, y, z) || i6 != Tile.dirt.id && i6 != Tile.grass.id) {
			level.setTile(x, y, z, 0);
		}

	}

	public final boolean render(Tesselator t, Level level, int layer, int x, int y, int z) {
		float level1 = level.getBrightness(x, y, z);
		t.color(level1, level1, level1);
		this.renderFlower(t, (float)x, (float)y, (float)z);
		return true;
	}

	/**
	 * NOT OFFICIAL
	 */
	private void renderFlower(Tesselator t, float x, float y, float z) {
		int i15 = this.getTexture(15);
		int i5;
		float f6;
		float f7;
		int i8;
		float f16;
		float f17;
		if(!isNormalTile) {
			i5 = i15 % 16 << 4;
			i8 = i15 / 16 << 4;
			f16 = (float)i5 / 256.0F;
			f17 = ((float)i5 + 15.99F) / 256.0F;
			f6 = (float)i8 / 256.0F;
			f7 = ((float)i8 + 15.99F) / 256.0F;
		} else {
			i8 = ((i5 = i15 % 16) << 4) + i15 / 16 << 4;
			f16 = 0.0F;
			f17 = 1.0F;
			f6 = (float)i8 / 4096.0F;
			f7 = ((float)i8 + 15.99F) / 4096.0F;
		}

		for(i8 = 0; i8 < 2; ++i8) {
			float f9 = (float)(Math.sin((double)i8 * Math.PI / (double)2 + 0.7853981633974483D) * 0.5D);
			float f10 = (float)(Math.cos((double)i8 * Math.PI / (double)2 + 0.7853981633974483D) * 0.5D);
			float f11 = x + 0.5F - f9;
			f9 += x + 0.5F;
			float f13 = y + 1.0F;
			float f14 = z + 0.5F - f10;
			f10 += z + 0.5F;
			t.vertexUV(f11, f13, f14, f17, f6);
			t.vertexUV(f9, f13, f10, f16, f6);
			t.vertexUV(f9, y, f10, f16, f7);
			t.vertexUV(f11, y, f14, f17, f7);
			t.vertexUV(f9, f13, f10, f17, f6);
			t.vertexUV(f11, f13, f14, f16, f6);
			t.vertexUV(f11, y, f14, f16, f7);
			t.vertexUV(f9, y, f10, f17, f7);
		}

	}

	public final AABB getTileAABB(int x, int y, int z) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}

	public final void renderGuiTile(Tesselator t) {
		t.normal(0.0F, 1.0F, 0.0F);
		t.begin(DefaultVertexFormats.POSITION_TEX);
		this.renderFlower(t, 0.0F, 0.4F, -0.3F);
		t.end();
	}

	public final boolean isOpaque() {
		return false;
	}
}