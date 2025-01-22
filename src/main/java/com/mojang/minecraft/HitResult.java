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

    float distanceTo(Player player1, int i2) {
        int i3 = this.x;
        int i4 = this.y;
        int i5 = this.z;
        if(i2 == 1) {
            if(this.f == 0) {
                --i4;
            }

            if(this.f == 1) {
                ++i4;
            }

            if(this.f == 2) {
                --i5;
            }

            if(this.f == 3) {
                ++i5;
            }

            if(this.f == 4) {
                --i3;
            }

            if(this.f == 5) {
                ++i3;
            }
        }

        float f6 = (float)i3 - player1.x;
        float f8 = (float)i4 - player1.y;
        float f7 = (float)i5 - player1.z;
        return f6 * f6 + f8 * f8 + f7 * f7;
    }
}
