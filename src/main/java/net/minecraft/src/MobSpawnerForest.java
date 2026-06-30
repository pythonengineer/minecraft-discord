package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class MobSpawnerForest extends MobSpawnerBase {
	public WorldGenerator getRandomWorldGenForTrees(EaglercraftRandom var1) {
		return (WorldGenerator)(var1.nextInt(5) == 0 ? new WorldGenForest() : (var1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees()));
	}
}
