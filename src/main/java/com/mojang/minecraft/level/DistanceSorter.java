package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;

import java.util.Comparator;

public final class DistanceSorter implements Comparator {
	private Player player;

	public DistanceSorter(Player player1) {
		this.player = player1;
	}

	public final int compare(Object object1, Object object2) {
		Chunk chunk10001 = (Chunk)object1;
		Chunk chunk4 = (Chunk)object2;
		Chunk chunk3 = chunk10001;
		return chunk3.compare(this.player) < chunk4.compare(this.player) ? -1 : 1;
	}
}
