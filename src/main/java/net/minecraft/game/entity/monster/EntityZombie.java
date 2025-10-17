package net.minecraft.game.entity.monster;

import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityZombie extends EntityMob {
    public EntityZombie(World var1) {
        super(var1);
        this.texture = "/mob/zombie.png";
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
    }

	public final void updatePlayerActionState() {
        this.getBrightness(1.0F);
		super.updatePlayerActionState();
	}

	protected final int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
}
