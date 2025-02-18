package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public abstract class SoundPos {
    private Entity listener;
    public float x;
    public float y;
    public float z;

    public SoundPos(Entity entity1) {
        this.listener = entity1;
        this.x = this.listener.x;
        this.y = this.listener.y;
        this.z = this.listener.z;
    }

    public final float getRotationDiff(float f1, float f2) {
        f1 -= this.listener.x;
        f2 -= this.listener.z;
        float f3 = (float)Math.sqrt((double)(f1 * f1 + f2 * f2));
        f1 /= f3;
        f2 /= f3;
        if((f3 /= 2.0F) > 1.0F) {
            f3 = 1.0F;
        }

        float f4 = (float)Math.cos((double)(-this.listener.yRot) * Math.PI / 180.0D + Math.PI);
        return ((float)Math.sin((double)(-this.listener.yRot) * Math.PI / 180.0D + Math.PI) * f2 - f4 * f1) * f3;
    }

    public final float getDistanceSq(float f1, float f2, float f3) {
        f1 -= this.listener.x;
        f2 -= this.listener.y;
        float f4 = f3 - this.listener.z;
        f4 = (float)Math.sqrt((double)(f1 * f1 + f2 * f2 + f4 * f4));
        if((f4 = 1.0F - f4 / 32.0F) < 0.0F) {
            f4 = 0.0F;
        }

        return f4;
    }

    public float getRotationDiff() {
        return this.getRotationDiff(this.x, this.z);
    }

    public float getDistanceSq() {
        return this.getDistanceSq(this.x, this.y, this.z);
    }
}