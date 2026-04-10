package net.minecraft.game.world.chunk;

import java.io.IOException;
import java.io.InputStream;

import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.world.World;

public final class ChunkProviderIso implements IChunkProvider {
    private Chunk[] chunkMapping = new Chunk[1024];
    private VFile2 saveDirectory;
    private World worldObj;
    private byte[] blankChunk = new byte[32768];

    public ChunkProviderIso(World world, VFile2 saveDir) {
        this.worldObj = world;
        this.saveDirectory = saveDir;
    }

    public final boolean chunkExists(int chunkX, int chunkZ) {
        int i3 = chunkX & 31 | (chunkZ & 31) << 5;
        return this.chunkMapping[i3] != null && this.chunkMapping[i3].isAtLocation(chunkX, chunkZ);
    }

    public final Chunk provideChunk(int chunkX, int chunkZ) {
        int i3 = chunkX & 31 | (chunkZ & 31) << 5;

        try {
            if(!this.chunkExists(chunkX, chunkZ)) {
                Chunk chunk4;
                if((chunk4 = this.loadChunk(chunkX, chunkZ)) == null) {
                    (chunk4 = new Chunk(this.worldObj, this.blankChunk, chunkX, chunkZ)).isChunkRendered = true;
                    chunk4.neverSave = true;
                }

                this.chunkMapping[i3] = chunk4;
            }

            return this.chunkMapping[i3];
        } catch (Exception exception5) {
            exception5.printStackTrace();
            return null;
        }
    }

    private Chunk loadChunk(int chunkX, int chunkZ) {
        int i3 = chunkZ;
        String string4 = "c." + Integer.toString(chunkX, 36) + "." + Integer.toString(chunkZ, 36) + ".dat";
        String chunkZ1 = Integer.toString(chunkX & 63, 36);
        String string9 = Integer.toString(i3 & 63, 36);
        VFile2 chunkX1;
        chunkX1 = new VFile2(this.saveDirectory, chunkZ1);
        chunkX1 = new VFile2(chunkX1, string9);
        if((chunkX1 = new VFile2(chunkX1, string4)).exists()) {
            try (InputStream fis = chunkX1.getInputStream()) {
                NBTTagCompound chunkX2 = LoadingScreenRenderer.read(fis);
                return Chunk.readChunkNBTData(this.worldObj, chunkX2.getCompoundTag("Level"));
            } catch (IOException exception5) {
                exception5.printStackTrace();
            }
        }

        return null;
    }

    public final void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
    }

    public final void saveChunks(boolean flag) {
    }

    public final boolean unload100OldestChunks() {
        return false;
    }

    public final boolean canSave() {
        return false;
    }
}