package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;

public final class Player extends Entity {
    boolean[] keys = new boolean[10];

    public Player(Level level1) {
        super(level1);
        this.heightOffset = 1.62F;
    }

    public final void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float f1 = 0.0F;
        float f2 = 0.0F;
        boolean z3 = this.isInWater();
        boolean z4 = this.isInLava();
        if(this.keys[0]) {
            f2 = 0.0F - 1.0F;
        }

        if(this.keys[1]) {
            ++f2;
        }

        if(this.keys[2]) {
            f1 = 0.0F - 1.0F;
        }

        if(this.keys[3]) {
            ++f1;
        }

        if(this.keys[4]) {
            if(z3) {
                this.yd += 0.04F;
            } else if(z4) {
                this.yd += 0.04F;
            } else if(this.onGround) {
                this.yd = 0.42F;
                this.keys[4] = false;
            }
        }

        float f5;
        if(z3) {
            f5 = this.y;
            this.moveRelative(f1, f2, 0.02F);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.8F;
            this.yd *= 0.8F;
            this.zd *= 0.8F;
            this.yd = (float)((double)this.yd - 0.02D);
            if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f5, this.zd)) {
                this.yd = 0.3F;
            }

        } else if(z4) {
            f5 = this.y;
            this.moveRelative(f1, f2, 0.02F);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.5F;
            this.yd *= 0.5F;
            this.zd *= 0.5F;
            this.yd = (float)((double)this.yd - 0.02D);
            if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + f5, this.zd)) {
                this.yd = 0.3F;
            }

        } else {
            this.moveRelative(f1, f2, this.onGround ? 0.1F : 0.02F);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.91F;
            this.yd *= 0.98F;
            this.zd *= 0.91F;
            this.yd = (float)((double)this.yd - 0.08D);
            if(this.onGround) {
                this.xd *= 0.6F;
                this.zd *= 0.6F;
            }

        }
    }
}
