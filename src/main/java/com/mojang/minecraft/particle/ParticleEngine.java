package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

import java.util.ArrayList;
import java.util.List;

public final class ParticleEngine {
	public List particles = new ArrayList();
	private Textures textures;

	public ParticleEngine(Level level1, Textures textures2) {
		this.textures = textures2;
	}

	public final void render(Player player1, float f2) {
		if(this.particles.size() != 0) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			int i3 = this.textures.loadTexture("/terrain.png", 9728);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i3);
			float f12 = -((float)Math.cos((double)player1.yRot * Math.PI / 180.0D));
			float f4;
			float f5 = -(f4 = -((float)Math.sin((double)player1.yRot * Math.PI / 180.0D))) * (float)Math.sin((double)player1.xRot * Math.PI / 180.0D);
			float f6 = f12 * (float)Math.sin((double)player1.xRot * Math.PI / 180.0D);
			float f11 = (float)Math.cos((double)player1.xRot * Math.PI / 180.0D);
			Tesselator tesselator7 = Tesselator.instance;
			Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX_COLOR);

			for(int i8 = 0; i8 < this.particles.size(); ++i8) {
				Particle particle9 = (Particle)this.particles.get(i8);
				float f10 = 0.8F * particle9.getBrightness();
				tesselator7.color(f10, f10, f10);
				particle9.render(tesselator7, f2, f12, f11, f4, f5, f6);
			}

			tesselator7.end();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
}
