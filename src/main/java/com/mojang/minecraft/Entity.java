package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.net.PlayerMove;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Textures;

import java.io.Serializable;
import java.util.ArrayList;

public class Entity implements Serializable {
	public static final long serialVersionUID = 0L;
	protected Level level;
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
	protected float bbWidth = 0.6F;
	public float bbHeight = 1.8F;

	public Entity(Level level1) {
		this.level = level1;
		this.setPos(0.0F, 0.0F, 0.0F);
	}

	public void resetPos() {
		if(this.level != null) {
			float f1 = (float)this.level.xSpawn + 0.5F;
			float f2 = (float)this.level.ySpawn;

			for(float f3 = (float)this.level.zSpawn + 0.5F; f2 > 0.0F; ++f2) {
				this.setPos(f1, f2, f3);
				if(this.level.getCubes(this.bb).size() == 0) {
					break;
				}
			}

			this.xd = this.yd = this.zd = 0.0F;
			this.yRot = this.level.rotSpawn;
			this.xRot = 0.0F;
		}
	}

	public void remove() {
		this.removed = true;
	}

	public void setSize(float f1, float f2) {
		this.bbWidth = f1;
		this.bbHeight = f2;
	}

	public void setPos(PlayerMove playerMove1) {
		if(playerMove1.moving) {
			this.setPos(playerMove1.x, playerMove1.y, playerMove1.z);
		} else {
			this.setPos(this.x, this.y, this.z);
		}

		if(playerMove1.rotating) {
			this.setRot(playerMove1.yRot, playerMove1.xRot);
		} else {
			this.setRot(this.yRot, this.xRot);
		}
	}

	protected void setRot(float f1, float f2) {
		this.yRot = f1;
		this.xRot = f2;
	}

	public void setPos(float f1, float f2, float f3) {
		this.x = f1;
		this.y = f2;
		this.z = f3;
		float f4 = this.bbWidth / 2.0F;
		float f5 = this.bbHeight / 2.0F;
		this.bb = new AABB(f1 - f4, f2 - f5, f3 - f4, f1 + f4, f2 + f5, f3 + f4);
	}

	public void turn(float f1, float f2) {
		float f3 = this.xRot;
		float f4 = this.yRot;
		this.yRot = (float)((double)this.yRot + (double)f1 * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)f2 * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

		this.xRotO += this.xRot - f3;
		this.yRotO += this.yRot - f4;
	}

	public void interpolateTurn(float f1, float f2) {
		this.yRot = (float)((double)this.yRot + (double)f1 * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)f2 * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
	}

	public boolean isFree(float f1, float f2, float f3) {
		AABB aABB4 = this.bb.cloneMove(f1, f2, f3);
		return this.level.getCubes(aABB4).size() > 0 ? false : !this.level.containsAnyLiquid(aABB4);
	}

	public void move(float f1, float f2, float f3) {
		float f4 = f1;
		float f5 = f2;
		float f6 = f3;
		ArrayList arrayList7 = this.level.getCubes(this.bb.expand(f1, f2, f3));

		int i8;
		for(i8 = 0; i8 < arrayList7.size(); ++i8) {
			f2 = ((AABB)arrayList7.get(i8)).clipYCollide(this.bb, f2);
		}

		this.bb.move(0.0F, f2, 0.0F);

		for(i8 = 0; i8 < arrayList7.size(); ++i8) {
			f1 = ((AABB)arrayList7.get(i8)).clipXCollide(this.bb, f1);
		}

		this.bb.move(f1, 0.0F, 0.0F);

		for(i8 = 0; i8 < arrayList7.size(); ++i8) {
			f3 = ((AABB)arrayList7.get(i8)).clipZCollide(this.bb, f3);
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

	public boolean isInWater() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.water);
	}

	public boolean isInLava() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.lava);
	}

	public void moveRelative(float f1, float f2, float f3) {
		float f4;
		if((f4 = (float)Math.sqrt((double)(f1 * f1 + f2 * f2))) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			f3 = (float)Math.sin((double)this.yRot * Math.PI / 180.0D);
			f4 = (float)Math.cos((double)this.yRot * Math.PI / 180.0D);
			this.xd += f1 * f4 - f2 * f3;
			this.zd += f2 * f4 + f1 * f3;
		}
	}

	public boolean isLit() {
		int i1 = (int)this.x;
		int i2 = (int)this.y;
		int i3 = (int)this.z;
		return this.level.isLit(i1, i2, i3);
	}

	public float getBrightness() {
		int i1 = (int)this.x;
		int i2 = (int)(this.y + this.heightOffset / 2.0F);
		int i3 = (int)this.z;
		return this.level.getBrightness(i1, i2, i3);
	}

	public void render(Textures textures1, float f2) {
	}

	public void setLevel(Level level1) {
		this.level = level1;
	}

	public void moveTo(float f1, float f2, float f3, float f4, float f5) {
		this.xo = this.x = f1;
		this.yo = this.y = f2;
		this.zo = this.z = f3;
		this.xRot = f4;
		this.yRot = f5;
		this.setPos(f1, f2, f3);
	}
}
