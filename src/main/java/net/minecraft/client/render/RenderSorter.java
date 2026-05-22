package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public final class RenderSorter implements Comparator {
	private EntityPlayer player;

	public RenderSorter(EntityPlayer playerEntity) {
		this.player = playerEntity;
	}

	public final int compare(Object worldRenderer1, Object worldRenderer2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)worldRenderer1;
		WorldRenderer worldRenderer3 = (WorldRenderer)worldRenderer2;
		WorldRenderer worldRenderer21 = worldRenderer10001;
		boolean z4 = worldRenderer21.isInFrustum;
		boolean z5 = worldRenderer3.isInFrustum;
		return z4 && !z5 ? 1 : ((!z5 || z4) && worldRenderer21.distanceToEntitySquared(this.player) < worldRenderer3.distanceToEntitySquared(this.player) ? 1 : -1);
	}
}