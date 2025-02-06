package com.mojang.minecraft.renderer;

import java.util.HashMap;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
    private HashMap<String, Integer> idMap = new HashMap();
    public IntBuffer idBuffer = BufferUtils.createIntBuffer(1);

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

    public final int addTexture(ImageData img) {
        this.idBuffer.clear();
        GL11.glGenTextures(this.idBuffer);
        int i11 = this.idBuffer.get(0);
        GL11.glBindTexture(i11);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        int w = img.getWidth();
        int h = img.getHeight();
        IntBuffer textureBuffer = BufferUtils.createIntBuffer(w * h << 2);
        textureBuffer.clear();
        textureBuffer.put(img.pixels);
        textureBuffer.position(0).limit(img.pixels.length);
        GL11.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, 5121, textureBuffer);
        return i11;
    }
}
