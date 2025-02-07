package com.mojang.minecraft.character;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class Zombie extends Entity {
	public static final long serialVersionUID = 77479605454997290L;
	private static ZombieModel zombieModel = new ZombieModel();
	public float rot;
	public float timeOffs;
	public float speed;
	public float rotA = (float)(Math.random() + 1.0D) * 0.01F;

	public Zombie(Level level1, float f2, float f3, float f4) {
		super(level1);
		this.setPos(f2, f3, f4);
		this.timeOffs = (float)Math.random() * 1239813.0F;
		this.rot = (float)(Math.random() * Math.PI * 2.0D);
		this.speed = 1.0F;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		float f1 = 0.0F;
		float f2 = 0.0F;
		if(this.y < -100.0F) {
			this.remove();
		}

		this.rot += this.rotA;
		this.rotA = (float)((double)this.rotA * 0.99D);
		this.rotA = (float)((double)this.rotA + (Math.random() - Math.random()) * Math.random() * Math.random() * (double)0.08F);
		f1 = (float)Math.sin((double)this.rot);
		f2 = (float)Math.cos((double)this.rot);
		if(this.onGround && Math.random() < 0.08D) {
			this.yd = 0.5F;
		}

		this.moveRelative(f1, f2, this.onGround ? 0.1F : 0.02F);
		this.yd = (float)((double)this.yd - 0.08D);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.91F;
		this.yd *= 0.98F;
		this.zd *= 0.91F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
		}

	}

	public void render(Textures textures1, float f2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures1.getTextureId("/char.png"));
		GL11.glPushMatrix();
		double d3 = (double)EagRuntime.nanoTime() / 1.0E9D * 10.0D * (double)this.speed + (double)this.timeOffs;
        float f6;
        GL11.glColor3f(f6 = this.getBrightness(), f6, f6);
        f6 = 0.058333334F;
        float f5 = (float)(-Math.abs(Math.sin(d3 * 0.6662D)) * 5.0D - 23.0D);
        GL11.glTranslatef(this.xo + (this.x - this.xo) * f2, this.yo + (this.y - this.yo) * f2, this.zo + (this.z - this.zo) * f2);
        GL11.glScalef(1.0F, -1.0F, 1.0F);
        GL11.glScalef(f6, f6, f6);
        GL11.glTranslatef(0.0F, f5, 0.0F);
        f6 = 57.29578F;
        GL11.glRotatef(this.rot * f6 + 180.0F, 0.0F, 1.0F, 0.0F);
        zombieModel.render((float)d3, 1.0F, 0.0F, 0.0F, 0.0F);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
}
