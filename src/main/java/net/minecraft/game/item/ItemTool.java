package net.minecraft.game.item;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.block.Block;

public class ItemTool extends Item {
    private Block[] blocksEffectiveAgainst;
    private float efficiencyOnProperMaterial = 4.0F;
    private int damageVsEntity;
    protected int toolMaterial;

    public ItemTool(int itemID, int damageAgainstEntities, int damage, Block[] effectiveBlocks) {
        super(itemID);
        this.toolMaterial = damage;
        this.blocksEffectiveAgainst = effectiveBlocks;
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
        if(damage == 3) {
            this.maxDamage *= 4;
        }

        this.efficiencyOnProperMaterial = (float)((damage + 1) * 2);
        this.damageVsEntity = damageAgainstEntities + damage;
    }

    public float getStrVsBlock(ItemStack itemStack, Block block) {
        for(int i2 = 0; i2 < this.blocksEffectiveAgainst.length; ++i2) {
            if(this.blocksEffectiveAgainst[i2] == block) {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    public void hitEntity(ItemStack stack, EntityLiving entityLiving) {
        stack.damageItem(2);
    }

    public void onBlockDestroyed(ItemStack stack, int i2, int i3, int i4, int i5) {
        stack.damageItem(1);
    }

    public int getDamageVsEntity(Entity entity) {
        return this.damageVsEntity;
    }
}
