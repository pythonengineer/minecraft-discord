package net.minecraft.game.world;

public final class ChunkPosition {
    public final int x;
    public final int y;
    public final int z;

    public ChunkPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final boolean equals(Object chunkPos) {
        ChunkPosition chunkPos1;
        return chunkPos instanceof ChunkPosition ? (chunkPos1 = (ChunkPosition)chunkPos).x == this.x && chunkPos1.y == this.y && chunkPos1.z == this.z : false;
    }

    public final int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}