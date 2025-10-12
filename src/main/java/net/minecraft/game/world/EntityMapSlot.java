package net.minecraft.game.world;

class EntityMapSlot {
    private int xSlot;
    private int ySlot;
    private int zSlot;
    private EntityMap entityMap;

    private EntityMapSlot(EntityMap var1, byte var2) {
        this.entityMap = var1;
    }

    public final EntityMapSlot init(double var1, double var3, double var5) {
        this.xSlot = (int)(var1 / 8.0D);
        this.ySlot = (int)(var3 / 8.0D);
        this.zSlot = (int)(var5 / 8.0D);
        if(this.xSlot < 0) {
            this.xSlot = 0;
        }

        if(this.ySlot < 0) {
            this.ySlot = 0;
        }

        if(this.zSlot < 0) {
            this.zSlot = 0;
        }

        if(this.xSlot >= this.entityMap.width) {
            this.xSlot = this.entityMap.width - 1;
        }

        if(this.ySlot >= this.entityMap.depth) {
            this.ySlot = this.entityMap.depth - 1;
        }

        if(this.zSlot >= this.entityMap.height) {
            this.zSlot = this.entityMap.height - 1;
        }

        return this;
    }

    EntityMapSlot(EntityMap var1) {
        this(var1, (byte)0);
    }

    static int a(EntityMapSlot var0) {
        return var0.xSlot;
    }

    static int b(EntityMapSlot var0) {
        return var0.ySlot;
    }

    static int c(EntityMapSlot var0) {
        return var0.zSlot;
    }
}
