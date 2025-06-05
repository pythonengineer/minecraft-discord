package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class RenderItem extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);
	private EaglercraftRandom rand = new EaglercraftRandom();

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		EntityItem var13 = (EntityItem)var1;
		RenderItem var12 = this;
		this.rand.setSeed(187L);
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
		int var15;
		if(var7.itemID < 256) {
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			var2 = 0.25F;
			if(!Block.blocksList[var7.itemID].renderAsNormalBlock() && var7.itemID != Block.stairSingle.blockID) {
				var2 = 0.5F;
			}

			GL11.glScalef(var2, var2, var2);

			for(var15 = 0; var15 < var9; ++var15) {
				GL11.glPushMatrix();
				if(var15 > 0) {
					var4 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var5 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					var6 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / var2;
					GL11.glTranslatef(var4, var5, var6);
				}

				var12.blockRenderer.renderBlockOnInventory(Block.blocksList[var7.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			this.loadTexture("/gui/items.png");
			Tessellator var14 = Tessellator.instance;
			var15 = var7.getItem().getIconIndex();
			var4 = (float)(var15 % 16 << 4) / 256.0F;
			var5 = (float)((var15 % 16 << 4) + 16) / 256.0F;
			var6 = (float)(var15 / 16 << 4) / 256.0F;
			var3 = (float)((var15 / 16 << 4) + 16) / 256.0F;

			for(int var16 = 0; var16 < var9; ++var16) {
				GL11.glPushMatrix();
				if(var16 > 0) {
					var8 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var10 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var11 = (var12.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(var8, var10, var11);
				}

				GL11.glRotatef(-var12.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				var14.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_NORMAL);
				var14.normal(0.0F, 1.0F, 0.0F);
				var14.addVertexWithUV(-0.5F, -0.25F, 0.0F, var4, var3);
				var14.addVertexWithUV(0.5F, -0.25F, 0.0F, var5, var3);
				var14.addVertexWithUV(0.5F, 12.0F / 16.0F, 0.0F, var5, var6);
				var14.addVertexWithUV(-0.5F, 12.0F / 16.0F, 0.0F, var4, var6);
				var14.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}
