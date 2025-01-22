package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class LiquidTile extends Tile {
	protected int liquidType;
	protected int calmTileId;
	protected int tileId;
	private int spreadSpeed = 1;

	protected LiquidTile(int i1, int i2) {
		super(i1);
		this.liquidType = i2;
		this.tex = 14;
		if(i2 == 2) {
			this.tex = 30;
		}

		if(i2 == 1) {
			this.spreadSpeed = 8;
		}

		if(i2 == 2) {
			this.spreadSpeed = 2;
		}

		this.tileId = i1;
		this.calmTileId = i1 + 1;
		float f3 = 0.1F;
		this.setShape(0.0F, 0.0F - f3, 0.0F, 1.0F, 1.0F - f3, 1.0F);
		this.setTicking(true);
	}

	public void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
		this.b(level1, i2, i3, i4, 0);
	}

	private boolean b(Level level1, int i2, int i3, int i4, int i5) {
		boolean z6 = false;

		boolean z7;
		do {
			--i3;
			if(level1.getTile(i2, i3, i4) != 0) {
				break;
			}

			if(z7 = level1.setTile(i2, i3, i4, this.tileId)) {
				z6 = true;
			}
		} while(z7 && this.liquidType != 2);

		++i3;
		if(this.liquidType == 1 || !z6) {
			z6 = z6 | this.c(level1, i2 - 1, i3, i4, i5) | this.c(level1, i2 + 1, i3, i4, i5) | this.c(level1, i2, i3, i4 - 1, i5) | this.c(level1, i2, i3, i4 + 1, i5);
		}

		if(!z6) {
			level1.setTileNoUpdate(i2, i3, i4, this.calmTileId);
		}

		return z6;
	}

	private boolean c(Level level1, int i2, int i3, int i4, int i5) {
		boolean z6 = false;
		if(level1.getTile(i2, i3, i4) == 0 && level1.setTile(i2, i3, i4, this.tileId) && i5 < this.spreadSpeed) {
			z6 = false | this.b(level1, i2, i3, i4, i5 + 1);
		}

		return z6;
	}

	protected final boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
		return i2 >= 0 && i3 >= 0 && i4 >= 0 && i2 < level1.width && i4 < level1.height ? (i5 != 2 && this.liquidType == 1 ? false : ((i5 = level1.getTile(i2, i3, i4)) != this.tileId && i5 != this.calmTileId ? super.shouldRenderFace(level1, i2, i3, i4, -1, i6) : false)) : false;
	}

	public final void renderFace(Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		super.renderFace(tesselator1, i2, i3, i4, i5);
		super.renderBackFace(tesselator1, i2, i3, i4, i5);
	}

	public final boolean mayTick() {
		return false;
	}

	public final AABB getAABB(int i1, int i2, int i3) {
		return null;
	}

	public final boolean blocksLight() {
		return true;
	}

	public final boolean isSolid() {
		return false;
	}

	public final int getLiquidType() {
		return this.liquidType;
	}

	public void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
		if(this.liquidType == 1 && (i5 == Tile.lava.id || i5 == Tile.calmLava.id)) {
			level1.setTileNoUpdate(i2, i3, i4, Tile.rock.id);
		}

		if(this.liquidType == 2 && (i5 == Tile.water.id || i5 == Tile.calmWater.id)) {
			level1.setTileNoUpdate(i2, i3, i4, Tile.rock.id);
		}

	}
}
