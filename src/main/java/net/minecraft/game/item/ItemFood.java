package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public final class ItemFood extends Item {
    private int healAmount = 4;

    public ItemFood(int var1, int var2) {
        super(260);
    }

    public final boolean onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        --var1.stackSize;
        int var4 = this.healAmount;
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
