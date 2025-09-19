package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

public class EntitySkeleton extends EntityLiving {
    public EntitySkeleton(World var1) {
        super(var1);
        this.texture = "/mob/skeleton.png";
    }
}
