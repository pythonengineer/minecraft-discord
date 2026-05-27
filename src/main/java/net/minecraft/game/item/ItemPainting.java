package net.minecraft.game.item;

import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;

public class ItemPainting extends Item {
	public ItemPainting(int i1) {
		super(i1);
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
		if(i7 == 0) {
			return false;
		} else if(i7 == 1) {
			return false;
		} else {
			byte b8 = 0;
			if(i7 == 4) {
				b8 = 1;
			}

			if(i7 == 3) {
				b8 = 2;
			}

			if(i7 == 5) {
				b8 = 3;
			}

			EntityPainting entityPainting9 = new EntityPainting(world3, xCoord, yCoord, zCoord, b8);
			if(entityPainting9.onValidSurface()) {
				world3.spawnEntityInWorld(entityPainting9);
				--itemStack1.stackSize;
			}

			return true;
		}
	}
}
