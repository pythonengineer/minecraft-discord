package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class GrassTile extends Tile {
	protected GrassTile(int i1) {
		super(2);
		this.tex = 3;
		this.setTicking(true);
	}

	protected final int getTexture(int i1) {
		return i1 == 1 ? 0 : (i1 == 0 ? 2 : 3);
	}

	public final void tick(Level level1, int i2, int i3, int i4, EaglercraftRandom random5) {
		if(random5.nextInt(4) == 0) {
			if(!level1.isLit(i2, i3 + 1, i4)) {
				level1.setTile(i2, i3, i4, Tile.dirt.id);
			} else {
				for(int i9 = 0; i9 < 4; ++i9) {
					int i6 = i2 + random5.nextInt(3) - 1;
					int i7 = i3 + random5.nextInt(5) - 3;
					int i8 = i4 + random5.nextInt(3) - 1;
					if(level1.getTile(i6, i7, i8) == Tile.dirt.id && level1.isLit(i6, i7 + 1, i8)) {
						level1.setTile(i6, i7, i8, Tile.grass.id);
					}
				}

			}
		}
	}
}
