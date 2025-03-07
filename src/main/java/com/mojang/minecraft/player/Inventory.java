package com.mojang.minecraft.player;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;

public final class Inventory {
    public int[] slots = new int[9];
    public int selectedSlot = 0;

    public Inventory() {
        for(int i1 = 0; i1 < 9; ++i1) {
            this.slots[i1] = ((Tile)User.creativeTiles.get(i1)).id;
        }

    }

    public final int getSelected() {
        return this.slots[this.selectedSlot];
    }

    public int containsTileAt(int i1) {
        for(int i2 = 0; i2 < this.slots.length; ++i2) {
            if(i1 == this.slots[i2]) {
                return i2;
            }
        }

        return -1;
    }

    public final void scrollHotbar(int i1) {
        if(i1 > 0) {
            i1 = 1;
        }

        if(i1 < 0) {
            i1 = -1;
        }

        for(this.selectedSlot -= i1; this.selectedSlot < 0; this.selectedSlot += this.slots.length) {
        }

        while(this.selectedSlot >= this.slots.length) {
            this.selectedSlot -= this.slots.length;
        }

    }

    public final void setTile(Tile tile1) {
        if(tile1 != null) {
            int i2;
            if((i2 = this.containsTileAt(tile1.id)) >= 0) {
                this.slots[i2] = this.slots[this.selectedSlot];
            }

            this.slots[this.selectedSlot] = tile1.id;
        }

    }
}
