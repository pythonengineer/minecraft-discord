package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;

public final class Timer {
	float ticksPerSecond;
	double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	public float timerSpeed = 1.0F;
	public float elapsedPartialTicks = 0.0F;
	long lastSyncSysClock;
	long lastSyncHRClock;
	double timeSyncAdjustment = 1.0D;

	public Timer(float var1) {
		this.ticksPerSecond = var1;
		this.lastSyncSysClock = EagRuntime.currentTimeMillis();
		this.lastSyncHRClock = EagRuntime.nanoTime() / 1000000L;
	}
}
