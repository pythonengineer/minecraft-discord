package net.minecraft.client.render;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.VertexFormat;

public final class Tessellator {
    private static net.lax1dude.eaglercraft.opengl.WorldRenderer worldRenderer = new net.lax1dude.eaglercraft.opengl.WorldRenderer(2097152);
	private float r;
	private float g;
	private float b;
    private float a;
	private float nx;
	private float ny;
	private float nz;
    private double xOffset;
    private double yOffset;
    private double zOffset;
	private boolean hasColor = false;
    private boolean hasNormal = false;
    private boolean isColorDisabled = false;
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
        this.isColorDisabled = false;
    }

	public final void startDrawingQuads(VertexFormat fmt) {
		this.startDrawing(GL11.GL_QUADS, fmt);
	}

	public final void setColorOpaque_F(float var1, float var2, float var3) {
        this.setColorOpaque((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F));
	}

    public final void setColorRGBA_F(float var1, float var2, float var3, float var4) {
        this.setAreaTransparent((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F), (int)(var4 * 255.0F));
    }

    private void setColorOpaque(int var1, int var2, int var3) {
        this.setAreaTransparent(var1, var2, var3, 255);
    }

    private void setAreaTransparent(int var1, int var2, int var3, int var4) {
        if(!this.isColorDisabled) {
            if(var1 > 255) {
                var1 = 255;
            }

            if(var2 > 255) {
                var2 = 255;
            }

            if(var3 > 255) {
                var3 = 255;
            }

            if(var4 > 255) {
                var4 = 255;
            }

            if(var1 < 0) {
                var1 = 0;
            }

            if(var2 < 0) {
                var2 = 0;
            }

            if(var3 < 0) {
                var3 = 0;
            }

            if(var4 < 0) {
                var4 = 0;
            }

            this.hasColor = true;
            this.r = (float)(var1 & 255) / 255.0F;
            this.g = (float)(var2 & 255) / 255.0F;
            this.b = (float)(var3 & 255) / 255.0F;
            this.a = (float)(var4 & 255) / 255.0F;
        }
    }

    public final void addUV(double u, double v) {
        worldRenderer.tex(u, v);
    }

	public final void addVertexWithUV(double x, double y, double z, double u, double v) {
	    this.addUV(u, v);
		this.addVertex(x, y, z);
	}

	public final void addVertex(double x, double y, double z) {
        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, this.a);
        }

        worldRenderer.pos(x + this.xOffset, y + this.yOffset, z + this.zOffset);
        if (this.hasNormal) {
            worldRenderer.normal(this.nx, this.ny, this.nz);
        }

        worldRenderer.endVertex();

	}

	public final void setColorOpaque_I(int var1) {
        int var2 = var1 >> 16 & 255;
        int var3 = var1 >> 8 & 255;
        var1 &= 255;
        this.setColorOpaque(var2, var3, var1);
	}

	public final void disableColor() {
        this.isColorDisabled = true;
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

    public final void setTranslationD(double var1, double var3, double var5) {
        this.xOffset = var1;
        this.yOffset = var3;
        this.zOffset = var5;
    }
}
