package com.mojang.minecraft.gui;

import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Font {
	private int[] charWidths = new int[256];
	private int fontTexture = 0;

	public Font(String name, Textures textures) {
        ImageData img;
        try {
            img = ImageData.loadImageFile("/assets" + name);
		} catch (Exception var16) {
			throw new RuntimeException(var16);
		}

		int w = img.getWidth();
		int h = img.getHeight();
		int[] rawPixels = new int[w * h];
		img.getRGB(0, 0, w, h, rawPixels, 0, w);

		for(int i = 0; i < 128; ++i) {
			int xt = i % 16;
			int yt = i / 16;
			int x = 0;

			for(boolean emptyColumn = false; x < 8 && !emptyColumn; ++x) {
				int xPixel = xt * 8 + x;
				emptyColumn = true;

				for(int y = 0; y < 8 && emptyColumn; ++y) {
					int yPixel = (yt * 8 + y) * w;
					int pixel = rawPixels[xPixel + yPixel] & 255;
					if(pixel > 128) {
						emptyColumn = false;
					}
				}
			}

			if(i == 32) {
				x = 4;
			}

			this.charWidths[i] = x;
		}

        this.fontTexture = textures.getTextureId(name);
	}

	public void drawShadow(String str, int x, int y, int color) {
		this.draw(str, x + 1, y + 1, color, true);
		this.draw(str, x, y, color);
	}

	public void draw(String str, int x, int y, int color) {
		this.draw(str, x, y, color, false);
	}

    private void draw(String string1, int i2, int i3, int i4, boolean z5) {
        if(string1 != null) {
            char[] c12 = string1.toCharArray();
            if(z5) {
                i4 = (i4 & 16579836) >> 2;
            }
    
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTexture);
            Tesselator tesselator6 = Tesselator.instance;
            tesselator6.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            tesselator6.color(i4);
            int i7 = 0;
    
            for(int i8 = 0; i8 < c12.length; ++i8) {
                int i9;
                if(c12[i8] == 38 && c12.length > i8 + 1) {
                    if((i4 = "0123456789abcdef".indexOf(c12[i8 + 1])) < 0) {
                        i4 = 15;
                    }

                    i9 = (i4 & 8) << 3;
                    int i10 = (i4 & 1) * 191 + i9;
                    int i11 = ((i4 & 2) >> 1) * 191 + i9;
                    i4 = ((i4 & 4) >> 2) * 191 + i9 << 16 | i11 << 8 | i10;
                    i8 += 2;
                    if(z5) {
                        i4 = (i4 & 16579836) >> 2;
                    }
    
                    tesselator6.color(i4);
                }
    
                i4 = c12[i8] % 16 << 3;
                i9 = c12[i8] / 16 << 3;
                float f13 = 7.99F;
                tesselator6.vertexUV((float)(i2 + i7), (float)i3 + f13, 0.0F, (float)i4 / 128.0F, ((float)i9 + f13) / 128.0F);
                tesselator6.vertexUV((float)(i2 + i7) + f13, (float)i3 + f13, 0.0F, ((float)i4 + f13) / 128.0F, ((float)i9 + f13) / 128.0F);
                tesselator6.vertexUV((float)(i2 + i7) + f13, (float)i3, 0.0F, ((float)i4 + f13) / 128.0F, (float)i9 / 128.0F);
                tesselator6.vertexUV((float)(i2 + i7), (float)i3, 0.0F, (float)i4 / 128.0F, (float)i9 / 128.0F);
                i7 += this.charWidths[c12[i8]];
            }
    
            tesselator6.end();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }

    public final int width(String string1) {
        if(string1 == null) {
            return 0;
        } else {
            char[] c4 = string1.toCharArray();
            int i2 = 0;

            for(int i3 = 0; i3 < c4.length; ++i3) {
                if(c4[i3] == 38) {
                    ++i3;
                } else {
                    i2 += this.charWidths[c4[i3]];
                }
            }

            return i2;
        }
    }

    public static String removeColorCodes(String string0) {
        char[] c3 = string0.toCharArray();
        String string1 = "";

        for(int i2 = 0; i2 < c3.length; ++i2) {
            if(c3[i2] == 38) {
                ++i2;
            } else {
                string1 = string1 + c3[i2];
            }
        }

        return string1;
    }
}
