package com.mojang.minecraft.level.fx;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.SmokeParticle;

public class Smolder extends Entity {
    public static final long serialVersionUID = 0L;
    int life;
    int lifeTime;

    public Smolder(Level level, int x, int y, int z) {
        super(level);
        this.setSize(0.0F, 0.0F);
        this.setPos((float)x + 0.5F, (float)y + 0.1F, (float)z + 0.5F);
        this.heightOffset = 1.5F;
        this.makeStepSound = false;
        this.lifeTime = (int)(40.0D / (Math.random() * 0.8D + 0.2D));
    }

    public boolean isPickable() {
        return !this.removed;
    }

    public void tick() {
        float f1 = (float)(Math.random() - 0.5D);
        float f2 = (float)(Math.random() - 0.5D);
        float f3 = (float)this.life / (float)this.lifeTime;

        int i4;
        for(i4 = 0; i4 < 4; ++i4) {
            if(Math.random() > (double)f3) {
                this.level.particleEngine.addParticle(new SmokeParticle(this.level, this.x + f1, this.y, this.z + f2));
            }
        }

        if(this.life++ < this.lifeTime) {
            i4 = (int)this.x;
            int i5 = (int)(this.y - 0.3F);
            int i6 = (int)this.z;
            if((i5 = this.level.getTile(i4, i5, i6)) != 0 && Tile.tiles[i5].isSolid()) {
                return;
            }
        }

        this.remove();
    }
}