package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class Bush extends Tile {
	protected Bush(int i1) {
		super(6);
		this.tex = 15;
		this.setTicking(true);
	}

	public final void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
		int i6 = level1.getTile(i2, i3 - 1, i4);
		if(!level1.isLit(i2, i3, i4) || i6 != Tile.dirt.id && i6 != Tile.grass.id) {
			level1.setTile(i2, i3, i4, 0);
		}

	}

	public final boolean render(Tesselator tesselator1, Level level2, int i3, int i4, int i5, int i6) {
		if(level2.isLit(i4, i5, i6) ^ i3 != 1) {
			return false;
		} else {
			int i15;
			float f17;
			float f18 = (f17 = (float)((i15 = this.getTexture(15)) % 16) / 16.0F) + 0.0624375F;
			float f16;
			float f7 = (f16 = (float)(i15 / 16) / 16.0F) + 0.0624375F;
			tesselator1.color((int)255, (int)255, (int)255);

			for(int i8 = 0; i8 < 2; ++i8) {
				float f9 = (float)(Math.sin((double)i8 * Math.PI / (double)2 + 0.7853981633974483D) * 0.5D);
				float f10 = (float)(Math.cos((double)i8 * Math.PI / (double)2 + 0.7853981633974483D) * 0.5D);
				float f11 = (float)i4 + 0.5F - f9;
				f9 += (float)i4 + 0.5F;
				float f12 = (float)i5;
				float f13 = (float)i5 + 1.0F;
				float f14 = (float)i6 + 0.5F - f10;
				f10 += (float)i6 + 0.5F;
				tesselator1.vertexUV(f11, f13, f14, f18, f16);
				tesselator1.vertexUV(f9, f13, f10, f17, f16);
				tesselator1.vertexUV(f9, f12, f10, f17, f7);
				tesselator1.vertexUV(f11, f12, f14, f18, f7);
				tesselator1.vertexUV(f9, f13, f10, f18, f16);
				tesselator1.vertexUV(f11, f13, f14, f17, f16);
				tesselator1.vertexUV(f11, f12, f14, f17, f7);
				tesselator1.vertexUV(f9, f12, f10, f18, f7);
			}

			return true;
		}
	}

	public final AABB getAABB(int i1, int i2, int i3) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}
}
