package com.mojang.minecraft.level;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public final class LevelGen {
    int width;
    int height;
    int depth;
    EaglercraftRandom random = new EaglercraftRandom();

    public LevelGen(int var1, int var2, int var3) {
        this.width = var1;
        this.height = var2;
        this.depth = var3;
    }
}
