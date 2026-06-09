package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
        if(world3.getBlockId(xCoord, yCoord, zCoord) == Block.snow.blockID) {
            i7 = 0;
        } else {
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
        }

		if(itemStack1.stackSize == 0) {
			return false;
		} else {
            if(world3.canBlockBePlacedAt(this.blockID, xCoord, yCoord, zCoord, false)) {
			    Block block8 = Block.blocksList[this.blockID];
			    if(world3.setBlockWithNotify(xCoord, yCoord, zCoord, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world3, xCoord, yCoord, zCoord, i7);
					world3.playSoundEffect((double)((float)xCoord + 0.5F), (double)((float)yCoord + 0.5F), (double)((float)zCoord + 0.5F), block8.stepSound.getStepSound(), (block8.stepSound.getVolume() + 1.0F) / 2.0F, block8.stepSound.getPitch() * 0.8F);
					--itemStack1.stackSize;
				}
			}

			return true;
		}
	}
}
