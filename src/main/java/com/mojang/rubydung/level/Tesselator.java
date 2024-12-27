package com.mojang.rubydung.level;

import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;

public class Tesselator {
    public static WorldRenderer worldRenderer = new WorldRenderer(2097152);

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
    }

    public void init(VertexFormat fmt) {
        worldRenderer.begin(GL11.GL_QUADS, fmt);
    }

    public void tex(float u, float v) {
        worldRenderer.tex(u, v);
    }

    public void color(float r, float g, float b) {
        worldRenderer.color(r, g, b, 255);
    }

    public void vertex(float x, float y, float z) {
        worldRenderer.pos(x, y, z);
        worldRenderer.endVertex();
    }
}
