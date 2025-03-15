package com.mojang.minecraft.character;

public final class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final Vec3 subtract(Vec3 t) {
        return new Vec3(this.x - t.x, this.y - t.y, this.z - t.z);
    }

    public final Vec3 normalize() {
        float f1 = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
        return new Vec3(this.x / f1, this.y / f1, this.z / f1);
    }

    public final Vec3 addVector(float x, float y, float z) {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }
}