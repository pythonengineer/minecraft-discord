package com.mojang.minecraft.renderer;

import com.mojang.minecraft.phys.AABB;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public final class Frustum {
	private float[][] m_Frustum = new float[6][4];
	private static Frustum frustum = new Frustum();
	private FloatBuffer _proj = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _modl = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _clip = BufferUtils.createFloatBuffer(16);
	private float[] proj = new float[16];
	private float[] modl = new float[16];
	private float[] clip = new float[16];

	public static Frustum getFrustum() {
		Frustum frustum0 = frustum;
		frustum._proj.clear();
		frustum0._modl.clear();
		frustum0._clip.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, frustum0._proj);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, frustum0._modl);
		frustum0._proj.flip().limit(16);
		frustum0._proj.get(frustum0.proj);
		frustum0._modl.flip().limit(16);
		frustum0._modl.get(frustum0.modl);
		frustum0.clip[0] = frustum0.modl[0] * frustum0.proj[0] + frustum0.modl[1] * frustum0.proj[4] + frustum0.modl[2] * frustum0.proj[8] + frustum0.modl[3] * frustum0.proj[12];
		frustum0.clip[1] = frustum0.modl[0] * frustum0.proj[1] + frustum0.modl[1] * frustum0.proj[5] + frustum0.modl[2] * frustum0.proj[9] + frustum0.modl[3] * frustum0.proj[13];
		frustum0.clip[2] = frustum0.modl[0] * frustum0.proj[2] + frustum0.modl[1] * frustum0.proj[6] + frustum0.modl[2] * frustum0.proj[10] + frustum0.modl[3] * frustum0.proj[14];
		frustum0.clip[3] = frustum0.modl[0] * frustum0.proj[3] + frustum0.modl[1] * frustum0.proj[7] + frustum0.modl[2] * frustum0.proj[11] + frustum0.modl[3] * frustum0.proj[15];
		frustum0.clip[4] = frustum0.modl[4] * frustum0.proj[0] + frustum0.modl[5] * frustum0.proj[4] + frustum0.modl[6] * frustum0.proj[8] + frustum0.modl[7] * frustum0.proj[12];
		frustum0.clip[5] = frustum0.modl[4] * frustum0.proj[1] + frustum0.modl[5] * frustum0.proj[5] + frustum0.modl[6] * frustum0.proj[9] + frustum0.modl[7] * frustum0.proj[13];
		frustum0.clip[6] = frustum0.modl[4] * frustum0.proj[2] + frustum0.modl[5] * frustum0.proj[6] + frustum0.modl[6] * frustum0.proj[10] + frustum0.modl[7] * frustum0.proj[14];
		frustum0.clip[7] = frustum0.modl[4] * frustum0.proj[3] + frustum0.modl[5] * frustum0.proj[7] + frustum0.modl[6] * frustum0.proj[11] + frustum0.modl[7] * frustum0.proj[15];
		frustum0.clip[8] = frustum0.modl[8] * frustum0.proj[0] + frustum0.modl[9] * frustum0.proj[4] + frustum0.modl[10] * frustum0.proj[8] + frustum0.modl[11] * frustum0.proj[12];
		frustum0.clip[9] = frustum0.modl[8] * frustum0.proj[1] + frustum0.modl[9] * frustum0.proj[5] + frustum0.modl[10] * frustum0.proj[9] + frustum0.modl[11] * frustum0.proj[13];
		frustum0.clip[10] = frustum0.modl[8] * frustum0.proj[2] + frustum0.modl[9] * frustum0.proj[6] + frustum0.modl[10] * frustum0.proj[10] + frustum0.modl[11] * frustum0.proj[14];
		frustum0.clip[11] = frustum0.modl[8] * frustum0.proj[3] + frustum0.modl[9] * frustum0.proj[7] + frustum0.modl[10] * frustum0.proj[11] + frustum0.modl[11] * frustum0.proj[15];
		frustum0.clip[12] = frustum0.modl[12] * frustum0.proj[0] + frustum0.modl[13] * frustum0.proj[4] + frustum0.modl[14] * frustum0.proj[8] + frustum0.modl[15] * frustum0.proj[12];
		frustum0.clip[13] = frustum0.modl[12] * frustum0.proj[1] + frustum0.modl[13] * frustum0.proj[5] + frustum0.modl[14] * frustum0.proj[9] + frustum0.modl[15] * frustum0.proj[13];
		frustum0.clip[14] = frustum0.modl[12] * frustum0.proj[2] + frustum0.modl[13] * frustum0.proj[6] + frustum0.modl[14] * frustum0.proj[10] + frustum0.modl[15] * frustum0.proj[14];
		frustum0.clip[15] = frustum0.modl[12] * frustum0.proj[3] + frustum0.modl[13] * frustum0.proj[7] + frustum0.modl[14] * frustum0.proj[11] + frustum0.modl[15] * frustum0.proj[15];
		frustum0.m_Frustum[0][0] = frustum0.clip[3] - frustum0.clip[0];
		frustum0.m_Frustum[0][1] = frustum0.clip[7] - frustum0.clip[4];
		frustum0.m_Frustum[0][2] = frustum0.clip[11] - frustum0.clip[8];
		frustum0.m_Frustum[0][3] = frustum0.clip[15] - frustum0.clip[12];
		normalizePlane(frustum0.m_Frustum, 0);
		frustum0.m_Frustum[1][0] = frustum0.clip[3] + frustum0.clip[0];
		frustum0.m_Frustum[1][1] = frustum0.clip[7] + frustum0.clip[4];
		frustum0.m_Frustum[1][2] = frustum0.clip[11] + frustum0.clip[8];
		frustum0.m_Frustum[1][3] = frustum0.clip[15] + frustum0.clip[12];
		normalizePlane(frustum0.m_Frustum, 1);
		frustum0.m_Frustum[2][0] = frustum0.clip[3] + frustum0.clip[1];
		frustum0.m_Frustum[2][1] = frustum0.clip[7] + frustum0.clip[5];
		frustum0.m_Frustum[2][2] = frustum0.clip[11] + frustum0.clip[9];
		frustum0.m_Frustum[2][3] = frustum0.clip[15] + frustum0.clip[13];
		normalizePlane(frustum0.m_Frustum, 2);
		frustum0.m_Frustum[3][0] = frustum0.clip[3] - frustum0.clip[1];
		frustum0.m_Frustum[3][1] = frustum0.clip[7] - frustum0.clip[5];
		frustum0.m_Frustum[3][2] = frustum0.clip[11] - frustum0.clip[9];
		frustum0.m_Frustum[3][3] = frustum0.clip[15] - frustum0.clip[13];
		normalizePlane(frustum0.m_Frustum, 3);
		frustum0.m_Frustum[4][0] = frustum0.clip[3] - frustum0.clip[2];
		frustum0.m_Frustum[4][1] = frustum0.clip[7] - frustum0.clip[6];
		frustum0.m_Frustum[4][2] = frustum0.clip[11] - frustum0.clip[10];
		frustum0.m_Frustum[4][3] = frustum0.clip[15] - frustum0.clip[14];
		normalizePlane(frustum0.m_Frustum, 4);
		frustum0.m_Frustum[5][0] = frustum0.clip[3] + frustum0.clip[2];
		frustum0.m_Frustum[5][1] = frustum0.clip[7] + frustum0.clip[6];
		frustum0.m_Frustum[5][2] = frustum0.clip[11] + frustum0.clip[10];
		frustum0.m_Frustum[5][3] = frustum0.clip[15] + frustum0.clip[14];
		normalizePlane(frustum0.m_Frustum, 5);
		return frustum;
	}

	private static void normalizePlane(float[][] f0, int i1) {
		float f2 = (float)Math.sqrt((double)(f0[i1][0] * f0[i1][0] + f0[i1][1] * f0[i1][1] + f0[i1][2] * f0[i1][2]));
		f0[i1][0] /= f2;
		f0[i1][1] /= f2;
		f0[i1][2] /= f2;
		f0[i1][3] /= f2;
	}

    public final boolean cubeInFrustum(float f1, float f2, float f3, float f4, float f5, float f6) {
        for(int i7 = 0; i7 < 6; ++i7) {
            if(this.m_Frustum[i7][0] * f1 + this.m_Frustum[i7][1] * f2 + this.m_Frustum[i7][2] * f3 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f4 + this.m_Frustum[i7][1] * f2 + this.m_Frustum[i7][2] * f3 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f1 + this.m_Frustum[i7][1] * f5 + this.m_Frustum[i7][2] * f3 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f4 + this.m_Frustum[i7][1] * f5 + this.m_Frustum[i7][2] * f3 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f1 + this.m_Frustum[i7][1] * f2 + this.m_Frustum[i7][2] * f6 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f4 + this.m_Frustum[i7][1] * f2 + this.m_Frustum[i7][2] * f6 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f1 + this.m_Frustum[i7][1] * f5 + this.m_Frustum[i7][2] * f6 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * f4 + this.m_Frustum[i7][1] * f5 + this.m_Frustum[i7][2] * f6 + this.m_Frustum[i7][3] <= 0.0F) {
                return false;
            }
        }

        return true;
    }

    public final boolean isVisible(AABB aABB1) {
        return this.cubeInFrustum(aABB1.x0, aABB1.y0, aABB1.z0, aABB1.x1, aABB1.y1, aABB1.z1);
    }
}
