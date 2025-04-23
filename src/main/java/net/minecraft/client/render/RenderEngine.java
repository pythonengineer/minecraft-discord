package net.minecraft.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.GameSettings;
import net.minecraft.client.render.texture.TextureFX;

public class RenderEngine {
	public HashMap textureMap = new HashMap();
	public HashMap textureContentsMap = new HashMap();
	public IntBuffer singleIntBuffer = BufferUtils.createIntBuffer(1);
	public ByteBuffer imageData = BufferUtils.createByteBuffer(262144);
	public List textureList = new ArrayList();
	public GameSettings options;
	boolean clampTexture = false;

	public RenderEngine(GameSettings var1) {
		this.options = var1;
	}

	public final int getTexture(String var1) {
		Integer var2 = (Integer)this.textureMap.get(var1);
		if(var2 != null) {
			return var2.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				if(var1.startsWith("##")) {
					this.setupTexture(ImageData.loadImageFile("/assets" + var1.substring(2)), var4);
				} else {
					this.setupTexture(ImageData.loadImageFile("/assets" + var1), var4);
				}

				this.textureMap.put(var1, Integer.valueOf(var4));
				return var4;
			} catch (Exception var3) {
				throw new RuntimeException("!!");
			}
		}
	}

	public final void setupTexture(ImageData var1, int var2) {
		GL11.glBindTexture(var2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		var2 = var1.getWidth();
		int var3 = var1.getHeight();
		int[] var4 = new int[var2 * var3];
		byte[] var5 = new byte[var2 * var3 << 2];
		var1.getRGB(0, 0, var2, var3, var4, 0, var2);

		for(int var11 = 0; var11 < var4.length; ++var11) {
			int a = var4[var11] >>> 24;
			int b = var4[var11] >> 16 & 255;
			int g = var4[var11] >> 8 & 255;
			int r = var4[var11] & 255;
			if(this.options != null && this.options.anaglyph) {
				int var10 = (r * 30 + g * 59 + b * 11) / 100;
				g = (r * 30 + g * 70) / 100;
				b = (r * 30 + b * 70) / 100;
				r = var10;
			}

			var5[var11 << 2] = (byte)r;
			var5[(var11 << 2) + 1] = (byte)g;
			var5[(var11 << 2) + 2] = (byte)b;
			var5[(var11 << 2) + 3] = (byte)a;
		}

		this.imageData.clear();
		this.imageData.put(var5);
		this.imageData.position(0).limit(var5.length);
		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var2, var3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
	}

	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}
}
