package net.minecraft.client.render.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.world.block.tileentity.TileEntity;

public abstract class TileEntitySpecialRenderer {
    private TileEntityRenderer tileEntityRenderer;

    public abstract void renderTileEntityMobSpawner(TileEntity tileEntity1, double d2, double d4, double d6, float f8);

    protected final void bindTextureByName(String textureName) {
        RenderEngine renderEngine2 = this.tileEntityRenderer.renderEngine;
        RenderEngine.bindTexture(this.tileEntityRenderer.renderEngine.getTexture(textureName));
    }

    public final void setTileEntityRenderer(TileEntityRenderer tileEntityRenderer) {
        this.tileEntityRenderer = tileEntityRenderer;
    }

    public final FontRenderer getFontRenderer() {
        return this.tileEntityRenderer.getFontRenderer();
    }
}