package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.SmokeParticle;
import com.mojang.minecraft.particle.TerrainParticle;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class PrimedTnt extends Entity {
    public static final long serialVersionUID = 0L;
    private float xd;
    private float yd;
    private float zd;
    public int life = 0;
    private boolean defused;

    public PrimedTnt(Level level, float float2, float float3, float float4) {
        super(level);
        this.setSize(0.98F, 0.98F);
        this.heightOffset = this.bbHeight / 2.0F;
        this.setPos(float2, float3, float4);
        float level1 = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
        this.xd = (float)(-Math.sin(level1 * (float)Math.PI / 180.0F) * 0.02F);
        this.yd = 0.2F;
        this.zd = (float)(-Math.cos(level1 * (float)Math.PI / 180.0F) * 0.02F);
        this.makeStepSound = false;
        this.life = 40;
        this.xo = float2;
        this.yo = float3;
        this.zo = float4;
    }

    public void hurt(Entity entity1, int i2) {
        if(!this.removed) {
            super.hurt(entity1, i2);
            if(entity1 instanceof Player) {
                this.remove();
                this.level.addEntity(new Item(this.level, this.x, this.y, this.z, Tile.tnt.id));
            }

        }
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

        if(!this.defused) {
            if(this.life-- > 0) {
                this.level.particleEngine.addParticle(new SmokeParticle(this.level, this.x, this.y + 0.6F, this.z));
            } else {
                this.remove();
                PrimedTnt primedTnt10 = this;
                EaglercraftRandom random1 = new EaglercraftRandom();
                float f2 = 4.0F;
                this.level.explode((Entity)null, this.x, this.y, this.z, f2);

                for(int i3 = 0; i3 < 100; ++i3) {
                    float f4 = (float)random1.nextGaussian() * f2 / 4.0F;
                    float f5 = (float)random1.nextGaussian() * f2 / 4.0F;
                    float f6 = (float)random1.nextGaussian() * f2 / 4.0F;
                    float f7 = (float)Math.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
                    float f8 = f4 / f7 / f7;
                    float f9 = f5 / f7 / f7;
                    f7 = f6 / f7 / f7;
                    primedTnt10.level.particleEngine.addParticle(new TerrainParticle(primedTnt10.level, primedTnt10.x + f4, primedTnt10.y + f5, primedTnt10.z + f6, f8, f9, f7, Tile.tnt));
                }

            }
        }
    }

    public void playerTouch(Entity entity1) {
        if(this.defused) {
            Player player2;
            if((player2 = (Player)entity1).addResource(Tile.tnt.id)) {
                this.level.addEntity(new TakeEntityAnim(this.level, this, player2));
                this.remove();
            }

        }
    }

    public void render(Textures textures, float translation) {
        int textures1 = textures.loadTexture("/terrain.png");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures1);
        float textures2 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
        GL11.glPushMatrix();
        GL11.glColor4f(textures2, textures2, textures2, 1.0F);
        GL11.glTranslatef(this.xo + (this.x - this.xo) * translation - 0.5F, this.yo + (this.y - this.yo) * translation - 0.5F, this.zo + (this.z - this.zo) * translation - 0.5F);
        GL11.glPushMatrix();
        Tesselator textures3 = Tesselator.instance;
        Tile.tnt.renderGuiTile(textures3);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)((this.life / 4 + 1) % 2) * 0.4F);
        if(this.life <= 16) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)((this.life + 1) % 2) * 0.6F);
        }

        if(this.life <= 2) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        Tile.tnt.renderGuiTile(textures3);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}