package com.mojang.minecraft;

import net.lax1dude.eaglercraft.EagRuntime;

public final class Timer {
    float ticksPerSecond;
    long lastTime;
    public int ticks;
    public float a;
    public float timeScale = 1.0F;
    public float fps = 0.0F;

    public Timer(float f1) {
        this.ticksPerSecond = f1;
        this.lastTime = EagRuntime.nanoTime();
    }
}
