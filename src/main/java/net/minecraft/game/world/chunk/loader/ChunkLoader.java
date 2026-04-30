package net.minecraft.game.world.chunk.loader;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.UnexpectedThrowable;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.NibbleArray;

public final class ChunkLoader implements IChunkLoader {
    private VFile2 saveDir;
    private boolean createIfNecessary;

    public ChunkLoader(VFile2 file1, boolean z2) {
        this.saveDir = file1;
        this.createIfNecessary = true;
    }

    private VFile2 chunkFileForXZ(int i1, int i2) {
        String string3 = "c." + Integer.toString(i1, 36) + "." + Integer.toString(i2, 36) + ".dat";
        String string4 = Integer.toString(i1 & 63, 36);
        String string6 = Integer.toString(i2 & 63, 36);
        VFile2 file5;
        if(!(file5 = new VFile2(this.saveDir, string4)).exists()) {
            if(!this.createIfNecessary) {
                return null;
            }
        }

        if(!(file5 = new VFile2(file5, string6)).exists()) {
            if(!this.createIfNecessary) {
                return null;
            }
        }

        return !(file5 = new VFile2(file5, string3)).exists() && !this.createIfNecessary ? null : file5;
    }

    public final Chunk loadChunk(World world1, int i2, int i3) {
        VFile2 file5;
        if((file5 = this.chunkFileForXZ(i2, i3)) != null && file5.exists()) {
            try (InputStream fis = file5.getInputStream()) {
                NBTTagCompound nBTTagCompound6 = UnexpectedThrowable.readCompressed(fis);
                return loadChunkIntoWorldFromCompound(world1, nBTTagCompound6.getCompoundTag("Level"));
            } catch (IOException exception4) {
                exception4.printStackTrace();
            }
        }

        return null;
    }

    public final void saveChunk(World world1, Chunk chunk2) {
        VFile2 file3;
        if((file3 = this.chunkFileForXZ(chunk2.xPosition, chunk2.zPosition)).exists()) {
            world1.sizeOnDisk -= file3.length();
        }

        try (OutputStream fos = file3.getOutputStream()) {
            NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
            NBTTagCompound nBTTagCompound6 = new NBTTagCompound();
            nBTTagCompound5.setTag("Level", nBTTagCompound6);
            storeChunkInCompound(chunk2, world1, nBTTagCompound6);
            UnexpectedThrowable.writeCompressed(nBTTagCompound5, fos);
            world1.sizeOnDisk += file3.length();
        } catch (IOException exception7) {
            exception7.printStackTrace();
        }
    }

    private static void storeChunkInCompound(Chunk chunk0, World world1, NBTTagCompound nBTTagCompound2) {
        nBTTagCompound2.setInteger("xPos", chunk0.xPosition);
        nBTTagCompound2.setInteger("zPos", chunk0.zPosition);
        nBTTagCompound2.setLong("LastUpdate", world1.worldTime);
        nBTTagCompound2.setByteArray("Blocks", chunk0.blocks);
        nBTTagCompound2.setByteArray("Data", chunk0.data.data);
        nBTTagCompound2.setByteArray("SkyLight", chunk0.skylightMap.data);
        nBTTagCompound2.setByteArray("BlockLight", chunk0.blocklightMap.data);
        nBTTagCompound2.setByteArray("HeightMap", chunk0.heightMap);
        nBTTagCompound2.setBoolean("TerrainPopulated", chunk0.isTerrainPopulated);
        NBTTagList nBTTagList7 = new NBTTagList();

        Iterator iterator4;
        NBTTagCompound nBTTagCompound6;
        for(int i3 = 0; i3 < chunk0.entities.length; ++i3) {
            iterator4 = chunk0.entities[i3].iterator();

            while(iterator4.hasNext()) {
                Entity entity5 = (Entity)iterator4.next();
                nBTTagCompound6 = new NBTTagCompound();
                if(entity5.addEntityID(nBTTagCompound6)) {
                    nBTTagList7.setTag(nBTTagCompound6);
                }
            }
        }

        nBTTagCompound2.setTag("Entities", nBTTagList7);
        NBTTagList nBTTagList8 = new NBTTagList();
        iterator4 = chunk0.chunkTileEntityMap.values().iterator();

        while(iterator4.hasNext()) {
            TileEntity tileEntity9 = (TileEntity)iterator4.next();
            nBTTagCompound6 = new NBTTagCompound();
            tileEntity9.writeToNBT(nBTTagCompound6);
            nBTTagList8.setTag(nBTTagCompound6);
        }

        nBTTagCompound2.setTag("TileEntities", nBTTagList8);
    }

    private static Chunk loadChunkIntoWorldFromCompound(World world0, NBTTagCompound nBTTagCompound1) {
        int i2 = nBTTagCompound1.getInteger("xPos");
        int i3 = nBTTagCompound1.getInteger("zPos");
        Chunk chunk8;
        (chunk8 = new Chunk(world0, i2, i3)).blocks = nBTTagCompound1.getByteArray("Blocks");
        chunk8.data = new NibbleArray(nBTTagCompound1.getByteArray("Data"));
        chunk8.skylightMap = new NibbleArray(nBTTagCompound1.getByteArray("SkyLight"));
        chunk8.blocklightMap = new NibbleArray(nBTTagCompound1.getByteArray("BlockLight"));
        chunk8.heightMap = nBTTagCompound1.getByteArray("HeightMap");
        chunk8.isTerrainPopulated = nBTTagCompound1.getBoolean("TerrainPopulated");
        if(!chunk8.data.isValid()) {
            chunk8.data = new NibbleArray(chunk8.blocks.length);
        }

        if(chunk8.heightMap == null || !chunk8.skylightMap.isValid()) {
            chunk8.heightMap = new byte[256];
            chunk8.skylightMap = new NibbleArray(chunk8.blocks.length);
            chunk8.generateHeightMap();
        }

        if(!chunk8.blocklightMap.isValid()) {
            chunk8.blocklightMap = new NibbleArray(chunk8.blocks.length);
        }

        NBTTagList nBTTagList9;
        if((nBTTagList9 = nBTTagCompound1.getTagList("Entities")) != null) {
            for(int i4 = 0; i4 < nBTTagList9.tagCount(); ++i4) {
                Entity entity6;
                if((entity6 = EntityList.createEntityFromNBT((NBTTagCompound)nBTTagList9.tagAt(i4), world0)) != null) {
                    chunk8.addEntity(entity6);
                }
            }
        }

        NBTTagList nBTTagList10;
        if((nBTTagList10 = nBTTagCompound1.getTagList("TileEntities")) != null) {
            for(int i5 = 0; i5 < nBTTagList10.tagCount(); ++i5) {
                TileEntity tileEntity7;
                if((tileEntity7 = TileEntity.createAndLoadEntity((NBTTagCompound)nBTTagList10.tagAt(i5))) != null) {
                    chunk8.addTileEntity(tileEntity7);
                }
            }
        }

        return chunk8;
    }

    public final void chunkTick() {
    }

    public final void saveExtraData() {
    }

    public final void saveExtraChunkData(World world1, Chunk chunk2) {
    }
}