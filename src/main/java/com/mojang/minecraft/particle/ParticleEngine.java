package com.mojang.minecraft.particle;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.Level;
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

    public void add(Particle p) {
        this.particles.add(p);
    }

    public final void render(Player player1, float f2, int i3) {
        if(this.particles.size() != 0) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int i4 = this.textures.loadTexture("/terrain.png", 9728);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i4);
            float f26 = -((float)Math.cos((double)player1.yRot * Math.PI / 180.0D));
            float f5;
            float f6 = -(f5 = -((float)Math.sin((double)player1.yRot * Math.PI / 180.0D))) * (float)Math.sin((double)player1.xRot * Math.PI / 180.0D);
            float f7 = f26 * (float)Math.sin((double)player1.xRot * Math.PI / 180.0D);
            float f25 = (float)Math.cos((double)player1.xRot * Math.PI / 180.0D);
            Tesselator tesselator8 = Tesselator.instance;
            GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
            tesselator8.begin(DefaultVertexFormats.POSITION_TEX);

            for(int i9 = 0; i9 < this.particles.size(); ++i9) {
                Particle particle10;
                if((particle10 = (Particle)this.particles.get(i9)).isLit() ^ i3 == 1) {
                    float f18;
                    float f19 = (f18 = ((float)((particle10 = particle10).tex % 16) + particle10.uo / 4.0F) / 16.0F) + 0.015609375F;
                    float f20;
                    float f21 = (f20 = ((float)(particle10.tex / 16) + particle10.vo / 4.0F) / 16.0F) + 0.015609375F;
                    float f22 = 0.1F * particle10.size;
                    float f23 = particle10.xo + (particle10.x - particle10.xo) * f2;
                    float f24 = particle10.yo + (particle10.y - particle10.yo) * f2;
                    float f27 = particle10.zo + (particle10.z - particle10.zo) * f2;
                    tesselator8.vertexUV(f23 - f26 * f22 - f6 * f22, f24 - f25 * f22, f27 - f5 * f22 - f7 * f22, f18, f21);
                    tesselator8.vertexUV(f23 - f26 * f22 + f6 * f22, f24 + f25 * f22, f27 - f5 * f22 + f7 * f22, f18, f20);
                    tesselator8.vertexUV(f23 + f26 * f22 + f6 * f22, f24 + f25 * f22, f27 + f5 * f22 + f7 * f22, f19, f20);
                    tesselator8.vertexUV(f23 + f26 * f22 - f6 * f22, f24 - f25 * f22, f27 + f5 * f22 - f7 * f22, f19, f21);
                }
            }

            tesselator8.end();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }
}
