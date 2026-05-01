package net.minecraft.game.item;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.block.Block;

public final class ItemSword extends Item {
    private int weaponDamage;

    public ItemSword(int itemID, int damage) {
        super(itemID);
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
        this.weaponDamage = 4 + (damage << 1);
    }

    public final float getStrVsBlock(Block block) {
        return 1.5F;
    }

    public final void hitEntity(ItemStack stack, EntityLiving entityLiving) {
        stack.damageItem(1);
    }

    public final void onBlockDestroyed(ItemStack stack) {
        stack.damageItem(2);
    }

    public final int getDamageVsEntity() {
        return this.weaponDamage;
    }
}