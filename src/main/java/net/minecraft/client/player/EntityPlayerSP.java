package net.minecraft.client.player;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public class EntityPlayerSP extends EntityPlayer {
    public MovementInput movementInput;

    public EntityPlayerSP(World var1) {
        super(var1);
        this.entityAI = new EntityPlayerInput(this);
    }

    public final void onLivingUpdate() {
        this.movementInput.updatePlayerMoveState();
        super.onLivingUpdate();
    }
}
