package com.mojang.minecraft.gui;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class InventoryScreen extends Screen {
	public InventoryScreen() {
		this.allowUserInput = true;
	}

	private int getTileAtSlot(int i1, int i2) {
		for(int i3 = 0; i3 < User.creativeTiles.size(); ++i3) {
			int i4 = this.width / 2 + i3 % 8 * 24 - 96 - 3;
			int i5 = this.height / 2 + i3 / 8 * 24 - 48 + 3;
			if(i1 >= i4 && i1 <= i4 + 24 && i2 >= i5 - 12 && i2 <= i5 + 12) {
				return i3;
			}
		}

		return -1;
	}

	public final void render(int i1, int i2) {
		i1 = this.getTileAtSlot(i1, i2);
		fillGradient(this.width / 2 - 120, this.height / 2 - 90, this.width / 2 + 120, this.height / 2 + 60, -1878719232, -1070583712);
		if(i1 >= 0) {
			i2 = this.width / 2 + i1 % 8 * 24 - 96;
			int i3 = this.height / 2 + i1 / 8 * 24 - 48;
			fillGradient(i2 - 3, i3 - 8, i2 + 23, i3 + 24 - 6, -1862270977, -1056964609);
		}

		drawCenteredString(this.font, "Select block", this.width / 2, this.height / 2 - 80, 0xFFFFFF);
		Textures textures7 = this.minecraft.textures;
		Tesselator tesselator8 = Tesselator.instance;
		i2 = textures7.getTextureId("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		for(i2 = 0; i2 < User.creativeTiles.size(); ++i2) {
			Tile tile4 = (Tile)User.creativeTiles.get(i2);
			GL11.glPushMatrix();
			int i5 = this.width / 2 + i2 % 8 * 24 - 96;
			int i6 = this.height / 2 + i2 / 8 * 24 - 48;
			GL11.glTranslatef((float)i5, (float)i6, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			if(i1 == i2) {
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			tesselator8.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
			tile4.render(tesselator8, this.minecraft.level, 0, -2, 0, 0);
			tesselator8.end();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	protected final void mousePressed(int i1, int i2, int i3) {
		if(i3 == 0) {
			Inventory inventory10000 = this.minecraft.player.inventory;
			i2 = this.getTileAtSlot(i1, i2);
			Inventory inventory4 = inventory10000;
			if(i2 >= 0) {
				inventory4.setTile((Tile)User.creativeTiles.get(i2));
			}

			this.minecraft.setScreen((Screen)null);
		}

	}
}
