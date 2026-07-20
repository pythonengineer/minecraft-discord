package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest() {
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
	}

	public WorldGenerator getRandomWorldGenForTrees(EaglercraftRandom var1) {
		return (WorldGenerator)(var1.nextInt(5) == 0 ? new WorldGenForest() : (var1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees()));
	}
}
