package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;

public final class FontRenderer {
    private int[] charList = new int[256];
    private int character = 0;
    private GameSettings settings;

	public FontRenderer(GameSettings var1, String var2, RenderEngine var3) {
        this.settings = var1;

		ImageData var14;
		try {
			var14 = ImageData.loadImageFile("/assets" + var2);
		} catch (Exception var13) {
			throw new RuntimeException(var13);
		}

		int var4 = var14.getWidth();
		int var5 = var14.getHeight();
		int[] var6 = new int[var4 * var5];
		var14.getRGB(0, 0, var4, var5, var6, 0, var4);

		for(int var15 = 0; var15 < 128; ++var15) {
			var5 = var15 % 16;
			int var7 = var15 / 16;
			int var8 = 0;

			for(boolean var9 = false; var8 < 8 && !var9; ++var8) {
				int var10 = (var5 << 3) + var8;
				var9 = true;

				for(int var11 = 0; var11 < 8 && var9; ++var11) {
					int var12 = ((var7 << 3) + var11) * var4;
                    var12 = var6[var10 + var12] & 255;
                    if(var12 > 128) {
						var9 = false;
					}
				}
			}

			if(var15 == 32) {
				var8 = 4;
			}

            this.charList[var15] = var8;
        }

        this.character = var3.getTexture(var2);
	}

    public final void drawStringWithShadow(String var1, int var2, int var3, int var4) {
        this.renderString(var1, var2 + 1, var3 + 1, var4, true);
        this.drawString(var1, var2, var3, var4);
    }

    public final void drawString(String var1, int var2, int var3, int var4) {
        this.renderString(var1, var2, var3, var4, false);
    }

	private void renderString(String var1, int var2, int var3, int var4, boolean var5) {
		if(var1 != null) {
			char[] var12 = var1.toCharArray();
			if(var5) {
				var4 = (var4 & 16579836) >> 2;
			}

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.character);
			Tessellator var6 = Tessellator.instance;
			var6.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
			var6.setColorOpaque_I(var4);
			int var7 = 0;

			for(int var8 = 0; var8 < var12.length; ++var8) {
				int var9;
				if(var12[var8] == 38 && var12.length > var8 + 1) {
					var4 = "0123456789abcdef".indexOf(var12[var8 + 1]);
					if(var4 < 0) {
						var4 = 15;
					}

					var9 = (var4 & 8) << 3;
					int var10 = (var4 & 1) * 191 + var9;
					int var11 = ((var4 & 2) >> 1) * 191 + var9;
					var4 = ((var4 & 4) >> 2) * 191 + var9;
                    if(this.settings.anaglyph) {
                        var9 = (var4 * 30 + var11 * 59 + var10 * 11) / 100;
                        var11 = (var4 * 30 + var11 * 70) / 100;
                        var10 = (var4 * 30 + var10 * 70) / 100;
                        var4 = var9;
                        var11 = var11;
                        var10 = var10;
                    }

					var4 = var4 << 16 | var11 << 8 | var10;
					var8 += 2;
					if(var5) {
						var4 = (var4 & 16579836) >> 2;
					}

					var6.setColorOpaque_I(var4);
				}

				var4 = var12[var8] % 16 << 3;
				var9 = var12[var8] / 16 << 3;
                var6.addVertexWithUV((float)(var2 + var7), (float)var3 + 7.99F, 0.0F, (float)var4 / 128.0F, ((float)var9 + 7.99F) / 128.0F);
                var6.addVertexWithUV((float)(var2 + var7) + 7.99F, (float)var3 + 7.99F, 0.0F, ((float)var4 + 7.99F) / 128.0F, ((float)var9 + 7.99F) / 128.0F);
                var6.addVertexWithUV((float)(var2 + var7) + 7.99F, (float)var3, 0.0F, ((float)var4 + 7.99F) / 128.0F, (float)var9 / 128.0F);
                var6.addVertexWithUV((float)(var2 + var7), (float)var3, 0.0F, (float)var4 / 128.0F, (float)var9 / 128.0F);
                var7 += this.charList[var12[var8]];
			}

			var6.draw();
		}
	}

    public final int getStringWidth(String var1) {
		if(var1 == null) {
			return 0;
		} else {
			char[] var4 = var1.toCharArray();
			int var2 = 0;

			for(int var3 = 0; var3 < var4.length; ++var3) {
				if(var4[var3] == 38) {
					++var3;
				} else {
                    var2 += this.charList[var4[var3]];
				}
			}

			return var2;
		}
	}
}
