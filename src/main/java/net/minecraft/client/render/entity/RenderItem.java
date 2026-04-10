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

public final class RenderItem extends Render {
    private RenderBlocks renderBlocks = new RenderBlocks();
    private EaglercraftRandom random = new EaglercraftRandom();

    public RenderItem() {
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public final void renderItemIntoGUI(RenderEngine renderEngine, ItemStack stack, int x, int y) {
        if(stack != null) {
            int stack1;
            if(stack.itemID < 256 && Block.blocksList[stack.itemID].getRenderType() == 0) {
                stack1 = stack.itemID;
                RenderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
                Block renderEngine3 = Block.blocksList[stack1];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(x - 2), (float)(y + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.renderBlocks.renderBlockOnInventory(renderEngine3);
                GL11.glPopMatrix();
            } else {
                if(stack.getItem().getIcon() >= 0) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    if(stack.itemID < 256) {
                        RenderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
                    } else {
                        RenderEngine.bindTexture(renderEngine.getTexture("/gui/items.png"));
                    }

                    int i10000 = x;
                    int i10001 = y;
                    int i10002 = stack.getItem().getIcon() % 16 << 4;
                    int i10003 = stack.getItem().getIcon() / 16 << 4;
                    boolean renderEngine1 = true;
                    renderEngine1 = true;
                    y = i10003;
                    x = i10002;
                    stack1 = i10001;
                    int renderEngine2 = i10000;
                    Tessellator tessellator5 = Tessellator.instance;
                    Tessellator.instance.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                    tessellator5.addVertexWithUV((double)renderEngine2, (double)(stack1 + 16), 0.0D, (double)((float)x * 0.00390625F), (double)((float)(y + 16) * 0.00390625F));
                    tessellator5.addVertexWithUV((double)(renderEngine2 + 16), (double)(stack1 + 16), 0.0D, (double)((float)(x + 16) * 0.00390625F), (double)((float)(y + 16) * 0.00390625F));
                    tessellator5.addVertexWithUV((double)(renderEngine2 + 16), (double)stack1, 0.0D, (double)((float)(x + 16) * 0.00390625F), (double)((float)y * 0.00390625F));
                    tessellator5.addVertexWithUV((double)renderEngine2, (double)stack1, 0.0D, (double)((float)x * 0.00390625F), (double)((float)y * 0.00390625F));
                    tessellator5.draw();
                    GL11.glEnable(GL11.GL_LIGHTING);
                }

            }
        }
    }

    public final void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack stack, int x, int y) {
        this.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, null);
    }

    public final void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack stack, int x, int y, String s) {
        if(stack != null) {
            if(s == null && stack.stackSize > 1) {
                s = "" + stack.stackSize;
            }

            if(s != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                fontRenderer.drawStringWithShadow(s, x + 19 - 2 - fontRenderer.width(s), y + 6 + 3, 0xFFFFFF);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if(stack.itemDamage > 0) {
                int i9 = 13 - stack.itemDamage * 13 / stack.getItemDamageForDisplay();
                int fontRenderer1 = 255 - stack.itemDamage * 255 / stack.getItemDamageForDisplay();
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
        tessellator.drawVertex((double)x, (double)y, 0.0D);
        tessellator.drawVertex((double)x, (double)(y + offsetY), 0.0D);
        tessellator.drawVertex((double)(x + z), (double)(y + offsetY), 0.0D);
        tessellator.drawVertex((double)(x + z), (double)y, 0.0D);
        tessellator.draw();
    }

    public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityItem entityItem19 = (EntityItem)entity;
        RenderItem renderItem18 = this;
        this.random.setSeed(187L);
        ItemStack itemStack24 = entityItem19.item;
        GL11.glPushMatrix();
        float f5 = MathHelper.sin(((float)entityItem19.age + partialTicks) / 10.0F + entityItem19.hoverStart) * 0.1F + 0.1F;
        float f3 = (((float)entityItem19.age + partialTicks) / 20.0F + entityItem19.hoverStart) * 57.295776F;
        byte b26 = 1;
        if(entityItem19.item.stackSize > 1) {
            b26 = 2;
        }

        if(entityItem19.item.stackSize > 5) {
            b26 = 3;
        }

        if(entityItem19.item.stackSize > 20) {
            b26 = 4;
        }

        GL11.glTranslatef((float)x, (float)y + f5, (float)z);
        GL11.glEnable(GL11.GL_RESCALE_NORMAL);
        float f7;
        float f21;
        if(itemStack24.itemID < 256 && Block.blocksList[itemStack24.itemID].getRenderType() == 0) {
            GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
            this.loadTexture("/terrain.png");
            f21 = 0.25F;
            if(!Block.blocksList[itemStack24.itemID].renderAsNormalBlock() && itemStack24.itemID != Block.stairSingle.blockID) {
                f21 = 0.5F;
            }

            GL11.glScalef(f21, f21, f21);

            for(int i23 = 0; i23 < b26; ++i23) {
                GL11.glPushMatrix();
                if(i23 > 0) {
                    f5 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
                    f7 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
                    yaw = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
                    GL11.glTranslatef(f5, f7, yaw);
                }

                renderItem18.renderBlocks.renderBlockOnInventory(Block.blocksList[itemStack24.itemID]);
                GL11.glPopMatrix();
            }
        } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int i20 = itemStack24.getItem().getIcon();
            if(itemStack24.itemID < 256) {
                this.loadTexture("/terrain.png");
            } else {
                this.loadTexture("/gui/items.png");
            }

            Tessellator tessellator22 = Tessellator.instance;
            f5 = (float)(i20 % 16 << 4) / 256.0F;
            f7 = (float)((i20 % 16 << 4) + 16) / 256.0F;
            yaw = (float)(i20 / 16 << 4) / 256.0F;
            f21 = (float)((i20 / 16 << 4) + 16) / 256.0F;

            for(int i25 = 0; i25 < b26; ++i25) {
                GL11.glPushMatrix();
                if(i25 > 0) {
                    partialTicks = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f10 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f11 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(partialTicks, f10, f11);
                }

                GL11.glRotatef(180.0F - renderItem18.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                tessellator22.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                tessellator22.setNormal(0.0F, 1.0F, 0.0F);
                tessellator22.addVertexWithUV(-0.5D, -0.25D, 0.0D, (double)f5, (double)f21);
                tessellator22.addVertexWithUV(0.5D, -0.25D, 0.0D, (double)f7, (double)f21);
                tessellator22.addVertexWithUV(0.5D, 0.75D, 0.0D, (double)f7, (double)yaw);
                tessellator22.addVertexWithUV(-0.5D, 0.75D, 0.0D, (double)f5, (double)yaw);
                tessellator22.draw();
                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(GL11.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}