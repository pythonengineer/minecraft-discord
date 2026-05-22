package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemMinecart extends Item {
    public ItemMinecart(int i1) {
        super(72);
        this.maxStackSize = 1;
    }

    public final boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
        if(world3.getBlockId(i4, i5, i6) == Block.minecartTrack.blockID) {
            world3.spawnEntityInWorld(new EntityMinecart(world3, (double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F)));
            --itemStack1.stackSize;
            return true;
        } else {
            return false;
        }
    }
}