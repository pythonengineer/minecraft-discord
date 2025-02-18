package com.mojang.minecraft.particle;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;

public class Particle extends Entity {
	private float xd;
	private float yd;
	private float zd;
	public int tex;
	private float uo;
	private float vo;
	private int age = 0;
	private int lifetime = 0;
	private float size;
	private float gravity;

	public Particle(Level level1, float f2, float f3, float f4, float f5, float f6, float f7, Tile tile8) {
		super(level1);
		this.tex = tile8.tex;
		this.gravity = tile8.particleGravity;
		this.setSize(0.2F, 0.2F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(f2, f3, f4);
		this.xd = f5 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.yd = f6 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.zd = f7 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		float f9 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		f2 = (float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd));
		this.xd = this.xd / f2 * f9 * 0.4F;
		this.yd = this.yd / f2 * f9 * 0.4F + 0.1F;
		this.zd = this.zd / f2 * f9 * 0.4F;
		this.uo = (float)Math.random() * 3.0F;
		this.vo = (float)Math.random() * 3.0F;
		this.size = (float)(Math.random() * 0.5D + 0.5D);
		this.lifetime = (int)(4.0D / (Math.random() * 0.9D + 0.1D));
		this.age = 0;
        this.makeStepSound = false;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd = (float)((double)this.yd - 0.04D * (double)this.gravity);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
		}

	}

	public void render(Tesselator tesselator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8;
		float f9 = (f8 = ((float)(this.tex % 16) + this.uo / 4.0F) / 16.0F) + 0.015609375F;
		float f10;
		float f11 = (f10 = ((float)(this.tex / 16) + this.vo / 4.0F) / 16.0F) + 0.015609375F;
		float f12 = 0.1F * this.size;
		float f13 = this.xo + (this.x - this.xo) * f2;
		float f14 = this.yo + (this.y - this.yo) * f2;
		float f15 = this.zo + (this.z - this.zo) * f2;
		tesselator1.vertexUV(f13 - f3 * f12 - f6 * f12, f14 - f4 * f12, f15 - f5 * f12 - f7 * f12, f8, f11);
		tesselator1.vertexUV(f13 - f3 * f12 + f6 * f12, f14 + f4 * f12, f15 - f5 * f12 + f7 * f12, f8, f10);
		tesselator1.vertexUV(f13 + f3 * f12 + f6 * f12, f14 + f4 * f12, f15 + f5 * f12 + f7 * f12, f9, f10);
		tesselator1.vertexUV(f13 + f3 * f12 - f6 * f12, f14 - f4 * f12, f15 + f5 * f12 - f7 * f12, f9, f11);
	}
}
