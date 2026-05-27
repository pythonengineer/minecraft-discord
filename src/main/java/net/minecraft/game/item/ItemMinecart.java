package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class ItemMinecart extends Item {
    public ItemMinecart(int i1) {
        super(i1);
        this.maxStackSize = 1;
    }

    public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int xCoord, int yCoord, int zCoord, int i7) {
        int i8 = world3.getBlockId(xCoord, yCoord, zCoord);
        if(i8 == Block.minecartTrack.blockID) {
            world3.spawnEntityInWorld(new EntityMinecart(world3, (double)((float)xCoord + 0.5F), (double)((float)yCoord + 0.5F), (double)((float)zCoord + 0.5F)));
            --itemStack1.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
