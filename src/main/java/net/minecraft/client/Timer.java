package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;

public class Timer {
    float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    private float timerSpeed = 1.0F;
    private float elapsedPartialTicks = 0.0F;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private double timeSyncAdjustment = 1.0D;

    public Timer(float tps) {
        this.ticksPerSecond = tps;
        this.lastSyncSysClock = EagRuntime.currentTimeMillis();
        this.lastSyncHRClock = EagRuntime.nanoTime() / 1000000L;
    }

    public void updateTimer() {
        long j1;
        long j3 = (j1 = EagRuntime.currentTimeMillis()) - this.lastSyncSysClock;
        long j5 = EagRuntime.nanoTime() / 1000000L;
        double d9;
        if(j3 > 1000L) {
            long j7 = j5 - this.lastSyncHRClock;
            d9 = (double)j3 / (double)j7;
            this.timeSyncAdjustment += (d9 - this.timeSyncAdjustment) * (double)0.2F;
            this.lastSyncSysClock = j1;
            this.lastSyncHRClock = j5;
        }

        if(j3 < 0L) {
            this.lastSyncSysClock = j1;
            this.lastSyncHRClock = j5;
        }

        double d11;
        d9 = ((d11 = (double)j5 / 1000.0D) - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = d11;
        if(d9 < 0.0D) {
            d9 = 0.0D;
        }

        if(d9 > 1.0D) {
            d9 = 1.0D;
        }

        this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + d9 * (double)this.timerSpeed * (double)this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float)this.elapsedTicks;
        if(this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }

        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
