package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.util.List;

public class Arrow extends Entity {
	private float xd;
	private float yd;
	private float zd;
	private float yRot;
	private float xRot;
	private float yRotO;
	private float xRotO;
	private boolean hasHit = false;
	private int stickTime = 0;
	private Player owner;
	private int time = 0;

	public Arrow(Minecraft minecraft, Player player, float x, float y, float z, float xr, float yr) {
		super(minecraft.level);
		this.owner = player;
		this.setSize(0.25F, 0.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.heightOffset = 0.25F;
		float minecraft1 = (float)Math.cos((double)(-xr) * Math.PI / 180.0D - Math.PI);
		float player1 = (float)Math.sin((double)(-xr) * Math.PI / 180.0D - Math.PI);
		xr = (float)Math.cos((double)(-yr) * Math.PI / 180.0D);
		yr = (float)Math.sin((double)(-yr) * Math.PI / 180.0D);
		this.slide = false;
		float f8 = 0.8F;
		this.xo -= minecraft1 * 0.2F;
		this.zo += player1 * 0.2F;
		x -= minecraft1 * 0.2F;
		z += player1 * 0.2F;
		this.xd = player1 * xr * f8;
		this.yd = yr * f8;
		this.zd = minecraft1 * xr * f8;
		this.setPos(x, y, z);
		minecraft1 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
		this.yRotO = this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / Math.PI);
		this.xRotO = this.xRot = (float)(Math.atan2((double)this.yd, (double)minecraft1) * 180.0D / Math.PI);
		this.makeStepSound = false;
	}

	public void tick() {
		++this.time;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.hasHit) {
			++this.stickTime;
			if(this.stickTime >= 20) {
				this.remove();
			}

		} else {
			this.xd *= 0.992F;
			this.yd *= 0.992F;
			this.zd *= 0.992F;
			this.yd -= 0.02F;
			int i1 = (int)((float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd)) / 0.2F + 1.0F);
			float f2 = this.xd / (float)i1;
			float f3 = this.yd / (float)i1;
			float f4 = this.zd / (float)i1;

			for(int i5 = 0; i5 < i1 && !this.collision; ++i5) {
				AABB aABB6 = this.bb.expand(f2, f3, f4);
				if(this.level.getCubes(aABB6).size() > 0) {
					this.collision = true;
				}

				List list10 = this.level.blockMap.getEntities(this, aABB6);

				for(int i7 = 0; i7 < list10.size(); ++i7) {
					Entity entity8;
					if((entity8 = (Entity)list10.get(i7)).isShootable() && entity8 != this.owner && this.time <= 20) {
						entity8.hurt(this, 3);
						this.collision = true;
						this.stickTime = 20;
					}
				}

				if(!this.collision) {
					this.bb.move(f2, f3, f4);
					this.x += f2;
					this.y += f3;
					this.z += f4;
				}
			}

			if(this.collision) {
				this.hasHit = true;
				this.xd = this.yd = this.zd = 0.0F;
			}

			if(!this.hasHit) {
				float f9 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
				this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / Math.PI);

				for(this.xRot = (float)(Math.atan2((double)this.yd, (double)f9) * 180.0D / Math.PI); this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F) {
				}

				while(this.xRot - this.xRotO >= 180.0F) {
					this.xRotO += 360.0F;
				}

				while(this.yRot - this.yRotO < -180.0F) {
					this.yRotO -= 360.0F;
				}

				while(this.yRot - this.yRotO >= 180.0F) {
					this.yRotO += 360.0F;
				}
			}

		}
	}

	public void render(Textures texture, float translation) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int i6 = texture.loadTexture("/item/arrows.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i6);
		float f7 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glColor4f(f7, f7, f7, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * translation);
		GL11.glRotatef(this.yRotO + (this.yRot - this.yRotO) * translation - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.xRotO + (this.xRot - this.xRotO) * translation, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		Tesselator tesselator5 = Tesselator.instance;
		f7 = 0.5F;
		translation = 0.15625F;
		float f3 = 0.05625F;
		GL11.glScalef(0.05625F, f3, f3);

		for(int i4 = 0; i4 < 4; ++i4) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, -f3, 0.0F);
			tesselator5.begin(DefaultVertexFormats.POSITION_TEX);
			tesselator5.vertexUV(-8.0F, -2.0F, 0.0F, 0.0F, 0.0F);
			tesselator5.vertexUV(8.0F, -2.0F, 0.0F, f7, 0.0F);
			tesselator5.vertexUV(8.0F, 2.0F, 0.0F, f7, translation);
			tesselator5.vertexUV(-8.0F, 2.0F, 0.0F, 0.0F, translation);
			tesselator5.end();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void awardKillScore(Entity entity1, int i2) {
		this.owner.awardKillScore(entity1, i2);
	}
}