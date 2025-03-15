package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

public final class SurvivalGameMode extends GameMode {
    private int x = -1;
    private int y = -1;
    private int z = -1;
    private int oDestroyProgress = 0;
    private int destroyProgress = 0;
    private int delay = 0;

    public SurvivalGameMode(Minecraft minecraft1) {
        super(minecraft1);
    }

    public final void destroyBlock(int x, int y, int z) {
        int i4 = this.minecraft.level.getTile(x, y, z);
        Tile.tiles[i4].spawnResources(this.minecraft.level, x, y, z);
        super.destroyBlock(x, y, z);
    }

    public final boolean removeResource(int quantity) {
        return this.minecraft.player.inventory.removeResource(quantity);
    }

    public final void startDestroyBlock(int x, int y, int z) {
        int i4;
        if((i4 = this.minecraft.level.getTile(x, y, z)) > 0 && Tile.tiles[i4].getDestroyProgress() == 0) {
            this.destroyBlock(x, y, z);
        }

    }

    public final void tick() {
        this.oDestroyProgress = 0;
        this.delay = 0;
    }

    public final void stopDestroyBlock(int x, int y, int z, int id) {
        if(this.delay > 0) {
            --this.delay;
        } else if(x == this.x && y == this.y && z == this.z) {
            int i5;
            if((i5 = this.minecraft.level.getTile(x, y, z)) != 0) {
                Tile tile6 = Tile.tiles[i5];
                this.destroyProgress = tile6.getDestroyProgress();
                tile6.destroy(this.minecraft.level, x, y, z, id, this.minecraft.particleEngine);
                ++this.oDestroyProgress;
                if(this.oDestroyProgress == this.destroyProgress + 1) {
                    this.destroyBlock(x, y, z);
                    this.oDestroyProgress = 0;
                    this.delay = 5;
                }

            }
        } else {
            this.oDestroyProgress = 0;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public final void render(float damageTime) {
        if(this.oDestroyProgress <= 0) {
            this.minecraft.levelRenderer.hurtTime = 0.0F;
        } else {
            this.minecraft.levelRenderer.hurtTime = ((float)this.oDestroyProgress + damageTime - 1.0F) / (float)this.destroyProgress;
        }
    }

    public final float getPickRange() {
        return 4.0F;
    }

    public final boolean removeResource(Player player, int quantity) {
        Tile tile3;
        if((tile3 = Tile.tiles[quantity]) == Tile.mushroom2 && this.minecraft.player.inventory.removeResource(quantity)) {
            player.hurt((Entity)null, 3);
            return true;
        } else if(tile3 == Tile.mushroom1 && this.minecraft.player.inventory.removeResource(quantity)) {
            player.heal(5);
            return true;
        } else {
            return false;
        }
    }
}