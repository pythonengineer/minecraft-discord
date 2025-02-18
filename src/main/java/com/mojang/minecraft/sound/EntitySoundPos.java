package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public final class EntitySoundPos extends SoundPos {
    private Entity source;

    public EntitySoundPos(Entity entity1, Entity entity2) {
        super(entity2);
        this.source = entity1;
    }

    public final float getRotationDiff() {
        return super.getRotationDiff(this.source.x, this.source.z);
    }

    public final float getDistanceSq() {
        return super.getDistanceSq(this.source.x, this.source.y, this.source.z);
    }
}