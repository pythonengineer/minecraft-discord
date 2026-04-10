package net.minecraft.game.physics;

import net.lax1dude.eaglercraft.util.MathHelper;

public final class Vec3D {
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public Vec3D(double x, double y, double z) {
        if(x == -0.0D) {
            x = 0.0D;
        }

        if(y == -0.0D) {
            y = 0.0D;
        }

        if(z == -0.0D) {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public final Vec3D subtract(Vec3D vector) {
        return new Vec3D(vector.xCoord - this.xCoord, vector.yCoord - this.yCoord, vector.zCoord - this.zCoord);
    }

    public final Vec3D normalize() {
        double d1;
        return (d1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord)) < 1.0E-4D ? new Vec3D(0.0D, 0.0D, 0.0D) : new Vec3D(this.xCoord / d1, this.yCoord / d1, this.zCoord / d1);
    }

    public final Vec3D addVector(double x, double y, double z) {
        return new Vec3D(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public final double distanceTo(Vec3D vector) {
        double d2 = vector.xCoord - this.xCoord;
        double d4 = vector.yCoord - this.yCoord;
        double d6 = vector.zCoord - this.zCoord;
        return (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4 + d6 * d6);
    }

    public final double squaredDistanceTo(Vec3D vector) {
        double d2 = vector.xCoord - this.xCoord;
        double d4 = vector.yCoord - this.yCoord;
        double d6 = vector.zCoord - this.zCoord;
        return d2 * d2 + d4 * d4 + d6 * d6;
    }

    public final Vec3D getIntermediateWithXValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8 = vector.zCoord - this.zCoord;
        double d10;
        return d4 * d4 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.xCoord) / d4) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final Vec3D getIntermediateWithYValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8 = vector.zCoord - this.zCoord;
        double d10;
        return d6 * d6 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.yCoord) / d6) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final Vec3D getIntermediateWithZValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8;
        double d10;
        return (d8 = vector.zCoord - this.zCoord) * d8 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.zCoord) / d8) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }
}