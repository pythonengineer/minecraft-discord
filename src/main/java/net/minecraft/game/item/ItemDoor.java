package net.minecraft.game.item;

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
            world.notifyBlockChange(x, y, z, Block.doorWood.blockID);
            world.setBlockMetadata(x, y, z, (int)((double)((playerEntity.rotationYaw + 90.0F) * 4.0F / 360.0F) - 0.5D) & 3);
            world.notifyBlockChange(x, y + 1, z, Block.doorWood.blockID);
            world.setBlockMetadata(x, y + 1, z, ((int)((double)((playerEntity.rotationYaw + 90.0F) * 4.0F / 360.0F) - 0.5D) & 3) + 8);
            --stack.stackSize;
            return true;
        }
    }
}