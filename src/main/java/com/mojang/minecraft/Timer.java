package com.mojang.minecraft;

import net.lax1dude.eaglercraft.EagRuntime;

public final class Timer {
    float ticksPerSecond;
    double lastHRTime;
    public int ticks;
    public float a;
    public float timeScale = 1.0F;
    public float fps = 0.0F;
    long lastSyncSysClock;
    long lastSyncHRClock;
    double timeSyncAdjustment = 1.0D;

    public Timer(float f1) {
        this.ticksPerSecond = f1;
        this.lastSyncSysClock = EagRuntime.currentTimeMillis();
        this.lastSyncHRClock = EagRuntime.nanoTime() / 1000000L;
    }
}
