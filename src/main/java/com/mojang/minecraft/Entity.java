package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;

import java.util.ArrayList;

public class Entity {
	private Level level;
	public float xo;
	public float yo;
	public float zo;
	public float x;
	public float y;
	public float z;
	public float xd;
	public float yd;
	public float zd;
	public float yRot;
	public float xRot;
    public float yRotO;
    public float xRotO;
	public AABB bb;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	public boolean removed = false;
	public float heightOffset = 0.0F;
	private float bbWidth = 0.6F;
	public float bbHeight = 1.8F;

	public Entity(Level level1) {
		this.level = level1;
		this.resetPos();
	}

	public final void resetPos() {
		float f1 = (float)Math.random() * (float)(this.level.width - 2) + 1.0F;
		float f2 = (float)(this.level.depth + 10);
		float f3 = (float)Math.random() * (float)(this.level.height - 2) + 1.0F;
		this.setPos(f1, f2, f3);
	}

	public final void setSize(float f1, float f2) {
		this.bbWidth = f1;
		this.bbHeight = f2;
	}

	public final void setPos(float f1, float f2, float f3) {
		this.x = f1;
		this.y = f2;
		this.z = f3;
		float f4 = this.bbWidth / 2.0F;
		float f5 = this.bbHeight / 2.0F;
		this.bb = new AABB(f1 - f4, f2 - f5, f3 - f4, f1 + f4, f2 + f5, f3 + f4);
	}

    public void turn(float xo, float yo) {
        float orgXRot = this.xRot;
        float orgYRot = this.yRot;
        this.yRot = (float)((double)this.yRot + (double)xo * 0.15D);
        this.xRot = (float)((double)this.xRot - (double)yo * 0.15D);
        if(this.xRot < -90.0F) {
            this.xRot = -90.0F;
        }

        if(this.xRot > 90.0F) {
            this.xRot = 90.0F;
        }

        this.xRotO += this.xRot - orgXRot;
        this.yRotO += this.yRot - orgYRot;
    }

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
	}

	public final boolean isFree(float f1, float f2, float f3) {
		AABB aABB11 = this.bb;
		aABB11 = new AABB(aABB11.x0 + f3, aABB11.y0 + f2, aABB11.z0 + f3, aABB11.x1 + f1, aABB11.y1 + f2, aABB11.z1 + f3);
		if(this.level.getCubes(aABB11).size() > 0) {
			return false;
		} else {
			Level level12 = this.level;
			int i15 = (int)Math.floor((double)aABB11.x0);
			int i16 = (int)Math.floor((double)(aABB11.x1 + 1.0F));
			int i10 = (int)Math.floor((double)aABB11.y0);
			int i5 = (int)Math.floor((double)(aABB11.y1 + 1.0F));
			int i6 = (int)Math.floor((double)aABB11.z0);
			int i14 = (int)Math.floor((double)(aABB11.z1 + 1.0F));
			if(i15 < 0) {
				i15 = 0;
			}

			if(i10 < 0) {
				i10 = 0;
			}

			if(i6 < 0) {
				i6 = 0;
			}

			if(i16 > level12.width) {
				i16 = level12.width;
			}

			if(i5 > level12.depth) {
				i5 = level12.depth;
			}

			if(i14 > level12.height) {
				i14 = level12.height;
			}

			boolean z10000;
			for(i15 = i15; i15 < i16; ++i15) {
				for(int i7 = i10; i7 < i5; ++i7) {
					for(int i8 = i6; i8 < i14; ++i8) {
						Tile tile9;
						if((tile9 = Tile.tiles[level12.getTile(i15, i7, i8)]) != null && tile9.getLiquidType() > 0) {
							z10000 = true;
							return !z10000;
						}
					}
				}
			}

			z10000 = false;
			return !z10000;
		}
	}

	public final void move(float f1, float f2, float f3) {
		float f4 = f1;
		float f5 = f2;
		float f6 = f3;
		Level level10000 = this.level;
		AABB aABB9 = this.bb;
		float f7 = this.bb.x0;
		float f8 = aABB9.y0;
		float f13 = aABB9.z0;
		float f14 = aABB9.x1;
		float f15 = aABB9.y1;
		float f18 = aABB9.z1;
		if(f1 < 0.0F) {
			f7 += f1;
		}

		if(f1 > 0.0F) {
			f14 += f1;
		}

		if(f2 < 0.0F) {
			f8 += f2;
		}

		if(f2 > 0.0F) {
			f15 += f2;
		}

		if(f3 < 0.0F) {
			f13 += f3;
		}

		if(f3 > 0.0F) {
			f18 += f3;
		}

		ArrayList arrayList16 = level10000.getCubes(new AABB(f7, f8, f13, f14, f15, f18));

		AABB aABB10;
		float f11;
		float f12;
		int i17;
		AABB aABB19;
		float f20;
		for(i17 = 0; i17 < arrayList16.size(); ++i17) {
			aABB19 = (AABB)arrayList16.get(i17);
			f11 = f2;
			aABB10 = this.bb;
			aABB9 = aABB19;
			if(aABB10.x1 > aABB9.x0 && aABB10.x0 < aABB9.x1) {
				if(aABB10.z1 > aABB9.z0 && aABB10.z0 < aABB9.z1) {
					if(f2 > 0.0F && aABB10.y1 <= aABB9.y0 && (f12 = aABB9.y0 - aABB10.y1) < f2) {
						f11 = f12;
					}

					if(f11 < 0.0F && aABB10.y0 >= aABB9.y1 && (f12 = aABB9.y1 - aABB10.y0) > f11) {
						f11 = f12;
					}

					f20 = f11;
				} else {
					f20 = f2;
				}
			} else {
				f20 = f2;
			}

			f2 = f20;
		}

		this.bb.move(0.0F, f2, 0.0F);

		for(i17 = 0; i17 < arrayList16.size(); ++i17) {
			aABB19 = (AABB)arrayList16.get(i17);
			f11 = f1;
			aABB10 = this.bb;
			aABB9 = aABB19;
			if(aABB10.y1 > aABB9.y0 && aABB10.y0 < aABB9.y1) {
				if(aABB10.z1 > aABB9.z0 && aABB10.z0 < aABB9.z1) {
					if(f1 > 0.0F && aABB10.x1 <= aABB9.x0 && (f12 = aABB9.x0 - aABB10.x1) < f1) {
						f11 = f12;
					}

					if(f11 < 0.0F && aABB10.x0 >= aABB9.x1 && (f12 = aABB9.x1 - aABB10.x0) > f11) {
						f11 = f12;
					}

					f20 = f11;
				} else {
					f20 = f1;
				}
			} else {
				f20 = f1;
			}

			f1 = f20;
		}

		this.bb.move(f1, 0.0F, 0.0F);

		for(i17 = 0; i17 < arrayList16.size(); ++i17) {
			aABB19 = (AABB)arrayList16.get(i17);
			f11 = f3;
			aABB10 = this.bb;
			aABB9 = aABB19;
			if(aABB10.x1 > aABB9.x0 && aABB10.x0 < aABB9.x1) {
				if(aABB10.y1 > aABB9.y0 && aABB10.y0 < aABB9.y1) {
					if(f3 > 0.0F && aABB10.z1 <= aABB9.z0 && (f12 = aABB9.z0 - aABB10.z1) < f3) {
						f11 = f12;
					}

					if(f11 < 0.0F && aABB10.z0 >= aABB9.z1 && (f12 = aABB9.z1 - aABB10.z0) > f11) {
						f11 = f12;
					}

					f20 = f11;
				} else {
					f20 = f3;
				}
			} else {
				f20 = f3;
			}

			f3 = f20;
		}

		this.bb.move(0.0F, 0.0F, f3);
		this.horizontalCollision = f4 != f1 || f6 != f3;
		this.onGround = f5 != f2 && f5 < 0.0F;
		if(f4 != f1) {
			this.xd = 0.0F;
		}

		if(f5 != f2) {
			this.yd = 0.0F;
		}

		if(f6 != f3) {
			this.zd = 0.0F;
		}

		this.x = (this.bb.x0 + this.bb.x1) / 2.0F;
		this.y = this.bb.y0 + this.heightOffset;
		this.z = (this.bb.z0 + this.bb.z1) / 2.0F;
	}

	public final boolean isInWater() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), 1);
	}

	public final boolean isInLava() {
		return this.level.containsLiquid(this.bb, 2);
	}

	public final void moveRelative(float f1, float f2, float f3) {
		float f4;
		if((f4 = f1 * f1 + f2 * f2) >= 0.01F) {
			f4 = f3 / (float)Math.sqrt((double)f4);
			f1 *= f4;
			f2 *= f4;
			f3 = (float)Math.sin((double)this.yRot * Math.PI / 180.0D);
			f4 = (float)Math.cos((double)this.yRot * Math.PI / 180.0D);
			this.xd += f1 * f4 - f2 * f3;
			this.zd += f2 * f4 + f1 * f3;
		}
	}

	public final boolean isLit() {
		int i1 = (int)this.x;
		int i2 = (int)this.y;
		int i3 = (int)this.z;
		return this.level.isLit(i1, i2, i3);
	}

	public void render(float f1) {
	}
}
