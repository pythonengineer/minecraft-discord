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
    private HashMap textureMap = new HashMap();
    private HashMap textureContentsMap = new HashMap();
    private IntBuffer singleIntBuffer = GLAllocation.createIntBuffer(1);
    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(262144);
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
				GLAllocation.generateDisplayLists(this.singleIntBuffer);
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
        GLAllocation.generateDisplayLists(this.singleIntBuffer);
        int i2 = this.singleIntBuffer.get(0);
        this.setupTexture(image, i2);
        this.textureContentsMap.put(i2, image);
        return i2;
    }

	private void setupTexture(ImageData image, int bindedTextureID) {
		GL11.glBindTexture(bindedTextureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        if(this.clampTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }

		bindedTextureID = image.getWidth();
		int i3 = image.getHeight();
		int[] i4 = new int[bindedTextureID * i3];
		byte[] b5 = new byte[bindedTextureID * i3 << 2];
		image.getRGB(0, 0, bindedTextureID, i3, i4, 0, bindedTextureID);

		for(int var11 = 0; var11 < i4.length; ++var11) {
			int a = i4[var11] >>> 24;
			int b = i4[var11] >> 16 & 255;
			int g = i4[var11] >> 8 & 255;
			int r = i4[var11] & 255;
			if(this.options != null && this.options.anaglyph) {
				int var10 = (r * 30 + g * 59 + b * 11) / 100;
				g = (r * 30 + g * 70) / 100;
				b = (r * 30 + b * 70) / 100;
				r = var10;
			}

			b5[var11 << 2] = (byte)r;
			b5[(var11 << 2) + 1] = (byte)g;
			b5[(var11 << 2) + 2] = (byte)b;
			b5[(var11 << 2) + 3] = (byte)a;
		}

		this.imageData.clear();
		this.imageData.put(b5);
		this.imageData.position(0).limit(b5.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bindedTextureID, i3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
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
                GLAllocation.generateDisplayLists(this.singleIntBuffer);
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
        for(i1 = 0; i1 < this.textureList.size(); ++i1) {
            textureFX = (TextureFX)this.textureList.get(i1);
            textureFX.anaglyphEnabled = this.options.anaglyph;
            textureFX.onTick();
            this.imageData.clear();
            this.imageData.put(textureFX.imageData);
            this.imageData.position(0).limit(textureFX.imageData.length);

            for(int i3 = 0; i3 < textureFX.tileSize; ++i3) {
                for(int i4 = 0; i4 < textureFX.tileSize; ++i4) {
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, (textureFX.iconIndex % 16 << 4) + (i3 << 4), (textureFX.iconIndex / 16 << 4) + (i4 << 4), 16, 16, 6408, 5121, this.imageData);
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
            }
        }

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
