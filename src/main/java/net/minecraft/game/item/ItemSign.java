package net.minecraft.game.item;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public class ItemSign extends Item {
	public ItemSign(int i1) {
		super(i1);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			++yCoord;
			if(!Block.signStanding.canPlaceBlockAt(world3, xCoord, yCoord, zCoord)) {
				return false;
			} else {
				world3.setBlockWithNotify(xCoord, yCoord, zCoord, Block.signStanding.blockID);
				world3.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, MathHelper.floor_double((double)((entityPlayer2.rotationYaw + 180.0F) * 16.0F / 360.0F) - 0.5D) & 15);
				--itemStack1.stackSize;
				entityPlayer2.displayGUIEditSign((TileEntitySign)world3.getBlockTileEntity(xCoord, yCoord, zCoord));
				return true;
			}
		}
	}
}
