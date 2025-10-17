package net.minecraft.game.world;

public interface IChunkProvider {
    boolean chunkExists(int var1, int var2);

    Chunk provideChunk(int var1, int var2);
}
