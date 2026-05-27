package net.minecraft.game.world.chunk.loader;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.CompressedStreamTools;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.NibbleArray;

public class ChunkLoader implements IChunkLoader {
    private VFile2 saveDir;
    private boolean createIfNecessary;

    public ChunkLoader(VFile2 file1, boolean z2) {
        this.saveDir = file1;
        this.createIfNecessary = z2;
    }

    private VFile2 chunkFileForXZ(int i1, int i2) {
        String string3 = "c." + Integer.toString(i1, 36) + "." + Integer.toString(i2, 36) + ".dat";
        String string4 = Integer.toString(i1 & 63, 36);
        String string5 = Integer.toString(i2 & 63, 36);
        VFile2 file6 = new VFile2(this.saveDir, string4);
        if(!file6.exists()) {
            if(!this.createIfNecessary) {
                return null;
            }
        }

        file6 = new VFile2(file6, string5);
        if(!file6.exists()) {
            if(!this.createIfNecessary) {
                return null;
            }
        }

        file6 = new VFile2(file6, string3);
        return !file6.exists() && !this.createIfNecessary ? null : file6;
    }

    public Chunk loadChunk(World world1, int i2, int i3) {
        VFile2 file4 = this.chunkFileForXZ(i2, i3);
        if(file4 != null && file4.exists()) {
            try (InputStream fis = file4.getInputStream()) {
                NBTTagCompound nBTTagCompound6 = CompressedStreamTools.readCompressed(fis);
                return loadChunkIntoWorldFromCompound(world1, nBTTagCompound6.getCompoundTag("Level"));
            } catch (IOException exception7) {
                exception7.printStackTrace();
            }
        }

        return null;
    }

    public void saveChunk(World world1, Chunk chunk2) {
        VFile2 file3 = this.chunkFileForXZ(chunk2.xPosition, chunk2.zPosition);
        if(file3.exists()) {
            world1.sizeOnDisk -= file3.length();
        }

        try {
            VFile2 file4 = new VFile2(this.saveDir, "tmp_chunk.dat");
            OutputStream fos = file4.getOutputStream();
            NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
            NBTTagCompound nBTTagCompound6 = new NBTTagCompound();
            nBTTagCompound5.setTag("Level", nBTTagCompound6);
            this.storeChunkInCompound(chunk2, world1, nBTTagCompound6);
            CompressedStreamTools.writeCompressed(nBTTagCompound5, fos);
            fos.close();
            if(file3.exists()) {
                file3.delete();
            }

            file4.renameTo(file3);
            world1.sizeOnDisk += file3.length();
        } catch (IOException exception7) {
            exception7.printStackTrace();
        }

    }

    public void storeChunkInCompound(Chunk chunk1, World world2, NBTTagCompound nBTTagCompound3) {
        nBTTagCompound3.setInteger("xPos", chunk1.xPosition);
        nBTTagCompound3.setInteger("zPos", chunk1.zPosition);
        nBTTagCompound3.setLong("LastUpdate", world2.worldTime);
        nBTTagCompound3.setByteArray("Blocks", chunk1.blocks);
        nBTTagCompound3.setByteArray("Data", chunk1.data.data);
        nBTTagCompound3.setByteArray("SkyLight", chunk1.skylightMap.data);
        nBTTagCompound3.setByteArray("BlockLight", chunk1.blocklightMap.data);
        nBTTagCompound3.setByteArray("HeightMap", chunk1.heightMap);
        nBTTagCompound3.setBoolean("TerrainPopulated", chunk1.isTerrainPopulated);
        chunk1.hasEntities = false;
        NBTTagList nBTTagList4 = new NBTTagList();

        Iterator iterator6;
        NBTTagCompound nBTTagCompound8;
        for(int i5 = 0; i5 < chunk1.entities.length; ++i5) {
            iterator6 = chunk1.entities[i5].iterator();

            while(iterator6.hasNext()) {
                Entity entity7 = (Entity)iterator6.next();
                chunk1.hasEntities = true;
                nBTTagCompound8 = new NBTTagCompound();
                if(entity7.addEntityID(nBTTagCompound8)) {
                    nBTTagList4.setTag(nBTTagCompound8);
                }
            }
        }

        nBTTagCompound3.setTag("Entities", nBTTagList4);
        NBTTagList nBTTagList9 = new NBTTagList();
        iterator6 = chunk1.chunkTileEntityMap.values().iterator();

        while(iterator6.hasNext()) {
            TileEntity tileEntity10 = (TileEntity)iterator6.next();
            nBTTagCompound8 = new NBTTagCompound();
            tileEntity10.writeToNBT(nBTTagCompound8);
            nBTTagList9.setTag(nBTTagCompound8);
        }

        nBTTagCompound3.setTag("TileEntities", nBTTagList9);
    }

    public static Chunk loadChunkIntoWorldFromCompound(World world0, NBTTagCompound nBTTagCompound1) {
        int i2 = nBTTagCompound1.getInteger("xPos");
        int i3 = nBTTagCompound1.getInteger("zPos");
        Chunk chunk4 = new Chunk(world0, i2, i3);
        chunk4.blocks = nBTTagCompound1.getByteArray("Blocks");
        chunk4.data = new NibbleArray(nBTTagCompound1.getByteArray("Data"));
        chunk4.skylightMap = new NibbleArray(nBTTagCompound1.getByteArray("SkyLight"));
        chunk4.blocklightMap = new NibbleArray(nBTTagCompound1.getByteArray("BlockLight"));
        chunk4.heightMap = nBTTagCompound1.getByteArray("HeightMap");
        chunk4.isTerrainPopulated = nBTTagCompound1.getBoolean("TerrainPopulated");
        if(!chunk4.data.isValid()) {
            chunk4.data = new NibbleArray(chunk4.blocks.length);
        }

        if(chunk4.heightMap == null || !chunk4.skylightMap.isValid()) {
            chunk4.heightMap = new byte[256];
            chunk4.skylightMap = new NibbleArray(chunk4.blocks.length);
            chunk4.generateHeightMap();
        }

        if(!chunk4.blocklightMap.isValid()) {
            chunk4.blocklightMap = new NibbleArray(chunk4.blocks.length);
            chunk4.doNothing();
        }

        NBTTagList nBTTagList5 = nBTTagCompound1.getTagList("Entities");
        if(nBTTagList5 != null) {
            for(int i6 = 0; i6 < nBTTagList5.tagCount(); ++i6) {
                NBTTagCompound nBTTagCompound7 = (NBTTagCompound)nBTTagList5.tagAt(i6);
                Entity entity8 = EntityList.createEntityFromNBT(nBTTagCompound7, world0);
                chunk4.hasEntities = true;
                if(entity8 != null) {
                    chunk4.addEntity(entity8);
                }
            }
        }

        NBTTagList nBTTagList10 = nBTTagCompound1.getTagList("TileEntities");
        if(nBTTagList10 != null) {
            for(int i11 = 0; i11 < nBTTagList10.tagCount(); ++i11) {
                NBTTagCompound nBTTagCompound12 = (NBTTagCompound)nBTTagList10.tagAt(i11);
                TileEntity tileEntity9 = TileEntity.createAndLoadEntity(nBTTagCompound12);
                if(tileEntity9 != null) {
                    chunk4.addTileEntity(tileEntity9);
                }
            }
        }

        return chunk4;
    }

    public void chunkTick() {
    }

    public void saveExtraData() {
    }

    public void saveExtraChunkData(World world1, Chunk chunk2) {
    }
}
