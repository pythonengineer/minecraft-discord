package com.mojang.minecraft.phys;

public final class AABB {
    private float epsilon = 0.0F;
    public float x0;
    public float y0;
    public float z0;
    public float x1;
    public float y1;
    public float z1;

    public AABB(float f1, float f2, float f3, float f4, float f5, float f6) {
        this.x0 = f1;
        this.y0 = f2;
        this.z0 = f3;
        this.x1 = f4;
        this.y1 = f5;
        this.z1 = f6;
    }

    public final AABB grow(float f1, float f2, float f3) {
        float f4 = this.x0 - f1;
        float f5 = this.y0 - f2;
        float f6 = this.z0 - f3;
        f1 += this.x1;
        f2 += this.y1;
        float f7 = this.z1 + f3;
        return new AABB(f4, f5, f6, f1, f2, f7);
    }

    public final boolean intersects(AABB aABB1) {
        return aABB1.x1 > this.x0 && aABB1.x0 < this.x1 ? (aABB1.y1 > this.y0 && aABB1.y0 < this.y1 ? aABB1.z1 > this.z0 && aABB1.z0 < this.z1 : false) : false;
    }

    public final void move(float f1, float f2, float f3) {
        this.x0 += f1;
        this.y0 += f2;
        this.z0 += f3;
        this.x1 += f1;
        this.y1 += f2;
        this.z1 += f3;
    }
}
