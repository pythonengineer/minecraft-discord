package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class CalmLiquidTile extends LiquidTile {
	protected CalmLiquidTile(int i1, Liquid liquid2) {
		super(i1, liquid2);
		this.tileId = i1 - 1;
		this.calmTileId = i1;
		this.setTicking(false);
	}

	public final void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
	}

	public final void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
		boolean z6 = false;
		if(level1.getTile(i2 - 1, i3, i4) == 0) {
			z6 = true;
		}

		if(level1.getTile(i2 + 1, i3, i4) == 0) {
			z6 = true;
		}

		if(level1.getTile(i2, i3, i4 - 1) == 0) {
			z6 = true;
		}

		if(level1.getTile(i2, i3, i4 + 1) == 0) {
			z6 = true;
		}

		if(level1.getTile(i2, i3 - 1, i4) == 0) {
			z6 = true;
		}

		if(i5 != 0) {
			Liquid liquid7 = Tile.tiles[i5].getLiquidType();
			if(this.liquid == Liquid.water && liquid7 == Liquid.lava || liquid7 == Liquid.water && this.liquid == Liquid.lava) {
				level1.setTile(i2, i3, i4, Tile.rock.id);
				return;
			}
		}

		if(z6) {
			level1.setTileNoUpdate(i2, i3, i4, this.tileId);
			level1.addToTickNextTick(i2, i3, i4, this.tileId);
		}

	}
}
