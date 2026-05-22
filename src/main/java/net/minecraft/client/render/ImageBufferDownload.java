package net.minecraft.client.render;

import net.lax1dude.eaglercraft.opengl.ImageData;

public class ImageBufferDownload {
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
            return bufferedImage2;
        }
    }

    private void setAreaTransparent(int minX, int minY, int maxX, int maxY) {
        if(!this.hasTransparency(32, 0, 64, 32)) {
            for(minX = 32; minX < 64; ++minX) {
                for(minY = 0; minY < 32; ++minY) {
                    this.imageData[minX + minY * this.imageWidth] &= 0xFFFFFF;
                }
            }

        }
    }

    private void setAreaOpaque(int minX, int minY, int maxX, int maxY) {
        for(minX = 0; minX < maxX; ++minX) {
            for(int i5 = minY; i5 < maxY; ++i5) {
                this.imageData[minX + i5 * this.imageWidth] |= 0xFF000000;
            }
        }

    }

    boolean hasTransparency(int i1, int i2, int i3, int i4) {
        for(i1 = i1; i1 < i3; ++i1) {
            for(int i5 = i2; i5 < i4; ++i5) {
                if(this.imageData[i1 + i5 * this.imageWidth] >>> 24 < 128) {
                    return true;
                }
            }
        }

        return false;
    }
}