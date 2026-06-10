package net.minecraft.game.world.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.IProgressUpdate;
import net.minecraft.game.world.World;

public class ChunkProviderClient implements IChunkProvider {
    private Chunk blankChunk;
    private Map chunkMapping = new HashMap();
    private List chunkListing = new ArrayList();
    private World worldObj;

    public ChunkProviderClient(World worldObj) {
        this.blankChunk = new Chunk(worldObj, new byte[32768], 0, 0);
        this.blankChunk.isChunkRendered = true;
        this.blankChunk.neverSave = true;
        this.worldObj = worldObj;
    }

    public boolean chunkExists(int i1, int i2) {
        ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(i1, i2);
        return this.chunkMapping.containsKey(chunkCoordinates3);
    }

    public void unloadChunk(int chunkX, int chunkZ) {
        Chunk chunk3 = this.provideChunk(chunkX, chunkZ);
        if(!chunk3.isChunkRendered) {
            chunk3.onChunkUnload();
        }

        this.chunkMapping.remove(new ChunkCoordinates(chunkX, chunkZ));
        this.chunkListing.remove(chunk3);
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(chunkX, chunkZ);
        Chunk chunk4 = new Chunk(this.worldObj, new byte[32768], chunkX, chunkZ);
        this.chunkMapping.put(chunkCoordinates3, chunk4);
        return chunk4;
    }

    public Chunk provideChunk(int i1, int i2) {
        ChunkCoordinates chunkCoordinates3 = new ChunkCoordinates(i1, i2);
        Chunk chunk4 = (Chunk)this.chunkMapping.get(chunkCoordinates3);
        return chunk4 == null ? this.blankChunk : chunk4;
    }

    public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
        return true;
    }

    public boolean unload100OldestChunks() {
        return false;
    }

    public boolean canSave() {
        return false;
    }

    public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
    }
}
