package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;

public abstract class WorldGenerator {
	public abstract boolean generate(World world1, EaglercraftRandom random2, int i3, int i4, int i5);
}