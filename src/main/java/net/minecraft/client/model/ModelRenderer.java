package net.minecraft.client.model;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.render.Tessellator;

public class ModelRenderer {
	private PositionTextureVertex[] corners;
	private TexturedQuad[] faces;
	private int textureOffsetX;
	private int textureOffsetY;
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled = false;
	private int displayList = 0;
	public boolean mirror = false;
	public boolean showModel = true;
	public boolean isHidden = false;

	public ModelRenderer(int u, int v) {
		this.textureOffsetX = u;
		this.textureOffsetY = v;
	}

	public void addBox(float offsetX, float offsetY, float offsetZ, int width, int height, int depth, float scaleFactor) {
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
                this.faces[i16].flipFace();
			}
		}

	}

	public void setRotationPoint(float rotX, float rotY, float rotZ) {
		this.rotationPointX = rotX;
		this.rotationPointY = rotY;
		this.rotationPointZ = rotZ;
	}

	public void render(float partialTicks) {
        if(!this.isHidden) {
    		if(this.showModel) {
    			if(!this.compiled) {
                    this.compileDisplayList(partialTicks);
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

    private void compileDisplayList(float partialTicks) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
        Tessellator tessellator2 = Tessellator.instance;

        for(int i3 = 0; i3 < this.faces.length; ++i3) {
            this.faces[i3].draw(tessellator2, partialTicks);
        }

        GL11.glEndList();
        this.compiled = true;
    }
}
