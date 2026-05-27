package net.minecraft.client.render.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.world.block.tileentity.TileEntity;

public abstract class TileEntitySpecialRenderer {
    protected TileEntityRenderer tileEntityRenderer;

    public abstract void renderTileEntityMobSpawner(TileEntity tileEntity1, double d2, double d4, double d6, float f8);

    protected void bindTextureByName(String textureName) {
        RenderEngine renderEngine2 = this.tileEntityRenderer.renderEngine;
        renderEngine2.bindTexture(this.tileEntityRenderer.renderEngine.getTexture(textureName));
    }

    public void setTileEntityRenderer(TileEntityRenderer tileEntityRenderer) {
        this.tileEntityRenderer = tileEntityRenderer;
    }

    public FontRenderer getFontRenderer() {
        return this.tileEntityRenderer.getFontRenderer();
    }
}
