package net.minecraft.client.net;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.chunk.ChunkProviderClient;
import net.minecraft.game.world.chunk.IChunkProvider;

public class WorldClient extends World {
    private NetClientHandler sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private MCHashTable entityHashTable = new MCHashTable();

    public WorldClient(NetClientHandler netClientHandler) {
        super("MpServer");
        this.sendQueue = netClientHandler;
        this.spawnX = 8;
        this.spawnY = 64;
        this.spawnZ = 8;
    }

    public void tick() {
        this.sendQueue.processReadPackets();
    }

    protected IChunkProvider getChunkProvider(VFile2 file1) {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    public void setSpawnLocation() {
        this.spawnX = 8;
        this.spawnY = 64;
        this.spawnZ = 8;
    }

    protected void updateBlocksAndPlayCaveSounds() {
    }

    public void scheduleBlockUpdate(int i1, int i2, int i3, int i4) {
    }

    public boolean tickUpdates(boolean z1) {
        return false;
    }

    public void doPreChunk(int x, int z, boolean mode) {
        if(mode) {
            this.clientChunkProvider.loadChunk(x, z);
        } else {
            this.clientChunkProvider.unloadChunk(x, z);
        }

    }

    public void addEntityToWorld(int id, Entity entity) {
        this.entityHashTable.addKey(id, entity);
    }

    public Entity getEntityByID(int id) {
        return (Entity)this.entityHashTable.lookup(id);
    }

    public Entity removeEntityFromWorld(int id) {
        return (Entity)this.entityHashTable.removeObject(id);
    }
}
