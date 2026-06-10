package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;

public class FontRenderer {
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	private int fontDisplayLists;
	private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);

	public FontRenderer(GameSettings gameSettings, String fontTextureLocation, RenderEngine renderEngine) {
	    ImageData img;
		try {
            img = ImageData.loadImageFile("/assets" + fontTextureLocation);
        } catch (Exception iOException15) {
            throw new RuntimeException(iOException15);
		}

		int i5 = img.getWidth();
		int i6 = img.getHeight();
		int[] i7 = new int[i5 * i6];
		img.getRGB(0, 0, i5, i6, i7, 0, i5);

		int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i15;
        int i16;
        for(i8 = 0; i8 < 256; ++i8) {
            i9 = i8 % 16;
            i10 = i8 / 16;

            for(i11 = 7; i11 >= 0; --i11) {
                i12 = i9 * 8 + i11;
                boolean z13 = true;

                for(int i14 = 0; i14 < 8 && z13; ++i14) {
                    i15 = (i10 * 8 + i14) * i5;
                    i16 = i7[i12 + i15] & 255;
                    if(i16 > 0) {
                        z13 = false;
                    }
                }

                if(!z13) {
                    break;
                }
            }

            if(i8 == 32) {
                i11 = 2;
            }

            this.charWidth[i8] = i11 + 2;
        }

        this.fontTextureName = renderEngine.allocateAndSetupTexture(img);
        this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
        Tessellator tessellator18 = Tessellator.instance;

        for(i6 = 0; i6 < 256; ++i6) {
            GL11.glNewList(this.fontDisplayLists + i6, GL11.GL_COMPILE);
            tessellator18.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            i8 = i6 % 16 << 3;
            i9 = i6 / 16 << 3;
            tessellator18.addVertexWithUV(0.0D, 7.989999771118164D, 0.0D, (double)((float)i8 / 128.0F), (double)(((float)i9 + 7.99F) / 128.0F));
            tessellator18.addVertexWithUV(7.989999771118164D, 7.989999771118164D, 0.0D, (double)(((float)i8 + 7.99F) / 128.0F), (double)(((float)i9 + 7.99F) / 128.0F));
            tessellator18.addVertexWithUV(7.989999771118164D, 0.0D, 0.0D, (double)(((float)i8 + 7.99F) / 128.0F), (double)((float)i9 / 128.0F));
            tessellator18.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)((float)i8 / 128.0F), (double)((float)i9 / 128.0F));
            tessellator18.draw();
            GL11.glTranslatef((float)this.charWidth[i6], 0.0F, 0.0F);
            GL11.glEndList();
        }

        for(i6 = 0; i6 < 32; ++i6) {
            i8 = (i6 & 8) << 3;
            i9 = (i6 & 1) * 191 + i8;
            int i19 = ((i6 & 2) >> 1) * 191 + i8;
            i11 = ((i6 & 4) >> 2) * 191 + i8;
            boolean z20 = i6 >= 16;
            if(gameSettings.anaglyph) {
                int i13 = (i11 * 30 + i19 * 59 + i9 * 11) / 100;
                int i14 = (i11 * 30 + i19 * 70) / 100;
                i16 = (i11 * 30 + i9 * 70) / 100;
                i11 = i13;
                i19 = i14;
                i9 = i16;
            }

            i6 += 2;
            if(z20) {
                i11 /= 4;
                i19 /= 4;
                i9 /= 4;
            }

            GL11.glColor4f((float)i11 / 255.0F, (float)i19 / 255.0F, (float)i9 / 255.0F, 1.0F);
        }

	}

    public void drawStringWithShadow(String message, int x, int y, int color) {
        this.renderString(message, x + 1, y + 1, color, true);
        this.drawString(message, x, y, color);
    }

    public void drawString(String message, int x, int y, int color) {
        this.renderString(message, x, y, color, false);
    }

    public void renderString(String message, int x, int y, int color, boolean dropShadow) {
        if(message != null) {
            int i6;
            if(dropShadow) {
                i6 = color & 0xFF000000;
                color = (color & 16579836) >> 2;
                color += i6;
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
            float f10 = (float)(color >> 16 & 255) / 255.0F;
            float f7 = (float)(color >> 8 & 255) / 255.0F;
            float f8 = (float)(color & 255) / 255.0F;
            float f9 = (float)(color >> 24 & 255) / 255.0F;
            if(f9 == 0.0F) {
                f9 = 1.0F;
            }

            GL11.glColor4f(f10, f7, f8, f9);
            this.buffer.clear();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, 0.0F);

            for(i6 = 0; i6 < message.length(); ++i6) {
                int i11;
                for(; message.charAt(i6) == 167 && message.length() > i6 + 1; i6 += 2) {
                    i11 = "0123456789abcdef".indexOf(message.charAt(i6 + 1));
                    if(i11 < 0 || i11 > 15) {
                        i11 = 15;
                    }

                    this.buffer.put(this.fontDisplayLists + 256 + i11 + (dropShadow ? 16 : 0));
                    if(this.buffer.remaining() == 0) {
                        this.buffer.flip();
                        GL11.glCallLists(this.buffer);
                        this.buffer.clear();
                    }
                }

                i11 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(message.charAt(i6));
                if(i11 >= 0) {
                    this.buffer.put(this.fontDisplayLists + i11 + 32);
                }

                if(this.buffer.remaining() == 0) {
                    this.buffer.flip();
                    GL11.glCallLists(this.buffer);
                    this.buffer.clear();
                }
            }

            this.buffer.flip();
            GL11.glCallLists(this.buffer);
            GL11.glPopMatrix();
        }
    }

    public int getStringWidth(String message) {
        if(message == null) {
            return 0;
        } else {
            int i2 = 0;

            for(int i3 = 0; i3 < message.length(); ++i3) {
                if(message.charAt(i3) == 167) {
                    ++i3;
                } else {
                    int i4 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(message.charAt(i3));
                    if(i4 >= 0) {
                        i2 += this.charWidth[i4 + 32];
                    }
                }
            }

            return i2;
        }
    }
}
