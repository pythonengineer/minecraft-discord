package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class LiquidTile extends Tile {
	protected int liquidType;
	protected int calmTileId;
	protected int tileId;

	protected LiquidTile(int i1, int i2) {
		super(i1);
		this.liquidType = i2;
		this.tex = 14;
		if(i2 == 2) {
			this.tex = 30;
		}

		this.tileId = i1;
		this.calmTileId = i1 + 1;
		float f3 = 0.1F;
		this.setShape(0.0F, 0.0F - f3, 0.0F, 1.0F, 1.0F - f3, 1.0F);
		this.setTicking(true);
	}

	public final void onBlockAdded(Level level1, int i2, int i3, int i4) {
		level1.addToTickNextTick(i2, i3, i4, this.tileId);
	}

	public void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
		boolean z7 = false;
		i4 = i4;
		i3 = i3;
		i2 = i2;
		level1 = level1;
		LiquidTile liquidTile8 = this;
		boolean z9 = false;

		boolean z6;
		do {
			--i3;
			if(level1.getTile(i2, i3, i4) != 0) {
				break;
			}

			if(z6 = level1.setTile(i2, i3, i4, liquidTile8.tileId)) {
				z9 = true;
			}
		} while(z6 && liquidTile8.liquidType != 2);

		++i3;
		if(liquidTile8.liquidType == 1 || !z9) {
			z9 = z9 | liquidTile8.checkWater(level1, i2 - 1, i3, i4) | liquidTile8.checkWater(level1, i2 + 1, i3, i4) | liquidTile8.checkWater(level1, i2, i3, i4 - 1) | liquidTile8.checkWater(level1, i2, i3, i4 + 1);
		}

		if(!z9) {
			level1.setTileNoUpdate(i2, i3, i4, liquidTile8.calmTileId);
		} else {
			level1.addToTickNextTick(i2, i3, i4, liquidTile8.tileId);
		}

	}

	private boolean checkWater(Level level1, int i2, int i3, int i4) {
		if(level1.getTile(i2, i3, i4) == 0 && level1.setTile(i2, i3, i4, this.tileId)) {
			level1.addToTickNextTick(i2, i3, i4, this.tileId);
		}

		return false;
	}

	protected final boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
		return i2 >= 0 && i3 >= 0 && i4 >= 0 && i2 < level1.width && i4 < level1.height ? (i5 != 1 && this.liquidType == 1 ? false : ((i5 = level1.getTile(i2, i3, i4)) != this.tileId && i5 != this.calmTileId ? super.shouldRenderFace(level1, i2, i3, i4, -1, i6) : false)) : false;
	}

	public final void renderFace(Tesselator tesselator1, int i2, int i3, int i4, int i5) {
		super.renderFace(tesselator1, i2, i3, i4, i5);
		super.renderBackFace(tesselator1, i2, i3, i4, i5);
	}

	public final boolean mayPick() {
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

		level1.addToTickNextTick(i2, i3, i4, i5);
	}
}
