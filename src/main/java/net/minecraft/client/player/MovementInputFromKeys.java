package net.minecraft.client.player;

import net.minecraft.client.GameSettings;

public final class MovementInputFromKeys extends MovementInput {
    private boolean[] keys = new boolean[10];
    private GameSettings options;

    public MovementInputFromKeys(GameSettings var1) {
        this.options = var1;
    }

    public final void checkKeyForMovementInput(int var1, boolean var2) {
        byte var3 = -1;
        if(var1 == this.options.keyBindForward.keyCode) {
            var3 = 0;
        }

        if(var1 == this.options.keyBindBack.keyCode) {
            var3 = 1;
        }

        if(var1 == this.options.keyBindLeft.keyCode) {
            var3 = 2;
        }

        if(var1 == this.options.keyBindRight.keyCode) {
            var3 = 3;
        }

        if(var1 == this.options.keyBindJump.keyCode) {
            var3 = 4;
        }

        if(var3 >= 0) {
            this.keys[var3] = var2;
        }

    }

    public final void resetKeyState() {
        for(int var1 = 0; var1 < 10; ++var1) {
            this.keys[var1] = false;
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

        this.jump = this.keys[4];
    }
}
