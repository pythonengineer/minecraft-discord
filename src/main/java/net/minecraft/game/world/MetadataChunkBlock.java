package net.minecraft.game.world;

public final class MetadataChunkBlock {
    public final EnumSkyBlock skyBlock;
    public final int x;
    public final int y;
    public final int z;
    public final int maxX;
    public final int maxY;
    public final int maxZ;

    public MetadataChunkBlock(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.skyBlock = var1;
        this.x = var2;
        this.y = var3;
        this.z = var4;
        this.maxX = var5;
        this.maxY = var6;
        this.maxZ = var7;
    }
}
