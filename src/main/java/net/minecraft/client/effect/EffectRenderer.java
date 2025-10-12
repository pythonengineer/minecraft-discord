package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class EffectRenderer {
    private World worldObj;
    private List[] fxLayers = new List[3];
    private RenderEngine renderEngine;
    private EaglercraftRandom rand = new EaglercraftRandom();

    public EffectRenderer(World var1, RenderEngine var2) {
        if(var1 != null) {
            this.worldObj = var1;
        }

        this.renderEngine = var2;

        for(int var3 = 0; var3 < 3; ++var3) {
            this.fxLayers[var3] = new ArrayList();
        }

    }

    public final void addEffect(EntityFX var1) {
        int var2 = var1.getFXLayer();
        this.fxLayers[var2].add(var1);
    }

    public final void updateEffects() {
        for(int var1 = 0; var1 < 3; ++var1) {
            for(int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
                EntityFX var3 = (EntityFX)this.fxLayers[var1].get(var2);
                var3.onUpdate();
                if(var3.isDead) {
                    this.fxLayers[var1].remove(var2--);
                }
            }
        }

    }

    public final void renderParticles(Entity var1, float var2) {
        float var3 = MathHelper.cos(var1.rotationYaw * (float)Math.PI / 180.0F);
        float var4 = MathHelper.sin(var1.rotationYaw * (float)Math.PI / 180.0F);
        float var5 = -var4 * MathHelper.sin(var1.rotationPitch * (float)Math.PI / 180.0F);
        float var6 = var3 * MathHelper.sin(var1.rotationPitch * (float)Math.PI / 180.0F);
        float var7 = MathHelper.cos(var1.rotationPitch * (float)Math.PI / 180.0F);
        EntityFX.V = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
        EntityFX.W = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
        EntityFX.X = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;

        for(int var11 = 0; var11 < 2; ++var11) {
            if(this.fxLayers[var11].size() != 0) {
                int var8 = 0;
                if(var11 == 0) {
                    var8 = this.renderEngine.getTexture("/particles.png");
                }

                if(var11 == 1) {
                    var8 = this.renderEngine.getTexture("/terrain.png");
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
                Tessellator var12 = Tessellator.instance;
                var12.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);

                for(int var9 = 0; var9 < this.fxLayers[var11].size(); ++var9) {
                    EntityFX var10 = (EntityFX)this.fxLayers[var11].get(var9);
                    var10.renderParticle(var12, var2, var3, var7, var4, var5, var6);
                }

                var12.draw();
            }
        }

    }

    public final void renderLitParticles(float var1) {
        if(this.fxLayers[2].size() != 0) {
            Tessellator var2 = Tessellator.instance;

            for(int var3 = 0; var3 < this.fxLayers[2].size(); ++var3) {
                EntityFX var4 = (EntityFX)this.fxLayers[2].get(var3);
                var4.renderParticle(var2, var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            }

        }
    }

    public final void clearEffects(World var1) {
        this.worldObj = var1;

        for(int var2 = 0; var2 < 3; ++var2) {
            this.fxLayers[var2].clear();
        }

    }

    public final void addBlockDestroyEffects(int var1, int var2, int var3) {
        int var4 = this.worldObj.getBlockId(var1, var2, var3);
        if(var4 != 0) {
            Block var15 = Block.blocksList[var4];

            for(int var5 = 0; var5 < 4; ++var5) {
                for(int var6 = 0; var6 < 4; ++var6) {
                    for(int var7 = 0; var7 < 4; ++var7) {
                        double var9 = (double)var1 + ((double)var5 + 0.5D) / 4.0D;
                        double var11 = (double)var2 + ((double)var6 + 0.5D) / 4.0D;
                        double var13 = (double)var3 + ((double)var7 + 0.5D) / 4.0D;
                        this.addEffect(new EntityDiggingFX(this.worldObj, var9, var11, var13, var9 - (double)var1 - 0.5D, var11 - (double)var2 - 0.5D, var13 - (double)var3 - 0.5D, var15));
                    }
                }
            }

        }
    }

    public final void addBlockHitEffects(int var1, int var2, int var3, int var4) {
        int var5 = this.worldObj.getBlockId(var1, var2, var3);
        if(var5 != 0) {
            Block var9 = Block.blocksList[var5];
            double var6 = (double)var1 + this.rand.nextDouble() * (var9.maxX - var9.minX - (double)0.2F) + (double)0.1F + var9.minX;
            double var7 = (double)var2 + this.rand.nextDouble() * (var9.maxY - var9.minY - (double)0.2F) + (double)0.1F + var9.minY;
            double var8 = (double)var3 + this.rand.nextDouble() * (var9.maxZ - var9.minZ - (double)0.2F) + (double)0.1F + var9.minZ;
            if(var4 == 0) {
                var7 = (double)var2 + var9.minY - (double)0.1F;
            }

            if(var4 == 1) {
                var7 = (double)var2 + var9.maxY + (double)0.1F;
            }

            if(var4 == 2) {
                var8 = (double)var3 + var9.minZ - (double)0.1F;
            }

            if(var4 == 3) {
                var8 = (double)var3 + var9.maxZ + (double)0.1F;
            }

            if(var4 == 4) {
                var6 = (double)var1 + var9.minX - (double)0.1F;
            }

            if(var4 == 5) {
                var6 = (double)var1 + var9.maxX + (double)0.1F;
            }

            this.addEffect((new EntityDiggingFX(this.worldObj, var6, var7, var8, 0.0D, 0.0D, 0.0D, var9)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
    }

    public final String getStatistics() {
        return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
    }
}
