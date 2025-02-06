package com.mojang.minecraft.net;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.character.ZombieModel;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.util.LinkedList;
import java.util.List;

public class NetworkPlayer extends Entity {
	public static final long serialVersionUID = 77479605454997290L;
	private ZombieModel zombieModel;
	private float animStep;
	private float animStepO;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private float yBodyRot = 0.0F;
	private float yBodyRotO = 0.0F;
	private float oRun;
	private float run;
	private transient int skin = -1;
	public transient ImageData newTexture = null;
	public String name;
	private Textures textures;

	public NetworkPlayer(Minecraft minecraft1, int i2, String string3, int i4, int i5, int i6, float f7, float f8) {
		super(minecraft1.level);
		this.minecraft = minecraft1;
		this.zombieModel = minecraft1.playerModel;
		this.name = string3;
		this.xp = i4;
		this.yp = i5;
		this.zp = i6;
		this.setPos((float)i4 / 32.0F, (float)i5 / 32.0F, (float)i6 / 32.0F);
		this.xRot = f8;
		this.yRot = f7;
		this.heightOffset = 1.62F;
	}

	public void tick() {
		super.tick();
		this.animStepO = this.animStep;
		int i1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((PlayerMove)this.moveQueue.remove(0));
			}
		} while(i1-- > 0 && this.moveQueue.size() > 10);

		float f7 = this.x - this.xo;
		float f2 = this.z - this.zo;
		this.yBodyRotO = this.yBodyRot;
		float f3 = (float)Math.sqrt((double)(f7 * f7 + f2 * f2));
		float f4 = this.yBodyRot;
		float f5 = 0.0F;
		this.oRun = this.run;
		float f6 = 0.0F;
		if(f3 == 0.0F) {
			this.animStep = 0.0F;
		} else {
			f6 = 1.0F;
			f5 = f3 * 3.0F;
			f4 = -((float)Math.atan2((double)f2, (double)f7) * 180.0F / (float)Math.PI + 90.0F);
		}

		this.run += (f6 - this.run) * 0.1F;

		for(f7 = f4 - this.yBodyRot; f7 < -180.0F; f7 += 360.0F) {
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		this.yBodyRot += f7 * 0.1F;

		for(f7 = this.yRot - this.yBodyRot; f7 < -180.0F; f7 += 360.0F) {
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		this.yBodyRot += f7 * 0.1F;

		for(f7 = this.yRot - this.yBodyRot; f7 < -180.0F; f7 += 360.0F) {
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		boolean z8 = f7 < -90.0F || f7 >= 90.0F;
		if(f7 < -75.0F) {
			f7 = -75.0F;
		}

		if(f7 >= 75.0F) {
			f7 = 75.0F;
		}

		this.yBodyRot = this.yRot - f7;
		if(z8) {
			f5 = -f5;
		}

		while(this.yRot - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while(this.yRot - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO < -180.0F) {
			this.yBodyRotO -= 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
			this.yBodyRotO += 360.0F;
		}

		while(this.xRot - this.xRotO < -180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.xRot - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		this.animStep += f5;
	}

	public void render(Textures textures1, float f2) {
		this.textures = textures1;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(this.newTexture != null) {
			this.skin = textures1.addTexture(this.newTexture);
			this.newTexture = null;
		}

		if(this.skin < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures1.getTextureId("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.skin);
		}

		float f8 = this.yBodyRotO + (this.yBodyRot - this.yBodyRotO) * f2;
		float f3 = this.yRotO + (this.yRot - this.yRotO) * f2;
		float f4 = this.xRotO + (this.xRot - this.xRotO) * f2;
		f3 -= f8;
		GL11.glPushMatrix();
		float f5 = this.animStepO + (this.animStep - this.animStepO) * f2;
		float f6;
		GL11.glColor3f(f6 = this.getBrightness(), f6, f6);
		f6 = 0.0625F;
		float f7 = (float)(-Math.abs(Math.sin((double)f5 * 0.6662D)) * 5.0D - 23.0D);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * f2, this.yo + (this.y - this.yo) * f2 - this.heightOffset, this.zo + (this.z - this.zo) * f2);
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		GL11.glScalef(f6, f6, f6);
		GL11.glTranslatef(0.0F, f7, 0.0F);
		GL11.glRotatef(f8, 0.0F, 1.0F, 0.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.zombieModel.render(f5, f3, f4);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Font font9 = this.minecraft.font;
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xo + (this.x - this.xo) * f2, this.yo + (this.y - this.yo) * f2 + 0.8F, this.zo + (this.z - this.zo) * f2);
		GL11.glRotatef(-this.minecraft.player.yRot, 0.0F, 1.0F, 0.0F);
		f2 = 0.05F;
		GL11.glScalef(0.05F, -f2, f2);
		GL11.glTranslatef((float)(-font9.width(this.name)) / 2.0F, 0.0F, 0.0F);
		if(this.name.equalsIgnoreCase("Notch")) {
			font9.draw(this.name, 0, 0, 16776960);
		} else {
			font9.draw(this.name, 0, 0, 0xFFFFFF);
		}

		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		font9.draw(this.name, 0, 0, 5263440);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void queue(byte b1, byte b2, byte b3, float f4, float f5) {
		this.moveQueue.add(new PlayerMove(((float)this.xp + (float)b1 / 2.0F) / 32.0F, ((float)this.yp + (float)b2 / 2.0F) / 32.0F, ((float)this.zp + (float)b3 / 2.0F) / 32.0F, (this.yRot + f4) / 2.0F, (this.xRot + f5) / 2.0F));
		this.xp += b1;
		this.yp += b2;
		this.zp += b3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, f4, f5));
	}

	public void teleport(short s1, short s2, short s3, float f4, float f5) {
		this.moveQueue.add(new PlayerMove((float)(this.xp + s1) / 64.0F, (float)(this.yp + s2) / 64.0F, (float)(this.zp + s3) / 64.0F, (this.yRot + f4) / 2.0F, (this.xRot + f5) / 2.0F));
		this.xp = s1;
		this.yp = s2;
		this.zp = s3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, f4, f5));
	}

	public void queue(byte b1, byte b2, byte b3) {
		this.moveQueue.add(new PlayerMove(((float)this.xp + (float)b1 / 2.0F) / 32.0F, ((float)this.yp + (float)b2 / 2.0F) / 32.0F, ((float)this.zp + (float)b3 / 2.0F) / 32.0F));
		this.xp += b1;
		this.yp += b2;
		this.zp += b3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float f1, float f2) {
		this.moveQueue.add(new PlayerMove((this.yRot + f1) / 2.0F, (this.xRot + f2) / 2.0F));
		this.moveQueue.add(new PlayerMove(f1, f2));
	}

	public void clear() {
		if(this.skin >= 0) {
			System.out.println("Releasing texture for " + this.name);
			Textures textures10000 = this.textures;
			int i1 = this.skin;
			Textures textures2 = this.textures;
			textures10000.idBuffer.clear();
			textures2.idBuffer.put(i1);
			textures2.idBuffer.flip();
			GL11.glDeleteTextures(textures2.idBuffer);
		}

	}
}
