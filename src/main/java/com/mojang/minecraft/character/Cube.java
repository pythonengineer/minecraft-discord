package com.mojang.minecraft.character;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public class Cube {
	private Vertex[] vertices;
	private Polygon[] polygons;
    private int xTexOffs;
    private int yTexOffs;
    public float x;
    public float y;
    public float z;
	public float xRot;
	public float yRot;
	public float zRot;
	private boolean compiled = false;
	private int list = 0;

    public Cube(int xTexOffs, int yTexOffs) {
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }

    public void setTexOffs(int xTexOffs, int yTexOffs) {
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }

    public void addBox(float x0, float y0, float z0, int w, int h, int d) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
        float x1 = x0 + (float)w;
        float y1 = y0 + (float)h;
        float z1 = z0 + (float)d;
        Vertex u0 = new Vertex(x0, y0, z0, 0.0F, 0.0F);
        Vertex u1 = new Vertex(x1, y0, z0, 0.0F, 8.0F);
        Vertex u2 = new Vertex(x1, y1, z0, 8.0F, 8.0F);
        Vertex u3 = new Vertex(x0, y1, z0, 8.0F, 0.0F);
        Vertex l0 = new Vertex(x0, y0, z1, 0.0F, 0.0F);
        Vertex l1 = new Vertex(x1, y0, z1, 0.0F, 8.0F);
        Vertex l2 = new Vertex(x1, y1, z1, 8.0F, 8.0F);
        Vertex l3 = new Vertex(x0, y1, z1, 8.0F, 0.0F);
        this.vertices[0] = u0;
        this.vertices[1] = u1;
        this.vertices[2] = u2;
        this.vertices[3] = u3;
        this.vertices[4] = l0;
        this.vertices[5] = l1;
        this.vertices[6] = l2;
        this.vertices[7] = l3;
        this.polygons[0] = new Polygon(new Vertex[]{l1, u1, u2, l2}, this.xTexOffs + d + w, this.yTexOffs + d, this.xTexOffs + d + w + d, this.yTexOffs + d + h);
        this.polygons[1] = new Polygon(new Vertex[]{u0, l0, l3, u3}, this.xTexOffs + 0, this.yTexOffs + d, this.xTexOffs + d, this.yTexOffs + d + h);
        this.polygons[2] = new Polygon(new Vertex[]{l1, l0, u0, u1}, this.xTexOffs + d, this.yTexOffs + 0, this.xTexOffs + d + w, this.yTexOffs + d);
        this.polygons[3] = new Polygon(new Vertex[]{u2, u3, l3, l2}, this.xTexOffs + d + w, this.yTexOffs + 0, this.xTexOffs + d + w + w, this.yTexOffs + d);
        this.polygons[4] = new Polygon(new Vertex[]{u1, u0, u3, u2}, this.xTexOffs + d, this.yTexOffs + d, this.xTexOffs + d + w, this.yTexOffs + d + h);
        this.polygons[5] = new Polygon(new Vertex[]{l0, l1, l2, l3}, this.xTexOffs + d + w + d, this.yTexOffs + d, this.xTexOffs + d + w + d + w, this.yTexOffs + d + h);
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = 0.0F;
    }

    public final void render(float f1) {
        if(!this.compiled) {
            float f3 = f1;
            Cube cube2 = this;
            this.list = GL11.glGenLists(1);
            GL11.glNewList(this.list, GL11.GL_COMPILE);
            GL11.glBegin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            for(int i4 = 0; i4 < cube2.polygons.length; ++i4) {
                Polygon polygon10000 = cube2.polygons[i4];
                float f6 = f3;
                Polygon polygon5 = polygon10000;
                Vec3 vec37 = polygon10000.vertices[1].pos.subtract(polygon5.vertices[0].pos).normalize();
                Vec3 vec38 = polygon5.vertices[1].pos.subtract(polygon5.vertices[2].pos).normalize();
                GL11.glNormal3f((vec37 = (new Vec3(vec37.y * vec38.z - vec37.z * vec38.y, vec37.z * vec38.x - vec37.x * vec38.z, vec37.x * vec38.y - vec37.y * vec38.x)).normalize()).x, vec37.y, vec37.z);

                for(int i10 = 0; i10 < 4; ++i10) {
                    Vertex vertex11;
                    GL11.glTexCoord2f((vertex11 = polygon5.vertices[i10]).u / 64.0F, vertex11.v / 32.0F);
                    GL11.glVertex3f(vertex11.pos.x * f6, vertex11.pos.y * f6, vertex11.pos.z * f6);
                }
            }

            GL11.glEnd();
            GL11.glEndList();
            cube2.compiled = true;
        }

        float f9 = 57.29578F;
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x * f1, this.y * f1, this.z * f1);
        GL11.glRotatef(this.zRot * f9, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(this.yRot * f9, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.xRot * f9, 1.0F, 0.0F, 0.0F);
        GL11.glCallList(this.list);
        GL11.glPopMatrix();
    }
}
