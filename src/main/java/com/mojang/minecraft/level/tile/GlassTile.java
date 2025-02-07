package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class GlassTile extends Tile {
    private boolean renderAdjacentFaces = false;

    protected GlassTile(int i1, int i2, boolean z3) {
        super(20, 49);
    }

    public final boolean isSolid() {
        return false;
    }

    protected final boolean shouldRenderFace(Level level1, int i2, int i3, int i4, int i5, int i6) {
        int i7 = level1.getTile(i2, i3, i4);
        return !this.renderAdjacentFaces && i7 == this.id ? false : super.shouldRenderFace(level1, i2, i3, i4, i5, i6);
    }

    public final boolean blocksLight() {
        return false;
    }
}
