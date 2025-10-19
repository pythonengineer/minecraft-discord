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
        if(!this.chunkExists(var1, var2)) {
            if(this.chunks[var3] != null) {
                this.chunks[var3].unloadEntities();
            }

            this.chunks[var3] = this.chunkProvider.provideChunk(var1, var2);
            if(this.chunks[var3] != null) {
                this.chunks[var3].loadEntities();
            }

            if(this.chunkExists(var1 + 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 + 1, var2)) {
                this.populate(this, var1, var2);
            }

            if(this.chunkExists(var1 - 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 - 1, var2)) {
                this.populate(this, var1 - 1, var2);
            }

            if(this.chunkExists(var1 + 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 + 1, var2)) {
                this.populate(this, var1, var2 - 1);
            }

            if(this.chunkExists(var1 - 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 - 1, var2)) {
                this.populate(this, var1 - 1, var2 - 1);
            }
        }

        return this.chunks[var3];
    }

    public final void populate(IChunkProvider var1, int var2, int var3) {
        this.chunkProvider.populate(var1, var2, var3);
    }
}
