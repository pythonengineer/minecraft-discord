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
	public List[] particles = new List[2];
	private Textures textures;

	public ParticleEngine(Level level, Textures textures) {
		this.textures = textures;
		level.particleEngine = this;

		for(int i3 = 0; i3 < 2; ++i3) {
			this.particles[i3] = new ArrayList();
		}

	}

	public final void addParticle(Particle particle) {
		int i2 = particle.getParticleTexture();
		this.particles[i2].add(particle);
	}

	public final void tick() {
		for(int i1 = 0; i1 < 2; ++i1) {
			for(int i2 = 0; i2 < this.particles[i1].size(); ++i2) {
				Particle particle3;
				(particle3 = (Particle)this.particles[i1].get(i2)).tick();
				if(particle3.removed) {
					this.particles[i1].remove(i2--);
				}
			}
		}

	}

	public final void render(Player player, float translation) {
		float f3 = -((float)Math.cos((double)player.yRot * Math.PI / 180.0D));
		float f4;
		float f5 = -(f4 = -((float)Math.sin((double)player.yRot * Math.PI / 180.0D))) * (float)Math.sin((double)player.xRot * Math.PI / 180.0D);
		float f6 = f3 * (float)Math.sin((double)player.xRot * Math.PI / 180.0D);
		float f11 = (float)Math.cos((double)player.xRot * Math.PI / 180.0D);

		for(int i7 = 0; i7 < 2; ++i7) {
			if(this.particles[i7].size() != 0) {
				int i8 = 0;
				if(i7 == 0) {
					i8 = this.textures.loadTexture("/particles.png");
				}

				if(i7 == 1) {
					i8 = this.textures.loadTexture("/terrain.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
				Tesselator tesselator12 = Tesselator.instance;
				Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX_COLOR);

				for(int i9 = 0; i9 < this.particles[i7].size(); ++i9) {
					((Particle)this.particles[i7].get(i9)).render(tesselator12, translation, f3, f11, f4, f5, f6);
				}

				tesselator12.end();
			}
		}

	}
}