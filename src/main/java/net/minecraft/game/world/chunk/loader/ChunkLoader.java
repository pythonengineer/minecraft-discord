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

public final class ChunkLoader implements IChunkLoader {
    private VFile2 saveDir;
    private boolean createIfNecessary;

    public ChunkLoader(VFile2 file1, boolean z2) {
        this.saveDir = file1;
        this.createIfNecessary = z2;
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
                NBTTagCompound nBTTagCompound6 = CompressedStreamTools.read(fis);
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
            CompressedStreamTools.writeCompressed(nBTTagCompound5, fos);
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
        chunk0.hasEntities = false;
        NBTTagList nBTTagList7 = new NBTTagList();

        Iterator iterator4;
        NBTTagCompound nBTTagCompound6;
        for(int i3 = 0; i3 < chunk0.entities.length; ++i3) {
            iterator4 = chunk0.entities[i3].iterator();

            while(iterator4.hasNext()) {
                Entity entity5 = (Entity)iterator4.next();
                chunk0.hasEntities = true;
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
        Chunk chunk9;
        (chunk9 = new Chunk(world0, i2, i3)).blocks = nBTTagCompound1.getByteArray("Blocks");
        chunk9.data = new NibbleArray(nBTTagCompound1.getByteArray("Data"));
        chunk9.skylightMap = new NibbleArray(nBTTagCompound1.getByteArray("SkyLight"));
        chunk9.blocklightMap = new NibbleArray(nBTTagCompound1.getByteArray("BlockLight"));
        chunk9.heightMap = nBTTagCompound1.getByteArray("HeightMap");
        chunk9.isTerrainPopulated = nBTTagCompound1.getBoolean("TerrainPopulated");
        if(!chunk9.data.isValid()) {
            chunk9.data = new NibbleArray(chunk9.blocks.length);
        }

        if(chunk9.heightMap == null || !chunk9.skylightMap.isValid()) {
            chunk9.heightMap = new byte[256];
            chunk9.skylightMap = new NibbleArray(chunk9.blocks.length);
            chunk9.generateHeightMap();
        }

        if(!chunk9.blocklightMap.isValid()) {
            chunk9.blocklightMap = new NibbleArray(chunk9.blocks.length);
        }

        NBTTagList nBTTagList10;
        if((nBTTagList10 = nBTTagCompound1.getTagList("Entities")) != null) {
            for(int i4 = 0; i4 < nBTTagList10.tagCount(); ++i4) {
                Entity entity6 = EntityList.createEntityFromNBT((NBTTagCompound)nBTTagList10.tagAt(i4), world0);
                chunk9.hasEntities = true;
                if(entity6 != null) {
                    chunk9.addEntity(entity6);
                }
            }
        }

        NBTTagList nBTTagList11;
        if((nBTTagList11 = nBTTagCompound1.getTagList("TileEntities")) != null) {
            for(int i5 = 0; i5 < nBTTagList11.tagCount(); ++i5) {
                TileEntity tileEntity8;
                if((tileEntity8 = TileEntity.createAndLoadEntity((NBTTagCompound)nBTTagList11.tagAt(i5))) != null) {
                    i3 = tileEntity8.xCoord - (chunk9.xPosition << 4);
                    int i12 = tileEntity8.yCoord;
                    int i7 = tileEntity8.zCoord - (chunk9.zPosition << 4);
                    chunk9.setChunkBlockTileEntity(i3, i12, i7, tileEntity8);
                }
            }
        }

        return chunk9;
    }
}