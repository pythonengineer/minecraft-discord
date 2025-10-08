package net.minecraft.game.world;

final class ChunkPosition {
    public final int xPosition;
    public final int zPosition;

    public ChunkPosition(ChunkProviderGenerate var1, int var2, int var3) {
        this.xPosition = var2;
        this.zPosition = var3;
    }

    public final boolean equals(Object var1) {
        ChunkPosition var2 = (ChunkPosition)var1;
        return this.xPosition == var2.xPosition && this.zPosition == var2.zPosition;
    }

    public final int hashCode() {
        return this.zPosition + this.xPosition * 184621;
    }
}
