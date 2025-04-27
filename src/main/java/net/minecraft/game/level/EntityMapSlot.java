package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

final class EntityMapSlot {
    private int posX;
    private int posY;
    private int posZ;
    private EntityMap entityMap;

    private EntityMapSlot(EntityMap var1, byte var2) {
        this.entityMap = var1;
    }

    public final EntityMapSlot init(float var1, float var2, float var3) {
        this.posX = (int)(var1 / 16.0F);
        this.posY = (int)(var2 / 16.0F);
        this.posZ = (int)(var3 / 16.0F);
        if(this.posX < 0) {
            this.posX = 0;
        }

        if(this.posY < 0) {
            this.posY = 0;
        }

        if(this.posZ < 0) {
            this.posZ = 0;
        }

        if(this.posX >= this.entityMap.width) {
            this.posX = this.entityMap.width - 1;
        }

        if(this.posY >= this.entityMap.depth) {
            this.posY = this.entityMap.depth - 1;
        }

        if(this.posZ >= this.entityMap.height) {
            this.posZ = this.entityMap.height - 1;
        }

        return this;
    }

    public final void add(Entity var1) {
        if(this.posX >= 0 && this.posY >= 0 && this.posZ >= 0) {
            this.entityMap.entityGrid[(this.posZ * this.entityMap.depth + this.posY) * this.entityMap.width + this.posX].add(var1);
        }

    }

    public final void remove(Entity var1) {
        if(this.posX >= 0 && this.posY >= 0 && this.posZ >= 0) {
            this.entityMap.entityGrid[(this.posZ * this.entityMap.depth + this.posY) * this.entityMap.width + this.posX].remove(var1);
        }

    }

    EntityMapSlot(EntityMap var1) {
        this(var1, (byte)0);
    }

    static int a(EntityMapSlot var0) {
        return var0.posX;
    }

    static int b(EntityMapSlot var0) {
        return var0.posY;
    }

    static int c(EntityMapSlot var0) {
        return var0.posZ;
    }
}
