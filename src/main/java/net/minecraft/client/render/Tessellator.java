package net.minecraft.client.render;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.VertexFormat;

public class Tessellator {
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

	public void draw() {
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

    public void startDrawing(int mode, VertexFormat fmt) {
        this.reset();
        worldRenderer.begin(mode, fmt);
        this.hasNormal = false;
        this.hasColor = false;
        this.isColorDisabled = false;
    }

	public void startDrawingQuads(VertexFormat fmt) {
		this.startDrawing(GL11.GL_QUADS, fmt);
	}

    public void setColorOpaque_F(float r, float g, float b) {
        this.setColorOpaque((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F));
    }

    public void setColorRGBA_F(float r, float g, float b, float a) {
        this.setColorRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
    }

    private void setColorOpaque(int r, int g, int b) {
        this.setColorRGBA(r, g, b, 255);
    }

    private void setColorRGBA(int r, int g, int b, int a) {
        if(!this.isColorDisabled) {
            if(r > 255) {
                r = 255;
            }

            if(g > 255) {
                g = 255;
            }

            if(b > 255) {
                b = 255;
            }

            if(a > 255) {
                a = 255;
            }

            if(r < 0) {
                r = 0;
            }

            if(g < 0) {
                g = 0;
            }

            if(b < 0) {
                b = 0;
            }

            if(a < 0) {
                a = 0;
            }

            this.hasColor = true;
            this.r = (float)(r & 255) / 255.0F;
            this.g = (float)(g & 255) / 255.0F;
            this.b = (float)(b & 255) / 255.0F;
            this.a = (float)(a & 255) / 255.0F;
        }
    }

    public void addUV(double u, double v) {
        worldRenderer.tex(u, v);
    }

	public void addVertexWithUV(double x, double y, double z, double u, double v) {
	    this.addUV(u, v);
		this.addVertex(x, y, z);
	}

	public void addVertex(double x, double y, double z) {
        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, this.a);
        }

        worldRenderer.pos(x + this.xOffset, y + this.yOffset, z + this.zOffset);
        if (this.hasNormal) {
            worldRenderer.normal(this.nx, this.ny, this.nz);
        }

        worldRenderer.endVertex();

	}

    public void setColorOpaque_I(int color) {
        int i2 = color >> 16 & 255;
        int i3 = color >> 8 & 255;
        color &= 255;
        this.setColorOpaque(i2, i3, color);
    }

	public void disableColor() {
        this.isColorDisabled = true;
	}

    public void setNormal(float normalX, float normalY, float normalZ) {
        this.hasNormal = true;
        this.nx = normalX;
        this.ny = normalY;
        this.nz = normalZ;
    }

    public void setTranslationD(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }
}
