package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.Entity;

public class EntitySorter implements Comparator {
	private Entity comparedEntity;

	public EntitySorter(Entity entity) {
		this.comparedEntity = entity;
	}

    public int compare(WorldRenderer worldRenderer1, WorldRenderer worldRenderer2) {
        return worldRenderer1.distanceToEntitySquared(this.comparedEntity) < worldRenderer2.distanceToEntitySquared(this.comparedEntity) ? -1 : 1;
    }

    public int compare(Object object1, Object object2) {
        return this.compare((WorldRenderer)object1, (WorldRenderer)object2);
    }
}
