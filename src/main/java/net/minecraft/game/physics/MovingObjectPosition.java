package net.minecraft.game.physics;

import net.minecraft.game.entity.Entity;

public final class MovingObjectPosition {
	public int typeOfHit;
	public int blockX;
	public int blockY;
	public int blockZ;
	public int sideHit;
	public Vec3D hitVec;
	public Entity entityHit;

    public MovingObjectPosition(int x, int y, int z, int side, Vec3D hitVector) {
        this.typeOfHit = 0;
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
        this.sideHit = side;
        this.hitVec = Vec3D.createVector(hitVector.xCoord, hitVector.yCoord, hitVector.zCoord);
    }

    public MovingObjectPosition(Entity entity) {
        this.typeOfHit = 1;
        this.entityHit = entity;
        this.hitVec = Vec3D.createVector(entity.posX, entity.posY, entity.posZ);
    }
}
