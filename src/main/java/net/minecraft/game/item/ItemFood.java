package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;

public class ItemFood extends Item {
    private int healAmount;

    public ItemFood(int itemID, int healAmount) {
        super(itemID);
        this.healAmount = healAmount;
        this.maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
        --itemStack1.stackSize;
        entityPlayer3.heal(this.healAmount);
        return itemStack1;
    }
}
