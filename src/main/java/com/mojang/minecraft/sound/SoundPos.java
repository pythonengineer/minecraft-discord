package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public class SoundPos {
    public float x;
    public float y;
    public float z;

    public SoundPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SoundPos(Entity listener) {
        this(listener.x, listener.y, listener.z);
    }
}