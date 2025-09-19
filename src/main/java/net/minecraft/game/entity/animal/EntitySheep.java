package net.minecraft.game.entity.animal;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

public class EntitySheep extends EntityLiving {
    public EntitySheep(World var1) {
        super(var1);
        this.texture = "/mob/sheep.png";
        this.setSize(0.9F, 1.3F);
    }
}
