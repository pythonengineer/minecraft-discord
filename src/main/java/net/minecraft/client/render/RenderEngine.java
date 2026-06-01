package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.texture.TextureFX;

public class RenderEngine {
    public static boolean useMipmaps = false;
    private HashMap textureMap = new HashMap();
    private HashMap textureContentsMap = new HashMap();
    private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);
    private List textureList = new ArrayList();
    private Map urlToImageDataMap = new HashMap();
    private GameSettings options;
    private boolean clampTexture = false;

    public RenderEngine(GameSettings options) {
        this.options = options;
    }

	public int getTexture(String textureName) {
		Integer integer2 = (Integer)this.textureMap.get(textureName);
		if(integer2 != null) {
			return integer2.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int i4 = this.singleIntBuffer.get(0);
				if(textureName.startsWith("##")) {
					this.setupTexture(ImageData.loadImageFile("/assets" + textureName.substring(2)), i4);
                } else if(textureName.startsWith("%%")) {
                    this.clampTexture = true;
                    this.setupTexture(ImageData.loadImageFile("/assets" + textureName.substring(2)), i4);
                    this.clampTexture = false;
				} else {
					this.setupTexture(ImageData.loadImageFile("/assets" + textureName), i4);
				}

				this.textureMap.put(textureName, Integer.valueOf(i4));
				return i4;
			} catch (Exception iOException3) {
				throw new RuntimeException("!! " + iOException3);
			}
		}
	}

    public int allocateAndSetupTexture(ImageData image) {
        this.singleIntBuffer.clear();
        GLAllocation.generateTextureNames(this.singleIntBuffer);
        int i2 = this.singleIntBuffer.get(0);
        this.setupTexture(image, i2);
        this.textureContentsMap.put(i2, image);
        return i2;
    }

	private void setupTexture(ImageData image, int bindedTextureID) {
		GL11.glBindTexture(bindedTextureID);
        if(useMipmaps) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        if(this.clampTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }

		int i3 = image.getWidth();
		int i4 = image.getHeight();
		int[] i5 = new int[i3 * i4];
		byte[] b6 = new byte[i3 * i4 << 2];
		image.getRGB(0, 0, i3, i4, i5, 0, i3);

        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
		for(i7 = 0; i7 < i5.length; ++i7) {
			int a = i5[i7] >>> 24;
			int b = i5[i7] >> 16 & 255;
			int g = i5[i7] >> 8 & 255;
			int r = i5[i7] & 255;
			if(this.options != null && this.options.anaglyph) {
				i10 = (r * 30 + g * 59 + b * 11) / 100;
				g = (r * 30 + g * 70) / 100;
				b = (r * 30 + b * 70) / 100;
				r = i10;
			}

			b6[i7 << 2] = (byte)r;
			b6[(i7 << 2) + 1] = (byte)g;
			b6[(i7 << 2) + 2] = (byte)b;
			b6[(i7 << 2) + 3] = (byte)a;
		}

		this.imageData.clear();
		this.imageData.put(b6);
		this.imageData.position(0).limit(b6.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i3, i4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
        if(useMipmaps) {
            for(i7 = 1; i7 <= 4; ++i7) {
                i8 = i3 >> i7 - 1;
                i9 = i3 >> i7;
                i10 = i4 >> i7;

                for(i11 = 0; i11 < i9; ++i11) {
                    for(i12 = 0; i12 < i10; ++i12) {
                        i13 = this.imageData.getInt((i11 * 2 + 0 + (i12 * 2 + 0) * i8) * 4);
                        i14 = this.imageData.getInt((i11 * 2 + 1 + (i12 * 2 + 0) * i8) * 4);
                        int i15 = this.imageData.getInt((i11 * 2 + 1 + (i12 * 2 + 1) * i8) * 4);
                        int i16 = this.imageData.getInt((i11 * 2 + 0 + (i12 * 2 + 1) * i8) * 4);
                        int i17 = this.alphaBlend(this.alphaBlend(i13, i14), this.alphaBlend(i15, i16));
                        this.imageData.putInt((i11 + i12 * i9) * 4, i17);
                    }
                }

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i7, GL11.GL_RGBA, i9, i10, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
            }
        }

	}

    public void deleteTexture(int tex) {
        this.textureContentsMap.remove(tex);
        this.singleIntBuffer.clear();
        this.singleIntBuffer.put(tex);
        this.singleIntBuffer.flip();
        GL11.glDeleteTextures(this.singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String url, String textureName) {
        ThreadDownloadImageData url1 = (ThreadDownloadImageData)this.urlToImageDataMap.get(url);
        if(url1 != null && url1.image != null && !url1.textureSetupComplete) {
            if(url1.textureName < 0) {
                ImageData bufferedImage = url1.image;
                this.singleIntBuffer.clear();
                GLAllocation.generateTextureNames(this.singleIntBuffer);
                int i5 = this.singleIntBuffer.get(0);
                this.setupTexture(bufferedImage, i5);
                this.textureContentsMap.put(Integer.valueOf(i5), bufferedImage);
                url1.textureName = i5;
            } else {
                this.setupTexture(url1.image, url1.textureName);
            }

            url1.textureSetupComplete = true;
        }

        return url1 != null && url1.textureName >= 0 ? url1.textureName : this.getTexture(textureName);
    }

    public ThreadDownloadImageData obtainImageData(String url, ImageBuffer imageBufferDownloader) {
        ThreadDownloadImageData threadDownloadImageData = (ThreadDownloadImageData)this.urlToImageDataMap.get(url);
        if(threadDownloadImageData == null) {
            this.urlToImageDataMap.put(url, new ThreadDownloadImageData(url, imageBufferDownloader));
        } else {
            ++threadDownloadImageData.referenceCount;
        }

        return threadDownloadImageData;
    }

    public void releaseImageData(String url) {
        ThreadDownloadImageData threadDownloadImageData = (ThreadDownloadImageData)this.urlToImageDataMap.get(url);
        if(threadDownloadImageData != null) {
            --threadDownloadImageData.referenceCount;
            if(threadDownloadImageData.referenceCount == 0) {
                if(threadDownloadImageData.textureName >= 0) {
                    this.deleteTexture(threadDownloadImageData.textureName);
                }

                this.urlToImageDataMap.remove(url);
            }
        }

    }

	public void registerTextureFX(TextureFX fxTexture) {
		this.textureList.add(fxTexture);
        fxTexture.onTick();
    }

    public void updateDynamicTextures() {
        int i1;
        TextureFX textureFX;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        for(i1 = 0; i1 < this.textureList.size(); ++i1) {
            textureFX = (TextureFX)this.textureList.get(i1);
            textureFX.anaglyphEnabled = this.options.anaglyph;
            textureFX.onTick();
            this.imageData.clear();
            this.imageData.put(textureFX.imageData);
            this.imageData.position(0).limit(textureFX.imageData.length);

            for(i3 = 0; i3 < textureFX.tileSize; ++i3) {
                for(i4 = 0; i4 < textureFX.tileSize; ++i4) {
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, (textureFX.iconIndex % 16 << 4) + (i3 << 4), (textureFX.iconIndex / 16 << 4) + (i4 << 4), 16, 16, 6408, 5121, this.imageData);
                    if(useMipmaps) {
                        for(i5 = 1; i5 <= 4; ++i5) {
                            i6 = 16 >> i5 - 1;
                            i7 = 16 >> i5;

                            for(i8 = 0; i8 < i7; ++i8) {
                                for(i9 = 0; i9 < i7; ++i9) {
                                    i10 = this.imageData.getInt((i8 * 2 + 0 + (i9 * 2 + 0) * i6) * 4);
                                    i11 = this.imageData.getInt((i8 * 2 + 1 + (i9 * 2 + 0) * i6) * 4);
                                    i12 = this.imageData.getInt((i8 * 2 + 1 + (i9 * 2 + 1) * i6) * 4);
                                    int i13 = this.imageData.getInt((i8 * 2 + 0 + (i9 * 2 + 1) * i6) * 4);
                                    int i14 = this.averageColor(this.averageColor(i10, i11), this.averageColor(i12, i13));
                                    this.imageData.putInt((i8 + i9 * i7) * 4, i14);
                                }
                            }

                            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, i5, textureFX.iconIndex % 16 * i7, textureFX.iconIndex / 16 * i7, i7, i7, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                        }
                    }
                }
            }
        }

        for(i1 = 0; i1 < this.textureList.size(); ++i1) {
            textureFX = (TextureFX)this.textureList.get(i1);
            if(textureFX.textureId > 0) {
                this.imageData.clear();
                this.imageData.put(textureFX.imageData);
                this.imageData.position(0).limit(textureFX.imageData.length);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureFX.textureId);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                if(useMipmaps) {
                    for(i3 = 1; i3 <= 4; ++i3) {
                        i4 = 16 >> i3 - 1;
                        i5 = 16 >> i3;

                        for(i6 = 0; i6 < i5; ++i6) {
                            for(i7 = 0; i7 < i5; ++i7) {
                                i8 = this.imageData.getInt((i6 * 2 + 0 + (i7 * 2 + 0) * i4) * 4);
                                i9 = this.imageData.getInt((i6 * 2 + 1 + (i7 * 2 + 0) * i4) * 4);
                                i10 = this.imageData.getInt((i6 * 2 + 1 + (i7 * 2 + 1) * i4) * 4);
                                i11 = this.imageData.getInt((i6 * 2 + 0 + (i7 * 2 + 1) * i4) * 4);
                                i12 = this.averageColor(this.averageColor(i8, i9), this.averageColor(i10, i11));
                                this.imageData.putInt((i6 + i7 * i5) * 4, i12);
                            }
                        }

                        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, i3, 0, 0, i5, i5, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                    }
                }
            }
        }

    }

    private int averageColor(int i1, int i2) {
        int i3 = (i1 & 0xFF000000) >> 24 & 255;
        int i4 = (i2 & 0xFF000000) >> 24 & 255;
        return (i3 + i4 >> 1 << 24) + ((i1 & 16711422) + (i2 & 16711422) >> 1);
    }

    private int alphaBlend(int i1, int i2) {
        int i3 = (i1 & 0xFF000000) >> 24 & 255;
        int i4 = (i2 & 0xFF000000) >> 24 & 255;
        short s5 = 255;
        if(i3 + i4 == 0) {
            i3 = 1;
            i4 = 1;
            s5 = 0;
        }

        int i6 = (i1 >> 16 & 255) * i3;
        int i7 = (i1 >> 8 & 255) * i3;
        int i8 = (i1 & 255) * i3;
        int i9 = (i2 >> 16 & 255) * i4;
        int i10 = (i2 >> 8 & 255) * i4;
        int i11 = (i2 & 255) * i4;
        int i12 = (i6 + i9) / (i3 + i4);
        int i13 = (i7 + i10) / (i3 + i4);
        int i14 = (i8 + i11) / (i3 + i4);
        return s5 << 24 | i12 << 16 | i13 << 8 | i14;
    }

    public void refreshTextures() {
        Iterator iterator1 = this.textureContentsMap.keySet().iterator();

        int i2;
        ImageData bufferedImage;
        while(iterator1.hasNext()) {
            i2 = ((Integer)iterator1.next()).intValue();
            bufferedImage = (ImageData)this.textureContentsMap.get(Integer.valueOf(i2));
            this.setupTexture(bufferedImage, i2);
        }

        for(iterator1 = this.urlToImageDataMap.values().iterator(); iterator1.hasNext(); ((ThreadDownloadImageData)iterator1.next()).textureSetupComplete = false) {
        }

        iterator1 = this.textureMap.keySet().iterator();

        while(iterator1.hasNext()) {
            String string5 = (String)iterator1.next();

            try {
                if(string5.startsWith("##")) {
                    bufferedImage = ImageData.loadImageFile("/assets" + string5.substring(2));
                } else if(string5.startsWith("%%")) {
                    this.clampTexture = true;
                    bufferedImage = ImageData.loadImageFile("/assets" + string5.substring(2));
                    this.clampTexture = false;
                } else {
                    bufferedImage = ImageData.loadImageFile("/assets" + string5);
                }

                i2 = ((Integer)this.textureMap.get(string5)).intValue();
                this.setupTexture(bufferedImage, i2);
            } catch (Exception iOException4) {
                iOException4.printStackTrace();
            }
        }

    }

    public void bindTexture(int bindedTextureID) {
        if(bindedTextureID >= 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, bindedTextureID);
        }
    }
}
