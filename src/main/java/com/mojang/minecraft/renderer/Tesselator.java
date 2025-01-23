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

    public final void color(float f1, float f2, float f3) {
        if(!this.noColor) {
            this.hasColor = true;
            this.r = f1;
            this.g = f2;
            this.b = f3;
        }
    }

    public final void color(int i1, int i2, int i3) {
        byte b10001 = (byte)i1;
        byte b10002 = (byte)i2;
        byte b6 = (byte)i3;
        byte b5 = b10002;
        byte b4 = b10001;
        if(!this.noColor) {
            this.hasColor = true;
            this.r = (float)(b4 & 255) / 255.0F;
            this.g = (float)(b5 & 255) / 255.0F;
            this.b = (float)(b6 & 255) / 255.0F;
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
        int r = c >> 16 & 255;
        int g = c >> 8 & 255;
        int b = c & 255;
        this.color(r, g, b);
    }

    public void noColor() {
        this.noColor = true;
    }
}
