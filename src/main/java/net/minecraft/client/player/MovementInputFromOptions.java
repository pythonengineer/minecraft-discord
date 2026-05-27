package net.minecraft.client.player;

import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.player.EntityPlayer;

public class MovementInputFromOptions extends MovementInput {
    private boolean[] movementKeyStates = new boolean[10];
    private GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public void checkKeyForMovementInput(int keyState, boolean isMovementInput) {
        byte b3 = -1;
        if(keyState == this.gameSettings.keyBindForward.keyCode) {
            b3 = 0;
        }

        if(keyState == this.gameSettings.keyBindBack.keyCode) {
            b3 = 1;
        }

        if(keyState == this.gameSettings.keyBindLeft.keyCode) {
            b3 = 2;
        }

        if(keyState == this.gameSettings.keyBindRight.keyCode) {
            b3 = 3;
        }

        if(keyState == this.gameSettings.keyBindJump.keyCode) {
            b3 = 4;
        }

        if(b3 >= 0) {
            this.movementKeyStates[b3] = isMovementInput;
        }

    }

    public void resetKeyState() {
        for(int i1 = 0; i1 < 10; ++i1) {
            this.movementKeyStates[i1] = false;
        }

    }

    public void updatePlayerMoveState(EntityPlayer player) {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        if(this.movementKeyStates[0]) {
            ++this.moveForward;
        }

        if(this.movementKeyStates[1]) {
            --this.moveForward;
        }

        if(this.movementKeyStates[2]) {
            ++this.moveStrafe;
        }

        if(this.movementKeyStates[3]) {
            --this.moveStrafe;
        }

        this.jump = this.movementKeyStates[4];
    }
}
