package net.minecraft.game.world;

import com.mojang.nbt.NBTTagCompound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.CompressedStreamTools;
import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockFluid;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.ChunkCoordIntPair;
import net.minecraft.game.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.chunk.loader.ChunkLoader;
import net.minecraft.game.world.material.Material;
import net.minecraft.game.world.path.PathEntity;
import net.minecraft.game.world.path.Pathfinder;
import net.minecraft.game.world.terrain.ChunkProviderGenerate;

public class World implements IBlockAccess {
	private List lightingToUpdate;
	private List loadedEntityList;
    private List unloadedEntityList;
    private TreeSet scheduledTickTreeSet;
    private Set scheduledTickSet;
	public List loadedTileEntityList;
	public long worldTime;
    public boolean snowCovered;
	private long skyColor;
	private long fogColor;
	private long cloudColor;
    public int skylightSubtracted;
	protected int updateLCG;
	protected int DIST_HASH_MAGIC;
    public boolean editingBlocks;
    public static float[] lightBrightnessTable = new float[16];
    public List playerEntities;
	public int difficultySetting;
	public Object fontRenderer;
	public EaglercraftRandom rand;
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public boolean isNewWorld;
	private List worldAccesses;
	private IChunkProvider chunkProvider;
	private VFile2 saveDirectory;
    public long randomSeed;
	private NBTTagCompound nbtCompoundPlayer;
	public long sizeOnDisk;
	public final String levelName;
    private ArrayList collidingBoundingBoxes;
    private Set positionsToUpdate;
    private int soundCounter;
    private List entitiesWithinAABBExcludingEntity;

	public static NBTTagCompound getLevelData(String worldName) {
		VFile2 worldFile = new VFile2("saves");
		worldFile = new VFile2(worldFile, worldName);
		worldFile = new VFile2(worldFile, "level.dat");
		if(worldFile.exists()) {
	        try (InputStream fis = worldFile.getInputStream()) {
                return CompressedStreamTools.readCompressed(fis).getCompoundTag("Data");
			} catch (IOException exception2) {
				exception2.printStackTrace();
			}
		}

		return null;
	}

	public static void deleteWorld(String worldName) {
		VFile2 worldFile = new VFile2("saves");
		worldFile = new VFile2(worldFile, worldName);
		deleteWorldFiles(worldFile.listFiles(true));
		worldFile.delete();
	}

	private static void deleteWorldFiles(List<VFile2> files) {
		for(int i1 = 0; i1 < files.size(); ++i1) {
			files.get(i1).delete();
		}

	}

	public World(String worldName) {
		this(worldName, (new EaglercraftRandom()).nextLong());
	}

	public World(String worldName, long seed) {
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
        this.unloadedEntityList = new ArrayList();
        this.scheduledTickTreeSet = new TreeSet();
        this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.worldTime = 0L;
        this.snowCovered = false;
        this.skyColor = 8961023L;
        this.fogColor = 12638463L;
		this.cloudColor = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new EaglercraftRandom()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
        this.editingBlocks = false;
        this.playerEntities = new ArrayList();
		this.rand = new EaglercraftRandom();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.randomSeed = 0L;
		this.sizeOnDisk = 0L;
        this.collidingBoundingBoxes = new ArrayList();
        this.positionsToUpdate = new HashSet();
        this.soundCounter = this.rand.nextInt(12000);
        this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.levelName = worldName;
		this.saveDirectory = new VFile2("saves", worldName);
		VFile2 worldFile = new VFile2(this.saveDirectory, "level.dat");
		this.isNewWorld = !worldFile.exists();
		if(worldFile.exists()) {
            try (InputStream fis = worldFile.getInputStream()) {
				NBTTagCompound worldFile1 = CompressedStreamTools.readCompressed(fis).getCompoundTag("Data");
				this.randomSeed = worldFile1.getLong("RandomSeed");
				this.spawnX = worldFile1.getInteger("SpawnX");
				this.spawnY = worldFile1.getInteger("SpawnY");
				this.spawnZ = worldFile1.getInteger("SpawnZ");
				this.worldTime = worldFile1.getLong("Time");
				this.sizeOnDisk = worldFile1.getLong("SizeOnDisk");
                this.snowCovered = worldFile1.getBoolean("SnowCovered");
                if(worldFile1.hasKey("Player")) {
                    this.nbtCompoundPlayer = worldFile1.getCompoundTag("Player");
                }
			} catch (IOException exception5) {
				exception5.printStackTrace();
			}
        } else {
            this.snowCovered = this.rand.nextInt(4) == 0;
		}

        boolean worldFile2 = false;
        if(this.randomSeed == 0L) {
            this.randomSeed = seed;
            worldFile2 = true;
        }

        this.chunkProvider = this.getChunkProvider(this.saveDirectory);
        if(worldFile2) {
            this.spawnX = 0;
            this.spawnY = 64;

            for(this.spawnZ = 0; !this.findSpawn(this.spawnX, this.spawnZ); this.spawnZ += this.rand.nextInt(64) - this.rand.nextInt(64)) {
                this.spawnX += this.rand.nextInt(64) - this.rand.nextInt(64);
            }
        }

        this.calculateInitialSkylight();
    }

    protected IChunkProvider getChunkProvider(VFile2 worldFile) {
        return new ChunkProviderLoadOrGenerate(this, new ChunkLoader(worldFile, true), new ChunkProviderGenerate(this, this.randomSeed));
    }

	public void setSpawnLocation() {
		if(this.spawnY <= 0) {
			this.spawnY = 64;
		}

		while(this.getFirstUncoveredBlock(this.spawnX, this.spawnZ) == 0) {
			this.spawnX += this.rand.nextInt(8) - this.rand.nextInt(8);
			this.spawnZ += this.rand.nextInt(8) - this.rand.nextInt(8);
		}

	}

	private boolean findSpawn(int i1, int i2) {
		return this.getFirstUncoveredBlock(i1, i2) == Block.sand.blockID;
	}

    private int getFirstUncoveredBlock(int x, int z) {
        int i3;
        for(i3 = 63; this.getBlockId(x, i3 + 1, z) != 0; ++i3) {
        }

        return this.getBlockId(x, i3, z);
    }

    public void spawnPlayerWithLoadedChunks(EntityPlayer entityPlayer) {
        try {
            if(this.nbtCompoundPlayer != null) {
                entityPlayer.readFromNBT(this.nbtCompoundPlayer);
                this.nbtCompoundPlayer = null;
            }

            this.spawnEntityInWorld(entityPlayer);
        } catch (Exception exception3) {
            exception3.printStackTrace();
        }

    }
    public void saveWorld(boolean saveWorldIndirectly, IProgressUpdate loadingScreen) {
        if(this.chunkProvider.canSave()) {
            if(loadingScreen != null) {
                loadingScreen.displayProgressMessage("Saving level");
            }

            this.saveLevel();
            if(loadingScreen != null) {
                loadingScreen.displayLoadingString("Saving chunks");
            }

            this.chunkProvider.saveChunks(saveWorldIndirectly, loadingScreen);
        }
    }

    private void saveLevel() {
        NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
        nBTTagCompound1.setLong("RandomSeed", this.randomSeed);
        nBTTagCompound1.setInteger("SpawnX", this.spawnX);
        nBTTagCompound1.setInteger("SpawnY", this.spawnY);
        nBTTagCompound1.setInteger("SpawnZ", this.spawnZ);
        nBTTagCompound1.setLong("Time", this.worldTime);
        nBTTagCompound1.setLong("SizeOnDisk", this.sizeOnDisk);
        nBTTagCompound1.setBoolean("SnowCovered", this.snowCovered);
        nBTTagCompound1.setLong("LastPlayed", EagRuntime.currentTimeMillis());
        EntityPlayer entityPlayer2 = null;
        if(this.playerEntities.size() > 0) {
            entityPlayer2 = (EntityPlayer)this.playerEntities.get(0);
        }

        NBTTagCompound nBTTagCompound3;
        if(entityPlayer2 != null) {
            nBTTagCompound3 = new NBTTagCompound();
            entityPlayer2.writeToNBT(nBTTagCompound3);
            nBTTagCompound1.setCompoundTag("Player", nBTTagCompound3);
        }

        nBTTagCompound3 = new NBTTagCompound();
        nBTTagCompound3.setTag("Data", nBTTagCompound1);

        VFile2 file6 = new VFile2(this.saveDirectory, "level.dat_new");
        try (OutputStream fos = file6.getOutputStream()) {
            VFile2 file3 = new VFile2(this.saveDirectory, "level.dat_old");
            VFile2 file4 = new VFile2(this.saveDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(nBTTagCompound3, fos);
            if(file3.exists()) {
                file3.delete();
            }

            file4.renameTo(file3);
            if(file4.exists()) {
                file4.delete();
            }

            file6.renameTo(file4);
            if(file6.exists()) {
                file6.delete();
            }

        } catch (IOException exception5) {
            exception5.printStackTrace();
        }
	}

    public boolean saveWorld(int i1) {
        if(!this.chunkProvider.canSave()) {
            return true;
        } else {
            if(i1 == 0) {
                this.saveLevel();
            }

            return this.chunkProvider.saveChunks(false, (IProgressUpdate)null);
        }
    }

    public int getBlockId(int x, int y, int z) {
        return x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000 ? (y <= 0 ? 0 : (y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15))) : 0;
    }

    public boolean blockExists(int x, int y, int z) {
        return y >= 0 && y < 128 ? this.chunkExists(x >> 4, z >> 4) : false;
    }

    public boolean checkChunksExist(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if(maxY >= 0 && minY < 128) {
            minX >>= 4;
            minY >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxY >>= 4;
            maxZ >>= 4;

            for(minX = minX; minX <= maxX; ++minX) {
                for(minY = minZ; minY <= maxZ; ++minY) {
                    if(!this.chunkExists(minX, minY)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

	private boolean chunkExists(int chunkX, int chunkZ) {
		return this.chunkProvider.chunkExists(chunkX, chunkZ);
	}

    public Chunk getChunkFromBlockCoords(int x, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4);
	}

    public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
		return this.chunkProvider.provideChunk(chunkX, chunkZ);
	}

    public boolean setBlockAndMetadata(int x, int y, int z, int blockID, int metadata) {
        if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if(y < 0) {
                return false;
            } else if(y >= 128) {
                return false;
            } else {
                Chunk chunk6 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                return chunk6.setBlockIDWithMetadata(x & 15, y, z & 15, blockID, metadata);
            }
        } else {
            return false;
        }
    }

	public boolean setBlock(int x, int y, int z, int blockID) {
        if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if(y < 0) {
                return false;
            } else if(y >= 128) {
                return false;
            } else {
                Chunk chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                return chunk5.setBlockID(x & 15, y, z & 15, blockID);
            }
        } else {
            return false;
        }
    }

	public Material getBlockMaterial(int x, int y, int z) {
		int i4 = this.getBlockId(x, y, z);
		return i4 == 0 ? Material.air : Block.blocksList[i4].material;
	}

	public int getBlockMetadata(int x, int y, int z) {
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

    public void setBlockMetadataWithNotify(int xCoord, int yCoord, int zCoord, int metadataValue) {
        this.setBlockMetadata(xCoord, yCoord, zCoord, metadataValue);
    }

    public boolean setBlockMetadata(int xCoord, int yCoord, int zCoord, int metadataValue) {
        if(xCoord >= -32000000 && zCoord >= -32000000 && xCoord < 32000000 && zCoord <= 32000000) {
            if(yCoord < 0) {
                return false;
            } else if(yCoord >= 128) {
                return false;
            } else {
                Chunk chunk5 = this.getChunkFromChunkCoords(xCoord >> 4, zCoord >> 4);
                xCoord &= 15;
                zCoord &= 15;
                chunk5.setBlockMetadata(xCoord, yCoord, zCoord, metadataValue);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean setBlockWithNotify(int x, int y, int z, int blockID) {
        if(this.setBlock(x, y, z, blockID)) {
            this.markBlockNeedsUpdate(x, y, z, blockID);
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int blockID, int metadata) {
        if(this.setBlockAndMetadata(x, y, z, blockID, metadata)) {
            this.markBlockNeedsUpdate(x, y, z, blockID);
            return true;
        } else {
            return false;
        }
    }

    private void markBlockNeedsUpdate(int x, int y, int z, int blockID) {
        for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
            ((IWorldAccess)this.worldAccesses.get(i5)).markBlockAndNeighborsNeedsUpdate(x, y, z);
        }

        this.notifyBlocksOfNeighborChange(x, y, z, blockID);
    }

	public void markBlocksDirtyVertical(int x, int z, int minY, int maxY) {
		if(minY > maxY) {
			int i5 = maxY;
			maxY = minY;
			minY = i5;
		}

		this.markBlocksDirty(x, minY, z, x, maxY, z);
	}

	public void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).markBlockRangeNeedsUpdate(minX, minY, minZ, maxX, maxY, maxZ);
		}

	}

	public void notifyBlocksOfNeighborChange(int x, int y, int z, int blockID) {
		this.notifyBlockOfNeighborChange(x - 1, y, z, blockID);
		this.notifyBlockOfNeighborChange(x + 1, y, z, blockID);
		this.notifyBlockOfNeighborChange(x, y - 1, z, blockID);
		this.notifyBlockOfNeighborChange(x, y + 1, z, blockID);
		this.notifyBlockOfNeighborChange(x, y, z - 1, blockID);
		this.notifyBlockOfNeighborChange(x, y, z + 1, blockID);
	}

	private void notifyBlockOfNeighborChange(int x, int y, int z, int blockID) {
        if(!this.editingBlocks) {
            Block block5;
            if((block5 = Block.blocksList[this.getBlockId(x, y, z)]) != null) {
                block5.onNeighborBlockChange(this, x, y, z, blockID);
            }

        }
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
	}

	public int getBlockLightValue(int x, int y, int z) {
		return this.getBlockLightValue_do(x, y, z, true);
	}

    private int getBlockLightValue_do(int x, int y, int z, boolean update) {
        if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            int i5;
            if(update) {
                i5 = this.getBlockId(x, y, z);
                if(i5 == Block.stairSingle.blockID || i5 == Block.tilledField.blockID) {
                    int i6 = this.getBlockLightValue_do(x, y + 1, z, false);
                    int i7 = this.getBlockLightValue_do(x + 1, y, z, false);
                    int i8 = this.getBlockLightValue_do(x - 1, y, z, false);
                    int i9 = this.getBlockLightValue_do(x, y, z + 1, false);
                    int i10 = this.getBlockLightValue_do(x, y, z - 1, false);
                    if(i7 > i6) {
                        i6 = i7;
                    }

                    if(i8 > i6) {
                        i6 = i8;
                    }

                    if(i9 > i6) {
                        i6 = i9;
                    }

                    if(i10 > i6) {
                        i6 = i10;
                    }

                    return i6;
                }
            }

            if(y < 0) {
                return 0;
            } else if(y >= 128) {
                i5 = 15 - this.skylightSubtracted;
                if(i5 < 0) {
                    i5 = 0;
                }

                return i5;
            } else {
                Chunk chunk11 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                x &= 15;
                z &= 15;
                return chunk11.getBlockLightValue(x, y, z, this.skylightSubtracted);
            }
        } else {
            return 15;
        }
    }

	public boolean canExistingBlockSeeTheSky(int x, int y, int z) {
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

    public int getHeightValue(int x, int z) {
        if(x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if(!this.chunkExists(x >> 4, z >> 4)) {
                return 0;
            } else {
                Chunk chunk3 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                return chunk3.getHeightValue(x & 15, z & 15);
            }
        } else {
            return 0;
        }
    }

	public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
        if(this.blockExists(x, y, z)) {
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
				this.scheduleLightingUpdate_do(skyBlock, x, y, z, x, y, z);
			}

		}
	}

    public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
        if(y >= 0 && y < 128 && x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            int i5 = x >> 4;
            int i6 = z >> 4;
            if(!this.chunkExists(i5, i6)) {
                return 0;
            } else {
                Chunk chunk7 = this.getChunkFromChunkCoords(i5, i6);
                return chunk7.getSavedLightValue(skyBlock, x & 15, y, z & 15);
            }
        } else {
            return skyBlock.defaultLightValue;
        }
    }

	public void setLightValue(EnumSkyBlock enumSkyBlock, int xCoord, int yCoord, int zCoord, int lightValue) {
		if(xCoord >= -32000000 && zCoord >= -32000000 && xCoord < 32000000 && zCoord <= 32000000) {
			if(yCoord >= 0) {
				if(yCoord < 128) {
					if(this.chunkExists(xCoord >> 4, zCoord >> 4)) {
						Chunk chunk6 = this.getChunkFromChunkCoords(xCoord >> 4, zCoord >> 4);
						chunk6.setLightValue(enumSkyBlock, xCoord & 15, yCoord, zCoord & 15, lightValue);

						for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
							((IWorldAccess)this.worldAccesses.get(i7)).markBlockAndNeighborsNeedsUpdate(xCoord, yCoord, zCoord);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int x, int y, int z) {
		return lightBrightnessTable[this.getBlockLightValue(x, y, z)];
	}

	public boolean isDaytime() {
        return this.skylightSubtracted < 4;
	}

    public MovingObjectPosition rayTraceBlocks(Vec3D vector1, Vec3D vector2) {
        return this.rayTraceBlocks_do(vector1, vector2, false);
    }

    public MovingObjectPosition rayTraceBlocks_do(Vec3D vector1, Vec3D vector2, boolean flag) {
        if(!Double.isNaN(vector1.xCoord) && !Double.isNaN(vector1.yCoord) && !Double.isNaN(vector1.zCoord)) {
            if(!Double.isNaN(vector2.xCoord) && !Double.isNaN(vector2.yCoord) && !Double.isNaN(vector2.zCoord)) {
                int i4 = MathHelper.floor_double(vector2.xCoord);
                int i5 = MathHelper.floor_double(vector2.yCoord);
                int i6 = MathHelper.floor_double(vector2.zCoord);
                int i7 = MathHelper.floor_double(vector1.xCoord);
                int i8 = MathHelper.floor_double(vector1.yCoord);
                int i9 = MathHelper.floor_double(vector1.zCoord);
                int i10 = 20;

                while(i10-- >= 0) {
                    if(Double.isNaN(vector1.xCoord) || Double.isNaN(vector1.yCoord) || Double.isNaN(vector1.zCoord)) {
                        return null;
                    }

                    if(i7 == i4 && i8 == i5 && i9 == i6) {
                        return null;
                    }

                    double d11 = 999.0D;
                    double d13 = 999.0D;
                    double d15 = 999.0D;
                    if(i4 > i7) {
                        d11 = (double)i7 + 1.0D;
                    }

                    if(i4 < i7) {
                        d11 = (double)i7;
                    }

                    if(i5 > i8) {
                        d13 = (double)i8 + 1.0D;
                    }

                    if(i5 < i8) {
                        d13 = (double)i8;
                    }

                    if(i6 > i9) {
                        d15 = (double)i9 + 1.0D;
                    }

                    if(i6 < i9) {
                        d15 = (double)i9;
                    }

                    double d17 = 999.0D;
                    double d19 = 999.0D;
                    double d21 = 999.0D;
                    double d23 = vector2.xCoord - vector1.xCoord;
                    double d25 = vector2.yCoord - vector1.yCoord;
                    double d27 = vector2.zCoord - vector1.zCoord;
                    if(d11 != 999.0D) {
                        d17 = (d11 - vector1.xCoord) / d23;
                    }

                    if(d13 != 999.0D) {
                        d19 = (d13 - vector1.yCoord) / d25;
                    }

                    if(d15 != 999.0D) {
                        d21 = (d15 - vector1.zCoord) / d27;
                    }

                    byte b29;
                    if(d17 < d19 && d17 < d21) {
                        if(i4 > i7) {
                            b29 = 4;
                        } else {
                            b29 = 5;
                        }

                        vector1.xCoord = d11;
                        vector1.yCoord += d25 * d17;
                        vector1.zCoord += d27 * d17;
                    } else if(d19 < d21) {
                        if(i5 > i8) {
                            b29 = 0;
                        } else {
                            b29 = 1;
                        }

                        vector1.xCoord += d23 * d19;
                        vector1.yCoord = d13;
                        vector1.zCoord += d27 * d19;
                    } else {
                        if(i6 > i9) {
                            b29 = 2;
                        } else {
                            b29 = 3;
                        }

                        vector1.xCoord += d23 * d21;
                        vector1.yCoord += d25 * d21;
                        vector1.zCoord = d15;
                    }

                    Vec3D vec3D30;
                    i7 = (int)((vec3D30 = Vec3D.createVector(vector1.xCoord, vector1.yCoord, vector1.zCoord)).xCoord = (double)MathHelper.floor_double(vector1.xCoord));
                    if(b29 == 5) {
                        --i7;
                        ++vec3D30.xCoord;
                    }

                    i8 = (int)(vec3D30.yCoord = (double)MathHelper.floor_double(vector1.yCoord));
                    if(b29 == 1) {
                        --i8;
                        ++vec3D30.yCoord;
                    }

                    i9 = (int)(vec3D30.zCoord = (double)MathHelper.floor_double(vector1.zCoord));
                    if(b29 == 3) {
                        --i9;
                        ++vec3D30.zCoord;
                    }

                    int i31 = this.getBlockId(i7, i8, i9);
                    int i12 = this.getBlockMetadata(i7, i8, i9);
                    Block block33 = Block.blocksList[i31];
                    if(i31 > 0 && block33.canCollideCheck(i12, flag)) {
                        MovingObjectPosition movingObjectPosition34 = block33.collisionRayTrace(this, i7, i8, i9, vector1, vector2);
                        if(movingObjectPosition34 != null) {
                            return movingObjectPosition34;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

	public void playSoundAtEntity(Entity entity, String soundName, float volume, float pitch) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playSound(soundName, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, volume, pitch);
		}

	}

	public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
		for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
			((IWorldAccess)this.worldAccesses.get(i10)).playSound(soundName, x, y, z, volume, pitch);
		}

	}

	public void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(particleName, x, y, z, motionX, motionY, motionZ);
		}

	}

    public void spawnEntityInWorld(Entity entity) {
        int i2 = MathHelper.floor_double(entity.posX / 16.0D);
        int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
        boolean z4 = false;
        if(entity instanceof EntityPlayer) {
            this.playerEntities.add((EntityPlayer)entity);
            System.out.println("Player count: " + this.playerEntities.size());
            z4 = true;
        }

        if(!z4 && !this.chunkExists(i2, i3)) {
            System.out.println("Failed to add entity " + entity + " because the chunk wasn\'t loaded");
        } else {
            this.getChunkFromChunkCoords(i2, i3).addEntity(entity);
            this.loadedEntityList.add(entity);

            for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
                ((IWorldAccess)this.worldAccesses.get(i5)).obtainEntitySkin(entity);
            }
        }

    }

    public void setEntityDead(Entity entity) {
        entity.setEntityDead();
        if(entity instanceof EntityPlayer) {
            this.playerEntities.remove((EntityPlayer)entity);
        }

        System.out.println("Player count: " + this.playerEntities.size());
    }

	public void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	public void removeWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.remove(worldAccess);
	}

    public List getCollidingBoundingBoxes(Entity entity, AxisAlignedBB aabb) {
        this.collidingBoundingBoxes.clear();
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
                    if((block11 = Block.blocksList[this.getBlockId(i3, i9, i10)]) != null) {
                        block11.getCollidingBoundingBoxes(this, i3, i9, i10, aabb, this.collidingBoundingBoxes);
                    }
                }
            }
        }

        List list12 = this.getEntitiesWithinAABBExcludingEntity(entity, aabb.expand(0.25D, 0.25D, 0.25D));

        for(int i13 = 0; i13 < list12.size(); ++i13) {
            if((aabb = ((Entity)list12.get(i13)).getBoundingBox()) != null) {
                this.collidingBoundingBoxes.add(aabb);
            }

            if((aabb = entity.getCollisionBox((Entity)list12.get(i13))) != null) {
                this.collidingBoundingBoxes.add(aabb);
            }
        }

        return this.collidingBoundingBoxes;
    }

    public int calculateSkylightSubtracted(float partialTicks) {
        float f2 = this.getCelestialAngle(partialTicks);
        float f3 = 1.0F - (MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
        if(f3 < 0.0F) {
            f3 = 0.0F;
        }

        if(f3 > 1.0F) {
            f3 = 1.0F;
        }

        return (int)(f3 * 11.0F);
    }

	public Vec3D getSkyColor(float partialTicks) {
		float f2 = this.getCelestialAngle(partialTicks);
		float f3 = MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = (float)(this.skyColor >> 16 & 255L) / 255.0F;
		float f5 = (float)(this.skyColor >> 8 & 255L) / 255.0F;
		float f6 = (float)(this.skyColor & 255L) / 255.0F;
		f4 *= f3;
		f5 *= f3;
		f6 *= f3;
		return Vec3D.createVector((double)f4, (double)f5, (double)f6);
	}

    public float getCelestialAngle(float partialTicks) {
        int i2 = (int)(this.worldTime % 24000L);
        float f3 = ((float)i2 + partialTicks) / 24000.0F - 0.25F;
        if(f3 < 0.0F) {
            ++f3;
        }

        if(f3 > 1.0F) {
            --f3;
        }

        float f4 = f3;
        f3 = 1.0F - (float)((Math.cos((double)f3 * Math.PI) + 1.0D) / 2.0D);
        f3 = f4 + (f3 - f4) / 3.0F;
        return f3;
    }

	public Vec3D getCloudColor(float partialTicks) {
		float f2 = this.getCelestialAngle(partialTicks);
		float f3 = MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = (float)(this.cloudColor >> 16 & 255L) / 255.0F;
		float f5 = (float)(this.cloudColor >> 8 & 255L) / 255.0F;
		float f6 = (float)(this.cloudColor & 255L) / 255.0F;
		f4 *= f3 * 0.9F + 0.1F;
		f5 *= f3 * 0.9F + 0.1F;
		f6 *= f3 * 0.85F + 0.15F;
		return Vec3D.createVector((double)f4, (double)f5, (double)f6);
	}

	public Vec3D getFogColor(float partialTicks) {
		float f2 = this.getCelestialAngle(partialTicks);
		float f3 = MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = (float)(this.fogColor >> 16 & 255L) / 255.0F;
		float f5 = (float)(this.fogColor >> 8 & 255L) / 255.0F;
		float f6 = (float)(this.fogColor & 255L) / 255.0F;
		f4 *= f3 * 0.94F + 0.06F;
		f5 *= f3 * 0.94F + 0.06F;
		f6 *= f3 * 0.91F + 0.09F;
		return Vec3D.createVector((double)f4, (double)f5, (double)f6);
	}

    public int getPrecipitationHeight(int x, int z) {
        return this.getChunkFromBlockCoords(x, z).getHeightValue(x & 15, z & 15);
    }

	public float getStarBrightness(float partialTicks) {
		float f2 = this.getCelestialAngle(partialTicks);
		float f3 = 1.0F - (MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.75F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		return f3 * f3 * 0.5F;
	}

    public void scheduleBlockUpdate(int x, int y, int z, int blockID) {
        NextTickListEntry nextTickListEntry5 = new NextTickListEntry(x, y, z, blockID);
        if(this.checkChunksExist(x - 8, y - 8, z - 8, x + 8, y + 8, z + 8)) {
            if(blockID > 0) {
                nextTickListEntry5.setScheduledTime((long)Block.blocksList[blockID].tickRate() + this.worldTime);
            }

            if(!this.scheduledTickSet.contains(nextTickListEntry5)) {
                this.scheduledTickSet.add(nextTickListEntry5);
                this.scheduledTickTreeSet.add(nextTickListEntry5);
            }
        }

    }

    public void updateEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        int i1;
        int i3;
        for(i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
            IWorldAccess iWorldAccess2 = (IWorldAccess)this.worldAccesses.get(i1);

            for(i3 = 0; i3 < this.unloadedEntityList.size(); ++i3) {
                iWorldAccess2.releaseEntitySkin((Entity)this.unloadedEntityList.get(i3));
            }
        }

        this.unloadedEntityList.clear();

        for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
            Entity entity5;
            if((entity5 = (Entity)this.loadedEntityList.get(i1)).ridingEntity != null) {
                if(!entity5.ridingEntity.isDead && entity5.ridingEntity.riddenByEntity == entity5) {
                    continue;
                }

                entity5.ridingEntity.riddenByEntity = null;
                entity5.ridingEntity = null;
            }

            if(!entity5.isDead) {
                this.updateEntity(entity5);
            }

            if(entity5.isDead) {
                i3 = MathHelper.floor_double(entity5.posX / 16.0D);
                int i4 = MathHelper.floor_double(entity5.posZ / 16.0D);
                if(this.chunkExists(i3, i4)) {
                    this.getChunkFromChunkCoords(i3, i4).removeEntity(entity5);
                }

                this.loadedEntityList.remove(i1--);

                for(i3 = 0; i3 < this.worldAccesses.size(); ++i3) {
                    ((IWorldAccess)this.worldAccesses.get(i3)).releaseEntitySkin(entity5);
                }
            }
        }

        for(i1 = 0; i1 < this.loadedTileEntityList.size(); ++i1) {
            ((TileEntity)this.loadedTileEntityList.get(i1)).updateEntity();
        }

    }

    private void updateEntity(Entity entity) {
        int i2 = MathHelper.floor_double(entity.posX);
        int i4 = MathHelper.floor_double(entity.posZ);
        if(this.checkChunksExist(i2 - 16, 0, i4 - 16, i2 + 16, 128, i4 + 16)) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
            entity.prevRotationYaw = entity.rotationYaw;
            entity.prevRotationPitch = entity.rotationPitch;
            i2 = MathHelper.floor_double(entity.posX / 16.0D);
            int i3 = MathHelper.floor_double(entity.posY / 16.0D);
            i4 = MathHelper.floor_double(entity.posZ / 16.0D);
            if(entity.ridingEntity != null) {
                entity.updateRidden();
            } else {
                entity.onUpdate();
            }

            int i5 = MathHelper.floor_double(entity.posX / 16.0D);
            int i6 = MathHelper.floor_double(entity.posY / 16.0D);
            int i7 = MathHelper.floor_double(entity.posZ / 16.0D);
            if(i2 != i5 || i3 != i6 || i4 != i7) {
                if(this.chunkExists(i2, i4)) {
                    this.getChunkFromChunkCoords(i2, i4).removeEntityAtIndex(entity, i3);
                }

                if(this.chunkExists(i5, i7)) {
                    this.getChunkFromChunkCoords(i5, i7).addEntity(entity);
                } else {
                    entity.setEntityDead();
                }
            }

            if(entity.riddenByEntity != null) {
                if(!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
                    this.updateEntity(entity.riddenByEntity);
                } else {
                    entity.riddenByEntity.ridingEntity = null;
                    entity.riddenByEntity = null;
                }
            }

            if(Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
                entity.posX = entity.lastTickPosX;
            }

            if(Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
                entity.posY = entity.lastTickPosY;
            }

            if(Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
                entity.posZ = entity.lastTickPosZ;
            }

            if(Double.isNaN((double)entity.rotationPitch) || Double.isInfinite((double)entity.rotationPitch)) {
                entity.rotationPitch = entity.prevRotationPitch;
            }

            if(Double.isNaN((double)entity.rotationYaw) || Double.isInfinite((double)entity.rotationYaw)) {
                entity.rotationYaw = entity.prevRotationYaw;
            }

        }
    }

    public boolean checkIfAABBIsClear(AxisAlignedBB aabb) {
        List list4 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, aabb);

        for(int i2 = 0; i2 < list4.size(); ++i2) {
            Entity entity3;
            if(!(entity3 = (Entity)list4.get(i2)).isDead && entity3.preventEntitySpawning) {
                return false;
            }
        }

        return true;
    }

	public boolean getIsAnyLiquid(AxisAlignedBB aabb) {
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
					if((block9 = Block.blocksList[this.getBlockId(i10, i2, i8)]) != null && block9.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB aabb) {
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

    public boolean handleMaterialAcceleration(AxisAlignedBB aabb, Material material, Entity entity) {
        int i4 = MathHelper.floor_double(aabb.minX);
        int i5 = MathHelper.floor_double(aabb.maxX + 1.0D);
        int i6 = MathHelper.floor_double(aabb.minY);
        int i7 = MathHelper.floor_double(aabb.maxY + 1.0D);
        int i8 = MathHelper.floor_double(aabb.minZ);
        int i18 = MathHelper.floor_double(aabb.maxZ + 1.0D);
        boolean z9 = false;
        Vec3D vec3D10 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

        for(i4 = i4; i4 < i5; ++i4) {
            for(int i11 = i6; i11 < i7; ++i11) {
                for(int i12 = i8; i12 < i18; ++i12) {
                    Block block13;
                    if((block13 = Block.blocksList[this.getBlockId(i4, i11, i12)]) != null && block13.material == material) {
                        double d16 = (double)((float)(i11 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(i4, i11, i12)));
                        if((double)i7 >= d16) {
                            z9 = true;
                            block13.velocityToAddToEntity(this, i4, i11, i12, entity, vec3D10);
                        }
                    }
                }
            }
        }

        if(vec3D10.lengthVector() > 0.0D) {
            vec3D10 = vec3D10.normalize();
            entity.motionX += vec3D10.xCoord * 0.004D;
            entity.motionY += vec3D10.yCoord * 0.004D;
            entity.motionZ += vec3D10.zCoord * 0.004D;
        }

        return z9;
    }

	public boolean isMaterialInBB(AxisAlignedBB aabb, Material material) {
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
					if((block10 = Block.blocksList[this.getBlockId(i3, i8, i9)]) != null && block10.material == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void createExplosion(Entity entity, double d2, double d4, double d6, float f8) {
		(new Explosion()).doExplosion(this, entity, d2, d4, d6, f8);
	}

	public float getBlockDensity(Vec3D vector, AxisAlignedBB aabb) {
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
                    if(this.rayTraceBlocks(Vec3D.createVector(d14, d16, d18), vector) == null) {
                        ++i9;
                    }

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public void extinguishFire(int x, int y, int z, int side) {
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
			this.setBlockWithNotify(x, y, z, 0);
		}

	}

	public Entity createDebugPlayer(Class cVar) {
		return null;
	}

	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public TileEntity getBlockTileEntity(int x, int y, int z) {
		Chunk chunk4;
		return (chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4)) != null ? chunk4.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
	}

	public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		Chunk chunk5;
		if((chunk5 = this.getChunkFromChunkCoords(x >> 4, z >> 4)) != null) {
			chunk5.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
		}

	}

    public void removeBlockTileEntity(int x, int y, int z) {
        Chunk chunk4 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        if(chunk4 != null) {
            chunk4.removeChunkBlockTileEntity(x & 15, y, z & 15);
        }

    }

	public boolean isBlockNormalCube(int x, int y, int z) {
		Block x1;
		return (x1 = Block.blocksList[this.getBlockId(x, y, z)]) == null ? false : x1.isOpaqueCube();
	}

    public void saveWorldIndirectly(IProgressUpdate loadscreen) {
        this.saveWorld(true, loadscreen);
    }

    public boolean updatingLighting() {
        int i1 = 100000;

        while(this.lightingToUpdate.size() > 0) {
            --i1;
            if(i1 <= 0) {
                return true;
            }

            ((MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1)).updateLight(this);
        }

        return false;
    }

	public void scheduleLightingUpdate_do(EnumSkyBlock enumSkyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int i8 = this.lightingToUpdate.size();
		int i9 = 4;
		if(i9 > i8) {
			i9 = i8;
		}

		for(int i10 = 0; i10 < i9; ++i10) {
			MetadataChunkBlock metadataChunkBlock11 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i10 - 1);
			if(metadataChunkBlock11.skyBlock == enumSkyBlock && metadataChunkBlock11.getLightUpdated(minX, minY, minZ, maxX, maxY, maxZ)) {
				return;
			}
		}

		this.lightingToUpdate.add(new MetadataChunkBlock(enumSkyBlock, minX, minY, minZ, maxX, maxY, maxZ));
		if(this.lightingToUpdate.size() > 100000) {
			while(this.lightingToUpdate.size() > 50000) {
				this.updatingLighting();
			}
		}

	}

    public void calculateInitialSkylight() {
        int i1 = this.calculateSkylightSubtracted(1.0F);
        if(i1 != this.skylightSubtracted) {
            this.skylightSubtracted = i1;
        }

    }

    public void tick() {
        this.chunkProvider.unload100OldestChunks();
        int i1;
        if((i1 = this.calculateSkylightSubtracted(1.0F)) != this.skylightSubtracted) {
            this.skylightSubtracted = i1;

            for(i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
                ((IWorldAccess)this.worldAccesses.get(i1)).updateAllRenderers();
            }
        }

        ++this.worldTime;
        if(this.worldTime % 20L == 0L) {
            this.saveWorld(false, (IProgressUpdate)null);
        }

        this.tickUpdates(false);
        this.updateBlocksAndPlayCaveSounds();
    }

    protected void updateBlocksAndPlayCaveSounds() {
        this.positionsToUpdate.clear();

        int i3;
        int i4;
        int i6;
        int i7;
        for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
            EntityPlayer entityPlayer2 = (EntityPlayer)this.playerEntities.get(i1);
            i3 = MathHelper.floor_double(entityPlayer2.posX / 16.0D);
            i4 = MathHelper.floor_double(entityPlayer2.posZ / 16.0D);
            byte b5 = 9;

            for(i6 = -b5; i6 <= b5; ++i6) {
                for(i7 = -b5; i7 <= b5; ++i7) {
                    this.positionsToUpdate.add(new ChunkCoordIntPair(i6 + i3, i7 + i4));
                }
            }
        }

        if(this.soundCounter > 0) {
            --this.soundCounter;
        }

        Iterator iterator12 = this.positionsToUpdate.iterator();

        while(iterator12.hasNext()) {
            ChunkCoordIntPair chunkCoordIntPair13 = (ChunkCoordIntPair)iterator12.next();
            i3 = chunkCoordIntPair13.chunkXPos * 16;
            i4 = chunkCoordIntPair13.chunkZPos * 16;
            Chunk chunk14 = this.getChunkFromChunkCoords(chunkCoordIntPair13.chunkXPos, chunkCoordIntPair13.chunkZPos);
            int i8;
            int i9;
            int i10;
            if(this.soundCounter == 0) {
                this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
                i6 = this.updateLCG >> 2;
                i7 = i6 & 15;
                i8 = i6 >> 8 & 15;
                i9 = i6 >> 16 & 127;
                i10 = chunk14.getBlockID(i7, i9, i8);
                i7 += i3;
                i8 += i4;
                if(i10 == 0 && this.getBlockLightValue(i7, i9, i8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, i7, i9, i8) <= 0) {
                    EntityPlayer entityPlayer11 = this.getClosestPlayer((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, 8.0D);
                    if(entityPlayer11 != null && entityPlayer11.getDistanceSq((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D) > 4.0D) {
                        this.playSoundEffect((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
                        this.soundCounter = this.rand.nextInt(12000) + 6000;
                    }
                }
            }

            if(this.snowCovered && this.rand.nextInt(4) == 0) {
                this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
                i6 = this.updateLCG >> 2;
                i7 = i6 & 15;
                i8 = i6 >> 8 & 15;
                i9 = chunk14.getHeightValue(i7, i8);
                if(chunk14.getSavedLightValue(EnumSkyBlock.Block, i7, i9, i8) < 10 && chunk14.getBlockID(i7, i9, i8) == 0) {
                    i10 = chunk14.getBlockID(i7, i9 - 1, i8);
                    if(i10 != 0 && i10 != Block.ice.blockID && Block.blocksList[i10].material.getIsSolid()) {
                        this.setBlockWithNotify(i7 + i3, i9, i8 + i4, Block.snow.blockID);
                    }

                    if(i10 == Block.waterStill.blockID && chunk14.getBlockMetadata(i7, i9 - 1, i8) == 0) {
                        this.setBlockWithNotify(i7 + i3, i9 - 1, i8 + i4, Block.ice.blockID);
                    }
                }
            }

            for(i6 = 0; i6 < 80; ++i6) {
                this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
                i7 = this.updateLCG >> 2;
                i8 = i7 & 15;
                i9 = i7 >> 8 & 15;
                i10 = i7 >> 16 & 127;
                int i15 = chunk14.getBlockID(i8, i10, i9);
                if(Block.tickOnLoad[i15]) {
                    Block.blocksList[i15].updateTick(this, i8 + i3, i10, i9 + i4, this.rand);
                }
            }
        }

    }

    public boolean tickUpdates(boolean skipUpdate) {
        int i2;
        if((i2 = this.scheduledTickTreeSet.size()) != this.scheduledTickSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if(i2 > 1000) {
                i2 = 1000;
            }

            for(int i3 = 0; i3 < i2; ++i3) {
                NextTickListEntry nextTickListEntry4 = (NextTickListEntry)this.scheduledTickTreeSet.first();
                if(!skipUpdate && nextTickListEntry4.scheduledTime > this.worldTime) {
                    break;
                }

                this.scheduledTickTreeSet.remove(nextTickListEntry4);
                this.scheduledTickSet.remove(nextTickListEntry4);
                int i5;
                if(this.checkChunksExist(nextTickListEntry4.xCoord - 8, nextTickListEntry4.yCoord - 8, nextTickListEntry4.zCoord - 8, nextTickListEntry4.xCoord + 8, nextTickListEntry4.yCoord + 8, nextTickListEntry4.zCoord + 8) && (i5 = this.getBlockId(nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord)) == nextTickListEntry4.blockID && i5 > 0) {
                    Block.blocksList[i5].updateTick(this, nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord, this.rand);
                }
            }

            return this.scheduledTickTreeSet.size() != 0;
        }
    }

	public void randomDisplayUpdates(int x, int y, int z) {
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

	public List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB aabb) {
	    this.entitiesWithinAABBExcludingEntity.clear();
		int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);

		for(i3 = i3; i3 <= i4; ++i3) {
			for(int i8 = i5; i8 <= i6; ++i8) {
				if(this.chunkExists(i3, i8)) {
					this.getChunkFromChunkCoords(i3, i8).getEntitiesWithinAABBForEntity(entity, aabb, this.entitiesWithinAABBExcludingEntity);
				}
			}
		}

        return this.entitiesWithinAABBExcludingEntity;
	}

    public List getEntitiesWithinAABB(Class<? extends Entity> entityClass, AxisAlignedBB aabb) {
        int i3 = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
        int i4 = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
        int i5 = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
        int i6 = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
        ArrayList arrayList7 = new ArrayList();

        for(i3 = i3; i3 <= i4; ++i3) {
            for(int i8 = i5; i8 <= i6; ++i8) {
                if(this.chunkExists(i3, i8)) {
                    this.getChunkFromChunkCoords(i3, i8).getEntitiesOfTypeWithinAABB(entityClass, aabb, arrayList7);
                }
            }
        }

        return arrayList7;
    }

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void updateTileEntityChunkAndDoNothing(int x, int y, int z) {
	    if(this.blockExists(x, y, z)) {
			this.getChunkFromBlockCoords(x, z).setChunkModified();
		}

	}

	public int countEntities(Class entityClass) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			Entity entity4 = (Entity)this.loadedEntityList.get(i3);
			if(entityClass.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public void addLoadedEntities(List loadedEntities) {
		this.loadedEntityList.addAll(loadedEntities);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			IWorldAccess iWorldAccess3 = (IWorldAccess)this.worldAccesses.get(i2);

			for(int i4 = 0; i4 < loadedEntities.size(); ++i4) {
				iWorldAccess3.obtainEntitySkin((Entity)loadedEntities.get(i4));
			}
		}

	}

	public void unloadEntities(List unloadedEntities) {
        this.unloadedEntityList.addAll(unloadedEntities);
	}

	public void dropOldChunks() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

    public boolean canBlockBePlacedAt(int blockID, int x, int y, int z, boolean ignoreBoundingBox) {
        int i6 = this.getBlockId(x, y, z);
        Block block9 = Block.blocksList[i6];
        Block block7;
        AxisAlignedBB axisAlignedBB8 = (block7 = Block.blocksList[blockID]).getCollisionBoundingBoxFromPool(this, x, y, z);
        if(ignoreBoundingBox) {
            axisAlignedBB8 = null;
        }

        return (blockID > 0 && block9 == null || block9 == Block.waterMoving || block9 == Block.waterStill || block9 == Block.lavaMoving || block9 == Block.lavaStill || block9 == Block.fire) && (axisAlignedBB8 == null || this.checkIfAABBIsClear(axisAlignedBB8)) && block7.canPlaceBlockAt(this, x, y, z);
    }

    public PathEntity getPathToEntity(Entity entity, Entity entity2, float f3) {
        int i4 = MathHelper.floor_double(entity.posX);
        int i5 = MathHelper.floor_double(entity.posY);
        int i6 = MathHelper.floor_double(entity.posZ);
        int i7 = (int)(f3 + 16.0F);
        int i8 = i4 - i7;
        int i9 = i5 - i7;
        int i10 = i6 - i7;
        int i11 = i4 + i7;
        int i12 = i5 + i7;
        int i13 = i6 + i7;
        ChunkCache chunkCache14 = new ChunkCache(this, i8, i9, i10, i11, i12, i13);
        return (new Pathfinder(chunkCache14)).createEntityPathTo(entity, entity2, f3);
    }

    public PathEntity getEntityPathToXYZ(Entity entity, int xCoord, int yCoord, int zCoord, float f5) {
        int i6 = MathHelper.floor_double(entity.posX);
        int i7 = MathHelper.floor_double(entity.posY);
        int i8 = MathHelper.floor_double(entity.posZ);
        int i9 = (int)(f5 + 8.0F);
        int i10 = i6 - i9;
        int i11 = i7 - i9;
        int i12 = i8 - i9;
        int i13 = i6 + i9;
        int i14 = i7 + i9;
        int i15 = i8 + i9;
        ChunkCache chunkCache16 = new ChunkCache(this, i10, i11, i12, i13, i14, i15);
        return (new Pathfinder(chunkCache16)).createEntityPathTo(entity, xCoord, yCoord, zCoord, f5);
    }

    public boolean isBlockProvidingPowerTo(int x, int y, int z, int side) {
        int i5 = this.getBlockId(x, y, z);
        return i5 == 0 ? false : Block.blocksList[i5].isIndirectlyPoweringTo(this, x, y, z, side);
    }

    public boolean isBlockGettingPowered(int x, int y, int z) {
        return this.isBlockProvidingPowerTo(x, y - 1, z, 0) ? true : (this.isBlockProvidingPowerTo(x, y + 1, z, 1) ? true : (this.isBlockProvidingPowerTo(x, y, z - 1, 2) ? true : (this.isBlockProvidingPowerTo(x, y, z + 1, 3) ? true : (this.isBlockProvidingPowerTo(x - 1, y, z, 4) ? true : this.isBlockProvidingPowerTo(x + 1, y, z, 5)))));
    }

    public boolean isBlockIndirectlyProvidingPowerTo(int x, int y, int z, int side) {
        if(this.isBlockNormalCube(x, y, z)) {
            return this.isBlockGettingPowered(x, y, z);
        } else {
            int i5 = this.getBlockId(x, y, z);
            return i5 == 0 ? false : Block.blocksList[i5].isPoweringTo(this, x, y, z, side);
        }
    }

    public boolean isBlockIndirectlyGettingPowered(int x, int y, int z) {
        return this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5)))));
    }

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double distance) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, distance);
    }

    public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance) {
        double d9 = -1.0D;
        EntityPlayer entityPlayer11 = null;

        for(int i12 = 0; i12 < this.playerEntities.size(); ++i12) {
            EntityPlayer entityPlayer13 = (EntityPlayer)this.playerEntities.get(i12);
            double d14 = entityPlayer13.getDistanceSq(posX, posY, posZ);
            if((distance < 0.0D || d14 < distance * distance) && (d9 == -1.0D || d14 < d9)) {
                d9 = d14;
                entityPlayer11 = entityPlayer13;
            }
        }

        return entityPlayer11;
    }

	static {
        float f0 = 0.05F;

        for(int i1 = 0; i1 <= 15; ++i1) {
            float f2 = 1.0F - (float)i1 / 15.0F;
            lightBrightnessTable[i1] = (1.0F - f2) / (f2 * 3.0F + 1.0F) * (1.0F - f0) + f0;
        }

	}
}
