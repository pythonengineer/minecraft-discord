package net.minecraft.game.world.chunk;

import com.mojang.nbt.NBTTagCompound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.world.World;

public final class ChunkProviderLoadOrGenerate implements IChunkProvider {
    private Chunk currentChunk;
	private IChunkProvider chunkProvider;
	private Chunk[] chunks = new Chunk[1024];
	private VFile2 saveDirectory;
	private World worldObj;
	private List emptyList = new ArrayList();

	public ChunkProviderLoadOrGenerate(World world, VFile2 saveDir, IChunkProvider chunkProvider) {
        this.currentChunk = new Chunk(world, new byte[32768], 0, 0);
        this.currentChunk.neverSave = true;
		this.worldObj = world;
		this.chunkProvider = chunkProvider;
		this.saveDirectory = saveDir;
	}

	public final boolean chunkExists(int chunkX, int chunkZ) {
		int i3 = chunkX & 31 | (chunkZ & 31) << 5;
		if(this.chunks[i3] != null) {
            if(this.chunks[i3] == this.currentChunk) {
                return true;
            }

			Chunk chunk10000 = this.chunks[i3];
			i3 = chunkZ;
			chunkZ = chunkX;
			Chunk chunkX1 = chunk10000;
			if(chunkZ == chunkX1.xPosition && i3 == chunkX1.zPosition) {
				return true;
			}
		}

		return false;
	}

	public final Chunk provideChunk(int chunkX, int chunkZ) {
		int i3 = chunkX & 31 | (chunkZ & 31) << 5;
		if(!this.chunkExists(chunkX, chunkZ)) {
			if(this.chunks[i3] != null) {
				this.chunks[i3].unloadTileEntities();
				this.saveChunk(this.chunks[i3]);
			}

			Chunk chunk4;
			if((chunk4 = this.loadChunk(chunkX, chunkZ)) == null) {
                if(this.chunkProvider == null) {
                    chunk4 = this.currentChunk;
                } else {
                    chunk4 = this.chunkProvider.provideChunk(chunkX, chunkZ);
                }
			}

			this.chunks[i3] = chunk4;
			if(this.chunks[i3] != null) {
				this.chunks[i3].loadTileEntities();
			}

			if(!this.chunks[i3].isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX + 1, chunkZ)) {
				this.populate(this, chunkX, chunkZ);
			}

			if(this.chunkExists(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX - 1, chunkZ)) {
				this.populate(this, chunkX - 1, chunkZ);
			}

			if(this.chunkExists(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX + 1, chunkZ)) {
				this.populate(this, chunkX, chunkZ - 1);
			}

			if(this.chunkExists(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX - 1, chunkZ)) {
				this.populate(this, chunkX - 1, chunkZ - 1);
			}
		}

		return this.chunks[i3];
	}

	private VFile2 chunkFileForXZ(int chunkX, int chunkZ) {
		String string3 = "c." + Integer.toString(chunkX, 36) + "." + Integer.toString(chunkZ, 36) + ".dat";
		String chunkX1 = Integer.toString(chunkX & 63, 36);
		String chunkZ1 = Integer.toString(chunkZ & 63, 36);
		VFile2 chunkX2;
		chunkX2 = new VFile2(this.saveDirectory, chunkX1);
		chunkX2 = new VFile2(chunkX2, chunkZ1);
		return new VFile2(chunkX2, string3);
	}

	private Chunk loadChunk(int chunkX, int chunkZ) {
		VFile2 chunkX1;
		if((chunkX1 = this.chunkFileForXZ(chunkX, chunkZ)).exists()) {
            try (InputStream fis = chunkX1.getInputStream()) {
				NBTTagCompound chunkX2 = LoadingScreenRenderer.read(fis);
				return Chunk.readChunkNBTData(this.worldObj, chunkX2.getCompoundTag("Level"));
			} catch (IOException exception3) {
				exception3.printStackTrace();
			}
		}

		return null;
	}

	private void saveChunk(Chunk chunk) {
		VFile2 file2;
		if((file2 = this.chunkFileForXZ(chunk.xPosition, chunk.zPosition)).exists()) {
			this.worldObj.sizeOnDisk -= file2.length();
		}

        try (OutputStream fos = file2.getOutputStream()) {
			NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
			NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
			nBTTagCompound4.setTag("Level", nBTTagCompound5);
			chunk.writeChunkNBTData(nBTTagCompound5);
			LoadingScreenRenderer.write(nBTTagCompound4, fos);
			this.worldObj.sizeOnDisk += file2.length();
		} catch (IOException exception6) {
			exception6.printStackTrace();
		}
	}

	public final void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		Chunk chunk4;
		if(!(chunk4 = this.provideChunk(chunkX, chunkZ)).isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
            if(this.chunkProvider != null) {
                this.chunkProvider.populate(chunkProvider, chunkX, chunkZ);
            }
		}

	}

	public final void saveChunks(boolean flag) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.chunks.length; ++i3) {
			if(this.chunks[i3] != null && this.chunks[i3].needsSaving(flag)) {
				this.saveChunk(this.chunks[i3]);
				this.chunks[i3].isModified = false;
				++i2;
				if(i2 == 2 && !flag) {
					return;
				}
			}
		}

	}

	public final boolean unload100OldestChunks() {
		this.chunkProvider.unload100OldestChunks();
		int i1 = 0;

        while(i1++ <= 0 && !this.emptyList.isEmpty()) {
            this.emptyList.remove(0);
            if(this.chunks[0].neverSave) {
                Chunk chunk2 = this.chunkProvider.provideChunk(0, 0);
                this.chunks[0] = chunk2;
                if(this.chunks[0] != null) {
                    this.chunks[0].loadTileEntities();
                }

                if(!this.chunks[0].isTerrainPopulated && this.chunkExists(1, 1) && this.chunkExists(0, 1) && this.chunkExists(1, 0)) {
                    this.populate(this, 0, 0);
                }

                if(this.chunkExists(-1, 0) && !this.provideChunk(-1, 0).isTerrainPopulated && this.chunkExists(-1, 1) && this.chunkExists(0, 1) && this.chunkExists(-1, 0)) {
                    this.populate(this, -1, 0);
                }

                if(this.chunkExists(0, -1) && !this.provideChunk(0, -1).isTerrainPopulated && this.chunkExists(1, -1) && this.chunkExists(0, -1) && this.chunkExists(1, 0)) {
                    this.populate(this, 0, -1);
                }

                if(this.chunkExists(-1, -1) && !this.provideChunk(-1, -1).isTerrainPopulated && this.chunkExists(-1, -1) && this.chunkExists(0, -1) && this.chunkExists(-1, 0)) {
                    this.populate(this, -1, -1);
                }
            }
        }

		return !this.emptyList.isEmpty();
	}
}