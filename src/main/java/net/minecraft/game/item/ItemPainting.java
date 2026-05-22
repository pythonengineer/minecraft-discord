package net.minecraft.game.item;

import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;

public final class ItemPainting extends Item {
	public ItemPainting(int i1) {
		super(65);
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
		if(side == 0) {
			return false;
		} else if(side == 1) {
			return false;
		} else {
			byte b8 = 0;
			if(side == 4) {
				b8 = 1;
			}

			if(side == 3) {
				b8 = 2;
			}

			if(side == 5) {
				b8 = 3;
			}

			EntityPainting entityPainting9;
			if((entityPainting9 = new EntityPainting(world, x, y, z, b8)).onValidSurface()) {
				world.spawnEntityInWorld(entityPainting9);
				--stack.stackSize;
			}

			return true;
		}
	}
}