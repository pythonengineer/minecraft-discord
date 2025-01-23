package com.mojang.minecraft.level.tile;

public final class LeafTile extends Tile {
    protected LeafTile(int i1, int i2) {
        super(18, 22);
    }

    public final boolean isSolid() {
        return false;
    }

    public final boolean blocksLight() {
        return false;
    }
}
