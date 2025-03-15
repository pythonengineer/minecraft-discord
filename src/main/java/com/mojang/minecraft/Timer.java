package com.mojang.minecraft;

import net.lax1dude.eaglercraft.EagRuntime;

public final class Timer {
    float ticksPerSecond;
    double lastTime;
    public int frames;
    public float alpha;
    public float fps = 1.0F;
    public float ticks = 0.0F;
    long msPerTick;
    long passedTime;
    double averageFrameTime = 1.0D;

    public Timer(float ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.msPerTick = EagRuntime.currentTimeMillis();
        this.passedTime = EagRuntime.nanoTime() / 1000000L;
    }
}