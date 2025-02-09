package com.mojang.minecraft.character;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final Vec3 subtract(Vec3 vec31) {
        return new Vec3(this.x - vec31.x, this.y - vec31.y, this.z - vec31.z);
    }

    public final Vec3 normalize() {
        float f1 = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
        return new Vec3(this.x / f1, this.y / f1, this.z / f1);
    }

    public Vec3 interpolateTo(Vec3 t, float p) {
        float xt = this.x + (t.x - this.x) * p;
        float yt = this.y + (t.y - this.y) * p;
        float zt = this.z + (t.z - this.z) * p;
        return new Vec3(xt, yt, zt);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
