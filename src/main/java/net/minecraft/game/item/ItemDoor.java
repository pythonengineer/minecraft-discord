package net.minecraft.game.item;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class ItemDoor extends Item {
	private Material material;

	public ItemDoor(int i1, Material material2) {
		super(i1);
		this.material = material2;
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world, int x, int y, int z, int i7) {
		if(i7 != 1) {
			return false;
		} else {
			++y;
			Block block8;
			if(this.material == Material.wood) {
				block8 = Block.doorWood;
			} else {
				block8 = Block.doorSteel;
			}

			if(!block8.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else {
				int i9 = MathHelper.floor_double((double)((entityPlayer2.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte b10 = 0;
				byte b11 = 0;
				if(i9 == 0) {
					b11 = 1;
				}

				if(i9 == 1) {
					b10 = -1;
				}

				if(i9 == 2) {
					b11 = -1;
				}

				if(i9 == 3) {
					b10 = 1;
				}

				int i12 = (world.isBlockNormalCube(x - b10, y, z - b11) ? 1 : 0) + (world.isBlockNormalCube(x - b10, y + 1, z - b11) ? 1 : 0);
				int i13 = (world.isBlockNormalCube(x + b10, y, z + b11) ? 1 : 0) + (world.isBlockNormalCube(x + b10, y + 1, z + b11) ? 1 : 0);
				boolean z14 = world.getBlockId(x - b10, y, z - b11) == block8.blockID || world.getBlockId(x - b10, y + 1, z - b11) == block8.blockID;
				boolean z15 = world.getBlockId(x + b10, y, z + b11) == block8.blockID || world.getBlockId(x + b10, y + 1, z + b11) == block8.blockID;
				boolean z16 = false;
				if(z14 && !z15) {
					z16 = true;
				} else if(i13 > i12) {
					z16 = true;
				}

				if(z16) {
					i9 = i9 - 1 & 3;
					i9 += 4;
				}

				world.setBlockWithNotify(x, y, z, block8.blockID);
				world.setBlockMetadataWithNotify(x, y, z, i9);
				world.setBlockWithNotify(x, y + 1, z, block8.blockID);
				world.setBlockMetadataWithNotify(x, y + 1, z, i9 + 8);
				--itemStack1.stackSize;
				return true;
			}
		}
	}
}
