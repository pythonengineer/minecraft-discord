package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class CalmLiquidTile extends LiquidTile {
	protected CalmLiquidTile(int i1, int i2) {
		super(i1, i2);
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

        if(this.liquidType == 1 && i5 == Tile.lava.id) {
            level1.setTileNoUpdate(i2, i3, i4, Tile.rock.id);
        } else if(this.liquidType == 2 && i5 == Tile.water.id) {
            level1.setTileNoUpdate(i2, i3, i4, Tile.rock.id);
        } else {
            if(z6) {
                level1.setTileNoUpdate(i2, i3, i4, this.tileId);
                level1.addToTickNextTick(i2, i3, i4, this.tileId);
            }

        }
	}
}
