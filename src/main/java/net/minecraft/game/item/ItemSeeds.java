package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int itemID, int blockType) {
		super(itemID);
		this.blockType = blockType;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			int i8 = world3.getBlockId(xCoord, yCoord, zCoord);
			if(i8 == Block.tilledField.blockID) {
				world3.setBlockWithNotify(xCoord, yCoord + 1, zCoord, this.blockType);
				--itemStack1.stackSize;
				return true;
			} else {
				return false;
			}
		}
	}
}
