package net.minecraft.client.render;

import java.io.IOException;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.md3.MD3Loader;
import net.minecraft.client.model.md3.MD3Model;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class RenderManager {
    private MD3Model[] model = new MD3Model[1];
    private World worldObj;
    private RenderBlocks renderBlocks;
    private float playerViewY;

    public RenderManager() {
        new ModelBiped();
        this.renderBlocks = new RenderBlocks(Tessellator.instance);

        try {
            this.model[0] = new MD3Model((new MD3Loader()).loadModel("/test2.md3"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public final void renderShadow(Entity var1, RenderEngine var2, float var3) {
        float var4 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
        float var5 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
        float var6 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
        float var7 = this.worldObj.getBlockLightValue((int)var4, (int)(var5 + var1.height * 2.0F / 3.0F), (int)var6);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        if(var1 instanceof EntityLiving) {
            float var12 = var6;
            float var11 = var5;
            float var10 = var4;
            RenderManager var8 = this;
            GL11.glEnable(GL11.GL_BLEND);
            var2.setClampTexture(true);
            int var13 = var2.getTexture("/shadow.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var13);
            var2.setClampTexture(false);
            GL11.glDepthMask(false);

            for(int var9 = (int)(var4 - 0.5F); var9 <= (int)(var10 + 0.5F); ++var9) {
                for(var13 = (int)(var11 - 2.0F); var13 <= (int)var11; ++var13) {
                    for(int var14 = (int)(var12 - 0.5F); var14 <= (int)(var12 + 0.5F); ++var14) {
                        int var15 = var8.worldObj.getBlockId(var9, var13 - 1, var14);
                        if(var15 > 0 && var8.worldObj.isHalfLit(var9, var13, var14)) {
                            Block var10001 = Block.blocksList[var15];
                            float var25 = 0.5F;
                            Block var16 = var10001;
                            Tessellator var23 = Tessellator.instance;
                            var25 = (1.0F - (var11 - (float)var13) / 2.0F) * 0.5F * var8.worldObj.getBlockLightValue(var9, var13, var14);
                            if(var25 >= 0.0F) {
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, var25);
                                var23.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                                var25 = (float)var9 + var16.minX;
                                float var18 = (float)var9 + var16.maxX;
                                float var20 = (float)var13 + var16.minY;
                                float var21 = (float)var14 + var16.minZ;
                                float var26 = (float)var14 + var16.maxZ;
                                float var22 = (var10 - var25) / 2.0F / 0.5F + 0.5F;
                                float var17 = (var10 - var18) / 2.0F / 0.5F + 0.5F;
                                float var24 = (var12 - var21) / 2.0F / 0.5F + 0.5F;
                                float var19 = (var12 - var26) / 2.0F / 0.5F + 0.5F;
                                var23.addVertexWithUV(var25, var20, var21, var22, var24);
                                var23.addVertexWithUV(var25, var20, var26, var22, var19);
                                var23.addVertexWithUV(var18, var20, var26, var17, var19);
                                var23.addVertexWithUV(var18, var20, var21, var17, var24);
                                var23.draw();
                            }
                        }
                    }
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
        }

        GL11.glColor3f(var7, var7, var7);
        this.doRender(var1, var2, var4, var5, var6, 1.0F, var3);
    }

    public final void doRender(Entity var1, RenderEngine var2, float var3, float var4, float var5, float var6, float var7) {
        int var9;
        if(var1.fire > 0) {
            GL11.glDisable(GL11.GL_LIGHTING);
            int var14 = Block.fire.blockIndexInTexture;
            int var15 = (var14 & 15) << 4;
            var14 &= 240;
            float var16 = (float)var15 / 256.0F;
            float var28 = ((float)var15 + 15.99F) / 256.0F;
            float var17 = (float)var14 / 256.0F;
            float var27 = ((float)var14 + 15.99F) / 256.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(var3, var4, var5);
            float var11 = var1.width * 1.4F;
            GL11.glScalef(var11, var11, var11);
            var9 = var2.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
            Tessellator var26 = Tessellator.instance;
            var11 = 1.0F;
            float var12 = 0.0F;
            float var10 = var1.height / var1.width;
            GL11.glRotatef(-this.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)var10) * 0.02F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var26.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

            while(var10 > 0.0F) {
                var26.addVertexWithUV(-0.5F, 0.0F - var12, 0.0F, var16, var27);
                var26.addVertexWithUV(var11 - 0.5F, 0.0F - var12, 0.0F, var28, var27);
                var26.addVertexWithUV(var11 - 0.5F, 1.4F - var12, 0.0F, var28, var17);
                var26.addVertexWithUV(-0.5F, 1.4F - var12, 0.0F, var16, var17);
                --var10;
                --var12;
                var11 *= 0.9F;
                GL11.glTranslatef(0.0F, 0.0F, -0.04F);
            }

            var26.draw();
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        float var11;
        int var15;
        if(var1 instanceof EntityLiving) {
            EntityLiving var12 = (EntityLiving)var1;
            GL11.glPushMatrix();

            try {
                var11 = var12.prevRenderYawOffset + (var12.renderYawOffset - var12.prevRenderYawOffset) * var7;
                var11 *= var6;
                GL11.glTranslatef(var3, var4, var5);
                var15 = var2.getTexture("/cube-nes.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
                GL11.glRotatef(-var11 + 180.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(0.02F, -0.02F, 0.02F);
                GL11.glEnable(GL11.GL_NORMALIZE);
                this.model[0].renderModelVertices(0, 0, 0.0F);
                GL11.glDisable(GL11.GL_NORMALIZE);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            GL11.glPopMatrix();
        } else if(var1 instanceof EntityTNTPrimed) {
            GL11.glPushMatrix();
            GL11.glTranslatef(var3, var4, var5);
            var15 = var2.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
            this.renderBlocks.renderBlockOnInventory(Block.tnt);
            GL11.glPopMatrix();
        } else {
            if(var1 instanceof EntityItem) {
                EntityItem var19 = (EntityItem)var1;
                ItemStack var20 = var19.item;
                GL11.glPushMatrix();
                GL11.glTranslatef(var3, var4, var5);
                GL11.glEnable(GL11.GL_NORMALIZE);
                if(var20.itemID < 256) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.25F, 0.25F, 0.25F);
                    var9 = var2.getTexture("/terrain.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
                    this.renderBlocks.renderBlockOnInventory(Block.blocksList[var20.itemID]);
                    GL11.glPopMatrix();
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    var15 = var2.getTexture("/gui/items.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
                    Tessellator var13 = Tessellator.instance;
                    int var10 = var20.getItem().getIconIndex();
                    var3 = (float)(var10 % 16 << 4) / 256.0F;
                    var4 = (float)((var10 % 16 << 4) + 16) / 256.0F;
                    var5 = (float)(var10 / 16 << 4) / 256.0F;
                    var11 = (float)((var10 / 16 << 4) + 16) / 256.0F;
                    GL11.glRotatef(-this.playerViewY, 0.0F, 1.0F, 0.0F);
                    var13.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                    var13.normal(0.0F, 1.0F, 0.0F);
                    var13.addVertexWithUV(-0.5F, -0.25F, 0.0F, var3, var11);
                    var13.addVertexWithUV(0.5F, -0.25F, 0.0F, var4, var11);
                    var13.addVertexWithUV(0.5F, 12.0F / 16.0F, 0.0F, var4, var5);
                    var13.addVertexWithUV(-0.5F, 12.0F / 16.0F, 0.0F, var3, var5);
                    var13.draw();
                }

                GL11.glDisable(GL11.GL_NORMALIZE);
                GL11.glPopMatrix();
            }

        }
    }

    public final void changeWorld(World var1) {
        this.worldObj = var1;
    }

    public final void setPlayerViewY(float var1) {
        EntityPlayer var2 = (EntityPlayer)this.worldObj.getPlayerEntity();
        this.playerViewY = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1;
    }
}
