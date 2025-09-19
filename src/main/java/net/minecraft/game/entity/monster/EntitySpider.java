package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

public class EntitySpider extends EntityLiving {
    public EntitySpider(World var1) {
        super(var1);
        this.texture = "/mob/spider.png";
        this.setSize(1.4F, 0.9F);
    }
}
