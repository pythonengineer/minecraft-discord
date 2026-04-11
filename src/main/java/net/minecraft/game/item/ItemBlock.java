package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

public final class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
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

		if(stack.stackSize == 0) {
			return false;
		} else {
            if(world.canBlockBePlacedAt(this.blockID, x, y, z, false)) {
			    Block block10 = Block.blocksList[this.blockID];
			    if(world.setBlockWithNotify(x, y, z, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world, x, y, z, side);
					double d10001 = (double)((float)x + 0.5F);
					double d10002 = (double)((float)y + 0.5F);
					double d10003 = (double)((float)z + 0.5F);
					String string10004 = block10.stepSound.getStepSound();
					StepSound stepSound11 = block10.stepSound;
					float f10005 = (block10.stepSound.stepSoundVolume + 1.0F) / 2.0F;
					stepSound11 = block10.stepSound;
					world.playSoundEffect(d10001, d10002, d10003, string10004, f10005, block10.stepSound.stepSoundPitch * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}
}