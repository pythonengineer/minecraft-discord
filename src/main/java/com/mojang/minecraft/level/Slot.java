package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;

final class Slot {
    int xSlot;
    int ySlot;
    int zSlot;
    private BlockMap blockInMapArray;

    public Slot(BlockMap blockMap) {
        this.blockInMapArray = blockMap;
    }

    public final Slot init(float x, float y, float z) {
        this.xSlot = (int)(x / 16.0F);
        this.ySlot = (int)(y / 16.0F);
        this.zSlot = (int)(z / 16.0F);
        if(this.xSlot < 0) {
            this.xSlot = 0;
        }

        if(this.ySlot < 0) {
            this.ySlot = 0;
        }

        if(this.zSlot < 0) {
            this.zSlot = 0;
        }

        BlockMap x1 = this.blockInMapArray;
        if(this.xSlot >= this.blockInMapArray.width) {
            x1 = this.blockInMapArray;
            this.xSlot = this.blockInMapArray.width - 1;
        }

        x1 = this.blockInMapArray;
        if(this.ySlot >= this.blockInMapArray.depth) {
            x1 = this.blockInMapArray;
            this.ySlot = this.blockInMapArray.depth - 1;
        }

        x1 = this.blockInMapArray;
        if(this.zSlot >= this.blockInMapArray.height) {
            x1 = this.blockInMapArray;
            this.zSlot = this.blockInMapArray.height - 1;
        }

        return this;
    }

    public final void add(Entity entity) {
        if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
            BlockMap blockMap2 = this.blockInMapArray;
            int i10001 = this.zSlot * this.blockInMapArray.depth + this.ySlot;
            blockMap2 = this.blockInMapArray;
            this.blockInMapArray.entityGrid[i10001 * this.blockInMapArray.width + this.xSlot].add(entity);
        }

    }

    public final void remove(Entity entity) {
        if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
            BlockMap blockMap2 = this.blockInMapArray;
            int i10001 = this.zSlot * this.blockInMapArray.depth + this.ySlot;
            blockMap2 = this.blockInMapArray;
            this.blockInMapArray.entityGrid[i10001 * this.blockInMapArray.width + this.xSlot].remove(entity);
        }

    }
}