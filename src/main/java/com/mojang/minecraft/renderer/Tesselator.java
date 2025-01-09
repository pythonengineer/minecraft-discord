package com.mojang.minecraft.renderer;

import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;

public final class Tesselator {
    public static WorldRenderer worldRenderer = new WorldRenderer(2097152);
    public static Tesselator tesselator = new Tesselator();
    private boolean hasColor;
    private boolean noColor = false;
    private float r;
    private float g;
    private float b;

    public final void end() {
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

    public final void begin(VertexFormat fmt) {
        this.clear();
        worldRenderer.begin(GL11.GL_QUADS, fmt);
        this.noColor = false;
    }

    public final void color(int var1, int var2, int var3) {
        this.color((byte)var1, (byte)var2, (byte)var3);
    }

    public final void color(byte var1, byte var2, byte var3) {
        if (!this.noColor) {
            this.hasColor = true;
            this.r = (float)(var1 & 255) / 255.0F;
            this.g = (float)(var2 & 255) / 255.0F;
            this.b = (float)(var3 & 255) / 255.0F;
        }
    }

    public final void tex(float u, float v) {
        worldRenderer.tex(u, v);
    }

    public final void vertexUV(float var1, float var2, float var3, float var4, float var5) {
        this.tex(var4, var5);
        this.vertex(var1, var2, var3);
    }

    public final void vertex(float x, float y, float z) {
        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, 1);
        }
        worldRenderer.pos(x, y, z);
        worldRenderer.endVertex();
    }

    public final void color(int var1) {
        int var2 = var1 >> 16 & 255;
        int var3 = var1 >> 8 & 255;
        var1 &= 255;
        this.color(var2, var3, var1);
    }

    public final void noColor() {
        this.noColor = true;
    }
}
