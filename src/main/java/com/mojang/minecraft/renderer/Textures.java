package com.mojang.minecraft.renderer;

import com.mojang.minecraft.renderer.texture.TextureFX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
    private HashMap<String, Integer> idMap = new HashMap();
    public IntBuffer idBuffer = BufferUtils.createIntBuffer(1);
    public ByteBuffer textureBuffer = BufferUtils.createByteBuffer(262144);
    public List textureList = new ArrayList();

    public final int getTextureId(String string1) {
        try {
            if(this.idMap.containsKey(string1)) {
                return ((Integer)this.idMap.get(string1)).intValue();
            } else {
                int i2 = this.addTexture(ImageData.loadImageFile("/assets" + string1));
                this.idMap.put(string1, i2);
                return i2;
            }
        } catch (Exception exception3) {
            throw new RuntimeException("!!");
        }
    }

    public final int addTexture(ImageData bufferedImage1) {
        this.idBuffer.clear();
        GL11.glGenTextures(this.idBuffer);
        int i2 = this.idBuffer.get(0);
        GL11.glBindTexture(i2);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        int i3 = bufferedImage1.getWidth();
        int i4 = bufferedImage1.getHeight();
        int[] i5 = new int[i3 * i4];
        byte[] b6 = new byte[i3 * i4 << 2];
        bufferedImage1.getRGB(0, 0, i3, i4, i5, 0, i3);

        for(int i11 = 0; i11 < i5.length; ++i11) {
            int i7 = i5[i11] >>> 24;
            int i8 = i5[i11] >> 16 & 255;
            int i9 = i5[i11] >> 8 & 255;
            int i10 = i5[i11] & 255;
            b6[i11 << 2] = (byte)i10;
            b6[(i11 << 2) + 1] = (byte)i9;
            b6[(i11 << 2) + 2] = (byte)i8;
            b6[(i11 << 2) + 3] = (byte)i7;
        }

        this.textureBuffer.clear();
        this.textureBuffer.put(b6);
        this.textureBuffer.position(0).limit(b6.length);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i3, i4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.textureBuffer);
        return i2;
    }

    public final void registerTextureFX(TextureFX textureFX1) {
        this.textureList.add(textureFX1);
        textureFX1.onTick();
    }
}
