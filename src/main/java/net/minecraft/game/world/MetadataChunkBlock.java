package net.minecraft.game.world;

public final class MetadataChunkBlock {
    public final EnumSkyBlock skyBlock;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public MetadataChunkBlock(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int axZ) {
        this.skyBlock = skyBlock;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = axZ;
    }
}