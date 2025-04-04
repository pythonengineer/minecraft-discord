package com.mojang.minecraft.level.tile;

public final class OreTile extends Tile {
    public OreTile(int i1, int i2) {
        super(i1, i2);
    }

    public final int resourceCount() {
        return random.nextInt(3) + 1;
    }
}