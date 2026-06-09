package net.minecraft.game.entity.player;

import net.minecraft.game.world.World;

public class EntityOtherPlayerMP extends EntityPlayer {
    public EntityOtherPlayerMP(World world1, String string2) {
        super(world1);
        this.username = string2;
        this.yOffset = 1.62F;
        if(string2 != null && string2.length() > 0) {
            this.skinUrl = string2;
            System.out.println("Loading texture " + this.skinUrl);
        }

        this.noClip = true;
    }

    public void onUpdate() {
        super.onUpdate();
    }

    public void onLivingUpdate() {
    }
}
