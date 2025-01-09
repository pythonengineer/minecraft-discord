package com.mojang.minecraft.renderer;

import java.util.HashMap;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
    private HashMap idMap = new HashMap();

    public final int loadTexture(String var1, int var2) {
        try {
            if(this.idMap.containsKey(var1)) {
                return ((Integer)this.idMap.get(var1)).intValue();
            } else {
                IntBuffer var14 = BufferUtils.createIntBuffer(1);
                var14.clear();
                GL11.glGenTextures(var14);
                var2 = var14.get(0);
                this.idMap.put(var1, Integer.valueOf(var2));
                GL11.glBindTexture(var2);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                ImageData img = ImageData.loadImageFile("/assets" + var1);
                int w = img.getWidth();
                int h = img.getHeight();
                IntBuffer textureBuffer = BufferUtils.createIntBuffer(w * h << 2);
                textureBuffer.clear();
                textureBuffer.put(img.pixels);
                textureBuffer.position(0).limit(img.pixels.length);
                GL11.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, 5121, textureBuffer);
                return var2;
            }
        } catch (Exception var10) {
            throw new RuntimeException("!!");
        }
    }
}
