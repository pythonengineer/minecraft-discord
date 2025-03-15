package com.mojang.minecraft.phys;

import com.mojang.minecraft.character.Vec3;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.io.Serializable;

public class AABB implements Serializable {
    public static final long serialVersionUID = 0L;
    private float epsilon = 0.0F;
    public float x0;
    public float y0;
    public float z0;
    public float x1;
    public float y1;
    public float z1;

    public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }

    public AABB expand(float xa, float ya, float za) {
        float f4 = this.x0;
        float f5 = this.y0;
        float f6 = this.z0;
        float f7 = this.x1;
        float f8 = this.y1;
        float f9 = this.z1;
        if(xa < 0.0F) {
            f4 += xa;
        }

        if(xa > 0.0F) {
            f7 += xa;
        }

        if(ya < 0.0F) {
            f5 += ya;
        }

        if(ya > 0.0F) {
            f8 += ya;
        }

        if(za < 0.0F) {
            f6 += za;
        }

        if(za > 0.0F) {
            f9 += za;
        }

        return new AABB(f4, f5, f6, f7, f8, f9);
    }

    public AABB grow(float xa, float ya, float za) {
        float f4 = this.x0 - xa;
        float f5 = this.y0 - ya;
        float f6 = this.z0 - za;
        xa += this.x1;
        ya += this.y1;
        float f7 = this.z1 + za;
        return new AABB(f4, f5, f6, xa, ya, f7);
    }

    public AABB cloneMove(float xa, float ya, float za) {
        return new AABB(this.x0 + za, this.y0 + ya, this.z0 + za, this.x1 + xa, this.y1 + ya, this.z1 + za);
    }

    public float clipXCollide(AABB c, float xa) {
        if(c.y1 > this.y0 && c.y0 < this.y1) {
            if(c.z1 > this.z0 && c.z0 < this.z1) {
                float f3;
                if(xa > 0.0F && c.x1 <= this.x0 && (f3 = this.x0 - c.x1 - this.epsilon) < xa) {
                    xa = f3;
                }

                if(xa < 0.0F && c.x0 >= this.x1 && (f3 = this.x1 - c.x0 + this.epsilon) > xa) {
                    xa = f3;
                }

                return xa;
            } else {
                return xa;
            }
        } else {
            return xa;
        }
    }

    public float clipYCollide(AABB c, float ya) {
        if(c.x1 > this.x0 && c.x0 < this.x1) {
            if(c.z1 > this.z0 && c.z0 < this.z1) {
                float f3;
                if(ya > 0.0F && c.y1 <= this.y0 && (f3 = this.y0 - c.y1 - this.epsilon) < ya) {
                    ya = f3;
                }

                if(ya < 0.0F && c.y0 >= this.y1 && (f3 = this.y1 - c.y0 + this.epsilon) > ya) {
                    ya = f3;
                }

                return ya;
            } else {
                return ya;
            }
        } else {
            return ya;
        }
    }

    public float clipZCollide(AABB c, float za) {
        if(c.x1 > this.x0 && c.x0 < this.x1) {
            if(c.y1 > this.y0 && c.y0 < this.y1) {
                float f3;
                if(za > 0.0F && c.z1 <= this.z0 && (f3 = this.z0 - c.z1 - this.epsilon) < za) {
                    za = f3;
                }

                if(za < 0.0F && c.z0 >= this.z1 && (f3 = this.z1 - c.z0 + this.epsilon) > za) {
                    za = f3;
                }

                return za;
            } else {
                return za;
            }
        } else {
            return za;
        }
    }

    public boolean intersects(AABB c) {
        return c.x1 > this.x0 && c.x0 < this.x1 ? (c.y1 > this.y0 && c.y0 < this.y1 ? c.z1 > this.z0 && c.z0 < this.z1 : false) : false;
    }

    public void move(float xa, float ya, float za) {
        this.x0 += xa;
        this.y0 += ya;
        this.z0 += za;
        this.x1 += xa;
        this.y1 += ya;
        this.z1 += za;
    }

    public boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x2 > this.x0 && x1 < this.x1 ? (y2 > this.y0 && y1 < this.y1 ? z2 > this.z0 && z1 < this.z1 : false) : false;
    }

    public boolean contains(Vec3 t) {
        return t.x > this.x0 && t.x < this.x1 ? (t.y > this.y0 && t.y < this.y1 ? t.z > this.z0 && t.z < this.z1 : false) : false;
    }

    public void render() {
        GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        GL11.glVertex3f(this.x0, this.y0, this.z0);
        GL11.glVertex3f(this.x1, this.y0, this.z0);
        GL11.glVertex3f(this.x1, this.y0, this.z1);
        GL11.glVertex3f(this.x0, this.y0, this.z1);
        GL11.glVertex3f(this.x0, this.y0, this.z0);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        GL11.glVertex3f(this.x0, this.y1, this.z0);
        GL11.glVertex3f(this.x1, this.y1, this.z0);
        GL11.glVertex3f(this.x1, this.y1, this.z1);
        GL11.glVertex3f(this.x0, this.y1, this.z1);
        GL11.glVertex3f(this.x0, this.y1, this.z0);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        GL11.glVertex3f(this.x0, this.y0, this.z0);
        GL11.glVertex3f(this.x0, this.y1, this.z0);
        GL11.glVertex3f(this.x1, this.y0, this.z0);
        GL11.glVertex3f(this.x1, this.y1, this.z0);
        GL11.glVertex3f(this.x1, this.y0, this.z1);
        GL11.glVertex3f(this.x1, this.y1, this.z1);
        GL11.glVertex3f(this.x0, this.y0, this.z1);
        GL11.glVertex3f(this.x0, this.y1, this.z1);
        GL11.glEnd();
    }
}