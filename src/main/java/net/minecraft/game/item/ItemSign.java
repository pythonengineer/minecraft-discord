package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public final class ItemSign extends Item {
    public ItemSign(int i1) {
        super(67);
        this.maxDamage = 64;
        this.maxStackSize = 1;
    }

    public final boolean onItemUse(ItemStack stack, EntityPlayer playerEntity, World world, int x, int y, int z, int side) {
        if(side != 1) {
            return false;
        } else {
            ++y;
            world.notifyBlockChange(x, y, z, Block.signStanding.blockID);
            world.setBlockMetadata(x, y, z, (int)((double)((playerEntity.rotationYaw + 180.0F) * 16.0F / 360.0F) - 0.5D) & 15);
            --stack.stackSize;
            playerEntity.displayGUIEditSign((TileEntitySign)world.getBlockTileEntity(x, y, z));
            return true;
        }
    }
}