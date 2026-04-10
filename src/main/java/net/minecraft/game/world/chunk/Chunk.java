package net.minecraft.game.world.chunk;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockContainer;
import net.minecraft.game.world.block.tileentity.TileEntity;

public final class Chunk {
	public static boolean isLit;
	private byte[] blocks;
	private boolean hasTileEntities;
	private World worldObj;
	private NibbleArray data;
	private NibbleArray skyLightMap;
	private NibbleArray blockLightMap;
	private byte[] heightMap;
	private int height;
	public final int xPosition;
	public final int zPosition;
	private Map chunkTileEntityMap;
	private List[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	private boolean hasEntities;
    public boolean neverSave;

	private Chunk(World world, int chunkX, int chunkZ) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
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
		this.skyLightMap = new NibbleArray(blockData.length);
		this.blockLightMap = new NibbleArray(blockData.length);
	}

	public final int getHeightValue(int x, int z) {
		return this.heightMap[z << 4 | x] & 255;
	}

	public final void generateHeightMap() {
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
		x += this.xPosition << 4;
		z += this.zPosition << 4;
		this.checkSkylightNeighborHeight(x - 1, z, i3);
		this.checkSkylightNeighborHeight(x + 1, z, i3);
		this.checkSkylightNeighborHeight(x, z - 1, i3);
		this.checkSkylightNeighborHeight(x, z + 1, i3);
	}

	private void checkSkylightNeighborHeight(int x, int z, int y) {
		int i4;
		if((i4 = this.worldObj.getHeightValue(x, z)) > y) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, x, y, z, x, i4, z);
		} else if(i4 < y) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, x, i4, z, x, y, z);
		}

		this.isModified = true;
	}

	private void relightBlock(int x, int y, int z) {
		int i4;
		int i5 = i4 = this.heightMap[z << 4 | x] & 255;
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
			if(i5 < this.height) {
				this.height = i5;
			} else {
				y = 127;

				for(i6 = 0; i6 < 16; ++i6) {
					for(i7 = 0; i7 < 16; ++i7) {
						if((this.heightMap[i7 << 4 | i6] & 255) < y) {
							y = this.heightMap[i7 << 4 | i6] & 255;
						}
					}
				}

				this.height = y;
			}

			y = (this.xPosition << 4) + x;
			i6 = (this.zPosition << 4) + z;
			if(i5 < i4) {
				for(i7 = i5; i7 < i4; ++i7) {
					this.skyLightMap.setNibble(x, i7, z, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, y, i4, i6, y, i5, i6);

				for(i7 = i4; i7 < i5; ++i7) {
					this.skyLightMap.setNibble(x, i7, z, 0);
				}
			}

			i7 = 15;

			for(i4 = i5; i5 > 0 && i7 > 0; this.skyLightMap.setNibble(x, i5, z, i7)) {
				--i5;
				int i8;
				if((i8 = Block.lightOpacity[this.getBlockID(x, i5, z)]) == 0) {
					i8 = 1;
				}

				if((i7 -= i8) < 0) {
					i7 = 0;
				}
			}

			while(i5 > 0 && Block.lightOpacity[this.getBlockID(x, i5 - 1, z)] == 0) {
				--i5;
			}

			if(i5 != i4) {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, y - 1, i5, i6 - 1, y + 1, i4, i6 + 1);
			}

			this.isModified = true;
		}
	}

	public final int getBlockID(int x, int y, int z) {
		return this.blocks[x << 11 | z << 7 | y];
	}

	public final boolean setBlockID(int x, int y, int z, int blockID) {
		byte b5 = (byte)blockID;
		int i6 = this.heightMap[z << 4 | x] & 255;
		int i7;
		if((i7 = this.blocks[x << 11 | z << 7 | y] & 255) == blockID) {
			return false;
		} else {
			int i8 = (this.xPosition << 4) + x;
			int i9 = (this.zPosition << 4) + z;
			if(i7 != 0) {
				Block.blocksList[i7].onBlockRemoval(this.worldObj, i8, y, i9);
			}

			this.blocks[x << 11 | z << 7 | y] = b5;
			this.data.setNibble(x, y, z, 0);
			if(Block.lightOpacity[b5] != 0) {
				if(y >= i6) {
					this.relightBlock(x, y + 1, z);
				}
			} else if(y == i6 - 1) {
				this.relightBlock(x, y, z);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i8, y, i9, i8, y, i9);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i8, y, i9, i8, y, i9);
			this.updateSkylight_do(x, z);
			if(blockID != 0) {
				Block.blocksList[blockID].onBlockAdded(this.worldObj, i8, y, i9);
			}

			this.isModified = true;
			return true;
		}
	}

	public final int getBlockMetadata(int x, int y, int z) {
		return this.data.getNibble(x, y, z);
	}

	public final void setBlockMetadata(int x, int y, int z, int metadata) {
		this.isModified = true;
		this.data.setNibble(x, y, z, metadata);
	}

	public final int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
		return skyBlock == EnumSkyBlock.Sky ? this.skyLightMap.getNibble(x, y, z) : (skyBlock == EnumSkyBlock.Block ? this.blockLightMap.getNibble(x, y, z) : 0);
	}

	public final void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int lightValue) {
		this.isModified = true;
		if(skyBlock == EnumSkyBlock.Sky) {
			this.skyLightMap.setNibble(x, y, z, lightValue);
		} else if(skyBlock == EnumSkyBlock.Block) {
			this.blockLightMap.setNibble(x, y, z, lightValue);
		}
	}

	public final int getBlockLightValue(int x, int y, int z, int skyLightSubtracted) {
		int i5;
		if((i5 = this.skyLightMap.getNibble(x, y, z)) > 0) {
			isLit = true;
		}

		i5 -= skyLightSubtracted;
		if((x = this.blockLightMap.getNibble(x, y, z)) > i5) {
			i5 = x;
		}

		return i5;
	}

	public final void writeChunkNBTData(NBTTagCompound compoundTag) {
		compoundTag.setInteger("xPos", this.xPosition);
		compoundTag.setInteger("zPos", this.zPosition);
		compoundTag.setLong("LastUpdate", this.worldObj.worldTime);
		compoundTag.setByteArray("Blocks", this.blocks);
		compoundTag.setByteArray("Data", this.data.data);
		compoundTag.setByteArray("SkyLight", this.skyLightMap.data);
		compoundTag.setByteArray("BlockLight", this.blockLightMap.data);
		compoundTag.setByteArray("HeightMap", this.heightMap);
		compoundTag.setBoolean("TerrainPopulated", this.isTerrainPopulated);
		this.hasEntities = false;
		NBTTagList nBTTagList2 = new NBTTagList();

		Iterator iterator4;
		NBTTagCompound nBTTagCompound6;
		for(int i3 = 0; i3 < this.entities.length; ++i3) {
			iterator4 = this.entities[i3].iterator();

			while(iterator4.hasNext()) {
				Entity entity5 = (Entity)iterator4.next();
				nBTTagCompound6 = new NBTTagCompound();
				if(entity5.addEntityID(nBTTagCompound6)) {
					nBTTagList2.setTag(nBTTagCompound6);
					this.hasEntities = true;
				}
			}
		}

		compoundTag.setTag("Entities", nBTTagList2);
		NBTTagList nBTTagList7 = new NBTTagList();
		iterator4 = this.chunkTileEntityMap.values().iterator();

		while(iterator4.hasNext()) {
			TileEntity tileEntity8 = (TileEntity)iterator4.next();
			nBTTagCompound6 = new NBTTagCompound();
			tileEntity8.writeToNBT(nBTTagCompound6);
			nBTTagList7.setTag(nBTTagCompound6);
		}

		compoundTag.setTag("TileEntities", nBTTagList7);
	}

	public static Chunk readChunkNBTData(World world, NBTTagCompound compoundTag) {
		int i2 = compoundTag.getInteger("xPos");
		int i3 = compoundTag.getInteger("zPos");
		Chunk chunk9;
		(chunk9 = new Chunk(world, i2, i3)).blocks = compoundTag.getByteArray("Blocks");
		chunk9.data = new NibbleArray(compoundTag.getByteArray("Data"));
		chunk9.skyLightMap = new NibbleArray(compoundTag.getByteArray("SkyLight"));
		chunk9.blockLightMap = new NibbleArray(compoundTag.getByteArray("BlockLight"));
		chunk9.heightMap = compoundTag.getByteArray("HeightMap");
		chunk9.isTerrainPopulated = compoundTag.getBoolean("TerrainPopulated");
		if(!chunk9.data.isValid()) {
			chunk9.data = new NibbleArray(chunk9.blocks.length);
		}

		if(chunk9.heightMap == null || !chunk9.skyLightMap.isValid()) {
			chunk9.heightMap = new byte[256];
			chunk9.skyLightMap = new NibbleArray(chunk9.blocks.length);
			chunk9.generateHeightMap();
		}

		if(!chunk9.blockLightMap.isValid()) {
			chunk9.blockLightMap = new NibbleArray(chunk9.blocks.length);
		}

		chunk9.hasEntities = false;
		NBTTagList nBTTagList10;
		if((nBTTagList10 = compoundTag.getTagList("Entities")) != null) {
			for(int i4 = 0; i4 < nBTTagList10.tagCount(); ++i4) {
				Entity entity6;
				if((entity6 = EntityList.createEntityFromNBT((NBTTagCompound)nBTTagList10.tagAt(i4), world)) != null) {
					chunk9.hasEntities = true;
					chunk9.addEntity(entity6);
				}
			}
		}

		NBTTagList nBTTagList11;
		if((nBTTagList11 = compoundTag.getTagList("TileEntities")) != null) {
			for(int i5 = 0; i5 < nBTTagList11.tagCount(); ++i5) {
				TileEntity tileEntity8;
				if((tileEntity8 = TileEntity.createAndLoadEntity((NBTTagCompound)nBTTagList11.tagAt(i5))) != null) {
					i3 = tileEntity8.xCoord - (chunk9.xPosition << 4);
					int i12 = tileEntity8.yCoord;
					int i7 = tileEntity8.zCoord - (chunk9.zPosition << 4);
					chunk9.setChunkBlockTileEntity(i3, i12, i7, tileEntity8);
				}
			}
		}

		return chunk9;
	}

	public final void addEntity(Entity entity) {
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
		this.isModified = true;
	}

	public final void removeEntityAtIndex(Entity entity, int entityID) {
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
		this.isModified = true;
	}

	public final boolean canBlockSeeTheSky(int x, int y, int z) {
		return y >= (this.heightMap[z << 4 | x] & 255);
	}

	public final TileEntity getChunkBlockTileEntity(int x, int y, int z) {
		int i4 = x + (y << 10) + (z << 10 << 10);
		TileEntity tileEntity5;
		if((tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4)) == null) {
			int i6 = this.getBlockID(x, y, z);
			((BlockContainer)Block.blocksList[i6]).onBlockAdded(this.worldObj, (this.xPosition << 4) + x, y, (this.zPosition << 4) + z);
			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4);
		}

		return tileEntity5;
	}

	public final void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		this.isModified = true;
		int i5 = x + (y << 10) + (z << 10 << 10);
		tileEntity.worldObj = this.worldObj;
		tileEntity.xCoord = (this.xPosition << 4) + x;
		tileEntity.yCoord = y;
		tileEntity.zCoord = (this.zPosition << 4) + z;
		if(this.getBlockID(x, y, z) != 0 && Block.blocksList[this.getBlockID(x, y, z)] instanceof BlockContainer) {
			if(this.hasTileEntities) {
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

	public final void removeChunkBlockTileEntity(int x, int y, int z) {
		this.isModified = true;
		x = x + (y << 10) + (z << 10 << 10);
		if(this.hasTileEntities) {
			this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.remove(x));
		}

	}

	public final void loadTileEntities() {
		this.hasTileEntities = true;
		this.worldObj.loadedTileEntityList.addAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entities[i1]);
		}

	}

	public final void unloadTileEntities() {
		this.hasTileEntities = false;
		this.worldObj.loadedTileEntityList.removeAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.unloadEntities(this.entities[i1]);
		}

	}

	public final void getEntitiesOfTypeWithinAAAB(Entity entity, AxisAlignedBB aabb, List entitiesOfTypeWithinAABBList) {
		int i4 = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(i4 = i4; i4 <= i5; ++i4) {
			List list6 = this.entities[i4];

			for(int i7 = 0; i7 < list6.size(); ++i7) {
				Entity entity8;
				if((entity8 = (Entity)list6.get(i7)) != entity && entity8.boundingBox.intersectsWith(aabb)) {
					entitiesOfTypeWithinAABBList.add(entity8);
				}
			}
		}

	}

	public final boolean needsSaving(boolean flag) {
        if(this.neverSave) {
            return false;
        } else if(this.isModified) {
			return true;
		} else {
			if(flag) {
				if(this.hasEntities) {
					return true;
				}

				for(int i2 = 0; i2 < this.entities.length; ++i2) {
					if(this.entities[i2].size() > 0) {
						return true;
					}
				}
			}

			return false;
		}
	}
}