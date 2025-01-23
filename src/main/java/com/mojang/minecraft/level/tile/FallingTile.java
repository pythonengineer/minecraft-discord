package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class FallingTile extends Tile {
    public FallingTile(int i1, int i2) {
        super(i1, i2);
    }

    public final void onBlockAdded(Level level1, int i2, int i3, int i4) {
        tryToFall(level1, i2, i3, i4);
    }

    public final void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
        tryToFall(level1, i2, i3, i4);
    }

    private static void tryToFall(Level level0, int i1, int i2, int i3) {
        int i4 = i1;
        int i5 = i2;

        int i6;
        for(i6 = i3; level0.getTile(i4, i5 - 1, i6) == 0 && i5 > 0; --i5) {
        }

        if(i5 != i2) {
            level0.swap(i1, i2, i3, i4, i5, i6);
        }

    }
}
