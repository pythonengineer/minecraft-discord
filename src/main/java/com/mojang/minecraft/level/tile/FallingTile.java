package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;

public final class FallingTile extends Tile {
    public FallingTile(int i1, int i2) {
        super(i1, i2);
    }

    public final void onBlockAdded(Level level1, int i2, int i3, int i4) {
        this.tryToFall(level1, i2, i3, i4);
    }

    public final void neighborChanged(Level level1, int i2, int i3, int i4, int i5) {
        this.tryToFall(level1, i2, i3, i4);
    }

    private void tryToFall(Level level1, int i2, int i3, int i4) {
        int i11 = i2;
        int i5 = i3;
        int i6 = i4;

        while(true) {
            int i9 = i5 - 1;
            int i7;
            Liquid liquid12;
            if(!((i7 = level1.getTile(i11, i9, i6)) == 0 ? true : ((liquid12 = Tile.tiles[i7].getLiquidType()) == Liquid.water ? true : liquid12 == Liquid.lava)) || i5 <= 0) {
                if(i5 != i3) {
                    level1.swap(i2, i3, i4, i11, i5, i6);
                }

                return;
            }

            --i5;
        }
    }
}
