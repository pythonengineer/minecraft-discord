package net.minecraft.game.world;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.material.Material;

public final class SpawnerAnimals {
	private int entityMax;
	private EaglercraftRandom rand = new EaglercraftRandom();
	private Class<? extends Entity> entityType;
	private Class<? extends Entity>[] entities;

	public SpawnerAnimals(int maxEntities, Class<? extends Entity> entityType, Class<? extends Entity>[] entityClasses) {
		this.entityMax = maxEntities;
		this.entityType = entityType;
		this.entities = entityClasses;
	}

	public final void doRandomSpawn(World world) {
        if(world.countEntities(this.entityType) < this.entityMax && this.rand.nextInt(5) == 1) {
			this.performSpawning(world, 1, world.playerEntity, (IProgressUpdate)null);
		}

	}

	private int performSpawning(World world, int canSpawnHereFlag, Entity entity, IProgressUpdate progressListener) {
		canSpawnHereFlag = 0;
		int i30 = MathHelper.floor_double(entity.posX);
		int i5 = MathHelper.floor_double(entity.posZ);

		for(int i6 = 0; i6 <= 0; ++i6) {
			int i7 = world.rand.nextInt(this.entities.length);
			int i8 = i30 + world.rand.nextInt(256) - 128;
			int i9 = world.rand.nextInt(128);
			int i10 = i5 + world.rand.nextInt(256) - 128;
			if(!world.isBlockNormalCube(i8, i9, i10) && world.getBlockMaterial(i8, i9, i10) == Material.air) {
				for(int i11 = 0; i11 < 4; ++i11) {
					int i12 = i8;
					int i13 = i9;
					int i14 = i10;

					for(int i15 = 0; i15 < 4; ++i15) {
						i12 += world.rand.nextInt(6) - world.rand.nextInt(6);
						i13 += world.rand.nextInt(1) - world.rand.nextInt(1);
						i14 += world.rand.nextInt(6) - world.rand.nextInt(6);
						if(world.isBlockNormalCube(i12, i13 - 1, i14) && !world.isBlockNormalCube(i12, i13, i14) && !world.getBlockMaterial(i12, i13, i14).getIsLiquid() && !world.isBlockNormalCube(i12, i13 + 1, i14)) {
							float f16 = (float)i12 + 0.5F;
							float f17 = (float)i13 + 1.0F;
							float f18 = (float)i14 + 0.5F;
							if(entity != null) {
								double d21 = (double)f16 - entity.posX;
								double d23 = (double)f17 - entity.posY;
								double d25 = (double)f18 - entity.posZ;
								if(d21 * d21 + d23 * d23 + d25 * d25 < 256.0D) {
									continue;
								}
							} else {
								float f31 = f16 - (float)world.spawnX;
								float f22 = f17 - (float)world.spawnY;
								float f33 = f18 - (float)world.spawnZ;
								if(f31 * f31 + f22 * f22 + f33 * f33 < 256.0F) {
									continue;
								}
							}

							EntityLiving entityLiving32;
                            try {
                                entityLiving32 = (EntityLiving)EntityList.createEntityByClassUnsafe(this.entities[i7], world);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                return canSpawnHereFlag;
                            }

							entityLiving32.setLocationAndAngles((double)f16, (double)f17, (double)f18, world.rand.nextFloat() * 360.0F, 0.0F);
							if(entityLiving32.getCanSpawnHere(f16, f17, f18)) {
								++canSpawnHereFlag;
								world.entityJoinedWorld(entityLiving32);
							}
						}
					}
				}
			}
		}

		return canSpawnHereFlag;
	}
}