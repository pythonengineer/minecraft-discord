package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class MobSpawnerRainforest extends MobSpawnerBase {
	public WorldGenerator getRandomWorldGenForTrees(EaglercraftRandom var1) {
		return (WorldGenerator)(var1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
	}
}
