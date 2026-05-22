package net.minecraft.game.world.path;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.Vec3D;

public final class PathEntity {
	private final PathPoint[] points;
	private int pathIndex;

	public PathEntity(PathPoint[] points) {
		this.points = points;
	}

	public final void incrementPathIndex() {
		++this.pathIndex;
	}

	public final boolean isFinished() {
		return this.pathIndex >= this.points.length;
	}

	public final Vec3D getPosition(Entity entity) {
		float f2 = (float)this.points[this.pathIndex].xCoord + (float)((int)(entity.width + 1.0F)) * 0.5F;
		float f3 = (float)this.points[this.pathIndex].yCoord;
		float entity1 = (float)this.points[this.pathIndex].zCoord + (float)((int)(entity.width + 1.0F)) * 0.5F;
		return Vec3D.createVector((double)f2, (double)f3, (double)entity1);
	}
}