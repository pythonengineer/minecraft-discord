package net.minecraft.game.world;

import com.mojang.nbt.NBTTagCompound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.material.Material;
import net.minecraft.game.world.path.Pathfinder;
import net.minecraft.game.world.terrain.ChunkProviderGenerate;

public class World {
	private List lightingToUpdate;
	private List loadedEntityList;
	private List scheduledTickList;
	public List loadedTileEntityList;
	public long worldTime;
	private long skyColor;
	private long fogColor;
	private long cloudColor;
	private int skylightSubtracted;
	private int randInt;
	private int tickUpdateRandom;
	private static float[] lightBrightnessTable = new float[16];
	public Entity playerEntity;
	public int difficultySetting;
	public final Pathfinder pathFinder;
	public EaglercraftRandom rand;
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public boolean isNewWorld;
	private List worldAccesses;
	private IChunkProvider chunkProvider;
	private VFile2 levelFile;
	private long seed;
	private NBTTagCompound playerData;
	public long sizeOnDisk;

	public static NBTTagCompound saveWorldFile(String worldName) {
		VFile2 worldFile = new VFile2("saves");
		worldFile = new VFile2(worldFile, worldName);
		worldFile = new VFile2(worldFile, "level.dat");
		if(worldFile.exists()) {
	        try (InputStream fis = worldFile.getInputStream()) {
	            return LoadingScreenRenderer.read(fis).getCompoundTag("Data");
			} catch (IOException exception2) {
				exception2.printStackTrace();
			}
		}

		return null;
	}

	public static void deleteWorld(String worldName) {
		VFile2 worldFile = new VFile2("saves");
		worldFile = new VFile2(worldFile, worldName);
		listDirectoryFiles(worldFile.listFiles(true));
		worldFile.delete();
	}

	private static void listDirectoryFiles(List<VFile2> files) {
		for(int i1 = 0; i1 < files.size(); ++i1) {
			files.get(i1).delete();
		}

	}

	public World(String worldName) {
		this(worldName, (new EaglercraftRandom()).nextLong());
	}

	private World(String worldName, long seed) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.scheduledTickList = new LinkedList();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
		this.skyColor = 10079487L;
		this.fogColor = 11587839L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.randInt = (new EaglercraftRandom()).nextInt();
		this.tickUpdateRandom = 1013904223;
		this.pathFinder = new Pathfinder(this);
		this.rand = new EaglercraftRandom();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.seed = 0L;
		this.sizeOnDisk = 0L;
		this.levelFile = new VFile2("saves", worldName);
		VFile2 worldFile = new VFile2(this.levelFile, "level.dat");
		this.isNewWorld = !worldFile.exists();
		if(worldFile.exists()) {
            try (InputStream fis = worldFile.getInputStream()) {
				NBTTagCompound worldFile1 = LoadingScreenRenderer.read(fis).getCompoundTag("Data");
				this.seed = worldFile1.getLong("RandomSeed");
				this.spawnX = worldFile1.getInteger("SpawnX");
				this.spawnY = worldFile1.getInteger("SpawnY");
				this.spawnZ = worldFile1.getInteger("SpawnZ");
				this.worldTime = worldFile1.getLong("Time");
				this.sizeOnDisk = worldFile1.getLong("SizeOnDisk");
				this.playerData = worldFile1.getCompoundTag("Player");
			} catch (IOException exception5) {
				exception5.printStackTrace();
			}
		}

		if(this.seed == 0L) {
			this.seed = seed;
			this.spawnX = 0;
			this.spawnY = 64;
			this.spawnZ = 0;
		}

		this.chunkProvider = new ChunkProviderLoadOrGenerate(this, this.levelFile, new ChunkProviderGenerate(this, this.seed));
	}

	public final void joinPlayerInWorld() {
		try {
			if(this.playerData != null) {
				this.playerEntity.readFromNBT(this.playerData);
				this.playerData = null;
			}

			this.entityJoinedWorld(this.playerEntity);
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}

	public void saveWorld(boolean saveWorldIndirectly) {
		VFile2 file2 = new VFile2(this.levelFile, "level.dat");
		NBTTagCompound nBTTagCompound3;
		(nBTTagCompound3 = new NBTTagCompound()).setLong("RandomSeed", this.seed);
		nBTTagCompound3.setInteger("SpawnX", this.spawnX);
		nBTTagCompound3.setInteger("SpawnY", this.spawnY);
		nBTTagCompound3.setInteger("SpawnZ", this.spawnZ);
		nBTTagCompound3.setLong("Time", this.worldTime);
		nBTTagCompound3.setLong("SizeOnDisk", this.sizeOnDisk);
		nBTTagCompound3.setLong("LastPlayed", EagRuntime.currentTimeMillis());
		NBTTagCompound nBTTagCompound4;
		if(this.playerEntity != null) {
			nBTTagCompound4 = new NBTTagCompound();
			this.playerEntity.writeToNBT(nBTTagCompound4);
			nBTTagCompound3.setCompoundTag("Player", nBTTagCompound4);
		}

		(nBTTagCompound4 = new NBTTagCompound()).setTag("Data", nBTTagCompound3);

        try (OutputStream fos = file2.getOutputStream()) {
			LoadingScreenRenderer.write(nBTTagCompound4, fos);
		} catch (IOException exception5) {
			exception5.printStackTrace();
		}

		this.chunkProvider.saveChunks(saveWorldIndirectly);
	}

	public final int getBlockId(int x, int y, int z) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (y <= 0 ? Block.lavaStill.blockID : (y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15))) : 0;
	}

	public final boolean checkChunksExist(int minX, int minY, int minZ) {
		return minY >= 0 && minY < 128 ? this.chunkExists(minX >> 4, minZ >> 4) : false;
	}

	private boolean chunkExists(int chunkX, int chunkZ) {
		return this.chunkProvider.chunkExists(chunkX, chunkZ);
	}

	private Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
		return this.chunkProvider.provideChunk(chunkX, chunkZ);
	}

	public final boolean setBlock(int x, int y, int z, int blockID) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (y < 0 ? false : (y >= 128 ? false : this.getChunkFromChunkCoords(x >> 4, z >> 4).setBlockID(x & 15, y, z & 15, blockID))) : false;
	}

	public final Material getBlockMaterial(int x, int y, int z) {
		return (x = this.getBlockId(x, y, z)) == 0 ? Material.air : Block.blocksList[x].blockMaterial;
	}

	public final int getBlockMetadata(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return 0;
			} else if(y >= 128) {
				return 0;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return chunk4.getBlockMetadata(x, y, z);
			}
		} else {
			return 0;
		}
	}

	public final void setBlockMetadata(int metadata, int x, int y, int z) {
		this.setBlockAndMetadata(metadata, x, y, z);
	}

	private boolean setBlockAndMetadata(int x, int y, int z, int blockID) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				chunk5.setBlockMetadata(x, y, z, blockID);
				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean notifyBlockChange(int blockID, int x, int y, int z) {
		if(!this.setBlock(blockID, x, y, z)) {
			return false;
		} else {
			int i5 = z;
			z = y;
			y = x;
			x = blockID;
			World world7 = this;

			for(int i6 = 0; i6 < world7.worldAccesses.size(); ++i6) {
				((IWorldAccess)world7.worldAccesses.get(i6)).markBlockNeedsUpdate(x, y, z);
			}

			world7.notifyBlocksOfNeighborChange(x, y, z, i5);
			return true;
		}
	}

	public final void markBlocksDirtyVertical(int x, int z, int minY, int maxY) {
		if(minY > maxY) {
			int i5 = maxY;
			maxY = minY;
			minY = i5;
		}

		this.markBlocksDirty(x, minY, z, x, maxY, z);
	}

	public final void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).markBlockRangeNeedsUpdate(minX, minY, minZ, maxX, maxY, maxZ);
		}

	}

	public final void swap(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int i7 = this.getBlockId(minX, minY, minZ);
		int i8 = this.getBlockMetadata(minX, minY, minZ);
		int i9 = this.getBlockId(maxX, maxY, maxZ);
		int i10 = this.getBlockMetadata(maxX, maxY, maxZ);
		this.setBlock(minX, minY, minZ, i9);
		this.setBlockAndMetadata(minX, minY, minZ, i10);
		this.setBlock(maxX, maxY, maxZ, i7);
		this.setBlockAndMetadata(maxX, maxY, maxZ, i8);
		this.notifyBlocksOfNeighborChange(minX, minY, minZ, i9);
		this.notifyBlocksOfNeighborChange(maxX, maxY, maxZ, i7);
	}

	public final void notifyBlocksOfNeighborChange(int x, int y, int z, int blockID) {
		this.notifyBlockOfNeighborChange(x - 1, y, z, blockID);
		this.notifyBlockOfNeighborChange(x + 1, y, z, blockID);
		this.notifyBlockOfNeighborChange(x, y - 1, z, blockID);
		this.notifyBlockOfNeighborChange(x, y + 1, z, blockID);
		this.notifyBlockOfNeighborChange(x, y, z - 1, blockID);
		this.notifyBlockOfNeighborChange(x, y, z + 1, blockID);
	}

	private void notifyBlockOfNeighborChange(int x, int y, int z, int blockID) {
		Block block5;
		if((block5 = Block.blocksList[this.getBlockId(x, y, z)]) != null) {
			block5.onNeighborBlockChange(this, x, y, z, blockID);
		}

	}

	public final boolean canBlockSeeTheSky(int x, int y, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
	}

	public final int getBlockLightValue(int x, int y, int z) {
		return this.getBlockLightValue_do(x, y, z, true);
	}

	private int getBlockLightValue_do(int x, int y, int z, boolean update) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int update1;
			if(!update || (update1 = this.getBlockId(x, y, z)) != Block.stairSingle.blockID && update1 != Block.farmland.blockID) {
				if(y < 0) {
					return 0;
				} else if(y >= 128) {
					if((update1 = 15 - this.skylightSubtracted) < 0) {
						update1 = 0;
					}

					return update1;
				} else {
					Chunk update2 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
					x &= 15;
					z &= 15;
					return update2.getBlockLightValue(x, y, z, this.skylightSubtracted);
				}
			} else {
				update1 = this.getBlockLightValue_do(x, y + 1, z, false);
				int i5 = this.getBlockLightValue_do(x + 1, y, z, false);
				int i6 = this.getBlockLightValue_do(x - 1, y, z, false);
				int i7 = this.getBlockLightValue_do(x, y, z + 1, false);
				x = this.getBlockLightValue_do(x, y, z - 1, false);
				if(i5 > update1) {
					update1 = i5;
				}

				if(i6 > update1) {
					update1 = i6;
				}

				if(i7 > update1) {
					update1 = i7;
				}

				if(x > update1) {
					update1 = x;
				}

				return update1;
			}
		} else {
			return 15;
		}
	}

	public final boolean canExistingBlockSeeTheSky(int x, int y, int z) {
		if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if(y < 0) {
				return false;
			} else if(y >= 128) {
				return true;
			} else if(!this.chunkExists(x >> 4, z >> 4)) {
				return false;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				return chunk4.canBlockSeeTheSky(x, y, z);
			}
		} else {
			return false;
		}
	}

	public final int getHeightValue(int x, int z) {
		return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (!this.chunkExists(x >> 4, z >> 4) ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getHeightValue(x & 15, z & 15)) : 0;
	}

	public final void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		if(this.checkChunksExist(x, y, z)) {
			if(skyBlock == EnumSkyBlock.Sky) {
				if(this.canExistingBlockSeeTheSky(x, y, z)) {
					lightValue = 15;
				}
			} else if(skyBlock == EnumSkyBlock.Block) {
				int i6 = this.getBlockId(x, y, z);
				if(Block.lightValue[i6] > lightValue) {
					lightValue = Block.lightValue[i6];
				}
			}

			if(this.getSavedLightValue(skyBlock, x, y, z) != lightValue) {
				this.scheduleLightingUpdate(skyBlock, x, y, z, x, y, z);
			}

		}
	}

	public final int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
		if(y >= 0 && y < 128 && x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			int i5 = x >> 4;
			int i6 = z >> 4;
			return !this.chunkExists(i5, i6) ? 0 : this.getChunkFromChunkCoords(i5, i6).getSavedLightValue(skyBlock, x & 15, y, z & 15);
		} else {
			return skyBlock.defaultLightValue;
		}
	}

	public final float getBrightness(int x, int y, int z) {
		return lightBrightnessTable[this.getBlockLightValue(x, y, z)];
	}

	public final boolean isDaytime() {
		return this.skylightSubtracted < 8;
	}

	public final MovingObjectPosition rayTraceBlocks_do(Vec3D vector1, Vec3D vector2) {
		if(!Double.isNaN(vector1.xCoord) && !Double.isNaN(vector1.yCoord) && !Double.isNaN(vector1.zCoord)) {
			if(!Double.isNaN(vector2.xCoord) && !Double.isNaN(vector2.yCoord) && !Double.isNaN(vector2.zCoord)) {
				int i3 = MathHelper.floor_double(vector2.xCoord);
				int i4 = MathHelper.floor_double(vector2.yCoord);
				int i5 = MathHelper.floor_double(vector2.zCoord);
				int i6 = MathHelper.floor_double(vector1.xCoord);
				int i7 = MathHelper.floor_double(vector1.yCoord);
				int i8 = MathHelper.floor_double(vector1.zCoord);
				int i9 = 20;

				Block block11;
				int i30;
				MovingObjectPosition movingObjectPosition31;
				do {
					if(i9-- < 0) {
						return null;
					}

					if(Double.isNaN(vector1.xCoord) || Double.isNaN(vector1.yCoord) || Double.isNaN(vector1.zCoord)) {
						return null;
					}

					if(i6 == i3 && i7 == i4 && i8 == i5) {
						return null;
					}

					double d10 = 999.0D;
					double d12 = 999.0D;
					double d14 = 999.0D;
					if(i3 > i6) {
						d10 = (double)i6 + 1.0D;
					}

					if(i3 < i6) {
						d10 = (double)i6;
					}

					if(i4 > i7) {
						d12 = (double)i7 + 1.0D;
					}

					if(i4 < i7) {
						d12 = (double)i7;
					}

					if(i5 > i8) {
						d14 = (double)i8 + 1.0D;
					}

					if(i5 < i8) {
						d14 = (double)i8;
					}

					double d16 = 999.0D;
					double d18 = 999.0D;
					double d20 = 999.0D;
					double d22 = vector2.xCoord - vector1.xCoord;
					double d24 = vector2.yCoord - vector1.yCoord;
					double d26 = vector2.zCoord - vector1.zCoord;
					if(d10 != 999.0D) {
						d16 = (d10 - vector1.xCoord) / d22;
					}

					if(d12 != 999.0D) {
						d18 = (d12 - vector1.yCoord) / d24;
					}

					if(d14 != 999.0D) {
						d20 = (d14 - vector1.zCoord) / d26;
					}

					byte b28;
					if(d16 < d18 && d16 < d20) {
						if(i3 > i6) {
							b28 = 4;
						} else {
							b28 = 5;
						}

						vector1.xCoord = d10;
						vector1.yCoord += d24 * d16;
						vector1.zCoord += d26 * d16;
					} else if(d18 < d20) {
						if(i4 > i7) {
							b28 = 0;
						} else {
							b28 = 1;
						}

						vector1.xCoord += d22 * d18;
						vector1.yCoord = d12;
						vector1.zCoord += d26 * d18;
					} else {
						if(i5 > i8) {
							b28 = 2;
						} else {
							b28 = 3;
						}

						vector1.xCoord += d22 * d20;
						vector1.yCoord += d24 * d20;
						vector1.zCoord = d14;
					}

					Vec3D vec3D29;
					i6 = (int)((vec3D29 = new Vec3D(vector1.xCoord, vector1.yCoord, vector1.zCoord)).xCoord = (double)MathHelper.floor_double(vector1.xCoord));
					if(b28 == 5) {
						--i6;
						++vec3D29.xCoord;
					}

					i7 = (int)(vec3D29.yCoord = (double)MathHelper.floor_double(vector1.yCoord));
					if(b28 == 1) {
						--i7;
						++vec3D29.yCoord;
					}

					i8 = (int)(vec3D29.zCoord = (double)MathHelper.floor_double(vector1.zCoord));
					if(b28 == 3) {
						--i8;
						++vec3D29.zCoord;
					}

					i30 = this.getBlockId(i6, i7, i8);
					block11 = Block.blocksList[i30];
					if(i30 > 0 && block11.isCollidable() && (movingObjectPosition31 = block11.collisionRayTrace(this, i6, i7, i8, vector1, vector2)) != null) {
						return movingObjectPosition31;
					}

					i30 = this.getBlockId(i6, i7 - 1, i8);
					block11 = Block.blocksList[i30];
				} while(i30 <= 0 || !block11.isCollidable() || (movingObjectPosition31 = block11.collisionRayTrace(this, i6, i7 - 1, i8, vector1, vector2)) == null);

				return movingObjectPosition31;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final void playSoundAtEntity(Entity entity, String soundName, float volume, float pitch) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			float f6 = 16.0F;
			if(volume > 1.0F) {
				f6 = 16.0F * volume;
			}

			if(this.playerEntity.getDistanceToEntity(entity) < (double)(f6 * f6)) {
				((IWorldAccess)this.worldAccesses.get(i5)).playSound(soundName, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, volume, pitch);
			}
		}

	}

	public final void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
		try {
			for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
				float f11 = 16.0F;
				if(volume > 1.0F) {
					f11 = 16.0F * volume;
				}

				double d12 = x - this.playerEntity.posX;
				double d14 = y - this.playerEntity.posY;
				double d16 = z - this.playerEntity.posZ;
				if(d12 * d12 + d14 * d14 + d16 * d16 < (double)(f11 * f11)) {
					((IWorldAccess)this.worldAccesses.get(i10)).playSound(soundName, x, y, z, volume, pitch);
				}
			}

		} catch (Exception exception18) {
			exception18.printStackTrace();
		}
	}

	public final void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(particleName, x, y, z, motionX, motionY, motionZ);
		}

	}

	public final void entityJoinedWorld(Entity entity) {
		int i2 = MathHelper.floor_double(entity.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
		if(!this.chunkExists(i2, i3)) {
			System.out.println("Failed to add entity " + entity);
		} else {
			this.getChunkFromChunkCoords(i2, i3).addEntity(entity);
			this.loadedEntityList.add(entity);

			for(i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
				((IWorldAccess)this.worldAccesses.get(i2)).obtainEntitySkin(entity);
			}

		}
	}

	public static void setEntityDead(Entity entity) {
		entity.isDead = true;
	}

	public final void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	public final void removeWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.remove(worldAccess);
	}

	public final List getCollidingBoundingBoxes(AxisAlignedBB aabb) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = MathHelper.floor_double(aabb.minX);
		int i4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i5 = MathHelper.floor_double(aabb.minY);
		int i6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i7 = MathHelper.floor_double(aabb.minZ);
		int i8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i9 = i5 - 1; i9 < i6; ++i9) {
				for(int i10 = i7; i10 < i8; ++i10) {
					Block block11;
					AxisAlignedBB axisAlignedBB12;
					if((block11 = Block.blocksList[this.getBlockId(i3, i9, i10)]) != null && (axisAlignedBB12 = block11.getCollisionBoundingBoxFromPool(this, i3, i9, i10)) != null && aabb.intersectsWith(axisAlignedBB12)) {
						arrayList2.add(axisAlignedBB12);
					}
				}
			}
		}

		return arrayList2;
	}

	public final Vec3D getSkyColor(float partialTicks) {
		if((partialTicks = MathHelper.cos(this.getCelestialAngle(partialTicks) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			partialTicks = 0.0F;
		}

		if(partialTicks > 1.0F) {
			partialTicks = 1.0F;
		}

		float f2 = (float)(this.skyColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.skyColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.skyColor & 255L) / 255.0F;
		f2 *= partialTicks;
		f3 *= partialTicks;
		f4 *= partialTicks;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final float getCelestialAngle(float partialTicks) {
		int i2;
		return ((float)(i2 = (int)(this.worldTime % 24000L)) + partialTicks) / 24000.0F - 0.15F;
	}

	public final Vec3D getCloudColor(float partialTicks) {
		if((partialTicks = MathHelper.cos(this.getCelestialAngle(partialTicks) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			partialTicks = 0.0F;
		}

		if(partialTicks > 1.0F) {
			partialTicks = 1.0F;
		}

		float f2 = (float)(this.cloudColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.cloudColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.cloudColor & 255L) / 255.0F;
		f2 *= partialTicks * 0.9F + 0.1F;
		f3 *= partialTicks * 0.9F + 0.1F;
		f4 *= partialTicks * 0.85F + 0.15F;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final Vec3D getFogColor(float partialTicks) {
		if((partialTicks = MathHelper.cos(this.getCelestialAngle(partialTicks) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			partialTicks = 0.0F;
		}

		if(partialTicks > 1.0F) {
			partialTicks = 1.0F;
		}

		float f2 = (float)(this.fogColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.fogColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.fogColor & 255L) / 255.0F;
		f2 *= partialTicks * 0.94F + 0.06F;
		f3 *= partialTicks * 0.94F + 0.06F;
		f4 *= partialTicks * 0.91F + 0.09F;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final float calculateFogLight(float partialTicks) {
		partialTicks = this.getCelestialAngle(partialTicks);
		if((partialTicks = 1.0F - (MathHelper.cos(partialTicks * (float)Math.PI * 2.0F) * 2.0F + 0.75F)) < 0.0F) {
			partialTicks = 0.0F;
		}

		if(partialTicks > 1.0F) {
			partialTicks = 1.0F;
		}

		return partialTicks * partialTicks * 0.5F;
	}

	public final void scheduleBlockUpdate(int x, int y, int z, int blockID) {
		NextTickListEntry x1 = new NextTickListEntry(x, y, z, blockID);
		if(blockID > 0) {
			z = Block.blocksList[blockID].tickRate();
			x1.scheduledTime = z;
		}

		this.scheduledTickList.add(x1);
	}

	public final void updateEntities() {
		int i1;
		for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			Entity entity2;
			int i3;
			int i4;
			int i5;
			if(!(entity2 = (Entity)this.loadedEntityList.get(i1)).isDead) {
				i3 = MathHelper.floor_double(entity2.posX / 16.0D);
				i4 = MathHelper.floor_double(entity2.posY / 16.0D);
				i5 = MathHelper.floor_double(entity2.posZ / 16.0D);
				entity2.lastTickPosX = entity2.posX;
				entity2.lastTickPosY = entity2.posY;
				entity2.lastTickPosZ = entity2.posZ;
				entity2.prevRotationYaw = entity2.rotationYaw;
				entity2.prevRotationPitch = entity2.rotationPitch;
				entity2.onUpdate();
				int i6 = MathHelper.floor_double(entity2.posX / 16.0D);
				int i7 = MathHelper.floor_double(entity2.posY / 16.0D);
				int i8 = MathHelper.floor_double(entity2.posZ / 16.0D);
				if(i3 != i6 || i4 != i7 || i5 != i8) {
					if(this.chunkExists(i3, i5)) {
						this.getChunkFromChunkCoords(i3, i5).removeEntityAtIndex(entity2, i4);
					}

					if(this.chunkExists(i6, i8)) {
						this.getChunkFromChunkCoords(i6, i8).addEntity(entity2);
					} else {
						entity2.isDead = true;
					}
				}
			}

			if(entity2.isDead) {
				i3 = MathHelper.floor_double(entity2.posX / 16.0D);
				i4 = MathHelper.floor_double(entity2.posZ / 16.0D);
				if(this.chunkExists(i3, i4)) {
					this.getChunkFromChunkCoords(i3, i4).removeEntityAtIndex(entity2, MathHelper.floor_double(entity2.posY / 16.0D));
				}

				this.loadedEntityList.remove(i1--);

				for(i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
					((IWorldAccess)this.worldAccesses.get(i5)).releaseEntitySkin(entity2);
				}
			}
		}

		for(i1 = 0; i1 < this.loadedTileEntityList.size(); ++i1) {
			((TileEntity)this.loadedTileEntityList.get(i1)).updateEntity();
		}

	}

	public final boolean checkIfAABBIsClear(AxisAlignedBB aabb) {
		List list3 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, aabb);

		for(int i2 = 0; i2 < list3.size(); ++i2) {
			if(((Entity)list3.get(i2)).preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB aabb) {
		int i2 = MathHelper.floor_double(aabb.minX);
		int i3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i4 = MathHelper.floor_double(aabb.minY);
		int i5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i6 = MathHelper.floor_double(aabb.minZ);
		int i7 = MathHelper.floor_double(aabb.maxZ + 1.0D);
		if(aabb.minX < 0.0D) {
			--i2;
		}

		if(aabb.minY < 0.0D) {
			--i4;
		}

		if(aabb.minZ < 0.0D) {
			--i6;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Block block9;
					if((block9 = Block.blocksList[this.getBlockId(i10, i2, i8)]) != null && block9.blockMaterial.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isBoundingBoxBurning(AxisAlignedBB aabb) {
		int i2 = MathHelper.floor_double(aabb.minX);
		int i3 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i4 = MathHelper.floor_double(aabb.minY);
		int i5 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i6 = MathHelper.floor_double(aabb.minZ);
		int i10 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(i2 = i2; i2 < i3; ++i2) {
			for(int i7 = i4; i7 < i5; ++i7) {
				for(int i8 = i6; i8 < i10; ++i8) {
					int i9;
					if((i9 = this.getBlockId(i2, i7, i8)) == Block.fire.blockID || i9 == Block.lavaMoving.blockID || i9 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isMaterialInBB(AxisAlignedBB aabb, Material material) {
		int i3 = MathHelper.floor_double(aabb.minX);
		int i4 = MathHelper.floor_double(aabb.maxX + 1.0D);
		int i5 = MathHelper.floor_double(aabb.minY);
		int i6 = MathHelper.floor_double(aabb.maxY + 1.0D);
		int i7 = MathHelper.floor_double(aabb.minZ);
		int i11 = MathHelper.floor_double(aabb.maxZ + 1.0D);

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i8 = i5; i8 < i6; ++i8) {
				for(int i9 = i7; i9 < i11; ++i9) {
					Block block10;
					if((block10 = Block.blocksList[this.getBlockId(i3, i8, i9)]) != null && block10.blockMaterial == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void createExplosion(Entity entity, double x, double y, double z, float radius) {
		new Explosion();
		float f3 = radius;
		double d15 = z;
		double d13 = y;
		double d11 = x;
		Entity entity67 = entity;
		World world66 = this;
		this.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet hashSet68 = new HashSet();

		int i7;
		double d32;
		double d34;
		double d36;
		int i69;
		int i71;
		for(i69 = 0; i69 < 16; ++i69) {
			for(i7 = 0; i7 < 16; ++i7) {
				for(i71 = 0; i71 < 16; ++i71) {
					if(i69 == 0 || i69 == 15 || i7 == 0 || i7 == 15 || i71 == 0 || i71 == 15) {
						double d23 = (double)((float)i69 / 15.0F * 2.0F - 1.0F);
						double d25 = (double)((float)i7 / 15.0F * 2.0F - 1.0F);
						double d27 = (double)((float)i71 / 15.0F * 2.0F - 1.0F);
						double d29 = Math.sqrt(d23 * d23 + d25 * d25 + d27 * d27);
						d23 /= d29;
						d25 /= d29;
						d27 /= d29;
						float f31 = f3 * (0.7F + world66.rand.nextFloat() * 0.6F);
						d32 = d11;
						d34 = d13;

						for(d36 = d15; f31 > 0.0F; f31 -= 0.22500001F) {
							int i39 = MathHelper.floor_double(d32);
							int i40 = MathHelper.floor_double(d34);
							int i41 = MathHelper.floor_double(d36);
							int i42;
							if((i42 = world66.getBlockId(i39, i40, i41)) > 0) {
								f31 -= (Block.blocksList[i42].getExplosionResistance() + 0.3F) * 0.3F;
							}

							if(f31 > 0.0F) {
								hashSet68.add(new ChunkPosition(i39, i40, i41));
							}

							d32 += d23 * (double)0.3F;
							d34 += d25 * (double)0.3F;
							d36 += d27 * (double)0.3F;
						}
					}
				}
			}
		}

		f3 *= 2.0F;
		i69 = MathHelper.floor_double(d11 - (double)f3 - 1.0D);
		i7 = MathHelper.floor_double(d11 + (double)f3 + 1.0D);
		i71 = MathHelper.floor_double(d13 - (double)f3 - 1.0D);
		int i72 = MathHelper.floor_double(d13 + (double)f3 + 1.0D);
		int i24 = MathHelper.floor_double(d15 - (double)f3 - 1.0D);
		int i73 = MathHelper.floor_double(d15 + (double)f3 + 1.0D);
		List list26 = world66.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB((double)i69, (double)i71, (double)i24, (double)i7, (double)i72, (double)i73));
		Vec3D vec3D74 = new Vec3D(d11, d13, d15);

		double d38;
		double d65;
		double d82;
		for(int i28 = 0; i28 < list26.size(); ++i28) {
			Entity entity70;
			Entity entity76;
			double d59 = (entity70 = entity76 = (Entity)list26.get(i28)).posX - d11;
			double d61 = entity70.posY - d13;
			double d63 = entity70.posZ - d15;
			double d30;
			if((d30 = (double)MathHelper.sqrt_double(d59 * d59 + d61 * d61 + d63 * d63) / (double)f3) <= 1.0D) {
				d32 = entity76.posX - d11;
				d34 = entity76.posY - d13;
				d36 = entity76.posZ - d15;
				d38 = (double)MathHelper.sqrt_double(d32 * d32 + d34 * d34 + d36 * d36);
				d32 /= d38;
				d34 /= d38;
				d36 /= d38;
				d82 = (double)world66.getBlockDensity(vec3D74, entity76.boundingBox);
				d65 = (1.0D - d30) * d82;
				entity76.attackEntityFrom(entity67, (int)((d65 * d65 + d65) / 2.0D * 8.0D * (double)f3 + 1.0D));
				entity76.motionZ += d32 * d65;
				entity76.motionY += d34 * d65;
				entity76.motionX += d36 * d65;
			}
		}

		f3 = radius;
		ArrayList arrayList75;
		(arrayList75 = new ArrayList()).addAll(hashSet68);

		for(int i77 = arrayList75.size() - 1; i77 >= 0; --i77) {
			ChunkPosition chunkPosition78;
			int i79 = (chunkPosition78 = (ChunkPosition)arrayList75.get(i77)).x;
			int i80 = chunkPosition78.y;
			int i33 = chunkPosition78.z;
			int i81 = world66.getBlockId(i79, i80, i33);

			for(int i35 = 0; i35 <= 0; ++i35) {
				d36 = (double)((float)i79 + world66.rand.nextFloat());
				d38 = (double)((float)i80 + world66.rand.nextFloat());
				d82 = (double)((float)i33 + world66.rand.nextFloat());
				d65 = d36 - d11;
				double d44 = d38 - d13;
				double d46 = d82 - d15;
				double d48 = (double)MathHelper.sqrt_double(d65 * d65 + d44 * d44 + d46 * d46);
				d65 /= d48;
				d44 /= d48;
				d46 /= d48;
				double d50 = (d50 = 0.5D / (d48 / (double)f3 + 0.1D)) * (double)(world66.rand.nextFloat() * world66.rand.nextFloat() + 0.3F);
				d65 *= d50;
				d44 *= d50;
				d46 *= d50;
				world66.spawnParticle("explode", (d36 + d11) / 2.0D, (d38 + d13) / 2.0D, (d82 + d15) / 2.0D, d65, d44, d46);
				world66.spawnParticle("smoke", d36, d38, d82, d65, d44, d46);
			}

			if(i81 > 0) {
				Block.blocksList[i81].dropBlockAsItemWithChance(world66, i79, i80, i33, world66.getBlockMetadata(i79, i80, i33), 0.3F);
				world66.notifyBlockChange(i79, i80, i33, 0);
				Block.blocksList[i81].onBlockDestroyedByExplosion(world66, i79, i80, i33);
			}
		}

	}

	public final float getBlockDensity(Vec3D vector, AxisAlignedBB aabb) {
		double d3 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
		double d5 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
		double d7 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
		int i9 = 0;
		int i10 = 0;

		for(float f11 = 0.0F; f11 <= 1.0F; f11 = (float)((double)f11 + d3)) {
			for(float f12 = 0.0F; f12 <= 1.0F; f12 = (float)((double)f12 + d5)) {
				for(float f13 = 0.0F; f13 <= 1.0F; f13 = (float)((double)f13 + d7)) {
					double d14 = aabb.minX + (aabb.maxX - aabb.minX) * (double)f11;
					double d16 = aabb.minY + (aabb.maxY - aabb.minY) * (double)f12;
					double d18 = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)f13;
					if(this.rayTraceBlocks_do(new Vec3D(d14, d16, d18), vector) == null) {
						++i9;
					}

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public final void onBlockHit(int x, int y, int z, int side) {
		if(side == 0) {
			--y;
		}

		if(side == 1) {
			++y;
		}

		if(side == 2) {
			--z;
		}

		if(side == 3) {
			++z;
		}

		if(side == 4) {
			--x;
		}

		if(side == 5) {
			++x;
		}

		if(this.getBlockId(x, y, z) == Block.fire.blockID) {
			this.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
			this.notifyBlockChange(x, y, z, 0);
		}

	}

	public final String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final TileEntity getBlockTileEntity(int x, int y, int z) {
		Chunk chunk4;
		return (chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4)) != null ? chunk4.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
	}

	public final void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		Chunk chunk5;
		if((chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4)) != null) {
			chunk5.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
		}

	}

	public final void removeBlockTileEntity(int x, int y, int z) {
		Chunk chunk4;
		if((chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4)) != null) {
			chunk4.removeChunkBlockTileEntity(x & 15, y, z & 15);
		}

	}

	public final boolean isBlockNormalCube(int x, int y, int z) {
		Block x1;
		return (x1 = Block.blocksList[this.getBlockId(x, y, z)]) == null ? false : x1.isOpaqueCube();
	}

	public final void saveWorldIndirectly() {
		this.saveWorld(true);
	}

	public final int lightUpdatesNeeded() {
		return this.lightingToUpdate.size();
	}

	public final boolean updatingLighting() {
		int i1 = 100000;

		while(this.lightingToUpdate.size() > 0) {
			--i1;
			if(i1 <= 0) {
				return true;
			}

			MetadataChunkBlock metadataChunkBlock10000 = (MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1);
			World world3 = this;
			MetadataChunkBlock metadataChunkBlock2 = metadataChunkBlock10000;

			for(int i4 = metadataChunkBlock10000.minX; i4 <= metadataChunkBlock2.maxX; ++i4) {
				for(int i5 = metadataChunkBlock2.minZ; i5 <= metadataChunkBlock2.maxZ; ++i5) {
					if(world3.checkChunksExist(i4, 0, i5)) {
						for(int i6 = metadataChunkBlock2.minY; i6 <= metadataChunkBlock2.maxY; ++i6) {
							if(i6 >= 0 && i6 < 128) {
								int i7 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5);
								int i8 = world3.getBlockId(i4, i6, i5);
								int i9;
								if((i9 = Block.lightOpacity[i8]) == 0) {
									i9 = 1;
								}

								int i10 = 0;
								if(metadataChunkBlock2.skyBlock == EnumSkyBlock.Sky) {
									if(world3.canExistingBlockSeeTheSky(i4, i6, i5)) {
										i10 = 15;
									}
								} else if(metadataChunkBlock2.skyBlock == EnumSkyBlock.Block) {
									i10 = Block.lightValue[i8];
								}

								int i11;
								int i12;
								if(i9 >= 15 && i10 == 0) {
									i8 = 0;
								} else {
									i8 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4 - 1, i6, i5);
									i11 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4 + 1, i6, i5);
									i12 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6 - 1, i5);
									int i13 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6 + 1, i5);
									int i14 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5 - 1);
									int i15 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5 + 1);
									i8 = i8;
									if(i11 > i8) {
										i8 = i11;
									}

									if(i12 > i8) {
										i8 = i12;
									}

									if(i13 > i8) {
										i8 = i13;
									}

									if(i14 > i8) {
										i8 = i14;
									}

									if(i15 > i8) {
										i8 = i15;
									}

									if((i8 -= i9) < 0) {
										i8 = 0;
									}

									if(i10 > i8) {
										i8 = i10;
									}
								}

								if(i7 != i8) {
									i12 = i5;
									i11 = i6;
									i10 = i4;
									EnumSkyBlock enumSkyBlock17 = metadataChunkBlock2.skyBlock;
									World world16 = world3;
									if(i4 >= -32000000 && i5 >= -32000000 && i4 < 32000000 && i5 <= 32000000 && i6 >= 0 && i6 < 128 && world3.chunkExists(i4 >> 4, i5 >> 4)) {
										world3.getChunkFromChunkCoords(i4 >> 4, i5 >> 4).setLightValue(enumSkyBlock17, i4 & 15, i6, i5 & 15, i8);

										for(i9 = 0; i9 < world16.worldAccesses.size(); ++i9) {
											((IWorldAccess)world16.worldAccesses.get(i9)).markBlockNeedsUpdate(i10, i11, i12);
										}
									}

									if(--i8 < 0) {
										i8 = 0;
									}

									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4 - 1, i6, i5, i8);
									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6 - 1, i5, i8);
									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6, i5 - 1, i8);
									if(i4 + 1 >= metadataChunkBlock2.maxX) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4 + 1, i6, i5, i8);
									}

									if(i6 + 1 >= metadataChunkBlock2.maxY) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6 + 1, i5, i8);
									}

									if(i5 + 1 >= metadataChunkBlock2.maxZ) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6, i5 + 1, i8);
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public final void scheduleLightingUpdate(EnumSkyBlock skyBlocki, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int i8 = this.lightingToUpdate.size();
		int i9 = 4;
		if(4 > i8) {
			i9 = i8;
		}

		for(i8 = 0; i8 < i9; ++i8) {
			MetadataChunkBlock metadataChunkBlock10;
			if((metadataChunkBlock10 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i8 - 1)).skyBlock == skyBlocki) {
				boolean z10000;
				if(minX >= metadataChunkBlock10.minX && minY >= metadataChunkBlock10.minY && minZ >= metadataChunkBlock10.minZ && maxX <= metadataChunkBlock10.maxX && maxY <= metadataChunkBlock10.maxY && maxZ <= metadataChunkBlock10.maxZ) {
					z10000 = true;
				} else if(minX >= metadataChunkBlock10.minX - 1 && minY >= metadataChunkBlock10.minY - 1 && minZ >= metadataChunkBlock10.minZ - 1 && maxX <= metadataChunkBlock10.maxX + 1 && maxY <= metadataChunkBlock10.maxY + 1 && maxZ <= metadataChunkBlock10.maxZ + 1) {
					if(minX < metadataChunkBlock10.minX) {
						metadataChunkBlock10.minX = minX;
					}

					if(minY < metadataChunkBlock10.minY) {
						metadataChunkBlock10.minY = minY;
					}

					if(minZ < metadataChunkBlock10.minZ) {
						metadataChunkBlock10.minZ = minZ;
					}

					if(maxX > metadataChunkBlock10.maxX) {
						metadataChunkBlock10.maxX = maxX;
					}

					if(maxY > metadataChunkBlock10.maxY) {
						metadataChunkBlock10.maxY = maxY;
					}

					if(maxZ > metadataChunkBlock10.maxZ) {
						metadataChunkBlock10.maxZ = maxZ;
					}

					z10000 = true;
				} else {
					z10000 = false;
				}

				if(z10000) {
					return;
				}
			}
		}

		this.lightingToUpdate.add(new MetadataChunkBlock(skyBlocki, minX, minY, minZ, maxX, maxY, maxZ));
		if(this.lightingToUpdate.size() > 1000000) {
			while(this.lightingToUpdate.size() > 500000) {
				this.updatingLighting();
			}
		}

	}

	public final void tickUpdates() {
		this.chunkProvider.unload100OldestChunks();
		if(!this.loadedEntityList.contains(this.playerEntity)) {
			this.entityJoinedWorld(this.playerEntity);
		}

		float f1 = 1.0F;
		f1 = this.getCelestialAngle(1.0F);
		if((f1 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F)) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i8;
		if((i8 = (int)(f1 * 13.0F)) != this.skylightSubtracted) {
			this.skylightSubtracted = i8;

			for(i8 = 0; i8 < this.worldAccesses.size(); ++i8) {
				((IWorldAccess)this.worldAccesses.get(i8)).updateAllRenderers();
			}
		}

		++this.worldTime;
		if(this.worldTime % 100L == 0L) {
			this.saveWorld(false);
		}

		if((i8 = this.scheduledTickList.size()) > 200) {
			i8 = 200;
		}

		int i2;
		int i4;
		for(i2 = 0; i2 < i8; ++i2) {
			NextTickListEntry nextTickListEntry3;
			if((nextTickListEntry3 = (NextTickListEntry)this.scheduledTickList.remove(0)).scheduledTime > 0) {
				--nextTickListEntry3.scheduledTime;
				this.scheduledTickList.add(nextTickListEntry3);
			} else if(this.checkChunksExist(nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord) && (i4 = this.getBlockId(nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord)) == nextTickListEntry3.blockID && i4 > 0) {
				Block.blocksList[i4].updateTick(this, nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord, this.rand);
			}
		}

		i8 = MathHelper.floor_double(this.playerEntity.posX);
		i2 = MathHelper.floor_double(this.playerEntity.posZ);

		for(int i9 = 0; i9 < 32000; ++i9) {
			this.randInt = this.randInt * 3 + this.tickUpdateRandom;
			int i5 = ((i4 = this.randInt >> 2) & 255) - 128 + i8;
			int i6 = (i4 >> 8 & 255) - 128 + i2;
			i4 = i4 >> 16 & 127;
			int i7 = this.getBlockId(i5, i4, i6);
			if(Block.tickOnLoad[i7]) {
				Block.blocksList[i7].updateTick(this, i5, i4, i6, this.rand);
			}
		}

	}

	public final void randomDisplayUpdates(int x, int y, int z) {
		EaglercraftRandom random4 = new EaglercraftRandom();

		for(int i5 = 0; i5 < 1000; ++i5) {
			int i6 = x + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i7 = y + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i8 = z + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i9;
			if((i9 = this.getBlockId(i6, i7, i8)) > 0) {
				Block.blocksList[i9].randomDisplayTick(this, i6, i7, i8, random4);
			}
		}

	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB aabb) {
		int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
		ArrayList arrayList7 = new ArrayList();

		for(i3 = i3; i3 <= i4; ++i3) {
			for(int i8 = i5; i8 <= i6; ++i8) {
				if(this.chunkExists(i3, i8)) {
					this.getChunkFromChunkCoords(i3, i8).getEntitiesOfTypeWithinAAAB(entity, aabb, arrayList7);
				}
			}
		}

		return arrayList7;
	}

	public final List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public final void updateTileEntityChunkAndDoNothing(int x, int y, int z) {
		if(this.checkChunksExist(x, y, z)) {
			this.getChunkFromChunkCoords(x >> 4, z >> 4).isModified = true;
		}

	}

	public final int countEntities(Class entityClass) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			Entity entity4 = (Entity)this.loadedEntityList.get(i3);
			if(entityClass.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public final void addLoadedEntities(List loadedEntities) {
		this.loadedEntityList.addAll(loadedEntities);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			IWorldAccess iWorldAccess3 = (IWorldAccess)this.worldAccesses.get(i2);

			for(int i4 = 0; i4 < loadedEntities.size(); ++i4) {
				iWorldAccess3.obtainEntitySkin((Entity)loadedEntities.get(i4));
			}
		}

	}

	public final void unloadEntities(List unloadedEntities) {
		this.loadedEntityList.removeAll(unloadedEntities);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			IWorldAccess iWorldAccess3 = (IWorldAccess)this.worldAccesses.get(i2);

			for(int i4 = 0; i4 < unloadedEntities.size(); ++i4) {
				iWorldAccess3.releaseEntitySkin((Entity)unloadedEntities.get(i4));
			}
		}

	}

	public final void dropOldChunks() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

	static {
		for(int i0 = 0; i0 <= 15; ++i0) {
			float f1 = 1.0F - (float)i0 / 15.0F;
			lightBrightnessTable[i0] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

	}
}