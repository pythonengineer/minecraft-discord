package net.minecraft.game.item;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityPig;

public final class ItemSaddle extends Item {
    public ItemSaddle(int i1) {
        super(73);
        this.maxStackSize = 1;
        this.maxDamage = 64;
    }

    public final void saddleEntity(ItemStack itemStack, EntityLiving entityLiving) {
        EntityPig entityPig3;
        if(entityLiving instanceof EntityPig && !(entityPig3 = (EntityPig)entityLiving).saddled) {
            entityPig3.saddled = true;
            --itemStack.stackSize;
        }

    }

    public final void hitEntity(ItemStack itemStack, EntityLiving entityLiving) {
        this.saddleEntity(itemStack, entityLiving);
    }
}