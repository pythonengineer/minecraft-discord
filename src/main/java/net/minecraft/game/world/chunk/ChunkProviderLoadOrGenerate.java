package net.minecraft.game.world.chunk;

import java.io.IOException;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.BlockSand;
import net.minecraft.game.world.chunk.loader.IChunkLoader;

public final class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Chunk[] chunkMap = new Chunk[1024];
	private World world;
	private int chunkXPos = -999999999;
	private int chunkZPos = -999999999;
	private Chunk currentChunk;

	public ChunkProviderLoadOrGenerate(World world1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {
		this.blankChunk = new Chunk(world1, new byte[32768], 0, 0);
		this.blankChunk.isChunkRendered = true;
		this.blankChunk.neverSave = true;
		this.world = world1;
		this.chunkLoader = iChunkLoader2;
		this.chunkProvider = iChunkProvider3;
	}

	public final boolean chunkExists(int i1, int i2) {
		if(i1 == this.chunkXPos && i2 == this.chunkZPos && this.currentChunk != null) {
			return true;
		} else {
			int i3 = i1 & 31;
			int i4 = i2 & 31;
			i3 += i4 << 5;
			return this.chunkMap[i3] != null && (this.chunkMap[i3] == this.blankChunk || this.chunkMap[i3].isAtLocation(i1, i2));
		}
	}

	public final Chunk provideChunk(int i1, int i2) {
		if(i1 == this.chunkXPos && i2 == this.chunkZPos && this.currentChunk != null) {
			return this.currentChunk;
		} else {
			int i3 = i1 & 31;
			int i4 = i2 & 31;
			i3 += i4 << 5;
			if(!this.chunkExists(i1, i2)) {
				BlockSand.fallInstantly = true;
				int i5;
				Chunk chunk6;
				if(this.chunkMap[i3] != null) {
					(chunk6 = this.chunkMap[i3]).isChunkLoaded = false;
					chunk6.worldObj.loadedTileEntityList.removeAll(chunk6.chunkTileEntityMap.values());

					for(i5 = 0; i5 < chunk6.entities.length; ++i5) {
						chunk6.worldObj.unloadEntities(chunk6.entities[i5]);
					}

					this.saveChunk(this.chunkMap[i3]);
					this.saveChunkMap(this.chunkMap[i3]);
				}

				if((chunk6 = this.loadChunkAtPos(i1, i2)) == null) {
					if(this.chunkProvider == null) {
						chunk6 = this.blankChunk;
					} else {
						chunk6 = this.chunkProvider.provideChunk(i1, i2);
					}
				}

				this.chunkMap[i3] = chunk6;
				if(this.chunkMap[i3] != null) {
					(chunk6 = this.chunkMap[i3]).isChunkLoaded = true;
					chunk6.worldObj.loadedTileEntityList.addAll(chunk6.chunkTileEntityMap.values());

					for(i5 = 0; i5 < chunk6.entities.length; ++i5) {
						chunk6.worldObj.addLoadedEntities(chunk6.entities[i5]);
					}
				}

				if(!this.chunkMap[i3].isTerrainPopulated && this.chunkExists(i1 + 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 + 1, i2)) {
					this.populate(this, i1, i2);
				}

				if(this.chunkExists(i1 - 1, i2) && !this.provideChunk(i1 - 1, i2).isTerrainPopulated && this.chunkExists(i1 - 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 - 1, i2)) {
					this.populate(this, i1 - 1, i2);
				}

				if(this.chunkExists(i1, i2 - 1) && !this.provideChunk(i1, i2 - 1).isTerrainPopulated && this.chunkExists(i1 + 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 + 1, i2)) {
					this.populate(this, i1, i2 - 1);
				}

				if(this.chunkExists(i1 - 1, i2 - 1) && !this.provideChunk(i1 - 1, i2 - 1).isTerrainPopulated && this.chunkExists(i1 - 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 - 1, i2)) {
					this.populate(this, i1 - 1, i2 - 1);
				}

				BlockSand.fallInstantly = false;
			}

			this.chunkXPos = i1;
			this.chunkZPos = i2;
			this.currentChunk = this.chunkMap[i3];
			return this.chunkMap[i3];
		}
	}

	private Chunk loadChunkAtPos(int i1, int i2) {
		try {
			return this.chunkLoader.loadChunk(this.world, i1, i2);
		} catch (Exception exception3) {
			exception3.printStackTrace();
			return null;
		}
	}

	private void saveChunkMap(Chunk chunk1) {
	}

	private void saveChunk(Chunk chunk1) {
		try {
			this.chunkLoader.saveChunk(this.world, chunk1);
		} catch (IOException iOException2) {
			iOException2.printStackTrace();
		}
	}

	public final void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4;
		if(!(chunk4 = this.provideChunk(i2, i3)).isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(iChunkProvider1, i2, i3);
				chunk4.isModified = true;
			}
		}

	}

	public final boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		int i3 = 0;
		int i4 = 0;
		int i5;
		if(iProgressUpdate2 != null) {
			for(i5 = 0; i5 < this.chunkMap.length; ++i5) {
				if(this.chunkMap[i5] != null && this.chunkMap[i5].needsSaving()) {
					++i4;
				}
			}
		}

		i5 = 0;

		for(int i6 = 0; i6 < this.chunkMap.length; ++i6) {
			if(this.chunkMap[i6] != null) {
				if(z1 && !this.chunkMap[i6].neverSave) {
					this.saveChunkMap(this.chunkMap[i6]);
				}

				if(this.chunkMap[i6].needsSaving()) {
					this.saveChunk(this.chunkMap[i6]);
					this.chunkMap[i6].isModified = false;
					++i3;
					if(i3 == 2 && !z1) {
						return false;
					}

					if(iProgressUpdate2 != null) {
						++i5;
						if(i5 % 10 == 0) {
							iProgressUpdate2.setLoadingProgress(i5 * 100 / i4);
						}
					}
				}
			}
		}

		return true;
	}

	public final boolean canSave() {
		return true;
	}
}