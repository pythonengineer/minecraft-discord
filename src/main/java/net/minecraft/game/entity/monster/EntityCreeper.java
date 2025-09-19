package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

public class EntityCreeper extends EntityLiving {
    public EntityCreeper(World var1) {
        super(var1);
        this.texture = "/mob/creeper.png";
    }
}
