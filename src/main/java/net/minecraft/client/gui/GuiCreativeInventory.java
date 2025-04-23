package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.Session;
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.block.Block;

public final class GuiCreativeInventory extends GuiScreen {
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);

	public GuiCreativeInventory() {
		this.allowUserInput = true;
	}

	private int getIsMouseOverSlot(int var1, int var2) {
		for(int var3 = 0; var3 < Session.allowedBlocks.size(); ++var3) {
			int var4 = this.width / 2 + var3 % 9 * 24 + -108 - 3;
			int var5 = this.height / 2 + var3 / 9 * 24 + -60 + 3;
			if(var1 >= var4 && var1 <= var4 + 24 && var2 >= var5 - 12 && var2 <= var5 + 12) {
				return var3;
			}
		}

		return -1;
	}

	public final void drawScreen(int var1, int var2) {
		var1 = this.getIsMouseOverSlot(var1, var2);
		drawGradientRect(this.width / 2 - 120, this.height / 2 - 90, this.width / 2 + 120, this.height / 2 + 60, -1878719232, -1070583712);
		int var3;
		if(var1 >= 0) {
			var2 = this.width / 2 + var1 % 9 * 24 + -108;
			var3 = this.height / 2 + var1 / 9 * 24 + -60;
			drawGradientRect(var2 - 3, var3 - 8, var2 + 23, var3 + 24 - 6, -1862270977, -1056964609);
		}

		drawCenteredString(this.fontRenderer, "Select block", this.width / 2, this.height / 2 - 80, 16777215);
		Object var6 = null;
		var3 = this.mc.renderEngine.getTexture("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		for(var2 = 0; var2 < Session.allowedBlocks.size(); ++var2) {
			Block var7 = (Block)Session.allowedBlocks.get(var2);
			GL11.glPushMatrix();
			int var4 = this.width / 2 + var2 % 9 * 24 + -108;
			int var5 = this.height / 2 + var2 / 9 * 24 + -60;
			GL11.glTranslatef((float)var4, (float)var5, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			if(var1 == var2) {
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			this.blockRenderer.renderBlockOnInventory(var7);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
	}

	protected final void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0) {
			InventoryPlayer var10000 = this.mc.thePlayer.inventory;
			var2 = this.getIsMouseOverSlot(var1, var2);
			InventoryPlayer var4 = var10000;
			if(var2 >= 0) {
				var4.replaceSlot((Block)Session.allowedBlocks.get(var2));
			}

			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}
}
