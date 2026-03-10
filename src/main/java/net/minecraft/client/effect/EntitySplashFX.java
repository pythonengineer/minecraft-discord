package net.minecraft.client.effect;

import net.minecraft.game.world.World;

public final class EntitySplashFX extends EntityRainFX {
    public EntitySplashFX(World world1, double d2, double d4, double d6) {
        super(world1, d2, d4, d6);
        this.particleGravity = 0.04F;
        ++this.particleTextureIndex;
    }
}