package net.minecraft.game.entity.monster;

import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityZombie extends EntityMob {
	private EntityZombie(World var1) {
		super(var1);
	}

	public final void updatePlayerActionState() {
		super.updatePlayerActionState();
	}

	protected final int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
}
