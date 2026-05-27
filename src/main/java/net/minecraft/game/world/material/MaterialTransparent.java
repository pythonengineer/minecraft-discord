package net.minecraft.game.world.material;

public class MaterialTransparent extends Material {
    public boolean isSolid() {
        return false;
    }

    public boolean getCanBlockGrass() {
        return false;
    }

    public boolean getIsSolid() {
        return false;
    }
}
