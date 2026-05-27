package net.minecraft.game.world.path;

import net.lax1dude.eaglercraft.util.MathHelper;

public class PathPoint {
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    public final int hash;
    int index = -1;
    float totalPathDistance;
    float distanceToNext;
    float distanceToTarget;
    PathPoint previous;
    public boolean isFirst = false;

    public PathPoint(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.hash = x | y << 10 | z << 20;
    }

    public float distanceTo(PathPoint pathPoint1) {
        float f2 = (float)(pathPoint1.xCoord - this.xCoord);
        float f3 = (float)(pathPoint1.yCoord - this.yCoord);
        float f4 = (float)(pathPoint1.zCoord - this.zCoord);
        return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
    }

    public boolean equals(Object object1) {
        return ((PathPoint)object1).hash == this.hash;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean isAssigned() {
        return this.index >= 0;
    }

    public String toString() {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
}
