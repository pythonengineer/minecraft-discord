package net.minecraft.game.item;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntityPig;

public class ItemSaddle extends Item {
    public ItemSaddle(int i1) {
        super(i1);
        this.maxStackSize = 1;
        this.maxDamage = 64;
    }

    public void saddleEntity(ItemStack itemStack, EntityLiving entityLiving) {
        if(entityLiving instanceof EntityPig) {
            EntityPig entityPig3 = (EntityPig)entityLiving;
            if(!entityPig3.saddled) {
                entityPig3.saddled = true;
                --itemStack.stackSize;
            }
        }

    }

    public void hitEntity(ItemStack itemStack, EntityLiving entityLiving) {
        this.saddleEntity(itemStack, entityLiving);
    }
}
