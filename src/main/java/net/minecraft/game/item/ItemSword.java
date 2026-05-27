package net.minecraft.game.item;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.block.Block;

public class ItemSword extends Item {
    private int weaponDamage;

    public ItemSword(int itemID, int damage) {
        super(itemID);
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
        this.weaponDamage = 4 + damage * 2;
    }

    public float getStrVsBlock(ItemStack itemStack, Block block) {
        return 1.5F;
    }

    public void hitEntity(ItemStack stack, EntityLiving entityLiving) {
        stack.damageItem(1);
    }

    public void onBlockDestroyed(ItemStack stack, int i2, int i3, int i4, int i5) {
        stack.damageItem(2);
    }

    public int getDamageVsEntity(Entity entity) {
        return this.weaponDamage;
    }
}
