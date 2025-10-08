package net.minecraft.game.world;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public class ChunkProviderGenerate {
    private EaglercraftRandom rand = new EaglercraftRandom();
    private NoiseGeneratorOctaves noiseGen1 = new NoiseGeneratorOctaves(16);
    private NoiseGeneratorOctaves noiseGen2 = new NoiseGeneratorOctaves(16);
    private NoiseGeneratorOctaves noiseGen3 = new NoiseGeneratorOctaves(8);
    private NoiseGeneratorOctaves noiseGen4 = new NoiseGeneratorOctaves(4);
    private NoiseGeneratorOctaves noiseGen5 = new NoiseGeneratorOctaves(4);
    private NoiseGeneratorOctaves noiseGen6 = new NoiseGeneratorOctaves(5);
    private Map chunks;

    public int getBlockAt(int var1, int var2, int var3) {
        return var2 < 0 ? Block.lavaStill.blockID : (var2 >= 128 ? 0 : this.getBlockAtXZ(var1 >>> 4, var3 >>> 4).getBlockId(var1 & 15, var2, var3 & 15));
    }

    public boolean setBlockAt(int var1, int var2, int var3, int var4) {
        if(var2 < 0) {
            return false;
        } else if(var2 >= 128) {
            return false;
        } else {
            Chunk var5 = this.getBlockAtXZ(var1 >>> 4, var3 >>> 4);
            var1 &= 15;
            var3 &= 15;
            if((var5.getBlockId(var1, var2, var3) & 255) == var4) {
                return false;
            } else {
                var5.setBlockId(var1, var2, var3, var4);
                return true;
            }
        }
    }

    public ChunkProviderGenerate() {
        new NoiseGeneratorOctaves(3);
        new NoiseGeneratorOctaves(3);
        new NoiseGeneratorOctaves(3);
        this.chunks = new HashMap();
    }

    private Chunk getBlockAtXZ(int var1, int var2) {
        ChunkPosition var3 = new ChunkPosition(this, var1, var2);
        Chunk var4 = (Chunk)this.chunks.get(var3);
        if(var4 == null) {
            var4 = new Chunk(this, this.provideChunk(var3));
            this.chunks.put(var3, var4);
        }

        return var4;
    }

    public byte[] provideChunk(ChunkPosition var1) {
        byte[] var2 = new byte[-Short.MIN_VALUE];
        int var3 = var1.xPosition << 4;
        int var14 = var1.zPosition << 4;
        int var4 = 0;

        for(int var5 = var3; var5 < var3 + 16; ++var5) {
            for(int var6 = var14; var6 < var14 + 16; ++var6) {
                int var7 = var5 / 1024;
                int var8 = var6 / 1024;
                float var9 = (float)(this.noiseGen1.generateNoise((double)((float)var5 / 0.03125F), 0.0D, (double)((float)var6 / 0.03125F)) - this.noiseGen2.generateNoise((double)((float)var5 / 0.015625F), 0.0D, (double)((float)var6 / 0.015625F))) / 512.0F / 4.0F;
                float var10 = (float)this.noiseGen5.generateNoise((double)((float)var5 / 4.0F), (double)((float)var6 / 4.0F));
                float var11 = (float)this.noiseGen6.generateNoise((double)((float)var5 / 8.0F), (double)((float)var6 / 8.0F)) / 8.0F;
                var10 = var10 > 0.0F ? (float)(this.noiseGen3.generateNoise((double)((float)var5 * 0.25714284F * 2.0F), (double)((float)var6 * 0.25714284F * 2.0F)) * (double)var11 / 4.0D) : (float)(this.noiseGen4.generateNoise((double)((float)var5 * 0.25714284F), (double)((float)var6 * 0.25714284F)) * (double)var11);
                int var15 = (int)(var9 + 64.0F + var10);
                if((float)this.noiseGen5.generateNoise((double)var5, (double)var6) < 0.0F) {
                    var15 = var15 / 2 << 1;
                    if((float)this.noiseGen5.generateNoise((double)(var5 / 5), (double)(var6 / 5)) < 0.0F) {
                        ++var15;
                    }
                }

                for(int var16 = 0; var16 < 128; ++var16) {
                    int var17 = 0;
                    if((var5 == 0 || var6 == 0) && var16 <= var15 + 2) {
                        var17 = Block.obsidian.blockID;
                    } else if(var16 == var15 + 1 && var15 >= 64 && Math.random() < 0.02D) {
                        var17 = Block.plantYellow.blockID;
                    } else if(var16 == var15 && var15 >= 64) {
                        var17 = Block.grass.blockID;
                    } else if(var16 <= var15 - 2) {
                        var17 = Block.stone.blockID;
                    } else if(var16 <= var15) {
                        var17 = Block.dirt.blockID;
                    } else if(var16 <= 64) {
                        var17 = Block.waterStill.blockID;
                    }

                    this.rand.setSeed((long)(var7 + var8 * 13871));
                    int var12 = (var7 << 10) + 128 + this.rand.nextInt(512);
                    int var13 = (var8 << 10) + 128 + this.rand.nextInt(512);
                    var12 = var5 - var12;
                    var13 = var6 - var13;
                    if(var12 < 0) {
                        var12 = -var12;
                    }

                    if(var13 < 0) {
                        var13 = -var13;
                    }

                    if(var13 > var12) {
                        var12 = var13;
                    }

                    var12 = 127 - var12;
                    if(var12 == 255) {
                        var12 = 1;
                    }

                    if(var12 < var15) {
                        var12 = var15;
                    }

                    if(var16 <= var12 && (var17 == 0 || var17 == Block.waterStill.blockID)) {
                        var17 = Block.brick.blockID;
                    }

                    if(var17 < 0) {
                        var17 = 0;
                    }

                    var2[var4++] = (byte)var17;
                }
            }
        }

        return var2;
    }
}
