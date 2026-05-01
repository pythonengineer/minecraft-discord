package net.minecraft.game.world.chunk;

import java.io.IOException;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.world.World;
import net.minecraft.game.world.chunk.loader.IChunkLoader;

public final class ChunkProviderIso implements IChunkProvider {
	private Chunk[] chunkMapping = new Chunk[256];
	private World worldObj;
	private IChunkLoader chunkLoader;
	private byte[] blankChunk = new byte[32768];

	public ChunkProviderIso(World world1, IChunkLoader iChunkLoader2) {
		this.worldObj = world1;
		this.chunkLoader = iChunkLoader2;
	}

	public final boolean chunkExists(int i1, int i2) {
		int i3 = i1 & 15 | (i2 & 15) << 4;
		return this.chunkMapping[i3] != null && this.chunkMapping[i3].isAtLocation(i1, i2);
	}

	public final Chunk provideChunk(int i1, int i2) {
		int i3 = i1 & 15 | (i2 & 15) << 4;

		try {
			if(!this.chunkExists(i1, i2)) {
				Chunk chunk4;
				if((chunk4 = this.loadChunk(i1, i2)) == null) {
					(chunk4 = new Chunk(this.worldObj, this.blankChunk, i1, i2)).isChunkRendered = true;
					chunk4.neverSave = true;
				}

				this.chunkMapping[i3] = chunk4;
			}

			return this.chunkMapping[i3];
		} catch (Exception exception5) {
			exception5.printStackTrace();
			return null;
		}
	}

	private synchronized Chunk loadChunk(int i1, int i2) {
		try {
			return this.chunkLoader.loadChunk(this.worldObj, i1, i2);
		} catch (IOException iOException3) {
			iOException3.printStackTrace();
			return null;
		}
	}

	public final void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
	}

	public final boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public final boolean canSave() {
		return false;
	}
}