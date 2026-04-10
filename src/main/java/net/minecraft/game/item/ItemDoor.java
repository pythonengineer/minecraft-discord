package net.minecraft.game.item;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemDoor extends Item {
	public ItemDoor(int i1) {
		super(68);
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			++y;
			if(!Block.doorWood.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else {
				int i12 = MathHelper.floor_double((double)((playerEntity.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte b13 = 0;
				byte b8 = 0;
				if(i12 == 0) {
					b8 = 1;
				}

				if(i12 == 1) {
					b13 = -1;
				}

				if(i12 == 2) {
					b8 = -1;
				}

				if(i12 == 3) {
					b13 = 1;
				}

				int i9 = (world.isBlockNormalCube(x - b13, y, z - b8) ? 1 : 0) + (world.isBlockNormalCube(x - b13, y + 1, z - b8) ? 1 : 0);
				int i10 = (world.isBlockNormalCube(x + b13, y, z + b8) ? 1 : 0) + (world.isBlockNormalCube(x + b13, y + 1, z + b8) ? 1 : 0);
				boolean z11 = world.getBlockId(x - b13, y, z - b8) == Block.doorWood.blockID || world.getBlockId(x - b13, y + 1, z - b8) == Block.doorWood.blockID;
				boolean z14 = world.getBlockId(x + b13, y, z + b8) == Block.doorWood.blockID || world.getBlockId(x + b13, y + 1, z + b8) == Block.doorWood.blockID;
				boolean z15 = false;
				if(z11 && !z14) {
					z15 = true;
				} else if(i10 > i9) {
					z15 = true;
				}

				if(z15) {
					i12 = i12 - 1 & 3;
					i12 += 4;
				}

				world.setBlockWithNotify(x, y, z, Block.doorWood.blockID);
				world.setBlockMetadata(x, y, z, i12);
				world.setBlockWithNotify(x, y + 1, z, Block.doorWood.blockID);
				world.setBlockMetadata(x, y + 1, z, i12 + 8);
				--stack.stackSize;
				return true;
			}
		}
	}
}