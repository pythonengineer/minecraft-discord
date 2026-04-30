package net.minecraft.game.world.chunk.loader;

import java.io.IOException;

import net.minecraft.game.world.World;
import net.minecraft.game.world.chunk.Chunk;

public final class IsomChunkLoader implements IChunkLoader {
    private IChunkLoader chunkLoader;
    private IChunkLoader chunkLoader2;

    public IsomChunkLoader(IChunkLoader iChunkLoader1, IChunkLoader iChunkLoader2) {
        this.chunkLoader = iChunkLoader1;
        this.chunkLoader2 = iChunkLoader2;
    }

    public final void saveExtraData() {
        this.chunkLoader.saveExtraData();
    }

    public final Chunk loadChunk(World world1, int i2, int i3) throws IOException {
        Chunk chunk4;
        if((chunk4 = this.chunkLoader.loadChunk(world1, i2, i3)) == null && (chunk4 = this.chunkLoader2.loadChunk(world1, i2, i3)) != null) {
            chunk4.isModified = true;
        }

        return chunk4;
    }

    public final void saveChunk(World world1, Chunk chunk2) throws IOException {
        this.chunkLoader.saveChunk(world1, chunk2);
    }

    public final void chunkTick() {
        this.chunkLoader.chunkTick();
    }

    public final void saveExtraChunkData(World world1, Chunk chunk2) throws IOException {
        this.chunkLoader.saveExtraChunkData(world1, chunk2);
    }
}