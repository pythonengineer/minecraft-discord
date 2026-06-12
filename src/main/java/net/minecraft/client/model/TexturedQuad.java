package net.minecraft.client.model;

import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.physics.Vec3D;

public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

    private TexturedQuad(PositionTextureVertex[] vertexPositions) {
        this.nVertices = 0;
        this.invertNormal = false;
        this.vertexPositions = vertexPositions;
        this.nVertices = vertexPositions.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertexPositions, int u1, int v1, int u2, int v2) {
        this(vertexPositions);
        vertexPositions[0] = vertexPositions[0].setTexturePosition((float)u2 / 64.0F - 0.0015625F, (float)v1 / 32.0F + 0.003125F);
        vertexPositions[1] = vertexPositions[1].setTexturePosition((float)u1 / 64.0F + 0.0015625F, (float)v1 / 32.0F + 0.003125F);
        vertexPositions[2] = vertexPositions[2].setTexturePosition((float)u1 / 64.0F + 0.0015625F, (float)v2 / 32.0F - 0.003125F);
        vertexPositions[3] = vertexPositions[3].setTexturePosition((float)u2 / 64.0F - 0.0015625F, (float)v2 / 32.0F - 0.003125F);
    }

    public void flipFace() {
        PositionTextureVertex[] positionTextureVertex1 = new PositionTextureVertex[this.vertexPositions.length];

        for(int i2 = 0; i2 < this.vertexPositions.length; ++i2) {
            positionTextureVertex1[i2] = this.vertexPositions[this.vertexPositions.length - i2 - 1];
        }

        this.vertexPositions = positionTextureVertex1;
    }

    public void draw(Tessellator tessellator, float partialTicks) {
        Vec3D vec3D3 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[0].vector3D);
        Vec3D vec3D4 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[2].vector3D);
        Vec3D vec3D5 = vec3D4.crossProduct(vec3D3).normalize();
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
        if(this.invertNormal) {
            tessellator.setNormal(-((float)vec3D5.xCoord), -((float)vec3D5.yCoord), -((float)vec3D5.zCoord));
        } else {
            tessellator.setNormal((float)vec3D5.xCoord, (float)vec3D5.yCoord, (float)vec3D5.zCoord);
        }

        for(int i6 = 0; i6 < 4; ++i6) {
            PositionTextureVertex positionTextureVertex7 = this.vertexPositions[i6];
            tessellator.addVertexWithUV((double)((float)positionTextureVertex7.vector3D.xCoord * partialTicks), (double)((float)positionTextureVertex7.vector3D.yCoord * partialTicks), (double)((float)positionTextureVertex7.vector3D.zCoord * partialTicks), (double)positionTextureVertex7.texturePositionX, (double)positionTextureVertex7.texturePositionY);
        }

        tessellator.draw();
    }
}
