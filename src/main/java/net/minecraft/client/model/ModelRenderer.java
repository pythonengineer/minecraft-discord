package net.minecraft.client.model;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.physics.Vec3D;

public final class ModelRenderer {
	private PositionTextureVertex[] corners;
	private TexturedQuad[] faces;
	private int textureOffsetX;
	private int textureOffsetY;
	private float rotationPointX;
	private float rotationPointY;
	private float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled = false;
	private int displayList = 0;
	public boolean mirror = false;
	public boolean showModel = true;
	private boolean isHidden = false;

	public ModelRenderer(int u, int v) {
		this.textureOffsetX = u;
		this.textureOffsetY = v;
	}

	public final void addBox(float offsetX, float offsetY, float offsetZ, int width, int height, int depth, float scaleFactor) {
		this.corners = new PositionTextureVertex[8];
		this.faces = new TexturedQuad[6];
		float f8 = offsetX + (float)width;
		float f9 = offsetY + (float)height;
		float f10 = offsetZ + (float)depth;
		offsetX -= scaleFactor;
		offsetY -= scaleFactor;
		offsetZ -= scaleFactor;
		f8 += scaleFactor;
		f9 += scaleFactor;
		f10 += scaleFactor;
		if(this.mirror) {
			scaleFactor = f8;
			f8 = offsetX;
			offsetX = scaleFactor;
		}

		PositionTextureVertex positionTextureVertex20 = new PositionTextureVertex(offsetX, offsetY, offsetZ, 0.0F, 0.0F);
		PositionTextureVertex positionTextureVertex11 = new PositionTextureVertex(f8, offsetY, offsetZ, 0.0F, 8.0F);
		PositionTextureVertex positionTextureVertex12 = new PositionTextureVertex(f8, f9, offsetZ, 8.0F, 8.0F);
		PositionTextureVertex positionTextureVertex18 = new PositionTextureVertex(offsetX, f9, offsetZ, 8.0F, 0.0F);
		PositionTextureVertex positionTextureVertex13 = new PositionTextureVertex(offsetX, offsetY, f10, 0.0F, 0.0F);
		PositionTextureVertex positionTextureVertex15 = new PositionTextureVertex(f8, offsetY, f10, 0.0F, 8.0F);
		PositionTextureVertex positionTextureVertex21 = new PositionTextureVertex(f8, f9, f10, 8.0F, 8.0F);
		PositionTextureVertex positionTextureVertex14 = new PositionTextureVertex(offsetX, f9, f10, 8.0F, 0.0F);
		this.corners[0] = positionTextureVertex20;
		this.corners[1] = positionTextureVertex11;
		this.corners[2] = positionTextureVertex12;
		this.corners[3] = positionTextureVertex18;
		this.corners[4] = positionTextureVertex13;
		this.corners[5] = positionTextureVertex15;
		this.corners[6] = positionTextureVertex21;
		this.corners[7] = positionTextureVertex14;
		this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex15, positionTextureVertex11, positionTextureVertex12, positionTextureVertex21}, this.textureOffsetX + depth + width, this.textureOffsetY + depth, this.textureOffsetX + depth + width + depth, this.textureOffsetY + depth + height);
		this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex20, positionTextureVertex13, positionTextureVertex14, positionTextureVertex18}, this.textureOffsetX, this.textureOffsetY + depth, this.textureOffsetX + depth, this.textureOffsetY + depth + height);
		this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex15, positionTextureVertex13, positionTextureVertex20, positionTextureVertex11}, this.textureOffsetX + depth, this.textureOffsetY, this.textureOffsetX + depth + width, this.textureOffsetY + depth);
		this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex12, positionTextureVertex18, positionTextureVertex14, positionTextureVertex21}, this.textureOffsetX + depth + width, this.textureOffsetY, this.textureOffsetX + depth + width + width, this.textureOffsetY + depth);
		this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex11, positionTextureVertex20, positionTextureVertex18, positionTextureVertex12}, this.textureOffsetX + depth, this.textureOffsetY + depth, this.textureOffsetX + depth + width, this.textureOffsetY + depth + height);
		this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex13, positionTextureVertex15, positionTextureVertex21, positionTextureVertex14}, this.textureOffsetX + depth + width + depth, this.textureOffsetY + depth, this.textureOffsetX + depth + width + depth + width, this.textureOffsetY + depth + height);
		if(this.mirror) {
			for(int i16 = 0; i16 < this.faces.length; ++i16) {
				TexturedQuad texturedQuad17;
				PositionTextureVertex[] positionTextureVertex19 = new PositionTextureVertex[(texturedQuad17 = this.faces[i16]).vertexPositions.length];

				for(width = 0; width < texturedQuad17.vertexPositions.length; ++width) {
					positionTextureVertex19[width] = texturedQuad17.vertexPositions[texturedQuad17.vertexPositions.length - width - 1];
				}

				texturedQuad17.vertexPositions = positionTextureVertex19;
			}
		}

	}

	public final void setRotationPoint(float rotX, float rotY, float rotZ) {
		this.rotationPointX = rotX;
		this.rotationPointY = rotY;
		this.rotationPointZ = rotZ;
	}

	public final void render(float partialTicks) {
		if(this.showModel) {
			if(!this.compiled) {
				float f3 = partialTicks;
				ModelRenderer modelRenderer2 = this;
				this.displayList = GL11.glGenLists(1);
				GL11.glNewList(this.displayList, GL11.GL_COMPILE);
				Tessellator tessellator4 = Tessellator.instance;

				for(int i5 = 0; i5 < modelRenderer2.faces.length; ++i5) {
					tessellator4.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
					TexturedQuad texturedQuad10000 = modelRenderer2.faces[i5];
					float f8 = f3;
					Tessellator tessellator7 = tessellator4;
					TexturedQuad texturedQuad6 = texturedQuad10000;
					Vec3D vec3D9 = texturedQuad10000.vertexPositions[1].vector3D.subtract(texturedQuad6.vertexPositions[0].vector3D).normalize();
					Vec3D vec3D10 = texturedQuad6.vertexPositions[1].vector3D.subtract(texturedQuad6.vertexPositions[2].vector3D).normalize();
					Tessellator.setNormal((float)(-(vec3D9 = (new Vec3D(vec3D9.yCoord * vec3D10.zCoord - vec3D9.zCoord * vec3D10.yCoord, vec3D9.zCoord * vec3D10.xCoord - vec3D9.xCoord * vec3D10.zCoord, vec3D9.xCoord * vec3D10.yCoord - vec3D9.yCoord * vec3D10.xCoord)).normalize()).xCoord), (float)(-vec3D9.yCoord), (float)(-vec3D9.zCoord));

					for(int i11 = 0; i11 < 4; ++i11) {
						PositionTextureVertex positionTextureVertex12 = texturedQuad6.vertexPositions[i11];
						tessellator7.addVertexWithUV((double)((float)positionTextureVertex12.vector3D.xCoord * f8), (double)((float)positionTextureVertex12.vector3D.yCoord * f8), (double)((float)positionTextureVertex12.vector3D.zCoord * f8), (double)positionTextureVertex12.texturePositionX, (double)positionTextureVertex12.texturePositionY);
					}

					tessellator4.draw();
				}

				GL11.glEndList();
				modelRenderer2.compiled = true;
			}

			if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
				if(this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
					GL11.glCallList(this.displayList);
				} else {
					GL11.glTranslatef(this.rotationPointX * partialTicks, this.rotationPointY * partialTicks, this.rotationPointZ * partialTicks);
					GL11.glCallList(this.displayList);
					GL11.glTranslatef(-this.rotationPointX * partialTicks, -this.rotationPointY * partialTicks, -this.rotationPointZ * partialTicks);
				}
			} else {
				GL11.glPushMatrix();
				GL11.glTranslatef(this.rotationPointX * partialTicks, this.rotationPointY * partialTicks, this.rotationPointZ * partialTicks);
				if(this.rotateAngleZ != 0.0F) {
					GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
				}

				if(this.rotateAngleY != 0.0F) {
					GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
				}

				if(this.rotateAngleX != 0.0F) {
					GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glCallList(this.displayList);
				GL11.glPopMatrix();
			}
		}
	}
}