package com.mojang.minecraft.renderer;

import java.util.HashMap;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
    private HashMap<String, Integer> idMap = new HashMap();

    public int loadTexture(String resourceName, int mode) {
		try {
            if(this.idMap.containsKey(resourceName)) {
                return ((Integer)this.idMap.get(resourceName)).intValue();
			} else {
				IntBuffer e = BufferUtils.createIntBuffer(1);
				e.clear();
				GL11.glGenTextures(e);
				int id = e.get(0);
                this.idMap.put(resourceName, Integer.valueOf(id));
				System.out.println(resourceName + " -> " + id);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mode);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mode);
                ImageData img = ImageData.loadImageFile("/assets" + resourceName);
                int w = img.getWidth();
                int h = img.getHeight();
                IntBuffer textureBuffer = BufferUtils.createIntBuffer(w * h << 2);
                textureBuffer.clear();
                textureBuffer.put(img.pixels);
                textureBuffer.position(0).limit(img.pixels.length);
                GL11.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, 5121, textureBuffer);
				return id;
			}
        } catch (Exception var15) {
			throw new RuntimeException("!!");
		}
	}
}
