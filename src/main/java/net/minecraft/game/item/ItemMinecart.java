package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemMinecart extends Item {
    public ItemMinecart(int id) {
        super(id);
        this.maxStackSize = 1;
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        int i8 = world.getBlockId(x, y, z);
        if(i8 == Block.minecartTrack.blockID) {
            world.spawnEntityInWorld(new EntityMinecart(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F)));
            --itemStack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
