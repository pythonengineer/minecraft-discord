package com.mojang.minecraft;

public final class HitResult {
    public int x;
    public int y;
    public int z;
    public int f;

    public HitResult(int i1, int i2, int i3, int i4, int i5) {
        this.x = i2;
        this.y = i3;
        this.z = i4;
        this.f = i5;
    }
}
