package net.minecraft.client.render.camera;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.GLAllocation;

public final class ClippingHelperImplementation extends ClippingHelper {
	private static ClippingHelperImplementation instance = new ClippingHelperImplementation();
	private FloatBuffer projectionMatrixBuffer = GLAllocation.createFloatBuffer(16);
	private FloatBuffer modelviewMatrixBuffer = GLAllocation.createFloatBuffer(16);
	private FloatBuffer floatBuffer = GLAllocation.createFloatBuffer(16);

	public static ClippingHelper getInstance() {
		ClippingHelperImplementation clippingHelperImplementation0 = instance;
		instance.projectionMatrixBuffer.clear();
		clippingHelperImplementation0.modelviewMatrixBuffer.clear();
		clippingHelperImplementation0.floatBuffer.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, clippingHelperImplementation0.projectionMatrixBuffer);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, clippingHelperImplementation0.modelviewMatrixBuffer);
		clippingHelperImplementation0.projectionMatrixBuffer.flip().limit(16);
		clippingHelperImplementation0.projectionMatrixBuffer.get(clippingHelperImplementation0.projectionMatrix);
		clippingHelperImplementation0.modelviewMatrixBuffer.flip().limit(16);
		clippingHelperImplementation0.modelviewMatrixBuffer.get(clippingHelperImplementation0.modelviewMatrix);
		clippingHelperImplementation0.clippingMatrix[0] = clippingHelperImplementation0.modelviewMatrix[0] * clippingHelperImplementation0.projectionMatrix[0] + clippingHelperImplementation0.modelviewMatrix[1] * clippingHelperImplementation0.projectionMatrix[4] + clippingHelperImplementation0.modelviewMatrix[2] * clippingHelperImplementation0.projectionMatrix[8] + clippingHelperImplementation0.modelviewMatrix[3] * clippingHelperImplementation0.projectionMatrix[12];
		clippingHelperImplementation0.clippingMatrix[1] = clippingHelperImplementation0.modelviewMatrix[0] * clippingHelperImplementation0.projectionMatrix[1] + clippingHelperImplementation0.modelviewMatrix[1] * clippingHelperImplementation0.projectionMatrix[5] + clippingHelperImplementation0.modelviewMatrix[2] * clippingHelperImplementation0.projectionMatrix[9] + clippingHelperImplementation0.modelviewMatrix[3] * clippingHelperImplementation0.projectionMatrix[13];
		clippingHelperImplementation0.clippingMatrix[2] = clippingHelperImplementation0.modelviewMatrix[0] * clippingHelperImplementation0.projectionMatrix[2] + clippingHelperImplementation0.modelviewMatrix[1] * clippingHelperImplementation0.projectionMatrix[6] + clippingHelperImplementation0.modelviewMatrix[2] * clippingHelperImplementation0.projectionMatrix[10] + clippingHelperImplementation0.modelviewMatrix[3] * clippingHelperImplementation0.projectionMatrix[14];
		clippingHelperImplementation0.clippingMatrix[3] = clippingHelperImplementation0.modelviewMatrix[0] * clippingHelperImplementation0.projectionMatrix[3] + clippingHelperImplementation0.modelviewMatrix[1] * clippingHelperImplementation0.projectionMatrix[7] + clippingHelperImplementation0.modelviewMatrix[2] * clippingHelperImplementation0.projectionMatrix[11] + clippingHelperImplementation0.modelviewMatrix[3] * clippingHelperImplementation0.projectionMatrix[15];
		clippingHelperImplementation0.clippingMatrix[4] = clippingHelperImplementation0.modelviewMatrix[4] * clippingHelperImplementation0.projectionMatrix[0] + clippingHelperImplementation0.modelviewMatrix[5] * clippingHelperImplementation0.projectionMatrix[4] + clippingHelperImplementation0.modelviewMatrix[6] * clippingHelperImplementation0.projectionMatrix[8] + clippingHelperImplementation0.modelviewMatrix[7] * clippingHelperImplementation0.projectionMatrix[12];
		clippingHelperImplementation0.clippingMatrix[5] = clippingHelperImplementation0.modelviewMatrix[4] * clippingHelperImplementation0.projectionMatrix[1] + clippingHelperImplementation0.modelviewMatrix[5] * clippingHelperImplementation0.projectionMatrix[5] + clippingHelperImplementation0.modelviewMatrix[6] * clippingHelperImplementation0.projectionMatrix[9] + clippingHelperImplementation0.modelviewMatrix[7] * clippingHelperImplementation0.projectionMatrix[13];
		clippingHelperImplementation0.clippingMatrix[6] = clippingHelperImplementation0.modelviewMatrix[4] * clippingHelperImplementation0.projectionMatrix[2] + clippingHelperImplementation0.modelviewMatrix[5] * clippingHelperImplementation0.projectionMatrix[6] + clippingHelperImplementation0.modelviewMatrix[6] * clippingHelperImplementation0.projectionMatrix[10] + clippingHelperImplementation0.modelviewMatrix[7] * clippingHelperImplementation0.projectionMatrix[14];
		clippingHelperImplementation0.clippingMatrix[7] = clippingHelperImplementation0.modelviewMatrix[4] * clippingHelperImplementation0.projectionMatrix[3] + clippingHelperImplementation0.modelviewMatrix[5] * clippingHelperImplementation0.projectionMatrix[7] + clippingHelperImplementation0.modelviewMatrix[6] * clippingHelperImplementation0.projectionMatrix[11] + clippingHelperImplementation0.modelviewMatrix[7] * clippingHelperImplementation0.projectionMatrix[15];
		clippingHelperImplementation0.clippingMatrix[8] = clippingHelperImplementation0.modelviewMatrix[8] * clippingHelperImplementation0.projectionMatrix[0] + clippingHelperImplementation0.modelviewMatrix[9] * clippingHelperImplementation0.projectionMatrix[4] + clippingHelperImplementation0.modelviewMatrix[10] * clippingHelperImplementation0.projectionMatrix[8] + clippingHelperImplementation0.modelviewMatrix[11] * clippingHelperImplementation0.projectionMatrix[12];
		clippingHelperImplementation0.clippingMatrix[9] = clippingHelperImplementation0.modelviewMatrix[8] * clippingHelperImplementation0.projectionMatrix[1] + clippingHelperImplementation0.modelviewMatrix[9] * clippingHelperImplementation0.projectionMatrix[5] + clippingHelperImplementation0.modelviewMatrix[10] * clippingHelperImplementation0.projectionMatrix[9] + clippingHelperImplementation0.modelviewMatrix[11] * clippingHelperImplementation0.projectionMatrix[13];
		clippingHelperImplementation0.clippingMatrix[10] = clippingHelperImplementation0.modelviewMatrix[8] * clippingHelperImplementation0.projectionMatrix[2] + clippingHelperImplementation0.modelviewMatrix[9] * clippingHelperImplementation0.projectionMatrix[6] + clippingHelperImplementation0.modelviewMatrix[10] * clippingHelperImplementation0.projectionMatrix[10] + clippingHelperImplementation0.modelviewMatrix[11] * clippingHelperImplementation0.projectionMatrix[14];
		clippingHelperImplementation0.clippingMatrix[11] = clippingHelperImplementation0.modelviewMatrix[8] * clippingHelperImplementation0.projectionMatrix[3] + clippingHelperImplementation0.modelviewMatrix[9] * clippingHelperImplementation0.projectionMatrix[7] + clippingHelperImplementation0.modelviewMatrix[10] * clippingHelperImplementation0.projectionMatrix[11] + clippingHelperImplementation0.modelviewMatrix[11] * clippingHelperImplementation0.projectionMatrix[15];
		clippingHelperImplementation0.clippingMatrix[12] = clippingHelperImplementation0.modelviewMatrix[12] * clippingHelperImplementation0.projectionMatrix[0] + clippingHelperImplementation0.modelviewMatrix[13] * clippingHelperImplementation0.projectionMatrix[4] + clippingHelperImplementation0.modelviewMatrix[14] * clippingHelperImplementation0.projectionMatrix[8] + clippingHelperImplementation0.modelviewMatrix[15] * clippingHelperImplementation0.projectionMatrix[12];
		clippingHelperImplementation0.clippingMatrix[13] = clippingHelperImplementation0.modelviewMatrix[12] * clippingHelperImplementation0.projectionMatrix[1] + clippingHelperImplementation0.modelviewMatrix[13] * clippingHelperImplementation0.projectionMatrix[5] + clippingHelperImplementation0.modelviewMatrix[14] * clippingHelperImplementation0.projectionMatrix[9] + clippingHelperImplementation0.modelviewMatrix[15] * clippingHelperImplementation0.projectionMatrix[13];
		clippingHelperImplementation0.clippingMatrix[14] = clippingHelperImplementation0.modelviewMatrix[12] * clippingHelperImplementation0.projectionMatrix[2] + clippingHelperImplementation0.modelviewMatrix[13] * clippingHelperImplementation0.projectionMatrix[6] + clippingHelperImplementation0.modelviewMatrix[14] * clippingHelperImplementation0.projectionMatrix[10] + clippingHelperImplementation0.modelviewMatrix[15] * clippingHelperImplementation0.projectionMatrix[14];
		clippingHelperImplementation0.clippingMatrix[15] = clippingHelperImplementation0.modelviewMatrix[12] * clippingHelperImplementation0.projectionMatrix[3] + clippingHelperImplementation0.modelviewMatrix[13] * clippingHelperImplementation0.projectionMatrix[7] + clippingHelperImplementation0.modelviewMatrix[14] * clippingHelperImplementation0.projectionMatrix[11] + clippingHelperImplementation0.modelviewMatrix[15] * clippingHelperImplementation0.projectionMatrix[15];
		clippingHelperImplementation0.frustum[0][0] = clippingHelperImplementation0.clippingMatrix[3] - clippingHelperImplementation0.clippingMatrix[0];
		clippingHelperImplementation0.frustum[0][1] = clippingHelperImplementation0.clippingMatrix[7] - clippingHelperImplementation0.clippingMatrix[4];
		clippingHelperImplementation0.frustum[0][2] = clippingHelperImplementation0.clippingMatrix[11] - clippingHelperImplementation0.clippingMatrix[8];
		clippingHelperImplementation0.frustum[0][3] = clippingHelperImplementation0.clippingMatrix[15] - clippingHelperImplementation0.clippingMatrix[12];
		normalize(clippingHelperImplementation0.frustum, 0);
		clippingHelperImplementation0.frustum[1][0] = clippingHelperImplementation0.clippingMatrix[3] + clippingHelperImplementation0.clippingMatrix[0];
		clippingHelperImplementation0.frustum[1][1] = clippingHelperImplementation0.clippingMatrix[7] + clippingHelperImplementation0.clippingMatrix[4];
		clippingHelperImplementation0.frustum[1][2] = clippingHelperImplementation0.clippingMatrix[11] + clippingHelperImplementation0.clippingMatrix[8];
		clippingHelperImplementation0.frustum[1][3] = clippingHelperImplementation0.clippingMatrix[15] + clippingHelperImplementation0.clippingMatrix[12];
		normalize(clippingHelperImplementation0.frustum, 1);
		clippingHelperImplementation0.frustum[2][0] = clippingHelperImplementation0.clippingMatrix[3] + clippingHelperImplementation0.clippingMatrix[1];
		clippingHelperImplementation0.frustum[2][1] = clippingHelperImplementation0.clippingMatrix[7] + clippingHelperImplementation0.clippingMatrix[5];
		clippingHelperImplementation0.frustum[2][2] = clippingHelperImplementation0.clippingMatrix[11] + clippingHelperImplementation0.clippingMatrix[9];
		clippingHelperImplementation0.frustum[2][3] = clippingHelperImplementation0.clippingMatrix[15] + clippingHelperImplementation0.clippingMatrix[13];
		normalize(clippingHelperImplementation0.frustum, 2);
		clippingHelperImplementation0.frustum[3][0] = clippingHelperImplementation0.clippingMatrix[3] - clippingHelperImplementation0.clippingMatrix[1];
		clippingHelperImplementation0.frustum[3][1] = clippingHelperImplementation0.clippingMatrix[7] - clippingHelperImplementation0.clippingMatrix[5];
		clippingHelperImplementation0.frustum[3][2] = clippingHelperImplementation0.clippingMatrix[11] - clippingHelperImplementation0.clippingMatrix[9];
		clippingHelperImplementation0.frustum[3][3] = clippingHelperImplementation0.clippingMatrix[15] - clippingHelperImplementation0.clippingMatrix[13];
		normalize(clippingHelperImplementation0.frustum, 3);
		clippingHelperImplementation0.frustum[4][0] = clippingHelperImplementation0.clippingMatrix[3] - clippingHelperImplementation0.clippingMatrix[2];
		clippingHelperImplementation0.frustum[4][1] = clippingHelperImplementation0.clippingMatrix[7] - clippingHelperImplementation0.clippingMatrix[6];
		clippingHelperImplementation0.frustum[4][2] = clippingHelperImplementation0.clippingMatrix[11] - clippingHelperImplementation0.clippingMatrix[10];
		clippingHelperImplementation0.frustum[4][3] = clippingHelperImplementation0.clippingMatrix[15] - clippingHelperImplementation0.clippingMatrix[14];
		normalize(clippingHelperImplementation0.frustum, 4);
		clippingHelperImplementation0.frustum[5][0] = clippingHelperImplementation0.clippingMatrix[3] + clippingHelperImplementation0.clippingMatrix[2];
		clippingHelperImplementation0.frustum[5][1] = clippingHelperImplementation0.clippingMatrix[7] + clippingHelperImplementation0.clippingMatrix[6];
		clippingHelperImplementation0.frustum[5][2] = clippingHelperImplementation0.clippingMatrix[11] + clippingHelperImplementation0.clippingMatrix[10];
		clippingHelperImplementation0.frustum[5][3] = clippingHelperImplementation0.clippingMatrix[15] + clippingHelperImplementation0.clippingMatrix[14];
		normalize(clippingHelperImplementation0.frustum, 5);
		return instance;
	}

	private static void normalize(float[][] values, int index) {
		float f2 = MathHelper.sqrt_float(values[index][0] * values[index][0] + values[index][1] * values[index][1] + values[index][2] * values[index][2]);
		values[index][0] /= f2;
		values[index][1] /= f2;
		values[index][2] /= f2;
		values[index][3] /= f2;
	}
}