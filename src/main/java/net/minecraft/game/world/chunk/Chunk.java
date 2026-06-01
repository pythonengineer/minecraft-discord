package net.minecraft.game.world.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockContainer;
import net.minecraft.game.world.block.tileentity.TileEntity;

public class Chunk {
	public static boolean isLit;
	public byte[] blocks;
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray data;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	public int height;
	public final int xPosition;
	public final int zPosition;
	public Map chunkTileEntityMap;
	public List<Entity>[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean neverSave;
	public boolean isChunkRendered;
    public boolean hasEntities;
    public long lastSaveTime;

	public Chunk(World world, int chunkX, int chunkZ) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.isChunkRendered = false;
        this.hasEntities = false;
        this.lastSaveTime = 0L;
		this.worldObj = world;
		this.xPosition = chunkX;
		this.zPosition = chunkZ;
		this.heightMap = new byte[256];

		for(int i4 = 0; i4 < this.entities.length; ++i4) {
			this.entities[i4] = new ArrayList();
		}

	}

	public Chunk(World world, byte[] blockData, int chunkX, int chunkZ) {
		this(world, chunkX, chunkZ);
		this.blocks = blockData;
		this.data = new NibbleArray(blockData.length);
		this.skylightMap = new NibbleArray(blockData.length);
		this.blocklightMap = new NibbleArray(blockData.length);
	}

	public boolean isAtLocation(int chunkX, int chunkZ) {
		return chunkX == this.xPosition && chunkZ == this.zPosition;
	}

	public int getHeightValue(int x, int z) {
		return this.heightMap[z << 4 | x] & 255;
	}

	public void doNothing() {
	}

	public void generateSkylightMap() {
		int i1 = 127;

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.heightMap[i3 << 4 | i2] = -128;
				this.relightBlock(i2, 127, i3);
				if((this.heightMap[i3 << 4 | i2] & 255) < i1) {
					i1 = this.heightMap[i3 << 4 | i2] & 255;
				}
			}
		}

		this.height = i1;

		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.updateSkylight_do(i2, i3);
			}
		}

		this.isModified = true;
	}

	private void updateSkylight_do(int x, int z) {
		int i3 = this.getHeightValue(x, z);
		int i4 = this.xPosition * 16 + x;
		int i5 = this.zPosition * 16 + z;
		this.checkSkylightNeighborHeight(i4 - 1, i5, i3);
		this.checkSkylightNeighborHeight(i4 + 1, i5, i3);
		this.checkSkylightNeighborHeight(i4, i5 - 1, i3);
		this.checkSkylightNeighborHeight(i4, i5 + 1, i3);
	}

	private void checkSkylightNeighborHeight(int x, int z, int y) {
		int i4 = this.worldObj.getHeightValue(x, z);
		if(i4 > y) {
			this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, x, y, z, x, i4, z);
		} else if(i4 < y) {
			this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, x, i4, z, x, y, z);
		}

		this.isModified = true;
	}

	private void relightBlock(int x, int y, int z) {
		int i4 = this.heightMap[z << 4 | x] & 255;
		int i5 = i4;
		if(y > i4) {
			i5 = y;
		}

		while(i5 > 0 && Block.lightOpacity[this.getBlockID(x, i5 - 1, z)] == 0) {
			--i5;
		}

		if(i5 != i4) {
			this.worldObj.markBlocksDirtyVertical(x, z, i5, i4);
			this.heightMap[z << 4 | x] = (byte)i5;
			int i6;
			int i7;
			int i8;
			if(i5 < this.height) {
				this.height = i5;
			} else {
				i6 = 127;

				for(i7 = 0; i7 < 16; ++i7) {
					for(i8 = 0; i8 < 16; ++i8) {
						if((this.heightMap[i8 << 4 | i7] & 255) < i6) {
							i6 = this.heightMap[i8 << 4 | i7] & 255;
						}
					}
				}

				this.height = i6;
			}

			i6 = this.xPosition * 16 + x;
			i7 = this.zPosition * 16 + z;
			if(i5 < i4) {
				for(i8 = i5; i8 < i4; ++i8) {
					this.skylightMap.set(x, i8, z, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, i6, i4, i7, i6, i5, i7);

				for(i8 = i4; i8 < i5; ++i8) {
					this.skylightMap.set(x, i8, z, 0);
				}
			}

			i8 = 15;

			int i9;
			for(i9 = i5; i5 > 0 && i8 > 0; this.skylightMap.set(x, i5, z, i8)) {
				--i5;
				int i10 = Block.lightOpacity[this.getBlockID(x, i5, z)];
				if(i10 == 0) {
					i10 = 1;
				}

				i8 -= i10;
				if(i8 < 0) {
					i8 = 0;
				}
			}

			while(i5 > 0 && Block.lightOpacity[this.getBlockID(x, i5 - 1, z)] == 0) {
				--i5;
			}

			if(i5 != i9) {
				this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, i6 - 1, i5, i7 - 1, i6 + 1, i9, i7 + 1);
			}

			this.isModified = true;
		}
	}

	public int getBlockID(int x, int y, int z) {
		return this.blocks[x << 11 | z << 7 | y];
	}

    public boolean setBlockIDWithMetadata(int x, int y, int z, int blockID, int metadata) {
        byte b6 = (byte)blockID;
        int i7 = this.heightMap[z << 4 | x] & 255;
        int i8 = this.blocks[x << 11 | z << 7 | y] & 255;
        if(i8 == blockID) {
            return false;
        } else {
            int i9 = this.xPosition * 16 + x;
            int i10 = this.zPosition * 16 + z;
            this.blocks[x << 11 | z << 7 | y] = b6;
            if(i8 != 0) {
                Block.blocksList[i8].onBlockRemoval(this.worldObj, i9, y, i10);
            }

            this.data.set(x, y, z, metadata);
            if(Block.lightOpacity[b6] != 0) {
                if(y >= i7) {
                    this.relightBlock(x, y + 1, z);
                }
            } else if(y == i7 - 1) {
                this.relightBlock(x, y, z);
            }

            this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, i9, y, i10, i9, y, i10);
            this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Block, i9, y, i10, i9, y, i10);
            this.updateSkylight_do(x, z);
            if(blockID != 0) {
                Block.blocksList[blockID].onBlockAdded(this.worldObj, i9, y, i10);
            }

            this.isModified = true;
            return true;
        }
    }

    public boolean setBlockID(int x, int y, int z, int blockID) {
        byte b5 = (byte)blockID;
        int i6 = this.heightMap[z << 4 | x] & 255;
        int i7 = this.blocks[x << 11 | z << 7 | y] & 255;
        if(i7 == blockID) {
            return false;
        } else {
            int i8 = this.xPosition * 16 + x;
            int i9 = this.zPosition * 16 + z;
            this.blocks[x << 11 | z << 7 | y] = b5;
            if(i7 != 0) {
                Block.blocksList[i7].onBlockRemoval(this.worldObj, i8, y, i9);
            }

            this.data.set(x, y, z, 0);
            if(Block.lightOpacity[b5] != 0) {
                if(y >= i6) {
                    this.relightBlock(x, y + 1, z);
                }
            } else if(y == i6 - 1) {
                this.relightBlock(x, y, z);
            }

            this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Sky, i8, y, i9, i8, y, i9);
            this.worldObj.scheduleLightingUpdate_do(EnumSkyBlock.Block, i8, y, i9, i8, y, i9);
            this.updateSkylight_do(x, z);
            if(blockID != 0) {
                Block.blocksList[blockID].onBlockAdded(this.worldObj, i8, y, i9);
            }

            this.isModified = true;
            return true;
        }
    }

	public int getBlockMetadata(int x, int y, int z) {
		return this.data.get(x, y, z);
	}

	public void setBlockMetadata(int i1, int i2, int i3, int i4) {
		this.isModified = true;
		this.data.set(i1, i2, i3, i4);
	}

	public int getSavedLightValue(EnumSkyBlock enumSkyBlock, int i2, int i3, int i4) {
		return enumSkyBlock == EnumSkyBlock.Sky ? this.skylightMap.get(i2, i3, i4) : (enumSkyBlock == EnumSkyBlock.Block ? this.blocklightMap.get(i2, i3, i4) : 0);
	}

	public void setLightValue(EnumSkyBlock enumSkyBlock, int i2, int i3, int i4, int i5) {
		this.isModified = true;
		if(enumSkyBlock == EnumSkyBlock.Sky) {
			this.skylightMap.set(i2, i3, i4, i5);
		} else {
			if(enumSkyBlock != EnumSkyBlock.Block) {
				return;
			}

			this.blocklightMap.set(i2, i3, i4, i5);
		}

	}

	public int getBlockLightValue(int x, int y, int z, int skyLightSubtracted) {
		int i5;
		if((i5 = this.skylightMap.get(x, y, z)) > 0) {
			isLit = true;
		}

		i5 -= skyLightSubtracted;
		if((x = this.blocklightMap.get(x, y, z)) > i5) {
			i5 = x;
		}

		return i5;
	}

	public void addEntity(Entity entity) {
        this.hasEntities = true;
		int i2 = MathHelper.floor_double(entity.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
		if(i2 != this.xPosition || i3 != this.zPosition) {
			System.out.println("Wrong location! " + entity);
		}

		if((i2 = MathHelper.floor_double(entity.posY / 16.0D)) < 0) {
			i2 = 0;
		}

		if(i2 >= this.entities.length) {
			i2 = this.entities.length - 1;
		}

		this.entities[i2].add(entity);
	}

	public void removeEntity(Entity entity) {
		this.removeEntityAtIndex(entity, MathHelper.floor_double(entity.posY / 16.0D));
	}

	public void removeEntityAtIndex(Entity entity, int entityID) {
		if(entityID < 0) {
			entityID = 0;
		}

		if(entityID >= this.entities.length) {
			entityID = this.entities.length - 1;
		}

		if(!this.entities[entityID].contains(entity)) {
			System.out.println("There\'s no such entity to remove: " + entity);
		}

		this.entities[entityID].remove(entity);
	}

	public boolean canBlockSeeTheSky(int x, int y, int z) {
		return y >= (this.heightMap[z << 4 | x] & 255);
	}

	public TileEntity getChunkBlockTileEntity(int x, int y, int z) {
		int i4 = x + y * 1024 + z * 1024 * 1024;
		TileEntity tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4);
		if(tileEntity5 == null) {
			int i6 = this.getBlockID(x, y, z);
			BlockContainer blockContainer7 = (BlockContainer)Block.blocksList[i6];
			blockContainer7.onBlockAdded(this.worldObj, this.xPosition * 16 + x, y, this.zPosition * 16 + z);
			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4);
		}

		return tileEntity5;
	}

	public void addTileEntity(TileEntity tileEntity) {
		int i2 = tileEntity.xCoord - this.xPosition * 16;
		int i3 = tileEntity.yCoord;
		int i4 = tileEntity.zCoord - this.zPosition * 16;
		this.setChunkBlockTileEntity(i2, i3, i4, tileEntity);
	}

	public void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		int i5 = x + y * 1024 + z * 1024 * 1024;
		tileEntity.worldObj = this.worldObj;
		tileEntity.xCoord = this.xPosition * 16 + x;
		tileEntity.yCoord = y;
		tileEntity.zCoord = this.zPosition * 16 + z;
		if(this.getBlockID(x, y, z) != 0 && Block.blocksList[this.getBlockID(x, y, z)] instanceof BlockContainer) {
			if(this.isChunkLoaded) {
				if(this.chunkTileEntityMap.get(i5) != null) {
					this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.get(i5));
				}

				this.worldObj.loadedTileEntityList.add(tileEntity);
			}

			this.chunkTileEntityMap.put(i5, tileEntity);
		} else {
			System.out.println("Attempted to place a tile entity where there was no entity tile!");
		}
	}

	public void removeChunkBlockTileEntity(int i1, int i2, int i3) {
		int i4 = i1 + i2 * 1024 + i3 * 1024 * 1024;
		if(this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.remove(i4));
		}

	}

	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.loadedTileEntityList.addAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entities[i1]);
		}

	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		this.worldObj.loadedTileEntityList.removeAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.unloadEntities(this.entities[i1]);
		}

	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB aabb, List entitiesOfTypeWithinAABBList) {
		int i4 = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List list7 = this.entities[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(entity9 != entity && entity9.boundingBox.intersectsWith(aabb)) {
					entitiesOfTypeWithinAABBList.add(entity9);
				}
			}
		}

	}

    public void getEntitiesOfTypeWithinAABB(Class<? extends Entity> class1, AxisAlignedBB axisAlignedBB2, List<Entity> entitiesOfTypeWithinAABBList) {
        int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
        int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
        if(i4 < 0) {
            i4 = 0;
        }

        if(i5 >= this.entities.length) {
            i5 = this.entities.length - 1;
        }

        for(i4 = i4; i4 <= i5; ++i4) {
            List<Entity> list6 = this.entities[i4];

            for(int i7 = 0; i7 < list6.size(); ++i7) {
                Entity entity8 = (Entity)list6.get(i7);
                if(class1.isAssignableFrom(entity8.getClass()) && entity8.boundingBox.intersectsWith(axisAlignedBB2)) {
                    entitiesOfTypeWithinAABBList.add(entity8);
                }
            }
        }

    }

    public boolean needsSaving(boolean z1) {
        return this.neverSave ? false : (this.hasEntities && this.worldObj.worldTime != this.lastSaveTime ? true : this.isModified);
    }
}
