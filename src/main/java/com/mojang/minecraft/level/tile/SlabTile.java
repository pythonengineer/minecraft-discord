package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class SlabTile extends Tile {
    private boolean half;

    public SlabTile(int id, boolean half) {
        super(id, 6);
        this.half = half;
        if(!half) {
            this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

    }

    protected final int getTexture(int face) {
        return face <= 1 ? 6 : 5;
    }

    public final boolean isSolid() {
        return this.half;
    }

    public final void neighborChanged(Level level, int x, int y, int z, int type) {
        if(this == Tile.slabHalf) {
            int i6;
            if((i6 = level.getTile(x, y + 1, z)) > 0) {
                level.setTile(x, y, z, Tile.slabFull.id);
                if(i6 == Tile.slabHalf.id) {
                    level.setTile(x, y + 1, z, 0);
                }
            }

            if(level.getTile(x, y - 1, z) == slabHalf.id) {
                level.setTile(x, y, z, 0);
                level.setTile(x, y - 1, z, Tile.slabFull.id);
            }

        }
    }

    public final int getId() {
        return Tile.slabHalf.id;
    }

    public final boolean isOpaque() {
        return this.half;
    }
}