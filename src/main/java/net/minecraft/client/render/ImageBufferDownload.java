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
        byte b5 = 32;
        byte b11 = 64;
        byte b10 = 0;
        byte b9 = 32;
        minY = b9;

        boolean z10000;
        label43:
        while(true) {
            if(minY >= b11) {
                z10000 = false;
                break;
            }

            for(int i6 = b10; i6 < b5; ++i6) {
                int i7 = this.imageData[minY + i6 * this.imageWidth];
                if(i7 >>> 24 < 128) {
                    z10000 = true;
                    break label43;
                }
            }

            ++minY;
        }

        if(!z10000) {
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
}