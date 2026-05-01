package net.minecraft.game.item;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.block.Block;

public class ItemTool extends Item {
    private Block[] blocksEffectiveAgainst;
    private float efficiencyOnProperMaterial = 4.0F;
    private int damageVsEntity;

    public ItemTool(int itemID, int damageAgainstEntities, int damage, Block[] effectiveBlocks) {
        super(itemID);
        this.blocksEffectiveAgainst = effectiveBlocks;
        this.maxStackSize = 1;
        this.maxDamage = 32 << damage;
        if(damage == 3) {
            this.maxDamage <<= 1;
        }

        this.efficiencyOnProperMaterial = (float)(damage + 1 << 1);
        this.damageVsEntity = damageAgainstEntities + damage;
    }

    public final float getStrVsBlock(Block block) {
        for(int i2 = 0; i2 < this.blocksEffectiveAgainst.length; ++i2) {
            if(this.blocksEffectiveAgainst[i2] == block) {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    public final void hitEntity(ItemStack stack, EntityLiving entityLiving) {
        stack.damageItem(2);
    }

    public final void onBlockDestroyed(ItemStack stack) {
        stack.damageItem(1);
    }

    public final int getDamageVsEntity() {
        return this.damageVsEntity;
    }
}