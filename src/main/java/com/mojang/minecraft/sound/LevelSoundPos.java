package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public final class LevelSoundPos extends SoundPos {

    public LevelSoundPos(float f1, float f2, float f3, Entity entity4) {
        super(entity4);
        this.x = f1;
        this.y = f2;
        this.z = f3;
    }
}