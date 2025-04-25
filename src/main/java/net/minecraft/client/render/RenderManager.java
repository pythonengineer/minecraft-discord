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
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class RenderManager {
    private MD3Model[] model = new MD3Model[1];
    private World worldObj;
    private RenderBlocks blockRenderer;
    private float playerViewY;

    public RenderManager() {
        new ModelBiped();
        this.blockRenderer = new RenderBlocks(Tessellator.instance);

        try {
            this.model[0] = new MD3Model((new MD3Loader()).loadModel("/test2.md3"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public final void renderEntityWithPosYaw(Entity var1, RenderEngine var2, float var3) {
        float var4 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
        float var5 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
        float var6 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
        float var7 = this.worldObj.getBlockLightValue((int)var4, (int)(var5 + var1.bbHeight * 2.0F / 3.0F), (int)var6);
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
            this.blockRenderer.renderBlockOnInventory(Block.tnt);
            GL11.glPopMatrix();
        } else {
            if(var1 instanceof EntityItem) {
                EntityItem var9 = (EntityItem)var1;
                GL11.glPushMatrix();
                GL11.glTranslatef(var3, var4, var5);
                GL11.glEnable(GL11.GL_NORMALIZE);
                ItemStack var14 = var9.item;
                if(var14.itemID > 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.25F, 0.25F, 0.25F);
                    var15 = var2.getTexture("/terrain.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
                    var14 = var9.item;
                    this.blockRenderer.renderBlockOnInventory(Block.blocksList[var14.itemID]);
                    GL11.glPopMatrix();
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                    var15 = var2.getTexture("/gui/items.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
                    Tessellator var13 = Tessellator.instance;
                    int var10 = var9.item.iconIndex;
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

    public final void setWorld(World var1) {
        this.worldObj = var1;
    }

    public final void cacheActiveRenderInfo(float var1) {
        EntityPlayer var2 = (EntityPlayer)this.worldObj.getPlayer();
        this.playerViewY = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1;
    }
}
