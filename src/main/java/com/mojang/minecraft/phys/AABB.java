package com.mojang.minecraft.phys;

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

    public AABB(float f1, float f2, float f3, float f4, float f5, float f6) {
        this.x0 = f1;
        this.y0 = f2;
        this.z0 = f3;
        this.x1 = f4;
        this.y1 = f5;
        this.z1 = f6;
    }

    public AABB expand(float f1, float f2, float f3) {
        float f4 = this.x0;
        float f5 = this.y0;
        float f6 = this.z0;
        float f7 = this.x1;
        float f8 = this.y1;
        float f9 = this.z1;
        if(f1 < 0.0F) {
            f4 += f1;
        }

        if(f1 > 0.0F) {
            f7 += f1;
        }

        if(f2 < 0.0F) {
            f5 += f2;
        }

        if(f2 > 0.0F) {
            f8 += f2;
        }

        if(f3 < 0.0F) {
            f6 += f3;
        }

        if(f3 > 0.0F) {
            f9 += f3;
        }

        return new AABB(f4, f5, f6, f7, f8, f9);
    }

    public AABB grow(float f1, float f2, float f3) {
        float f4 = this.x0 - f1;
        float f5 = this.y0 - f2;
        float f6 = this.z0 - f3;
        f1 += this.x1;
        f2 += this.y1;
        float f7 = this.z1 + f3;
        return new AABB(f4, f5, f6, f1, f2, f7);
    }

    public AABB cloneMove(float f1, float f2, float f3) {
        return new AABB(this.x0 + f3, this.y0 + f2, this.z0 + f3, this.x1 + f1, this.y1 + f2, this.z1 + f3);
    }

    public float clipXCollide(AABB aABB1, float f2) {
        if(aABB1.y1 > this.y0 && aABB1.y0 < this.y1) {
            if(aABB1.z1 > this.z0 && aABB1.z0 < this.z1) {
                float f3;
                if(f2 > 0.0F && aABB1.x1 <= this.x0 && (f3 = this.x0 - aABB1.x1 - this.epsilon) < f2) {
                    f2 = f3;
                }

                if(f2 < 0.0F && aABB1.x0 >= this.x1 && (f3 = this.x1 - aABB1.x0 + this.epsilon) > f2) {
                    f2 = f3;
                }

                return f2;
            } else {
                return f2;
            }
        } else {
            return f2;
        }
    }

    public float clipYCollide(AABB aABB1, float f2) {
        if(aABB1.x1 > this.x0 && aABB1.x0 < this.x1) {
            if(aABB1.z1 > this.z0 && aABB1.z0 < this.z1) {
                float f3;
                if(f2 > 0.0F && aABB1.y1 <= this.y0 && (f3 = this.y0 - aABB1.y1 - this.epsilon) < f2) {
                    f2 = f3;
                }

                if(f2 < 0.0F && aABB1.y0 >= this.y1 && (f3 = this.y1 - aABB1.y0 + this.epsilon) > f2) {
                    f2 = f3;
                }

                return f2;
            } else {
                return f2;
            }
        } else {
            return f2;
        }
    }

    public float clipZCollide(AABB aABB1, float f2) {
        if(aABB1.x1 > this.x0 && aABB1.x0 < this.x1) {
            if(aABB1.y1 > this.y0 && aABB1.y0 < this.y1) {
                float f3;
                if(f2 > 0.0F && aABB1.z1 <= this.z0 && (f3 = this.z0 - aABB1.z1 - this.epsilon) < f2) {
                    f2 = f3;
                }

                if(f2 < 0.0F && aABB1.z0 >= this.z1 && (f3 = this.z1 - aABB1.z0 + this.epsilon) > f2) {
                    f2 = f3;
                }

                return f2;
            } else {
                return f2;
            }
        } else {
            return f2;
        }
    }

    public boolean intersects(AABB aABB1) {
        return aABB1.x1 > this.x0 && aABB1.x0 < this.x1 ? (aABB1.y1 > this.y0 && aABB1.y0 < this.y1 ? aABB1.z1 > this.z0 && aABB1.z0 < this.z1 : false) : false;
    }

    public void move(float f1, float f2, float f3) {
        this.x0 += f1;
        this.y0 += f2;
        this.z0 += f3;
        this.x1 += f1;
        this.y1 += f2;
        this.z1 += f3;
    }
}
