package com.mojang.minecraft.net;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.mob.HumanoidMob;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.util.LinkedList;
import java.util.List;

public class NetworkPlayer extends HumanoidMob {
	public static final long serialVersionUID = 77479605454997290L;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private transient int texture = -1;
	public transient ImageData newTexture = null;
	public String name;
	public String displayName;
	int tickCount = 0;
	private Textures textures;

	public NetworkPlayer(Minecraft minecraft, int id, String username, int x, int y, int z, float yRot, float xRot) {
		super(minecraft.level, (float)x, (float)y, (float)z);
		this.minecraft = minecraft;
		this.displayName = username;
		username = Font.removeColorCodes(username);
		this.name = username;
		this.xp = x;
		this.yp = y;
		this.zp = z;
		this.setPos((float)x / 32.0F, (float)y / 32.0F, (float)z / 32.0F);
		this.xRot = xRot;
		this.yRot = yRot;
        (new NetworkPlayerTextureLoader(this)).start();
		this.allowAlpha = false;
	}

	public void aiStep() {
		int i1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((EntityPos)this.moveQueue.remove(0));
			}
		} while(i1-- > 0 && this.moveQueue.size() > 10);

	}

	public void bindTexture(Textures textures) {
		if(this.newTexture != null) {
			ImageData bufferedImage2 = this.newTexture;
			int[] i3 = new int[512];
			bufferedImage2.getRGB(32, 0, 32, 16, i3, 0, 32);
			int i5 = 0;

			boolean z10001;
			while(true) {
				if(i5 >= i3.length) {
					z10001 = false;
					break;
				}

				if(i3[i5] >>> 24 < 128) {
					z10001 = true;
					break;
				}

				++i5;
			}

			this.hasHair = z10001;
			this.texture = textures.loadTexture(this.newTexture);
			this.newTexture = null;
		}

		if(this.texture < 0) {
			GL11.glBindTexture(3553, textures.loadTexture("/char.png"));
		} else {
			GL11.glBindTexture(3553, this.texture);
		}
	}

	public void render(Textures textures, float translation) {
		super.render(textures, translation);
		this.textures = textures;
		Font textures1 = this.minecraft.font;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation + 0.8F, this.zo + (this.z - this.zo) * translation);
		GL11.glRotatef(-this.minecraft.player.yRot, 0.0F, 1.0F, 0.0F);
		translation = 0.05F;
		GL11.glScalef(0.05F, -translation, translation);
		GL11.glTranslatef((float)(-textures1.width(this.displayName)) / 2.0F, 0.0F, 0.0F);
		GL11.glNormal3f(1.0F, -1.0F, 1.0F);
		GL11.glDisable(2896);
		GL11.glDisable(16384);
		if(this.name.equalsIgnoreCase("Notch")) {
			textures1.draw(this.displayName, 0, 0, 16776960);
		} else {
			textures1.draw(this.displayName, 0, 0, 16777215);
		}

		GL11.glEnable(16384);
		GL11.glEnable(2896);
		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		textures1.draw(this.name, 0, 0, 5263440);
		GL11.glPopMatrix();
		GL11.glDisable(3553);
	}

	public void queue(byte xa, byte ya, byte za, float xr, float yr) {
		float f6 = xr - this.yRot;

		float f7;
		for(f7 = yr - this.xRot; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.yRot + f6 * 0.5F;
		f7 = this.xRot + f7 * 0.5F;
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)xa / 2.0F) / 32.0F, ((float)this.yp + (float)ya / 2.0F) / 32.0F, ((float)this.zp + (float)za / 2.0F) / 32.0F, f6, f7));
		this.xp += xa;
		this.yp += ya;
		this.zp += za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void teleport(short xa, short ya, short za, float xr, float yr) {
		float f6 = xr - this.yRot;

		float f7;
		for(f7 = yr - this.xRot; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.yRot + f6 * 0.5F;
		f7 = this.xRot + f7 * 0.5F;
		this.moveQueue.add(new EntityPos((float)(this.xp + xa) / 64.0F, (float)(this.yp + ya) / 64.0F, (float)(this.zp + za) / 64.0F, f6, f7));
		this.xp = xa;
		this.yp = ya;
		this.zp = za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void queue(byte x, byte y, byte z) {
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)x / 2.0F) / 32.0F, ((float)this.yp + (float)y / 2.0F) / 32.0F, ((float)this.zp + (float)z / 2.0F) / 32.0F));
		this.xp += x;
		this.yp += y;
		this.zp += z;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float xr, float yr) {
		float f3 = xr - this.yRot;

		float f4;
		for(f4 = yr - this.xRot; f3 >= 180.0F; f3 -= 360.0F) {
		}

		while(f3 < -180.0F) {
			f3 += 360.0F;
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		while(f4 < -180.0F) {
			f4 += 360.0F;
		}

		f3 = this.yRot + f3 * 0.5F;
		f4 = this.xRot + f4 * 0.5F;
		this.moveQueue.add(new EntityPos(f3, f4));
		this.moveQueue.add(new EntityPos(xr, yr));
	}

	public void clear() {
		if(this.texture >= 0) {
			Textures textures10000 = this.textures;
			int i1 = this.texture;
			Textures textures2 = this.textures;
			textures10000.pixelsMap.remove(i1);
			textures2.ib.clear();
			textures2.ib.put(i1);
			textures2.ib.flip();
			GL11.glDeleteTextures(textures2.ib);
		}

	}
}
