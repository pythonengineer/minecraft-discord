package net.minecraft.client.model;

public final class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;

    private TexturedQuad(PositionTextureVertex[] vertexPositions) {
        this.vertexPositions = vertexPositions;
    }

    public TexturedQuad(PositionTextureVertex[] vertexPositions, int u1, int v1, int u2, int v2) {
        this(vertexPositions);
        vertexPositions[0] = vertexPositions[0].setTexturePosition((float)u2 / 64.0F - 0.0015625F, (float)v1 / 32.0F + 0.003125F);
        vertexPositions[1] = vertexPositions[1].setTexturePosition((float)u1 / 64.0F + 0.0015625F, (float)v1 / 32.0F + 0.003125F);
        vertexPositions[2] = vertexPositions[2].setTexturePosition((float)u1 / 64.0F + 0.0015625F, (float)v2 / 32.0F - 0.003125F);
        vertexPositions[3] = vertexPositions[3].setTexturePosition((float)u2 / 64.0F - 0.0015625F, (float)v2 / 32.0F - 0.003125F);
    }
}