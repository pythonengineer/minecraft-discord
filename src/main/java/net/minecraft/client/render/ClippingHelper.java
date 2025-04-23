package net.minecraft.client.render;

public class ClippingHelper {
	public float[][] frustrum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];

	public final boolean isBoundingBoxInFrustrum(float var1, float var2, float var3, float var4, float var5, float var6) {
		for(int var7 = 0; var7 < 6; ++var7) {
			if(this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var3 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var2 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var1 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F && this.frustrum[var7][0] * var4 + this.frustrum[var7][1] * var5 + this.frustrum[var7][2] * var6 + this.frustrum[var7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
}
