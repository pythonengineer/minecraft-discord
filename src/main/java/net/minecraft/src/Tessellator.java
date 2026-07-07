package net.minecraft.src;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.opengl.VertexFormat;

public class Tessellator {
    private static net.lax1dude.eaglercraft.opengl.WorldRenderer worldRenderer = new net.lax1dude.eaglercraft.opengl.WorldRenderer(2097152);
	private int drawMode;
    private double textureU;
    private double textureV;
    private float r;
    private float g;
    private float b;
    private float a;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private boolean hasNormals = false;
	private boolean isColorDisabled = false;
	private double xOffset;
	private double yOffset;
	private double zOffset;
    private float normalX;
    private float normalY;
    private float normalZ;
	public static final Tessellator instance = new Tessellator(2097152);
	private boolean isDrawing = false;

	private Tessellator(int var1) {
	}

	public void draw() {
        int cunt = worldRenderer.getVertexCount();
        if (cunt > 0) {
            worldRenderer.finishDrawing();
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

	public void startDrawingQuads() {
		this.startDrawing(7);
	}

	public void startDrawing(int var1) {
		this.isDrawing = true;
		this.reset();
		this.drawMode = var1;
		this.hasNormals = false;
		this.hasColor = false;
		this.hasTexture = false;
		this.isColorDisabled = false;
	}

	public void setTextureUV(double var1, double var3) {
		this.hasTexture = true;
		this.textureU = var1;
		this.textureV = var3;
	}

	public void setColorOpaque_F(float var1, float var2, float var3) {
		this.setColorOpaque((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F));
	}

	public void setColorRGBA_F(float var1, float var2, float var3, float var4) {
		this.setColorRGBA((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F), (int)(var4 * 255.0F));
	}

	public void setColorOpaque(int var1, int var2, int var3) {
		this.setColorRGBA(var1, var2, var3, 255);
	}

	public void setColorRGBA(int var1, int var2, int var3, int var4) {
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

	public void addVertexWithUV(double var1, double var3, double var5, double var7, double var9) {
		this.setTextureUV(var7, var9);
		this.addVertex(var1, var3, var5);
	}

	public void addVertex(double var1, double var3, double var5) {
	    if (this.isDrawing && !worldRenderer.isDrawing) {
    	    if (this.hasColor && this.hasNormals && this.hasTexture) {
    	        worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
    	    } else if (this.hasNormals && this.hasTexture) {
                worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION_TEX_NORMAL);
            } else if (this.hasColor && this.hasTexture) {
                worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION_TEX_COLOR);
            } else if (this.hasColor) {
                worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION_COLOR);
            } else if (this.hasTexture) {
                worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION_TEX);
            } else {
                worldRenderer.begin(this.drawMode, DefaultVertexFormats.POSITION);
    	    }
	    }

        if (this.hasColor) {
            worldRenderer.color(this.r, this.g, this.b, this.a);
        }

        if (this.hasTexture) {
            worldRenderer.tex(this.textureU, this.textureV);
        }

        worldRenderer.pos(var1 + this.xOffset, var3 + this.yOffset, var5 + this.zOffset);
        if (this.hasNormals) {
            worldRenderer.normal(this.normalX, this.normalY, this.normalZ);
        }

        worldRenderer.endVertex();

	}

	public void setColorOpaque_I(int var1) {
		int var2 = var1 >> 16 & 255;
		int var3 = var1 >> 8 & 255;
		int var4 = var1 & 255;
		this.setColorOpaque(var2, var3, var4);
	}

	public void setColorRGBA_I(int var1, int var2) {
		int var3 = var1 >> 16 & 255;
		int var4 = var1 >> 8 & 255;
		int var5 = var1 & 255;
		this.setColorRGBA(var3, var4, var5, var2);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float var1, float var2, float var3) {
		if(!this.isDrawing) {
			System.out.println("But..");
		}

		this.hasNormals = true;
		this.normalX = var1;
		this.normalY = var2;
		this.normalZ = var3;
	}

	public void setTranslationD(double var1, double var3, double var5) {
		this.xOffset = var1;
		this.yOffset = var3;
		this.zOffset = var5;
	}

	public void setTranslationF(float var1, float var2, float var3) {
		this.xOffset += (double)var1;
		this.yOffset += (double)var2;
		this.zOffset += (double)var3;
	}
}
