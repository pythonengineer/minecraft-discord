package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DirtyChunkSorter implements Comparator<Chunk> {
	private Player player;

	public DirtyChunkSorter(Player player1) {
		this.player = player1;
	}

	public final int compare(Chunk chunk10001, Chunk chunk6) {
		Chunk chunk5 = chunk10001;
		boolean z3 = chunk5.isInFrustum;
		boolean z4 = chunk6.isInFrustum;
		return z3 && !z4 ? 1 : ((!z4 || z3) && chunk5.a(this.player) < chunk6.a(this.player) ? 1 : -1);
	}
}
