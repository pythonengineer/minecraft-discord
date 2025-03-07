package com.mojang.minecraft.player;

import com.mojang.minecraft.Options;

public final class MovementInputFromOptions extends MovementInput {
    private boolean[] keys = new boolean[10];
    private Options f;

    public MovementInputFromOptions(Options options1) {
        this.f = options1;
    }

    public final void setKey(int i1, boolean z2) {
        byte b3 = -1;
        if(i1 == this.f.forward.key) {
            b3 = 0;
        }

        if(i1 == this.f.back.key) {
            b3 = 1;
        }

        if(i1 == this.f.left.key) {
            b3 = 2;
        }

        if(i1 == this.f.right.key) {
            b3 = 3;
        }

        if(i1 == this.f.jump.key) {
            b3 = 4;
        }

        if(b3 >= 0) {
            this.keys[b3] = z2;
        }

    }

    public final void releaseAllKeys() {
        for(int i1 = 0; i1 < 10; ++i1) {
            this.keys[i1] = false;
        }

    }

    public final void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        if(this.keys[0]) {
            --this.moveForward;
        }

        if(this.keys[1]) {
            ++this.moveForward;
        }

        if(this.keys[2]) {
            --this.moveStrafe;
        }

        if(this.keys[3]) {
            ++this.moveStrafe;
        }

        this.jumpHeld = this.keys[4];
    }
}
