package net.minecraft.client.player;

import net.minecraft.game.entity.player.EntityPlayer;

public class MovementInput {
	public float moveStrafe = 0.0F;
	public float moveForward = 0.0F;
    public boolean unused = false;
	public boolean jump = false;

	public void updatePlayerMoveState(EntityPlayer player) {
	}

    public void resetKeyState() {
    }

    public void checkKeyForMovementInput(int keyState, boolean isMovementInput) {
    }
}
