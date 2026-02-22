package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;

public abstract class WorldGenerator {
    public abstract boolean generate(World var1, EaglercraftRandom var2, int var3, int var4, int var5);

    public void setScale(double var1, double var3, double var5) {
    }
}
