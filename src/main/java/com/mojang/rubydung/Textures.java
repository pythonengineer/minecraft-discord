package com.mojang.rubydung;

import java.util.HashMap;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

public class Textures {
	private static HashMap<String, Integer> idMap = new HashMap();
	private static int lastId = -9999999;

	public static int loadTexture(String resourceName, int mode) {
		try {
			if(idMap.containsKey(resourceName)) {
				return ((Integer)idMap.get(resourceName)).intValue();
			} else {
				IntBuffer e = BufferUtils.createIntBuffer(1);
				e.clear();
				GL11.glGenTextures(e);
				int id = e.get(0);
				idMap.put(resourceName, Integer.valueOf(id));
				System.out.println(resourceName + " -> " + id);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mode);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mode);
				ImageData img = ImageData.loadImageFile("/assets" + resourceName);
				int w = img.width;
				int h = img.height;
                IntBuffer textureBuffer = BufferUtils.createIntBuffer(w * h << 2);
                textureBuffer.clear();
                textureBuffer.put(img.pixels);
                textureBuffer.position(0).limit(img.pixels.length);
                GL11.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, 5121, textureBuffer);
				return id;
			}
		} catch (Exception var14) {
			throw new RuntimeException("!!");
		}
	}
}
