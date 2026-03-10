package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public final class PositionTextureVertex {
    public Vec3D vector3D;
    public float texturePositionX;
    public float texturePositionY;

    public PositionTextureVertex(float posX, float posY, float posZ, float u, float v) {
        this(new Vec3D((double)posX, (double)posY, (double)posZ), u, v);
    }

    public final PositionTextureVertex setTexturePosition(float u, float v) {
        return new PositionTextureVertex(this, u, v);
    }

    private PositionTextureVertex(PositionTextureVertex positionTextureVertex, float u, float v) {
        this.vector3D = positionTextureVertex.vector3D;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }

    private PositionTextureVertex(Vec3D positionVector, float u, float v) {
        this.vector3D = positionVector;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }
}