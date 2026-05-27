package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class ImageBufferDownload implements ImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public ImageData parseUserSkin(ImageData skinImage) {
        if(skinImage == null) {
            return null;
        } else {
            this.imageWidth = 64;
            this.imageHeight = 32;
            ImageData bufferedImage2 = new ImageData(this.imageWidth, this.imageHeight, true);
            bufferedImage2.drawImage(skinImage, 0, 0);
            this.imageData = bufferedImage2.pixels;
            this.setAreaOpaque(0, 0, 32, 16);
            this.setAreaTransparent(32, 0, 64, 32);
            this.setAreaOpaque(0, 16, 64, 32);
            boolean z4 = false;

            int i5;
            int i6;
            int i7;
            for(i5 = 32; i5 < 64; ++i5) {
                for(i6 = 0; i6 < 16; ++i6) {
                    i7 = this.imageData[i5 + i6 * 64];
                    if((i7 >> 24 & 255) < 128) {
                        z4 = true;
                    }
                }
            }

            if(!z4) {
                for(i5 = 32; i5 < 64; ++i5) {
                    for(i6 = 0; i6 < 16; ++i6) {
                        i7 = this.imageData[i5 + i6 * 64];
                        if((i7 >> 24 & 255) < 128) {
                            z4 = true;
                        }
                    }
                }
            }

            return bufferedImage2;
        }
    }

    private void setAreaTransparent(int minX, int minY, int maxX, int maxY) {
        if(!this.hasTransparency(minX, minY, maxX, maxY)) {
            for(int x = minX; x < maxX; ++x) {
                for(int y = minY; y < maxY; ++y) {
                    this.imageData[x + y * this.imageWidth] &= 0xFFFFFF;
                }
            }

        }
    }

    private void setAreaOpaque(int minX, int minY, int maxX, int maxY) {
        for(int x = minX; x < maxX; ++x) {
            for(int y = minY; y < maxY; ++y) {
                this.imageData[x + y * this.imageWidth] |= 0xFF000000;
            }
        }

    }

    private boolean hasTransparency(int minX, int minY, int maxX, int maxY) {
        for(int x = minX; x < maxX; ++x) {
            for(int y = minY; y < maxY; ++y) {
                int i7 = this.imageData[x + y * this.imageWidth];
                if((i7 >> 24 & 255) < 128) {
                    return true;
                }
            }
        }

        return false;
    }
}
