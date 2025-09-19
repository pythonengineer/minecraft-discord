package net.minecraft.game.entity.animal;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

public class EntityPig extends EntityLiving {
    public EntityPig(World var1) {
        super(var1);
        this.texture = "/mob/pig.png";
        this.setSize(0.9F, 0.9F);
    }
}
