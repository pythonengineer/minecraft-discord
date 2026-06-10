package net.minecraft.client.net;

import java.util.LinkedList;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.chunk.ChunkProviderClient;
import net.minecraft.game.world.chunk.IChunkProvider;

public class WorldClient extends World {
    private LinkedList blocksToReceive = new LinkedList();
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

        for(int i1 = 0; i1 < this.blocksToReceive.size(); ++i1) {
            WorldBlockPositionType worldBlockPositionType2 = (WorldBlockPositionType)this.blocksToReceive.get(i1);
            if(--worldBlockPositionType2.acceptCountdown == 0) {
                super.setBlockAndMetadata(worldBlockPositionType2.posX, worldBlockPositionType2.posY, worldBlockPositionType2.posZ, worldBlockPositionType2.blockID, worldBlockPositionType2.metadata);
                super.markBlockNeedsUpdate(worldBlockPositionType2.posX, worldBlockPositionType2.posY, worldBlockPositionType2.posZ);
                this.blocksToReceive.remove(i1--);
            }
        }

    }

    public void invalidateBlockReceiveRegion(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for(int i7 = 0; i7 < this.blocksToReceive.size(); ++i7) {
            WorldBlockPositionType worldBlockPositionType8 = (WorldBlockPositionType)this.blocksToReceive.get(i7);
            if(worldBlockPositionType8.posX >= minX && worldBlockPositionType8.posY >= minY && worldBlockPositionType8.posZ >= minZ && worldBlockPositionType8.posX <= maxX && worldBlockPositionType8.posY <= maxY && worldBlockPositionType8.posZ <= maxZ) {
                this.blocksToReceive.remove(i7--);
            }
        }

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

    public void scheduleBlockUpdate(int x, int y, int z, int id) {
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
        this.spawnEntityInWorld(entity);
        this.entityHashTable.addKey(id, entity);
    }

    public Entity getEntityByID(int id) {
        return (Entity)this.entityHashTable.lookup(id);
    }

    public Entity removeEntityFromWorld(int id) {
        Entity entity2 = (Entity)this.entityHashTable.removeObject(id);
        if(entity2 != null) {
            this.setEntityDead(entity2);
        }

        return entity2;
    }

    public boolean setBlockMetadata(int i1, int i2, int i3, int i4) {
        int i5 = this.getBlockId(i1, i2, i3);
        int i6 = this.getBlockMetadata(i1, i2, i3);
        if(super.setBlockMetadata(i1, i2, i3, i4)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, i1, i2, i3, i5, i6));
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlockAndMetadata(int x, int y, int z, int id, int metadata) {
        int i6 = this.getBlockId(x, y, z);
        int i7 = this.getBlockMetadata(x, y, z);
        if(super.setBlockAndMetadata(x, y, z, id, metadata)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, x, y, z, i6, i7));
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlock(int x, int y, int z, int id) {
        int i5 = this.getBlockId(x, y, z);
        int i6 = this.getBlockMetadata(x, y, z);
        if(super.setBlock(x, y, z, id)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, x, y, z, i5, i6));
            return true;
        } else {
            return false;
        }
    }

    public boolean handleBlockChange(int x, int y, int z, int id, int metadata) {
        this.invalidateBlockReceiveRegion(x, y, z, x, y, z);
        if(super.setBlockAndMetadata(x, y, z, id, metadata)) {
            this.notifyBlockChange(x, y, z, id);
            return true;
        } else {
            return false;
        }
    }
}
