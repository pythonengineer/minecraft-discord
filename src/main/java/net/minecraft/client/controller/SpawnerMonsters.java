package net.minecraft.client.controller;

import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.world.ChunkPosition;
import net.minecraft.game.world.SpawnerAnimals;
import net.minecraft.game.world.World;

class SpawnerMonsters extends SpawnerAnimals {
    final PlayerControllerSP playerController;

    SpawnerMonsters(PlayerControllerSP playerControllerSP1, int i2, Class<? extends EntityMob> class3, Class<? extends EntityMob>[] class4) {
        super(100, class3, class4);
        this.playerController = playerControllerSP1;
    }

    protected ChunkPosition getRandomSpawningPointInChunk(World world1, int i2, int i3) {
        i2 = i2 + world1.rand.nextInt(256) - 128;
        int i4 = world1.rand.nextInt(world1.rand.nextInt(world1.rand.nextInt(112) + 8) + 8);
        int i5 = i3 + world1.rand.nextInt(256) - 128;
        return new ChunkPosition(i2, i4, i5);
    }
}
