package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class LiquidTile extends Tile {
	protected Liquid liquid;
	protected int calmTileId;
	protected int tileId;

	protected LiquidTile(int i1, Liquid liquid2) {
		super(i1);
		this.liquid = liquid2;
		this.tex = 14;
		if(liquid2 == Liquid.lava) {
			this.tex = 30;
		}

		this.tileId = i1;
		this.calmTileId = i1 + 1;
		float f4 = 0.01F;
		float f3 = 0.1F;
		this.setShape(0.0F - f4, 0.0F - f3 - f4, 0.0F - f4, f4 + 1.0F, 1.0F - f3 + f4, f4 + 1.0F);
		this.setTicking(true);
		if(liquid2 == Liquid.lava) {
			this.setTickSpeed(16);
		}

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
			if(level1.getTile(i2, i3, i4) != 0 || !liquidTile8.checkSponge(level1, i2, i3, i4)) {
				break;
			}

			if(z6 = level1.setTile(i2, i3, i4, liquidTile8.tileId)) {
				z9 = true;
			}
		} while(z6 && liquidTile8.liquid != Liquid.lava);

		++i3;
		if(liquidTile8.liquid == Liquid.water || !z9) {
			z9 = z9 | liquidTile8.checkWater(level1, i2 - 1, i3, i4) | liquidTile8.checkWater(level1, i2 + 1, i3, i4) | liquidTile8.checkWater(level1, i2, i3, i4 - 1) | liquidTile8.checkWater(level1, i2, i3, i4 + 1);
		}

		if(!z9) {
			level1.setTileNoUpdate(i2, i3, i4, liquidTile8.calmTileId);
		} else {
			level1.addToTickNextTick(i2, i3, i4, liquidTile8.tileId);
		}

	}

	private boolean checkSponge(Level level1, int i2, int i3, int i4) {
		if(this.liquid == Liquid.water) {
			for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
				for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
					for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
						if(level1.getTile(i7, i5, i6) == Tile.sponge.id) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean checkWater(Level level1, int i2, int i3, int i4) {
		if(level1.getTile(i2, i3, i4) == 0) {
			if(!this.checkSponge(level1, i2, i3, i4)) {
				return false;
			}

			if(level1.setTile(i2, i3, i4, this.tileId)) {
				level1.addToTickNextTick(i2, i3, i4, this.tileId);
			}
		}

		return false;
	}

	protected final float getBrightness(Level level1, int i2, int i3, int i4) {
		return this.liquid == Liquid.lava ? 100.0F : level1.getBrightness(i2, i3, i4);
	}

	protected final boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
		return i2 >= 0 && i3 >= 0 && i4 >= 0 && i2 < level1.width && i4 < level1.height ? (i5 != 1 && this.liquid == Liquid.water ? false : ((i5 = level1.getTile(i2, i3, i4)) != this.tileId && i5 != this.calmTileId ? (i6 != 1 || level1.getTile(i2 - 1, i3, i4) != 0 && level1.getTile(i2 + 1, i3, i4) != 0 && level1.getTile(i2, i3, i4 - 1) != 0 && level1.getTile(i2, i3, i4 + 1) != 0 ? super.shouldRenderFace(level1, i2, i3, i4, -1, i6) : true) : false)) : false;
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

	public final Liquid getLiquidType() {
		return this.liquid;
	}

	public void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
		if(i5 != 0) {
			Liquid liquid6 = Tile.tiles[i5].getLiquidType();
			if(this.liquid == Liquid.water && liquid6 == Liquid.lava || liquid6 == Liquid.water && this.liquid == Liquid.lava) {
				level1.setTile(i2, i3, i4, Tile.rock.id);
				return;
			}
		}

		level1.addToTickNextTick(i2, i3, i4, i5);
	}

	public final int getTickDelay() {
		return this.liquid == Liquid.lava ? 5 : 0;
	}
}
