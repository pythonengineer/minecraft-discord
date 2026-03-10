package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.Entity;

public final class EntitySorter implements Comparator {
	private Entity entity;

	public EntitySorter(Entity entity) {
		this.entity = entity;
	}

	public final int compare(Object entity1, Object entity2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)entity1;
		WorldRenderer worldRenderer3 = (WorldRenderer)entity2;
		WorldRenderer entity21 = worldRenderer10001;
		return entity21.distanceToEntitySquared(this.entity) < worldRenderer3.distanceToEntitySquared(this.entity) ? -1 : 1;
	}
}