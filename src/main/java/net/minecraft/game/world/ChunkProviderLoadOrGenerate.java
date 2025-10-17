package net.minecraft.game.world;

public final class ChunkProviderLoadOrGenerate implements IChunkProvider {
    private IChunkProvider chunkProvider;
    private Chunk[] chunks = new Chunk[1024];

    public ChunkProviderLoadOrGenerate(IChunkProvider var1) {
        this.chunkProvider = var1;
    }

    public final boolean chunkExists(int var1, int var2) {
        int var3 = var1 & 31 | (var2 & 31) << 5;
        return this.chunks[var3] != null && this.chunks[var3].isAtLocation(var1, var2);
    }

    public final Chunk provideChunk(int var1, int var2) {
        int var3 = var1 & 31 | (var2 & 31) << 5;
        if(this.chunks[var3] == null || !this.chunks[var3].isAtLocation(var1, var2)) {
            this.chunks[var3] = this.chunkProvider.provideChunk(var1, var2);
        }

        return this.chunks[var3];
    }
}
