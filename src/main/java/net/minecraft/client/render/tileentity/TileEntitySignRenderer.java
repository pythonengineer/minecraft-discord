package net.minecraft.client.render.tileentity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.SignModel;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntitySign;

public final class TileEntitySignRenderer extends TileEntitySpecialRenderer {
    private SignModel modelSign = new SignModel();

    public final void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileEntitySign tileEntitySign18 = (TileEntitySign)tileEntity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
        GL11.glRotatef(-((float)(tileEntitySign18.getBlockMetadata() * 360) / 16.0F), 0.0F, 1.0F, 0.0F);
        this.bindTextureByName("/item/sign.png");
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        SignModel signModel3 = this.modelSign;
        this.modelSign.signBoard.render(0.0625F);
        signModel3.signStick.render(0.0625F);
        GL11.glPopMatrix();
        GL11.glTranslatef(0.0F, 0.5F, 0.09F);
        GL11.glScalef(0.016666668F, -0.016666668F, 0.016666668F);
        GL11.glNormal3f(0.0F, 0.0F, -0.016666668F);
        FontRenderer fontRenderer17 = this.getFontRenderer();

        for(int i19 = 0; i19 < tileEntitySign18.signText.length; ++i19) {
            String string20 = tileEntitySign18.signText[i19];
            if(i19 == tileEntitySign18.lineBeingEdited) {
                string20 = "> " + string20 + " <";
                fontRenderer17.drawString(string20, -fontRenderer17.width(string20) / 2, i19 * 10 - tileEntitySign18.signText.length * 5, 0);
            } else {
                fontRenderer17.drawString(string20, -fontRenderer17.width(string20) / 2, i19 * 10 - tileEntitySign18.signText.length * 5, 0);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}