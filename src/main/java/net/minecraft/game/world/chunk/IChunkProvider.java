package net.minecraft.game.world.chunk;

public interface IChunkProvider {
    boolean chunkExists(int i1, int i2);

    Chunk provideChunk(int i1, int i2);

    void populate(IChunkProvider iChunkProvider1, int i2, int i3);

    void saveChunks(boolean z1);

    boolean unload100OldestChunks();
}