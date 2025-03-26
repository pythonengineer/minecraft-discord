package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class Sign extends Entity {
	public static final long serialVersionUID = 0L;
	private static SignModel model = new SignModel();
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private String[] messages = new String[]{"This is a test", "of the signs.", "Each line can", "be 15 chars!"};

	public Sign(Level level, float x, float y, float z, float rot) {
		super(level);
		this.setSize(0.5F, 1.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(x, y, z);
		this.rot = -rot;
		this.heightOffset = 1.5F;
		this.xd = -((float)Math.sin((double)this.rot * 3.141592653589793D / 180.0D)) * 0.05F;
		this.yd = 0.2F;
		this.zd = -((float)Math.cos((double)this.rot * 3.141592653589793D / 180.0D)) * 0.05F;
		this.makeStepSound = false;
	}

	public boolean isPickable() {
		return !this.removed;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.yd -= 0.04F;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
			this.yd *= -0.5F;
		}

	}

	public void render(Textures textures, float translation) {
		GL11.glEnable(3553);
		int i4 = textures.loadTexture("/item/sign.png");
		GL11.glBindTexture(3553, i4);
		float f5 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glColor4f(f5, f5, f5, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * translation);
		GL11.glRotatef(this.rot, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		SignModel signModel6 = model;
		model.signBoard.render(0.0625F);
		signModel6.signStick.render(0.0625F);
		GL11.glPopMatrix();
		f5 = 0.016666668F;
		GL11.glTranslatef(0.0F, 0.5F, 0.09F);
		GL11.glScalef(f5, -f5, f5);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f5);
		GL11.glEnable(3042);
		Font font8 = this.level.font;

		for(int i7 = 0; i7 < this.messages.length; ++i7) {
			String string3 = this.messages[i7];
			font8.draw(string3, -font8.width(string3) / 2, i7 * 10 - this.messages.length * 5, 2105376);
		}

		GL11.glDisable(3042);
		GL11.glDisable(3553);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
