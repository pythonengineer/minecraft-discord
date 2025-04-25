package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;

public final class Timer {
    float ticksPerSecond = 20.0F;
    private double d;
    public int elapsedTicks;
    public float renderPartialTicks;
    private float delta = 1.0F;
    private float f = 0.0F;
    private long g = EagRuntime.currentTimeMillis();
    private long h = EagRuntime.nanoTime() / 1000000L;
    private double i = 1.0D;

    public Timer(float var1) {
    }

    public final void updateTimer() {
        long var1 = EagRuntime.currentTimeMillis();
        long var3 = var1 - this.g;
        long var5 = EagRuntime.nanoTime() / 1000000L;
        double var9;
        if(var3 > 1000L) {
            long var7 = var5 - this.h;
            var9 = (double)var3 / (double)var7;
            this.i += (var9 - this.i) * (double)0.2F;
            this.g = var1;
            this.h = var5;
        }

        if(var3 < 0L) {
            this.g = var1;
            this.h = var5;
        }

        double var11 = (double)var5 / 1000.0D;
        var9 = (var11 - this.d) * this.i;
        this.d = var11;
        if(var9 < 0.0D) {
            var9 = 0.0D;
        }

        if(var9 > 1.0D) {
            var9 = 1.0D;
        }

        this.f = (float)((double)this.f + var9 * (double)this.delta * (double)this.ticksPerSecond);
        this.elapsedTicks = (int)this.f;
        if(this.elapsedTicks > 100) {
            this.elapsedTicks = 100;
        }

        this.f -= (float)this.elapsedTicks;
        this.renderPartialTicks = this.f;
    }
}
