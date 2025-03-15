package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class Sign extends Entity {
	private static SignModel model = new SignModel();
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private Font font;
	private String[] messages = new String[]{"This is a test", "of the signs.", "Each line can", "be 15 chars!"};

	public Sign(Minecraft minecraft, float x, float y, float z, float rot) {
		super(minecraft.level);
		this.setSize(0.5F, 1.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(x, y, z);
		this.font = minecraft.font;
		this.rot = -rot;
		this.heightOffset = 1.5F;
		this.xd = -((float)Math.sin((double)this.rot * Math.PI / 180.0D)) * 0.05F;
		this.yd = 0.2F;
		this.zd = -((float)Math.cos((double)this.rot * Math.PI / 180.0D)) * 0.05F;
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

	public void render(Textures texture, float translation) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int i3 = texture.loadTexture("/item/sign.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i3);
		float f4 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glColor4f(f4, f4, f4, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * translation);
		GL11.glRotatef(this.rot, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		SignModel signModel5 = model;
		model.signBoard.render(0.0625F);
		signModel5.signStick.render(0.0625F);
		GL11.glPopMatrix();
		f4 = 0.016666668F;
		GL11.glTranslatef(0.0F, 0.5F, 0.09F);
		GL11.glScalef(f4, -f4, f4);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f4);
		GL11.glEnable(GL11.GL_BLEND);

		for(i3 = 0; i3 < this.messages.length; ++i3) {
			String string6 = this.messages[i3];
			this.font.draw(string6, -this.font.width(string6) / 2, i3 * 10 - this.messages.length * 5, 2105376);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}