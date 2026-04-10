package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int i1) {
		super(3);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
		if(side == 0) {
			--y;
		}

		if(side == 1) {
			++y;
		}

		if(side == 2) {
			--z;
		}

		if(side == 3) {
			++z;
		}

		if(side == 4) {
			--x;
		}

		if(side == 5) {
			++x;
		}

		if(world.getBlockId(x, y, z) == 0) {
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			world.setBlockWithNotify(x, y, z, Block.fire.blockID);
		}

		stack.damageItem(1);
		return true;
	}
}