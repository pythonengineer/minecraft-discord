package net.minecraft.game.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.chunk.ChunkCoordIntPair;
import net.minecraft.game.world.material.Material;

public class SpawnerAnimals {
	private int maxSpawns;
	private Class<? extends Entity> entityType;
	private Class<? extends Entity>[] entities;
    private Set eligibleChunksForSpawning = new HashSet();

	public SpawnerAnimals(int maxEntities, Class<? extends Entity> entityType, Class<? extends Entity>[] entityClasses) {
		this.maxSpawns = maxEntities;
		this.entityType = entityType;
		this.entities = entityClasses;
	}

	public void onUpdate(World world) {
		int i2 = world.countEntities(this.entityType);
		if(i2 < this.maxSpawns) {
			for(int i3 = 0; i3 < 3; ++i3) {
				this.performSpawning(world, 1, (IProgressUpdate)null);
			}
		}

	}

	protected ChunkPosition getRandomSpawningPointInChunk(World world, int i2, int i3) {
		int i4 = i2 + world.rand.nextInt(16);
		int i5 = world.rand.nextInt(128);
		int i6 = i3 + world.rand.nextInt(16);
		return new ChunkPosition(i4, i5, i6);
	}

    private int performSpawning(World world, int i2, IProgressUpdate loadingScreen) {
        this.eligibleChunksForSpawning.clear();

        int i4;
        int i7;
        int i9;
        int i10;
        for(i4 = 0; i4 < world.playerEntities.size(); ++i4) {
            EntityPlayer entityPlayer5 = (EntityPlayer)world.playerEntities.get(i4);
            int i6 = MathHelper.floor_double(entityPlayer5.posX / 16.0D);
            i7 = MathHelper.floor_double(entityPlayer5.posZ / 16.0D);
            byte b8 = 4;

            for(i9 = -b8; i9 <= b8; ++i9) {
                for(i10 = -b8; i10 <= b8; ++i10) {
                    this.eligibleChunksForSpawning.add(new ChunkCoordIntPair(i9 + i6, i10 + i7));
                }
            }
        }

        i4 = 0;
        Iterator iterator26 = this.eligibleChunksForSpawning.iterator();

        while(true) {
            ChunkCoordIntPair chunkCoordIntPair27;
            do {
                if(!iterator26.hasNext()) {
                    return i4;
                }

                chunkCoordIntPair27 = (ChunkCoordIntPair)iterator26.next();
            } while(world.rand.nextInt(10) != 0);

            i7 = world.rand.nextInt(this.entities.length);
            ChunkPosition chunkPosition28 = this.getRandomSpawningPointInChunk(world, chunkCoordIntPair27.chunkXPos * 16, chunkCoordIntPair27.chunkZPos * 16);
            i9 = chunkPosition28.x;
            i10 = chunkPosition28.y;
            int i11 = chunkPosition28.z;
            if(world.isBlockNormalCube(i9, i10, i11)) {
                return 0;
            }

            if(world.getBlockMaterial(i9, i10, i11) != Material.air) {
                return 0;
            }

            for(int i12 = 0; i12 < 3; ++i12) {
                int i13 = i9;
                int i14 = i10;
                int i15 = i11;
                byte b16 = 6;

                for(int i17 = 0; i17 < 2; ++i17) {
                    i13 += world.rand.nextInt(b16) - world.rand.nextInt(b16);
                    i14 += world.rand.nextInt(1) - world.rand.nextInt(1);
                    i15 += world.rand.nextInt(b16) - world.rand.nextInt(b16);
                    if(world.isBlockNormalCube(i13, i14 - 1, i15) && !world.isBlockNormalCube(i13, i14, i15) && !world.getBlockMaterial(i13, i14, i15).getIsLiquid() && !world.isBlockNormalCube(i13, i14 + 1, i15)) {
                        float f18 = (float)i13 + 0.5F;
                        float f19 = (float)i14;
                        float f20 = (float)i15 + 0.5F;
                        if(world.getClosestPlayer((double)f18, (double)f19, (double)f20, 24.0D) == null) {
                            float f21 = f18 - (float)world.spawnX;
                            float f22 = f19 - (float)world.spawnY;
                            float f23 = f20 - (float)world.spawnZ;
                            float f24 = f21 * f21 + f22 * f22 + f23 * f23;
                            if(f24 >= 576.0F) {
                                EntityLiving entityLiving29;
                                try {
                                    entityLiving29 = (EntityLiving)EntityList.createEntityByClassUnsafe(this.entities[i7], world);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    return i4;
                                }

                                entityLiving29.setLocationAndAngles((double)f18, (double)f19, (double)f20, world.rand.nextFloat() * 360.0F, 0.0F);
                                if(entityLiving29.getCanSpawnHere()) {
                                    ++i4;
                                    world.spawnEntityInWorld(entityLiving29);
                                    if(entityLiving29 instanceof EntitySpider && world.rand.nextInt(100) == 0) {
                                        EntitySkeleton entitySkeleton30 = new EntitySkeleton(world);
                                        entitySkeleton30.setLocationAndAngles((double)f18, (double)f19, (double)f20, entityLiving29.rotationYaw, 0.0F);
                                        world.spawnEntityInWorld(entitySkeleton30);
                                        entitySkeleton30.mountEntity(entityLiving29);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
