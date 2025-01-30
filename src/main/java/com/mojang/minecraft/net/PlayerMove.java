package com.mojang.minecraft.net;

public final class PlayerMove {
    public float x;
    public float y;
    public float z;
    public float yRot;
    public float xRot;
    public boolean rotating = false;
    public boolean moving = false;

    public PlayerMove(float f1, float f2, float f3, float f4, float f5) {
        this.x = f1;
        this.y = f2;
        this.z = f3;
        this.yRot = f4;
        this.xRot = f5;
        this.rotating = true;
        this.moving = true;
    }

    public PlayerMove(float f1, float f2, float f3) {
        this.x = f1;
        this.y = f2;
        this.z = f3;
        this.moving = true;
        this.rotating = false;
    }

    public PlayerMove(float f1, float f2) {
        this.yRot = f1;
        this.xRot = f2;
        this.rotating = true;
        this.moving = false;
    }
}
