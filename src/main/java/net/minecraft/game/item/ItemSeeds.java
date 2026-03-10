package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int itemID, int blockType) {
		super(39);
		this.blockType = blockType;
	}

	public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else if(world.getBlockId(x, y, z) == Block.farmland.blockID) {
			world.notifyBlockChange(x, y + 1, z, this.blockType);
			--stack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}