package com.mojang.minecraft.renderer;

import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;

public class Tesselator {
    public static WorldRenderer worldRenderer = new WorldRenderer(2097152);
    public static Tesselator instance = new Tesselator();
    private boolean hasColor;
    private boolean noColor = false;
    private float r;
    private float g;
    private float b;

    public void flush() {
        worldRenderer.finishDrawing();
        int cunt = worldRenderer.getVertexCount();
        if (cunt > 0) {
            VertexFormat fmt = worldRenderer.getVertexFormat();
            ByteBuffer buf = worldRenderer.getByteBuffer();
            buf.position(0).limit(cunt * fmt.attribStride);
            GL11.renderBuffer(buf, fmt.mcAttribBits, worldRenderer.getDrawMode(), cunt);
            worldRenderer.reset();
        }

        this.clear();
    }

    private void clear() {
        this.hasColor = false;
        this.r = 0.0f;
        this.g = 0.0f;
        this.b = 0.0f;
    }

    public void init(VertexFormat fmt) {
        this.clear();
        worldRenderer.begin(GL11.GL_QUADS, fmt);
        this.noColor = false;
    }

    public void tex(float u, float v) {
        worldRenderer.tex(u, v);
    }

    public void color(float r, float g, float b) {
        if (!this.noColor) {
            this.hasColor = true;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public void vertexUV(float x, float y, float z, float u, float v) {
        this.tex(u, v);
        this.vertex(x, y, z);
    }

    public void vertex(float x, float y, float z) {
        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, 1);
        }
        worldRenderer.pos(x, y, z);
        worldRenderer.endVertex();
    }

    public void color(int c) {
        float r = (float)(c >> 16 & 255) / 255.0F;
        float g = (float)(c >> 8 & 255) / 255.0F;
        float b = (float)(c & 255) / 255.0F;
        this.color(r, g, b);
    }

    public void noColor() {
        this.noColor = true;
    }
}
