package net.minecraft.client.model;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
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
    private boolean showModel = true;
    private boolean isHidden = false;

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

    public final void setRotationPoint(float var1, float var2, float var3) {
        this.rotationPointX = var1;
        this.rotationPointY = var2;
        this.rotationPointZ = 0.0F;
    }

    public final void render(float var1) {
        if(this.showModel) {
            if(!this.compiled) {
                float var3 = var1;
                ModelRenderer var2 = this;
                this.displayList = GL11.glGenLists(1);
                GL11.glNewList(this.displayList, GL11.GL_COMPILE);
                GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

                for(int var4 = 0; var4 < var2.faces.length; ++var4) {
                    TexturedQuad var10000 = var2.faces[var4];
                    float var6 = var3;
                    TexturedQuad var5 = var10000;
                    Vec3D var7 = var5.vertexPositions[1].vector3D.subtract(var5.vertexPositions[0].vector3D).normalize();
                    Vec3D var8 = var5.vertexPositions[1].vector3D.subtract(var5.vertexPositions[2].vector3D).normalize();
                    var7 = (new Vec3D(var7.yCoord * var8.zCoord - var7.zCoord * var8.yCoord, var7.zCoord * var8.xCoord - var7.xCoord * var8.zCoord, var7.xCoord * var8.yCoord - var7.yCoord * var8.xCoord)).normalize();
                    GL11.glNormal3f(-var7.xCoord, -var7.yCoord, -var7.zCoord);

                    for(int var9 = 0; var9 < 4; ++var9) {
                        PositionTextureVertex var10 = var5.vertexPositions[var9];
                        GL11.glTexCoord2f(var10.texturePositionX, var10.texturePositionY);
                        GL11.glVertex3f(var10.vector3D.xCoord * var6, var10.vector3D.yCoord * var6, var10.vector3D.zCoord * var6);
                    }
                }

                GL11.glEnd();
                GL11.glEndList();
                var2.compiled = true;
            }

            if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if(this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                    GL11.glCallList(this.displayList);
                } else {
                    GL11.glTranslatef(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
                    GL11.glCallList(this.displayList);
                    GL11.glTranslatef(-this.rotationPointX * var1, -this.rotationPointY * var1, -this.rotationPointZ * var1);
                }
            } else {
                GL11.glPushMatrix();
                GL11.glTranslatef(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
                if(this.rotateAngleZ != 0.0F) {
                    GL11.glRotatef(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }

                if(this.rotateAngleY != 0.0F) {
                    GL11.glRotatef(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if(this.rotateAngleX != 0.0F) {
                    GL11.glRotatef(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }

                GL11.glCallList(this.displayList);
                GL11.glPopMatrix();
            }
        }
    }
}
