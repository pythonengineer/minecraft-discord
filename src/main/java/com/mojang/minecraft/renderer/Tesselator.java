package com.mojang.minecraft.renderer;

import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;

public final class Tesselator {
    private static WorldRenderer worldRenderer = new WorldRenderer(2097152);
    public static Tesselator instance = new Tesselator();
    private boolean hasColor;
    private boolean noColor = false;
    private float r;
    private float g;
    private float b;

    public void end() {
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

    public void begin(int mode, VertexFormat fmt) {
        this.clear();
        worldRenderer.begin(mode, fmt);
        this.noColor = false;
    }

    public void begin(VertexFormat fmt) {
        this.begin(GL11.GL_QUADS, fmt);
    }

    public void tex(float u, float v) {
        worldRenderer.tex(u, v);
    }

    public final void color(float r, float g, float b) {
        if(!this.noColor) {
            this.hasColor = true;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public final void vertexUV(float x, float y, float z, float u, float v) {
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

    public final void color(int color) {
        int i2 = color >> 16 & 255;
        int i3 = color >> 8 & 255;
        color &= 255;
        int i10001 = i2;
        int i10002 = i3;
        i3 = color;
        i2 = i10002;
        color = i10001;
        byte b7 = (byte)color;
        byte b8 = (byte)i2;
        byte b6 = (byte)i3;
        byte b5 = b8;
        byte color1 = b7;
        if(!this.noColor) {
            this.hasColor = true;
            this.r = (float)(color1 & 255) / 255.0F;
            this.g = (float)(b5 & 255) / 255.0F;
            this.b = (float)(b6 & 255) / 255.0F;
        }

    }

    public void noColor() {
        this.noColor = true;
    }

    public final void normal(float x, float y, float z) {
        GL11.glNormal3f(x, y, z);
    }
}
