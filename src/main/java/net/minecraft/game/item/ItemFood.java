package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;

public final class ItemFood extends Item {
    private int healAmount = 4;

    public ItemFood(int var1, int var2) {
        super(260);
    }

    public final boolean onPlaced(ItemStack var1, EntityPlayer var2) {
        --var1.stackSize;
        EntityPlayer var10000 = var2;
        int var4 = this.healAmount;
        EntityPlayer var3 = var10000;
        if(var3.health > 0) {
            var3.health += var4;
            if(var3.health > 20) {
                var3.health = 20;
            }

            var3.heartsLife = var3.heartsHalvesLife / 2;
        }

        return true;
    }

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return true;
    }
}
