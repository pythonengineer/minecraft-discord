package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;

public class RenderItem extends Render {
    private RenderBlocks itemRenderBlocks = new RenderBlocks();
    private EaglercraftRandom random = new EaglercraftRandom();

    public RenderItem() {
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void doRenderItem(EntityItem entity, double x, double y, double z, float yaw, float partialTicks) {
        this.random.setSeed(187L);
        ItemStack itemStack2 = entity.item;
        GL11.glPushMatrix();
        float f3 = MathHelper.sin(((float)entity.age + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
        float f4 = (((float)entity.age + partialTicks) / 20.0F + entity.hoverStart) * 57.295776F;
        byte b5 = 1;
        if(entity.item.stackSize > 1) {
            b5 = 2;
        }

        if(entity.item.stackSize > 5) {
            b5 = 3;
        }

        if(entity.item.stackSize > 20) {
            b5 = 4;
        }

        GL11.glTranslatef((float)x, (float)y + f3, (float)z);
        GL11.glEnable(GL11.GL_RESCALE_NORMAL);
        float f6;
        float f7;
        float f8;
        if(itemStack2.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack2.itemID].getRenderType())) {
            GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
            this.loadTexture("/terrain.png");
            float scale = 0.25F;
            if(!Block.blocksList[itemStack2.itemID].renderAsNormalBlock() && itemStack2.itemID != Block.stairSingle.blockID) {
                scale = 0.5F;
            }

            GL11.glScalef(scale, scale, scale);

            for(int i9 = 0; i9 < b5; ++i9) {
                GL11.glPushMatrix();
                if(i9 > 0) {
                    f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale;
                    f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale;
                    f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / scale;
                    GL11.glTranslatef(f6, f7, f8);
                }

                this.itemRenderBlocks.renderBlockOnInventory(Block.blocksList[itemStack2.itemID]);
                GL11.glPopMatrix();
            }
        } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int icon = itemStack2.getIconIndex();
            if(itemStack2.itemID < 256) {
                this.loadTexture("/terrain.png");
            } else {
                this.loadTexture("/gui/items.png");
            }

            Tessellator tessellator10 = Tessellator.instance;
            f6 = (float)(icon % 16 << 4) / 256.0F;
            f7 = (float)((icon % 16 << 4) + 16) / 256.0F;
            f8 = (float)(icon / 16 << 4) / 256.0F;
            float f11 = (float)((icon / 16 << 4) + 16) / 256.0F;

            for(int i12 = 0; i12 < b5; ++i12) {
                GL11.glPushMatrix();
                if(i12 > 0) {
                    float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f15 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f13, f14, f15);
                }

                GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                tessellator10.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator10.setNormal(0.0F, 1.0F, 0.0F);
                tessellator10.addVertexWithUV(-0.5D, -0.25D, 0.0D, (double)f6, (double)f11);
                tessellator10.addVertexWithUV(0.5D, -0.25D, 0.0D, (double)f7, (double)f11);
                tessellator10.addVertexWithUV(0.5D, 0.75D, 0.0D, (double)f7, (double)f8);
                tessellator10.addVertexWithUV(-0.5D, 0.75D, 0.0D, (double)f6, (double)f8);
                tessellator10.draw();
                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public void renderItemIntoGUI(RenderEngine renderEngine, ItemStack stack, int x, int y) {
        if(stack != null) {
            if(stack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[stack.itemID].getRenderType())) {
                int itemID = stack.itemID;
                renderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
                Block block3 = Block.blocksList[itemID];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(x - 2), (float)(y + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                this.itemRenderBlocks.renderBlockOnInventory(block3);
                GL11.glPopMatrix();
            } else if(stack.getIconIndex() >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                if(stack.itemID < 256) {
                    renderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
                } else {
                    renderEngine.bindTexture(renderEngine.getTexture("/gui/items.png"));
                }

                this.renderIcon(x, y, stack.getIconIndex() % 16 * 16, stack.getIconIndex() / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack stack, int x, int y) {
        this.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack stack, int x, int y, String s) {
        if(stack != null) {
            if(s == null && stack.stackSize > 1) {
                s = "" + stack.stackSize;
            }

            if(s != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                fontRenderer.drawStringWithShadow(s, x + 19 - 2 - fontRenderer.getStringWidth(s), y + 6 + 3, 0xFFFFFF);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if(stack.itemDmg > 0) {
                int i9 = 13 - stack.itemDmg * 13 / stack.getMaxDamage();
                int fontRenderer1 = 255 - stack.itemDmg * 255 / stack.getMaxDamage();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator stack1 = Tessellator.instance;
                int i6 = 255 - fontRenderer1 << 16 | fontRenderer1 << 8;
                fontRenderer1 = (255 - fontRenderer1) / 4 << 16 | 16128;
                renderQuad(stack1, x + 2, y + 13, 13, 2, 0);
                renderQuad(stack1, x + 2, y + 13, 12, 1, fontRenderer1);
                renderQuad(stack1, x + 2, y + 13, i9, 1, i6);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }

    private static void renderQuad(Tessellator tessellator, int x, int y, int z, int offsetY, int color) {
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_COLOR);
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex((double)x, (double)y, 0.0D);
        tessellator.addVertex((double)x, (double)(y + offsetY), 0.0D);
        tessellator.addVertex((double)(x + z), (double)(y + offsetY), 0.0D);
        tessellator.addVertex((double)(x + z), (double)y, 0.0D);
        tessellator.draw();
    }

    public void renderIcon(int x, int y, int u, int v, int i5, int i6) {
        float f7 = 0.0F;
        float f8 = 0.00390625F;
        float f9 = 0.00390625F;
        Tessellator tessellator10 = Tessellator.instance;
        tessellator10.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator10.addVertexWithUV((double)(x + 0), (double)(y + i6), (double)f7, (double)((float)(u + 0) * f8), (double)((float)(v + i6) * f9));
        tessellator10.addVertexWithUV((double)(x + i5), (double)(y + i6), (double)f7, (double)((float)(u + i5) * f8), (double)((float)(v + i6) * f9));
        tessellator10.addVertexWithUV((double)(x + i5), (double)(y + 0), (double)f7, (double)((float)(u + i5) * f8), (double)((float)(v + 0) * f9));
        tessellator10.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)f7, (double)((float)(u + 0) * f8), (double)((float)(v + 0) * f9));
        tessellator10.draw();
    }

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.doRenderItem((EntityItem)entity, x, y, z, yaw, partialTicks);
    }
}
