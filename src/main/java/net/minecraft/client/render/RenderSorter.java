package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public class RenderSorter implements Comparator {
	private EntityPlayer baseEntity;

	public RenderSorter(EntityPlayer playerEntity) {
		this.baseEntity = playerEntity;
	}

    public int doCompare(WorldRenderer worldRenderer1, WorldRenderer worldRenderer2) {
        boolean z3 = worldRenderer1.isInFrustum;
        boolean z4 = worldRenderer2.isInFrustum;
        return z3 && !z4 ? 1 : (z4 && !z3 ? -1 : (worldRenderer1.distanceToEntitySquared(this.baseEntity) < worldRenderer2.distanceToEntitySquared(this.baseEntity) ? 1 : -1));
    }

    public int compare(Object object1, Object object2) {
        return this.doCompare((WorldRenderer)object1, (WorldRenderer)object2);
    }
}
