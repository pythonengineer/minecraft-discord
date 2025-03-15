package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Options;
import com.mojang.minecraft.renderer.texture.DynamicTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
    public HashMap idMap = new HashMap();
    public HashMap pixelsMap = new HashMap();
    public IntBuffer ib = BufferUtils.createIntBuffer(1);
    public ByteBuffer pixels = BufferUtils.createByteBuffer(262144);
    public List textureList = new ArrayList();
    public Options options;

    public Textures(Options options) {
        this.options = options;
    }

    public final int loadTexture(String resourceName) {
        try {
            if(this.idMap.containsKey(resourceName)) {
                return ((Integer)this.idMap.get(resourceName)).intValue();
            } else {
                this.ib.clear();
                GL11.glGenTextures(this.ib);
                int i2 = this.ib.get(0);
                if(resourceName.startsWith("##")) {
                    this.addTexture(ImageData.loadImageFile("/assets" + resourceName), i2);
                } else {
                    this.addTexture(ImageData.loadImageFile("/assets" + resourceName), i2);
                }

                this.idMap.put(resourceName, i2);
                return i2;
            }
        } catch (Exception exception) {
            throw new RuntimeException("!!");
        }
    }

    public void addTexture(ImageData bufferedImage, int textureId) {
        GL11.glBindTexture(textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        int i3 = bufferedImage.getWidth();
        int i4 = bufferedImage.getHeight();
        int[] i5 = new int[i3 * i4];
        byte[] b6 = new byte[i3 * i4 << 2];
        bufferedImage.getRGB(0, 0, i3, i4, i5, 0, i3);

        for(int i11 = 0; i11 < i5.length; ++i11) {
            int a = i5[i11] >>> 24;
            int b = i5[i11] >> 16 & 255;
            int g = i5[i11] >> 8 & 255;
            int r = i5[i11] & 255;
            if(this.options.anaglyph3d) {
                int i6 = (r * 30 + g * 59 + b * 11) / 100;
                g = (r * 30 + g * 70) / 100;
                b = (r * 30 + b * 70) / 100;
                r = i6;
            }

            b6[i11 << 2] = (byte)r;
            b6[(i11 << 2) + 1] = (byte)g;
            b6[(i11 << 2) + 2] = (byte)b;
            b6[(i11 << 2) + 3] = (byte)a;
        }

        this.pixels.clear();
        this.pixels.put(b6);
        this.pixels.position(0).limit(b6.length);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i3, i4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.pixels);
    }

    public final void addDynamicTexture(DynamicTexture dynamicTexture) {
        this.textureList.add(dynamicTexture);
        dynamicTexture.tick();
    }
}
