package net.minecraft.game.world;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.material.Material;

public class SpawnerAnimals {
	private int entityMax;
	private Class<? extends Entity> entityAmount;
	private Class<? extends Entity>[] entities;

	public SpawnerAnimals(int maxEntities, Class<? extends Entity> entityType, Class<? extends Entity>[] entityClasses) {
		this.entityMax = maxEntities;
		this.entityAmount = entityType;
		this.entities = entityClasses;
	}

	public final void doRandomSpawn(World world) {
		if(world.countEntities(this.entityAmount) < this.entityMax) {
			for(int i2 = 0; i2 < 10; ++i2) {
				this.performSpawning(world, world.playerEntity);
			}
		}

	}

	protected ChunkPosition getRandomSpawningPointInChunk(World world1, int i2, int i3) {
		i2 = i2 + world1.rand.nextInt(256) - 128;
		int i4 = world1.rand.nextInt(128);
		int i5 = i3 + world1.rand.nextInt(256) - 128;
		return new ChunkPosition(i2, i4, i5);
	}

	private int performSpawning(World world, Entity entity) {
		int i3 = 0;
		int i4 = MathHelper.floor_double(entity.posX);
		int i5 = MathHelper.floor_double(entity.posZ);
		int i6 = world.rand.nextInt(this.entities.length);
		ChunkPosition chunkPosition28;
		i5 = (chunkPosition28 = this.getRandomSpawningPointInChunk(world, i4, i5)).x;
		int i7 = chunkPosition28.y;
		i4 = chunkPosition28.z;
		if(world.isBlockNormalCube(i5, i7, i4)) {
			return 0;
		} else if(world.getBlockMaterial(i5, i7, i4) != Material.air) {
			return 0;
		} else {
			for(int i8 = 0; i8 < 4; ++i8) {
				int i9 = i5;
				int i10 = i7;
				int i11 = i4;

				for(int i12 = 0; i12 < 4; ++i12) {
					i9 += world.rand.nextInt(6) - world.rand.nextInt(6);
					i10 += world.rand.nextInt(1) - world.rand.nextInt(1);
					i11 += world.rand.nextInt(6) - world.rand.nextInt(6);
					if(world.isBlockNormalCube(i9, i10 - 1, i11) && !world.isBlockNormalCube(i9, i10, i11) && !world.getBlockMaterial(i9, i10, i11).getIsLiquid() && !world.isBlockNormalCube(i9, i10 + 1, i11)) {
						float f13 = (float)i9 + 0.5F;
						float f14 = (float)i10 + 1.0F;
						float f15 = (float)i11 + 0.5F;
						if(entity != null) {
							double d19 = (double)f13 - entity.posX;
							double d21 = (double)f14 - entity.posY;
							double d23 = (double)f15 - entity.posZ;
							if(d19 * d19 + d21 * d21 + d23 * d23 < 32.0D) {
								continue;
							}
						} else {
							float f29 = f13 - (float)world.spawnX;
							float f20 = f14 - (float)world.spawnY;
							float f31 = f15 - (float)world.spawnZ;
							if(f29 * f29 + f20 * f20 + f31 * f31 < 32.0F) {
								continue;
							}
						}

						EntityLiving entityLiving30;
						try {
							entityLiving30 = (EntityLiving)EntityList.createEntityByClassUnsafe(this.entities[i6], world);
						} catch (Exception ex) {
							ex.printStackTrace();
							return i3;
						}

						entityLiving30.setLocationAndAngles((double)f13, (double)f14, (double)f15, world.rand.nextFloat() * 360.0F, 0.0F);
						if(entityLiving30.getCanSpawnHere(f13, f14, f15)) {
							++i3;
							world.entityJoinedWorld(entityLiving30);
						}
					}
				}
			}

			return i3;
		}
	}
}