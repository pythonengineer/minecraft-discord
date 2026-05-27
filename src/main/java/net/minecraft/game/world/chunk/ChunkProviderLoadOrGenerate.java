package net.minecraft.game.world.chunk;

import java.io.IOException;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.BlockSand;
import net.minecraft.game.world.chunk.loader.IChunkLoader;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Chunk[] chunks = new Chunk[1024];
	private World worldObj;
	int lastQueriedChunkXPos = -999999999;
	int lastQueriedChunkZPos = -999999999;
	private Chunk lastQueriedChunk;

	public ChunkProviderLoadOrGenerate(World world1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {
		this.blankChunk = new Chunk(world1, new byte[32768], 0, 0);
		this.blankChunk.isChunkRendered = true;
		this.blankChunk.neverSave = true;
		this.worldObj = world1;
		this.chunkLoader = iChunkLoader2;
		this.chunkProvider = iChunkProvider3;
	}

	public boolean chunkExists(int i1, int i2) {
		if(i1 == this.lastQueriedChunkXPos && i2 == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return true;
		} else {
			int i3 = i1 & 31;
			int i4 = i2 & 31;
			int i5 = i3 + i4 * 32;
			return this.chunks[i5] != null && (this.chunks[i5] == this.blankChunk || this.chunks[i5].isAtLocation(i1, i2));
		}
	}

	public Chunk provideChunk(int i1, int i2) {
		if(i1 == this.lastQueriedChunkXPos && i2 == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return this.lastQueriedChunk;
		} else {
			int i3 = i1 & 31;
			int i4 = i2 & 31;
			int i5 = i3 + i4 * 32;
			if(!this.chunkExists(i1, i2)) {
				BlockSand.fallInstantly = true;
				if(this.chunks[i5] != null) {
                    this.chunks[i5].onChunkUnload();
					this.saveChunk(this.chunks[i5]);
					this.saveExtraChunkData(this.chunks[i5]);
				}

				Chunk chunk6 = this.getChunkAt(i1, i2);
				if(chunk6 == null) {
					if(this.chunkProvider == null) {
						chunk6 = this.blankChunk;
					} else {
						chunk6 = this.chunkProvider.provideChunk(i1, i2);
					}
				}

				this.chunks[i5] = chunk6;
				if(this.chunks[i5] != null) {
                    this.chunks[i5].onChunkLoad();
				}

				if(!this.chunks[i5].isTerrainPopulated && this.chunkExists(i1 + 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 + 1, i2)) {
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

			this.lastQueriedChunkXPos = i1;
			this.lastQueriedChunkZPos = i2;
			this.lastQueriedChunk = this.chunks[i5];
			return this.chunks[i5];
		}
	}

	private Chunk getChunkAt(int i1, int i2) {
        if(this.chunkLoader == null) {
            return null;
        } else {
    		try {
    			Chunk chunk3 = this.chunkLoader.loadChunk(this.worldObj, i1, i2);
    			if(chunk3 != null) {
    				chunk3.lastSaveTime = this.worldObj.worldTime;
    			}

    			return chunk3;
    		} catch (Exception exception4) {
    			exception4.printStackTrace();
    			return null;
    		}
        }
	}

	private void saveExtraChunkData(Chunk chunk) {
        if(this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, chunk);
            } catch (Exception exception3) {
                exception3.printStackTrace();
            }

        }
	}

	private void saveChunk(Chunk chunk) {
        if(this.chunkLoader != null) {
    		try {
                chunk.lastSaveTime = this.worldObj.worldTime;
                this.chunkLoader.saveChunk(this.worldObj, chunk);
    		} catch (IOException iOException2) {
    			iOException2.printStackTrace();
    		}
        }
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4 = this.provideChunk(i2, i3);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(iChunkProvider1, i2, i3);
                chunk4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean flag, IProgressUpdate loadingScreen) {
		int i3 = 0;
		int i4 = 0;
		int i5;
		if(loadingScreen != null) {
			for(i5 = 0; i5 < this.chunks.length; ++i5) {
				if(this.chunks[i5] != null && this.chunks[i5].needsSaving(flag)) {
					++i4;
				}
			}
		}

		i5 = 0;

		for(int i6 = 0; i6 < this.chunks.length; ++i6) {
			if(this.chunks[i6] != null) {
				if(flag && !this.chunks[i6].neverSave) {
					this.saveExtraChunkData(this.chunks[i6]);
				}

				if(this.chunks[i6].needsSaving(flag)) {
					this.saveChunk(this.chunks[i6]);
					this.chunks[i6].isModified = false;
					++i3;
					if(i3 == 2 && !flag) {
						return false;
					}

					if(loadingScreen != null) {
						++i5;
						if(i5 % 10 == 0) {
							loadingScreen.setLoadingProgress(i5 * 100 / i4);
						}
					}
				}
			}
		}

        if(flag) {
            if(this.chunkLoader == null) {
                return true;
            }

            this.chunkLoader.saveExtraData();
        }

		return true;
	}

	public boolean unload100OldestChunks() {
		if(this.chunkLoader != null) {
			this.chunkLoader.chunkTick();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}
}
