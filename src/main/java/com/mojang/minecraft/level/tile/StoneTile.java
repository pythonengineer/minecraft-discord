package com.mojang.minecraft.level.tile;

public final class StoneTile extends Tile {
    public StoneTile(int i1, int i2) {
        super(1, 1);
    }

    public final int getId() {
        return Tile.stoneBrick.id;
    }
}