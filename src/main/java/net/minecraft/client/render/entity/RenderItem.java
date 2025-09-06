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
import net.minecraft.game.level.block.Block;

public final class RenderItem extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks(Tessellator.instance);
	private EaglercraftRandom random = new EaglercraftRandom();

    public final void renderItemIntoGUI(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5) {
        this.renderItemIntoGUI(var1, var2, var3, var4, var5, null);
    }

    public final void renderItemIntoGUI(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5, String s) {
        if(var3 != null) {
            int var7;
            if(var3.itemID < 256 && Block.blocksList[var3.itemID].getRenderType() == 0) {
                int var6 = var3.itemID;
                var7 = var2.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
                Block var11 = Block.blocksList[var6];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var4 - 2), (float)(var5 + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.renderBlocks.renderBlockOnInventory(var11);
                GL11.glPopMatrix();
            } else if(var3.getItem().getIconIndex() >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                if(var3.itemID < 256) {
                    var7 = var2.getTexture("/terrain.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
                } else {
                    var7 = var2.getTexture("/gui/items.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
                }

                int var10002 = var3.getItem().getIconIndex() % 16 << 4;
                int var10003 = var3.getItem().getIconIndex() / 16 << 4;
                boolean var10 = true;
                var10 = true;
                int var8 = var10003;
                var7 = var10002;
                Tessellator var9 = Tessellator.instance;
                var9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
                var9.addVertexWithUV((float)var4, (float)(var5 + 16), 0.0F, (float)var7 * 0.00390625F, (float)(var8 + 16) * 0.00390625F);
                var9.addVertexWithUV((float)(var4 + 16), (float)(var5 + 16), 0.0F, (float)(var7 + 16) * 0.00390625F, (float)(var8 + 16) * 0.00390625F);
                var9.addVertexWithUV((float)(var4 + 16), (float)var5, 0.0F, (float)(var7 + 16) * 0.00390625F, (float)var8 * 0.00390625F);
                var9.addVertexWithUV((float)var4, (float)var5, 0.0F, (float)var7 * 0.00390625F, (float)var8 * 0.00390625F);
                var9.draw();
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            if(s == null && var3.stackSize > 1) {
                s = "" + var3.stackSize;
            }

            if(s != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                var1.drawStringWithShadow(s, var4 + 19 - 2 - var1.getStringWidth(s), var5 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

        }
    }

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityItem var13 = (EntityItem)var1;
		RenderItem var12 = this;
		this.random.setSeed(187L);
		ItemStack var7 = var13.item;
		GL11.glPushMatrix();
		float var8 = MathHelper.sin(((float)var13.age + var6) / 10.0F + var13.hoverStart) * 0.1F + 0.1F;
		var6 = (((float)var13.age + var6) / 20.0F + var13.hoverStart) * (180.0F / (float)Math.PI);
		byte var9 = 1;
		if(var13.item.stackSize > 1) {
			var9 = 2;
		}

		if(var13.item.stackSize > 5) {
			var9 = 3;
		}

		if(var13.item.stackSize > 20) {
			var9 = 4;
		}

		GL11.glTranslatef(var2, var3 + var8, var4);
		GL11.glEnable(GL11.GL_NORMALIZE);
        if(var7.itemID < 256 && Block.blocksList[var7.itemID].getRenderType() == 0) {
            GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			var2 = 0.25F;
			if(!Block.blocksList[var7.itemID].renderAsNormalBlock() && var7.itemID != Block.stairSingle.blockID) {
				var2 = 0.5F;
			}

			GL11.glScalef(var2, var2, var2);

            for(int var16 = 0; var16 < var9; ++var16) {
                GL11.glPushMatrix();
                if(var16 > 0) {
					var4 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var5 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var6 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					GL11.glTranslatef(var4, var5, var6);
				}

				var12.renderBlocks.renderBlockOnInventory(Block.blocksList[var7.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
            int var14 = var7.getItem().getIconIndex();
            if(var7.itemID < 256) {
                this.loadTexture("/terrain.png");
            } else {
                this.loadTexture("/gui/items.png");
            }

            Tessellator var15 = Tessellator.instance;
            var4 = (float)(var14 % 16 << 4) / 256.0F;
            var5 = (float)((var14 % 16 << 4) + 16) / 256.0F;
            var6 = (float)(var14 / 16 << 4) / 256.0F;
            var2 = (float)((var14 / 16 << 4) + 16) / 256.0F;

            for(int var17 = 0; var17 < var9; ++var17) {
                GL11.glPushMatrix();
                if(var17 > 0) {
                    var8 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var10 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var11 = (var12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var8, var10, var11);
                }

                GL11.glRotatef(-var12.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                var15.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
                var15.normal(0.0F, 1.0F, 0.0F);
                var15.addVertexWithUV(-0.5F, -0.25F, 0.0F, var4, var2);
                var15.addVertexWithUV(0.5F, -0.25F, 0.0F, var5, var2);
                var15.addVertexWithUV(0.5F, 12.0F / 16.0F, 0.0F, var5, var6);
                var15.addVertexWithUV(-0.5F, 12.0F / 16.0F, 0.0F, var4, var6);
                var15.draw();
                GL11.glPopMatrix();
            }
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}
