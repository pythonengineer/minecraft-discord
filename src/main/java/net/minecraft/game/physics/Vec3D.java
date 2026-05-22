package net.minecraft.game.physics;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.util.MathHelper;

public final class Vec3D {
    private static List vectorList = new ArrayList();
    private static int nextVector = 0;
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public static Vec3D createVectorHelper(double x, double y, double z) {
        return new Vec3D(x, y, z);
    }

    public static void initialize() {
        nextVector = 0;
    }

    public static Vec3D createVector(double x, double y, double z) {
        if(nextVector >= vectorList.size()) {
            vectorList.add(createVectorHelper(0.0D, 0.0D, 0.0D));
        }

        Vec3D vec3D10000 = (Vec3D)vectorList.get(nextVector++);
        double d7 = x;
        Vec3D vec3D13 = vec3D10000;
        vec3D10000.xCoord = d7;
        vec3D13.yCoord = y;
        vec3D13.zCoord = z;
        return vec3D13;
    }

    private Vec3D(double x, double y, double z) {
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
        return createVector(vector.xCoord - this.xCoord, vector.yCoord - this.yCoord, vector.zCoord - this.zCoord);
    }

    public final Vec3D normalize() {
        double d1;
        return (d1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord)) < 1.0E-4D ? createVector(0.0D, 0.0D, 0.0D) : createVector(this.xCoord / d1, this.yCoord / d1, this.zCoord / d1);
    }

    public final Vec3D crossProduct(Vec3D vec3d) {
        return createVector(this.yCoord * vec3d.zCoord - this.zCoord * vec3d.yCoord, this.zCoord * vec3d.xCoord - this.xCoord * vec3d.zCoord, this.xCoord * vec3d.yCoord - this.yCoord * vec3d.xCoord);
    }

    public final Vec3D addVector(double x, double y, double z) {
        return createVector(this.xCoord + x, this.yCoord + y, this.zCoord + z);
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

    public final double squareDistanceTo(double x, double y, double z) {
        double d7 = x - this.xCoord;
        double d9 = y - this.yCoord;
        double d11 = z - this.zCoord;
        return d7 * d7 + d9 * d9 + d11 * d11;
    }

    public final double lengthVector() {
        return (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public final Vec3D getIntermediateWithXValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8 = vector.zCoord - this.zCoord;
        double d10;
        return d4 * d4 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.xCoord) / d4) >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final Vec3D getIntermediateWithYValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8 = vector.zCoord - this.zCoord;
        double d10;
        return d6 * d6 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.yCoord) / d6) >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final Vec3D getIntermediateWithZValue(Vec3D vector, double intermediateValue) {
        double d4 = vector.xCoord - this.xCoord;
        double d6 = vector.yCoord - this.yCoord;
        double d8;
        double d10;
        return (d8 = vector.zCoord - this.zCoord) * d8 < 1.0000000116860974E-7D ? null : ((d10 = (intermediateValue - this.zCoord) / d8) >= 0.0D && d10 <= 1.0D ? createVector(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
    }

    public final String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }
}