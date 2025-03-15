package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DirtyChunkSorter implements Comparator<Chunk> {
	private Player player;

	public DirtyChunkSorter(Player player) {
		this.player = player;
	}

    public final int compare(Chunk c01, Chunk c11) {
        boolean z3 = c01.isInFrustum;
        boolean z4 = c11.isInFrustum;
        return z3 && !z4 ? 1 : ((!z4 || z3) && c01.compare(this.player) < c11.compare(this.player) ? 1 : -1);
    }
}