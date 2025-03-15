package com.mojang.minecraft.net;

public final class PlayerPos {
    public float x;
    public float y;
    public float z;
    public float yRot;
    public float xRot;
    public boolean rotating = false;
    public boolean moving = false;

    public PlayerPos(float x, float y, float z, float yr, float xr) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yRot = yr;
        this.xRot = xr;
        this.rotating = true;
        this.moving = true;
    }

    public PlayerPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.moving = true;
        this.rotating = false;
    }

    public PlayerPos(float xr, float yr) {
        this.yRot = xr;
        this.xRot = yr;
        this.rotating = true;
        this.moving = false;
    }
}