package net.minecraft.game.world.chunk;

import net.minecraft.client.IProgressUpdate;

public interface IChunkProvider {
    boolean chunkExists(int i1, int i2);

    Chunk provideChunk(int i1, int i2);

    void populate(IChunkProvider iChunkProvider1, int i2, int i3);

    boolean saveChunks(boolean flag, IProgressUpdate loadingScreen);

    boolean unload100OldestChunks();

    boolean canSave();
}
