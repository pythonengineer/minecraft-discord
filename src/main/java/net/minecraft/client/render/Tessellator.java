package net.minecraft.client.render;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.VertexFormat;

public final class Tessellator {
    private static net.lax1dude.eaglercraft.opengl.WorldRenderer worldRenderer = new net.lax1dude.eaglercraft.opengl.WorldRenderer(2097152);
	private float r;
	private float g;
	private float b;
	private float nx;
	private float ny;
	private float nz;
	private boolean hasColor = false;
    private boolean hasNormal = false;
	private boolean drawMode = false;
	public static Tessellator instance = new Tessellator();

	public final void draw() {
        worldRenderer.finishDrawing();
        int cunt = worldRenderer.getVertexCount();
        if (cunt > 0) {
            VertexFormat fmt = worldRenderer.getVertexFormat();
            ByteBuffer buf = worldRenderer.getByteBuffer();
            buf.position(0).limit(cunt * fmt.attribStride);
            GL11.renderBuffer(buf, fmt.mcAttribBits, worldRenderer.getDrawMode(), cunt);
            worldRenderer.reset();
        }

		this.reset();
	}

	private void reset() {
	}

    public final void startDrawing(int mode, VertexFormat fmt) {
        this.reset();
        worldRenderer.begin(mode, fmt);
        this.hasNormal = false;
        this.hasColor = false;
        this.drawMode = false;
    }

	public final void startDrawingQuads(VertexFormat fmt) {
		this.startDrawing(GL11.GL_QUADS, fmt);
	}

	public final void setColorOpaque_F(float var1, float var2, float var3) {
		if(!this.drawMode) {
			this.hasColor = true;
			this.r = var1;
			this.g = var2;
			this.b = var3;
		}
	}

    public final void addUV(float u, float v) {
        worldRenderer.tex(u, v);
    }

	public final void addVertexWithUV(float x, float y, float z, float u, float v) {
	    this.addUV(u, v);
		this.addVertex(x, y, z);
	}

	public final void addVertex(float x, float y, float z) {
        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, 1);
        }

        worldRenderer.pos(x, y, z);
        if (this.hasNormal) {
            worldRenderer.normal(this.nx, this.ny, this.nz);
        }

        worldRenderer.endVertex();

	}

	public final void setColorOpaque_I(int var1) {
		int var2 = var1 >> 16 & 255;
		int var3 = var1 >> 8 & 255;
		var1 &= 255;
		int var10001 = var2;
		int var10002 = var3;
		var3 = var1;
		var2 = var10002;
		var1 = var10001;
		byte var7 = (byte)var1;
		byte var8 = (byte)var2;
		byte var6 = (byte)var3;
		byte var5 = var8;
		byte var4 = var7;
		if(!this.drawMode) {
			this.hasColor = true;
			this.r = (float)(var4 & 255) / 255.0F;
			this.g = (float)(var5 & 255) / 255.0F;
			this.b = (float)(var6 & 255) / 255.0F;
		}

	}

	public final void disableColor() {
		this.drawMode = true;
	}

    public void normal(float x, float y, float z) {
        this.hasNormal = true;
        this.nx = x;
        this.ny = y;
        this.nz = z;
    }

	public static void setNormal(float x, float y, float z) {
		GL11.glNormal3f(x, y, z);
	}
}
