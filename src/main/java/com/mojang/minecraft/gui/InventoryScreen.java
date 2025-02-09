package com.mojang.minecraft.gui;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;

public final class InventoryScreen extends Screen {
    private int getTileAtSlot(int i1, int i2) {
        for(int i3 = 0; i3 < User.creativeTiles.size(); ++i3) {
            int i4 = this.width / 2 + i3 % 8 * 24 - 96;
            int i5 = this.height / 2 + i3 / 8 * 24 - 48;
            if(i1 >= i4 && i1 <= i4 + 24 && i2 >= i5 - 12 && i2 <= i5 + 12) {
                return i3;
            }
        }

        return -1;
    }

    public final void render(int i1, int i2) {
        fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
        this.drawCenteredString("Select block", this.width / 2, 40, 0xFFFFFF);
        Textures textures3 = this.minecraft.textures;
        Tesselator tesselator4 = Tesselator.instance;
        i1 = this.getTileAtSlot(i1, i2);
        i2 = textures3.getTextureId("/terrain.png");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        for(i2 = 0; i2 < User.creativeTiles.size(); ++i2) {
            Tile tile7 = (Tile)User.creativeTiles.get(i2);
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
            tesselator4.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            tile7.render(tesselator4, this.minecraft.level, 0, -2, 0, 0);
            tesselator4.end();
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
                inventory4.getSlotContainsTile((Tile)User.creativeTiles.get(i2));
            }

            this.minecraft.setScreen((Screen)null);
        }

    }
}
