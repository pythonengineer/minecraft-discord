package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public class PositionTextureVertex {
    public Vec3D vector3D;
    public float texturePositionX;
    public float texturePositionY;

    public PositionTextureVertex(float posX, float posY, float posZ, float u, float v) {
        this(Vec3D.createVectorHelper((double)posX, (double)posY, (double)posZ), u, v);
    }

    public PositionTextureVertex setTexturePosition(float u, float v) {
        return new PositionTextureVertex(this, u, v);
    }

    public PositionTextureVertex(PositionTextureVertex positionTextureVertex, float u, float v) {
        this.vector3D = positionTextureVertex.vector3D;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }

    public PositionTextureVertex(Vec3D positionVector, float u, float v) {
        this.vector3D = positionVector;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }
}
