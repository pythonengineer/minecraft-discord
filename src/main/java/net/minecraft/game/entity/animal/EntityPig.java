package net.minecraft.game.entity.animal;

import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityPig extends EntityAnimal {
    private EntityPig(World var1) {
        super(var1);
    }

    protected final String getLivingSound() {
        return "mob.pig";
    }

    protected final String getHurtSound() {
        return "mob.pig";
    }

    protected final String getDeathSound() {
        return "mob.pigdeath";
    }

    protected final int getDropItemId() {
        return Item.porkRaw.shiftedIndex;
    }
}
