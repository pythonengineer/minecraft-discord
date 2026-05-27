package net.minecraft.game.world.path;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.Vec3D;

public class PathEntity {
	private final PathPoint[] points;
	public final int pathLength;
	private int pathIndex;

	public PathEntity(PathPoint[] points) {
		this.points = points;
		this.pathLength = points.length;
	}

	public void incrementPathIndex() {
		++this.pathIndex;
	}

	public boolean isFinished() {
		return this.pathIndex >= this.points.length;
	}

	public Vec3D getPosition(Entity entity1) {
		float f2 = (float)this.points[this.pathIndex].xCoord + (float)((int)(entity1.width + 1.0F)) * 0.5F;
		float f3 = (float)this.points[this.pathIndex].yCoord;
		float f4 = (float)this.points[this.pathIndex].zCoord + (float)((int)(entity1.width + 1.0F)) * 0.5F;
		return Vec3D.createVector((double)f2, (double)f3, (double)f4);
	}
}
