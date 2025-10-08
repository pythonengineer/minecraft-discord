package net.minecraft.game.world;

class Chunk {
    private final byte[] blocks;

    public Chunk(ChunkProviderGenerate var1, byte[] var2) {
        this.blocks = var2;
    }

    public final int getBlockId(int var1, int var2, int var3) {
        return this.blocks[var1 << 11 | var3 << 7 | var2];
    }

    public final void setBlockId(int var1, int var2, int var3, int var4) {
        this.blocks[var1 << 11 | var3 << 7 | var2] = (byte)var4;
    }
}
