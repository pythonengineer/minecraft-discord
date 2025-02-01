package com.mojang.minecraft;

import com.mojang.minecraft.level.tile.Tile;

public final class User {
    public static final int[] creativeTiles = new int[]{Tile.rock.id, Tile.dirt.id, Tile.stoneBrick.id, Tile.wood.id, Tile.bush.id, Tile.log.id, Tile.leaf.id, Tile.sand.id, Tile.gravel.id};
    public String name;
    public String sessionId;
    public String mpPass;

    public User(String string1, String string2) {
        this.name = string1;
        this.sessionId = string2;
    }
}
