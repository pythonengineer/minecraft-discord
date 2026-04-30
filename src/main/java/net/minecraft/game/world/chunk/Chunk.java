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

public final class Chunk {
	public static boolean isLit;
	public byte[] blocks;
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray data;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	private int height;
	public final int xPosition;
	public final int zPosition;
	public Map chunkTileEntityMap;
	public List[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean neverSave;
	public boolean isChunkRendered;

	public Chunk(World world, int chunkX, int chunkZ) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.isChunkRendered = false;
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

	public final boolean isAtLocation(int chunkX, int chunkZ) {
		return chunkX == this.xPosition && chunkZ == this.zPosition;
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
					this.skylightMap.setNibble(x, i7, z, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, y, i4, i6, y, i5, i6);

				for(i7 = i4; i7 < i5; ++i7) {
					this.skylightMap.setNibble(x, i7, z, 0);
				}
			}

			i7 = 15;

			for(i4 = i5; i5 > 0 && i7 > 0; this.skylightMap.setNibble(x, i5, z, i7)) {
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

	public final boolean setBlockIDWithMetadata(int x, int y, int z, int blockID, int metadata) {
		byte b6 = (byte)blockID;
		int i7 = this.heightMap[z << 4 | x] & 255;
		int i8;
		if((i8 = this.blocks[x << 11 | z << 7 | y] & 255) == blockID) {
			return false;
		} else {
			int i9 = (this.xPosition << 4) + x;
			int i10 = (this.zPosition << 4) + z;
			this.blocks[x << 11 | z << 7 | y] = b6;
			if(i8 != 0) {
				Block.blocksList[i8].onBlockRemoval(this.worldObj, i9, y, i10);
			}

			this.data.setNibble(x, y, z, metadata);
			if(Block.lightOpacity[b6] != 0) {
				if(y >= i7) {
					this.relightBlock(x, y + 1, z);
				}
			} else if(y == i7 - 1) {
				this.relightBlock(x, y, z);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i9, y, i10, i9, y, i10);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i9, y, i10, i9, y, i10);
			this.updateSkylight_do(x, z);
			if(blockID != 0) {
				Block.blocksList[blockID].onBlockAdded(this.worldObj, i9, y, i10);
			}

			this.isModified = true;
			return true;
		}
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
			this.blocks[x << 11 | z << 7 | y] = b5;
			if(i7 != 0) {
				Block.blocksList[i7].onBlockRemoval(this.worldObj, i8, y, i9);
			}

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

	public final int getBlockLightValue(int x, int y, int z, int skyLightSubtracted) {
		int i5;
		if((i5 = this.skylightMap.getNibble(x, y, z)) > 0) {
			isLit = true;
		}

		i5 -= skyLightSubtracted;
		if((x = this.blocklightMap.getNibble(x, y, z)) > i5) {
			i5 = x;
		}

		return i5;
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

	public final void addTileEntity(TileEntity tileEntity) {
		int i2 = tileEntity.xCoord - (this.xPosition << 4);
		int i3 = tileEntity.yCoord;
		int i4 = tileEntity.zCoord - (this.zPosition << 4);
		this.setChunkBlockTileEntity(i2, i3, i4, tileEntity);
	}

	public final void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
		int i5 = x + (y << 10) + (z << 10 << 10);
		tileEntity.worldObj = this.worldObj;
		tileEntity.xCoord = (this.xPosition << 4) + x;
		tileEntity.yCoord = y;
		tileEntity.zCoord = (this.zPosition << 4) + z;
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

	public final boolean needsSaving() {
		return this.neverSave ? false : this.isModified;
	}
}