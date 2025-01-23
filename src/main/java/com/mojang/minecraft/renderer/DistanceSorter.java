package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DistanceSorter implements Comparator<Chunk> {
	private Player player;

	public DistanceSorter(Player player1) {
		this.player = player1;
	}

	public final int compare(Chunk chunk10001, Chunk chunk4) {
		Chunk chunk3 = chunk10001;
		return chunk3.a(this.player) < chunk4.a(this.player) ? -1 : 1;
	}
}
