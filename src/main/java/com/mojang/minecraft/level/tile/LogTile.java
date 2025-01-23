package com.mojang.minecraft.level.tile;

public final class LogTile extends Tile {
    protected LogTile(int i1) {
        super(17);
        this.tex = 20;
    }

    protected final int getTexture(int i1) {
        return i1 == 1 ? 21 : (i1 == 0 ? 21 : 20);
    }
}
