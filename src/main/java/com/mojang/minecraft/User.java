package com.mojang.minecraft;

import com.mojang.minecraft.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public final class User {
	public static List creativeTiles;
	public String name;
	public String sessionId;
	public String mpPass;

	public User(String name, String sessionId) {
		this.name = name;
		this.sessionId = sessionId;
	}

	static {
		(creativeTiles = new ArrayList()).add(Tile.rock);
		creativeTiles.add(Tile.stoneBrick);
        creativeTiles.add(Tile.mossStone);
		creativeTiles.add(Tile.dirt);
		creativeTiles.add(Tile.wood);
		creativeTiles.add(Tile.log);
		creativeTiles.add(Tile.leaf);
		creativeTiles.add(Tile.bush);
		creativeTiles.add(Tile.flower);
		creativeTiles.add(Tile.rose);
		creativeTiles.add(Tile.mushroom1);
		creativeTiles.add(Tile.mushroom2);
		creativeTiles.add(Tile.sand);
		creativeTiles.add(Tile.gravel);
		creativeTiles.add(Tile.glass);
		creativeTiles.add(Tile.sponge);
		creativeTiles.add(Tile.clothRed);
		creativeTiles.add(Tile.clothOrange);
		creativeTiles.add(Tile.clothYellow);
		creativeTiles.add(Tile.clothChartreuse);
		creativeTiles.add(Tile.clothGreen);
		creativeTiles.add(Tile.clothSpringGreen);
		creativeTiles.add(Tile.clothCyan);
		creativeTiles.add(Tile.clothCapri);
		creativeTiles.add(Tile.clothUltramarine);
		creativeTiles.add(Tile.clothViolet);
		creativeTiles.add(Tile.clothPurple);
		creativeTiles.add(Tile.clothMagenta);
		creativeTiles.add(Tile.clothRose);
		creativeTiles.add(Tile.clothDarkGray);
		creativeTiles.add(Tile.clothGray);
		creativeTiles.add(Tile.clothWhite);
        creativeTiles.add(Tile.coalOre);
        creativeTiles.add(Tile.ironOre);
        creativeTiles.add(Tile.goldOre);
        creativeTiles.add(Tile.iron);
        creativeTiles.add(Tile.gold);
        creativeTiles.add(Tile.slabHalf);
        creativeTiles.add(Tile.bookshelf);
        creativeTiles.add(Tile.brick);
        creativeTiles.add(Tile.tnt);
	}
}
