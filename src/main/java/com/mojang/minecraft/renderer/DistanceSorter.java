package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DistanceSorter implements Comparator<Chunk> {
	private Player player;

	public DistanceSorter(Player player) {
		this.player = player;
	}

	public final int compare(Chunk c01, Chunk c11) {
		return c01.compare(this.player) < c11.compare(this.player) ? -1 : 1;
	}
}