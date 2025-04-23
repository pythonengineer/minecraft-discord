package net.minecraft.client.model;

public final class ModelRenderer {
	private PositionTextureVertex[] corners;
	private TexturedQuad[] faces;
	private int textureOffsetX;
	private int textureOffsetY;
	public boolean mirror = false;

	public ModelRenderer(int var1, int var2) {
		this.textureOffsetX = var1;
		this.textureOffsetY = var2;
	}

	public final void addBox(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
		this.corners = new PositionTextureVertex[8];
		this.faces = new TexturedQuad[6];
		float var8 = var1 + (float)var4;
		float var9 = var2 + (float)var5;
		float var10 = var3 + (float)var6;
		var1 -= var7;
		var2 -= var7;
		var3 -= var7;
		var8 += var7;
		var9 += var7;
		var10 += var7;
		if(this.mirror) {
			var7 = var8;
			var8 = var1;
			var1 = var7;
		}

		PositionTextureVertex var20 = new PositionTextureVertex(var1, var2, var3, 0.0F, 0.0F);
		PositionTextureVertex var11 = new PositionTextureVertex(var8, var2, var3, 0.0F, 8.0F);
		PositionTextureVertex var12 = new PositionTextureVertex(var8, var9, var3, 8.0F, 8.0F);
		PositionTextureVertex var18 = new PositionTextureVertex(var1, var9, var3, 8.0F, 0.0F);
		PositionTextureVertex var13 = new PositionTextureVertex(var1, var2, var10, 0.0F, 0.0F);
		PositionTextureVertex var15 = new PositionTextureVertex(var8, var2, var10, 0.0F, 8.0F);
		PositionTextureVertex var21 = new PositionTextureVertex(var8, var9, var10, 8.0F, 8.0F);
		PositionTextureVertex var14 = new PositionTextureVertex(var1, var9, var10, 8.0F, 0.0F);
		this.corners[0] = var20;
		this.corners[1] = var11;
		this.corners[2] = var12;
		this.corners[3] = var18;
		this.corners[4] = var13;
		this.corners[5] = var15;
		this.corners[6] = var21;
		this.corners[7] = var14;
		this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{var15, var11, var12, var21}, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4 + var6, this.textureOffsetY + var6 + var5);
		this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{var20, var13, var14, var18}, this.textureOffsetX, this.textureOffsetY + var6, this.textureOffsetX + var6, this.textureOffsetY + var6 + var5);
		this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{var15, var13, var20, var11}, this.textureOffsetX + var6, this.textureOffsetY, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6);
		this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{var12, var18, var14, var21}, this.textureOffsetX + var6 + var4, this.textureOffsetY, this.textureOffsetX + var6 + var4 + var4, this.textureOffsetY + var6);
		this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{var11, var20, var18, var12}, this.textureOffsetX + var6, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6 + var5);
		this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{var13, var15, var21, var14}, this.textureOffsetX + var6 + var4 + var6, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4 + var6 + var4, this.textureOffsetY + var6 + var5);
		if(this.mirror) {
			for(int var16 = 0; var16 < this.faces.length; ++var16) {
				TexturedQuad var17 = this.faces[var16];
				PositionTextureVertex[] var19 = new PositionTextureVertex[var17.vertexPositions.length];

				for(var4 = 0; var4 < var17.vertexPositions.length; ++var4) {
					var19[var4] = var17.vertexPositions[var17.vertexPositions.length - var4 - 1];
				}

				var17.vertexPositions = var19;
			}
		}

	}
}
