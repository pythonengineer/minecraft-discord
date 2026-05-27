package net.minecraft.game.world.material;

public class MaterialLiquid extends Material {
    public boolean getIsLiquid() {
        return true;
    }

    public boolean isSolid() {
        return false;
    }
}
