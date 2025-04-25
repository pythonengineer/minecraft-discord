package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public final class PositionTextureVertex {
    private Vec3D vec3D;

    public PositionTextureVertex(float var1, float var2, float var3, float var4, float var5) {
        this(new Vec3D(var1, var2, var3), var4, var5);
    }

    public final PositionTextureVertex setTexturePosition(float var1, float var2) {
        return new PositionTextureVertex(this, var1, var2);
    }

    private PositionTextureVertex(PositionTextureVertex var1, float var2, float var3) {
        this.vec3D = var1.vec3D;
    }

    private PositionTextureVertex(Vec3D var1, float var2, float var3) {
        this.vec3D = var1;
    }
}
