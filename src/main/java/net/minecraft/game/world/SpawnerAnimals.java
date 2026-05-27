package net.minecraft.game.world;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.material.Material;

public class SpawnerAnimals {
	private int maxSpawns;
	private Class<? extends Entity> entityType;
	private Class<? extends Entity>[] entities;

	public SpawnerAnimals(int maxEntities, Class<? extends Entity> entityType, Class<? extends Entity>[] entityClasses) {
		this.maxSpawns = maxEntities;
		this.entityType = entityType;
		this.entities = entityClasses;
	}

	public void onUpdate(World world) {
		int i2 = world.countEntities(this.entityType);
		if(i2 < this.maxSpawns) {
			for(int i3 = 0; i3 < 10; ++i3) {
				this.performSpawning(world, 1, world.playerEntity, (IProgressUpdate)null);
			}
		}

	}

	protected ChunkPosition getRandomSpawningPointInChunk(World world, int i2, int i3) {
		int i4 = i2 + world.rand.nextInt(256) - 128;
		int i5 = world.rand.nextInt(128);
		int i6 = i3 + world.rand.nextInt(256) - 128;
		return new ChunkPosition(i4, i5, i6);
	}

	private int performSpawning(World world, int i2, Entity entity, IProgressUpdate loadingScreen) {
		int i3 = 0;
		int i4 = MathHelper.floor_double(entity.posX);
		int i5 = MathHelper.floor_double(entity.posZ);
		int i6 = world.rand.nextInt(this.entities.length);
		ChunkPosition chunkPosition7 = this.getRandomSpawningPointInChunk(world, i4, i5);
		int i8 = chunkPosition7.x;
		int i9 = chunkPosition7.y;
		int i10 = chunkPosition7.z;
		if(world.isBlockNormalCube(i8, i9, i10)) {
			return 0;
		} else if(world.getBlockMaterial(i8, i9, i10) != Material.air) {
			return 0;
		} else {
			for(int i11 = 0; i11 < 3; ++i11) {
				int i12 = i8;
				int i13 = i9;
				int i14 = i10;
				byte b15 = 6;

				for(int i16 = 0; i16 < 3; ++i16) {
					i12 += world.rand.nextInt(b15) - world.rand.nextInt(b15);
					i13 += world.rand.nextInt(1) - world.rand.nextInt(1);
					i14 += world.rand.nextInt(b15) - world.rand.nextInt(b15);
					if(world.isBlockNormalCube(i12, i13 - 1, i14) && !world.isBlockNormalCube(i12, i13, i14) && !world.getBlockMaterial(i12, i13, i14).getIsLiquid() && !world.isBlockNormalCube(i12, i13 + 1, i14)) {
						float f17 = (float)i12 + 0.5F;
						float f18 = (float)i13 + 1.0F;
						float f19 = (float)i14 + 0.5F;
						if(entity != null) {
							double d20 = (double)f17 - entity.posX;
							double d22 = (double)f18 - entity.posY;
							double d24 = (double)f19 - entity.posZ;
							double d26 = d20 * d20 + d22 * d22 + d24 * d24;
							if(d26 < 1024.0D) {
								continue;
							}
						} else {
							float f27 = f17 - (float)world.spawnX;
							float f21 = f18 - (float)world.spawnY;
							float f28 = f19 - (float)world.spawnZ;
							float f23 = f27 * f27 + f21 * f21 + f28 * f28;
							if(f23 < 1024.0F) {
								continue;
							}
						}

						EntityLiving entityLiving29;
						try {
							entityLiving29 = (EntityLiving)EntityList.createEntityByClassUnsafe(this.entities[i6], world);
						} catch (Exception ex) {
							ex.printStackTrace();
							return i3;
						}

						entityLiving29.setPositionAndRotation((double)f17, (double)f18, (double)f19, world.rand.nextFloat() * 360.0F, 0.0F);
						if(entityLiving29.getCanSpawnHere((double)f17, (double)f18, (double)f19)) {
							++i3;
							world.spawnEntityInWorld(entityLiving29);
						}
					}
				}
			}

			return i3;
		}
	}
}
