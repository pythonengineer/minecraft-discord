package net.minecraft.game.level;

public interface IWorldAccess {
    void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3);

    void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6);

    void loadRenderers();

    void playSound(String var1, float var2, float var3, float var4, float var5, float var6);

    void spawnParticle(String var1, float var2, float var3, float var4, float var5, float var6, float var7);
}
