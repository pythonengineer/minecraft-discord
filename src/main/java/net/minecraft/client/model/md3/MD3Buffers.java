package net.minecraft.client.model.md3;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;

public final class MD3Buffers {
	public int verts;
	public int frames;
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
}
