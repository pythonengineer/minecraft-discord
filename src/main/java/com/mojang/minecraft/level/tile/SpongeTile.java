package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class SpongeTile extends Tile {
    protected SpongeTile(int i1) {
        super(19);
        this.tex = 48;
    }

    public final void onTileAdded(Level level1, int i2, int i3, int i4) {
        for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
            for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
                for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
                    if(level1.isWater(i7, i5, i6)) {
                        level1.setTileNoNeighborChange(i7, i5, i6, 0);
                    }
                }
            }
        }

    }

    public final void onTileRemoved(Level level1, int i2, int i3, int i4) {
        for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
            for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
                for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
                    level1.updateNeighborsAt(i7, i5, i6, level1.getTile(i7, i5, i6));
                }
            }
        }

    }
}
