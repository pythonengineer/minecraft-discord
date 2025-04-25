package net.minecraft.client.model.md3;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;

public final class MD3Buffers {
	public int verts;
	private int frames;
	public MD3Shader[] shaders;
	public IntBuffer triangles;
	public FloatBuffer xBuffer;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	private float[] data1;
	private float[] data2;

	public MD3Buffers(int var1, int var2, int var3) {
		this.verts = var2;
		this.frames = var3;
		this.triangles = BufferUtils.createIntBuffer(var1 * 3);
		this.xBuffer = BufferUtils.createFloatBuffer(var2 << 1);
		this.vertices = BufferUtils.createFloatBuffer(var2 * (var3 + 2) * 3);
		this.normals = BufferUtils.createFloatBuffer(var2 * (var3 + 2) * 3);
		this.data1 = new float[var2 * 3];
		this.data2 = new float[var2 * 3];
	}

	public final void setAndClearBuffers(int var1, int var2, float var3) {
		this.triangles.position(0).limit(this.triangles.capacity());
		this.xBuffer.position(0).limit(this.xBuffer.capacity());
		int var4 = 0;
		if(var3 != 0.0F) {
			this.setBuffer(this.vertices, var1, var2, var3);
			this.setBuffer(this.normals, var1, var2, var3);
			var4 = this.frames;
		}

		this.vertices.clear().position(var4 * this.verts * 3).limit((var4 + 1) * this.verts * 3);
		this.normals.clear().position(var4 * this.verts * 3).limit((var4 + 1) * this.verts * 3);
	}

	private void setBuffer(FloatBuffer var1, int var2, int var3, float var4) {
		var1.clear().position(var2 * this.verts * 3).limit((var2 + 1) * this.verts * 3);
		var1.get(this.data1);
		var1.clear().position(var3 * this.verts * 3).limit((var3 + 1) * this.verts * 3);
		var1.get(this.data2);
		float var5 = var4;
		float[] var9 = this.data2;
		float[] var8 = this.data1;
		MD3Buffers var7 = this;

		for(int var6 = 0; var6 < var7.verts * 3; ++var6) {
			var8[var6] += (var9[var6] - var8[var6]) * var5;
		}

		var2 = this.frames;
		var1.clear().position(var2 * this.verts * 3);
		var1.put(this.data1);
	}
}
