package net.minecraft.game.item;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemDoor extends Item {
	public ItemDoor(int i1) {
		super(i1);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			++yCoord;
			if(!Block.doorWood.canPlaceBlockAt(world3, xCoord, yCoord, zCoord)) {
				return false;
			} else {
				int i8 = MathHelper.floor_double((double)((entityPlayer2.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte b9 = 0;
				byte b10 = 0;
				if(i8 == 0) {
					b10 = 1;
				}

				if(i8 == 1) {
					b9 = -1;
				}

				if(i8 == 2) {
					b10 = -1;
				}

				if(i8 == 3) {
					b9 = 1;
				}

				int i11 = (world3.isBlockNormalCube(xCoord - b9, yCoord, zCoord - b10) ? 1 : 0) + (world3.isBlockNormalCube(xCoord - b9, yCoord + 1, zCoord - b10) ? 1 : 0);
				int i12 = (world3.isBlockNormalCube(xCoord + b9, yCoord, zCoord + b10) ? 1 : 0) + (world3.isBlockNormalCube(xCoord + b9, yCoord + 1, zCoord + b10) ? 1 : 0);
				boolean z13 = world3.getBlockId(xCoord - b9, yCoord, zCoord - b10) == Block.doorWood.blockID || world3.getBlockId(xCoord - b9, yCoord + 1, zCoord - b10) == Block.doorWood.blockID;
				boolean z14 = world3.getBlockId(xCoord + b9, yCoord, zCoord + b10) == Block.doorWood.blockID || world3.getBlockId(xCoord + b9, yCoord + 1, zCoord + b10) == Block.doorWood.blockID;
				boolean z15 = false;
				if(z13 && !z14) {
					z15 = true;
				} else if(i12 > i11) {
					z15 = true;
				}

				if(z15) {
					i8 = i8 - 1 & 3;
					i8 += 4;
				}

				world3.setBlockWithNotify(xCoord, yCoord, zCoord, Block.doorWood.blockID);
				world3.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i8);
				world3.setBlockWithNotify(xCoord, yCoord + 1, zCoord, Block.doorWood.blockID);
				world3.setBlockMetadataWithNotify(xCoord, yCoord + 1, zCoord, i8 + 8);
				--itemStack1.stackSize;
				return true;
			}
		}
	}
}
