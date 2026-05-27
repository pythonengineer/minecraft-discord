package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public class RenderSorter implements Comparator {
	private EntityPlayer player;

	public RenderSorter(EntityPlayer playerEntity) {
		this.player = playerEntity;
	}

    public int compare(WorldRenderer worldRenderer1, WorldRenderer worldRenderer2) {
        boolean z3 = worldRenderer1.isInFrustum;
        boolean z4 = worldRenderer2.isInFrustum;
        return z3 && !z4 ? 1 : (z4 && !z3 ? -1 : (worldRenderer1.distanceToEntitySquared(this.player) < worldRenderer2.distanceToEntitySquared(this.player) ? 1 : -1));
    }

    public int compare(Object object1, Object object2) {
        return this.compare((WorldRenderer)object1, (WorldRenderer)object2);
    }
}
