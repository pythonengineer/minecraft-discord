package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int i1) {
		super(i1);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
		if(i7 == 0) {
			--yCoord;
		}

		if(i7 == 1) {
			++yCoord;
		}

		if(i7 == 2) {
			--zCoord;
		}

		if(i7 == 3) {
			++zCoord;
		}

		if(i7 == 4) {
			--xCoord;
		}

		if(i7 == 5) {
			++xCoord;
		}

		int i8 = world3.getBlockId(xCoord, yCoord, zCoord);
		if(i8 == 0) {
			world3.playSoundEffect((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			world3.setBlockWithNotify(xCoord, yCoord, zCoord, Block.fire.blockID);
		}

		itemStack1.damageItem(1);
		return true;
	}
}
