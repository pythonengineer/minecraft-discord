package net.minecraft.client.model.md3;

import java.util.HashMap;

import net.lax1dude.eaglercraft.lwjgl.BufferUtils;

public final class MD3Model {
	public MD3Vertices vertices;
	public int displayList = 0;

	public MD3Model(MD3Vertices var1) {
		new HashMap();
		BufferUtils.createFloatBuffer(16);
		this.vertices = var1;
	}
}
