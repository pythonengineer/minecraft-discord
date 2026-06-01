package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemRedstone extends Item {
    public ItemRedstone(int i1) {
        super(i1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
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

        if(world.getBlockId(x, y, z) != 0) {
            return false;
        } else {
            if(Block.redstoneWire.canPlaceBlockAt(world, x, y, z)) {
                --itemStack.stackSize;
                world.setBlockWithNotify(x, y, z, Block.redstoneWire.blockID);
            }

            return true;
        }
    }
}
