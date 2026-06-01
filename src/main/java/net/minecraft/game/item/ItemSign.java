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

    public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int x, int y, int z, int i7) {
        if(i7 == 0) {
            return false;
        } else if(!world3.getBlockMaterial(x, y, z).isSolid()) {
            return false;
        } else {
            if(i7 == 1) {
                ++y;
            }

            if(i7 == 2) {
                --z;
            }

            if(i7 == 3) {
                ++z;
            }

            if(i7 == 4) {
                --x;
            }

            if(i7 == 5) {
                ++x;
            }

            if(!Block.signStanding.canPlaceBlockAt(world3, x, y, z)) {
                return false;
            } else {
                if(i7 == 1) {
                    world3.setBlockAndMetadataWithNotify(x, y, z, Block.signStanding.blockID, MathHelper.floor_double((double)((entityPlayer2.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
                } else {
                    world3.setBlockAndMetadataWithNotify(x, y, z, Block.signWall.blockID, i7);
                }

                --itemStack1.stackSize;
                entityPlayer2.displayGUIEditSign((TileEntitySign)world3.getBlockTileEntity(x, y, z));
                return true;
            }
        }
    }
}
